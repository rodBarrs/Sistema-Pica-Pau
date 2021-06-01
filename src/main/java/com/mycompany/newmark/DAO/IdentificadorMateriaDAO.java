package com.mycompany.newmark.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mycompany.newmark.Aviso;
import com.mycompany.newmark.Banco;
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

	public void inserirIdentificadorMateria(String pedido, String complementoPedido, String subnucleo,
			Integer pesoSelecionado) {

		final String SQL = "INSERT INTO ETIQUETAS (PALAVRACHAVE, COMPLEMENTO, ETIQUETA, TIPO, PRIORIDADE, BANCO) VALUES (?, ?, ?, ?, ?, ?);";

		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			stmt.setString(1, pedido);
			stmt.setString(2, complementoPedido);
			stmt.setString(3, subnucleo);
			stmt.setString(4, "PET");
			stmt.setInt(5, pesoSelecionado);
			stmt.setString(6, "JEF");

			stmt.execute();

			new Aviso().aviso("Etiqueta inserida com sucesso!");

		} catch (SQLException erro) {
			if (erro.getMessage().contains("UNIQUE constraint")) {
				new Aviso().aviso("Não foi possível inserir o registro:\n" + "O registro já esta cadastrado!");
				erro.printStackTrace();
			} else {
				Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, erro);
				new Aviso().aviso(erro.getMessage());
			}
		}
	}

	public Boolean removerIdentificadorMateria(Integer idSelecionado) {
		Boolean isSucesso = null;
		final String SQL = "DELETE FROM ETIQUETAS WHERE ID = ?";
		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			stmt.setInt(1, idSelecionado);
			stmt.executeUpdate();
			isSucesso = true;
		} catch (Exception e) {
			e.printStackTrace();
			isSucesso = false;
		}

		return isSucesso;

	}

	public List<Chaves_Banco> buscaIdentificadorPorID(String id) {

		List<Chaves_Banco> chaves = new ArrayList<>();

		final String SQL = "SELECT * FROM ETIQUETAS WHERE ID = ?";

		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Chaves_Banco chave = new Chaves_Banco();
				chave.setID(Integer.valueOf(rs.getString("ID")));
				chave.setPALAVRACHAVE(rs.getString("PALAVRACHAVE"));
				chave.setCOMPLEMENTO(rs.getString("COMPLEMENTO"));
				chave.setETIQUETA(rs.getString("ETIQUETA"));
				chave.setTIPO(rs.getString("TIPO"));
				chave.setPRIORIDADE(rs.getString("PRIORIDADE"));
				chave.setBANCO(rs.getString("BANCO"));
				chaves.add(chave);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return chaves;

	}

	public List<Chaves_Banco> buscaIdentificador(String textoBusca) {
		final String SQL = "SELECT * FROM ETIQUETAS WHERE TIPO = 'PET' AND PALAVRACHAVE LIKE ? OR COMPLEMENTO LIKE ? OR ETIQUETA LIKE ?";
		
		List<Chaves_Banco> chaves = new ArrayList<>();
		
		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)){
				stmt.setString(1, '%' + textoBusca + '%');
				stmt.setString(2, '%' + textoBusca + '%');
				stmt.setString(3, '%' + textoBusca + '%');
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Chaves_Banco chave = new Chaves_Banco();
				chave.setID(Integer.valueOf(rs.getString("ID")));
				chave.setPALAVRACHAVE(rs.getString("PALAVRACHAVE"));
				chave.setCOMPLEMENTO(rs.getString("COMPLEMENTO"));
				chave.setETIQUETA(rs.getString("ETIQUETA"));
				chave.setTIPO(rs.getString("TIPO"));
				chave.setPRIORIDADE(rs.getString("PRIORIDADE"));
				chave.setBANCO(rs.getString("BANCO"));
				chaves.add(chave);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return chaves;
		
	}
	
	public void atualizarIndetificadorMateria(Integer id, String palavrachave, String complemento, String etiqueta, String prioridade) {
		
		final String SQL = "UPDATE ETIQUETAS SET PALAVRACHAVE = ?, COMPLEMENTO = ?, ETIQUETA = ?, PRIORIDADE = ? WHERE ID = ?";
		
		try (Connection connection = new ConnectionFactory().obterConexao();
				PreparedStatement stmt = connection.prepareStatement(SQL)) {
			
			stmt.setString(1, palavrachave);
			stmt.setString(2, complemento);
			stmt.setString(3, etiqueta);
			stmt.setString(4, prioridade);
			stmt.setInt(5, id);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
