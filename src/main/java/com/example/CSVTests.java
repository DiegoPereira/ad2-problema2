package main.java.com.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.csvreader.CsvWriter;

public class CSVTests{
	
	public static void escreve(String ano, String tag, String nomeMusica){
		String outputFile = "mainDados.csv";
		
		// before we open the file check to see if it already exists
		boolean alreadyExists = new File(outputFile).exists();
			
		try {
			// use FileWriter constructor that specifies open for appending
			CsvWriter csvOutput = new CsvWriter(new FileWriter(outputFile, true), ',');
			
			// if the file didn't already exist then we need to write out the header line
			if (!alreadyExists)
			{
				csvOutput.write("ano");
				csvOutput.write("tag");
				csvOutput.write("nomeMusica");
				csvOutput.endRecord();
			}
			// else assume that the file already has the correct header line
			
			// write out a few records
			csvOutput.write(ano);
			csvOutput.write(tag);
			csvOutput.write(nomeMusica);
			csvOutput.endRecord();
			
			//csvOutput.write("2");
			//csvOutput.write("John");
			//csvOutput.endRecord();
			
			csvOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
