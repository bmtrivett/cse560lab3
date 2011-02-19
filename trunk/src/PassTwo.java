import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PassTwo {
	public static String output(String objOutName, String ppOutName,
			Tables machineTables) throws IOException {
		File inputFile = new File("intermediate.txt");
		boolean fileExists = inputFile.exists();
		BufferedWriter bufferedWriter = null;
		if (fileExists == false) {
			return "The file does not exist. Try another one.";
		}

		String read = "";
		String textRecord = "";
		 StringBuffer stringBuffer = new StringBuffer();
		FileReader reader = new FileReader("intermediate.txt");
		BufferedReader file = new BufferedReader(reader);
		bufferedWriter = new BufferedWriter(new FileWriter("object.txt"));

		read = file.readLine();
		String name = read.substring(0, 6);
		String start = read.substring(18, 22);
		boolean relative = false;
		if (start == "    ") {
			relative = true;
			start = "0000";
		}
		int adress = Utility.HexToDecimalValue(start);
		String length = Utility
				.DecimalValueToHex(machineTables.locationCounter);

		bufferedWriter.write("H" + name + start + length);
		bufferedWriter.newLine();

		read = file.readLine();
		while (read != null) {
			Tables machine = new Tables();
			String[] op;
			String newLine = "\n";
			String binary = "";
			int count = 1;
			String operations = read.substring(9, 14);
			if (machine.machineOpTable.containsKey(operations)) {

				int indexNewLine = read.indexOf(newLine);
				String operands = read.substring(17, indexNewLine);
				String delims = "[,]";
				op = operands.split(delims);
				// minus 1 anyone know
				while (op.length > count) {
					// convert to hex before its put into the table
					// then convert to binary
					if (machine.symbolTable.containsKey(op[count])) {
						String[] temp = machine.symbolTable.get(op[count]);
						String bin = Utility.HexToBinary(temp[1]);
						op[count] = bin;

					}
					if (machine.literalTable.containsKey(op[count])) {
						String[] temp = machine.literalTable.get(op[count]);
						String bin = Utility.HexToBinary(temp[1]);
						op[count] = bin;

					}
					if (op[count].charAt(0) == 'R') {
						String bin = Utility
								.HexToBinary(op[count].substring(1));
						op[count] = bin;

					}
					count++;
					if (op[count].charAt(0) == 'x') {
						String bin = Utility
								.HexToBinary(op[count].substring(1));
						op[count] = bin;

					}
					if (op[count].charAt(0) == '#') {
						// check if neg????
						int temp = 0;
						if(op[count].charAt(1) == '-')
						{
						temp = Integer.parseInt(op[count].substring(1)); 
						temp = Utility.convertToTwosComplement(temp);
						}
						else
						{
							temp = Integer.parseInt(op[count].substring(1));
						}
						
						String bin = Utility.DecimalValueToHex(temp);
						bin = Utility.HexToBinary(bin);
						op[count] = bin;
					}
					// else throw a fucktard error

				}

				if (operations == "BR   ") {
					// use string builders
					// put an if statment in these if booleans true then put a m
					// or p=partial
					binary = "0000000";
					op[1] = op[1].substring(7);
					binary = binary + op[1];
					textRecord = Utility.BinaryToHex(binary);
					adress = Utility.HexToDecimalValue(start);
					adress += 1;
					String hexAdress = Utility.DecimalValueToHex(adress);

					if (relative) {
						stringBuffer.append("P").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					} else {
						stringBuffer.append("T").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					}
				} else if (operations == "BRN  ") {
					binary = "0000100";
					//9 or 8?????
					op[1] = op[1].substring(7);
					binary = binary + op[1];
					textRecord = Utility.BinaryToHex(binary);
					adress = Utility.HexToDecimalValue(start);
					adress += 1;
					String hexAdress = Utility.DecimalValueToHex(adress);

					if (relative) {
						stringBuffer.append("P").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					} else {
						stringBuffer.append("T").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					}
				} else if (operations == "BRZ  ") {
					binary = "0000010";
					op[1] = op[1].substring(7);
					binary = binary + op[1];
					textRecord = Utility.BinaryToHex(binary);
					adress = Utility.HexToDecimalValue(start);
					adress += 1;
					String hexAdress = Utility.DecimalValueToHex(adress);

					if (relative) {
						stringBuffer.append("P").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					} else {
						stringBuffer.append("T").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					}
				} else if (operations == "BRP  ") {
					binary = "0000001";
					op[1] = op[1].substring(7);
					binary = binary + op[1];
					textRecord = Utility.BinaryToHex(binary);
					adress = Utility.HexToDecimalValue(start);
					adress += 1;
					String hexAdress = Utility.DecimalValueToHex(adress);

					if (relative) {
						stringBuffer.append("P").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					} else {
						stringBuffer.append("T").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					}
				} else if (operations == "BRNZ ") {
					binary = "0000110";
					op[1] = op[1].substring(7);
					binary = binary + op[1];
					textRecord = Utility.BinaryToHex(binary);
					adress = Utility.HexToDecimalValue(start);
					adress += 1;
					String hexAdress = Utility.DecimalValueToHex(adress);

					if (relative) {
						stringBuffer.append("P").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					} else {
						stringBuffer.append("T").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					}
				} else if (operations == "BRNP ") {
					binary = "0000101";
					op[1] = op[1].substring(7);
					binary = binary + op[1];
					textRecord = Utility.BinaryToHex(binary);
					adress = Utility.HexToDecimalValue(start);
					adress += 1;
					String hexAdress = Utility.DecimalValueToHex(adress);
					if (relative) {
						stringBuffer.append("P").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					} else {
						stringBuffer.append("T").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					}
				} else if (operations == "BRZP ") {
					binary = "0000011";
					op[1] = op[1].substring(7);
					binary = binary + op[1];
					textRecord = Utility.BinaryToHex(binary);
					adress = Utility.HexToDecimalValue(start);
					adress += 1;
					String hexAdress = Utility.DecimalValueToHex(adress);

					if (relative) {
						stringBuffer.append("P").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					} else {
						stringBuffer.append("T").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					}
				} else if (operations == "BRNZP") {
					binary = "0000111";
					op[1] = op[1].substring(7);
					binary = binary + op[1];
					textRecord = Utility.BinaryToHex(binary);
					adress = Utility.HexToDecimalValue(start);
					adress += 1;
					String hexAdress = Utility.DecimalValueToHex(adress);

					if (relative) {
						stringBuffer.append("P").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					} else {
						stringBuffer.append("T").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					}
				} else if (operations == "NOP  ") {
					binary = "0000000000000000";

				} else if (operations == "ADD  ") {
					binary = "0001";
					binary = binary + op[1] + op[2] + "0" + "11" + op[3];
					textRecord = Utility.BinaryToHex(binary);
					adress = Utility.HexToDecimalValue(start);
					adress += 1;
					String hexAdress = Utility.DecimalValueToHex(adress);

					if (relative) {
						stringBuffer.append("P").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					} else {
						stringBuffer.append("T").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					}
				} else if (operations == "LD   ") {
					binary = "0010";
					op[2] = op[2].substring(7);
					binary = binary + op[1] + op[2];
					textRecord = Utility.BinaryToHex(binary);
					adress = Utility.HexToDecimalValue(start);
					adress += 1;
					String hexAdress = Utility.DecimalValueToHex(adress);

					if (relative) {
						stringBuffer.append("P").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					} else {
						stringBuffer.append("T").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					}
				} else if (operations == "ST   ") {
					binary = "0011";
					op[2] = op[2].substring(7);
					binary = binary + op[1] + op[2];
					textRecord = Utility.BinaryToHex(binary);
					adress = Utility.HexToDecimalValue(start);
					adress += 1;
					String hexAdress = Utility.DecimalValueToHex(adress);

					if (relative) {
						stringBuffer.append("P").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					} else {
						stringBuffer.append("T").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					}
				} else if (operations == "JSR  ") {// whats L
					binary = "0100";
					op[1] = op[1].substring(7);
					binary = binary + "1" + "11" + op[1];
					textRecord = Utility.BinaryToHex(binary);
					adress = Utility.HexToDecimalValue(start);
					adress += 1;
					String hexAdress = Utility.DecimalValueToHex(adress);

					if (relative) {
						stringBuffer.append("P").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					} else {
						stringBuffer.append("T").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					}
				} else if (operations == "JMP  ") {// donno //exact copy of
													// brnzp
					binary = "0000";
					op[1] = op[1].substring(7);
					binary = binary + "0" + "11" + op[1];
					textRecord = Utility.BinaryToHex(binary);
					adress = Utility.HexToDecimalValue(start);
					adress += 1;
					String hexAdress = Utility.DecimalValueToHex(adress);

					if (relative) {
						stringBuffer.append("P").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					} else {
						stringBuffer.append("T").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					}

				} else if (operations == "AND  ") {
					binary = "0101";
					binary = binary + op[1] + op[2] + "0" + "11" + op[3];
					textRecord = Utility.BinaryToHex(binary);
					adress = Utility.HexToDecimalValue(start);
					adress += 1;
					String hexAdress = Utility.DecimalValueToHex(adress);

					if (relative) {
						stringBuffer.append("M").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					} else {
						stringBuffer.append("T").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					}
				} else if (operations == "LDR  ") {
					binary = "0110";
					binary = binary + op[1] + op[2] + op[3];
					textRecord = Utility.BinaryToHex(binary);
					adress = Utility.HexToDecimalValue(start);
					adress += 1;
					String hexAdress = Utility.DecimalValueToHex(adress);

					if (relative) {
						stringBuffer.append("M").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					} else {
						stringBuffer.append("T").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					}
				} else if (operations == "STR  ") {
					binary = "0111";
					binary = binary + op[1] + op[2] + op[3];
					textRecord = Utility.BinaryToHex(binary);
					adress = Utility.HexToDecimalValue(start);
					adress += 1;
					String hexAdress = Utility.DecimalValueToHex(adress);

					if (relative) {
						stringBuffer.append("M").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					} else {
						stringBuffer.append("T").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					}
				} else if (operations == "DBUG ") {
					binary = "1000000000000000";
					textRecord = Utility.BinaryToHex(binary);
					adress = Utility.HexToDecimalValue(start);
					adress += 1;
					String hexAdress = Utility.DecimalValueToHex(adress);

					if (relative) {
						stringBuffer.append("M").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					} else {
						stringBuffer.append("T").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					}
				} else if (operations == "NOT  ") {
					binary = "1001";
					binary = binary + op[1] + op[2] + "000000";
					textRecord = Utility.BinaryToHex(binary);
					adress = Utility.HexToDecimalValue(start);
					adress += 1;
					String hexAdress = Utility.DecimalValueToHex(adress);

					if (relative) {
						stringBuffer.append("M").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					} else {
						stringBuffer.append("T").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					}
				} else if (operations == "LDI  ") {
					binary = "1010";
					op[2] = op[2].substring(7);
					binary = binary + op[1] + op[2];
					textRecord = Utility.BinaryToHex(binary);
					adress = Utility.HexToDecimalValue(start);
					adress += 1;
					String hexAdress = Utility.DecimalValueToHex(adress);

					if (relative) {
						stringBuffer.append("M").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					} else {
						stringBuffer.append("T").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					}
				} else if (operations == "STI  ") {
					binary = "1011";
					op[2] = op[2].substring(7);
					binary = binary + op[1] + op[2];
					textRecord = Utility.BinaryToHex(binary);
					adress = Utility.HexToDecimalValue(start);
					adress += 1;
					String hexAdress = Utility.DecimalValueToHex(adress);

					if (relative) {
						stringBuffer.append("P").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					} else {
						stringBuffer.append("T").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					}
				} else if (operations == "JMPR ") {// donno
					binary = "0000";
					binary = binary + "0" + "00" + op[1] + op[2];
					textRecord = Utility.BinaryToHex(binary);
					adress = Utility.HexToDecimalValue(start);
					adress += 1;
					String hexAdress = Utility.DecimalValueToHex(adress);

					if (relative) {
						stringBuffer.append("M").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					} else {
						stringBuffer.append("T").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					}
				} else if (operations == "JSRR ") {
					binary = "1100";
					binary = binary + "1" + "00" + op[1] + op[2];
					textRecord = Utility.BinaryToHex(binary);
					adress = Utility.HexToDecimalValue(start);
					adress += 1;
					String hexAdress = Utility.DecimalValueToHex(adress);

					if (relative) {
						stringBuffer.append("M").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					} else {
						stringBuffer.append("T").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					}
				} else if (operations == "RET  ") {
					binary = "1101000000000000";
					textRecord = Utility.BinaryToHex(binary);
					adress = Utility.HexToDecimalValue(start);
					adress += 1;
					String hexAdress = Utility.DecimalValueToHex(adress);

					if (relative) {
						stringBuffer.append("M").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					} else {
						stringBuffer.append("T").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					}
				} else if (operations == "LEA  ") {
					binary = "1110";
					op[2] = op[2].substring(7);
					binary = binary + op[1] + op[2];
					textRecord = Utility.BinaryToHex(binary);
					adress = Utility.HexToDecimalValue(start);
					adress += 1;
					String hexAdress = Utility.DecimalValueToHex(adress);

					if (relative) {
						stringBuffer.append("P").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					} else {
						stringBuffer.append("T").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					}
				} else if (operations == "TRAP ") {
					binary = "1111";
					binary = binary + "0000" + op[1];
					textRecord = Utility.BinaryToHex(binary);
					adress = Utility.HexToDecimalValue(start);
					adress += 1;
					String hexAdress = Utility.DecimalValueToHex(adress);

					if (relative) {
						stringBuffer.append("P").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					} else {
						stringBuffer.append("T").append(hexAdress).append(textRecord);
						textRecord = stringBuffer.toString();
					}
				}
			}
			if (machine.psuedoOpTable.containsKey(operations)) {
				int indexNewLine = read.indexOf(newLine);
				String operands = read.substring(17, indexNewLine);
				String delims = "[,]";
				op = operands.split(delims);
				// minus 1 anyone know
				while (op.length > count) {
					// convert to hex before its put into the table
					// then convert to binary
					if (machine.symbolTable.containsKey(op[count])) {
						String[] temp = machine.symbolTable.get(op[count]);
						String bin = Utility.HexToBinary(temp[1]);
						op[count] = bin;

					}
					if (machine.literalTable.containsKey(op[count])) {
						String[] temp = machine.literalTable.get(op[count]);
						String bin = Utility.HexToBinary(temp[1]);
						op[count] = bin;

					}
					if (op[count].charAt(0) == 'R') {
						String bin = Utility
								.HexToBinary(op[count].substring(1));
						op[count] = bin;

					}
					count++;
					if (op[count].charAt(0) == 'x') {
						String bin = Utility
								.HexToBinary(op[count].substring(1));
						op[count] = bin;

					}
					if (op[count].charAt(0) == '#') {
						// check if neg????
						if (op[count].charAt(0) == '-') {

						}

						String bin = Utility.DecimalValueToHex(Integer
								.parseInt(op[count].substring(1)));
						bin = Utility
						.HexToBinary(bin);
						op[count] = bin;
					}
					if (op[count].charAt(0) == '"') {
						String quotation = "\"";
						int index3 = read.indexOf(quotation);

						String value = read.substring(1, index3);
						op[count] = value;

					}
					// else throw a fucktard error

				}
				if(operations == ".EQU ")
				{
					String hexAdress = Utility.DecimalValueToHex(adress);
					textRecord = "T" + hexAdress + op[1];
				}
				else if(operations == ".BLKW")
				{
					adress += 1;
					
				}
				else if(operations == ".STRZ")
				{
					while(op.length > count)
					{
						//how to convert letter into hex?????
						String hexAdress = Utility.DecimalValueToHex(adress);
						int count2 = 1;
						while(op[count].length() >= count2)
						{
							op[count] = Utility.DecimalValueToHex((int)op[count].charAt(count2));
							textRecord = "T" + hexAdress + op[count];
							//output right here
							
							adress ++;
						}
						op[count] = Utility.DecimalValueToHex((int)op[count].charAt(count2));
						textRecord = "T" + hexAdress + op[count];
						adress ++;
					}
					
				}
				else if(operations == ".FILL")
				{
					String hexAdress = Utility.DecimalValueToHex(adress);
					textRecord = "T" + hexAdress + op[1];
					adress += 1;
				}
				else if(operations == ".END ")
				{
					
					textRecord = "T" + machine.locationCounter;
				
				}
				
			}

		}
		return null;

	}
}

