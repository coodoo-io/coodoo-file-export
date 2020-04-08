package io.coodoo.framework.export.boundary;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import io.coodoo.framework.export.control.FileExportConfig;
import io.coodoo.framework.export.control.FileExportUtil;

public final class FileExport {

    public static final String MEDIA_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String MEDIA_TYPE_CSV = "text/csv";
    public static final String MEDIA_TYPE_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

    private FileExport() {}

    public static ByteArrayOutputStream buildXLS(List<? extends Object> objects) {
        return buildXLS(null, objects, new XslLayout());
    }

    public static ByteArrayOutputStream buildXLS(String title, List<? extends Object> objects) {
        return buildXLS(title, objects, new XslLayout());
    }

    public static ByteArrayOutputStream buildXLS(List<? extends Object> objects, XslLayout layout) {
        return buildXLS(null, objects, layout);
    }

    public static ByteArrayOutputStream buildXLS(String title, List<? extends Object> objects, XslLayout layout) {

        if (objects == null || objects.isEmpty()) {
            throw new RuntimeException("No data");
        }

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            int rowIndex = 0;
            String exportTitle = title;
            Sheet sheet = workbook.createSheet();

            List<Field> fields = FileExportUtil.getFields(objects.get(0).getClass());

            // Title
            if (FileExportUtil.isEmpty(exportTitle)) {
                exportTitle = FileExportUtil.getTitle(fields.get(0));
            }
            if (FileExportUtil.notEmpty(exportTitle)) {
                Row titleRow = sheet.createRow(rowIndex++);
                Cell cell = titleRow.createCell(0);
                cell.setCellStyle(layout.getTitle().cellStyle(workbook));
                cell.setCellValue(exportTitle);
                CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, fields.size() - 1);
                sheet.addMergedRegion(cellRangeAddress);
            }

            // Header
            Row headerRow = sheet.createRow(rowIndex++);
            headerRow.setHeight((short) FileExportConfig.XSL_HEADER_HEIGTH);
            for (Field field : fields) {
                Cell cell = headerRow.createCell(fields.indexOf(field));
                cell.setCellStyle(layout.getHeader().cellStyle(workbook));
                cell.setCellValue(FileExportUtil.getFieldName(field));
            }

            // Content
            XSSFCellStyle rowStyleEven = layout.getRow().cellStyle(workbook);
            XSSFCellStyle rowStyleOdd = null;
            if (FileExportConfig.XSL_STYLE_COLOR_EVEN != null && FileExportConfig.XSL_STYLE_COLOR_EVEN != null) {
                rowStyleOdd = layout.getRow().cellStyle(workbook);
                rowStyleOdd.setFillForegroundColor(XslCellLayout.colorOfHex(FileExportConfig.XSL_STYLE_COLOR_ODD));
                rowStyleEven.setFillForegroundColor(XslCellLayout.colorOfHex(FileExportConfig.XSL_STYLE_COLOR_EVEN));
            }
            for (Object object : objects) {
                Row row = sheet.createRow(rowIndex++);
                row.setHeight((short) FileExportConfig.XSL_HEIGTH);
                for (Field field : fields) {
                    Cell cell = row.createCell(fields.indexOf(field));
                    if (rowStyleOdd != null && rowIndex % 2 != 0) {
                        cell.setCellStyle(rowStyleOdd);
                    } else {
                        cell.setCellStyle(rowStyleEven);
                    }
                    String fieldValue = FileExportUtil.getFieldValue(field, object);
                    if (fieldValue != null) {
                        cell.setCellValue(fieldValue);
                    }
                }
            }

            // Autoresize of columns
            for (Field field : fields) {
                sheet.autoSizeColumn(fields.indexOf(field));
            }

            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                workbook.write(baos);
                return baos;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static ByteArrayOutputStream buildDOC(List<? extends Object> objects) {
        return buildDOC(null, objects);
    }

