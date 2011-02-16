import java.io.IOException;

public class WilevenAssembler {

	/**
	 * @param args
	 *            The first argument is for the name of the assembly source code
	 *            file. The second argument is for the name of the output file.
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		Tables machineTables = new Tables();
		
		String firstPassError = PassOne.run(args[0], machineTables);
		
		String secondPassError = PassTwo.Output(args[1], machineTables);
	}
}
