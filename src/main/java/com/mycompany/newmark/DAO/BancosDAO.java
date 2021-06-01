package com.mycompany.newmark.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.newmark.Aviso;
import com.mycompany.newmark.Chaves_GrupoEtiquetas;
import com.mycompany.newmark.connectionFactory.ConnectionFactory;

public class BancosDAO {

	public List<Chaves_GrupoEtiquetas> getTabelaBancoDeDados() {

		final String SQL = "SELECT * FROM BANCOS ORDER BY NOME";

		List<Chaves_GrupoEtiquetas> chaves = new ArrayList<>();

		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				Chaves_GrupoEtiquetas key = new Chaves_GrupoEtiquetas();
				key.setSigla(resultSet.getString("SIGLA"));
				key.setNome(resultSet.getString("NOME"));
				key.setQntEtiquetas(getNumeroEtiquetasBanco(key.getSigla()));
				chaves.add(key);
			}

		} catch (SQLException erro) {
			Aviso aviso = new Aviso();
			String textoAviso = "" + erro.getMessage();
			aviso.aviso(textoAviso);
		}
		
		return chaves;
	}
	
	private Integer getNumeroEtiquetasBanco(String siglaBanco) {
		final String SQL = "SELECT * FROM ETIQUETAS WHERE BANCO = '" + siglaBanco + "'";
		Integer numeroEtiquetas = 0;
		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				numeroEtiquetas++;
			}
		} catch (Exception e) {
			Aviso aviso = new Aviso();
			String textoAviso = "" + e.getMessage();
			aviso.aviso(textoAviso);
		}
		return numeroEtiquetas;
	}
}
