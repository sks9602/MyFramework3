package project.jun.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class HoExcelReader {

	HSSFWorkbook workbook = null;
    HSSFSheet    sheet    = null;

    public void setExelFile(InputStream is) throws FileNotFoundException, IOException {
    	workbook = new HSSFWorkbook( is );
    }

    public void setExelFile(String fileName) throws FileNotFoundException, IOException {
    	workbook = new HSSFWorkbook( new FileInputStream( new File( fileName)));
    }

    public void setExelFile(File file) throws FileNotFoundException, IOException {
    	workbook = new HSSFWorkbook( new FileInputStream( file ));
    }

    public void setSheet(int sheetIdx) throws FileNotFoundException, IOException {
    	sheet = workbook.getSheetAt(sheetIdx);
    }

    public HSSFRow getRow(int rowIdx) throws FileNotFoundException, IOException {
    	return sheet.getRow(rowIdx);
    }

    public HSSFCell getCell(int rowIdx, short cellIdx) throws FileNotFoundException, IOException {
    	return getRow(rowIdx).getCell(cellIdx);
    }
    
    public int getLastRowNum() {
    	return sheet.getLastRowNum();
    }

    public int getLastRowNum(int sheetIdx) throws FileNotFoundException, IOException {
    	return workbook.getSheetAt(sheetIdx).getLastRowNum();
    }

    public int getLastCellNum(int rowIdx) throws FileNotFoundException, IOException {
    	return getRow(rowIdx).getLastCellNum();
    }

    public int getLastCellNum(int sheetIdx, int rowIdx) throws FileNotFoundException, IOException {
    	return workbook.getSheetAt(sheetIdx).getRow(rowIdx).getLastCellNum();
    }

    public HSSFRichTextString getValueRichTextString (int rowIdx, short cellIdx  ) throws FileNotFoundException, IOException {
    	return getCell(rowIdx, cellIdx) == null ? new HSSFRichTextString() : getCell(rowIdx, cellIdx).getRichStringCellValue();
    }

    public String getStringValue (int rowIdx, short cellIdx  ) throws FileNotFoundException, IOException {
    	return getCell(rowIdx, cellIdx) == null ? "" : getCell(rowIdx, cellIdx).getRichStringCellValue().getString();
    }

    public double getDoubleValue (int rowIdx, short cellIdx  ) throws FileNotFoundException, IOException {
    	return getCell(rowIdx, cellIdx) == null ? 0 : getCell(rowIdx, cellIdx).getNumericCellValue();
    }
 
    public Date getDateValue (int rowIdx, short cellIdx  ) throws FileNotFoundException, IOException {
    	return getCell(rowIdx, cellIdx) == null ? new Date() : getCell(rowIdx, cellIdx).getDateCellValue();
    }

    public Object getValue( int rowIdx, short cellIdx ) throws FileNotFoundException, IOException {
    	Object obj = null;
    	try {
    		if( getCell(rowIdx, cellIdx).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
	    		obj = HoUtil.toNumberRawFormat(new Double(getDoubleValue(rowIdx, cellIdx)));
	    		
	        } 
	    	else {
	    		obj = getStringValue(rowIdx, cellIdx);
	        }
    	} catch(NullPointerException e) 
    	{
    		obj = new String();
    	}
    	return obj;
    }
    
    public short getCellNum(String cellName) {
    	short cellNum = 0;
    	for( int k=0 ; k<cellName.length() ; k++) {
    		cellNum += ((k*25)+ ( k==0 ? 0 : 1 ) + ((int) cellName.charAt(k)) - 65);
    	}
    	return cellNum;
    }
}
