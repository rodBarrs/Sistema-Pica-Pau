/**
 * @author Felipe Marques, Gabriel Ramos, Rafael Henrique e Adriano Vilhena
 *
 */
package com.mycompany.newmark;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
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

	public Chaves_Resultado peticaoInicial(WebDriver driver, WebDriverWait wait, String bancos) throws Exception {
		LeituraPDF pdf = new LeituraPDF();
		Chaves_Resultado resultado = new Chaves_Resultado();
		Tratamento tratamento = new Tratamento();
		Triagem_Etiquetas triagem = new Triagem_Etiquetas();
		Triagem_Condicao condicao = new Triagem_Condicao();
		Actions action = new Actions(driver);
		String localTriagem = "PET";
		String localArquivo = "";
		//String linhaMovimentacao = "";
		boolean citacao = false;
		boolean intimacao = false;

		WebElement TabelaTref = null;
		boolean teste = false;
		// Thread.sleep(2000);
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("treeview-1015")));
			TabelaTref = driver.findElement(By.id("treeview-1015"));
		} catch (Exception e) {
			for (int j = 0; j < 2; j++) {
				for (int k = 0; k < 2; k++) {
					// Thread.sleep(2000);
					try {
						wait.until(ExpectedConditions.presenceOfElementLocated(By.id("treeview-1015")));
						TabelaTref = driver.findElement(By.id("treeview-1015"));
						teste = true;
						break;
					} catch (Exception erro) {
					}
				}
				if (teste = true) {
					break;
				} else {
					driver.navigate().refresh();
				}
			}
		}

		TabelaTref = driver.findElement(By.id("treeview-1015"));
		// Identifica as linhas da tabela de movimentação processual <rr>
		List<WebElement> listaMovimentacao = new ArrayList(TabelaTref.findElements(By.cssSelector("tr")));

		// Verifica nas providências jurídicas se existem Citações ou Intimações
		for (int i = listaMovimentacao.size() - 1; i >= 0; i--) {
			if (listaMovimentacao.get(i).getText().contains("CITAÇÃO")) {
				citacao = true;
				break;
			} else if (listaMovimentacao.get(i).getText().contains("INTIMAÇÃO")) {
				intimacao = true;
				break;
			}
		}

		resultado.setLocal("PETIÇÃO INICIAL");
		resultado.setEtiqueta("NÃO FOI POSSÍVEL LOCALIZAR PASTA DE PETIÇÃO INICIAL");
		resultado.setPalavraChave("");
		resultado.setComplemento("");
		// JOptionPane.showMessageDialog(null, listaMovimentacao.size());
		// FOR - Enquantou houve elementos na tabela, do primeiro ao último
		for (int i = 1; i < listaMovimentacao.size(); i++) {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[" + i + "]/td[2]/div/span/span[1]")));
			// IF - Busca pelas expressões descritas, dentro das <tr> da movimentação
			if (driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span/span[1]")).getText().toUpperCase()
					.contains("PETIÇÃO INICIAL")) {
				resultado.setEtiqueta("NÃO FOI POSSÍVEL LOCALIZAR ARQUIVO DE PETIÇÃO INICIAL");
				// Clica no <tr> identificado
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[" + i + "]/td/div")));
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[" + i + "]/td/div")));
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("ext-gen1020")));
				driver.findElement(By.xpath("//tr[" + i + "]/td/div")).click();

				boolean flag = true;
				do {
					try {
						driver.findElement(By.id("iframe-myiframe")).click();
						flag = false;
					} catch (Exception e) {
						// Nothing to do at all
					}

				} while (flag);

				Thread.sleep(500);
				action.keyDown(Keys.CONTROL).sendKeys(String.valueOf('\u0061')).perform();
				action.keyDown(Keys.CONTROL).sendKeys(String.valueOf('\u0063')).perform();
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				DataFlavor flavor = DataFlavor.stringFlavor;
				String BuscaPeticaoInicial = "";
				String BuscaPeticaoInicialSemTratamento = "";
				BuscaPeticaoInicialSemTratamento = clipboard.getData(flavor).toString();
				BuscaPeticaoInicial = clipboard.getData(flavor).toString();
				BuscaPeticaoInicial = tratamento.tratamento(BuscaPeticaoInicial);

				// If - Verifica se existe o termo "Petição" na variável BuscaPeticaoInicial
				// para seguir a tragem especifica

				if (BuscaPeticaoInicial.length() < 250) {
					// CADASTRAR POSSIVEIS VERIFICAÇÕES
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[" + i + "]/td/div")));
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[" + (i + 1) + "]/td/div")));
					int LinhaAtual = Integer.parseInt(driver.findElement(By.xpath("//tr[" + i + "]/td/div")).getText()); // Armazena a do FRONT em que está a movimentação
					int LinhaProxima = Integer
							.parseInt(driver.findElement(By.xpath("//tr[" + (i + 1) + "]/td/div")).getText()); // Armazena o valor da PROXIMA linha do front

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
						localArquivo = driver.findElement(By.xpath("//tr[" + j + "]/td[2]/div/span/span[1]")).getText();
						driver.findElement(By.xpath("//tr[" + j + "]/td[2]/div/span/span[2]/span")).click();

						boolean flag2 = true;

						while (flag2) {
							int resultadoPDF = pdf.verificarExistenciaPDF();
							System.out.println("resultadoPDF: " + resultadoPDF);
							switch (resultadoPDF) {
							case 0:
								System.out.println("ZERO");
								flag2 = false;
								throw new Exception("PDF Não encontrado");
							case 1:
								System.out.println("UM");
								flag2 = false;
								String processo = "";
								processo = pdf.lerPDF();

								if (condicao.verificaCondicao(processo, "PET")) {
									//JOptionPane.showMessageDialog(null, "CONDIÇÃO VÁLIDA");
									processo = tratamento.tratamento(processo);
									resultado = triagem.triarBanco(processo, bancos, localTriagem, "PETIÇÃO INICIAL");

									switch (resultado.getEtiqueta().toUpperCase()) {
									case "RURAL":
										System.out.println("RURAL");
										if (citacao) {
											resultado.setEtiqueta(resultado.getEtiqueta() + ":CITAÇÃO");
											resultado.setLocalArquivo(localArquivo);
											resultado.setDriver(driver);
											return resultado;
										} else if (intimacao) {
											resultado.setEtiqueta(resultado.getEtiqueta() + ":INTIMAÇÃO");
											resultado.setLocalArquivo(localArquivo);
											resultado.setDriver(driver);
											return resultado;
										}
									case "LOAS":
										System.out.println("LOAS");
										if (citacao) {
											resultado.setEtiqueta(resultado.getEtiqueta() + ":CITAÇÃO");
											resultado.setLocalArquivo(localArquivo);
											resultado.setDriver(driver);
											return resultado;
										} else if (intimacao) {
											resultado.setEtiqueta(resultado.getEtiqueta() + ":INTIMAÇÃO");
											resultado.setLocalArquivo(localArquivo);
											resultado.setDriver(driver);
											return resultado;
										}
									default:
										System.out.println("DEFAULT");
										resultado.setDriver(driver);
										resultado.setLocalArquivo(localArquivo);
										return resultado;
									}
								}
								break;
							case 2:
								System.out.println("DOIS");
								pdf.apagarPDF();
								driver.findElement(By.xpath("//tr[" + j + "]/td[2]/div/span/span[2]/span")).click();
								break;
							}
						}
					}
				} else if (condicao.verificaCondicao(BuscaPeticaoInicialSemTratamento, "PET")) {
					localArquivo = driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span/span[1]")).getText();
					BuscaPeticaoInicialSemTratamento = tratamento.tratamento(BuscaPeticaoInicialSemTratamento);
					resultado = triagem.triarBanco(BuscaPeticaoInicialSemTratamento, bancos, localTriagem,
							"PETIÇÃO INICIAL");
					if (resultado.getEtiqueta().toUpperCase().contains("RURAL")) {
						JOptionPane.showMessageDialog(null, "RURAL");
						if (citacao) {
							resultado.setEtiqueta(resultado.getEtiqueta() + ":CITAÇÃO");
							resultado.setLocalArquivo(localArquivo);
							resultado.setDriver(driver);
							return resultado;
						} else if (intimacao) {
							resultado.setEtiqueta(resultado.getEtiqueta() + ":INTIMAÇÃO");
							resultado.setLocalArquivo(localArquivo);
							resultado.setDriver(driver);
							return resultado;
						}

					} else if (resultado.getEtiqueta().toUpperCase().contains("LOAS")) {

						if (citacao) {
							resultado.setEtiqueta(resultado.getEtiqueta() + ":CITAÇÃO");
							resultado.setLocalArquivo(localArquivo);
							resultado.setDriver(driver);
							return resultado;
						} else if (intimacao) {
							resultado.setEtiqueta(resultado.getEtiqueta() + ":INTIMAÇÃO");
							resultado.setLocalArquivo(localArquivo);
							resultado.setDriver(driver);
							return resultado;
						}

					} else {
						resultado.setDriver(driver);
						resultado.setLocalArquivo(localArquivo);
						return resultado;
					}
				}

			}
		}
		resultado.setDriver(driver);
		return resultado;
	}

}
