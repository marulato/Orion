package org.orion.common.document.xlsx;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.orion.common.miscutil.PoiUtil;

import java.util.List;


public class Excel {
	
	private XSSFWorkbook workbook;
	private int fieldLine;
	private int recordLine;
	private int firstRowNo;
	private int lastRowNo;
	private String sheetName;
	private List<List<String>> content;
	
	public Excel(String path) {
		workbook = PoiUtil.getXlsxWorkbookByFile(path);
	}

	public Excel(String path, String sheetName) {
		workbook = PoiUtil.getXlsxWorkbookByFile(path);
		this.sheetName = sheetName;
	}
	
	public Excel() {}

	public ExcelCell getCell(int row, int col) {
		if (sheetName.isBlank()) {
			sheetName = PoiUtil.getSheetName(workbook, 0);
		}
		String value = PoiUtil.readCell(workbook, sheetName, row, col);
		ExcelCell excelCell = new ExcelCell();
		excelCell.setRow(row);
		excelCell.setCol(col);
		excelCell.setValue(value);
		return excelCell;
	}
	
	public List<String> getRow(int rowNum) {
		return PoiUtil.readRow(workbook, sheetName, rowNum);
	}

	public String getCellValue(int row, int col) {
		return PoiUtil.readCell(workbook, sheetName, row, col);
	}
	
	public List<List<String>> getContentList() {
		this.content = PoiUtil.readAll(workbook, sheetName);
		return content;
	}
	
	public XSSFWorkbook getWorkbook() {
		return workbook;
	}
	public void setWorkbook(XSSFWorkbook workbook) {
		this.workbook = workbook;
	}

	public int getFieldLine() {
		return fieldLine;
	}

	public void setFieldLine(int fieldLine) {
		this.fieldLine = fieldLine;
	}

	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public int getFirstRowNo() {
		return firstRowNo;
	}

	public int getLastRowNo() {
		return lastRowNo;
	}

	public int getRecordLine() {
		return recordLine;
	}

	public void setRecordLine(int recordLine) {
		this.recordLine = recordLine;
	}

	public void setContent(List<List<String>> content) {
		this.content = content;
	}
	
	
}
