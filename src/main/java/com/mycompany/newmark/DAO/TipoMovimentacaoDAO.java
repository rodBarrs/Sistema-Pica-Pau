package com.mycompany.newmark.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.newmark.Chaves_Condicao;
import com.mycompany.newmark.connectionFactory.ConnectionFactory;

public class TipoMovimentacaoDAO {

	public List<Chaves_Condicao> getTabelaTipoMovimentacao() {
		final String SQL = "SELECT * FROM CONDICAO WHERE TIPO = 'PRO' ORDER BY TEXTO";
		
		List<Chaves_Condicao> chaves = new ArrayList<>();
		
		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				Chaves_Condicao key = new Chaves_Condicao();
				key.setTIPO("PRO");
				key.setTEXTO(resultSet.getString("TEXTO"));
				chaves.add(key);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return chaves;
	}

}
