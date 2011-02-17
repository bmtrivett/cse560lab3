import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class PassTwo {
	public static String output (String objOutName, String ppOutName, Tables machineTables) throws IOException {
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
		//set boolean after this
		// i need this where is it
		String length = Utility.DecimalValueToHex(machineTables.locationCounter);
		
		bufferedWriter.write("H" + name + start + length);
		bufferedWriter.newLine();
		
		read = file.readLine();
		while(read != null)
		{
			Tables machine = new Tables();
			String[] op;
			String newLine = "\n";
			String binary = "";
			int count = 1;
			String operations = read.substring(9, 14);
			if(machine.machineOpTable.containsKey(operations))
			{
				//check which machine op it is
			
				int indexNewLine = read.indexOf(newLine);
				String operands = read.substring(17, indexNewLine);
				String delims = "[,]";
				op = operands.split(delims);
				//minus 1 anyone know
				while(op.length > count)
				{
					//convert to hex before its put into the table
					//then convert to binary
					if(machine.symbolTable.containsKey(op[count]))
							{
						String[] temp = machine.symbolTable.get(op[count]);
						op[count] = temp[1];
						
							}
					if(machine.literalTable.containsKey(op[count]))
					{
						String[] temp = machine.literalTable.get(op[count]);
						op[count] = temp[1];
						
					}
					if(op[count].charAt(0) == 'R')
					{
						op[count] = op[count].substring(1);
					}
					count++;
					if(op[count].charAt(0) == 'x')
					{
						op[count] = op[count].substring(1);
					}
					if(op[count].charAt(0) == '#')
					{
						//check if neg
						op[count] = op[count].substring(1);
					}
					//else throw a fucktard error
					
				}
			
				if (operations == "BR   ")
				{
					//use string builders
					//put an if statment in these if booleans true then put a m or p=partial
				binary = "0000000";
				binary = binary + op[1];
				Utility.BinaryToHex(binary);
				}				
				else if (operations == "BRN  ")
				{
				binary = "0000100";
				binary = binary + op[1];
				Utility.BinaryToHex(binary);
				}
				else if (operations == "BRZ  ")
				{
				binary = "0000010";
				binary = binary + op[1];
				Utility.BinaryToHex(binary);
				}
				else if (operations == "BRP  ")
				{
				binary = "0000001";
				binary = binary + op[1];
				Utility.BinaryToHex(binary);
				}
				else if (operations == "BRNZ ")
				{
				binary = "0000110";
				binary = binary + op[1];
				Utility.BinaryToHex(binary);
				}
				else if (operations == "BRNP ")
				{
				binary = "0000101";
				binary = binary + op[1];
				Utility.BinaryToHex(binary);
				}
				else if (operations == "BRZP ")
				{
				binary = "0000011";
				binary = binary + op[1];
				Utility.BinaryToHex(binary);
				}
				else if (operations == "BRNZP")
				{
				binary = "0000111";
				binary = binary + op[1];
				Utility.BinaryToHex(binary);
				}
				else if (operations == "NOP  ")
				{//donno
				binary = "0000";
				
				}
				else if (operations == "ADD  ")
				{
				binary = "0001";
				binary = binary + op[1] + op[2] + "0" + "11" + op[3];
				Utility.BinaryToHex(binary);
				}
				else if (operations == "LD   ")
				{
				binary = "0010";
				binary = binary + op[1] + op[2];
				Utility.BinaryToHex(binary);
				}
				else if (operations == "ST   ")
				{
				binary = "0011";
				binary = binary + op[1] + op[2];
				Utility.BinaryToHex(binary);
				}
				else if (operations == "JSR  ")
				{//whats L
				binary = "0100";
				binary = binary + "1" + "11" + op[1];
				Utility.BinaryToHex(binary);
				}
				else if (operations == "JMP  ")
				{//donno //exact copy of brnzp
				binary = "0000";
				
				}
				else if (operations == "AND  ")
				{
				binary = "0101";
				binary = binary + op[1] + op[2] + "0" + "11" + op[3];
				Utility.BinaryToHex(binary);
				}
				else if (operations == "LDR  ")
				{
				binary = "0110";
				binary = binary + op[1] + op[2] + op[3];
				Utility.BinaryToHex(binary);
				}
				else if (operations == "STR  ")
				{
				binary = "0111";
				binary = binary + op[1] + op[2] + op[3];
				Utility.BinaryToHex(binary);
				}
				else if (operations == "DBUG ")
				{
				binary = "1000000000000000";
				Utility.BinaryToHex(binary);
				
				}
				else if (operations == "NOT  ")
				{
				binary = "1001";
				binary = binary + op[1] + op[2] + "000000";
				Utility.BinaryToHex(binary);
				}
				else if (operations == "LDI  ")
				{
				binary = "1010";
				binary = binary + op[1] + op[2];
				Utility.BinaryToHex(binary);
				}
				else if (operations == "STI  ")
				{
				binary = "1011";
				binary = binary + op[1] + op[2];
				Utility.BinaryToHex(binary);
				}
				else if (operations == "JMPR ")
				{//donno
				binary = "0000";
				}
				else if (operations == "JSRR ")
				{
				binary = "1100";
				binary = binary + "L" + "00" + op[1] + op[2];
				Utility.BinaryToHex(binary);
				}
				else if (operations == "RET  ")
				{
				binary = "1101000000000000";
				Utility.BinaryToHex(binary);
				
				}
				else if (operations == "LEA  ")
				{
				binary = "1110";
				binary = binary + op[1] + op[2];
				Utility.BinaryToHex(binary);
				}
				else if (operations == "TRAP ")
				{
				binary = "1111";
				binary = binary + "0000" + op[1];
				Utility.BinaryToHex(binary);
				}
			}
			
		}
		return null;
		
	}
}
