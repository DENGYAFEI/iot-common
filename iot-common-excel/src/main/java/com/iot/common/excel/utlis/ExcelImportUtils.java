package com.iot.common.excel.utlis;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.iot.common.base.utils.JsonUtil;
import com.iot.common.excel.model.annotation.ExcelImport;
import com.iot.common.excel.model.exception.ImportException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @Author Orchid
 * @Create 2023/10/20
 * @Remark Excel导入导出工具类（简化版本）
 */
public class ExcelImportUtils {

    private static final Logger logger = LoggerFactory.getLogger( ExcelImportUtils.class );

    private static final String SUFFIX_XLS = ".xls";
    private static final String SUFFIX_XLSX = ".xlsx";
    private static final String FIELD_ROW_NUM = "rowNum";
    private static final String FIELD_ROW_DATA = "rowData";

    /**
     * 读取上传文件的信息
     */
    public static < T > List< T > read( MultipartFile mFile, Class< T > clazz ) throws Exception {
        return getBeanList( readExcel( mFile, null ), clazz );
    }

    /**
     * 读取文件的信息
     */
    public static < T > List< T > read( File file, Class< T > clazz ) throws Exception {
        return getBeanList( readExcel( null, file ), clazz );
    }

    /**
     * 将JSONArray转为指定实体
     */
    private static < T > List< T > getBeanList( JSONArray array, Class< T > clazz ) throws Exception {
        synchronized ( ExcelImportUtils.class ) {
            List< T > list = new ArrayList<>();
            //  唯一性校验
            Map< String, Integer > uniqueMap = new HashMap<>();
            //  联合唯一性校验
            Map< String, Integer > unionUniqueMap = new HashMap<>();
            logger.debug( "正在将{}条数据转化为{}", array.size(), clazz.getName() );
            Field[] fields = clazz.getDeclaredFields();
            Map< String, String > transMap = new ConcurrentHashMap<>();
            for ( int i = 0; i < array.size(); i++ ) {
                T instance = clazz.newInstance();
                JSONObject jsonObject = array.getJSONObject( i );
                //  缓存联合唯一性字段
                Map< String, String > unionUniqueFieldMap = new HashMap<>();
                Integer rowNum = jsonObject.getInteger( FIELD_ROW_NUM );
                for ( Field field : fields ) {
                    field.setAccessible( true );
                    // 行号
                    if ( field.getName().equals( FIELD_ROW_NUM ) ) {
                        field.set( instance, rowNum );
                        continue;
                    }
                    // 原始数据
                    if ( field.getName().equals( FIELD_ROW_DATA ) ) {
                        field.set( instance, jsonObject.toString() );
                        continue;
                    }
                    // 获取 Trans 翻译属性
                    // Trans transAnnotation = field.getAnnotation( Trans.class );
                    // if ( ObjectUtil.isNotNull( transAnnotation ) ) {
                    //     ExcelImport annotation = field.getAnnotation( ExcelImport.class );
                    //     if ( ObjectUtil.isNotNull( annotation ) ) {
                    //         String title = annotation.value();
                    //         if ( !title.trim().isEmpty() ) {
                    //             String key = ExcelExportUtils.getString( jsonObject.getString( annotation.value() ) );
                    //             if ( StrUtil.isNotBlank( key ) ) {
                    //                 List< String > values = new ArrayList<>();
                    //                 String[] split = key.replace( "，", "," ).split( "," );
                    //                 for ( String k : split ) {
                    //                     if ( transMap.containsKey( k ) ) {
                    //                         values.add( transMap.get( k ) );
                    //                     }
                    //                 }
                    //                 jsonObject.put( title, StrUtil.join( ",", values ) );
                    //             }
                    //         }
                    //     }
                    // }
                    setBeanFieldValue( instance, field, jsonObject, uniqueMap, unionUniqueFieldMap );
                }
                // 联合数据唯一性校验
                if ( unionUniqueFieldMap.size() > 0 ) {
                    StringJoiner joiner = new StringJoiner( "," );
                    for ( Map.Entry< String, String > entry : unionUniqueFieldMap.entrySet() ) {
                        joiner.add( entry.getKey() + "-" + entry.getValue() );
                    }
                    String key = joiner.toString();
                    if ( unionUniqueMap.containsKey( key ) ) {
                        StringBuilder builder = new StringBuilder();
                        unionUniqueFieldMap.keySet().forEach( fieldName -> builder.append( builder.length() == 0 ? "" : "," ).append( fieldName ) );
                        throw ImportException.uniqueException( String.format( "第%s行与第%s行的[%s]数据重复", unionUniqueMap.get( key ), rowNum, builder ) );
                    }
                    unionUniqueMap.put( key, rowNum );
                }
                list.add( instance );
            }
            logger.debug( "转换结果: {}", JsonUtil.toJsonString( list ) );
            logger.debug( "==================================================" );
            return list;
        }
    }

