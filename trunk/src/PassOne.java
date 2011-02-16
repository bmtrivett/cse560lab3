import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PassOne {

	public static String run(String input, Tables machineTables) throws IOException {
		File inputFile = new File(input);
		boolean fileExists = inputFile.exists();
		BufferedWriter bufferedWriter = null;
		BufferedWriter bufferedWriterComments = null;
		if (fileExists == false) {
			return "The file does not exist. Try another one.";
		}

		String read = "";
		FileReader reader = new FileReader(input);
		BufferedReader file = new BufferedReader(reader);
		bufferedWriter = new BufferedWriter(new FileWriter("intermediate.txt"));
		bufferedWriterComments = new BufferedWriter(new FileWriter(
				"comments.txt"));

		read = file.readLine();

		if (read.charAt(0) == ';') {
			bufferedWriterComments.write(read);
			bufferedWriterComments.newLine();
			read = file.readLine();
		} else {
			String orig = read.substring(9, 14);
			if (!orig.equals(".ORIG")) {
				return "The string does not contain the substring ";
			} else {
				String location = read.substring(18, 22);
				if (location.equals("    ")) {
					machineTables.locationCounter = 0000;
				}
				// if its 0 - 8000 fine else throw an error
				else {
					machineTables.locationCounter = Utility.HexToDecimalValue(location);
				}
				bufferedWriter.write(read);
				bufferedWriter.newLine();

				read = file.readLine();

				String end = read.substring(9, 14);

				while (!end.equals(".END")) {
					if (read.charAt(0) != ';') {

						String firstWord = read.substring(0, 6);
						if (!firstWord.equals("      ")) {
							String[] tempString = new String[2];
							String hexLC = Utility
									.DecimalValueToHex(machineTables.locationCounter);
							tempString[0] = hexLC;
							tempString[1] = "rel";
							// check with ben on which ones are abs or rel
							//one for rel, 0 for abs
							//rel br,jsr,jmp,ld,ldi,lea,st,sti
							machineTables.symbolTable.put(firstWord, tempString);

						}

						String operation = read.substring(9, 14);

						if (machineTables.machineOpTable.containsKey(operation)
								|| machineTables.psuedoOpTable.containsKey(operation)) {
							if (machineTables.machineOpTable.containsKey(operation)) {

								machineTables.locationCounter++;
							} else {
								if (operation.equals(".BLKW")){

									if (read.charAt(17) == '#') {
										String length = read.substring(18, 23);
										int lcLength = Integer.parseInt(length);

										machineTables.locationCounter = machineTables.locationCounter
												+ lcLength;

									} else if (read.charAt(17) == 'x') {
										String length = read.substring(18, 22);
										int lcLength = Utility
												.HexToDecimalValue(length);

										machineTables.locationCounter += lcLength;
									} 
									else {
										// throw a motherfuckin error
									}

								} else if (operation.equals(".STRZ")) {
									String text = read.substring(17);
									String index = text.substring(18);

									String quotation = "\"";
									int index3 = read.indexOf(quotation);

									String value = read.substring(17, index3);
									int lc = value.length();
									machineTables.locationCounter += lc;

								}

							}
						}

						else {
							return "error";
						}

					}
					String literal = "=";
					String inLineLiteral = "";
					int index3 = read.indexOf(literal);
					// read till ; /n " "
					// do they need to be hex and whats the value
					if (index3 != -1) {
						String space = " ";
						String semicolone = ";";
						String newline = "\n";
						int indexSpace = read.indexOf(space);
						int indexSemi = read.indexOf(semicolone);
						int indexNew = read.indexOf(newline);
						if (indexSpace != -1) {
							inLineLiteral = read.substring(index3,
									indexSpace);
							if (inLineLiteral.charAt(1) == '#') {
							//value of literal as a string, the value of hex,location counter
								String hex = inLineLiteral.substring(index3+2,indexSpace);
								String[] value = new String[2];
								value[1] = hex;
								value[2] = Utility.DecimalValueToHex(machineTables.locationCounter);
							machineTables.literalTable.put(inLineLiteral, value);
							}
							else if (inLineLiteral.charAt(1) == 'x') {
								int dec = Integer.parseInt(inLineLiteral.substring(index3+2,indexSpace));
								String hex = Utility.DecimalValueToHex(dec);
								String[] value = new String[2];
								value[1] = hex;
								value[2] = Utility.DecimalValueToHex(machineTables.locationCounter);
							machineTables.literalTable.put(inLineLiteral, value);
							}
							
						} else if (indexSemi != -1) {
							inLineLiteral = read.substring(index3,
									indexSemi);
							if (inLineLiteral.charAt(1) == '#') {
								//value of literal as a string, the value of hex,location counter
									String hex = inLineLiteral.substring(index3+2,indexSpace);
									String[] value = new String[2];
									value[1] = hex;
									value[2] = Utility.DecimalValueToHex(machineTables.locationCounter);
								machineTables.literalTable.put(inLineLiteral, value);
								}
								else if (inLineLiteral.charAt(1) == 'x') {
									int dec = Integer.parseInt(inLineLiteral.substring(index3+2,indexSpace));
									String hex = Utility.DecimalValueToHex(dec);
									String[] value = new String[2];
									value[1] = hex;
									value[2] = Utility.DecimalValueToHex(machineTables.locationCounter);
								machineTables.literalTable.put(inLineLiteral, value);
								}
						} else if (indexSemi != -1) {
							inLineLiteral = read
									.substring(index3, indexNew);
							if (inLineLiteral.charAt(1) == '#') {
								//value of literal as a string, the value of hex,location counter
									String hex = inLineLiteral.substring(index3+2,indexSpace);
									String[] value = new String[2];
									value[1] = hex;
									value[2] = Utility.DecimalValueToHex(machineTables.locationCounter);
								machineTables.literalTable.put(inLineLiteral, value);
								}
								else if (inLineLiteral.charAt(1) == 'x') {
									int dec = Integer.parseInt(inLineLiteral.substring(index3+2,indexSpace));
									String hex = Utility.DecimalValueToHex(dec);
									String[] value = new String[2];
									value[1] = hex;
									value[2] = Utility.DecimalValueToHex(machineTables.locationCounter);
								machineTables.literalTable.put(inLineLiteral, value);
								}
						}

					}

					String comment = ";";
					int index4 = read.indexOf(comment);
					if (index4 != -1) {
						String inLineComment = read.substring(index4);
						bufferedWriterComments.write(inLineComment);
						bufferedWriterComments.newLine();

						String restOfLine = read.substring(0, index4);
						bufferedWriter.write(restOfLine);
						bufferedWriter.newLine();
					}

					else {
						bufferedWriter.write(read);
						bufferedWriter.newLine();
						read = file.readLine();
					}
				}

			}
		}

		return null;

	}
}
