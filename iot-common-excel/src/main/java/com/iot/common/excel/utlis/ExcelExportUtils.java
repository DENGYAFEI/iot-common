package com.iot.common.excel.utlis;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.iot.common.excel.model.pojo.dto.Header;
import com.iot.common.excel.model.ExcelDefinition;
import com.iot.common.excel.model.ExcelField;
import com.iot.common.excel.model.ExcelStyleDefinition;
import com.iot.common.excel.model.annotation.ExcelExport;
import com.iot.common.excel.model.annotation.ExcelHeaderStyle;
import com.iot.common.excel.model.annotation.ExcelRowStyle;
import com.iot.common.excel.model.annotation.ExcelStyle;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.data.repository.init.ResourceReader;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Remark Excel导出工具类
 */
public class ExcelExportUtils {
    private static final String TEMPLATE_PATH = "/template/";
    /**
     * Cell_Column
     */
    private static final int CELL_OTHER = 0;
    private static final int CELL_ROW_MERGE = 1;
    private static final int CELL_COLUMN_MERGE = 2;
    /**
     * Cell_Merger
     */
    private static final String ROW_MERGE = "row_merge";
    private static final String COLUMN_MERGE = "column_merge";

    public static < T > void export( String fileName, List< T > list, Class< ? > clazz ) {
        export( fileName, list, clazz, true );
    }

    public static < T > void export( String fileName, List< T > list, Class< ? > clazz, boolean distinct ) {
        IoUtil.write( getResponseOutStream( fileName ), true, getExcelBytes( fileName, list, clazz, distinct, null ) );
    }

    public static < T > void exportToHeaders( String fileName, List< T > list, Class< ? > clazz, List< Header > headers ) {
        IoUtil.write( getResponseOutStream( fileName ), true, getExcelBytes( fileName, list, clazz, true, headers ) );
    }

    /**
     * 通过对象模板导出Excel
     */
    private static < T > byte[] getExcelBytes( String fileName, List< T > list, Class< ? > clazz ) {
        return getExcelBytes( fileName, list, clazz, true, null );
    }

    /**
     * 通过对象模板导出Excel
     */
    private static < T > byte[] getExcelBytes( String fileName, List< T > list, Class< ? > clazz, boolean distinct, List< Header > headers ) {
        // 如果 list 为空，则导出模板数据
        if ( CollectionUtil.isEmpty( list ) ) {
            return getTemplateBytes( fileName, clazz, true, distinct, headers );
        }
        clazz = list.get( 0 ).getClass();
        // 获取表头字段
        List< ExcelField > excelFields = getExcelFields( clazz, distinct, headers );
        ExcelStyleDefinition styleDefinition = new ExcelStyleDefinition();
        styleDefinition.setExcelStyle( clazz.getAnnotation( ExcelStyle.class ) );
        List< Object > headNames = new ArrayList<>();
        for ( int i = 0; i < excelFields.size(); i++ ) {
            ExcelField each = excelFields.get( i );
            styleDefinition.getHeaderStyleMap().put( i, each.getExcelHeaderStyle() );
            styleDefinition.getRowStyleMap().put( i, each.getExcelRowStyle() );
            headNames.add( each.getName() );
        }
        List< List< Object > > sheetData = getSheetData( excelFields, list );
        sheetData.add( 0, headNames );

        // 导出数据
        ExcelDefinition excelDefinition = new ExcelDefinition();
        excelDefinition.setStyleDefinition( styleDefinition );

        Map< String, List< List< Object > > > sheetMap = new HashMap<>();
        sheetMap.put( fileName, sheetData );
        excelDefinition.setSheetMap( sheetMap );
        excelDefinition.setFileName( fileName );
        return exportExcel( excelDefinition );
    }

    /**
     * 导出文件模板
     */
    public static void exportTemplate( String fileName ) {
        IoUtil.write( getResponseOutStream( fileName ), true, getTemplateBytes( fileName ) );
    }

    private static byte[] getTemplateBytes( String fileName ) {
        InputStream inputStream = ResourceReader.class.getResourceAsStream( TEMPLATE_PATH + fileName );
        Assert.notNull( inputStream, "读取模板文件失败" );
        return IoUtil.readBytes( inputStream );
    }

