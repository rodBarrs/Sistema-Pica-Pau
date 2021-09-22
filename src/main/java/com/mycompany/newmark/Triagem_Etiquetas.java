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
					stmt = connection.prepareStatement("SELECT * FROM identificador_materia ORDER BY prioridade DESC");
				} else {
					if (banco.contains("TODOS OS BANCOS")) {
						stmt = connection.prepareStatement(
								"SELECT * FROM etiquetas WHERE tipo = '" + localtriagem + "' ORDER BY prioridade DESC");
					} else {
						stmt = connection.prepareStatement("SELECT * FROM etiquetas WHERE banco = '" + banco
								+ "' AND tipo = '" + localtriagem + "' ORDER BY prioridade DESC");
					}
				}
				resultSet = stmt.executeQuery();
				while (resultSet.next()) {
					String PALAVRACHAVE = resultSet.getString("palavrachave");
					PALAVRACHAVE = tratamento.tratamento(PALAVRACHAVE);
					String COMPLEMENTO = resultSet.getString("complemento");
					COMPLEMENTO = tratamento.tratamento(COMPLEMENTO);

					if (processo.contains(PALAVRACHAVE) && processo.contains(COMPLEMENTO)) {
						//resultado.setId(resultSet.getInt("id"));
						resultado.setPalavraChave(resultSet.getString("id"));
						resultado.setSubnucleo(resultSet.getString("subnucleo"));

						resultSet.getString("etiqueta");
						if (resultSet.wasNull()) {
							resultado.setEtiqueta(resultSet.getString("subnucleo"));
						} else {							
							resultado.setEtiqueta(resultSet.getString("subnucleo") + "/" + resultSet.getString("etiqueta"));
						}
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