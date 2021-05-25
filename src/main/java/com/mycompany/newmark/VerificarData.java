package com.mycompany.newmark;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VerificarData {
	
	public boolean verificar(String textoMovimentacao) {

		try {
			LocalDateTime dataAtual = LocalDateTime.now();
			textoMovimentacao = textoMovimentacao.replaceAll(" ", "");
			Integer indexDataMovimentacao = textoMovimentacao.indexOf("-") + 1;

			String dataMovimentacao = textoMovimentacao.substring(indexDataMovimentacao, indexDataMovimentacao + 15);

			String diaMovimentacao = dataMovimentacao.substring(0, 2);
			String mesMovimentacao = dataMovimentacao.substring(3, 5);
			String anoMovimentacao = dataMovimentacao.substring(6, 10);
			String horaMovimentacao = dataMovimentacao.substring(10, 15);

			// Inverte ordem data para conversão
			dataMovimentacao = anoMovimentacao + "-" + mesMovimentacao + "-" + diaMovimentacao + " " + horaMovimentacao;

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime dataFinalMovimentacao = LocalDateTime.parse(dataMovimentacao, formatter);
			return dataFinalMovimentacao.isAfter(dataAtual.minusDays(30));
		} catch (Exception e) {
			return false;
		}

	}
}
