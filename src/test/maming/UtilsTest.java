package maming;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.sql.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
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
		String s = "2015-7-07";
        Date result = Date.valueOf(s);
        System.out.println(result);
	}
}
