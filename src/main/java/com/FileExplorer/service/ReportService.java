package com.FileExplorer.service;

import com.FileExplorer.entity.File;
import com.FileExplorer.interfaces.selectedOnlyForReports;
import com.FileExplorer.repository.FileRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class ReportService {

    private List<selectedOnlyForReports> fileList;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private final FileRepository fileRepository;

    public ReportService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
        workbook = new XSSFWorkbook();
    }

    private List<selectedOnlyForReports> getReports() {
        return fileRepository.findAllByOrderByCreatedAtDesc();
    }

    private void writeHeader() {
        sheet = workbook.createSheet("Report");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "ID", style);
        createCell(row, 1, "Owner Name", style);
        createCell(row, 2, "Barrier Name", style);
        createCell(row, 3, "Created At", style);
        createCell(row, 4, "updated At", style);
        createCell(row, 5, "Booked At", style);
        createCell(row, 6, "Unbooked At", style);
    }

    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        } else {
            cell.setCellValue((Boolean) valueOfCell);
        }
        cell.setCellStyle(style);
    }

    private ByteArrayInputStream write() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        fileList = this.getReports();
        for (selectedOnlyForReports record : fileList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, record.getId(), style);
            createCell(row, columnCount++, record.getUser() == null ? "" : record.getUser().getUsername(), style);
            createCell(row, columnCount++, record.getBarrier() == null ? "" : record.getBarrier(), style);
            createCell(row, columnCount++, record.getCreatedAt() == null ? "" : record.getCreatedAt(), style);
            createCell(row, columnCount++, record.getUpdatedAt() == null ? "" : record.getUpdatedAt(), style);
            createCell(row, columnCount++, record.getBookedAt() == null ? "" : record.getBookedAt(), style);
            createCell(row, columnCount++, record.getUnbookedAt() == null ? "": record.getUnbookedAt(), style);
        }
        workbook.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream generateExcelFile(HttpServletResponse response) throws IOException {
        writeHeader();
        write();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        ByteArrayInputStream in = write();
        workbook.close();
        return in;
    }
}
