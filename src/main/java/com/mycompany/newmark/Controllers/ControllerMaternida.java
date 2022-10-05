/**
 * @author Felipe Marques, Gabriel Ramos, Rafael Henrique e Adriano Vilhena
 */
package com.mycompany.newmark.Controllers;

import com.mycompany.newmark.*;
import com.mycompany.newmark.Repositories.RepositoryMaternidade;
import com.mycompany.newmark.entities.EtiquetaObservacao;
import com.mycompany.newmark.entities.InformacoesDosprev;
import com.mycompany.newmark.entities.InformacoesSislabra;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static com.mycompany.newmark.Controller_Login.som;

public class ControllerMaternida {


	public Chaves_Resultado iniciar(WebDriver driver, String assunto, WebDriverWait wait, Chaves_Configuracao config, String bancos) throws InterruptedException, IOException {



		InformacoesDosprev informacaoDosPrev = new InformacoesDosprev();
		InformacoesSislabra informacaoSislabra = new InformacoesSislabra();
		RepositoryMaternidade materidadeRepositorio = new RepositoryMaternidade();

		Chaves_Resultado resultado = new Chaves_Resultado();
		LeituraPDF pdf = new LeituraPDF();

			String dataNascimentoCrianca;

		boolean passouDosprev = materidadeRepositorio.clicarDosprev(driver, wait);
		if (passouDosprev) {
			informacaoDosPrev = materidadeRepositorio.coletarInformacoesDosprev(driver, wait);
		}


		boolean passouSislabra = materidadeRepositorio.clicarSislabra(driver, wait);
			if (passouSislabra){
				informacaoSislabra = materidadeRepositorio.coletarInformacoesSislabra(driver, wait);
			}




			//dataNascimentoCrianca = materidadeRepositorio.clicarProcesoAdministrativo(driver, wait, informacaoDosPrev.getNbProcessoIndeferido());

			EtiquetaObservacao etiquetaObservacao = materidadeRepositorio.etiquetarMaternidade(driver, wait, informacaoDosPrev, informacaoSislabra, assunto, passouSislabra, passouDosprev);

			resultado.setDriver(driver);
			resultado.setEtiqueta(etiquetaObservacao.getEtiqueta());
			resultado.setObservacao(etiquetaObservacao.getObservacao());
			materidadeRepositorio.etiquetar(driver, wait, etiquetaObservacao.getEtiqueta());


		return resultado;
	}

}




