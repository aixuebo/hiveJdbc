package maming.udf;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.junit.Test;

public class JsonTest {
	  
	@Test
	public void test1() throws JsonParseException, JsonMappingException, IOException{
		
		  final JsonFactory JSON_FACTORY = new JsonFactory();
		  JSON_FACTORY.enable(Feature.ALLOW_UNQUOTED_CONTROL_CHARS);
		  final ObjectMapper MAPPER = new ObjectMapper(JSON_FACTORY);
		  final JavaType MAP_TYPE = TypeFactory.fromClass(Map.class);
		  
		//{a={c=cc, d=dd}, n=bb}
		String jsonString = "{\"a\":{\"c\":\"cc\",\"d\":\"dd\"},\"n\":\"bb\"}";
	    Object extractObject = MAPPER.readValue(jsonString, MAP_TYPE);
	    System.out.println(extractObject);
	}
	
	//以字母、数字、_、-、:开头的都匹配
	  private final Pattern patternKey = Pattern.compile("^([a-zA-Z0-9_\\-\\:\\s]+).*");
	  private final Pattern patternIndex = Pattern.compile("\\[([0-9]+|\\*)\\]");
	  
	@Test
	public void test2(){
		String str = "[*]";
		System.out.println(patternIndex.matcher(str).matches());
	}
	
}
