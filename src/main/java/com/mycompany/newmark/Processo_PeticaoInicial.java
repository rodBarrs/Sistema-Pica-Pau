/**
 * @author Felipe Marques, Gabriel Ramos, Rafael Henrique e Adriano Vilhena
 *
 */
package com.mycompany.newmark;

import java.awt.HeadlessException;
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
	private Chaves_Configuracao debugPi;

	public Chaves_Resultado peticaoInicial(WebDriver driver, WebDriverWait wait, Chaves_Configuracao config,
			String bancos, Chaves_Configuracao debugPi) throws Exception {
		Tratamento tratamento = new Tratamento();
		Triagem_Condicao cond = new Triagem_Condicao();
		String localArquivo = "";
		Chaves_Resultado resultado = new Chaves_Resultado();
		LeituraPDF pdf = new LeituraPDF();
		VerificarData verificarData = new VerificarData();
		Actions action = new Actions(driver);
		boolean citacao = false;
		boolean intimacao = false;
		boolean laudoRecente = false;
		this.debugPi = debugPi;
		String documentoPeticaoInicial;

		//Aguarda até que Treeview esteja carregada
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("treeview-1015")));

		//Armazena toda a movimentação num ArrayList
		WebElement TabelaTref = driver.findElement(By.id("treeview-1015"));
		List<WebElement> listaMovimentacao = new ArrayList<WebElement>(TabelaTref.findElements(By.cssSelector("tr")));

		//Aguarda até que o iframe esteja carregado e então envia o Driver para o iframe (para que possa interagir com o interior do iframe)
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("iframe-myiframe")));
		WebElement capa = driver.findElement(By.id("iframe-myiframe"));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(capa));

		//Aguarda até que o campo "órgão julgador" esteja carregado e então salva seu conteúdo
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/div[4]/table/tbody/tr[3]/td[2]")));
		String orgaoJulgador = driver.findElement(By.xpath("/html/body/div/div[4]/table/tbody/tr[3]/td[2]")).getText();
		
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
		for (int i = 1; i < listaMovimentacao.size(); i++) {

			//Providência Jurídica é o título da movimentação
			Boolean existePeticaoInicial = driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span")).getText().toUpperCase().contains("PETIÇÃO INICIAL");
			if (existePeticaoInicial || existePasta) {

				resultado.setEtiqueta("NÃO FOI POSSÍVEL LOCALIZAR ARQUIVO DE PETIÇÃO INICIAL");

				//Clica no título da Providência Jurídica 
				driver.findElement(By.xpath("//tr[" + i + "]/td/div/span/span[1]")).click();

				//Verifica se a movimentação é do tipo PDF
				Boolean movimentacaoTemPDF = driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span/span[2]")).getText().contains("PDF");
				
				if (movimentacaoTemPDF) {
					if (pdf.PDFBaixado()) {
						documentoPeticaoInicial = pdf.lerPDF();
						documentoPeticaoInicial = tratamento.tratamento(documentoPeticaoInicial);
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
					BuscaPeticaoInicialSemTratamento = clipboard.getData(flavor).toString().toUpperCase();
					BuscaPeticaoInicial = BuscaPeticaoInicialSemTratamento;
					BuscaPeticaoInicial = tratamento.tratamento(BuscaPeticaoInicial);
				}
				// If - Verifica se existe o termo "Petição" na variável BuscaPeticaoInicial
				// para seguir a triagem especifica
				if (cond.verificaCondicao(BuscaPeticaoInicialSemTratamento, "PET") == false) {
					//&& (BuscaPeticaoInicial.contains("PETIÇÃO") || BuscaPeticaoInicial.contains("INICIAL")
					//|| BuscaPeticaoInicial.contains("ANEXO") || BuscaPeticaoInicial.contains("PDF"))) 

					// CADASTRAR POSSIVEIS VERIFICAÇÕES
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[" + i + "]/td/div")));
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[" + (i + 1) + "]/td/div")));
					int LinhaAtual = Integer.parseInt(driver.findElement(By.xpath("//tr[" + i + "]/td/div")).getText()); // Armazena a linha do Front em que está a movimentação
					int LinhaProxima = Integer
							.parseInt(driver.findElement(By.xpath("//tr[" + (i + 1) + "]/td/div")).getText()); // Armazena o valor da proxima linha do front

					wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[" + i + "]/td[2]/div/img[1]")));
					driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/img[1]")).click();
					// Laço para iterar dentro da lista aberta pela pasta "Petição Inicial"
					// 'J' recebe a posição atual do Driver mais 1, o que seria o documento seguinte
					// LinhaProxima refere-se ao valor final da lista aberta
					for (int j = i + 1; j <= LinhaProxima; j++) {
						wait.until(ExpectedConditions
								.presenceOfElementLocated(By.xpath("//tr[" + j + "]/td[2]/div/span/span[2]/span")));
						wait.until(ExpectedConditions
								.elementToBeClickable(By.xpath("//tr[" + j + "]/td[2]/div/span/span[2]/span")));
						pdf.apagarPDF();
						Thread.sleep(500);
						driver.findElement(By.xpath("//tr[" + j + "]/td[2]/div/span/span[2]/span")).click();

						if (driver.findElement(By.xpath("//tr[" + j + "]/td[2]/div/span/span[2]")).getText()
								.contains("PDF")) {
							boolean flag2 = true;

							do {
								String resultadoPDF = pdf.verificarExistenciaPDF();
								//System.out.println("resultadoPDF: " + resultadoPDF);

								switch (resultadoPDF) {
								case "NenhumPdfEncontrado":
									//System.out.println("ZERO");
									flag2 = false;
									throw new Exception("PDF Não encontrado");

								case "MaisDeUmPdfEncontrado":
									//System.out.println("DOIS");
									pdf.apagarPDF();
									driver.findElement(By.xpath("//tr[" + j + "]/td[2]/div/span/span[2]/span")).click();
									break;

								case "PdfEncontrado":
									//System.out.println("UM");
									flag2 = false;
									String processo = "";
									processo = pdf.lerPDF().toUpperCase();
									localArquivo = driver
											.findElement(By.xpath("//tr[" + j + "]/td[2]/div/span/span[1]")).getText();
									resultado = verificarConteudoPeticao(processo, orgaoJulgador, bancos, driver, wait,
											config, i, tratamento, resultado, citacao, intimacao, laudoRecente, true);
									if (resultado
											.getEtiqueta() != "NÃO FOI POSSÍVEL LOCALIZAR ARQUIVO DE PETIÇÃO INICIAL") {
										resultado.setPetição(localArquivo);
										return resultado;
									}
								}
							} while (flag2);
						} else {
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
							if (cond.verificaCondicao(processo, "PET")) {
								localArquivo = driver.findElement(By.xpath("//tr[" + j + "]/td[2]/div/span/span[1]"))
										.getText();
								resultado = verificarConteudoPeticao(processo, orgaoJulgador, bancos, driver, wait,
										config, i, tratamento, resultado, citacao, intimacao, laudoRecente, true);
								resultado.setDriver(driver);
								resultado.setPetição(localArquivo);
								return resultado;
							}
						}
					}

				} else {
					localArquivo = driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span/span[1]")).getText();
					resultado = verificarConteudoPeticao(BuscaPeticaoInicialSemTratamento, orgaoJulgador, bancos,
							driver, wait, config, i, tratamento, resultado, citacao, intimacao, laudoRecente, false);
					resultado.setDriver(driver);
					resultado.setPetição(localArquivo);
					return resultado;
				}

			}
		}
		resultado.setDriver(driver);
		return resultado;

	}

	private Chaves_Resultado verificarConteudoPeticao(String processo, String orgaoJulgador, String bancos,
			WebDriver driver, WebDriverWait wait, Chaves_Configuracao config, int indexPeticao, Tratamento tratamento,
			Chaves_Resultado resultado, boolean citacao, boolean intimacao, boolean laudoRecente, boolean dentroDaPasta)
			throws HeadlessException, SQLException, InterruptedException, UnsupportedFlavorException, IOException {
		StringBuilder stringBuilder;
		Triagem_Etiquetas triagem = new Triagem_Etiquetas();
		Triagem_Condicao condicao = new Triagem_Condicao();
		if (condicao.verificaCondicao(processo, "PET")) {
			processo = tratamento.tratamento(processo);
			resultado = triagem.triarBanco(processo, bancos, localTriagem, "PETIÇÃO INICIAL");
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
					resultado = invocarTriagemPadrao(driver, wait, config, bancos, indexPeticao, dentroDaPasta);
					stringBuilder = new StringBuilder(resultado.getEtiqueta());
					stringBuilder.insert(0, "GEAC/SSEAS:");
					resultado.setEtiqueta(stringBuilder.toString());
				} else {
					resultado = invocarTriagemPadrao(driver, wait, config, bancos, indexPeticao, dentroDaPasta);
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
					resultado = invocarTriagemPadrao(driver, wait, config, bancos, indexPeticao, dentroDaPasta);
					stringBuilder = new StringBuilder(resultado.getEtiqueta());
					stringBuilder.insert(0, "GEAC/SBI:");
					resultado.setEtiqueta(stringBuilder.toString());
				} else {
					resultado = invocarTriagemPadrao(driver, wait, config, bancos, indexPeticao, dentroDaPasta);
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
					resultado = invocarTriagemPadrao(driver, wait, config, bancos, indexPeticao, dentroDaPasta);
					stringBuilder = new StringBuilder(resultado.getEtiqueta());
					stringBuilder.insert(0, "GEAC/SCC:");
					resultado.setEtiqueta(stringBuilder.toString());
				} else {
					resultado = invocarTriagemPadrao(driver, wait, config, bancos, indexPeticao, dentroDaPasta);
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

	private Chaves_Resultado invocarTriagemPadrao(WebDriver driver, WebDriverWait wait, Chaves_Configuracao configs,
			String bancos, int indexPeticao, boolean dentroDaPasta)
			throws InterruptedException, SQLException, UnsupportedFlavorException, IOException {
		Processo_Movimentacao pm = new Processo_Movimentacao();
		Processo_Documento pd = new Processo_Documento();
		Chaves_Resultado resultado = new Chaves_Resultado();

		if (dentroDaPasta == true) {
			try {
				driver.findElement(By.xpath("//tr[" + indexPeticao + "]/td/div/img"));
				driver.findElement(By.xpath("//tr[" + indexPeticao + "]/td/div/img")).click();
			} catch (Exception e) {
				//
			}
		}

		driver.findElement(By.xpath("//tr[1]/td/div/img")).click();

		switch (configs.getTipoTriagem()) {
		case "MOV":
			//System.out.println("chamando movimentação 400");
			resultado = pm.movimentacao(driver, wait, configs, bancos);
			break;
		case "DOC":
			//System.out.println("chamando documento 403");
			resultado = pd.documento(driver, wait, configs, bancos);
			break;
		case "COM":
			//System.out.println("chamando movimentação 406");
			resultado = pm.movimentacao(driver, wait, configs, bancos);
			if (resultado.getEtiqueta().contains("NÃO FOI POSSÍVEL LOCALIZAR FRASE CHAVE ATUALIZADA")) {
				//System.out.println("chamando documento 409");
				resultado = pd.documento(driver, wait, configs, bancos);
			}
			break;
		}
		resultado.setDriver(driver);
		return resultado;
		//System.out.println("Chamando Movimentação");
		//resultado = pm.movimentacao(driver, wait, configs, bancos);
		//if (resultado.getEtiqueta().contains("NÃO FOI POSSÍVEL LOCALIZAR FRASE CHAVE ATUALIZADA")) {
		//System.out.println("Chamando Documento");
		//resultado = pd.documento(resultado.getDriver(), wait, configs, bancos);
		//}
		//resultado.setDriver(driver);
		//return resultado;
	}
}
