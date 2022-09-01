/**
 * @author Felipe Marques, Gabriel Ramos, Rafael Henrique e Adriano Vilhena
 */
package com.mycompany.newmark.Controllers;

import com.mycompany.newmark.*;
import com.mycompany.newmark.Repositories.RepositoryMaternidade;
import com.mycompany.newmark.entities.EtiquetaObservacao;
import com.mycompany.newmark.entities.InformacoesDosprev;
import com.mycompany.newmark.entities.InformacoesSislabra;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;

public class ControllerMaternida {


	public Chaves_Resultado iniciar(WebDriver driver, String assunto, WebDriverWait wait, Chaves_Configuracao config, String bancos) throws InterruptedException, IOException {
		InformacoesDosprev informacaoDosPrev = new InformacoesDosprev();
		InformacoesSislabra informacaoSislabra = new InformacoesSislabra();
		RepositoryMaternidade materidadeRepositorio = new RepositoryMaternidade();

		Chaves_Resultado resultado = new Chaves_Resultado();
		LeituraPDF pdf = new LeituraPDF();

			String dataNascimentoCrianca;

			String passou = materidadeRepositorio.clicarDosprev(driver, wait);
			informacaoDosPrev = materidadeRepositorio.coletarInformacoesDosprev(driver, wait);

			String passouSislabra = materidadeRepositorio.clicarSislabra(driver, wait);
			informacaoSislabra = materidadeRepositorio.coletarInformacoesSislabra(driver, wait);

			dataNascimentoCrianca = materidadeRepositorio.clicarProcesoAdministrativo(driver, wait, informacaoDosPrev.getNbProcessoIndeferido());

			EtiquetaObservacao etiquetaObservacao = materidadeRepositorio.etiquetarMaternidade(driver, wait, informacaoDosPrev, informacaoSislabra, dataNascimentoCrianca, assunto);

			resultado.setDriver(driver);
			resultado.setEtiqueta(etiquetaObservacao.getEtiqueta());
			resultado.setObservacao(etiquetaObservacao.getObservacao());


		return resultado;
	}

}




