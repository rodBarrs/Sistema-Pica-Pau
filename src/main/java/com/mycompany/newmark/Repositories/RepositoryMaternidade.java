package com.mycompany.newmark.Repositories;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RepositoryMaternidade {

    public void clicarDosprev(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS).pageLoadTimeout(30, TimeUnit.SECONDS);
        Thread.sleep(1000);
        List<String> janela = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(janela.get(1));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("treeview-1015")));
        WebElement TabelaTref = driver.findElement(By.id("treeview-1015"));
        List<WebElement> listaMovimentacao = new ArrayList<WebElement>(TabelaTref.findElements(By.cssSelector("tr")));
        for (int i = listaMovimentacao.size(); i > 2; i--) {

            // Providência Jurídica é o título da movimentação
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[" + i + "]/td[2]/div/span")));
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[" + i + "]/td[2]/div/span")));
            if (i > listaMovimentacao.size() - 3) {
                Thread.sleep(500);
            }


            Boolean existeDosPrev = driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span")).getText()
                    .toUpperCase().contains("DOSSIÊ PREVIDENCIÁRIO");
            if (existeDosPrev == true) {
                WebElement dosClick = driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span"));
                dosClick.click();
            }
        }
    }
}

