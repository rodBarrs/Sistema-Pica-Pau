package com.mycompany.newmark.Repositories;

import com.mycompany.newmark.LeituraPDF;
import com.mycompany.newmark.entities.InformacoesDosprev;

import com.mycompany.newmark.entities.InformacoesSislabra;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
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
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS).pageLoadTimeout(1, TimeUnit.SECONDS);
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
            if (existeDosPrev) {
                WebElement dosClick = driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span"));
                dosClick.click();
                return "passou";
            }

        }
        return "passou";
    }


    public InformacoesDosprev coletarInformacoesDosprev (WebDriver driver, WebDriverWait wait) throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS).pageLoadTimeout(1, TimeUnit.SECONDS);
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
            for(int i = 3; i<7; i++){
                String titulo = driver.findElement(By.xpath("/html/body/div/p["+(i+1)+"]/b/u")).getText();
                if (titulo.equals("RESUMO INICIAL – DADOS GERAIS DOS REQUERIMENTOS")) {
                    try{
                        WebElement TabelaTref = driver.findElement(By.xpath("/html/body/div/div["+i+"]"));
                        List<WebElement> listaRelPrev = new ArrayList<WebElement>(TabelaTref.findElements(By.cssSelector("tr")));
                        for(int j = 2; j <= listaRelPrev.size(); j++) {
                            String status = driver.findElement(By.xpath("/html/body/div/div["+i+"]/table/tbody/tr[" + j + "]/td[6]")).getText();
                            if (status.contains("INDEFERIDO")) {
                                String nbIndeferido = driver.findElement(By.xpath("/html/body/div/div["+i+"]/table/tbody/tr[" + j + "]/td[1]")).getText();
                                String dataDeInicio = driver.findElement(By.xpath("/html/body/div/div["+i+"]/table/tbody/tr[" + j + "]/td[3]")).getText();
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
                                    nbMaisRecente = driver.findElement(By.xpath("/html/body/div/div["+i+"]/table/tbody/tr[" + j + "]/td[1]")).getText();
                                }

                            }

                        }
                        i = 7;
                    }catch(Exception e){

                        System.out.println(e);
                    }
                }

            }

        String filiacao = "";
        String nomeEmpresa = "";
        String nit = "";
        String dataInicio = "";
        String dataFim = "";
        for (int i = 4; i<7; i++) {
            try{
                WebElement TabelaTref = driver.findElement(By.xpath("/html/body/div/div["+i+"]"));
                List<WebElement> listaRelPrev = new ArrayList<WebElement>(TabelaTref.findElements(By.cssSelector("tr")));
                for (int j = listaRelPrev.size(); j > 0; j--) {
                    filiacao = driver.findElement(By.xpath("/html/body/div/div["+i+"]/table/tbody/tr[" + j + "]/td[7]")).getText();
                    if (filiacao.contains("Empregado")) {

                        nit = driver.findElement(By.xpath("/html/body/div/div["+i+"]/table/tbody/tr[" + j + "]/td[2]")).getText();
                        nomeEmpresa = driver.findElement(By.xpath("/html/body/div/div["+i+"]/table/tbody/tr[" + j + "]/td[4]")).getText();
                        dataInicio = driver.findElement(By.xpath("/html/body/div/div["+i+"]/table/tbody/tr[" + j + "]/td[5]")).getText();
                        dataFim = driver.findElement(By.xpath("/html/body/div/div["+i+"]/table/tbody/tr[" + j + "]/td[6]")).getText();
                        j = -1;
                        i = 7;
                    }
                }
            }catch(Exception e){
               System.out.println(e);
            }

        }




        informacao.setDataDeAjuizamento(dataAjuizamento);
        informacao.setSexo(sexo);
        informacao.setExisteProcessoINSS(processoINSS);
        informacao.setDataInicioIndeferido(dataInicioMaisRecente);
        informacao.setNbProcessoIndeferido(nbMaisRecente);
        informacao.setNit(nit);
        informacao.setNomeEmpresa(nomeEmpresa);
        informacao.setDataInicio(dataInicio);
        informacao.setDataFim(dataFim);

        return informacao;
    }


    public String clicarSislabra(WebDriver driver, WebDriverWait wait) throws InterruptedException {

        driver.switchTo().defaultContent();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS).pageLoadTimeout(1, TimeUnit.SECONDS);
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


//            Boolean existeSislabra = driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span")).getText()
//                    .toUpperCase().contains("BENS") || driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span/span[3]")).getText()
//                    .toUpperCase().contains("SISLABRA") ;

            Boolean existeSislabra = driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span/span[3]")).getText()
                    .toUpperCase().contains("PROCESSO ADMINISTRATIVO") ;

            if (existeSislabra) {
                WebElement dosClick = driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span"));
                dosClick.click();
                return "passou";
            }

        }
        return "passou";
    }

    public InformacoesSislabra coletarInformacoesSislabra (WebDriver driver, WebDriverWait wait) throws InterruptedException, IOException {
        LeituraPDF pdf = new LeituraPDF();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS).pageLoadTimeout(1, TimeUnit.SECONDS);
        InformacoesSislabra informacao = new InformacoesSislabra();
        driver.switchTo().frame(0);
        String processo;


        int cont = 0;
        while (cont <= 2) {

            if (pdf.PDFBaixado()) {
                processo = pdf.lerPDF();
                System.out.println(processo);
                String[] buscaNb = processo.split("/r/n");
                for(int indexBuscaNb =0; indexBuscaNb<buscaNb.length; indexBuscaNb++){
                    if(buscaNb[indexBuscaNb].equals("196.960.955-6")){
                        System.out.println(buscaNb[indexBuscaNb] + " : "+ indexBuscaNb);
                        break;
                    }
                }
                break;
            } else {
                pdf.apagarPDF();
            }
            cont++;
        }

        pdf.apagarPDF();
        return informacao;
    }

    public String clicarProcesoAdministrativo(WebDriver driver, WebDriverWait wait) throws InterruptedException {

        driver.switchTo().defaultContent();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS).pageLoadTimeout(1, TimeUnit.SECONDS);
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


            Boolean existeSislabra = driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span")).getText()
                    .toUpperCase().contains("BENS") || driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span/span[3]")).getText()
                    .toUpperCase().contains("SISLABRA") ;
            if (existeSislabra) {
                WebElement dosClick = driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span"));
                dosClick.click();
                return "passou";
            }

        }
        return "passou";
    }

}

