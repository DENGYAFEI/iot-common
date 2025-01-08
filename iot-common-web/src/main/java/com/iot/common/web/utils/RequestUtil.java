package com.iot.common.web.utils;

import com.iot.common.constant.ExceptionConstant;
import com.iot.common.exception.NotFoundException;
import com.iot.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

/**
 * @Author Orchid
 * @Create 2024/4/2
 * @Remark Request Util
 */
@Slf4j
public class RequestUtil {

    private RequestUtil() {
        throw new IllegalStateException( ExceptionConstant.UTILITY_CLASS );
    }

    /**
     * 从 Request 中获取指定 Key 的 Header 值
     *
     * @param request {@link HttpServletRequest}
     * @param key     Header Name
     * @return Header Value
     */
    public static String getRequestHeader( HttpServletRequest request, String key ) {
        return request.getHeader( key );
    }

    /**
     * 返回下载文件
     *
     * @param path 文件 Path
     * @return Resource
     * @throws MalformedURLException MalformedURLException
     */
    public static ResponseEntity< Resource > responseFile( Path path ) throws MalformedURLException {
        Resource resource = new UrlResource( path.toUri() );
        if ( !resource.exists() ) {
            throw new NotFoundException( "File not found: " + path.getFileName() );
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add( HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename() );
        headers.add( HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE );
        try {
            return ResponseEntity.ok()
                    .headers( headers )
                    .contentLength( resource.contentLength() )
                    .body( resource );
        } catch ( IOException e ) {
            throw new ServiceException( "Error occurred while response file: {}", path.getFileName() );
        }
    }

}
