package com.bigdata.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;


public abstract class ReadFile {

    private String encoding;

    private String filePath;

    protected boolean isTrim = true;

    protected boolean isGz = false;//读取的数据源是否为gz压缩文件

    public ReadFile(){
      this.encoding = "UTF-8";
      this.init();
    }
    public ReadFile(String filePath, String encoding) {
        this.encoding = encoding;
        this.filePath = filePath;
        this.init();
    }

    public ReadFile(String filePath) {
        this.encoding = "UTF-8";
        this.filePath = filePath;
        this.init();
    }

    public void start() throws RuntimeException {
        //System.out.println("start:"+this.filePath);
        InputStream is = null;
        try {
            is = new FileInputStream(this.filePath);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        BufferedReader br = null;
        try {
            parseBefore();
            if (isGz) {//读取压缩文件
                br = new BufferedReader(new InputStreamReader(new GZIPInputStream(is), this.encoding), Constants.BUFFER_SIZE);
            } else {//读取非压缩文件
                br = new BufferedReader(new InputStreamReader(is, this.encoding), Constants.BUFFER_SIZE);
            }
            String theLine = null;
            do {
                theLine = br.readLine();
                if (theLine != null && !"".equals(theLine.trim())) {
                    try {
                        if (isTrim) {
                            parse(theLine.trim());
                        } else {
                            parse(theLine);
                        }
                    } catch (Exception ex) {
                    	ex.printStackTrace();
                    }
                }
            } while (theLine != null);
            parseEnd();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void parseBefore() {

    }

    public void parseEnd() {

    }

    public void init() {

    }

    public abstract void parse(String line);

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isTrim() {
        return isTrim;
    }

    public void setTrim(boolean isTrim) {
        this.isTrim = isTrim;
    }

    public boolean isGz() {
        return isGz;
    }

    public void setGz(boolean isGz) {
        this.isGz = isGz;
    }

}
