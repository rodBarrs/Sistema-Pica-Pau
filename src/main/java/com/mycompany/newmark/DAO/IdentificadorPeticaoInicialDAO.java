package com.mycompany.newmark.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.newmark.Aviso;
import com.mycompany.newmark.Chaves_Banco;
import com.mycompany.newmark.Chaves_Condicao;
import com.mycompany.newmark.connectionFactory.ConnectionFactory;

public class IdentificadorPeticaoInicialDAO {
	public List<Chaves_Condicao> getTabelaIdentificadorPeticaoInicial() {
		
		List<Chaves_Condicao> listaIdentificadoresPeticaoInicial = new ArrayList<>();
		
		final String SQL = "SELECT * FROM CONDICAO WHERE TIPO = 'PET' ORDER BY TEXTO";
		
		try(Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Chaves_Condicao chave = new Chaves_Condicao();
				chave.setTEXTO(rs.getString("TEXTO"));
				listaIdentificadoresPeticaoInicial.add(chave);
			}
			
		} catch (Exception e) {
			Aviso aviso = new Aviso();
			String textoAviso = "" + e.getMessage();
			aviso.aviso(textoAviso);
		}
		
		return listaIdentificadoresPeticaoInicial;
	}

	public void inserirIdentificadorPeticaoInicial(String identificadorPeticao) {
		
		final String SQL = "INSERT INTO CONDICAO (TEXTO, TIPO) VALUES (?, ?)";
		
		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)){
			stmt.setString(1, identificadorPeticao);
			stmt.setString(2, "PET");
			stmt.execute();
			new Aviso().aviso("Item inserido");
		} catch (Exception e) {
			new Aviso().aviso("Item não inserido");
			e.printStackTrace();
		}
		
	}

	public void removerIdentificadorPeticaoInicial(String texto) {
		
		final String SQL = "DELETE FROM CONDICAO WHERE TEXTO = ?";
		
		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)){
			stmt.setString(1, texto);
			stmt.execute();
			new Aviso().aviso("Item removido");
		} catch (Exception e) {
			new Aviso().aviso("Item removido");
			e.printStackTrace();
		}
		
	}

	public List<Chaves_Condicao> buscarIdentificadorPeticao(String textoBusca) {
		
		List<Chaves_Condicao> chaves = new ArrayList<>();
		
		final String SQL = "SELECT * FROM CONDICAO WHERE TIPO = 'PET' AND TEXTO LIKE ?";
		
		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)){
			stmt.setString(1, '%' + textoBusca + '%');
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				System.out.println(rs.getString("TEXTO"));
				Chaves_Condicao chave = new Chaves_Condicao();
				chave.setTEXTO(rs.getString("TEXTO"));
				chaves.add(chave);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return chaves;
		
	}

	public void atualizarIdentificadorPeticao(String texto, String novoTexto) {
		
		final String SQL = "UPDATE CONDICAO SET TEXTO = ? WHERE TEXTO = ?";
		
		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			stmt.setString(1, novoTexto);
			stmt.setString(2, texto);
			stmt.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
}
