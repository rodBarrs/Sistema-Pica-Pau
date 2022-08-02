package com.mycompany.newmark.Repositories;

import com.mycompany.newmark.entities.InformacoesDosprev;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class RepositoryMaternidade {

    public String clicarDosprev(WebDriver driver, WebDriverWait wait) throws InterruptedException {
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
                return "passou";
            }

        }
        return "passou";
    }

    public InformacoesDosprev coletarInformacoesDosprev (WebDriver driver, WebDriverWait wait) throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS).pageLoadTimeout(30, TimeUnit.SECONDS);
        Thread.sleep(1000);
        InformacoesDosprev informacao = new InformacoesDosprev();
        String dataInicioMaisRecente = "zero";
        String nbMaisRecente ="";
        driver.switchTo().frame(0);
        String dataAjuizamento = driver.findElement(By.xpath("/html/body/div/div[1]/table/tbody/tr[2]/td"))
                .getText();
        String sexo = driver.findElement(By.xpath("/html/body/div/div[1]/table/tbody/tr[11]/td"))
                .getText();
        boolean processoINSS = false;

        String textoNãoContemProcessosINSS = driver.findElement(By.xpath("/html/body/div/div[2]/table/tbody/tr[2]/td")).getText();
        if (!textoNãoContemProcessosINSS.contains("Não há relação dos processos movidos pelo autor contra o INSS.")){

                for (int i = 1;i <= 10; i++ ){
                    try {
                    String texto = driver.findElement(By.xpath("/html/body/div/div[2]/table/tbody/tr[2]/td[3]/table/tbody/tr["+i+"]/td")).getText();
                    if (texto.contains("INSTITUTO NACIONAL DO SEGURO SOCIAL")) {
                        processoINSS = true;
                        i = 11;
                    }
                }
                    catch (Exception e){
                        i = 11;
                    }
            }

        }


            for(int i = 2; i < 30; i++) {
                try {
                    String status = driver.findElement(By.xpath("/html/body/div/div[3]/table/tbody/tr[" + i + "]/td[6]")).getText();
                    driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS).pageLoadTimeout(1, TimeUnit.SECONDS);
                    if (status.contains("INDEFERIDO")) {
                        String nbIndeferido = driver.findElement(By.xpath("/html/body/div/div[3]/table/tbody/tr[" + i + "]/td[1]")).getText();
                        String dataDeInicio = driver.findElement(By.xpath("/html/body/div/div[3]/table/tbody/tr[" + i + "]/td[3]")).getText();
                        if (dataInicioMaisRecente.contains("zero")) {
                            dataInicioMaisRecente = dataDeInicio;
                            nbMaisRecente = nbIndeferido;
                        }
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        formatter = formatter.withLocale(Locale.US);
                        LocalDate dataAtual = LocalDate.parse(dataInicioMaisRecente, formatter);
                        LocalDate dataValidacao = LocalDate.parse(dataDeInicio, formatter);
                        long diferenca = ChronoUnit.DAYS.between(dataValidacao, dataAtual);
                        System.out.println(diferenca);
                        if (diferenca < 0) {
                            dataInicioMaisRecente = dataDeInicio;
                            nbMaisRecente = driver.findElement(By.xpath("/html/body/div/div[3]/table/tbody/tr[" + i + "]/td[1]")).getText();
                        }
                    }

                } catch (Exception e) {
                    i = 30;
                }
            }
        WebElement TabelaTref = driver.findElement(By.className("conteudo"));
        List<WebElement> listaEmpregos = new ArrayList<WebElement>(TabelaTref.findElements(By.cssSelector("tr")));

        informacao.setDataDeAjuizamento(dataAjuizamento);
        informacao.setSexo(sexo);
        informacao.setExisteProcessoINSS(processoINSS);
        informacao.setDataInicioIndeferido(dataInicioMaisRecente);
        informacao.setNbProcessoIndeferido(nbMaisRecente);

        return informacao;
    }
}

