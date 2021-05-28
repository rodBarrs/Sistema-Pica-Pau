package com.mycompany.newmark.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.newmark.Chaves_Banco;
import com.mycompany.newmark.connectionFactory.ConnectionFactory;

public class IdentificadorMateriaDAO {

	public List<Chaves_Banco> getTabelaIdentificadorMateria() {

		final String SQL = "SELECT * FROM ETIQUETAS WHERE TIPO = 'PET' ORDER BY ID";
		List<Chaves_Banco> identificadoresMateria = new ArrayList<>();
		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				Chaves_Banco key = new Chaves_Banco();
				key.setID(resultSet.getInt("ID"));
				key.setPALAVRACHAVE(resultSet.getString("PALAVRACHAVE"));
				key.setCOMPLEMENTO(resultSet.getString("COMPLEMENTO"));
				key.setETIQUETA(resultSet.getString("ETIQUETA"));
				key.setPRIORIDADE(resultSet.getString("PRIORIDADE"));
				identificadoresMateria.add(key);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return identificadoresMateria;
	}

}
