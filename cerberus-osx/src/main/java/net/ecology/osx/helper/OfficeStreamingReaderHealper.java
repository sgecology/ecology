/**
 * 
 */
package net.ecology.osx.helper;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.monitorjbl.xlsx.StreamingReader;

import lombok.Builder;
import net.ecology.common.CollectionsUtility;
import net.ecology.common.CommonUtility;
import net.ecology.global.GlobeConstants;
import net.ecology.model.osx.OSXConstants;
import net.ecology.model.osx.OSXWorkbook;
import net.ecology.model.osx.OSXWorksheet;
import net.ecology.osx.exceptions.OsxException;
import net.ecology.osx.model.DmxWorkbook;
import net.ecology.osx.model.DmxWorksheet;

/**
 * @author ducbq
 *
 */
@Builder
public class OfficeStreamingReaderHealper {
	/**
	 * 
	 */
	public OSXWorkbook readXlsx(Map<?, ?> parameters) throws OsxException {
		InputStream inputStream = null;
		Workbook workbook = null;

		OSXWorksheet worksheet = null;
		OSXWorkbook dataWorkbook = OSXWorkbook.builder().build();
		try {
			inputStream = (InputStream)parameters.get(OSXConstants.INPUT_STREAM);
			if (parameters.containsKey(OSXConstants.ENCRYPTED_KEYS)) {
				workbook = StreamingReader.builder()
						.rowCacheSize(100)
						.bufferSize(4096)
						.password((String)parameters.get(OSXConstants.ENCRYPTED_KEYS))
						.open(inputStream);
			} else {
				workbook = StreamingReader.builder()
						.rowCacheSize(100)
						.bufferSize(4096)
						.open(inputStream);
			}

			for (Sheet sheet : workbook) {
				if (!isValidSheet(sheet, parameters))
					continue;

				worksheet = buildDataWorksheet(sheet);
				dataWorkbook.put(worksheet.getId(), worksheet);
			}
		} catch (Exception e) {
			throw new OsxException(e);
		}
		return dataWorkbook;
	}

	/**
	 * 
	 */
	public DmxWorkbook loadXlsxData(
			String workbookId, 
			InputStream workbookInputStream, 
			String encryptionKey, 
			String[] worksheetIds) throws OsxException {
		Workbook workbook = null;
		DmxWorksheet worksheet = null;
		DmxWorkbook dataWorkbook = DmxWorkbook.builder().build();
		List<String> processingWorksheetIds = null;
		try {
			if (CommonUtility.isNotEmpty(encryptionKey)) {
				workbook = StreamingReader.builder()
						.rowCacheSize(100)
						.bufferSize(4096)
						.password(encryptionKey)
						.open(workbookInputStream);
			} else {
				workbook = StreamingReader.builder()
						.rowCacheSize(100)
						.bufferSize(4096)
						.open(workbookInputStream);
			}

			if (null != worksheetIds){
				processingWorksheetIds = CollectionsUtility.arraysAsList(worksheetIds);
				for (Sheet sheet : workbook) {
					if (!processingWorksheetIds.contains(sheet.getSheetName()))
						continue;

					worksheet = transformWorksheet(sheet);
					dataWorkbook.put(worksheet.getId(), worksheet);
				}
			} else {
				for (Sheet sheet : workbook) {
					worksheet = transformWorksheet(sheet);
					dataWorkbook.put(worksheet.getId(), worksheet);
				}
			}
		} catch (Exception e) {
			throw new OsxException(e);
		}
		return dataWorkbook;
	}