    /**
     * 导出Class模板
     */
    private static < T > void exportTemplate( String fileName, Class< T > clazz, boolean isContainExample ) {
        exportTemplate( fileName, clazz, isContainExample, true );
    }

    /**
     * 导出Class模板
     */
    public static < T > void exportTemplate( String fileName, Class< T > clazz, boolean isContainExample, boolean distinct ) {
        IoUtil.write( getResponseOutStream( fileName ), true, getTemplateBytes( fileName, clazz, isContainExample, distinct, null ) );
    }

    private static < T > byte[] getTemplateBytes( String fileName, Class< T > clazz, boolean isContainExample, boolean distinct, List< Header > headers ) {
        // 获取表头字段
        List< ExcelField > headFields = getExcelFields( clazz, distinct, headers );
        ExcelStyleDefinition styleDefinition = new ExcelStyleDefinition();
        styleDefinition.setExcelStyle( clazz.getAnnotation( ExcelStyle.class ) );
        // 获取表头数据和示例数据
        List< List< Object > > sheetData = new ArrayList<>();
        List< Object > headNames = new ArrayList<>();
        List< Object > exampleList = new ArrayList<>();
        Map< Integer, List< String > > selectMap = new LinkedHashMap<>();
        for ( int i = 0; i < headFields.size(); i++ ) {
            ExcelField each = headFields.get( i );
            styleDefinition.getHeaderStyleMap().put( i, each.getExcelHeaderStyle() );
            styleDefinition.getRowStyleMap().put( i, each.getExcelRowStyle() );
            headNames.add( each.getName() );
            exampleList.add( each.getExample() );
            LinkedHashMap< String, String > kvMap = each.getKvMap();
            if ( kvMap != null && !kvMap.isEmpty() ) {
                selectMap.put( i, new ArrayList<>( kvMap.values() ) );
            }
        }
        sheetData.add( headNames );
        if ( isContainExample ) {
            sheetData.add( exampleList );
        }
        // 导出数据
        Map< String, List< List< Object > > > map = new HashMap<>();
        map.put( fileName, sheetData );
        ExcelDefinition excelDefinition = new ExcelDefinition();
        excelDefinition.setSheetMap( map );
        excelDefinition.setSelectMap( selectMap );
        excelDefinition.setStyleDefinition( styleDefinition );
        excelDefinition.setFileName( fileName );
        return exportExcel( excelDefinition );
    }

    /**
     * 将List写入Excel并返回给response
     */
    private static byte[] exportExcel( ExcelDefinition excelDefinition ) {
        // 整个 Excel 表格 book 对象
        SXSSFWorkbook book = new SXSSFWorkbook();
        // 每个 Sheet 页
        Set< Map.Entry< String, List< List< Object > > > > entries = excelDefinition.getSheetMap().entrySet();
        for ( Map.Entry< String, List< List< Object > > > entry : entries ) {
            List< List< Object > > sheetDataList = entry.getValue();
            SXSSFSheet sheet = book.createSheet( entry.getKey() );
            ExcelStyleDefinition styleDefinition = excelDefinition.getStyleDefinition();
            renderSheetStyle( book, sheet, styleDefinition.getExcelStyle() );
            // 创建合并算法数组
            int rowLength = sheetDataList.size();
            int columnLength = sheetDataList.get( 0 ).size();
            int[][] mergeArray = new int[rowLength][columnLength];
            ExcelStyle excelStyle = styleDefinition.getExcelStyle();
            Map< Integer, CellStyle > cellStyleMap = new HashMap<>();
            boolean init = true;
            for ( int i = 0; i < sheetDataList.size(); i++ ) {
                // 每个 Sheet 页中的行数据
                Row row = sheet.createRow( i );
                List< Object > rowList = sheetDataList.get( i );
                for ( int j = 0; j < rowList.size(); j++ ) {
                    // 每个行数据中的单元格数据
                    Object o = rowList.get( j );
                    int v = 0;
                    Cell cell = row.createCell( j );
                    if ( i == 0 ) {
                        // 第一行为表头行，采用灰色底背景
                        ExcelHeaderStyle headerStyle = styleDefinition.getHeaderStyleMap().get( j );
                        v = setCellValue( cell, o, renderRowStyle( book, excelStyle, headerStyle ) );
                        int width = 15;
                        if ( ObjectUtil.isNotNull( headerStyle ) ) {
                            width = headerStyle.width() == 0 ? ( ObjectUtil.isNotNull( excelStyle ) ? excelStyle.width() : width ) : headerStyle.width();
                        } else if ( ObjectUtil.isNotNull( excelStyle ) ) {
                            width = excelStyle.width();
                        }
                        sheet.setColumnWidth( j, width * 256 );
                    } else {
                        if ( init && !cellStyleMap.containsKey( j ) ) {
                            cellStyleMap.put( j, renderRowStyle( book, excelStyle, styleDefinition.getRowStyleMap().get( j ) ) );
                        }
                        v = setCellValue( cell, o, cellStyleMap.get( j ) );
                        // v = setCellValue( cell, o, renderRowStyle( book, excelStyle, excelDefinition.getStyleDefinition().getRowStyleMap().get( j ) ) );
                    }
                    mergeArray[i][j] = v;
                }
                //  执行过一行非表头的渲染了 下一行不再进行style的判断
                if ( i > 0 ) {
                    init = false;
                }
            }
            // 合并单元格
            mergeCells( sheet, mergeArray );
            // 设置下拉列表
            // setSelect( sheet, selectMap );
        }
        // 写数据
        return getBytes( book );
    }

