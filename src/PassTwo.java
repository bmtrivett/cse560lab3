import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class PassTwo {
	public static String Output (String outputName, Tables machineTables) throws IOException {
		File inputFile = new File("intermediate.txt");
		boolean fileExists = inputFile.exists();
		BufferedWriter bufferedWriter = null;
		if (fileExists == false) {
			return "The file does not exist. Try another one.";
		}

		String read = "";
		FileReader reader = new FileReader("intermediate.txt");
		BufferedReader file = new BufferedReader(reader);
		bufferedWriter = new BufferedWriter(new FileWriter("object.txt"));

		read = file.readLine();
		String name = read.substring(0, 6);
		String start = read.substring(18, 22);
		// i need this where is it
		String length = locationCounter;
		
		bufferedWriter.write("H" + name + start + length);
		bufferedWriter.newLine();
		
		read = file.readLine();
		while(read != null)
		{
			Tables machine = new Tables();
			String newLine = "\n";
			int count = 0;
			String operations = read.substring(9, 14);
			if(machine.machineOpTable.containsKey(operations))
			{
				//check which machine op it is
			
				int indexNewLine = read.indexOf(newLine);
				String operands = read.substring(17, indexNewLine);
				String delims = "[,]";
				String[] op = operands.split(delims);
				//minus 1 anyone know
				while(op.length > count)
				{
					if(machine.symbolTable.containsKey(op[count]))
							{
						//what to do from here
						machine.symbolTable.get(op[count]);
						//output this value
							}
					if(machine.literalTable.containsKey(op[count]))
					{
						machine.literalTable.get(op[count]);	
					}
					count++;
				}
				 
				 
			}
			
		}
		return null;
		
	}
}
