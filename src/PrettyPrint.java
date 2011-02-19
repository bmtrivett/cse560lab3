import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PrettyPrint {

	public static void doPretty(String intermediate, String listingFile) throws IOException
	{
/*		File inputFile = new File(intermediate);
		boolean fileExists = inputFile.exists();
*/		BufferedWriter bufferedWriter = null;


		String read = "";
		FileReader reader = new FileReader(intermediate);
		BufferedReader file = new BufferedReader(reader);
		bufferedWriter = new BufferedWriter(new FileWriter(listingFile.concat(".txt")));

	}
}