    /**
     * 获取类上的@ExcelExport配置信息
     */
    private static < T > List< ExcelField > getExcelFields( Class< T > clazz, boolean distinct, List< Header > headers ) {
        Map< String, Header > headerMap = headers == null ? null : headers.stream().filter( header -> StrUtil.isNotBlank( header.getColumn() ) ).collect( Collectors.toMap( Header::getColumn, Function.identity() ) );
        Set< String > fieldNames = new HashSet<>();
        // 解析所有字段
        Class< ? > tempClazz = clazz;
        List< Field > fieldList = new ArrayList<>();
        while ( tempClazz != Object.class ) {
            Field[] fields = tempClazz.getDeclaredFields();
            for ( Field field : fields ) {
                String fieldName = field.getName();
                //  去重
                if ( distinct && fieldNames.contains( fieldName ) ) {
                    continue;
                }
                //  headers逻辑处理
                if ( headerMap != null ) {
                    //  如果headers中没指定字段 则不处理
                    if ( !headerMap.containsKey( fieldName ) ) {
                        continue;
                    }
                    Header header = headerMap.get( fieldName );
                    if ( header.getIsShow() == null || !header.getIsShow() ) {
                        continue;
                    }
                }
                fieldList.add( field );
                fieldNames.add( fieldName );
            }
            tempClazz = tempClazz.getSuperclass();
        }
        boolean hasExportAnnotation = false;
        Map< Integer, List< ExcelField > > map = new LinkedHashMap<>();
        List< Integer > sortList = new ArrayList<>();
        for ( Field field : fieldList ) {
            String fieldName = field.getName();
            ExcelField cf = getExcelClassField( field );
            if ( cf.isHasAnnotation() ) {
                hasExportAnnotation = true;
            }
            int sort = 0;
            if ( headerMap == null || !headerMap.containsKey( fieldName ) ) {
                sort = cf.getSort();
            } else {
                Header header = headerMap.get( fieldName );
                sort = header.getOrder();
                cf.setSort( sort );
            }
            if ( map.containsKey( sort ) ) {
                map.get( sort ).add( cf );
            } else {
                List< ExcelField > list = new ArrayList<>();
                list.add( cf );
                sortList.add( sort );
                map.put( sort, list );
            }
        }
        Collections.sort( sortList );
        // 获取表头
        List< ExcelField > headFieldList = new ArrayList<>();
        if ( hasExportAnnotation ) {
            for ( Integer sort : sortList ) {
                for ( ExcelField cf : map.get( sort ) ) {
                    if ( cf.isHasAnnotation() ) {
                        headFieldList.add( cf );
                    }
                }
            }
        } else {
            headFieldList.addAll( map.get( 0 ) );
        }
        return headFieldList;
    }

