package com.mycompany.newmark;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class LeituraPDF {
	
	public String lerPDF() throws IOException {
		String textoPDF = "";
		PDFTextStripper tStripper = new PDFTextStripper();
		for (File file : new File("C:\\Temp").listFiles()) {
			String path = file.getAbsolutePath();
			PDDocument document = PDDocument.load(new File(path));
			textoPDF = tStripper.getText(document);
			document.close();
		}
		apagarPDF();
		return textoPDF;
	}
	
	private void apagarPDF() {
		for(File file : new File("C:\\Temp").listFiles()) {
			file.delete();
		}
	}
}
