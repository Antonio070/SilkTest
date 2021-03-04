package Cotizacion;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
	
	private XSSFWorkbook workbook;
	private XSSFSheet curSheet;
	private String dateFormatStr = "d/mmm/yy";
	private SimpleDateFormat dateFormat;
	private String file_name;
	private FileInputStream file;
	private int header_row = -1;
	private CellStyle generalStyle;
	private CellStyle numberStyle;
	private CellStyle dateStyle;

	/**
	 * Opens the specified Excel file.
	 * @param xlsx_file The Excel Workbook to open (defaults to sheet 0).
	 */
	public ExcelUtils(String xlsx_file){
		file_name = xlsx_file;
		openWorkbook();			
				
		//Default to sheet 0
		curSheet = workbook.getSheetAt(0);
		
		generalStyle = this.getGeneralCellStyle();
		numberStyle = this.getNumberCellStyle();
		dateStyle = this.getDateCellStyle();
	}
	
	/**
	 * Opens an Excel workbook
	 */
	private void openWorkbook(){
		try{
			file = new FileInputStream(new File(file_name));		
			workbook = new XSSFWorkbook(file);
			file.close();
		}	
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Select a specific sheet within the workbook.
	 * @param index The index of the sheet.
	 */
	public void selectSheet(int index){
		curSheet = workbook.getSheetAt(index);
	}	
	
	/**
	 * Select a specific sheet within the workbook by the sheet name.
	 * @param name The name of the sheet.
	 */
	public void selectSheet(String name){
		curSheet = workbook.getSheet(name);
	}
	
	/**
	 * Returns the number of rows in the Excel workbook.
	 * @return The number of rows.
	 */
	public int getRowCount(){				
		return curSheet.getPhysicalNumberOfRows();
	}
	
	/**
	 * Returns the number of columns based on row 0
	 * @return The number of columns.
	 */
	public int getColumnCount(){
		return curSheet.getRow(header_row).getPhysicalNumberOfCells();
	}
	
	/**
	 * Returns the value of the cell at row,col as a string
	 * @param row The row number of the cell
	 * @param col The column number of the cell
	 * @return The string value of the cell.
	 */
	public String getCellValueAsString(int row, int col){
		String cellValue = "";
		
		XSSFRow curRow = curSheet.getRow(row);
		XSSFCell cell = curRow.getCell(col);
		
		if(cell != null){
			//In future versions of Apache POI, getCellType will return the enum CellType
			CellType type = cell.getCellTypeEnum();
			
			switch(type){
				case STRING: cellValue = cell.getStringCellValue();break;
				case NUMERIC: if(DateUtil.isCellDateFormatted(cell)){
					if(dateFormat == null){
						cellValue = cell.getDateCellValue().toString();
					}
					else{
						cellValue = dateFormat.format(cell.getDateCellValue());
					}
				}
				else{
					cellValue = ""+cell.getNumericCellValue();
				};break;
				case BOOLEAN: cellValue = ""+cell.getBooleanCellValue();break;
				case ERROR: cellValue = cell.getErrorCellString();break;
				case FORMULA: cellValue = workbook.getCreationHelper().createFormulaEvaluator().evaluate(cell).getStringValue();break;
				case BLANK: cellValue = "";break;
				case _NONE: cellValue = "";break;
			}
		}
		
		return 	cellValue;
	}
	
	
	/**
	 * Returns the values in a row.
	 * @param row The row number values to retrieve.
	 * @return String array containing the row values.
	 */
	public String[] getRowStringValues(int row){
		int colCount = getColumnCount();
		int rowCount = getRowCount();
		
		String [] rowValues = new String[colCount];
		
		if(row < rowCount){
			for(int i=0; i<colCount; i++){
				rowValues[i] = getCellValueAsString(row, i);
			}
		}
		
		
		return rowValues;
	}
	
	/**
	 * Returns the sheet data.
	 * @return ArrayList containing all the cell data of the currently selected sheet.
	 */
	public ArrayList<String[]> getSheetDataAsString(){
		ArrayList<String[]> sheetData = new ArrayList<String[]>();
		
		int rowCount = getRowCount();
		
		for(int i=header_row+1;i<rowCount;i++){
			sheetData.add(getRowStringValues(i));
		}
		
		return sheetData;
		
	}
	
	/**
	 * Sets the date format that should be returned from DATE cells
	 * @param format The date format, e.g. dd-MM-YYYY
	 */
	public void setDateFormat(String format){
		dateFormatStr = format;
		dateFormat = new SimpleDateFormat(format);
	}
	
	
	/**
	 * Creates an empty row at the end of the workbook
	 */
	public void addRow(){
		curSheet.createRow(getRowCount());
	}
	
	/**
	 * Adds a row to the Excel document with the specified values
	 * @param row_values The values of the row.
	 */
	public void addRow(Object... row_values){
		String data_type;
		addRow();
		int row = getRowCount();
		for(int i=0; i<row_values.length; i++){
			data_type = row_values[i].getClass().getTypeName();
			
			switch(data_type){
				case "java.lang.String": setCellValue(row-1, i, (String)row_values[i]);break;
				case "java.util.Date":  setCellValue(row-1, i, (Date)row_values[i]);break;
				case "java.lang.Integer":  setCellValue(row-1, i, (Integer)row_values[i]);break;
				case "java.lang.Double":  setCellValue(row-1, i, (Double)row_values[i]);break;
				case "java.lang.Boolean":  setCellValue(row-1, i, (Boolean)row_values[i]);break;
				case "org.apache.poi.ss.usermodel.RichTextString":  setCellValue(row-1, i, (RichTextString)row_values[i]);break;
				case "java.util.Calendar":  setCellValue(row-1, i, (Calendar)row_values[i]);break;
				default: setCellValue(row-1, i, row_values[i].toString());
			}
		}
	}
	
	/**
	 * Returns a row from the Excel workbook.
	 * @param row The row number to return.
	 * @return The Excel row.
	 */
	public XSSFRow getRow(int row){
		return curSheet.getRow(row);
	}
	
	/**
	 * Sets the string value of cell
	 * @param row The row number of the cell
	 * @param col The column number of the cell
	 * @param value The string value to be passed to the cell.
	 */
	public void setCellValue(int row, int col, String value){
		XSSFRow _row = getRow(row);
		XSSFCell _cell = _row.getCell(col);
		if(_cell == null){
			_row.createCell(col);
			_cell = _row.getCell(col);
			_cell.setCellType(CellType.STRING);
		}
		_cell.setCellValue(value);
		_cell.setCellStyle(generalStyle);
	}
	
	/**
	 * Sets the Date value of cell
	 * @param row The row number of the cell
	 * @param col The column number of the cell
	 * @param value The Date value to be passed to the cell.
	 */
	public void setCellValue(int row, int col, Date value){
		XSSFRow _row = getRow(row);
		XSSFCell _cell = _row.getCell(col);		
		if(_cell == null){
			_row.createCell(col);
			_cell = _row.getCell(col);			
			_cell.setCellType(CellType.NUMERIC);
			_cell.setCellStyle(dateStyle);
			
		}
		_cell.setCellValue(value);
	}
	
	
	/**
	 * Sets the double value of cell
	 * @param row The row number of the cell
	 * @param col The column number of the cell
	 * @param value The double value to be passed to the cell.
	 */
	public void setCellValue(int row, int col, double value){
		XSSFRow _row = getRow(row);
		XSSFCell _cell = _row.getCell(col);
		if(_cell == null){
			_row.createCell(col);			
			_cell = _row.getCell(col);
			_cell.setCellType(CellType.NUMERIC);						
		}
		_cell.setCellValue(value);
		_cell.setCellStyle(numberStyle);
	}
	
	/**
	 * Sets the boolean value of cell
	 * @param row The row number of the cell
	 * @param col The column number of the cell
	 * @param value The boolean value to be passed to the cell.
	 */
	public void setCellValue(int row, int col, boolean value){
		XSSFRow _row = getRow(row);
		XSSFCell _cell = _row.getCell(col);
		if(_cell == null){
			_row.createCell(col);
			_cell = _row.getCell(col);
			_cell.setCellType(CellType.BOOLEAN);
		}
		_cell.setCellValue(value);		
	}
	
	/**
	 * Sets the RichTextString value of cell
	 * @param row The row number of the cell
	 * @param col The column number of the cell
	 * @param value The RichTextString value to be passed to the cell.
	 */
	public void setCellValue(int row, int col, RichTextString value){
		XSSFRow _row = getRow(row);
		XSSFCell _cell = _row.getCell(col);
		if(_cell == null){
			_row.createCell(col);
			_cell = _row.getCell(col);
			_cell.setCellType(CellType.STRING);
		}
		_cell.setCellValue(value);		
	}
	
	/**
	 * Sets the Calendar value of cell
	 * @param row The row number of the cell
	 * @param col The column number of the cell
	 * @param value The Calendar value to be passed to the cell.
	 */
	public void setCellValue(int row, int col, Calendar value){
		XSSFRow _row = getRow(row);
		XSSFCell _cell = _row.getCell(col);
		if(_cell == null){
			_row.createCell(col);
			_cell = _row.getCell(col);
			_cell.setCellType(CellType.NUMERIC);
		}
		_cell.setCellValue(value);	
		_cell.setCellStyle(dateStyle);
	}
	
	/**
	 * Sets the header row
	 * @param row The row number for the column headers.
	 */
	public void setHeaderRow(int row){
		header_row = row;
	}
	
	/**
	 * Returns the header column text.
	 * @param col The column text to retrieve.
	 * @return The header text value.
	 */
	public String getHeaderText(int col){
		return curSheet.getRow(header_row).getCell(col).getStringCellValue();
	}
	
	/**
	 * Returns the sheet name at the specified index
	 * @param sheetNum The sheet index.
	 * @return The name of the sheet.
	 */
	public String getSheetName(int sheetNum){
		return workbook.getSheetName(sheetNum);
	}
		
	
	/**
	 * Saves any changes to the workbook.
	 */
	public void save(){
		try{
			FileOutputStream out = new FileOutputStream(new File(file_name));
			workbook.write(out);
			out.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private CellStyle getDateCellStyle(){
		CellStyle cellStyle = workbook.createCellStyle();
		CreationHelper createHelper = workbook.getCreationHelper();
		cellStyle.setDataFormat(
		    createHelper.createDataFormat().getFormat(dateFormatStr));
		return cellStyle;
	}
	
	private CellStyle getNumberCellStyle(){
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setDataFormat(
		    (short)BuiltinFormats.getBuiltinFormat("#,##0.0000"));
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		return cellStyle;
	}
	
	
	private CellStyle getGeneralCellStyle(){
		CellStyle cellStyle = workbook.createCellStyle();		
		cellStyle.setDataFormat(
		    (short)BuiltinFormats.getBuiltinFormat("General"));		
		return cellStyle;
	}
	/**
	 * Closes the Excel workbook
	 */
	public void closeWorkbook(){
		try{			
			workbook.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

}
