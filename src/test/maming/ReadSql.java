package maming;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


public class ReadSql extends ReadFile{

	public ReadSql(String path){
		super(path,"UTF-8");
	}
	
	
	private Map<String,String> map = new HashMap<String,String>();
	
	StringBuffer sb = new StringBuffer();
	
	Set<String> set = new TreeSet<String>();
	boolean ofei = true;
	
	int weixin = 0;
	int zone = 0;
	public boolean flag = false;
	@Override
	public void parse(String line) {
	  weixin++;
      if(set.add(line.split("\t")[3])){
        //sb.append(",'").append(line).append("'");
        sb.append(",").append(line);
      }
	}
	
    @Override
    public void parseEnd() {
    /*	mergeFileIoWriter.close();
    	Map<String,Long> sortMap = new TreeMap<String,Long>();
    	for(String date:cpcDownLoadMap.keySet()){
    	  sortMap.put(date,cpcDownLoadMap.get(date));
    	  //System.out.println(date+"=="+cpcDownLoadMap.get(date));
    	}
    	
    	System.out.println("ssss");
        for(String date:sortMap.keySet()){
          System.out.println(date+"=="+cpcDownLoadMap.get(date));
        }*/
      System.out.println(sb.toString());
      //System.out.println(set);
    }
    
	public static void main(String[] args) {
		String path = "E:\\tmp\\mobileOrder.log";
		ReadSql test = new ReadSql(path);
		test.start();
	}
}
