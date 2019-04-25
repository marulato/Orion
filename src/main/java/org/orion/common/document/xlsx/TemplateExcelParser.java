package org.orion.common.document.xlsx;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.orion.common.miscutil.ArrayUtil;
import org.orion.common.miscutil.Nullable;
import org.orion.common.miscutil.PoiUtil;
import org.orion.common.miscutil.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TemplateExcelParser {

	private XSSFWorkbook workbook;
	private Excel excel;
	private String sheetName;
	private int titleLine;
	private int recordLine;
	private List<List<String>> inputRows;
	private List<String> titles;
	private List<String> originalTitles;
	private final Logger logger = LogManager.getLogger(TemplateExcelParser.class);
	public TemplateExcelParser(){}
	
	public TemplateExcelParser(Excel excel) {
		if (excel == null) {
			return;
		}
		workbook = excel.getWorkbook();
		titleLine = excel.getFieldLine();
		recordLine = excel.getRecordLine();
		init();
	}

	
	private void init() {
		if (StringUtil.isEmpty(sheetName)) {
			sheetName = PoiUtil.getSheetName(workbook, 0);
		}
		
		inputRows = new ArrayList<List<String>>();
		try {
			originalTitles = PoiUtil.readRow(workbook, sheetName, titleLine);
			titles = convertTitle(originalTitles);
			for (int i = recordLine; i <= PoiUtil.getLastRow(workbook, sheetName); i++) {
				List<String> row = null;
				row = PoiUtil.readRow(workbook, sheetName, i);
				inputRows.add(row);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	
	}
	
	public <T> List<T> getMappedEntity(Class<T> target, Map<String, String> rowMapper) throws Exception {
		List<T> entityList = null;
		if (target != null) {
			entityList = new ArrayList<>();
			Field[] fieldsArray = target.getDeclaredFields();
			Method[] methodsArray = target.getDeclaredMethods();
			String[] allFieldsNames = new String[fieldsArray.length];
			for (int i = 0; i < fieldsArray.length; i++) {
				allFieldsNames[i] = fieldsArray[i].getName();
			}
			String[] fieldNamesArray = null;
			String[] methodNamesArray = null;
			if (!Nullable.isNull(rowMapper)) {
				if (rowMapper.size() > originalTitles.size()) {
					throw new IllegalArgumentException("RowMapper Size out of Range -> Size: " + rowMapper.size());
				}
				fieldNamesArray = new String[rowMapper.size()];
				int index = 0;
				for (int i = 0; i < originalTitles.size(); i++) {
					String name = rowMapper.get(originalTitles.get(i));
					if (ArrayUtil.contains(allFieldsNames, name)) {
						fieldNamesArray[index ++] = name;
					}
				}
				if (index < fieldNamesArray.length) {
					fieldNamesArray = ArrayUtil.get(fieldNamesArray, 0, index - 1);
				}
				methodNamesArray = new String[fieldNamesArray.length];
				for (int k = 0; k < fieldNamesArray.length; k++) {
					methodNamesArray[k] = "set" + StringUtil.toUpperCaseByIndex(fieldNamesArray[k], 0);
				}
			} else {
				fieldNamesArray = ArrayUtil.toStringArray(titles);
				methodNamesArray = new String[fieldNamesArray.length];
				for (int k = 0; k < fieldNamesArray.length; k++) {
					methodNamesArray[k] = "set" + StringUtil.toUpperCaseByIndex(fieldNamesArray[k], 0);
				}
			}
			if (!Nullable.isNull(inputRows)) {
				for (List<String> singleRow : inputRows) {
					T object = target.newInstance();
					for (int i = 0; i < fieldNamesArray.length; i++) {
						Method setter = target.getDeclaredMethod(methodNamesArray[i], String.class);
						if (setter != null) {
							setter.setAccessible(true);
							setter.invoke(object, singleRow.get(i));
						} else {
							Field field = target.getDeclaredField(fieldNamesArray[i]);
							if (field != null) {
								field.setAccessible(true);
								field.set(object, singleRow.get(i));
							}
						}
					}
					entityList.add(object);
				}
				logger.debug("Mapped "+ inputRows.size() +" Rows Successfully");
				return entityList;
			}
		}
		return entityList;
	}
	
	private List<String> convertTitle(List<String> originTitles) {
		List<String> titles = new ArrayList<String>();
		if (originTitles != null && originTitles.size() != 0) {
			for (String title : originTitles) {
				titles.add(StringUtil.convertFieldName(title));
			}
		}
		return titles;
	}

	public List<List<String>> getInputRows() {
		return inputRows;
	}

	public Excel getExcel() {
		return excel;
	}

	public void setExcel(Excel excel) {
		this.excel = excel;
	}
	
	
}
