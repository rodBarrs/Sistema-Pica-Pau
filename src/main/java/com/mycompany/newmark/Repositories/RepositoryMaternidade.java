package com.mycompany.newmark.Repositories;

import com.mycompany.newmark.LeituraPDF;
import com.mycompany.newmark.Processo_Etiquetar;
import com.mycompany.newmark.entities.*;

import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class RepositoryMaternidade {

    public InformacoesCapa procurarAdvogado(InformacoesCapa informacoesCapa, WebDriver driver, WebDriverWait wait) throws InterruptedException, IOException, UnsupportedFlavorException {
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS).pageLoadTimeout(1, TimeUnit.SECONDS);
        Thread.sleep(1000);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("iframe-myiframe")));
        WebElement capa = driver.findElement(By.id("iframe-myiframe"));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(capa));
        do {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(By.tagName("html")));
                wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
                driver.findElement(By.tagName("html")).click();
                break;
            } catch (Exception e) {
                //
            }
        } while (true);

        try {
            driver.findElement(By.xpath("/html/body/div/div[6]/span")).click();
        } catch (Exception e) {
            //
        }
        Actions action = new Actions(driver);
        action.keyDown(Keys.CONTROL).sendKeys(String.valueOf('\u0061')).perform();
        action.keyDown(Keys.CONTROL).sendKeys(String.valueOf('\u0063')).perform();
        driver.switchTo().defaultContent();
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        DataFlavor flavor = DataFlavor.stringFlavor;
        Thread.sleep(500);
        String processo = clipboard.getData(flavor).toString();

        if (processo.contains("ABEL BRITO DE QUEIROZ")){
            informacoesCapa.setNomeAdvogado("ABEL BRITO DE QUEIROZ");
            informacoesCapa.setTemAdvogadoPilantra(true);
        }else if(processo.contains("ADRIANO GOMES DE DEUS")){
            informacoesCapa.setNomeAdvogado("ADRIANO GOMES DE DEUS");
            informacoesCapa.setTemAdvogadoPilantra(true);
        }else if(processo.contains("ANDERSON JOSE LOPES FRANCO")){
            informacoesCapa.setNomeAdvogado("ANDERSON JOSE LOPES FRANCO");
            informacoesCapa.setTemAdvogadoPilantra(true);
        }else if(processo.contains("ARTHUR DE ALMEIDA E SOUSA")){
            informacoesCapa.setNomeAdvogado("ARTHUR DE ALMEIDA E SOUSA");
            informacoesCapa.setTemAdvogadoPilantra(true);
        }else if(processo.contains("EDER NILSON VIANA DA SILVA")){
            informacoesCapa.setNomeAdvogado("EDER NILSON VIANA DA SILVA");
            informacoesCapa.setTemAdvogadoPilantra(true);
        }else if (processo.contains("EVANDRO SOUZA MUNIZ")) {
            informacoesCapa.setNomeAdvogado("EVANDRO SOUZA MUNIZ");
            informacoesCapa.setTemAdvogadoPilantra(true);
        }else if (processo.contains("EUCLIDES RABELO ALENCAR")) {
            informacoesCapa.setNomeAdvogado("EUCLIDES RABELO ALENCAR");
            informacoesCapa.setTemAdvogadoPilantra(true);
        }else if (processo.contains("FRANKLIN DAYWYSON JAQUES DO MONT SERRAT ANDRADE")){
            informacoesCapa.setNomeAdvogado("FRANKLIN DAYWYSON JAQUES DO MONT SERRAT ANDRADE");
            informacoesCapa.setTemAdvogadoPilantra(true);
        }else if (processo.contains("GUILHERME HENRIQUE BRANCO DE OLIVEIRA")){
            informacoesCapa.setNomeAdvogado("GUILHERME HENRIQUE BRANCO DE OLIVEIRA");
            informacoesCapa.setTemAdvogadoPilantra(true);
        }else if(processo.contains("ITALO BENEDITO DA CRUZ MAGALHAES")) {
            informacoesCapa.setNomeAdvogado("ITALO BENEDITO DA CRUZ MAGALHAES");
            informacoesCapa.setTemAdvogadoPilantra(true);
        }else if(processo.contains("JOAO PAULO DE LIMA SILVA")) {
            informacoesCapa.setNomeAdvogado("JO??O PAULO DE LIMA SILVA");
            informacoesCapa.setTemAdvogadoPilantra(true);
        }else if (processo.contains("KELLY JAMILLY DE OLIVEIRA FERREIRA")){
            informacoesCapa.setNomeAdvogado("KELLY JAMILLY DE OLIVEIRA FERREIRA");
            informacoesCapa.setTemAdvogadoPilantra(true);
        }else if(processo.contains("MAYSA C??LIA DE SOUZA MAGALH??ES")){
            informacoesCapa.setNomeAdvogado("MAYSA C??LIA DE SOUZA MAGALH??ES");
            informacoesCapa.setTemAdvogadoPilantra(true);
        }else if(processo.contains("RAIMUNDO MAURICIO PINTO JUNIOR")){
            informacoesCapa.setNomeAdvogado("RAIMUNDO MAURICIO PINTO JUNIOR");
            informacoesCapa.setTemAdvogadoPilantra(true);
        }else if(processo.contains("RONALDO DIAS CAVALCANTE")){
            informacoesCapa.setNomeAdvogado("RONALDO DIAS CAVALCANTE");
            informacoesCapa.setTemAdvogadoPilantra(true);
        }else if(processo.contains("TARCISIO SAMPAIO DA SILVA")){
            informacoesCapa.setNomeAdvogado("TARCISIO SAMPAIO DA SILVA");
            informacoesCapa.setTemAdvogadoPilantra(true);
        }else if (processo.contains("WILLIAM VIANA DA SILVA")){
            informacoesCapa.setNomeAdvogado("WILLIAM VIANA DA SILVA");
            informacoesCapa.setTemAdvogadoPilantra(true);
        } else if (processo.contains("SABRINA DE PONTES ARAUJO")){
            informacoesCapa.setNomeAdvogado("SABRINA DE PONTES ARAUJO");
            informacoesCapa.setTemAdvogadoPilantra(true);
        }
        driver.switchTo().defaultContent();
        return informacoesCapa;
    }

    public boolean clicarDosprev(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS).pageLoadTimeout(1, TimeUnit.SECONDS);
        Thread.sleep(1000);
        List<String> janela = new ArrayList<String>(driver.getWindowHandles());
        String campoPassPath = "/html/body/div[3]/div[1]/div/div/table[1]/tbody/tr/td[2]/input";
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(campoPassPath)));
        //action.doubleClick(driver.findElement(By.xpath("//td["+i+"]/div"))).build().perform();

        WebElement campoPassElemt = driver.findElement(By.xpath(campoPassPath));
        campoPassElemt.click();

        campoPassElemt.sendKeys(" ");
        driver.switchTo().window(janela.get(1));



        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("treeview-1015")));
        WebElement TabelaTref = driver.findElement(By.id("treeview-1015"));
        List<WebElement> listaMovimentacao = new ArrayList<WebElement>(TabelaTref.findElements(By.cssSelector("tr")));
        for (int i = listaMovimentacao.size(); i > 2; i--) {

            // Provid??ncia Jur??dica ?? o t??tulo da movimenta????o
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[" + i + "]/td[2]/div/span")));
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[" + i + "]/td[2]/div/span")));
            if (i > listaMovimentacao.size() - 3) {
                Thread.sleep(500);
            }


            Boolean existeDosPrev = driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span")).getText()
                    .toUpperCase().contains("DOSSI?? PREVIDENCI??RIO");
            if (existeDosPrev) {
                WebElement dosClick = driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span"));
                dosClick.click();
                return true;
            }

        }
        return false;
    }


    public InformacoesDosprev coletarInformacoesDosprev(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS).pageLoadTimeout(1, TimeUnit.SECONDS);
        Thread.sleep(1000);
        InformacoesDosprev informacao = new InformacoesDosprev();
        String dataInicioMaisRecente = "zero";
        String nbMaisRecente = "";
        driver.switchTo().frame(0);
        String erro = driver.findElement(By.xpath("/html/body")).getText();
        if (erro.contains("Houve um problema na recupera????o do componente digital! Tente novamente mais tarde!")){
            return informacao;
        }
        String dataAjuizamento = driver.findElement(By.xpath("/html/body/div/div[1]/table/tbody/tr[2]/td"))
                .getText();
        String sexo = driver.findElement(By.xpath("/html/body/div/div[1]/table/tbody/tr[11]/td"))
                .getText();






        boolean processoINSS = false;

        String textoN??oContemProcessosINSS = driver.findElement(By.xpath("/html/body/div/div[2]/table/tbody/tr[2]/td")).getText();
        if (!textoN??oContemProcessosINSS.contains("N??o h?? rela????o dos processos movidos pelo autor contra o INSS.")) {

            for (int i = 1; i <= 10; i++) {
                try {
                    String texto = driver.findElement(By.xpath("/html/body/div/div[2]/table/tbody/tr[2]/td[3]/table/tbody/tr[" + i + "]/td")).getText();
                    if (texto.contains("INSTITUTO NACIONAL DO SEGURO SOCIAL") || texto.contains("AGENCIA DA PREVIDENCIA") || texto.contains("INSS")) {
                        processoINSS = true;
                        i = 11;
                    }
                } catch (Exception e) {
                    i = 11;
                }
            }

        }
        for (int i = 3; i < 7; i++) {
            try {
                String titulo = driver.findElement(By.xpath("/html/body/div/p[" + (i + 1) + "]/b/u")).getText();
                String conteudo = driver.findElement(By.xpath("/html/body/div/div[3]/table/tbody/tr[2]/td")).getText();
                if (titulo.equals("RESUMO INICIAL ??? DADOS GERAIS DOS REQUERIMENTOS")) {
                    if(conteudo.contains("N??o foram encontrados requerimentos em nome do autor.")){
                        dataInicioMaisRecente = "";
                        nbMaisRecente = "";
                        i = 8;
                    }else {
                        try {
                            WebElement TabelaTref = driver.findElement(By.xpath("/html/body/div/div[" + i + "]"));
                            List<WebElement> listaRelPrev = new ArrayList<WebElement>(TabelaTref.findElements(By.cssSelector("tr")));
                            for (int j = 2; j <= listaRelPrev.size(); j++) {
                                String status = driver.findElement(By.xpath("/html/body/div/div[" + i + "]/table/tbody/tr[" + j + "]/td[6]")).getText();
                                if (status.contains("INDEFERIDO")) {
                                    String nbIndeferido = driver.findElement(By.xpath("/html/body/div/div[" + i + "]/table/tbody/tr[" + j + "]/td[1]")).getText();
                                    String dataDeInicio = driver.findElement(By.xpath("/html/body/div/div[" + i + "]/table/tbody/tr[" + j + "]/td[3]")).getText();
                                    if (dataDeInicio.equals("")){

                                    } else {
                                        if (dataInicioMaisRecente.contains("zero")) {
                                            dataInicioMaisRecente = dataDeInicio;
                                            nbMaisRecente = nbIndeferido;
                                        }
                                        if(!dataInicioMaisRecente.equals("")){
                                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                            formatter = formatter.withLocale(Locale.US);
                                            LocalDate dataAtual = LocalDate.parse(dataInicioMaisRecente, formatter);
                                            LocalDate dataValidacao = LocalDate.parse(dataDeInicio, formatter);
                                            long diferenca = ChronoUnit.DAYS.between(dataValidacao, dataAtual);
                                            System.out.println(diferenca);
                                            if (diferenca < 0) {
                                                dataInicioMaisRecente = dataDeInicio;
                                                nbMaisRecente = driver.findElement(By.xpath("/html/body/div/div[" + i + "]/table/tbody/tr[" + j + "]/td[1]")).getText();
                                            }
                                        }
                                    }



                                }

                            }
                            i = 7;
                        } catch (Exception e) {

                            System.out.println(e);
                        }
                    }

                }
            }catch (Exception e){
                System.out.println(e);
            }



        }

        String filiacao = "";
        String origemVinculo = "";
        boolean temContribuinteIndividual = false;
        String nit = "";
