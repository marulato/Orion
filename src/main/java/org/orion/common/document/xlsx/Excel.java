package org.orion.common.document.xlsx;

import org.apache.poi.xssf.usermodel.*;
import org.orion.common.miscutil.PoiUtil;
import org.orion.common.miscutil.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;


public class Excel {

    private String path;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private Map<Integer, XSSFRow> rowMap;
    private String sheetName;
    private List<List<String>> content;
    private XSSFCellStyle stdStyle;
    private int miniumCol;
    private int maxCol;

    public Excel(String path, String sheetName) {
        this.path = path;
        this.sheetName = sheetName;
        workbook = PoiUtil.getXlsxWorkbookByFile(path);
        rowMap = new HashMap<>();
        content = new ArrayList<>();
        if (workbook != null) {
            sheet = PoiUtil.getSheet(workbook, sheetName);
            initData();
        } else {
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet(sheetName);
        }
        stdStyle = PoiUtil.stdContentCellStyle(workbook);
    }

    private void initData() {
        rowMap = PoiUtil.readRowMap(sheet);
        rowMap.forEach((rn, row) -> {
            List<String> content = PoiUtil.readRowContent(row);
            this.content.add(content);
        });
    }

    public String getValue(String coordinate) {
        String cord = PoiUtil.getNumericCoordinate(coordinate);
        if (!StringUtil.isEmpty(cord)) {
            String[] matrix = cord.split(",");
            return getValue(Integer.valueOf(matrix[1]) - 1, Integer.valueOf(matrix[0]) - 1);
        }
        return null;
    }

    public String getValue(int rn, int cn) {
        return PoiUtil.readCell(rowMap.get(rn).getCell(cn));
    }

    public void setValue(String coordinate, String value) {
        String cord = PoiUtil.getNumericCoordinate(coordinate);
        if (!StringUtil.isEmpty(cord)) {
            String[] matrix = cord.split(",");
            setValue(Integer.valueOf(matrix[1]) - 1, Integer.valueOf(matrix[0]) - 1, value);
        }
    }

    public void setValue(int rn, int cn, String value) {
        XSSFRow row = getRow(rn);
        if (row != null && !StringUtil.isEmpty(value)) {
            XSSFCell cell = getCell(row, cn);
            PoiUtil.setValue(cell, value, stdStyle);
        }
    }

    public void setRow(int rn, List<String> content, int start) {
        XSSFRow row = getRow(rn);
        if (row != null && content != null && !content.isEmpty()) {
            if (start < miniumCol) {
                miniumCol = start;
            }
            if (content.size() + start > maxCol) {
                maxCol = content.size() + start - 1;
            }
            for (int i = start; i < content.size() + start; i++) {
                XSSFCell cell = getCell(row, i);
                PoiUtil.setValue(cell, content.get(i - start), stdStyle);
            }
        }
    }

    public void setRow(int rn, List<String> content) {
        setRow(rn, content, 0);
    }

    public void save() {
        int size = (maxCol + 1 -miniumCol) / 5;
        try {
            autoSizeColumn(size).await();
            PoiUtil.save(workbook, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] toByteArray() {
        int size = (maxCol + 1 -miniumCol) / 5;
        try {
            autoSizeColumn(size).await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return PoiUtil.saveToByteArray(workbook);
    }

    private XSSFRow getRow(int rn) {
        XSSFRow row = null;
        if (rn >= 0) {
            row = rowMap.get(rn);
            if (row == null) {
                row = sheet.createRow(rn);
                rowMap.put(rn, row);
            }
        }
        return row;
    }

    private XSSFCell getCell(XSSFRow row, int cn) {
        XSSFCell cell = null;
        if (cn >= 0) {
            cell = row.getCell(cn);
            if (cell == null) {
                cell = row.createCell(cn);
            }
        }
        return cell;
    }

    private CountDownLatch autoSizeColumn(int size) {
        CountDownLatch latch = new CountDownLatch(size);
        for (int i = 0; i < size; i++) {
            new Thread(new RunAutoSize(i * 5, i * 5 + 4, latch)).start();
        }
        return latch;
    }

    private void autoSizeColumn(int from, int to) {
        for (int i = from; i <= to; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private class RunAutoSize implements Runnable {
        int start;
        int end;
        CountDownLatch latch;

        RunAutoSize(int start, int end, CountDownLatch latch) {
            this.start = start;
            this.end = end;
            this.latch = latch;
        }
        @Override
        public void run() {
            autoSizeColumn(start, end);
            latch.countDown();
        }
    }


}
