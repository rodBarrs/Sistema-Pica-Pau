package com.mycompany.newmark.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.newmark.Chaves_Banco;
import com.mycompany.newmark.connectionFactory.ConnectionFactory;

public class EtiquetaDAO {

	public List<Chaves_Banco> getTabelaEtiqueta(String banco) {
		final String SQL = "SELECT * FROM ETIQUETAS WHERE BANCO = '" + banco + "' AND TIPO != 'PET' ORDER BY ID";

		List<Chaves_Banco> chaves = new ArrayList<>();
		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				Chaves_Banco key = new Chaves_Banco();
				key.setID(resultSet.getInt("ID"));
				key.setPALAVRACHAVE(resultSet.getString("PALAVRACHAVE"));
				key.setCOMPLEMENTO(resultSet.getString("COMPLEMENTO"));
				key.setETIQUETA(resultSet.getString("ETIQUETA"));
				key.setTIPO(resultSet.getString("TIPO"));
				key.setPRIORIDADE(resultSet.getString("PRIORIDADE"));

				chaves.add(key);
			}
		} catch (Exception e) { }
		return chaves;

	}

}
