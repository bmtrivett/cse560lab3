import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PassOne {

	public static String run(String input, Tables machineTables)
			throws IOException {
		int lineCounter = 1;
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

		while (read.charAt(0) == ';') {
			bufferedWriterComments.write(lineCounter + '\t' + read);
			bufferedWriterComments.newLine();
			read = file.readLine();
			lineCounter++;
		}

		String orig = overSubstring(read, 9, 14);
		if (!orig.equals(".ORIG")) {
			return "The string does not contain the substring ";
		} else {
			String location = overSubstring(read, 18, 22);
			if (location.equals("    ")) {
				machineTables.locationCounter = 0;
				machineTables.isRelative = true;
			} else {
				machineTables.locationCounter = Utility
						.HexToDecimalValue(location);
				machineTables.isRelative = false;
			}
			bufferedWriter.write(read);
			bufferedWriter.newLine();

			read = file.readLine();
			lineCounter++;

			String end = overSubstring(read, 9, 14);

			while (!end.equals(".END ")) {
				if (read.charAt(0) != ';') {

					String firstWord = overSubstring(read, 0, 6);
					if (!firstWord.equals("      ")) {
						String[] tempString = new String[2];
						String hexLC = Utility
								.DecimalValueToHex(machineTables.locationCounter);
						tempString[0] = hexLC;
						tempString[1] = "1";

						if (overSubstring(read, 9, 14) == ".EQU ") {
							String inLine = "";
							int endIndex = read.indexOf(inLine);
							String temp = overSubstring(read, 9, endIndex);
							if (machineTables.symbolTable.containsKey(temp)) {
								machineTables.symbolTable.put(firstWord,
										tempString);
							} else {
								tempString[1] = "0";
								String[] equ = machineTables.symbolTable
										.get(temp);
								firstWord = equ[1];
								machineTables.symbolTable.put(firstWord,
										tempString);
							}
						}
						machineTables.symbolTable.put(firstWord, tempString);

					}

					String operation = overSubstring(read, 9, 14);

					if (machineTables.machineOpTable.containsKey(operation)
							|| machineTables.psuedoOpTable
									.containsKey(operation)) {
						if (machineTables.machineOpTable.containsKey(operation)) {

							machineTables.locationCounter++;
						} else {
							if (operation.equals(".BLKW")) {

								if (read.charAt(17) == '#') {
									String length = overSubstring(read, 18, 23);
									int lcLength = Integer.parseInt(length);

									machineTables.locationCounter = machineTables.locationCounter
											+ lcLength;

								} else if (read.charAt(17) == 'x') {
									String length = overSubstring(read, 18, 22);
									int lcLength = Utility
											.HexToDecimalValue(length);

									machineTables.locationCounter += lcLength;
								} else {
									// throw a motherfuckin error
								}

							} else if (operation.equals(".STRZ")) {
								String text = read.substring(17);
								String index = text.substring(18);

								String quotation = "\"";
								int index3 = read.indexOf(quotation);

								String value = overSubstring(read, 17, index3);
								int lc = value.length();
								machineTables.locationCounter += lc + 1;

							} else if (operation.equals(".FILL")) {
								machineTables.locationCounter += 1;
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
						inLineLiteral = overSubstring(read, index3, indexSpace);
						if (inLineLiteral.charAt(1) == '#') {
							// value of literal as a string, the value of
							// hex,location counter
							String hex = inLineLiteral.substring(index3 + 2,
									indexSpace);
							String[] value = new String[2];
							value[1] = hex;
							value[2] = Utility
									.DecimalValueToHex(machineTables.locationCounter);
							machineTables.literalTable
									.put(inLineLiteral, value);
						} else if (inLineLiteral.charAt(1) == 'x') {
							int dec = Integer.parseInt(inLineLiteral.substring(
									index3 + 2, indexSpace));
							String hex = Utility.DecimalValueToHex(dec);
							String[] value = new String[2];
							value[1] = hex;
							value[2] = Utility
									.DecimalValueToHex(machineTables.locationCounter);
							machineTables.literalTable
									.put(inLineLiteral, value);
						}

					} else if (indexSemi != -1) {
						inLineLiteral = overSubstring(read, index3, indexSemi);
						if (inLineLiteral.charAt(1) == '#') {
							// value of literal as a string, the value of
							// hex,location counter
							String hex = inLineLiteral.substring(index3 + 2,
									indexSpace);
							String[] value = new String[2];
							value[1] = hex;
							value[2] = Utility
									.DecimalValueToHex(machineTables.locationCounter);
							machineTables.literalTable
									.put(inLineLiteral, value);
						} else if (inLineLiteral.charAt(1) == 'x') {
							int dec = Integer.parseInt(inLineLiteral.substring(
									index3 + 2, indexSpace));
							String hex = Utility.DecimalValueToHex(dec);
							String[] value = new String[2];
							value[1] = hex;
							value[2] = Utility
									.DecimalValueToHex(machineTables.locationCounter);
							machineTables.literalTable
									.put(inLineLiteral, value);
						}
					} else if (indexSemi != -1) {
						inLineLiteral = overSubstring(read, index3, indexNew);
						if (inLineLiteral.charAt(1) == '#') {
							// value of literal as a string, the value of
							// hex,location counter
							String hex = inLineLiteral.substring(index3 + 2,
									indexSpace);
							String[] value = new String[2];
							value[1] = hex;
							value[2] = Utility
									.DecimalValueToHex(machineTables.locationCounter);
							machineTables.literalTable
									.put(inLineLiteral, value);
						} else if (inLineLiteral.charAt(1) == 'x') {
							int dec = Integer.parseInt(inLineLiteral.substring(
									index3 + 2, indexSpace));
							String hex = Utility.DecimalValueToHex(dec);
							String[] value = new String[2];
							value[1] = hex;
							value[2] = Utility
									.DecimalValueToHex(machineTables.locationCounter);
							machineTables.literalTable
									.put(inLineLiteral, value);
						}
					}

				}

				String comment = ";";
				int index4 = read.indexOf(comment);
				if (index4 != -1) {
					String inLineComment = read.substring(index4);
					bufferedWriterComments.write(lineCounter + '\t'
							+ inLineComment);
					bufferedWriterComments.newLine();

					String restOfLine = overSubstring(read, 0, index4);
					bufferedWriter.write(restOfLine);
					bufferedWriter.newLine();
				}

				else {
					bufferedWriter.write(read);
					bufferedWriter.newLine();
					read = file.readLine();
					lineCounter++;
				}
			}
			
			if (end.equals(".END ")) {
				String comment = ";";
				int index4 = read.indexOf(comment);
				if (index4 != -1) {
					String inLineComment = read.substring(index4);
					bufferedWriterComments.write(lineCounter + '\t'
							+ inLineComment);
					bufferedWriterComments.newLine();

					String restOfLine = overSubstring(read, 0, index4);
					bufferedWriter.write(restOfLine);
					bufferedWriter.newLine();
				}

				else {
					bufferedWriter.write(read);
					bufferedWriter.newLine();
					read = file.readLine();
					lineCounter++;
				}
				machineTables.locationCounter++;
			}

		}

		// Set locations in the literal table.
		int count = machineTables.literalTable.size();
		String keys[] = machineTables.literalTable.keySet().toArray(
				new String[0]);
		String tempVal[];
		while (count > 0) {
			tempVal = machineTables.literalTable.remove(keys[count]);
			machineTables.locationCounter++;
			tempVal[1] = Utility
					.DecimalValueToHex(machineTables.locationCounter);
			machineTables.literalTable.put(keys[count], tempVal);
			count--;
		}

		return null;

	}

	public static String overSubstring(String str, int x, int y) {
		Boolean exceptions = true;
		int z = 0;
		while (exceptions && x != y) {
			exceptions = false;
			try {
				str.substring(x, y);
			} catch (Exception e) {
				exceptions = true;
				y--;
				z++;
			}
		}
		String temp = str.substring(x, y);
		while (z > 0) {
			temp = temp + " ";
			z--;
		}
		return temp;
	}

}
