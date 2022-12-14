package com.mycompany.newmark.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.newmark.Aviso;
import com.mycompany.newmark.Chaves_Condicao;
import com.mycompany.newmark.connectionFactory.ConnectionFactory;

public class TipoMovimentacaoDAO {

	public List<Chaves_Condicao> getTabelaTipoMovimentacao() {
		final String SQL = "SELECT * FROM condicao WHERE tipo = 'PRO' ORDER BY texto";
		
		List<Chaves_Condicao> chaves = new ArrayList<>();
		
		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				Chaves_Condicao key = new Chaves_Condicao();
				key.setTIPO("PRO");
				key.setTEXTO(resultSet.getString("texto"));
				chaves.add(key);
			}
		} catch (Exception e) {
			new Aviso().aviso(e.getMessage());
		}
		return chaves;
	}

	public List<Chaves_Condicao> buscarTipoMovimentacao(String textoBusca) {
		final String SQL = "SELECT * FROM condicao WHERE tipo = 'PRO' AND texto LIKE ?";
		
		List<Chaves_Condicao> chaves = new ArrayList<>();
		
		try (Connection connection = new ConnectionFactory().obterConexao(); PreparedStatement stmt = connection.prepareStatement(SQL)){
			stmt.setString(1, '%' + textoBusca + '%');
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Chaves_Condicao chave = new Chaves_Condicao();
				chave.setTEXTO(rs.getString("texto"));
				chaves.add(chave);
			}
		} catch (Exception e) {
			new Aviso().aviso(e.getMessage());
		}
		
		return chaves;
	}

	public void removerTipoMovimentacao(String texto) {
		final String SQL = "DELETE FROM condicao WHERE tipo = 'PRO' AND texto = ?";
		try (Connection connection = new ConnectionFactory().obterConexao(); PreparedStatement stmt = connection.prepareStatement(SQL)){
			stmt.setString(1, texto);
			stmt.execute();
			new Aviso().aviso("Item removido");
		} catch (Exception e) {
			new Aviso().aviso("Item n??o removido\n" + e.getMessage());
		}
		
	}

	public void inserirTipoMovimentacao(String texto) {
		final String SQL = "INSERT INTO condicao (texto, tipo) VALUES (?, 'PRO')";
		try (Connection connection = new ConnectionFactory().obterConexao(); PreparedStatement stmt = connection.prepareStatement(SQL)){
			stmt.setString(1, texto);
			stmt.execute();
			new Aviso().aviso("Item inserido");
		} catch (Exception e) {
			new Aviso().aviso("Item n??o inserido\n" + e.getMessage());
		}
		
	}

	public void atualizarTipoMovimento(String texto, String novoTexto) {
		final String SQL = "UPDATE condicao SET texto = ? WHERE texto = ?";
		try (Connection connection = new ConnectionFactory().obterConexao(); PreparedStatement stmt = connection.prepareStatement(SQL)){
			stmt.setString(1, novoTexto);
			stmt.setString(2, texto);
			stmt.execute();
			new Aviso().aviso("Item atualizado");
		} catch (Exception e) {
			new Aviso().aviso("Item n??o atualizado\n" + e.getMessage());
		}
		
	}

}
