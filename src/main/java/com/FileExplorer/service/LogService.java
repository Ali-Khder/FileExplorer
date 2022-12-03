package com.FileExplorer.service;

import com.FileExplorer.entity.Log;
import com.FileExplorer.repository.LogRepository;
import com.FileExplorer.security.JwtTokenUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LogService {
    private final LogRepository logRepository;
    private final JwtTokenUtils jwtTokenUtils;

    public LogService(LogRepository logRepository,
                      JwtTokenUtils jwtTokenUtils) {
        this.logRepository = logRepository;
        this.jwtTokenUtils = jwtTokenUtils;
    }

    public Log setLog(String method,
                      String uri,
                      String requestBody,
                      int statusCode,
                      String responseBody,
                      Long time) {

        Log log = new Log(
                method,
                uri,
                requestBody,
                statusCode,
                responseBody,
                time);
        logRepository.save(log);
        return log;
    }

    public Map<String, Object> findPaginated(int page, int size) {
        System.out.println(jwtTokenUtils.getMyUsername());
        List<Log> logs = new ArrayList<Log>();
        Pageable paging = PageRequest.of(page, size);

        Page<Log> pageTuts;
        pageTuts = logRepository.findAll(paging);

        logs = pageTuts.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("logs", logs);
        response.put("currentPage", pageTuts.getNumber());
        response.put("totalItems", pageTuts.getTotalElements());
        response.put("totalPages", pageTuts.getTotalPages());

        return response;
    }
}
