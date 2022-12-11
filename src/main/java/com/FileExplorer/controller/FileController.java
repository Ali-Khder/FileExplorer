package com.FileExplorer.controller;

import com.FileExplorer.dto.foldersAndFiles.FileBookingDto;
import com.FileExplorer.dto.foldersAndFiles.FileDto;
import com.FileExplorer.dto.foldersAndFiles.UserFolderDto;
import com.FileExplorer.handler.ResponseHandler;
import com.FileExplorer.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/files")
public class FileController {

    @Autowired
    private FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        return ResponseHandler.responseBuilder(
                true,
                HttpStatus.OK,
                "File deleted successfully",
                fileService.fileDelete(id)
        );
    }

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
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
