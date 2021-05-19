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

public class Processo_Etiquetar {

    public void etiquetar(WebDriver driver, WebDriverWait wait, Chaves_Resultado resultado) throws InterruptedException {
        Actions action = new Actions(driver);
        
        try {
        	resultado.setPetição(resultado.getPetição().substring(0, 15));
        } catch (Exception e) {
        	//
        }
        
        action.doubleClick(driver.findElement(By.xpath("//td[5]/div"))).build().perform();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//fieldset[5]/div/span/div/table[4]/tbody/tr/td[2]/input")));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div/div/span/div/table/tbody/tr/td[2]/textarea")));
        driver.findElement(By.xpath("//fieldset[5]/div/span/div/table[4]/tbody/tr/td[2]/input")).clear();
        driver.findElement(By.xpath("//fieldset[5]/div/span/div/table[4]/tbody/tr/td[2]/input")).sendKeys(resultado.getEtiqueta());
        driver.findElement(By.xpath("//div/div/span/div/table/tbody/tr/td[2]/textarea")).clear();
        driver.findElement(By.xpath("//div/div/span/div/table/tbody/tr/td[2]/textarea")).sendKeys("MARK:"
                + "\nFC: " + resultado.getPalavraChave() + ";"
                + "\nCOMP: " + resultado.getComplemento() + ";"
                + "\nLOCAL: " + resultado.getLocal() + ";"
                + "\nINDEX PI: " + Chaves_Resultado.getNomePeticao() + ".");
        action.sendKeys(Keys.TAB).build().perform();
        action.sendKeys(Keys.TAB).build().perform();
        action.sendKeys(Keys.ENTER).build().perform();
    }
}