//        String dataInicio = "";
//        String dataFim = "";
        String codNB = "";
        try{
            for (int i = 4; i < 7; i++) {
                String titulo = driver.findElement(By.xpath("/html/body/div/p[" + (i + 1) + "]/b/u")).getText();
                if (titulo.equals("RELA????ES PREVIDENCI??RIAS")) {
                    try {
                        WebElement TabelaTref = driver.findElement(By.xpath("/html/body/div/div[" + i + "]"));
                        List<WebElement> listaRelPrev = new ArrayList<WebElement>(TabelaTref.findElements(By.cssSelector("tr")));
                        if (listaRelPrev.size() <= 1){
                            i = 8;
                        }else {
                            for (int j = listaRelPrev.size(); j >= 2; j--) {

                                origemVinculo = driver.findElement(By.xpath("/html/body/div/div[" + i + "]/table/tbody/tr[" + j + "]/td[7]")).getText();
                                if (origemVinculo.contains("Contribuinte Individual")) {
                                    temContribuinteIndividual = true;
                                    i = 8;
                                    break;
                                }

                                if (j == 2) {
                                    i = 8;
                                }

                            }
                        }

                    } catch (Exception e) {
                        System.out.println(e);
                    }

                }

            }
        } catch (Exception e) {
            System.out.println(e);
        }

        List<InformacoesUrbano> infUrbano = new ArrayList<>();

            for (int z = 6; z < 10; z++) {
                try{
                    String cabecalho = driver.findElement(By.xpath("/html/body/div/div[" + z + "]/p/b/u")).getText();
                    if (cabecalho.contains("COMPET??NCIAS DETALHADAS")) {
                        for (int i = 1; i < 50 ; i++) {
                            String titulo = driver.findElement(By.xpath(
                                    "/html/body/div/div[" + z + "]/div[" + i + "]/p[1]/b")).getText();
                            if (titulo.contains("V??nculo Previdenci??rio")) {
                                String colunaCod = driver.findElement(By.xpath(
                                        "/html/body/div/div[" + z + "]/div[" + i + "]/table[1]/tbody/tr[1]/th[2]")).getText();
                                if (colunaCod.contains("C??digo Emp.")) {
                                    try{
                                        String vinculo = driver.findElement(By.xpath(
                                                "/html/body/div/div[" + z + "]/div[" + i + "]/table[1]/tbody/tr[2]/td[3]")).getText();

                                        String dataInicio = driver.findElement(By.xpath(
                                                "/html/body/div/div[" + z + "]/div[" + i + "]/table[1]/tbody/tr[2]/td[4]")).getText();

                                        String dataFim = driver.findElement(By.xpath(
                                                "/html/body/div/div[" + z + "]/div[" + i + "]/table[1]/tbody/tr[2]/td[5]")).getText();
                                        infUrbano.add(new InformacoesUrbano(vinculo, dataInicio, dataFim));
                                        z=11;
                                        break;
                                    } catch (Exception e){
                                        System.out.println("Entrei no Else");
                                        z=11;
                                    }
                                }
                            } else {

                            }
                        }
                    }else if (z == 6){
                        z = 4;
                    } else if (z == 5){
                        z = 6;
                    }
                }catch (Exception e){
                    if (z==6){
                        z = 4;
                    } else if ( z == 5){
                        z = 6;
                    }
                }


            }
        boolean temSegurado = false;
        for (int z = 6; z < 10; z++) {
            try {
                String cabecalho = driver.findElement(By.xpath("/html/body/div/div[" + z + "]/p/b/u")).getText();
                if (cabecalho.contains("COMPET??NCIAS DETALHADAS")) {
                    for (int i = 1; i < 50 ; i++) {
                        String titulo = driver.findElement(By.xpath(
                                "/html/body/div/div[" + z + "]/div[" + i + "]/p[1]/b")).getText();
                        if (titulo.contains("Dados do Benef??cio")) {
                            String colunaSegurado = driver.findElement(By.xpath(
                                    "/html/body/div/div["+z+"]/div["+i+"]/table[3]/tbody/tr[2]/td[1]")).getText();
                            if (colunaSegurado.contains("SEGURADO_ESPECIAL")) {
                                try{
                                    temSegurado = true;
                                    z = 11;
                                    break;
                                } catch (Exception e){
                                    System.out.println("Entrei no Else");
                                    z=11;
                                }
                            }
                        } else {

                        }
                    }


                } else if (z == 6) {
                    z = 4;
                } else if (z == 5) {
                    z = 6;
                }
            } catch (Exception e) {
                if (z==6){
                    z = 4;
                } else if ( z == 5){
                    z = 6;
                } else {
                    z++;
                }

            }


        }




        informacao.setTemSegurado(temSegurado);
        informacao.setInformacoesUrbanos(infUrbano);
        informacao.setDataDeAjuizamento(dataAjuizamento);
        informacao.setSexo(sexo);
        informacao.setExisteProcessoINSS(processoINSS);
        informacao.setDataInicioIndeferido(dataInicioMaisRecente);
        informacao.setNbProcessoIndeferido(nbMaisRecente);
        informacao.setNit(nit);
        informacao.setNomeEmpresa(origemVinculo);
        informacao.setTemContribuinteIndividual(temContribuinteIndividual);
