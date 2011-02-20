import java.util.HashMap;


public class Tables {
	
	public HashMap<String, char[]> machineOpTable;
	
	public HashMap<String, Integer> psuedoOpTable;
	
	public HashMap<String, String[]> symbolTable;
	
	public HashMap<String, String[]> literalTable;
	
	public Integer locationCounter;
	
	public Boolean isRelative;
	
	public Tables() {
		machineOpTable = new HashMap<String, char[]>();
		psuedoOpTable = new HashMap<String, Integer>();
		symbolTable = new HashMap<String, String[]>();
		literalTable = new HashMap<String, String[]>();
		locationCounter = 0;
		isRelative = false;
		
		initMachineOpTable();
		initPsuedoOpTable();
	}
	
	private void initMachineOpTable (){
		char[] tempChar = new char[2];
		
		tempChar[0] = '0';
		tempChar[1] = '1';
		machineOpTable.put("BR   ", tempChar);
		
		tempChar[0] = '0';
		tempChar[1] = '1';
		machineOpTable.put("BRN  ", tempChar);
		
		tempChar[0] = '0';
		tempChar[1] = '1';
		machineOpTable.put("BRZ  ", tempChar);
		
		tempChar[0] = '0';
		tempChar[1] = '1';
		machineOpTable.put("BRP  ", tempChar);
		
		tempChar[0] = '0';
		tempChar[1] = '1';
		machineOpTable.put("BRNZ ", tempChar);
		
		tempChar[0] = '0';
		tempChar[1] = '1';
		machineOpTable.put("BRNP ", tempChar);
		
		tempChar[0] = '0';
		tempChar[1] = '1';
		machineOpTable.put("BRZP ", tempChar);
		
		tempChar[0] = '0';
		tempChar[1] = '1';
		machineOpTable.put("BRNZP", tempChar);
		
		tempChar[0] = '0';
		tempChar[1] = '0';
		machineOpTable.put("NOP  ", tempChar);
		
		tempChar[0] = '1';
		tempChar[1] = '3';
		machineOpTable.put("ADD  ", tempChar);
		
		tempChar[0] = '2';
		tempChar[1] = '2';
		machineOpTable.put("LD   ", tempChar);
		
		tempChar[0] = '3';
		tempChar[1] = '2';
		machineOpTable.put("ST   ", tempChar);
		
		tempChar[0] = '4';
		tempChar[1] = '1';
		machineOpTable.put("JSR  ", tempChar);
		
		tempChar[0] = '4';
		tempChar[1] = '1';
		machineOpTable.put("JMP  ", tempChar);

		tempChar[0] = '5';
		tempChar[1] = '3';
		machineOpTable.put("AND  ", tempChar);
		
		tempChar[0] = '6';
		tempChar[1] = '3';
		machineOpTable.put("LDR  ", tempChar);
		
		tempChar[0] = '7';
		tempChar[1] = '3';
		machineOpTable.put("STR  ", tempChar);
		
		tempChar[0] = '8';
		tempChar[1] = '0';
		machineOpTable.put("DBUG ", tempChar);
		
		tempChar[0] = '9';
		tempChar[1] = '2';
		machineOpTable.put("NOT  ", tempChar);

		tempChar[0] = 'A';
		tempChar[1] = '2';
		machineOpTable.put("LDI  ", tempChar);
		
		tempChar[0] = 'B';
		tempChar[1] = '2';
		machineOpTable.put("STI  ", tempChar);
		
		tempChar[0] = 'C';
		tempChar[1] = '2';
		machineOpTable.put("JMPR ", tempChar);
		
		tempChar[0] = 'C';
		tempChar[1] = '2';
		machineOpTable.put("JSRR ", tempChar);
		
		tempChar[0] = 'D';
		tempChar[1] = '0';
		machineOpTable.put("RET  ", tempChar);
		
		tempChar[0] = 'E';
		tempChar[1] = '2';
		machineOpTable.put("LEA  ", tempChar);
		
		tempChar[0] = 'F';
		tempChar[1] = '1';
		machineOpTable.put("TRAP ", tempChar);
	}

	private void initPsuedoOpTable (){
		psuedoOpTable.put(".ORIG", 1);
		psuedoOpTable.put(".EQU ", 1);
		psuedoOpTable.put(".BLKW", 1);
		psuedoOpTable.put(".END ", 1);
		psuedoOpTable.put(".STRZ", 1);
		psuedoOpTable.put(".FILL", 1);
	}
}

