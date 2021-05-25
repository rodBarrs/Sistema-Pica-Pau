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

import javax.swing.JOptionPane;

public class Triagem_Etiquetas {

	public Chaves_Resultado triarBanco(String processo, String banco, String localtriagem, String tipoTriagem,
			Boolean identificadorDePeticao) {
		Chaves_Resultado resultado = new Chaves_Resultado();
		Tratamento tratamento = new Tratamento();
		processo = tratamento.tratamento(processo);
		boolean bancoBool = false;
		while (bancoBool == false) {
			try {
				Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
				PreparedStatement stmt;
				ResultSet resultSet;
				if (identificadorDePeticao) {
					stmt = connection.prepareStatement("SELECT * FROM ETIQUETAS WHERE TIPO = 'PET' ORDER BY PRIORIDADE DESC");
				} else {
					if (banco.contains("TODOS OS BANCOS")) {
						stmt = connection.prepareStatement(
								"SELECT * FROM ETIQUETAS WHERE TIPO = '" + localtriagem + "' ORDER BY PRIORIDADE DESC");
					} else {
						stmt = connection.prepareStatement("SELECT * FROM ETIQUETAS WHERE BANCO = '" + banco
								+ "' AND TIPO = '" + localtriagem + "' ORDER BY PRIORIDADE DESC");
					}
				}
				resultSet = stmt.executeQuery();
				while (resultSet.next()) {
					String PALAVRACHAVE = resultSet.getString("PALAVRACHAVE");
					PALAVRACHAVE = tratamento.tratamento(PALAVRACHAVE);				
					String COMPLEMENTO = resultSet.getString("COMPLEMENTO");
					COMPLEMENTO = tratamento.tratamento(COMPLEMENTO);
					
					if (processo.contains(PALAVRACHAVE) && processo.contains(COMPLEMENTO)) {
						resultado.setPalavraChave(resultSet.getString("ID"));
						resultado.setEtiqueta(resultSet.getString("ETIQUETA"));
						connection.close();
						return resultado;
					}

				}
				connection.close();
				bancoBool = true;
			} catch (SQLException ex) {
				bancoBool = false;
				Logger.getLogger(Triagem_Etiquetas.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		resultado.setEtiqueta("NÃO FOI POSSÍVEL LOCALIZAR FRASE CHAVE ATUALIZADA (" + banco + ", " + tipoTriagem + ")");
		return resultado;
	}
}