//        informacao.setDataInicio(dataInicio);
//        informacao.setDataFim(dataFim);

        return informacao;
    }


    public boolean clicarSislabra(WebDriver driver, WebDriverWait wait) throws InterruptedException {

        driver.switchTo().defaultContent();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS).pageLoadTimeout(1, TimeUnit.SECONDS);
        Thread.sleep(1000);
        List<String> janela = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(janela.get(1));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("treeview-1015")));
        WebElement TabelaTref = driver.findElement(By.id("treeview-1015"));
        List<WebElement> listaMovimentacao = new ArrayList<WebElement>(TabelaTref.findElements(By.cssSelector("tr")));
        for (int i = listaMovimentacao.size(); i > 2; i--) {

            // Provid??ncia Jur??dica ?? o t??tulo da movimenta????o
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[" + i + "]/td[2]/div/span")));
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[" + i + "]/td[2]/div/span")));
            if (i > listaMovimentacao.size() - 3) {
                Thread.sleep(500);
            }
            Boolean existeSislabra = false;
            for(int j=3; j>0;){
                try{
                            existeSislabra = driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span")).getText()
                            .toUpperCase().contains("BENS") || driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span/span["+j+"]")).getText()
                            .toUpperCase().contains("SISLABRA");
                            j=0;
                }catch (Exception e){
                    j--;
                }
            }


            if (existeSislabra) {
                WebElement dosClick = driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span"));
                dosClick.click();
                LeituraPDF pdf = new LeituraPDF();
                pdf.apagarPDF();
                return true;
            }

        }
        return false;
    }

    public InformacoesSislabra coletarInformacoesSislabra(WebDriver driver, WebDriverWait wait) throws InterruptedException, IOException {
        LeituraPDF pdf = new LeituraPDF();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS).pageLoadTimeout(1, TimeUnit.SECONDS);
        InformacoesSislabra informacao = new InformacoesSislabra();
        driver.switchTo().frame(0);
        String processo;


        int cont = 0;
       do {
            if (pdf.PDFBaixado()) {
                processo = pdf.lerPDF();
                System.out.println(processo);
                String[] buscaNb = processo.split("\r\n");

                boolean temConjuge = false;
                String codIncra;
                String situacaoEmpAtual;
                String modelo;
                String tipo;
                List<String> situacaoEmpresa = new ArrayList<>();
                List<InformacoesSislabra.InfVeiculo> infVeiculo = new ArrayList<>();
                List<InfImovel> infImovel = new ArrayList<>();
                for (int indexBuscaNb = 0; indexBuscaNb < buscaNb.length; indexBuscaNb++) {

                    if (buscaNb[indexBuscaNb].contains("C??NJUGE") || buscaNb[indexBuscaNb].contains("C??njuge")) {
                       temConjuge = true;
                    }

                    if (buscaNb[indexBuscaNb].contains("Situa????o Empresa:")) {
                        System.out.println(buscaNb[indexBuscaNb] + " : " + indexBuscaNb);
                        situacaoEmpAtual = buscaNb[indexBuscaNb+1];
                        situacaoEmpresa.add(situacaoEmpAtual);

                    }
                    if (buscaNb[indexBuscaNb].contains("MOTOCICLETA") || buscaNb[indexBuscaNb].contains("AUTOMOVEL") || buscaNb[indexBuscaNb].contains("CAMINHONETE") || buscaNb[indexBuscaNb].contains("CAMINH??O")) {
                        System.out.println(buscaNb[indexBuscaNb] + " : " + indexBuscaNb);
                        tipo = buscaNb[indexBuscaNb];
                        modelo = buscaNb[indexBuscaNb+2];
                        infVeiculo.add(new InformacoesSislabra.InfVeiculo(modelo,tipo));
                    }

                    if (buscaNb[indexBuscaNb].contains("C??d. Im??vel INCRA")){
                        System.out.println(buscaNb[indexBuscaNb] + " : " + indexBuscaNb);
                        codIncra = buscaNb[indexBuscaNb];
                        infImovel.add(new InfImovel(codIncra));


                    }
                }
                informacao.setTemConjuge(temConjuge);
                informacao.setInformacoesImoveis(infImovel);
                informacao.setInfVeiculo(infVeiculo);
                informacao.setSituacaoEmpresa(situacaoEmpresa);

                break;
            }
            cont++;
        } while (!pdf.PDFBaixado());

        pdf.apagarPDF();

        return informacao;
    }

    public String clicarProcesoAdministrativo(WebDriver driver, WebDriverWait wait, String nbProcessoIndeferido) throws InterruptedException, IOException {

        driver.switchTo().defaultContent();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS).pageLoadTimeout(1, TimeUnit.SECONDS);
        Thread.sleep(1000);
        List<String> janela = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(janela.get(1));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("treeview-1015")));
        WebElement TabelaTref = driver.findElement(By.id("treeview-1015"));
        List<WebElement> listaMovimentacao = new ArrayList<WebElement>(TabelaTref.findElements(By.cssSelector("tr")));
        for (int i = listaMovimentacao.size(); i > 2; i--) {

            // Provid??ncia Jur??dica ?? o t??tulo da movimenta????o
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[" + i + "]/td[2]/div/span")));
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[" + i + "]/td[2]/div/span")));
            if (i > listaMovimentacao.size() - 3) {
                Thread.sleep(500);
            }


            Boolean existeProcessoAdministrativo = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div[2]/div/div[3]/div/table/tbody/tr["+ i +"]/td[2]/div/span/span[1]")).getText()
                    .toUpperCase().contains("PROCESSO ADMINISTRATIVO");
            if (existeProcessoAdministrativo) {
                WebElement dosClick = driver.findElement(By.xpath("//tr[" + i + "]/td[2]/div/span"));
                dosClick.click();

                LeituraPDF pdf = new LeituraPDF();

                driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS).pageLoadTimeout(1, TimeUnit.SECONDS);

                String processo;


                int cont = 0;
                while (cont <= 2) {

                    if (pdf.PDFBaixado()) {
                        processo = pdf.lerPDF();
                        System.out.println(processo);
                        String[] buscaNb = processo.split("\r\n");
                        for (int indexBuscaNb = 0; indexBuscaNb < buscaNb.length; indexBuscaNb++) {
                            if (buscaNb[indexBuscaNb].contains("NB")) {
                                System.out.println(buscaNb[indexBuscaNb] + " : " + indexBuscaNb);
                                String replaceNb = buscaNb[indexBuscaNb].replace(".", "");
                                replaceNb = replaceNb.replace("-", "");
                                if (replaceNb.contains(nbProcessoIndeferido)) {
                                    for(int j = indexBuscaNb; j>0; j--){
                                        if(buscaNb[j].contains("Informe a data do parto, atestado m??dico ou ado????o/guarda para fins de ado????o:")){
                                            String dataParto = buscaNb[j].replace("Informe a data do parto, atestado m??dico ou ado????o/guarda para fins de ado????o: ","");
                                            return dataParto;
                                        }
                                    }
                                }

                            }
                        }
                        break;
                    } else {
                        pdf.apagarPDF();
                    }
                    cont++;
                }

                pdf.apagarPDF();


            }


        }

        return  "nao achou";
    }

    public EtiquetaObservacao etiquetarMaternidade (WebDriver driver, WebDriverWait wait, InformacoesDosprev infoDosprev, InformacoesSislabra infoSislabra, String assunto, boolean passouSislabra, boolean passouDosprev, InformacoesCapa informacoesCapa){

        EtiquetaObservacao etiquetaObservacao = new EtiquetaObservacao();
        Processo_Etiquetar etiquetar = new Processo_Etiquetar();
        String etiqueta = "IMPEDITIVO: ";
        String observacao = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        formatter = formatter.withLocale(Locale.US);

//        if (!passouDosprev && !passouSislabra){
//            etiqueta = "NADA ENCONTRADO - DOSSI??; NADA ENCONTRADO - SISLABRA;";
//        } else if (!passouDosprev){
//            etiqueta = "NADA ENCONTRADO - DOSSI??; IMPEDITIVO: ";
//        } else if (!passouSislabra){
//            etiqueta = "NADA ENCONTRADO - SISLABRA; IMPEDITIVO: ";
//        }

       //PRESCRI????O
//        if (passouDosprev){
//            if(infoDosprev.getDataInicioIndeferido().equals("") || (infoDosprev.getDataDeAjuizamento().equals(""))){
//                etiqueta = "PRESCRI????O - N; ";
//            }else{
//               if(infoDosprev.getDataDeAjuizamento().equals("") || infoDosprev.getDataInicioIndeferido().equals("")){
//                   etiqueta = "PRESCRI????O - DADOS INSUFICIENTES ";
//               }else{
//                   LocalDate d1 = LocalDate.parse(infoDosprev.getDataDeAjuizamento(), formatter);
//                   LocalDate d2 = LocalDate.parse(infoDosprev.getDataInicioIndeferido(), formatter);
//                   Period period = Period.between(d1,d2);
//                   int difAnos = Math.abs(period.getYears());
//                   if(difAnos>=5){
//                       etiqueta = "PRESCRI????O - S; ";
//                       observacao +="|PRESC-data Ajuizamento: "+infoDosprev.getDataDeAjuizamento()+"Data Indeferido: "+ infoDosprev.getDataInicioIndeferido() + "| ";
//                   }
//               }
//
//            }
//        }else {
//            etiqueta+="PRESCRI????O - DOSSI?? PREVIDENCI??RIO N??O ENCONTRADO; ";
//        }




        //LITISPEND??NCIA
        if (passouDosprev) {
            if (infoDosprev.isExisteProcessoINSS()) {
                etiqueta += "LITISPEND??NCIA; ";
            }
        }


        //PATRIM??NIO
        if (passouSislabra) {
            int contadorVeiculos = 0;
            int contadorMotocilceta = 0;
            int contadorEmpresas = 0;
            int indexVeiculo = 0;
            int contadorImoveis = 0;


            if (infoSislabra.getInfVeiculo().size() > 0) {
                observacao += "Ve??culos: ";
                etiqueta += "VE??CULO; ";
            }
            for (InformacoesSislabra.InfVeiculo teste : infoSislabra.getInfVeiculo()) {
                if (infoSislabra.getInfVeiculo().get(indexVeiculo).getTipo().contains("MOTOCICLETA")) {
                    contadorMotocilceta++;
                } else {
                    contadorVeiculos++;
                }

                observacao += infoSislabra.getInfVeiculo().get(indexVeiculo).getTipo() + ", ";
                observacao += infoSislabra.getInfVeiculo().get(indexVeiculo).getModelo() + "; ";
                indexVeiculo++;

            }
            if (infoSislabra.getSituacaoEmpresa().size() > 0) {
                observacao += "Empresa situa????o: ";
                etiqueta += "EMPRESA; ";
            }
            for (int z = 0; z < infoSislabra.getSituacaoEmpresa().size(); z++) {

                contadorEmpresas++;
                observacao += infoSislabra.getSituacaoEmpresa() + ";";

            }


            if (infoSislabra.getInformacoesImoveis().size() > 0) {
                observacao += "Im??veis: ";
                etiqueta += "IM??VEL; ";
            }
            for (int z = 0; z < infoSislabra.getInformacoesImoveis().size(); z++) {

                contadorImoveis++;
                observacao += infoSislabra.getInformacoesImoveis().get(z).getCodIncra() + "-";

            }


        }


        //URBANO


//        infoDosprev.getDataFim()

        if (passouDosprev){
        if (infoDosprev.getDataDeAjuizamento() == null){
            etiqueta += "DOSSI?? PREVIDENCI??RIO INCOMPLETO";
        }
        else if(infoDosprev.getInformacoesUrbanos().size() > 0 || infoDosprev.isTemContribuinteIndividual()){
            etiqueta += "EMPREGO; ";
            observacao+= "Vinculos Urbano: ";
        }


        }


//        if(assunto.contains("SAL??RIO-MATERNIDADE")){
//            etiqueta += "";
////            if ((!passouProcessoAdministrativo || !passouDosprev)){
////                if((!passouDosprev && !passouProcessoAdministrativo)){
////                    etiqueta += "URBANO - PROCESSO ADMINISTRATIVO E DOSS??E PREVIDENCI??RIO N??O ENCONTRADOS ";
////                } else if (passouDosprev == false){
////                    etiqueta += "URBANO - DOSS??E PREVIDENCI??RIO N??O ENCONTRADO";
////                } else if (passouProcessoAdministrativo == false){
////                    etiqueta += "URBANO - PROCESSO ADMINISTRATIVO N??O ENCONTRADO";
////                }
////
////            }else {
////                if(!infoDosprev.getDataFim().equals("")){
////                    LocalDate data1 = LocalDate.parse(infoDosprev.getDataFim(), formatter);
////                    LocalDate data2 = LocalDate.parse(dataNascimentoCrianca, formatter);
////                    Period period2 = Period.between(data1,data2);
////                    int difMeses = Math.abs(period2.getMonths());
////
////                    if (difMeses < 10){
////                        etiqueta += "URBANO - S ";
////                        observacao += "|URBA - Data nascimento: " + dataNascimentoCrianca + "Data Fim: " + infoDosprev.getDataFim();
////                    }else {
////                        etiqueta += "URBANO - N ";
////                    }
////                }
////                else {
////                    etiqueta += "URBANO - SEM DATA FIM";
////                }
////
////            }
////        } else {
//            if (passouDosprev){
//                if (infoDosprev.getDataFim().equals("")) {
//                    etiqueta += "URBANO - N ";
//                }else {
//                    LocalDate data1 = LocalDate.parse(infoDosprev.getDataFim(), formatter);
//                    LocalDate data2 = LocalDate.parse(infoDosprev.getDataDeAjuizamento(), formatter);
//                    Period period2 = Period.between(data1,data2);
//                    int difAnosUrbano = Math.abs(period2.getYears());
//
//                    if (difAnosUrbano < 15){
//                        etiqueta += "URBANO - S ";
//                        observacao += "Data de Ajuizamento: " + infoDosprev.getDataDeAjuizamento() + "Data Fim: " + infoDosprev.getDataFim();
//                    }else {
//                        etiqueta += "URBANO - N ";
//                    }
//                }
//            } else {
//                etiqueta += "URBANO - DOSS??E PREVIDENCI??RIO N??O ENCONTRADO";
//            }
//            }

        if (!passouDosprev && !passouSislabra){
            etiqueta = "NADA ENCONTRADO - DOSSI??; NADA ENCONTRADO - SISLABRA;";
        } else if (!passouDosprev){
            etiqueta += "NADA ENCONTRADO - DOSSI??;";
        } else if (!passouSislabra){
            etiqueta += "NADA ENCONTRADO - SISLABRA;";
        }


        if (etiqueta.equals("IMPEDITIVO: ")){
            etiqueta = "PROCESSO LIMPO";
        }

        if (etiqueta.contains("NADA ENCONTRADO - DOSSI??; NADA ENCONTRADO - SISLABRA;")){
            etiqueta = "PROCESSO LIMPO; NADA ENCONTRADO - DOSSI??; NADA ENCONTRADO - SISLABRA;";
        }

        if (etiqueta.equals("IMPEDITIVO: NADA ENCONTRADO - DOSSI??;")){
            etiqueta = "PROCESSO LIMPO; NADA ENCONTRADO - DOSSI??;";
        }

        if (etiqueta.equals("IMPEDITIVO: NADA ENCONTRADO - SISLABRA;")){
            etiqueta = "PROCESSO LIMPO; NADA ENCONTRADO - SISLABRA; ";
        }

        if (infoSislabra.isTemConjuge()){
            etiqueta += "- C??NJUGE";
        }

        if (infoDosprev.isTemSegurado()){
            etiqueta += "- CONCESS??O ANTERIOR";
        }

        if (informacoesCapa.isTemAdvogadoPilantra()){
            etiqueta += "- FRAUDE PAR??";
        }

        etiquetaObservacao.setEtiqueta(etiqueta);
        etiquetaObservacao.setObservacao(observacao);


        return etiquetaObservacao;

    }

    public void etiquetar(WebDriver driver, WebDriverWait wait, String etiqueta) throws InterruptedException {
        List<String> janela = new ArrayList<String>(driver.getWindowHandles());
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS).pageLoadTimeout(1, TimeUnit.SECONDS);
        driver.switchTo().window(janela.get(1));

        String campoPassPath = "/html/body/div[3]/div[1]/div/div/table[1]/tbody/tr/td[2]/input";
        //action.doubleClick(driver.findElement(By.xpath("//td["+i+"]/div"))).build().perform();

        WebElement campoPassElemt = driver.findElement(By.xpath(campoPassPath));
        campoPassElemt.click();

        campoPassElemt.sendKeys(etiqueta);

        WebElement salvarEtiqueta = driver
                .findElement(By.xpath("/html/body/div[3]/div[1]/div/div/a/span/span/span[2]"));
        wait.until(ExpectedConditions.elementToBeClickable(salvarEtiqueta));
        salvarEtiqueta.click();
        Thread.sleep(200);

        driver.switchTo().window(janela.get(1)).close();


        driver.switchTo().window(janela.get(0));
        wait.until(ExpectedConditions.presenceOfElementLocated(By
                .xpath("/html/body/div[4]/div[1]/div[2]/div/div[2]/div/div[2]/div/div/a[5]/span/span/span[2]")));
        WebElement filtroSpace = driver.findElement(
                By.xpath("/html/body/div[4]/div[1]/div[2]/div/div[2]/div/div[2]/div/div/a[5]/span/span/span[2]"));
        filtroSpace.click();


    }
}

