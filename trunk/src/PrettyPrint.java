import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PrettyPrint {

	public static String doPretty(String intermediate, String output, String listingFile) throws IOException
	{
		File inputFile = new File(intermediate);
		File inputFileOutput = new File(output);
		boolean fileExists = inputFile.exists();
		boolean fileExistsOutput = inputFileOutput.exists();
		BufferedWriter bufferedWriter = null;
		if (fileExists == false) {
			return "The file does not exist. Try another one.";
		}
		if (fileExistsOutput == false) {
			return "The file does not exist. Try another one.";
		}


		String read = "";
		String readOutput = "";
		FileReader reader = new FileReader(intermediate);
		BufferedReader file = new BufferedReader(reader);
		FileReader readerOuput = new FileReader(output);
		BufferedReader fileOutput = new BufferedReader(readerOuput);
		bufferedWriter = new BufferedWriter(new FileWriter(listingFile.concat(".lst")));
		
		read = file.readLine();
		
		readOutput = fileOutput.readLine();
		while (read != null) {
			
			
		}

		
		return null;
	}
	
}