    /**
     * 获取字段上的@ExcelExport配置信息
     */
    private static ExcelField getExcelClassField( Field field ) {
        ExcelField cf = new ExcelField();
        String fieldName = field.getName();
        cf.setFieldName( fieldName );
        cf.setFieldType( field.getType() );
        //  获取翻译信息
        // Trans transAnnotation = field.getAnnotation( Trans.class );
        // if (  ObjectUtil.isNotNull( transAnnotation ) ) {
        //         cf.setTrans( true );
        // }
        ExcelExport annotation = field.getAnnotation( ExcelExport.class );
        // 无 ExcelExport 注解情况
        if ( annotation == null || annotation.trans() ) {
            cf.setHasAnnotation( false );
            cf.setName( fieldName );
            cf.setSort( 0 );
            return cf;
        }
        // 有 ExcelExport 注解情况
        cf.setHasAnnotation( true );
        cf.setName( annotation.value() );
        String example = getString( annotation.example() );
        if ( !example.isEmpty() ) {
            if ( !field.getType().equals( String.class ) && isNumeric( example ) && example.length() < 8 ) {
                cf.setExample( Double.valueOf( example ) );
            } else {
                cf.setExample( example );
            }
        } else {
            cf.setExample( "" );
        }
        cf.setSort( annotation.sort() );
        // 解析映射
        String kv = getString( annotation.kv() );
        cf.setKvMap( getKvMap( kv ) );

        //  获取对应的样式注解
        cf.setExcelHeaderStyle( field.getAnnotation( ExcelHeaderStyle.class ) );
        cf.setExcelRowStyle( field.getAnnotation( ExcelRowStyle.class ) );

        return cf;
    }

    /**
     * 设置Cell单元格的数据
     */
    private static int setCellValue( Cell cell, Object o, CellStyle style ) {
        // 设置样式
        cell.setCellStyle( style );
        // 数据为空时
        if ( ObjectUtil.isNull( o ) ) {
            cell.setCellType( CellType.STRING );
            cell.setCellValue( "" );
            return CELL_OTHER;
        }
        // 是否为字符串
        if ( o instanceof String || isNumeric( o ) ) {
            String s = o.toString();
            // 当数字类型长度超过8位时，改为字符串类型显示（Excel数字超过一定长度会显示为科学计数法）
            if ( isNumeric( o ) && s.length() < 8 ) {
                cell.setCellType( CellType.NUMERIC );
                cell.setCellValue( Double.parseDouble( s ) );
                return CELL_OTHER;
            } else {
                cell.setCellType( CellType.STRING );
                cell.setCellValue( s );
            }
            if ( s.equals( ROW_MERGE ) ) {
                return CELL_ROW_MERGE;
            } else if ( s.equals( COLUMN_MERGE ) ) {
                return CELL_COLUMN_MERGE;
            } else {
                return CELL_OTHER;
            }
        }
        // 是否为字符串
//        if ( o instanceof Integer || o instanceof Long || o instanceof Double || o instanceof Float ) {
//            cell.setCellType( CellType.NUMERIC );
//            cell.setCellValue( Double.parseDouble( o.toString() ) );
//            return CELL_OTHER;
//        }
        // 是否为Boolean
        if ( o instanceof Boolean ) {
            cell.setCellType( CellType.BOOLEAN );
            cell.setCellValue( ( Boolean ) o );
            return CELL_OTHER;
        }
        // 如果是BigDecimal，则默认3位小数
        if ( o instanceof BigDecimal ) {
            cell.setCellType( CellType.NUMERIC );
            cell.setCellValue( ( ( BigDecimal ) o ).setScale( 3, RoundingMode.HALF_UP ).doubleValue() );
            return CELL_OTHER;
        }
        // 如果是Date数据，则显示格式化数据
        if ( o instanceof Date ) {
            cell.setCellType( CellType.STRING );
            cell.setCellValue( DateUtil.format( ( Date ) o, DatePattern.NORM_DATE_PATTERN ) );
            return CELL_OTHER;
        }
        // 如果是LocalDate数据，则显示格式化数据
        if ( o instanceof LocalDate ) {
            cell.setCellType( CellType.STRING );
            cell.setCellValue( LocalDateTimeUtil.format( ( LocalDate ) o, DatePattern.NORM_DATE_PATTERN ) );
            return CELL_OTHER;
        }
        // 如果是LocalDateTime数据，则显示格式化数据
        if ( o instanceof LocalDateTime ) {
            cell.setCellType( CellType.STRING );
            cell.setCellValue( LocalDateTimeUtil.format( ( LocalDateTime ) o, DatePattern.NORM_DATETIME_PATTERN ) );
            return CELL_OTHER;
        }
        // 如果是其他，则默认字符串类型
        cell.setCellType( CellType.STRING );
        cell.setCellValue( o.toString() );
        return CELL_OTHER;
    }

