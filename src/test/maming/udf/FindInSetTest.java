package maming.udf;


public class FindInSetTest {

	  static int result = -1;
	  public static int evaluate(String s, String txtarray) {
		    if (s == null || txtarray == null) {
		      return -1;
		    }

		    byte[] search_bytes = s.getBytes();

		    for (int i = 0; i < s.length(); i++) {
		      if (search_bytes[i] == ',') {
		        result = 0;
		        return result;
		      }

		    }

		    byte[] data = txtarray.getBytes();
		    int search_length = s.length();

		    int cur_pos_in_array = 0;
		    int cur_length = 0;
		    boolean matching = true;

		    for (int i = 0; i < txtarray.length(); i++) {
		      if (data[i] == ',') {
		        cur_pos_in_array++;
		        if (matching && cur_length == search_length) {
		          result = cur_pos_in_array;
		          return result;
		        } else {
		          matching = true;
		          cur_length = 0;
		        }
		      } else {
		        if (cur_length + 1 <= search_length) {
		          if (!matching || search_bytes[cur_length] != data[i]) {
		            matching = false;
		          }
		        } else {
		          matching = false;
		        }
		        cur_length++;
		      }

		    }

		    if (matching && cur_length == search_length) {
		      cur_pos_in_array++;
		      result = cur_pos_in_array;
		      return result;
		    } else {
		      result = 0;
		      return result;
		    }
		  }
	  
	  public static void main(String[] args) {
		  String s = "null";
		  String txtarray = "abc,b,ab,c,def";
		  System.out.println(FindInSetTest.evaluate(s, txtarray));
	  }
}
