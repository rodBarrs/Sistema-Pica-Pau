/**
 * @author Felipe Marques, Gabriel Ramos, Rafael Henrique e Adriano Vilhena
 *
 * Classe responsável por etiquetar o processo no grid após a triagem
 */
package com.mycompany.newmark;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.mycompany.newmark.system.Sistema;

public class Processo_Etiquetar {

	public void etiquetar(WebDriver driver, WebDriverWait wait, Chaves_Resultado resultado)
			throws InterruptedException {
		Actions action = new Actions(driver);
		action.doubleClick(driver.findElement(By.xpath("//td[5]/div"))).build().perform();
		wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//fieldset[5]/div/span/div/table[4]/tbody/tr/td[2]/input")));
		wait.until(
				ExpectedConditions.elementToBeClickable(By.xpath("//div/div/span/div/table/tbody/tr/td[2]/textarea")));

		///////////////////////
		String preEtiqueta = driver.findElement(By.xpath("//fieldset[5]/div/span/div/table[4]/tbody/tr/td[2]/input")).getAttribute("value");

		System.out.println("OLHA AQUI: " + preEtiqueta);

		if (preEtiqueta.contains("CIT")) {
			preEtiqueta = "PJE CITAÇÃO - ";
		} else if (preEtiqueta.contains("INT")) {
			preEtiqueta = "PJE INTIMAÇÃO - ";
		} else {
			preEtiqueta = "";
		}
		resultado.setEtiqueta(preEtiqueta + resultado.getEtiqueta());
//
//		if (preEtiqueta.contains("CIT")) {
//			preEtiqueta = "rodcadu PJE CITAÇÃO - ";
//		} else if (preEtiqueta.contains("INT")) {
//			preEtiqueta = "rodcadu PJE INTIMAÇÃO - ";
//		} else {
//			preEtiqueta = "rodcadu -";
//		}
//		resultado.setEtiqueta(preEtiqueta + resultado.getEtiqueta());

		///////////////////////
		
		driver.findElement(By.xpath("//fieldset[5]/div/span/div/table[4]/tbody/tr/td[2]/input")).clear();

		driver.findElement(By.xpath("//fieldset[5]/div/span/div/table[4]/tbody/tr/td[2]/input"))
				.sendKeys(resultado.getEtiqueta());
		driver.findElement(By.xpath("//div/div/span/div/table/tbody/tr/td[2]/textarea")).clear();
		driver.findElement(By.xpath("//div/div/span/div/table/tbody/tr/td[2]/textarea"))
				.sendKeys("MARK" + Sistema.VERSAO + "\nSEQ PETIÇÃO INICIAL: " + Chaves_Resultado.getSeqPeticao()
						+ "\nFRASE CHAVE PETIÇÃO INICIAL: " + Chaves_Resultado.getPalavraChavePeticao());
		
// CASO HAJA TRIAGEM PADRÃO
//		driver.findElement(By.xpath("//div/div/span/div/table/tbody/tr/td[2]/textarea"))
//		.sendKeys("MARK" + Sistema.VERSAO + "\nSEQ PETIÇÃO INICIAL: " + Chaves_Resultado.getSeqPeticao()
//		+ "\nFRASE CHAVE PETIÇÃO INICIAL: " + Chaves_Resultado.getPalavraChavePeticao() + "\nSEQ: "
//		+ resultado.getLocal() + "\nFRASE CHAVE: " + resultado.getPalavraChave());

		action.sendKeys(Keys.TAB).build().perform();
		action.sendKeys(Keys.TAB).build().perform();
		action.sendKeys(Keys.ENTER).build().perform();
	}
}
