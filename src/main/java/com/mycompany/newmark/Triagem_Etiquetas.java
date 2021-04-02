/**
 * @author Felipe Marques, Gabriel Ramos, Rafael Henrique e Adriano Vilhena 
 * 
 */
package com.mycompany.newmark;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Triagem_Etiquetas {

	public Chaves_Resultado triarBanco(String processo, String bancos, String localtriagem, String tipoTriagem) {
		Chaves_Resultado resultado = new Chaves_Resultado();
		Tratamento tratamento = new Tratamento();
		processo = tratamento.tratamento(processo);
		boolean banco = false;
		while (banco == false) {
			try {
				Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
				PreparedStatement stmt;
				ResultSet resultSet;
				if (bancos.contains("TODOS OS BANCOS")) {
					stmt = connection.prepareStatement(
							"SELECT * FROM ETIQUETAS WHERE TIPO = '" + localtriagem + "' ORDER BY PRIORIDADE DESC");
				} else {
					stmt = connection.prepareStatement("SELECT * FROM ETIQUETAS WHERE BANCO = '" + bancos
							+ "' AND TIPO = '" + localtriagem + "' ORDER BY PRIORIDADE DESC");
				}
				resultSet = stmt.executeQuery();
				while (resultSet.next()) {
					String PALAVRACHAVE = resultSet.getString("PALAVRACHAVE");
					PALAVRACHAVE = tratamento.tratamento(PALAVRACHAVE);
					String COMPLEMENTO = resultSet.getString("COMPLEMENTO");
					COMPLEMENTO = tratamento.tratamento(COMPLEMENTO);

					if (processo.contains(PALAVRACHAVE) && processo.contains(COMPLEMENTO)) {
						resultado.setPalavraChave(resultSet.getString("PALAVRACHAVE"));
						resultado.setComplemento(resultSet.getString("COMPLEMENTO"));
						resultado.setEtiqueta(resultSet.getString("ETIQUETA"));
						connection.close();
						return resultado;
					}
				}
				connection.close();
				banco = true;
			} catch (SQLException ex) {
				banco = false;
				Logger.getLogger(Triagem_Etiquetas.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		resultado
				.setEtiqueta("NÃO FOI POSSÍVEL LOCALIZAR FRASE CHAVE ATUALIZADA (" + bancos + ", " + tipoTriagem + ")");
		return resultado;
	}
}