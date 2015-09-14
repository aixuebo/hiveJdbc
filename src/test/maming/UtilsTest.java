package maming;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class UtilsTest {

	@Test
	public void testUrl(){
		
	    String url = "mysql://rdsmmee6zmmee6z.mysql.rds.aliyuncs.com:3306/passport;aa=bb?rewriteBatchedStatements=true&amp;useUnicode=true&amp;characterEncoding=utf8";
	    URI jdbcURI = URI.create(url);
	    
	    System.out.println(jdbcURI.getPath());
	    System.out.println(jdbcURI.getQuery());
	}
	
	final public static char ESCAPE_CHAR = '\\';
	@Test
	public void test2(){
		System.out.println(System.getProperty("java.io.tmpdir"));
		
		File parent = new File("E://tmp");
	    try {
			File tmpFile = File.createTempFile("prefix", ".pipeout", parent);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test3(){

		List<String> cols = new ArrayList<String>();

		cols.add("aaa");
		cols.add("bbb");
		cols.add("ccc");
		
	    if (cols != null) {
	        String old = "";
	        StringBuilder result = new StringBuilder(old);
	        boolean first = old.isEmpty();
	        for(String col: cols) {
	        	System.out.println(first);
	          if (first) {
	            first = false;
	          } else {
	            result.append(',');
	          }
	          result.append(col);
	        }
	        System.out.println(result.toString());
	      }
	}
}
