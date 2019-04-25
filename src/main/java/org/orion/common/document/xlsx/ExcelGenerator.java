package org.orion.common.document.xlsx;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.orion.common.miscutil.PoiUtil;

import java.util.List;

public class ExcelGenerator {

    private List<List<String>> content;
    private  XSSFWorkbook workbook;

    public ExcelGenerator(List<List<String>> content) {
        this.content = content;
        workbook = new XSSFWorkbook();
    }

    public byte[] generateExcel(int startRow, int startColumn) {
        if (content != null && !content.isEmpty()) {
            for (List<String> singleRow : content) {
                PoiUtil.writeRow(workbook, "result", startRow ++, startColumn, singleRow, null);
            }
            return PoiUtil.saveToByteArray(workbook);
        }
        return null;

    }

}
