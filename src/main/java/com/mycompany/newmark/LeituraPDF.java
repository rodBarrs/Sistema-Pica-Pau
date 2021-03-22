package com.mycompany.newmark;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class LeituraPDF {
	File filePath = new File("C:\\Temp");
	public String verificarExistenciaPDF() throws InterruptedException {
		System.out.println("============ VERIFICANDO PDF ==============");
		for (int i = 0; i < 5; i++) {
			Thread.sleep(2000);
			System.out.println("verificando pdf...");
			if (filePath.listFiles().length == 1) {
				for (File pdfNome : filePath.listFiles()) {
					if(pdfNome.getName().contains(".pdf")) {
						System.out.println("pdf encontrado: " + pdfNome.getName());
						System.out.println("retornando 1");
						return "PdfEncontrado";
					}
				}
			}
			
		}
		
		if(filePath.listFiles().length > 1) {
			System.out.println("retornando 2");
			return "MaisDeUmPdfEncontrado";
		}
		return "NenhumPdfEncontrado";
	}
	
	public String lerPDF() throws IOException {
		boolean flag = true;
		String textoPDF = "";
		PDDocument document;
		PDFTextStripper tStripper = new PDFTextStripper();

		for (File file : filePath.listFiles()) {
			String path = file.getAbsolutePath();
			System.out.println(path);
			while(flag) {
				try {
					document = PDDocument.load(new File(path));
					flag = false;
					textoPDF = tStripper.getText(document);
					//System.out.println(textoPDF);
					document.close();
				} catch (Exception e) {
					flag = true;
				}
			}
	
		}
		return textoPDF;
	}
	
	public void apagarPDF() throws InterruptedException {
		for(File file : filePath.listFiles()) {
			file.delete();
		}
	}
}
