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

import javax.swing.JOptionPane;

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
		String orgaoJulgador = driver.findElement(By.xpath("/html/body/div/div[4]/table/tbody/tr[3]/td[2]")).getText();
		JOptionPane.showMessageDialog(null, orgaoJulgador);
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
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[\" + i + \"]/td/div/span/span[1]")));
				driver.findElement(By.xpath("//tr[" + i + "]/td/div/span/span[1]")).click();

				//Verifica se a movimentação é do tipo PDF
				Boolean movimentacaoTemPDF = driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span/span[2]")).getText().toUpperCase().contains("PDF");
				
				//CAPA
				if (movimentacaoTemPDF) {
					pdf.apagarPDF();
					JOptionPane.showMessageDialog(null, "MOV PDF");
					if (pdf.PDFBaixado()) {
						JOptionPane.showMessageDialog(null, "PDF BAIXADO!");
						documentoPeticaoInicial = pdf.lerPDF();
						documentoPeticaoInicial = tratamento.tratamento(documentoPeticaoInicial);
					} else {
						JOptionPane.showMessageDialog(null, "PDF NOPE BAIXADO!");
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
								JOptionPane.showMessageDialog(null, "PDF BAIXADO!");
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
					String nucleo = resultado.getEtiqueta();
					resultado = triagemPadrao(driver, wait, config, banco, i, false, nucleo);
					resultado.setDriver(driver);
					return resultado;
				}

			}
		}
		resultado.setDriver(driver);
		return resultado;

	}

	private Chaves_Resultado verificarNucleo(String processo, String orgaoJulgador, String banco){
		JOptionPane.showMessageDialog(null, "CONDIÇÃO VÁLIDA");
		Triagem_Etiquetas triagem = new Triagem_Etiquetas();
		//Identifica a matéria e salva na variável resultado
		Chaves_Resultado resultado = triagem.triarBanco(processo, banco, localTriagem, "PETIÇÃO INCIAL");
		
		String nucleo = resultado.getEtiqueta();
		JOptionPane.showMessageDialog(null, nucleo);
			
		if (nucleo.contains("SSEAS") && (orgaoJulgador.contains("JUIZADO ESPECIAL") || orgaoJulgador.contains("VARA FEDERAL"))) {
			return resultado;	
		}

		if (nucleo.contains("SBI") && (orgaoJulgador.contains("JUIZADO ESPECIAL"))) {
			return resultado;	
		}

		if (nucleo.contains("NÃO FOI POSSÍVEL")) {
			return resultado;
		}
		
		resultado.setEtiqueta("SCC");
		return resultado;
		

	}
	/*
	private Chaves_Resultado verificarConteudoPeticao(String processo, String orgaoJulgador, String banco,
			WebDriver driver, WebDriverWait wait, Chaves_Configuracao config, int indexPeticao, Tratamento tratamento,
			Chaves_Resultado resultado, boolean citacao, boolean intimacao, boolean laudoRecente, boolean dentroDaPasta)
			throws HeadlessException, SQLException, InterruptedException, UnsupportedFlavorException, IOException {
		StringBuilder stringBuilder;
		Triagem_Etiquetas triagem = new Triagem_Etiquetas();
		Triagem_Condicao condicao = new Triagem_Condicao();
		if (condicao.verificaCondicao(processo, "PET")) {
			processo = tratamento.tratamento(processo);
			//Busca o núcleo
			resultado = triagem.triarBanco(processo, banco, localTriagem, "PETIÇÃO INICIAL");
			String subnucleo = resultado.getEtiqueta();
			if (debugPi.isDebugpi()) {
				JOptionPane.showMessageDialog(null, "Condição Válida", "Petição Inicial Identificada", 1);
				JOptionPane.showMessageDialog(null, "Núcleo Identificado: " + subnucleo, "Identificador de Núcleo", 1);
				JOptionPane.showMessageDialog(null, "Palavra Chave: " + resultado.getPalavraChave() + "\nComplemento: "
						+ resultado.getComplemento(), "Combinação de Chaves Identificadas", 1);
				JOptionPane.showMessageDialog(null,
						"Citação: " + citacao + "\nIntimação: " + intimacao + "\nLaudo Recente: " + laudoRecente, "Condicionais", 1);
				JOptionPane.showMessageDialog(null, "Órgão Julgador Identificado: " + orgaoJulgador, "Órgão Julgador", 1);
			} 
			if (subnucleo.contains("SSEAS")
					&& (orgaoJulgador.contains("JUIZADO ESPECIAL") || orgaoJulgador.contains("VARA FEDERAL"))) {
				if (debugPi.isDebugpi()) {
					JOptionPane.showMessageDialog(null, "Iniciando Tratamento para o núcleo SSEAS", "Tratamento", 1);
				}
				if (citacao) {
					//stringBuilder = new StringBuilder(resultado.getEtiqueta());
					//stringBuilder.insert(0, "EATE/");
					resultado.setEtiqueta("EATE/SSEAS:CITAÇÃO");
				} else if (intimacao) {
					resultado = invocarTriagemPadrao(driver, wait, config, banco, indexPeticao, dentroDaPasta);
					stringBuilder = new StringBuilder(resultado.getEtiqueta());
					stringBuilder.insert(0, "GEAC/SSEAS:");
					resultado.setEtiqueta(stringBuilder.toString());
				} else {
					resultado = invocarTriagemPadrao(driver, wait, config, banco, indexPeticao, dentroDaPasta);
					if (resultado.getEtiqueta().toUpperCase().contains("CITAÇÃO")) {
						stringBuilder = new StringBuilder(resultado.getEtiqueta());
						stringBuilder.insert(0, "EATE/SSEAS:");
						resultado.setEtiqueta(stringBuilder.toString());
					} else if (resultado.getEtiqueta().toUpperCase().contains("INTIMAÇÃO")) {
						stringBuilder = new StringBuilder(resultado.getEtiqueta());
						stringBuilder.insert(0, "GEAC/SSEAS:");
						resultado.setEtiqueta(stringBuilder.toString());
					} else {
						stringBuilder = new StringBuilder(resultado.getEtiqueta());
						stringBuilder.insert(0, "SSEAS:");
						resultado.setEtiqueta(stringBuilder.toString());
					}
				}

			} else if (subnucleo.contains("SBI") && orgaoJulgador.contains("JUIZADO ESPECIAL")) {
				if (debugPi.isDebugpi()) {
					JOptionPane.showMessageDialog(null, "Iniciando Tratamento para o núcleo SBI", "Tratamento", 1);
				}
				if (laudoRecente) {
					//stringBuilder = new StringBuilder(resultado.getEtiqueta());
					//stringBuilder.insert(0, "EATE/");
					resultado.setEtiqueta("EATE/SBI:LAUDO");
				} else if (citacao) {
					resultado.setEtiqueta("EATE/SBI:CITAÇÃO");
				} else if (intimacao) {
					resultado = invocarTriagemPadrao(driver, wait, config, banco, indexPeticao, dentroDaPasta);
					stringBuilder = new StringBuilder(resultado.getEtiqueta());
					stringBuilder.insert(0, "GEAC/SBI:");
					resultado.setEtiqueta(stringBuilder.toString());
				} else {
					resultado = invocarTriagemPadrao(driver, wait, config, banco, indexPeticao, dentroDaPasta);
					if (resultado.getEtiqueta().toUpperCase().contains("CITAÇÃO")) {
						stringBuilder = new StringBuilder(resultado.getEtiqueta());
						stringBuilder.insert(0, "EATE/SBI:");
						resultado.setEtiqueta(stringBuilder.toString());
					} else if (resultado.getEtiqueta().toUpperCase().contains("INTIMAÇÃO")) {
						stringBuilder = new StringBuilder(resultado.getEtiqueta());
						stringBuilder.insert(0, "GEAC/SBI:");
						resultado.setEtiqueta(stringBuilder.toString());
					} else {
						stringBuilder = new StringBuilder(resultado.getEtiqueta());
						stringBuilder.insert(0, "SBI:");
						resultado.setEtiqueta(stringBuilder.toString());
					}
				}
			} else if (subnucleo.contains("NÃO FOI POSSÍVEL")) {
				if (debugPi.isDebugpi()) {
					JOptionPane.showMessageDialog(null, "Não foi possível identificar o núcleo");
				}
				resultado.setEtiqueta("NÃO FOI POSSÍVEL IDENTIFICAR A MATÉRIA");
				resultado.setDriver(driver);
				return resultado;
			} else {
				if (debugPi.isDebugpi()) {
					JOptionPane.showMessageDialog(null, "Iniciando Tratamento para o núcleo SCC", "Tratamento", 1);
				}
				if (citacao) {
					resultado.setEtiqueta("EATE/SCC:CITAÇÃO");
				} else if (intimacao) {
					resultado = invocarTriagemPadrao(driver, wait, config, banco, indexPeticao, dentroDaPasta);
					stringBuilder = new StringBuilder(resultado.getEtiqueta());
					stringBuilder.insert(0, "GEAC/SCC:");
					resultado.setEtiqueta(stringBuilder.toString());
				} else {
					resultado = invocarTriagemPadrao(driver, wait, config, banco, indexPeticao, dentroDaPasta);
					if (resultado.getEtiqueta().toUpperCase().contains("CITAÇÃO")) {
						stringBuilder = new StringBuilder(resultado.getEtiqueta());
						stringBuilder.insert(0, "EATE/SCC:");
						resultado.setEtiqueta(stringBuilder.toString());
					} else if (resultado.getEtiqueta().toUpperCase().contains("INTIMAÇÃO")) {
						stringBuilder = new StringBuilder(resultado.getEtiqueta());
						stringBuilder.insert(0, "GEAC/SCC:");
						resultado.setEtiqueta(stringBuilder.toString());
					} else {
						stringBuilder = new StringBuilder(resultado.getEtiqueta());
						stringBuilder.insert(0, "SCC:");
						resultado.setEtiqueta(stringBuilder.toString());
					}
				}
			}
			if(debugPi.isDebugpi()) {
				JOptionPane.showMessageDialog(null, "Etiqueta Final: " + resultado.getEtiqueta(), "Etiqueta Final", 1);
			}
			String verificarPJE = resultado.getEtiqueta().toUpperCase().replaceAll(" ", "");
			if (verificarPJE.contains("PJE-FEDERAL")
					|| verificarPJE.contains("PJE-ESTADUAL")) {
				String etiquetaComposta[] = resultado.getEtiqueta().split(":");
				try {
					resultado.setEtiqueta(etiquetaComposta[1]);
				} catch (Exception e) {
					//
				}
				if(debugPi.isDebugpi()) {
					JOptionPane.showMessageDialog(null, "Etiqueta editada (FEDERAL/ESTADUAL identificado): " + resultado.getEtiqueta(), "Etiqueta Final", 1);
				}
			}
		}
		resultado.setDriver(driver);
		return resultado;
	}
	*/

	private Chaves_Resultado triagemPadrao(WebDriver driver, WebDriverWait wait, Chaves_Configuracao configs,
			String banco, int indexPeticao, boolean dentroDaPasta, String nucleo)
			throws InterruptedException, SQLException, UnsupportedFlavorException, IOException {
		Processo_Movimentacao pm = new Processo_Movimentacao();
		Processo_Documento pd = new Processo_Documento();
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
			Thread.sleep(500);
			resultado = pm.movimentacao(driver, wait, configs, banco);
			break;
		case "DOC":
			//System.out.println("chamando documento 403");
			resultado = pd.documento(driver, wait, configs, banco);
			break;
		case "COM":
			//System.out.println("chamando movimentação 406");
			resultado = pm.movimentacao(driver, wait, configs, banco);
			if (resultado.getEtiqueta().contains("NÃO FOI POSSÍVEL LOCALIZAR FRASE CHAVE ATUALIZADA")) {
				//System.out.println("chamando documento 409");
				resultado = pd.documento(driver, wait, configs, banco);
			}
			break;
		}

		String etiqueta = resultado.getEtiqueta().toUpperCase().replaceAll("-", "").replaceAll(" ", "");

		if(etiqueta.contains("PJEFEDERAL") || etiqueta.contains("PJEPAR")){
			resultado.setDriver(driver);
			return resultado;
		}

		StringBuilder sb = new StringBuilder(resultado.getEtiqueta());

		switch(nucleo) {
			case "SSEAS":
				sb.insert(0, "SSEAS/");
				resultado.setEtiqueta(sb.toString());
				break;
			case "SBI":
				sb.insert(0, "SBI/");
				resultado.setEtiqueta(sb.toString());
				break;
			case "SCC":
				sb.insert(0, "SCC/");
				resultado.setEtiqueta(sb.toString());
				break;
		}

		resultado.setDriver(driver);
		return resultado;
		
	}
}