	private OSXWorksheet buildDataWorksheet(Sheet sheet) throws OsxException {
		List<Object> dataRow = null;
		OSXWorksheet dataWorksheet = OSXWorksheet.builder()
				.id(sheet.getSheetName())
				.build();
		Cell currentCell = null;
		short firstCellNum = 0;
		short lastCellNum = 0;
		for (Row currentRow : sheet) {
			firstCellNum = currentRow.getFirstCellNum();
			lastCellNum = currentRow.getLastCellNum();
			break;
		}

		for (Row currentRow : sheet) {
			dataRow = CollectionsUtility.createArrayList();
			for (short idx = firstCellNum; idx <= lastCellNum; idx++) {
				currentCell = currentRow.getCell(idx);
				if (null==currentCell || CellType._NONE.equals(currentCell.getCellType()) || CellType.BLANK.equals(currentCell.getCellType())) {
					dataRow.add(GlobeConstants.STRING_BLANK);
					continue;
				} 

				if (CellType.BOOLEAN.equals(currentCell.getCellType())) {
					dataRow.add(currentCell.getBooleanCellValue());
				} else if (CellType.FORMULA.equals(currentCell.getCellType())) {
					
				} else if (CellType.NUMERIC.equals(currentCell.getCellType())) {
					if (DateUtil.isCellDateFormatted(currentCell)) {
						dataRow.add(currentCell.getDateCellValue());
					} else {
						dataRow.add(currentCell.getNumericCellValue());
					}
				} else if (CellType.STRING.equals(currentCell.getCellType())) {
					dataRow.add(currentCell.getStringCellValue());
				} else {
					dataRow.add(currentCell.toString());
				}
			}
			dataWorksheet.addDataRows(Integer.valueOf(currentRow.getRowNum()), dataRow);
		}

		return dataWorksheet;
	}

	private DmxWorksheet transformWorksheet(Sheet sheet) throws OsxException {
		List<Object> dataRow = null;
		DmxWorksheet dataWorksheet = DmxWorksheet.builder()
				.id(sheet.getSheetName())
				.build();
		Cell currentCell = null;
		short firstCellNum = 0;
		short lastCellNum = 0;
		for (Row currentRow : sheet) {
			firstCellNum = currentRow.getFirstCellNum();
			lastCellNum = currentRow.getLastCellNum();
			break;
		}

		for (Row currentRow : sheet) {
			dataRow = CollectionsUtility.createArrayList();
			for (short idx = firstCellNum; idx <= lastCellNum; idx++) {
				currentCell = currentRow.getCell(idx);
				if (null==currentCell || CellType._NONE.equals(currentCell.getCellType()) || CellType.BLANK.equals(currentCell.getCellType())) {
					dataRow.add(GlobeConstants.STRING_BLANK);
					continue;
				} 

				if (CellType.BOOLEAN.equals(currentCell.getCellType())) {
					dataRow.add(currentCell.getBooleanCellValue());
				} else if (CellType.FORMULA.equals(currentCell.getCellType())) {
					
				} else if (CellType.NUMERIC.equals(currentCell.getCellType())) {
					if (DateUtil.isCellDateFormatted(currentCell)) {
						dataRow.add(currentCell.getDateCellValue());
					} else {
						dataRow.add(currentCell.getNumericCellValue());
					}
				} else if (CellType.STRING.equals(currentCell.getCellType())) {
					dataRow.add(currentCell.getStringCellValue());
				} else {
					dataRow.add(currentCell.toString());
				}
			}
			dataWorksheet.put(Integer.valueOf(currentRow.getRowNum()), dataRow);
		}
		return dataWorksheet;
	}

	/**
	 * True if no sheet id list otherwise check matched sheet id
	 */
	@SuppressWarnings("unchecked")
	private boolean isValidSheet(Sheet sheet, Map<?, ?> parameters) {
		if (!parameters.containsKey(OSXConstants.PROCESSING_WORKSHEET_IDS) || CommonUtility.isEmpty(parameters.get(OSXConstants.PROCESSING_WORKSHEET_IDS)))
			return true;

		//Map<String, List<String>> sheetIds = (Map<String, List<String>>)parameters.get(OSXConstants.PARAM_DATA_SHEET_IDS);
		List<String> sheetIds = (List<String>)parameters.get(OSXConstants.PROCESSING_WORKSHEET_IDS);
		return sheetIds.contains(sheet.getSheetName());
	}
}