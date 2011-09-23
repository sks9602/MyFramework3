package project.jun.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

/**
 * 디렉토리나 파일압축 해제를 지원하는 클래스.
 * @author hwanggu
 *
 */
public class HoZip {

    /**
     * zip file to extract
     */
    private File srcFile;

    /**
     * Extract directory
     */
    private File tgtFile;

    private String srcParent;
  
	protected  Logger          logger     = Logger.getRootLogger();

    public HoZip(){

    }
    
    public HoZip(String sourcePath) {
        setSourcePath(sourcePath);
    }
    
    public HoZip(String sourcePath, String targetPath) {
        setSourcePath(sourcePath);
        setTargetPath(targetPath);
    }

    
    public void setSourcePath(String srcPath) {
        this.srcFile = new File(srcPath);
    }
    
    public void setSourcePath(File srcFile) {
        this.srcFile = srcFile;
    }

    public String getSourcePath(){
        if(this.srcFile != null){
            return this.srcFile.getPath();
        }
        return null;
    }

    public void setTargetPath(String targetPath) {
        this.tgtFile = new File(targetPath);
    }
    
    public void setTargetPath(File tgtFile) {
        this.tgtFile = tgtFile;
    }
    
    public String getTargetPath(){
        if(this.tgtFile != null){
            return this.tgtFile.getPath();
        }
        return null;
    }

    private String filterPath(String s){
        char tok = 0;
        if(s != null){
            tok = ('/' != File.separatorChar)?'/':'\\';
            return s.replace(tok, File.separatorChar);
        }
        return null;
    }
    

    public String zipDisplay() throws IOException {
        
        if(srcFile == null) {
            return null;
        }
        
        StringBuffer buf = new StringBuffer();
        ZipFile archive = new ZipFile(srcFile);
        for (Enumeration entries = archive.entries(); entries.hasMoreElements();) {
            buf.append(((ZipEntry) entries.nextElement()).getName());
            buf.append('\n');
        }
        return buf.toString();
    }
    
    /**
     * 압축파일 목록을 반환
     * @return 압축파일목록
     * @throws IOException
     */
    public List zipList() throws IOException{
        if(srcFile == null) {
            return null;
        }
        
        List zipLst = new ArrayList();
        ZipFile archive = new ZipFile(srcFile);
        for (Enumeration entries = archive.entries(); entries.hasMoreElements();) {
            zipLst.add(((ZipEntry) entries.nextElement()).getName());
        }
        
        return zipLst;
    }

    /**
     * 소스경로에 주어진 압축파일을 타켓경로에 압축해제.<br>
     * 타켓경로가 주어지지 않으면 현재 . 소스디렉토리에 해체
     *
     * @return void
     * @throws IOException 
     * @throws ZipException 
     *
     */
    public void extract() throws ZipException, IOException {
        
        String fileName = null;
        OutputStream out = null;
        byte[] buffer = new byte[16384];
        ZipInputStream archive = null;
        
        try{
            if(tgtFile == null){
                tgtFile = srcFile.getParentFile();
            } else if(tgtFile.exists()){
                if(!tgtFile.isDirectory()){
                    throw new IOException("exists target path:"+tgtFile.getAbsolutePath());
                }
            } else {
                tgtFile.mkdirs();
            }
            
            
            archive = new ZipInputStream(new FileInputStream(srcFile));
            ZipEntry entry = null;
            while ((entry = archive.getNextEntry()) != null ) {
                //get the next entry in the archive
                fileName = filterPath(entry.getName());
                File unzipFile = new File(tgtFile, fileName);
                logger.info("ext entry : "+ unzipFile.getAbsolutePath());
                
                if (entry.isDirectory()) {
                    if(!unzipFile.exists()){
                        unzipFile.mkdirs();
                    }
                } else {
                    //create the destination path, if needed
                    String parent = unzipFile.getParent();
                    if (parent != null) {
                        File parentFile = new File(parent);
                        if (!parentFile.exists()) {
                            parentFile.mkdirs();
                        }
                        parentFile = null;
                    }
    
                    //open a stream to the destination file
                    out = new FileOutputStream(unzipFile);
    
                    //Repeat reading into buffer and writing buffer to file,
                    //until done.  Count will always be # bytes read, until
                    //EOF when it is -1.
                    int count;
                    while ((count = archive.read(buffer)) != -1) {
                        out.write(buffer, 0, count);
                    }
                    out.flush();
                    out.close();
                    out = null;
                }
            }
        } finally{            
            if(out != null){
                out.close();
            }
            
            if(archive != null){
                archive.close();
            }
        }
    }


    /**
     * 소스경로로 주어진 디렉토리나 파일을 타켓이름으로 압축<br>
     * 타켓경로가 없다면 소스파일이름으로 압축파일명 생성.<br>
     */
    public void compress() throws IOException{
        ZipOutputStream out = null;
        
        try{
            if(tgtFile == null || tgtFile.isDirectory()){
                String fileName = stripExt(srcFile.getName());
                String basePath = (tgtFile == null)?srcFile.getParent():tgtFile.getPath();
                tgtFile = new File(basePath+File.separatorChar+fileName+".zip");
            } else {
                File parentFile = tgtFile.getParentFile();
                if (parentFile != null && !parentFile.exists()) {
                    parentFile.mkdirs();
                    parentFile = null;
                }
            }
            
            if(srcFile.isFile()){
                srcParent = srcFile.getParent();
            } else {
                srcParent = srcFile.getPath();
            }
            out = new ZipOutputStream(new FileOutputStream(tgtFile));
            saveEntry(srcFile, out);
        }finally{
            if(out != null){
                out.close();
            }
        }
        
    }
    
    private void saveEntry(File entyFile, ZipOutputStream out) throws IOException {
        FileInputStream in = null;
        byte[] buffer = new byte[16384];
        String entyPath = null;
        
        logger.info("comp entry : " + entyFile.getAbsolutePath());
        
        if(entyFile.isDirectory()){
            File[] srcFiles = entyFile.listFiles();
            for(int i=0;i<srcFiles.length;i++){
                saveEntry(srcFiles[i], out);
            }
        } else {
            entyPath = entryPath(entyFile);
            out.putNextEntry(new ZipEntry(entyPath));
            in = new FileInputStream(entyFile);
            int count;
            while ((count = in.read(buffer)) != -1) {
                out.write(buffer, 0, count);
            }
            
            out.closeEntry();
            in.close();
        }
    }
    
    private String stripExt(String fileName){
        int sp = 0;
        String retNm = fileName;
        if(retNm != null && (sp = retNm.lastIndexOf('.')) != -1){
            retNm = retNm.substring(0, sp-1);
        }
        return retNm;
    }
 

    private String entryPath(File file) throws UnsupportedEncodingException {
        int sp = 0;
        String s = null;
        String path = file.getPath();
        if((sp = path.indexOf(srcParent)) != -1){
            s = path.substring(sp+srcParent.length());
            return s;
        } else {
            return "";
        }
        
    }

    //TEST MAIN
    public static void main(String[] args) throws ZipException, IOException{
        HoZip zip = new HoZip();
        
        zip.setSourcePath("D:/임시파일/케리스LCMS구축가이드.zip");
        zip.setTargetPath("D:/임시파일/테스트");
        zip.extract();
        System.out.println("extract ok");
        
        
//        zip.setSourcePath("D:/임시파일/케리스LCMS구축가이드.hwp");
//        zip.setTargetPath("D:/임시파일/테스트2.zip");
//        zip.compress();
//        System.out.println("compress ok");
    }
    
}
