package com.iot.common.web.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class RequestWrapper extends HttpServletRequestWrapper {

    private String body = null;

    public RequestWrapper( HttpServletRequest request ) throws IOException {
        super( request );
        String contentType = request.getContentType();
        if ( StringUtils.hasText( contentType ) && contentType.contains( MediaType.MULTIPART_FORM_DATA_VALUE ) )
            return;
        this.body = getBodyString();
    }

    public String getBodyString() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = null;
        try {
            InputStream is = getRequest().getInputStream();
            reader = new BufferedReader( new InputStreamReader( is, StandardCharsets.UTF_8 ) );
            String line;
            while ( ( line = reader.readLine() ) != null && !"null".equals( line ) ) {
                stringBuilder.append( line );
            }
            reader.close();
        } finally {
            if ( ObjectUtil.isNotNull( reader ) )
                reader.close();
        }
        return stringBuilder.length() == 0 || !JSONUtil.isTypeJSON( stringBuilder.toString() ) ? "" : JSONUtil.toJsonStr( JSONUtil.parse( stringBuilder.toString() ) );
    }

    public String getBody() {
        return body;
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader( new InputStreamReader( getInputStream() ) );

    }

    @Override
    public ServletInputStream getInputStream() {
        ByteArrayInputStream bis = new ByteArrayInputStream( body.getBytes( StandardCharsets.UTF_8 ) );
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return bis.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener( ReadListener readListener ) {

            }

            @Override
            public int read() {
                return bis.read();
            }
        };
    }
}
