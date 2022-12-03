package com.FileExplorer.controller;

import com.FileExplorer.handler.ResponseHandler;
import com.FileExplorer.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/logs")
public class LogController {

    @Autowired
    private LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping
    public ResponseEntity<Object> allLogs(
            @RequestParam(name = "page",
                    required = true,
                    defaultValue = "0") int page,
            @RequestParam(name = "size",
                    required = true,
                    defaultValue = "6") int pageSize) {
        return ResponseHandler.responseBuilder(true, HttpStatus.OK
                , "Logs", logService.findPaginated(page, pageSize));

    }
}
