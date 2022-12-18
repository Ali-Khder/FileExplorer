package com.FileExplorer.controller;

import com.FileExplorer.dto.foldersAndFiles.FileBookingDto;
import com.FileExplorer.dto.foldersAndFiles.FileDto;
import com.FileExplorer.handler.ResponseHandler;
import com.FileExplorer.service.FileService;
import com.FileExplorer.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/files")
public class FileController {

    @Autowired
    private FileService fileService;
    @Autowired
    private ReportService reportService;

    public FileController(FileService fileService, ReportService reportService) {
        this.fileService = fileService;
        this.reportService = reportService;
    }

    // path variable ID is for folder
    @GetMapping("/{id}")
    public ResponseEntity<Object> index(@PathVariable Long id) {
        return ResponseHandler.responseBuilder(
                true,
                HttpStatus.OK,
                "Files",
                fileService.getAll(id)
        );
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<Object> show(@PathVariable Long id) {
        return ResponseHandler.responseBuilder(
                true,
                HttpStatus.OK,
                "File",
                fileService.getFile(id)
        );
    }

    @GetMapping("/report/get")
    public ResponseEntity<Object> getReports() {
        return ResponseHandler.responseBuilder(
                true,
                HttpStatus.OK,
                "Reports",
                fileService.getReports()
        );
    }

    @Transactional(isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED,
            readOnly = false,
            timeout = 100,
            rollbackFor = Exception.class)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) throws IOException {
        return ResponseHandler.responseBuilder(
                true,
                HttpStatus.OK,
                "File deleted successfully",
                fileService.fileDelete(id)
        );
    }

    @Transactional(isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED,
            readOnly = false,
            timeout = 100,
            rollbackFor = Exception.class)
    @PostMapping
    public ResponseEntity<Object> createFile(@Valid FileDto fileDto) throws IOException {
        return ResponseHandler.responseBuilder(
                true,
                HttpStatus.OK,
                "File created successfully",
                fileService.create(
                        fileDto.getName(),
                        fileDto.getFile(),
                        fileDto.getFolderId()
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateFile(
            @PathVariable Long id,
            @Valid FileDto fileDto) throws IOException {
        return ResponseHandler.responseBuilder(
                true,
                HttpStatus.OK,
                "File updated successfully",
                fileService.update(
                        id,
                        fileDto.getName(),
                        fileDto.getFile()
                )
        );
    }

    @PutMapping("/booking")
    public ResponseEntity<Object> bookFiles(FileBookingDto fileBookingDto) {
        return ResponseHandler.responseBuilder(
                true,
                HttpStatus.OK,
                "Files updated successfully",
                fileService.booking(fileBookingDto.getFilesIds())
        );
    }

    @PutMapping("/booking/cancellation")
    public ResponseEntity<Object> cancellationBookFiles(FileBookingDto fileBookingDto) {
        return ResponseHandler.responseBuilder(
                true,
                HttpStatus.OK,
                "Files updated successfully",
                fileService.bookingCancellation(fileBookingDto.getFilesIds())
        );
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileService.loadFileAsResource(id);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/reports/export-excel")
    public ResponseEntity<Resource> exportExcel(HttpServletResponse response) throws IOException {
        String filename = "reports.xlsx";
        InputStreamResource file = new InputStreamResource(reportService.generateExcelFile(response));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
}
