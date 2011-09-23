package project.jun.doc.excel;import java.io.FileNotFoundException;import java.io.IOException;import org.apache.poi.hssf.usermodel.HSSFCellStyle;public interface HoExcelWriter {        public void setWorkbook(Object wb) throws Exception ;        public void createExel() throws FileNotFoundException, IOException ;    public Object getWorkBook() ;        public void createSheet(String sheetName) throws FileNotFoundException, IOException ;    public void createSheet() throws FileNotFoundException, IOException ;    public Object createRow(int rowIdx) throws FileNotFoundException, IOException ;    public Object createCell(int rowIdx, short cellIdx) throws FileNotFoundException, IOException ;        public void setCellValue(int rowIdx, short cellIdx, String value) throws FileNotFoundException, IOException ;     public void setCellValue(int rowIdx, short cellIdx, long value) throws FileNotFoundException, IOException ;    public void setCellValue(int rowIdx, short cellIdx, double value) throws FileNotFoundException, IOException ;    public void setCellStyleToTitle(int rowIdx, short cellIdx) throws FileNotFoundException, IOException ;    public void setCellStyleToData(int rowIdx, short cellIdx) throws FileNotFoundException, IOException ;    public void setCellStyle(int rowIdx, short cellIdx, HSSFCellStyle style) throws FileNotFoundException, IOException ;    public void setWidth(String cellName, short width) ;    public int getWidth(String cellName) ;        public void setHeight(int rowIdx, short height) ;        public void span(int rowFrom, short colFrom, int rowSpanLength, short colSpanLength) ;        public void addMergedRegion(int rowFrom, short colFrom, int rowTo, short colTo) ;    public void addMergedRegion(int rowFrom, int colFrom, int rowTo, int colTo) ;        public short getCellNum(String cellName) ;        public String getCellName(short cellNum) ;    }