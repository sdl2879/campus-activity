package com.campus.activity.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Excel导出工具类（适配架构文档「日志导出」功能）
 */
public class ExcelExportUtil {

    /**
     * 导出Excel（通用方法）
     * @param response 响应对象（用于下载）
     * @param fileName 导出文件名（如"管理员操作日志_2024.xlsx"）
     * @param headers Excel表头（如["日志ID","操作人姓名"]）
     * @param fields DTO字段名（与表头一一对应，如["logId","adminName"]）
     * @param dataList 导出数据列表（LogExportDTO列表）
     */
    public static <T> void exportExcel(HttpServletResponse response, String fileName,
                                       String[] headers, String[] fields, List<T> dataList) {
        // 1. 创建工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("日志数据");

        // 2. 设置表头样式
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        // 3. 构建表头
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.autoSizeColumn(i); // 自动调整列宽
        }

        // 4. 填充数据
        for (int i = 0; i < dataList.size(); i++) {
            Row dataRow = sheet.createRow(i + 1);
            T data = dataList.get(i);
            // 通过反射获取DTO字段值（简化版，实际项目可优化）
            for (int j = 0; j < fields.length; j++) {
                Cell cell = null;
                try {
                    java.lang.reflect.Field field = data.getClass().getDeclaredField(fields[j]);
                    field.setAccessible(true);
                    Object value = field.get(data);
                    cell = dataRow.createCell(j);
                    cell.setCellValue(value != null ? value.toString() : "");
                } catch (Exception e) {
                    cell.setCellValue("");
                }
            }
        }

        // 5. 响应配置（下载Excel）
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setCharacterEncoding("UTF-8");

        // 6. 写入响应流
        try (OutputStream outputStream = response.getOutputStream()) {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException("Excel导出失败：" + e.getMessage());
        }
    }
}