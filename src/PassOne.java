import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

/**
 * This class contains the run method that executes the first pass of assembly.
 * 
 * @author Ben Trivett, Bryant Schuck, Gerard Louis
 */
public class PassOne {

	public static String run(String input, Tables machineTables)
			throws IOException {
		int lineCounter = 1;

		// Make sure the source file exists.
		File inputFile = new File(input);
		boolean fileExists = inputFile.exists();
		if (fileExists == false) {
			return "The file does not exist. Try another one.";
		}

		// Initialize input and output files.
		FileReader reader = new FileReader(input);
		BufferedReader file = new BufferedReader(reader);
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
				"intermediate.txt"));
		BufferedWriter bufferedWriterComments = new BufferedWriter(
				new FileWriter("comments.txt"));

		// Read the first line of source.
		String read = file.readLine();

		// Remove all the comments at the beginning of the file and store them
		// to comments.txt.
		while (read.charAt(0) == ';') {
			bufferedWriterComments.write(lineCounter + "\t" + read);
			bufferedWriterComments.newLine();
			read = file.readLine();
			lineCounter++;
		}

		// The first line of code that isn't a comment must be a .ORIG pseudo
		// operation with a label.
		String orig = overSubstring(read, 9, 14);
		if (!orig.equals(".ORIG")) {
			return "Missing .ORIG operation at the beginning of the source file.";
		}
		String origLabel = overSubstring(read, 0, 6);
		if (origLabel.equals("      ")) {
			return ".ORIG operation missing label.";
		}

		// Check unused space
		if (!overSubstring(read, 6, 9).trim().equals("")
				|| !overSubstring(read, 14, 17).trim().equals("")) {
			return "Unused space has non-whitespace contents on line "
					+ lineCounter + ".";
		}

		// Determine the starting location and whether it is relative or
		// absolute.
		String location = "    ";
		if (read.length() > 18) {
			location = overSubstring(read, 18, 22);
		}
		if (location.equals("    ")) {
			machineTables.locationCounter = 0;
			machineTables.isRelative = true;
		} else {
			machineTables.locationCounter = Utility.HexToDecimalValue(location);
			machineTables.isRelative = false;
		}
		String origin = Utility
				.DecimalValueToHex(machineTables.locationCounter);

		// Save the .ORIG label to the symbol table.
		String tempArray[] = new String[2];
		tempArray[0] = Utility.DecimalValueToHex(machineTables.locationCounter);
		tempArray[1] = Utility.BooleanToString(machineTables.isRelative);
		machineTables.symbolTable.put(origLabel, tempArray);

		// Write to the intermediate file and read the second line.
		bufferedWriter.write(lineCounter + ":" + read);
		bufferedWriter.newLine();
		read = file.readLine();
		lineCounter++;

		// Read operations until there is a .END or the source file ends.
		while (!overSubstring(read, 9, 14).equals(".END ")) {

			// If the line is not a comment
			if (read.charAt(0) != ';') {

				// Check unused space
				if (read.length() > 16) {
					if (!overSubstring(read, 6, 9).trim().equals("")
							|| !overSubstring(read, 14, 17).trim().equals("")) {
						return "Unused space has non-whitespace contents on line "
								+ lineCounter + ".";
					}
				}

				// Remove in line comments and write them to comments.txt.
				String comment = ";";
				int index4 = read.indexOf(comment);
				if (index4 != -1) {
					String inLineComment = read.substring(index4);
					bufferedWriterComments.write(lineCounter + "\t-"
							+ inLineComment);
					bufferedWriterComments.newLine();
					read = overSubstring(read, 0, index4);
				}

				// Check for a label and add it to the symbol table.
				String firstWord = overSubstring(read, 0, 6);
				if (!firstWord.equals("      ")) {
					String[] tempString = new String[2];

					// Labels must not include blanks.
					if (firstWord.trim().contains(" ")) {
						return "Label has a space in it on line " + lineCounter
								+ ".";
					}

					// Labels can't start with x or R.
					if (firstWord.charAt(0) == 'x'
							|| firstWord.charAt(0) == 'R') {
						return "Label has either a x or R as the first letter on line "
								+ lineCounter + ".";
					}

					// Labels must be alphanumeric.
					if (!firstWord.matches("[a-zA-z0-9 ]*")) {
						return "Label must have only alphanumeric characters on line "
								+ lineCounter + ".";
					}

					// Labels cannot be a predefined operation
					if (machineTables.machineOpTable.containsKey(firstWord)) {
						return "Label cannot match machine operation name on line "
								+ lineCounter + ".";
					}

					// If the operation is .EQU then set values accordingly.
					if (overSubstring(read, 9, 14).equals(".EQU ")) {

						// Check if the operand is a symbol. Symbols are
						// length 6.
						String temp = overSubstring(read, 17, 23);
						if (machineTables.symbolTable.containsKey(temp)) {

							// If it is then set the values of this symbol
							// equal to that symbol.
							tempString = machineTables.symbolTable.get(temp);
						} else {

							// Check next to see if it is a decimal value.
							temp = overSubstring(read, 17, read.length());
							if (temp.charAt(0) == '#') {
								Integer decimalOperand = Integer.parseInt(
										temp.substring(1), 10);
								if (decimalOperand < -32768
										|| decimalOperand > 32767) {
									return "Decimal out of range on line "
											+ lineCounter + ".";
								}
								tempString[0] = Utility
										.DecimalValueToHex(Utility
												.convertToTwosComplement(decimalOperand));
								tempString[1] = "0";

								// Check if it is a hex value.
							} else if (temp.charAt(0) == 'x') {
								if (!Utility.isHexString(temp.substring(1))) {
									return "Invalid hexadecimal value on line "
											+ lineCounter + ".";
								}
								tempString[0] = Utility
										.DecimalValueToHex(Utility
												.HexToDecimalValue(temp
														.substring(1)));
								tempString[1] = "0";

								// Check if it is a register.
							} else if (temp.charAt(0) == 'R') {
								int count = Integer.parseInt(temp.substring(1,
										2));
								if (count < 0 || count > 7) {
									return "Invalid register symbol assignment on line "
											+ lineCounter + ".";
								}
								tempString[0] = Utility
										.DecimalValueToHex(count);
								tempString[1] = "0";
							} else {
								return "Symbol must be previously defined for a .EQU operation.";
							}
						}
					} else {
						// Store the symbol's value based on the location
						// counter if it is not on a .EQU operation.
						tempString[0] = Utility
								.DecimalValueToHex(machineTables.locationCounter);
						tempString[1] = Utility
								.BooleanToString(machineTables.isRelative);
					}

					// Check if the symbol already exists in the table.
					if (machineTables.symbolTable.containsKey(firstWord)) {
						return "Multiple definition of symbol " + firstWord.trim()
								+ " on line " + lineCounter + ".";
					}

					// Store the label in the symbol table.
					machineTables.symbolTable.put(firstWord, tempString);

					// If there was no label check if operation was .EQU.
				} else if (overSubstring(read, 9, 14).equals(".EQU ")) {
					return ".EQU operation requires a label on line "
							+ lineCounter + ".";
				}

				// Increment the location counter based on the operation.
				String operation = overSubstring(read, 9, 14);
				if (machineTables.machineOpTable.containsKey(operation)
						|| machineTables.psuedoOpTable.containsKey(operation)) {

					// All built in machine operations increment the
					// location counter by one.
					if (machineTables.machineOpTable.containsKey(operation)) {
						machineTables.locationCounter++;

						// If it is a pgoffset9 and it is decimal or hexadecimal
						// address, then check that addr is on the same page
						// as location counter.
						if (isAddrOperation(operation)) {
							String addr = "";
							if (operation.contains("BR")
									|| operation.contains("JSR")
									|| operation.contains("JMP")) {
								addr = overSubstring(read, 17, read.length());
							} else {
								addr = overSubstring(read, 20, read.length());
							}
							if (addr.length() > 0) {
								if (addr.charAt(0) == '#'
										|| addr.charAt(0) == 'x') {
									if (addr.charAt(0) == 'x'
											&& !Utility.isHexString((addr
													.substring(1)).trim())) {
										return "The hex string on line "
												+ lineCounter + " is "
												+ " not properly formatted: "
												+ addr + ".";
									}
									int i = 0;
									if (addr.charAt(0) == 'x') {
										i = Utility.HexToDecimalValue((addr
												.substring(1)).trim());
									} else {
										i = Integer.parseInt(addr.trim());
									}
									if (i < 0 || i > 65535) {
										return "The address on line "
												+ lineCounter
												+ " is "
												+ "not within valid decimal range (0-65,535).";
									}
									addr = Integer.toHexString(i);
									if (addr.length() < 4) {
										int j = addr.length();
										while (j < 4) {
											addr = "0" + addr;
											j++;
										}
									}
									if (!isAddrOnSamePage(
											addr,
											Utility.DecimalValueToHex(machineTables.locationCounter))) {
										return "The address on line "
												+ lineCounter
												+ " is "
												+ "not on the same page as the location counter.";
									}
								}
								// Otherwise, store symbol in
								// passOnePgoffsetCheck map,
								// so that it can be checked at the end of pass
								// one.
								else if (!(addr.charAt(0) == '=')) {
									if (addr.length() < 6) {
										int i = addr.length();
										while (i < 6) {
											addr = addr + " ";
											i++;
										}
									}
									Integer[] pgoffsetArray = new Integer[2];
									pgoffsetArray[0] = machineTables.locationCounter;
									pgoffsetArray[1] = lineCounter;
									machineTables.passOnePgoffsetCheck.put(
											addr, pgoffsetArray);
								}
							}
						}
					} else {

						// BLKW increments the location counter by the value
						// indicated in it's operand.
						if (operation.equals(".BLKW")) {

							// Check if the operand is a symbol. Symbols are
							// length 6.
							String temp = overSubstring(read, 17, 23);
							if (machineTables.symbolTable.containsKey(temp)) {

								// BLKW operations can only use symbols that
								// have absolute values.
								if (machineTables.symbolTable.get(temp)[1]
										.equals("1")) {
									return ".BLKW operation using symbol that is relative on line "
											+ lineCounter + ".";
								}

								// If it is then add the value of this
								// symbol to the location counter.
								machineTables.locationCounter += Utility
										.HexToDecimalValue(machineTables.symbolTable
												.get(temp)[0]);
							} else {

								// Check next to see if it is a decimal
								// value.
								temp = overSubstring(read, 17, read.length());
								if (temp.charAt(0) == '#') {
									Integer decimalOperand = Integer.parseInt(
											temp.substring(1), 10);
									if (decimalOperand < 1
											|| decimalOperand > 65535) {
										return "Decimal out of range on line "
												+ lineCounter + ".";
									}
									machineTables.locationCounter += decimalOperand;

									// Check if it is a hex value.
								} else if (temp.charAt(0) == 'x') {
									if (!Utility.isHexString(temp.substring(1))) {
										return "Invalid hexadecimal value on line "
												+ lineCounter + ".";
									}
									machineTables.locationCounter += Utility
											.HexToDecimalValue(temp
													.substring(1));
								} else {
									return "Symbol must be previously defined for a .BLKW operation.";
								}
							}

							// STRZ increments the location counter by the
							// string's length in the operand plus 1.
						} else if (operation.equals(".STRZ")) {
							String text = read.substring(18);
							String quotation = "\"";
							int index3 = text.indexOf(quotation);
							if (index3 == -1 || read.charAt(17) != '\"') {
								return ".STRZ operand missing quotations on line "
										+ lineCounter + ".";
							}
							String value = overSubstring(read, 18, 18 + index3);
							machineTables.locationCounter += value.length() + 1;

						} else if (operation.equals(".FILL")) {
							machineTables.locationCounter += 1;
						}
					}
				}

				// Operation was not in machine/pseudo op tables.
				else {
					return "Invalid operation: " + overSubstring(read, 9, 14)
							+ " not defined on line " + lineCounter + ".";
				}

				// Check for literals to add to the literal table.
				int index3 = read.indexOf("=");
				if (index3 != -1) {
					if (!overSubstring(read, 9, 14).equals("LD   ")) {
						return "Literal used in an operation other than LD on line "
								+ lineCounter + ".";
					}

					// Get just the literal value with no space on the end.
					String temp = overSubstring(read, index3 + 1, read.length())
							.trim();
					String tempString[] = new String[2];

					// Check next to see if it is a decimal value.
					if (temp.charAt(0) == '#') {
						Integer decimalOperand = Integer.parseInt(
								temp.substring(1), 10);
						if (decimalOperand < -32768 || decimalOperand > 32767) {
							return "Decimal out of range on line "
									+ lineCounter + ".";
						}
						tempString[0] = Utility.DecimalValueToHex(Utility
								.convertToTwosComplement(decimalOperand));

						// Check if it is a hex value.
					} else if (temp.charAt(0) == 'x') {
						if (!Utility.isHexString(temp.substring(1))
								|| temp.charAt(1) == '-') {
							return "Invalid hexadecimal value (permitted range is x0-xFFFF) "
									+ "on line " + lineCounter + ".";
						}
						tempString[0] = Utility.DecimalValueToHex(Utility
								.HexToDecimalValue(temp.substring(1)));
					} else {
						return "Literal with incorrect format on line "
								+ lineCounter + ".";
					}

					// Store the literal in the literal table.
					machineTables.literalTable.put(temp, tempString);
				}

				// Write the line of source code to the intermediate file
				// and read the next line.
				bufferedWriter.write(lineCounter + ":" + read);
				bufferedWriter.newLine();
				read = file.readLine();
				lineCounter++;

				// Empty line means there's no .END or the program is formatted
				// incorrectly.
				if (read == null || read.trim().equals("")) {
					return "Program is missing a .END pseudo-op.";
				}

				// Line is a comment. Write line to comments.txt and
				// increment line counter.
			} else {
				bufferedWriterComments.write(lineCounter + "\t" + read);
				bufferedWriterComments.newLine();
				read = file.readLine();
				lineCounter++;
			}
		}

		// Check to see if .END has a label (should not have one).
		if (!overSubstring(read, 0, 9).matches("[ ]{9}")) {
			return "The .END pseudo-op must not have a label (line "
					+ lineCounter + ").";
		}
		// Remove in line comments and write them to comments.txt.
		String comment = ";";
		int index4 = read.indexOf(comment);
		if (index4 != -1) {
			String inLineComment = read.substring(index4);
			bufferedWriterComments.write(lineCounter + "\t-" + inLineComment);
			bufferedWriterComments.newLine();
			read = overSubstring(read, 0, index4);
		}

		// To exit the while loop successfully a .END pseudo op must have been
		// read. Make sure the the operand is a previously defined symbol or a
		// hex value.
		String temp = "";
		if (read.length() > 16) {
			temp = overSubstring(read, 17, 23);
		}
		if (machineTables.symbolTable.containsKey(temp)) {

			// If it is then use the value of this
			// symbol as the starting location.
			machineTables.startingLocation = machineTables.symbolTable
					.get(temp)[0];
		} else {
			if (read.length() > 16) {
				temp = read.substring(17).trim();
			}

			// Check if it is a hex value.
			if (temp.equals("")) {
				machineTables.startingLocation = origin;
			} else if (temp.charAt(0) == 'x') {
				if (!Utility.isHexString(temp.substring(1))) {
					return "Invalid hexadecimal value on line " + lineCounter
							+ ".";
				}
				machineTables.startingLocation = temp.substring(1);
			} else {
				return "Symbol must be previously defined for a .END operation.";
			}
		}

		// Must be within maximum number of symbols allowed.
		int count = machineTables.symbolTable.size();
		if (count > machineTables.MAX_SYMBOLS) {
			return "Maximum number of symbols(" + machineTables.MAX_SYMBOLS
					+ ") exceeded: " + count + ".";
		}

		// Must be within maximum number of source records allowed.
		count = lineCounter;
		if (count > machineTables.MAX_RECORDS) {
			return "Maximum number of source records("
					+ machineTables.MAX_RECORDS + ") exceeded: " + count + ".";
		}

		// Must be within maximum number of literals allowed.
		count = machineTables.literalTable.size();
		if (count > machineTables.MAX_LITERALS) {
			return "Maximum number of literals(" + machineTables.MAX_LITERALS
					+ ") exceeded: " + count + ".";
		}

		// Set locations in the literal table.
		String keys[] = machineTables.literalTable.keySet().toArray(
				new String[machineTables.literalTable.size()]);
		String tempVal[];
		while (count > 0) {
			tempVal = machineTables.literalTable.remove(keys[count - 1]);
			tempVal[1] = Utility
					.DecimalValueToHex(machineTables.locationCounter);
			machineTables.literalTable.put(keys[count - 1].trim(), tempVal);
			count--;
			machineTables.locationCounter++;
		}
		machineTables.locationCounter--;

		// Check to see that all pgoffset9 addresses are in the same page
		// as the location counter.
		Set<String> pgoffsetKeys = machineTables.passOnePgoffsetCheck.keySet();
		Iterator<String> pgoffsetIterator = pgoffsetKeys.iterator();
		while (pgoffsetIterator.hasNext()) {
			String pgoffsetKey = pgoffsetIterator.next();
			Integer[] pgArray = machineTables.passOnePgoffsetCheck
					.get(pgoffsetKey);
			if (machineTables.symbolTable.containsKey(pgoffsetKey)) {
				String Addr = Utility.DecimalValueToHex(pgArray[0]);
				String locCount = (machineTables.symbolTable.get(pgoffsetKey))[0];
				if (!(isAddrOnSamePage(Addr, locCount))) {
					return "The symbol " + pgoffsetKey.trim() + " on line "
							+ pgArray[1] + " is not on the same page as the "
							+ "location counter.";
				}
			} else {
				return "The symbol " + pgoffsetKey.trim() + " on line "
						+ pgArray[1] + " is never defined.";
			}
		}

		// Make sure the program fits on one page of memory if relative.
		if (machineTables.isRelative) {
			if (!Utility.DecimalValueToHex(machineTables.locationCounter)
					.substring(0, 2).equals(origin.substring(0, 2))) {
				return "Program exceeds one page of memory: Starting location = "
						+ origin
						+ " Final location = "
						+ Utility
								.DecimalValueToHex(machineTables.locationCounter)
						+ ".";
			}
		}

		// Symbol table correction.
		String tempArray2[] = machineTables.symbolTable.keySet().toArray(
				new String[machineTables.symbolTable.size()]);
		for (String tempStr : tempArray2) {
			machineTables.symbolTable.put(tempStr.trim(),
					machineTables.symbolTable.get(tempStr));
		}

		// Write the .END line of source code to the intermediate file.
		bufferedWriter.write(lineCounter + ":" + read);
		bufferedWriter.newLine();

		// Close IO streams.
		bufferedWriter.close();
		bufferedWriterComments.close();
		reader.close();
		file.close();

		// Finish with no errors by returning null.
		return null;
	}

	private static String overSubstring(String str, int x, int y) {
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

	private static boolean isAddrOnSamePage(String addr, String locationCounter) {
		String addrBinary = Utility.HexToBinary(addr).substring(0, 7);
		String locationCounterBinary = Utility.HexToBinary(locationCounter)
				.substring(0, 7);
		return (addrBinary.equals(locationCounterBinary));
	}

	private static boolean isAddrOperation(String operation) {
		boolean result = false;
		if (operation.contains("BR") || operation.contains("JSR")
				|| operation.contains("JMP") || operation.contains("LD")
				|| operation.contains("LDI") || operation.contains("LEA")
				|| operation.contains("ST") || operation.contains("STI")) {
			result = true;
		}
		if (operation.contains("JSRR") || operation.contains("JMPR")
				|| operation.contains("LDR") || operation.contains("STR")) {
			result = false;
		}
		return result;
	}
}