    /**
     * 将JSONObject赋值给对应实体
     */
    private static < T > void setBeanFieldValue( T instance, Field field, JSONObject obj, Map< String, Integer > uniqueMap, Map< String, String > unionUniqueMap ) {
        // 获取 ExcelImport 注解属性
        ExcelImport annotation = field.getAnnotation( ExcelImport.class );
        if ( ObjectUtil.isNull( annotation ) ) {
            return;
        }
        String title = annotation.value();
        if ( title.trim().isEmpty() ) {
            return;
        }
        // 获取具体值
        String val = null;
        if ( obj.containsKey( title ) ) {
            val = handleScienceNotation( ExcelExportUtils.getString( obj.getString( title ) ) );// 处理科学计数法
        }
        if ( ObjectUtil.isNull( val ) ) {
            return;
        }

        field.setAccessible( true );
        Integer rowNum = obj.getInteger( FIELD_ROW_NUM );
        // 判断是否必填
        boolean require = annotation.required();
        if ( val.isEmpty() ) {
            if ( require ) {
                throw ImportException.required( String.format( "[%s]：不能为空", title ) );
            }
            return;
        }
        // 数据唯一性获取
        if ( annotation.unique() ) {
            // 数据唯一性校验
            String key = field.getName() + "-" + val;
            if ( uniqueMap.containsKey( key ) ) {
                throw ImportException.uniqueException( String.format( "第%s行与第%s行的%s重复", uniqueMap.get( key ), rowNum, title ) );
            }
            uniqueMap.put( key, rowNum );
        }
        // 联合数据唯一性获取
        if ( annotation.unionUnique() ) {
            unionUniqueMap.put( field.getName(), val );
        }
        // 判断是否超过最大长度
        int maxLength = annotation.maxLength();
        if ( maxLength > 0 && val.length() > maxLength ) {
            throw ImportException.lengthException( String.format( "[%s]：[%s]长度不能超过%s个字符或%s个汉字", title, val, maxLength, maxLength / 2 ) );
        }
        // 判断当前属性是否有映射关系
        LinkedHashMap< String, String > kvMap = getKvMap( annotation.kv() );
        if ( !kvMap.isEmpty() ) {
            boolean isMatch = false;
            for ( String key : kvMap.keySet() ) {
                if ( kvMap.get( key ).equals( val ) ) {
                    val = key;
                    isMatch = true;
                    break;
                }
            }
            if ( !isMatch ) {
                throw ImportException.matchException( String.format( "[%s]：[%s]未正确匹配", title, val ) );
            }
        }
        // 设置对应属性值
        setFieldValue( instance, field, title, val );
    }

    /**
     * 给实体类赋值
     */
    private static < T > void setFieldValue( T t, Field field, String cname, String val ) {
        // 其余情况根据类型赋值
        String fieldClassName = field.getType().getSimpleName();
        try {
            if ( "String".equalsIgnoreCase( fieldClassName ) ) {
                field.set( t, val );
            } else if ( "boolean".equalsIgnoreCase( fieldClassName ) ) {
                field.set( t, Boolean.valueOf( val ) );
            } else if ( "int".equalsIgnoreCase( fieldClassName ) || "Integer".equals( fieldClassName ) ) {
                field.set( t, Integer.valueOf( val ) );
            } else if ( "double".equalsIgnoreCase( fieldClassName ) ) {
                field.set( t, Double.valueOf( val ) );
            } else if ( "long".equalsIgnoreCase( fieldClassName ) && StrUtil.isNotBlank( val ) ) {
                field.set( t, Long.valueOf( val ) );
            } else if ( "BigDecimal".equalsIgnoreCase( fieldClassName ) ) {
                field.set( t, new BigDecimal( val ) );
            } else if ( "Date".equalsIgnoreCase( fieldClassName ) ) {
                Calendar calendar = new GregorianCalendar( 1900, Calendar.JANUARY, -1 );
                Date date = ExcelExportUtils.isNumeric( val ) ? new Date( calendar.getTime().getTime() + Long.parseLong( val ) * 24 * 60 * 60 * 1000 ) : DateUtil.parse( val );
                field.set( t, date );
            } else if ( "LocalDate".equalsIgnoreCase( fieldClassName ) ) {
                Calendar calendar = new GregorianCalendar( 1900, Calendar.JANUARY, -1 );
                Date date = ExcelExportUtils.isNumeric( val ) ? new Date( calendar.getTime().getTime() + Long.parseLong( val ) * 24 * 60 * 60 * 1000 ) : DateUtil.parse( val );
                field.set( t, LocalDate.parse( DateUtil.format( date, DatePattern.NORM_DATE_PATTERN ) ) );
            } else if ( "LocalDateTime".equalsIgnoreCase( fieldClassName ) ) {
                field.set( t, DateUtil.parseLocalDateTime( val ) );
            }
        } catch ( Exception e ) {
            throw ImportException.formatException( String.format( "[%s]的值格式不正确(当前值为%s)", cname, val ) );
        }
    }

