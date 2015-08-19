package maming;

import java.net.URI;

import org.junit.Test;

public class UtilsTest {

	@Test
	public void testUrl(){
		
	    String url = "mysql://rdsmmee6zmmee6z.mysql.rds.aliyuncs.com:3306/passport;aa=bb?rewriteBatchedStatements=true&amp;useUnicode=true&amp;characterEncoding=utf8";
	    URI jdbcURI = URI.create(url);
	    
	    System.out.println(jdbcURI.getPath());
	    System.out.println(jdbcURI.getQuery());
	}
}