    public static ByteArrayOutputStream buildDOC(String title, List<? extends Object> objects) {

        if (objects == null || objects.isEmpty()) {
            throw new RuntimeException("No data");
        }

        try {
            XWPFDocument document = new XWPFDocument();
            String exportTitle = title;
            List<Field> fields = FileExportUtil.getFields(objects.get(0).getClass());

            // Title
            if (FileExportUtil.isEmpty(exportTitle)) {
                exportTitle = FileExportUtil.getTitle(fields.get(0));
            }
            if (FileExportUtil.notEmpty(exportTitle)) {
                XWPFParagraph titleParagraph = document.createParagraph();
                XWPFRun titleRun = titleParagraph.createRun();

                titleRun.setText(exportTitle);
                titleRun.setFontSize(11);
                titleRun.setFontFamily("Helvetica");
                titleRun.setColor("000099");
                titleRun.setBold(true);
            }

            // Content
            for (Object object : objects) {
                XWPFTable table = document.createTable();
                boolean firstRow = true;
                for (Field field : fields) {
                    if (firstRow) {
                        XWPFTableRow tableRowOne = table.getRow(0);
                        tableRowOne.getCell(0).setText(FileExportUtil.getFieldName(field));
                        tableRowOne.addNewTableCell().setText(FileExportUtil.getFieldValue(field, object));
                        firstRow = false;
                    } else {
                        XWPFTableRow tableRowTwo = table.createRow();
                        tableRowTwo.getCell(0).setText(FileExportUtil.getFieldName(field));
                        tableRowTwo.getCell(1).setText(FileExportUtil.getFieldValue(field, object));
                    }
                }
                // make space between the tables
                document.createParagraph().createRun().setText(" ");
            }
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                document.write(baos);
                return baos;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static ByteArrayOutputStream buildCSV(List<? extends Object> objects) {

        if (objects == null || objects.isEmpty()) {
            throw new RuntimeException("No data");
        }
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            List<Field> fields = FileExportUtil.getFields(objects.get(0).getClass());
            String separator = FileExportConfig.CSV_SEPARATOR;

            // Header
            baos.write(fields.stream().map(field -> FileExportUtil.getFieldName(field)).collect(Collectors.joining(separator)).getBytes());
            baos.write(System.lineSeparator().getBytes());

            // Content
            for (Object object : objects) {
                baos.write(fields.stream().map(field -> FileExportUtil.getCsvValue(field, object)).collect(Collectors.joining(separator)).getBytes());
                baos.write(System.lineSeparator().getBytes());
            }
            return baos;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String createFileName(String name, String timestampPattern, String postFix) {

        List<String> parts = new ArrayList<>();

        if (FileExportUtil.notEmpty(name)) {
            parts.add(name);
        }
        if (FileExportUtil.notEmpty(timestampPattern)) {
            parts.add(LocalDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ofPattern(timestampPattern)));
        }
        if (FileExportUtil.notEmpty(postFix)) {
            parts.add(postFix);
        }
        return parts.stream().map(part -> part).collect(Collectors.joining("."));
    }

    public static Response createResponse(ByteArrayOutputStream stream, String name) {
        return Response.ok(stream.toByteArray(), MediaType.APPLICATION_OCTET_STREAM).header("Content-Disposition", "attachment;filename=" + name).build();
    }

    public static Response createXLSResponse(List<? extends Object> objects, String name) {
        return createResponse(buildXLS(name, objects), createFileName(name, FileExportConfig.TIMESTAMP_PATTERN, "xlsx"));
    }

    public static Response createDOCResponse(List<? extends Object> objects, String name) {
        return createResponse(buildDOC(name, objects), createFileName(name, FileExportConfig.TIMESTAMP_PATTERN, "docx"));
    }

    public static Response createCSVResponse(List<? extends Object> objects, String name) {
        return createResponse(buildCSV(objects), createFileName(name, FileExportConfig.TIMESTAMP_PATTERN, "csv"));
    }

}
