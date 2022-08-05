/**
 * @author Felipe Marques, Gabriel Ramos, Rafael Henrique e Adriano Vilhena
 */
package com.mycompany.newmark.Controllers;

import com.mycompany.newmark.*;
import com.mycompany.newmark.Repositories.RepositoryMaternidade;
import com.mycompany.newmark.entities.InformacoesDosprev;
import com.mycompany.newmark.entities.InformacoesSislabra;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;

public class ControllerMaternida {


	public Chaves_Resultado iniciar(WebDriver driver, WebDriverWait wait, Chaves_Configuracao config, String bancos) throws InterruptedException, IOException {
		InformacoesDosprev informacaoDosPrev = new InformacoesDosprev();
		InformacoesSislabra informacaoSislabra = new InformacoesSislabra();
		RepositoryMaternidade materidadeRepositorio = new RepositoryMaternidade();

		Chaves_Resultado resultado = new Chaves_Resultado();
		LeituraPDF pdf = new LeituraPDF();
		//resultado.setEtiqueta("NÃO FOI POSSÍVEL LOCALIZAR FRASE CHAVE ATUALIZADA");
		Triagem_Etiquetas triagem = new Triagem_Etiquetas();
		VerificarData verificarData = new VerificarData();
		Triagem_Condicao condicao = new Triagem_Condicao();
		String linhaMovimentacao = "";
		String condicaoProv = "PRO";
		String condicaoCabecalho = "CAB";
		String localTriagem = "DOC";
		String processo = "";

		String dataNascimentoCrianca;

		String passou = materidadeRepositorio.clicarDosprev(driver, wait);
		informacaoDosPrev = materidadeRepositorio.coletarInformacoesDosprev(driver, wait);
		String nbIndeferido = informacaoDosPrev.getNbProcessoIndeferido();
//		String passouSislabra = materidadeRepositorio.clicarSislabra(driver, wait);
//		informacaoSislabra = materidadeRepositorio.coletarInformacoesSislabra(driver, wait);
		dataNascimentoCrianca = materidadeRepositorio.clicarProcesoAdministrativo(driver, wait, nbIndeferido);

		resultado.setDriver(driver);
		return resultado;
	}

}