    /**
     * 读取Excel中的信息 优先读取上传文件的
     */
    private static JSONArray readExcel( MultipartFile mFile, File file ) throws IOException {
        synchronized ( ExcelImportUtils.class ) {
            String fileName = checkFileType( mFile, file );
            try ( Workbook book = getWorkbook( mFile, file ) ) {
                logger.debug( "==================================================" );
                logger.debug( "开始解析解析excel: [{}]", fileName );
                JSONArray result = ObjectUtil.isNull( book ) ? new JSONArray() : readSheet( book.getSheetAt( 0 ) );
                logger.debug( "excel解析完毕 数据如下" );
                logger.debug( result.toJSONString() );
                logger.debug( "==================================================" );
                return result;
            }
        }
    }

    /**
     * 从文件中读取Excel
     */
    private static Workbook getWorkbook( MultipartFile mFile, File file ) throws IOException {
        if ( mFile == null && ( file == null || !file.exists() ) ) {
            return null;
        }
        // 解析表格数据
        InputStream in = null;
        String fileName;
        try {
            if ( mFile != null ) {
                // 上传文件解析
                in = mFile.getInputStream();
                fileName = ExcelExportUtils.getString( mFile.getOriginalFilename() ).toLowerCase();
            } else {
                // 本地文件解析
                in = Files.newInputStream( file.toPath() );
                fileName = file.getName().toLowerCase();
            }
            logger.info( "正在从【{}】中读取数据", fileName );
            Workbook book;
            if ( fileName.endsWith( SUFFIX_XLSX ) ) {
                book = new XSSFWorkbook( in );
            } else if ( fileName.endsWith( SUFFIX_XLS ) ) {
                POIFSFileSystem poifsFileSystem = new POIFSFileSystem( in );
                book = new HSSFWorkbook( poifsFileSystem );
            } else {
                return null;
            }
            return book;
        } finally {
            if ( Objects.nonNull( in ) ) {
                in.close();
            }
        }
    }

    /**
     * 读取Excel的Sheet页数据
     */
    private static JSONArray readSheet( Sheet sheet ) {
        // 首行下标
        int rowStart = sheet.getFirstRowNum();
        // 尾行下标
        int rowEnd = sheet.getLastRowNum();
        // 获取表头行
        Row headRow = sheet.getRow( rowStart );
        if ( headRow == null ) {
            return new JSONArray();
        }
        int cellStart = headRow.getFirstCellNum();
        int cellEnd = headRow.getLastCellNum();
        Map< Integer, String > keyMap = new HashMap<>();
        for ( int j = cellStart; j < cellEnd; j++ ) {
            // 获取表头数据
            String titleName = getCellValue( headRow.getCell( j ) );
            if ( titleName != null && !titleName.trim().isEmpty() ) {
                //  不允许表头重复
                if ( keyMap.containsValue( titleName ) ) {
                    throw ImportException.uniqueException( "表头：" + titleName + "存在多个" );
                }
                keyMap.put( j, titleName );
            }
        }
        // 如果表头没有数据则不进行解析
        if ( keyMap.isEmpty() ) {
            return ( JSONArray ) Collections.emptyList();
        }
        // 获取每行JSON对象的值
        JSONArray array = new JSONArray();
        // 如果首行与尾行相同，表明只有一行，返回表头数据
        if ( rowStart == rowEnd ) {
            //JSONObject obj = new JSONObject();
            //// 添加行号
            //obj.put( FIELD_ROW_NUM, 1 );
            //for ( int i : keyMap.keySet() ) {
            //    obj.put( keyMap.get( i ), "" );
            //}
            //array.add( obj );
            return array;
        }
        for ( int i = rowStart + 1; i <= rowEnd; i++ ) {
            Row eachRow = sheet.getRow( i );
            JSONObject obj = new JSONObject();
            // 添加行号
            obj.put( FIELD_ROW_NUM, i + 1 );
            StringBuilder sb = new StringBuilder();
            for ( int k = cellStart; k < cellEnd; k++ ) {
                if ( eachRow != null ) {
                    String val = getCellValue( eachRow.getCell( k ) );
                    // 所有数据添加到里面，用于判断该行是否为空
                    sb.append( val );
                    obj.put( keyMap.get( k ), val );
                }
            }
            if ( sb.length() > 0 ) {
                array.add( obj );
            }
        }
        return array;
    }