    /**
     * 从List中获取Excel需要的数据
     */
    private static < T > List< List< Object > > getSheetData( List< ExcelField > excelFields, List< T > list ) {
        List< List< Object > > sheetData = new ArrayList<>();
        // 获取表数据
        for ( T t : list ) {
            Map< String, Object > dataMap = BeanUtil.beanToMap( t );
            Set< String > fields = dataMap.keySet();
            List< Object > rows = new ArrayList<>();
            for ( ExcelField excelField : excelFields ) {
                if ( !fields.contains( excelField.getFieldName() ) ) {
                    continue;
                }
                Object data = dataMap.get( excelField.getFieldName() );
                if ( data == null ) {
                    rows.add( "" );
                    continue;
                }
                // 判断是否有映射关系
                LinkedHashMap< String, String > kvMap = excelField.getKvMap();
                String val = data.toString();
                if ( CollectionUtil.isNotEmpty( kvMap ) ) {
                    val = kvMap.getOrDefault( val, val );
                    rows.add( val );
                } else {
                    rows.add( data );
                }
            }
            sheetData.add( rows );
        }
        return sheetData;
    }
    /********************************************************************************/
    /***************************          Other          ***************************/
    /********************************************************************************/

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
            String[] eachKv = getString( each ).split( "-" );
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

    protected static String getString( String s ) {
        return ( s == null || s.isEmpty() ) ? "" : s.trim();
    }

    protected static boolean isNumeric( Object obj ) {
        if ( ObjectUtil.isEmpty( obj ) ) {
            return false;
        }
        if ( obj instanceof Number )
            return true;
        String str = obj.toString();
        str = str.replaceFirst( "\\.", "" );
        for ( int i = str.length(); --i >= 0; ) {
            if ( !( str.charAt( i ) >= '0' && str.charAt( i ) <= '9' ) ) {
                return false;
            }
        }
        return true;
    }

    /**
     * 合并当前Sheet页的单元格
     *
     * @param sheet      当前 sheet 页
     * @param mergeArray 合并单元格算法
     */
    private static void mergeCells( Sheet sheet, int[][] mergeArray ) {
        // 横向合并
        for ( int x = 0; x < mergeArray.length; x++ ) {
            int[] arr = mergeArray[x];
            boolean merge = false;
            int y1 = 0;
            int y2 = 0;
            for ( int y = 0; y < arr.length; y++ ) {
                int value = arr[y];
                if ( value == CELL_COLUMN_MERGE ) {
                    if ( !merge ) {
                        y1 = y;
                    }
                    y2 = y;
                    merge = true;
                } else {
                    merge = false;
                    if ( y1 > 0 ) {
                        sheet.addMergedRegion( new CellRangeAddress( x, x, ( y1 - 1 ), y2 ) );
                    }
                    y1 = 0;
                    y2 = 0;
                }
            }
            if ( y1 > 0 ) {
                sheet.addMergedRegion( new CellRangeAddress( x, x, ( y1 - 1 ), y2 ) );
            }
        }
        // 纵向合并
        int xLen = mergeArray.length;
        int yLen = mergeArray[0].length;
        for ( int y = 0; y < yLen; y++ ) {
            boolean merge = false;
            int x1 = 0;
            int x2 = 0;
            for ( int x = 0; x < xLen; x++ ) {
                int value = mergeArray[x][y];
                if ( value == CELL_ROW_MERGE ) {
                    if ( !merge ) {
                        x1 = x;
                    }
                    x2 = x;
                    merge = true;
                } else {
                    merge = false;
                    if ( x1 > 0 ) {
                        sheet.addMergedRegion( new CellRangeAddress( ( x1 - 1 ), x2, y, y ) );
                    }
                    x1 = 0;
                    x2 = 0;
                }
            }
            if ( x1 > 0 ) {
                sheet.addMergedRegion( new CellRangeAddress( ( x1 - 1 ), x2, y, y ) );
            }
        }
    }

