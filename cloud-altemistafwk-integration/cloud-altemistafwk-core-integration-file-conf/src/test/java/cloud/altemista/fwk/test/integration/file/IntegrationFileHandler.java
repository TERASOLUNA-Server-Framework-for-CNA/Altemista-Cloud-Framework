package cloud.altemista.fwk.test.integration.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntegrationFileHandler {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	public String handleString(String input) {
		logger.info(" ====> Copying text: " + input);
		return input.toUpperCase();
	}
	
	/**
	 * Handler Method witch read and write in a logger the input file content.
	 * @param input
	 * @return
	 * @throws FileNotFoundException
	 */
	
	public File handleFile(File input) throws FileNotFoundException {
		logger.info(" ====> Input file handled: " + input.getAbsolutePath());
		Scanner scanner = new Scanner(input);
		while (scanner.hasNextLine()) {
		   logger.info(" - "+scanner.nextLine());
		}
		scanner.close();
		logger.info(" ====> Copying file: " + input.getAbsolutePath());
		return input;
	}
	
	public byte[] handleBytes(byte[] input) {
		logger.info(" ====> Copying " + input.length + " bytes ...");
		return new String(input).toUpperCase().getBytes();
	}

}