    /**
     * 获取单元格数据
     */
    private static String getCellValue( Cell cell ) {
        // 空白或空
        if ( cell == null || cell.getCellTypeEnum() == CellType.BLANK ) {
            return "";
        }
        // String类型
        if ( cell.getCellTypeEnum() == CellType.STRING ) {
            String val = cell.getStringCellValue();
            if ( val == null || val.trim().isEmpty() ) {
                return "";
            }
            return val.trim();
        }
        // 数字类型
        if ( cell.getCellTypeEnum() == CellType.NUMERIC ) {
            String s = cell.getNumericCellValue() + "";
            // 去掉尾巴上的小数点0
            if ( Pattern.matches( ".*\\.0*", s ) ) {
                return s.split( "\\." )[0];
            } else {
                return s;
            }
        }
        // 布尔值类型
        if ( cell.getCellTypeEnum() == CellType.BOOLEAN ) {
            return cell.getBooleanCellValue() + "";
        }
        // 错误类型
        return cell.getCellFormula();
    }

    /**
     * 将注解的kv转化为映射关系
     */
    private static LinkedHashMap< String, String > getKvMap( String kv ) {
        LinkedHashMap< String, String > kvMap = new LinkedHashMap<>();
        if ( kv.isEmpty() ) {
            return kvMap;
        }
        String[] kvs = kv.split( ";" );
        for ( String each : kvs ) {
            String[] eachKv = ExcelExportUtils.getString( each ).split( "-" );
            if ( eachKv.length != 2 ) {
                continue;
            }
            String k = eachKv[0];
            String v = eachKv[1];
            if ( k.isEmpty() || v.isEmpty() ) {
                continue;
            }
            kvMap.put( k, v );
        }
        return kvMap;
    }

    /**
     * 处理科学计数法
     */
    private static String handleScienceNotation( String val ) {
        int index = val.indexOf( "E" );
        if ( index > 0 ) {
            String before = val.substring( 0, index );
            String after = val.substring( index + 1 );
            if ( ExcelExportUtils.isNumeric( before ) && ExcelExportUtils.isNumeric( after ) ) {
                int bit = ( before.length() - before.indexOf( "." ) - 1 ) - Integer.parseInt( after );
                BigDecimal bigDecimal = BigDecimal.valueOf( Double.parseDouble( before ) * Math.pow( 10, Double.parseDouble( after ) ) );
                if ( bit >= 0 ) {
                    return bigDecimal.setScale( bit, RoundingMode.HALF_UP ).toPlainString();
                }
                return bigDecimal.toPlainString();
            }
        }
        return val;
    }

    private static String checkFileType( MultipartFile mFile, File file ) {
        boolean isMFile = ObjectUtil.isNotNull( mFile );
        Assert.isTrue( isMFile || ObjectUtil.isNotNull( file ), "文件不能为空" );
        String suffix = isMFile ? FileUtil.getSuffix( mFile.getOriginalFilename() ) : FileUtil.getSuffix( file );
        String fileName = isMFile ? mFile.getOriginalFilename() : FileUtil.getName( file );
        Assert.isTrue( SUFFIX_XLS.substring( 1 ).equals( suffix ) || SUFFIX_XLSX.substring( 1 ).equals( suffix ), "文件格式错误" );
        return fileName;
    }

}
