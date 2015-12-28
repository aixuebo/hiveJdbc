package maming.udf;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.hadoop.io.Text;
import org.junit.Test;

public class GenericUDFSentencesTest {

	@Test
	public void test1(){
		 Locale locale = null;
		 locale = Locale.getDefault();
		 
		    Text chunk = (Text) new Text("sdddd bb dd asws,ccc\r\ndd我们的生,活");
		    String text = chunk.toString();
		    ArrayList<ArrayList<Text> > result = new ArrayList<ArrayList<Text> >();

		    // Parse out sentences using Java's text-handling API
		    BreakIterator bi = BreakIterator.getSentenceInstance(locale);
		    bi.setText(text);
		    int idx = 0;
		    while(bi.next() != BreakIterator.DONE) {
		      String sentence = text.substring(idx, bi.current());
		      idx = bi.current();
		      result.add(new ArrayList<Text>());

		      // Parse out words in the sentence
		      BreakIterator wi = BreakIterator.getWordInstance(locale);
		      wi.setText(sentence);
		      int widx = 0;
		      ArrayList<Text> sent_array = result.get(result.size()-1);
		      while(wi.next() != BreakIterator.DONE) {
		        String word = sentence.substring(widx, wi.current());
		        widx = wi.current();
		        if(Character.isLetterOrDigit(word.charAt(0))) {
		          sent_array.add(new Text(word));
		        }
		      }
		    }
		    
		    System.out.println(result.toString());
		    
	}
}
