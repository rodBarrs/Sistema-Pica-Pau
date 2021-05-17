/**
 * @author Felipe Marques, Gabriel Ramos, Rafael Henrique e Adriano Vilhena
 *
 */
package com.mycompany.newmark;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Processo_PeticaoInicial {
	private String localTriagem = "PET";

	public Chaves_Resultado peticaoInicial(WebDriver driver, WebDriverWait wait, Chaves_Configuracao config,
			String banco) throws Exception {
		Tratamento tratamento = new Tratamento();
		Triagem_Condicao cond = new Triagem_Condicao();
		Chaves_Resultado resultado = new Chaves_Resultado();
		LeituraPDF pdf = new LeituraPDF();
		Actions action = new Actions(driver);
		String documentoPeticaoInicial = "";
		//Aguarda até que tabela com as movimentações (treeview) esteja carregada
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("treeview-1015")));

		//Armazena todas as movimentações num ArrayList
		WebElement TabelaTref = driver.findElement(By.id("treeview-1015"));
		List<WebElement> listaMovimentacao = new ArrayList<WebElement>(TabelaTref.findElements(By.cssSelector("tr")));
			
		//Aguarda até que o iframe esteja carregado e então envia o Driver para o iframe (para que possa interagir com o interior do iframe)
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("iframe-myiframe")));
		WebElement capa = driver.findElement(By.id("iframe-myiframe"));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(capa));

		//Aguarda até que o campo "órgão julgador" esteja carregado e então salva seu conteúdo
		String orgaoJulgador = "";
		try {
			orgaoJulgador = driver.findElement(By.xpath("/html/body/div/div[4]/table/tbody/tr[3]/td[2]")).getText();
		} catch (Exception e) {
			orgaoJulgador = driver.findElement(By.xpath("/html/body/div/div[5]/table/tbody/tr[3]/td[2]")).getText();
		}
		//Devolve o driver para a página
		driver.switchTo().defaultContent();
		
		//Seta previamente a etiqueta com erro
		resultado.setLocal("PETIÇÃO INICIAL");
		resultado.setEtiqueta("NÃO FOI POSSÍVEL LOCALIZAR PASTA DE PETIÇÃO INICIAL");
		resultado.setPalavraChave("");
		resultado.setComplemento("");
		
		//Verifica a existência de uma pasta na posição 1 do sapiens (isso significaria que existe uma possível petição inicial com nome diferente)
		Boolean existePasta = driver.findElement(By.xpath("//tr[2]/td[2]/div/img[1]")).getAttribute("class").contains("x-tree-expander");
		//Itera a lista de movimentação procurando por "Petição Inicial" ou uma pasta no index 1
		for (int i = 2; i < listaMovimentacao.size(); i++) {

			//Providência Jurídica é o título da movimentação
			Boolean existePeticaoInicial = driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span")).getText().toUpperCase().contains("PETIÇÃO INICIAL");
			if (existePeticaoInicial || existePasta) {

				resultado.setEtiqueta("NÃO FOI POSSÍVEL LOCALIZAR ARQUIVO DE PETIÇÃO INICIAL");

				//Clica no título da Providência Jurídica 
				
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("ext-gen1020")));
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[\" + i + \"]/td/div/span/span[1]")));
				driver.findElement(By.xpath("//tr[" + i + "]/td/div/span/span[1]")).click();

				//Verifica se a movimentação é do tipo PDF
				Boolean movimentacaoTemPDF = driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span/span[2]")).getText().toUpperCase().contains("PDF");
				
				//CAPA
				if (movimentacaoTemPDF) {
					pdf.apagarPDF();
					if (pdf.PDFBaixado()) {
						documentoPeticaoInicial = pdf.lerPDF();
						documentoPeticaoInicial = tratamento.tratamento(documentoPeticaoInicial);
					} else {
					}
				} else {
					wait.until(ExpectedConditions.presenceOfElementLocated(By.id("iframe-myiframe")));
					WebElement iframe = driver.findElement(By.id("iframe-myiframe"));
					wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframe));
					
					//Laço para garantir que o clique seja feito no HTML dentro do iframe
					//Isso garante que o conteúdo do iframe que contém o documento da movimentação clicada já carregou
					boolean flag = true;
					do {
						try {
							wait.until(ExpectedConditions.elementToBeClickable(By.tagName("html")));
							wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
							driver.findElement(By.tagName("html")).click();
							flag = false;
						} catch (Exception e) {
							//
						}
					} while (flag);

					driver.switchTo().defaultContent();

					Thread.sleep(500);
					action.keyDown(Keys.CONTROL).sendKeys(String.valueOf('\u0061')).perform();
					action.keyDown(Keys.CONTROL).sendKeys(String.valueOf('\u0063')).perform();
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					DataFlavor flavor = DataFlavor.stringFlavor;
					Thread.sleep(500);
					documentoPeticaoInicial = clipboard.getData(flavor).toString().toUpperCase();
					documentoPeticaoInicial = tratamento.tratamento(documentoPeticaoInicial);
				}

				Boolean contemPeticaoInicial = cond.verificaCondicao(documentoPeticaoInicial, "PET");
				if (!contemPeticaoInicial) {

					int proximaLinha = Integer.parseInt(driver.findElement(By.xpath("//tr[" + (i + 1) + "]/td/div")).getText()); 

					//Clica na seta para expandir a pasta
					driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/img[1]")).click();
					
					//Itera pela pasta
					for (int j = i + 1; j < proximaLinha; j++) {
						pdf.apagarPDF();
						wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[" + j + "]/td[2]/div/span/span[1]")));
						wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[" + j + "]/td[2]/div/span/span[1]")));

						Thread.sleep(500);

						//Clica para abrir o PDF
						driver.findElement(By.xpath("//tr[" + j + "]/td[2]/div/span/span[1]")).click();

						Boolean movimentacaoPastaContemPDF = driver.findElement(By.xpath("//tr[" + j + "]/td[2]/div/span/span[2]")).getText().contains("PDF");

						if (movimentacaoPastaContemPDF) {
							Boolean pdfBaixado = pdf.PDFBaixado();
							if(pdfBaixado){
								
								String processo = pdf.lerPDF().toUpperCase();
								if(cond.verificaCondicao(processo, "PET")){
									resultado = verificarNucleo(processo, orgaoJulgador, banco);
									String nucleo = resultado.getEtiqueta();
									resultado = triagemPadrao(driver, wait, config, banco, i, true, nucleo);
									resultado.setDriver(driver);
									return resultado;
								}
							}

						} else {
							wait.until(ExpectedConditions.presenceOfElementLocated(By.id("iframe-myiframe")));
							WebElement iframe = driver.findElement(By.id("iframe-myiframe"));
							wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframe));

							boolean flag = true;
							do {
								try {
									wait.until(ExpectedConditions.elementToBeClickable(By.tagName("html")));
									wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
									driver.findElement(By.tagName("html")).click();
									flag = false;
								} catch (Exception e) {
									//
								}

							} while (flag);

							driver.switchTo().defaultContent();

							Thread.sleep(500);
							action.keyDown(Keys.CONTROL).sendKeys(String.valueOf('\u0061')).perform();
							action.keyDown(Keys.CONTROL).sendKeys(String.valueOf('\u0063')).perform();
							Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
							DataFlavor flavor = DataFlavor.stringFlavor;
							Thread.sleep(500);
							String processo = clipboard.getData(flavor).toString().toUpperCase();
							processo = tratamento.tratamento(processo);

							contemPeticaoInicial = cond.verificaCondicao(processo, "PET");
							if (contemPeticaoInicial) {
								resultado = verificarNucleo(processo, orgaoJulgador, banco);
								String nucleo = resultado.getEtiqueta();
								resultado = triagemPadrao(driver, wait, config, banco, i, true, nucleo);
								resultado.setDriver(driver);
								return resultado;
							}
						}
					}

				} else {
					resultado = verificarNucleo(documentoPeticaoInicial, orgaoJulgador, banco);
					resultado = triagemPadrao(driver, wait, config, banco, i, false, resultado.getEtiqueta());
					resultado.setDriver(driver);
					return resultado;
				}

			}
		}
		resultado.setDriver(driver);
		return resultado;

	}

	private Chaves_Resultado verificarNucleo(String processo, String orgaoJulgador, String banco){
		Triagem_Etiquetas triagem = new Triagem_Etiquetas();
		//Identifica a matéria e salva na variável resultado
		Chaves_Resultado resultado = triagem.triarBanco(processo, banco, localTriagem, "PETIÇÃO INCIAL");
		
		String nucleo = resultado.getEtiqueta();
			
		if (nucleo.contains("SSEAS") && (orgaoJulgador.contains("JUIZADO ESPECIAL") || orgaoJulgador.contains("VARA FEDERAL"))) {
			return resultado;	
		}

		if (nucleo.contains("SBI") && (orgaoJulgador.contains("JUIZADO ESPECIAL"))) {
			return resultado;	
		}

		if (nucleo.contains("NÃO FOI POSSÍVEL")) {
			resultado.setEtiqueta("");
			return resultado;
		}
		
		resultado.setEtiqueta("SCC");
		return resultado;
	}

	private Chaves_Resultado triagemPadrao(WebDriver driver, WebDriverWait wait, Chaves_Configuracao configs,
			String banco, int indexPeticao, boolean dentroDaPasta, String nucleo)
			throws InterruptedException, SQLException, UnsupportedFlavorException, IOException {

		Chaves_Resultado resultado = new Chaves_Resultado();
		
		if (dentroDaPasta) {
			try {
				driver.findElement(By.xpath("//tr[" + indexPeticao + "]/td/div/img")).click();
			} catch (Exception e) {
				//
			}
		}
		
		//Clica na capa
		driver.findElement(By.xpath("//tr[1]/td/div/img")).click();

		switch (configs.getTipoTriagem()) {
		case "MOV":
			//System.out.println("chamando movimentação 400");
			resultado = new Processo_Movimentacao().movimentacao(driver, wait, configs, banco);
			break;
		case "DOC":
			//System.out.println("chamando documento 403");
			resultado = new Processo_Documento().documento(driver, wait, configs, banco);
			break;
		case "COM":
			//System.out.println("chamando movimentação 406");
			resultado = new Processo_Movimentacao().movimentacao(driver, wait, configs, banco);
			if (resultado.getEtiqueta().contains("NÃO FOI POSSÍVEL LOCALIZAR FRASE CHAVE ATUALIZADA")) {
				//System.out.println("chamando documento 409");
				resultado = new Processo_Documento().documento(driver, wait, configs, banco);
			}
			break;
		}

		String etiqueta = resultado.getEtiqueta().toUpperCase().replaceAll("-", "").replaceAll(" ", "");

		if(etiqueta.contains("PJEFEDERAL") || etiqueta.contains("PJEPAR")){
			resultado.setDriver(driver);
			return resultado;
		}
		
		String etiquetaFinal = nucleo + "/" + resultado.getEtiqueta();
		
		resultado.setEtiqueta(etiquetaFinal);
		resultado.setDriver(driver);
		return resultado;
		
	}
}