    private static CellStyle renderRowStyle( SXSSFWorkbook workbook, ExcelStyle excelStyle, Annotation style ) {
        if ( ObjectUtil.isNull( workbook ) ) {
            return null;
        }
        //  字体样式
        Font font = workbook.createFont();
        CellStyle cellStyle = workbook.createCellStyle();
        boolean styleNotNull = ObjectUtil.isNotNull( excelStyle );
        if ( styleNotNull ) {
            font.setBold( excelStyle.bold() );
            cellStyle.setWrapText( excelStyle.wrap() );
        }

        if ( ObjectUtil.isNotNull( style ) ) {
            ExcelRowStyle rowStyle;
            if ( style instanceof ExcelRowStyle ) {
                rowStyle = ( ExcelRowStyle ) style;
            } else {
                ExcelHeaderStyle headerStyle = ( ExcelHeaderStyle ) style;
                rowStyle = headerStyle.rowStyle();
            }
            int bold = rowStyle.bold();
            font.setBold( bold == 0 ? ( styleNotNull && excelStyle.bold() ) : bold > 0 );
            font.setColor( rowStyle.color().index );
            int wrap = rowStyle.wrap();
            cellStyle.setWrapText( wrap == 0 ? ( styleNotNull && excelStyle.wrap() ) : wrap > 0 );
            //  对齐方式
            cellStyle.setAlignment( rowStyle.horizontalAlign() );
            cellStyle.setVerticalAlignment( rowStyle.verticalAlign() );
            //  背景颜色
            cellStyle.setFillForegroundColor( rowStyle.backgroundColor().index );
            cellStyle.setFillPattern( FillPatternType.SOLID_FOREGROUND );
        } else {
            cellStyle.setAlignment( HorizontalAlignment.CENTER );
            cellStyle.setVerticalAlignment( VerticalAlignment.CENTER );
        }
        cellStyle.setFont( font );
        if ( styleNotNull && excelStyle.border() ) {
            cellStyle.setBorderTop( BorderStyle.THIN );
            cellStyle.setTopBorderColor( IndexedColors.BLACK.index );
            cellStyle.setBorderRight( BorderStyle.THIN );
            cellStyle.setRightBorderColor( IndexedColors.BLACK.index );
            cellStyle.setBorderBottom( BorderStyle.THIN );
            cellStyle.setBottomBorderColor( IndexedColors.BLACK.index );
            cellStyle.setBorderLeft( BorderStyle.THIN );
            cellStyle.setLeftBorderColor( IndexedColors.BLACK.index );
        }
        return cellStyle;
    }

    private static void renderSheetStyle( SXSSFWorkbook workbook, SXSSFSheet sheet, ExcelStyle style ) {
        if ( ObjectUtil.isNull( workbook ) || ObjectUtil.isNull( sheet ) || ObjectUtil.isNull( style ) ) {
            return;
        }
        sheet.setDefaultColumnWidth( style.width() );
    }

    @SneakyThrows
    private static byte[] getBytes( SXSSFWorkbook book ) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        book.write( bos );
        return bos.toByteArray();
    }

    @SneakyThrows
    private static ServletOutputStream getResponseOutStream( String fileName ) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletResponse response = ( ( ServletRequestAttributes ) requestAttributes ).getResponse();
        Assert.notNull( response, "导出失败，response为空！" );
        response.setContentType( "application/octet-stream" );
        response.setHeader( "Content-Disposition", "attachment;filename=" + URLEncoder.encode( fileName, "UTF-8" ) );
        return response.getOutputStream();
    }
}
