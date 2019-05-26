package org.orion.common.miscutil;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yuzhou
 * xlsx opreation class
 */
public final class PoiUtil {

    public static XSSFWorkbook getXlsxWorkbookByFile(String path) {
        XSSFWorkbook workbook = null;
        FileInputStream stream = IOUtil.getFileInputStream(path);
        String suffix = StringUtil.getFileSuffix(path);
        try {
            if ("xlsx".equalsIgnoreCase(suffix)) {
                workbook = new XSSFWorkbook(stream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workbook;

    }

    public static XSSFCellStyle stdContentCellStyle(XSSFWorkbook workbook) {
        if (workbook == null) {
            return null;
        }
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setFontName("微软雅黑");
        cellStyle.setFont(font);
        return cellStyle;
    }

    public static String getSheetName(XSSFWorkbook workbook,int index) {
        return workbook != null && index >= 0 ? workbook.getSheetName(index) : null;
    }

    public static XSSFSheet getSheet(XSSFWorkbook workbook,String sheetName) {
        return workbook != null && !StringUtil.isEmpty(sheetName) ? workbook.getSheet(sheetName) : null;
    }

    /**
     * this method is used for write
     * get Cell from workbook, sheet and cell will be created if do not exsit
     * @param workbook xlsx workbook
     * @param sheetName xlsx doc sheet name
     * @param rowNum row number start from 0
     * @param cellNum column number start from 0
     * @return cell
     */
    public static XSSFCell getCell(XSSFWorkbook workbook, String sheetName, int rowNum, int cellNum) {
        if (workbook == null || rowNum < 0 || cellNum < 0 || StringUtil.isEmpty(sheetName)) {
            return null;
        }
        XSSFSheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
        }
        XSSFRow row = sheet.getRow(rowNum);
        if (row == null) {
            row = sheet.createRow(rowNum);
        }
        XSSFCell cell = row.getCell(cellNum);
        if (cell == null) {
            cell = row.createCell(cellNum);
        }
        return cell;
    }

    public static int getFirstRow(XSSFWorkbook workbook, String sheetName) {
        if (workbook != null && !sheetName.isEmpty()) {
            XSSFSheet sheet = getSheet(workbook, sheetName);
            return sheet.getFirstRowNum();
        }
        return -1;
    }

    public static int getLastRow(XSSFWorkbook workbook, String sheetName) {
        if (workbook != null && !sheetName.isEmpty()) {
            XSSFSheet sheet = getSheet(workbook, sheetName);
            return sheet.getLastRowNum();
        }
        return -1;
    }



    public static XSSFWorkbook writeCell(XSSFWorkbook workbook, String sheetName, int rowNum, int cellNum, String value, XSSFCellStyle style) {
        if (workbook != null) {
            XSSFCell cell = getCell(workbook, sheetName, rowNum, cellNum);
            XSSFSheet sheet = workbook.getSheet(sheetName);
            sheet.autoSizeColumn(cellNum);
            if (style != null) {
                cell.setCellStyle(style);
            }
            cell.setCellValue(value);
        }
        return workbook;
    }

    /**
     * Simplified write cell
     * No Style Support
     * @param xssfRow
     * @param cellNum
     * @param value
     * @return
     */
    public static XSSFRow writeCell(XSSFRow xssfRow, int cellNum, String value) {
        if (xssfRow != null) {
            XSSFCell cell = xssfRow.getCell(cellNum);
            cell = cell == null ? xssfRow.createCell(cellNum) : cell;
            cell.setCellValue(value);
        }
        return xssfRow;
    }

    public static void writeCell(XSSFCell cell, String value, XSSFCellStyle style) {
        if (cell != null) {
            cell.setCellStyle(style);
            cell.setCellValue(value);
        }
    }

    public static void writeRow(XSSFWorkbook workbook, String sheetName, int rowNum, int colStartNum, List<String> content, List<XSSFCellStyle> styles) {
        if (workbook == null || StringUtil.isEmpty(sheetName) || rowNum < 0 || content == null) {
            return;
        }
        if (styles != null) {
            int styleSize = styles.size();
            for (int i = 0; i < content.size(); i++) {
                if (i < styleSize) {
                    writeCell(workbook, sheetName, rowNum, colStartNum + i, content.get(i), styles.get(i));
                } else {
                    writeCell(workbook, sheetName, rowNum, colStartNum + i, content.get(i), null);
                }
            }
        } else {
            for (int i = 0; i < content.size(); i++) {
                writeCell(workbook, sheetName, rowNum, colStartNum + i, content.get(i), null);
            }
        }
    }

    public static String readCell(XSSFWorkbook workbook, String sheetName, int rw, int col) {
        if (workbook == null || StringUtil.isEmpty(sheetName) || rw < 0 || col < 0) {
            return null;
        }
        XSSFSheet sheet = workbook.getSheet(sheetName);
        String value = null;
        if (sheet != null) {
            XSSFRow row = sheet.getRow(rw);
            if (row != null) {
                XSSFCell cell = row.getCell(col);
                value = convertCellValue(cell);
            }
        }
        return value;
    }

    public static String readCell(XSSFCell cell) {
        return convertCellValue(cell);
    }

    public static List<String> readRow(XSSFWorkbook workbook, String sheetName, int rowNum) {
        if (workbook == null || StringUtil.isEmpty(sheetName)) {
            return null;
        }
        XSSFSheet sheet = workbook.getSheet(sheetName);
        List<String> rowContent = new ArrayList<String>();
        if (sheet != null) {
            int firstRow = sheet.getFirstRowNum();
            int lastRow = sheet.getLastRowNum();
            if (firstRow <= rowNum && lastRow >= rowNum) {
                XSSFRow row = sheet.getRow(rowNum);
                int firstColumn = row.getFirstCellNum();
                int lastColumn = row.getLastCellNum();
                for (int j = firstColumn; j < lastColumn; j++) {
                    rowContent.add(convertCellValue(row.getCell(j)));
                }
            }
        }
        return rowContent;
    }

    public static List<List<String>> readAll(XSSFWorkbook workbook, String sheetName) {
        if (workbook == null || StringUtil.isEmpty(sheetName)) {
            return null;
        }
        List<List<String>> allContent = null;
        XSSFSheet sheet = getSheet(workbook, sheetName);
        if (sheet != null) {
            allContent = new ArrayList<List<String>>();
            int firstRow = sheet.getFirstRowNum();
            int lastRow = sheet.getLastRowNum();
            for (int i = firstRow; i <= lastRow; i++) {
                List<String> row = readRow(workbook, sheetName, i);
                allContent.add(row);
            }
        }
        return allContent;
    }

    public static int getFirstRowNo(XSSFWorkbook workbook, String sheetName) {
        if (workbook == null || StringUtil.isEmpty(sheetName)) {
            return -1;
        }
        XSSFSheet sheet = getSheet(workbook, sheetName);
        return sheet != null ? sheet.getFirstRowNum() : -1;
    }

    public static int getLastRowNo(XSSFWorkbook workbook, String sheetName) {
        if (workbook == null || StringUtil.isEmpty(sheetName)) {
            return -1;
        }
        XSSFSheet sheet = getSheet(workbook, sheetName);
        return sheet != null ? sheet.getLastRowNum() : -1;
    }

    public static void save(XSSFWorkbook workbook, String path) {
        if (workbook != null) {
            FileOutputStream stream = IOUtil.getFileOutputStream(path);
            try {
                workbook.write(stream);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static byte[] saveToByteArray(XSSFWorkbook workbook) {
        byte[] data = null;
        if (workbook != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try {
                workbook.write(stream);
                workbook.close();
                data = stream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    private static String convertCellValue(XSSFCell cell) {
        String value = null;
        if (cell != null) {
            switch (cell.getCellType()) {
                case NUMERIC:
                    value = String.valueOf(cell.getNumericCellValue());
                    break;
                case BOOLEAN:
                    value = String.valueOf(cell.getBooleanCellValue());
                    break;
                case FORMULA :
                    value = cell.getCellFormula();
                    break;
                case STRING:
                    value = cell.getStringCellValue();
                    break;
                case BLANK:
                    value = "";
                    break;
                case ERROR:
                    value = null;
                    break;
                default:
                    value = null;
                    break;
            }
        }
        return value;
    }


}
