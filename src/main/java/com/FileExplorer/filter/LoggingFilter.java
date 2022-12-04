package com.FileExplorer.filter;

import com.FileExplorer.entity.Log;
import com.FileExplorer.repository.LogRepository;
import com.FileExplorer.service.LogService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Component
public class LoggingFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    private final LogService logService;

    public LoggingFilter(LogService logService) {
        this.logService = logService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        Long startTime = System.currentTimeMillis();
        filterChain.doFilter(requestWrapper, responseWrapper);
        Long timeTaken = System.currentTimeMillis() - startTime;

        String requestBody = getStringValue(requestWrapper.getContentAsByteArray(),
                request.getCharacterEncoding());
        String responseBody = getStringValue(responseWrapper.getContentAsByteArray(),
                response.getCharacterEncoding());

        responseWrapper.copyBodyToResponse();

        Log log = logService.setLog(
                request.getMethod(),
                request.getRequestURI(),
                requestBody,
                response.getStatus(),
                responseBody,
                timeTaken);

        logger.info(log.toString());

        logger.error(log.toString());

        logger.debug(log.toString());

        logger.warn(log.toString());
    }

    private String getStringValue(byte[] contentAsByteArray, String charEncoding) {
        try {
            return new String(contentAsByteArray, 0, contentAsByteArray.length, charEncoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
