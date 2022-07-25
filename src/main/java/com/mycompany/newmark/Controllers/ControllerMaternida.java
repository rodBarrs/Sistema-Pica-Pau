/**
 * @author Felipe Marques, Gabriel Ramos, Rafael Henrique e Adriano Vilhena
 */
package com.mycompany.newmark.Controllers;

import com.mycompany.newmark.*;
import com.mycompany.newmark.Repositories.RepositoryMaternidade;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ControllerMaternida {


	public Chaves_Resultado iniciar(WebDriver driver, WebDriverWait wait, Chaves_Configuracao config, String bancos) throws InterruptedException {
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

		materidadeRepositorio.clicarDosprev(driver, wait);

		return resultado;
	}

}




