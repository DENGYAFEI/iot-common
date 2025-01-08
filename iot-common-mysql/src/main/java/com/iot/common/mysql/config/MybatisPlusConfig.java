package com.iot.common.mysql.config;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.iot.common.mysql.model.anotation.TableVO;
import com.iot.common.mysql.interceptor.QueryFilterInterceptor;
import com.iot.common.mysql.model.pojo.entity.Base;
import com.iot.common.mysql.model.pojo.entity.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;

import javax.annotation.Resource;
import java.io.File;

/**
 * @Author Orchid
 * @Create 2024/4/2
 * @Remark
 */
@Slf4j
@Configuration
@MapperScan( { "com.iot.*.mapper" } )
public class MybatisPlusConfig implements InitializingBean {

    @Resource
    private QueryFilterInterceptor queryFilterInterceptor;

//    @Resource
//    private PermissionInterceptor permissionInterceptor;

    @Bean
    public IdentifierGenerator identifierGenerator(){
        return entity -> IdUtil.getSnowflakeNextId();
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //  通用查询过滤插件
        interceptor.addInnerInterceptor( queryFilterInterceptor );
//        interceptor.addInnerInterceptor( permissionInterceptor );
        //  分页插件
        interceptor.addInnerInterceptor( new PaginationInnerInterceptor( DbType.MYSQL ) );
        return interceptor;
    }

    @Override
    public void afterPropertiesSet() {
        MybatisConfiguration configuration = new MybatisConfiguration();
        MapperBuilderAssistant baseBuilder = new MapperBuilderAssistant( configuration, "" );
        MapperScan mapperScan = MybatisPlusConfig.class.getAnnotation( MapperScan.class );
        String[] packageNames = mapperScan.value();
        TableInfoHelper.initTableInfo( baseBuilder, Base.class );
        TableInfoHelper.initTableInfo( baseBuilder, BaseEntity.class );
        for ( String packageName : packageNames ) {
            String pattern = packageName.replace( "mapper", "pojo.vo" ).replace( ".", File.separator ) + File.separator + "*.class";
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            try {
                org.springframework.core.io.Resource[] resources = resolver.getResources( pattern );
                CachingMetadataReaderFactory cachingMetadataReaderFactory = new CachingMetadataReaderFactory();
                for ( org.springframework.core.io.Resource resource : resources ) {
                    String className = cachingMetadataReaderFactory.getMetadataReader( resource ).getClassMetadata().getClassName();
                    Class< ? > clazz = Class.forName( className );
                    if ( ObjectUtil.isNotNull( clazz.getAnnotation( TableVO.class ) ) ) {
                        TableInfoHelper.initTableInfo( baseBuilder, clazz );
                    }
                }
            } catch ( Exception e ) {
                log.debug( e.getMessage() );
            }
        }
    }

}
