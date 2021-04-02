/**
 * @author Felipe Marques, João Paulo, Gabriel Ramos, Rafael Henrique
 * 
 * Classe responsável por identificar e, caso não exista, criar, o Banco de dados além de inserir, alterar e excluir os dados lá armazenados 
 */

package com.mycompany.newmark;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Banco {
	@SuppressWarnings("ConvertToTryWithResources")
	public void conectar() {
		try {
			//Estabelece a conecção com o banco de dados
			Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
			Statement comandoSql = connection.createStatement();
			//Cria o banco de dados, com todas as tabelas caso o mesmo não seja localizado no diretorio especificado
			comandoSql.execute("CREATE TABLE IF NOT EXISTS BANCOS (                 \n"
					+ "SIGLA        VARCHAR (3)         NOT NULL,                       \n"
					+ "NOME         STRING              NOT NULL,                       \n"
					+ "PRIMARY KEY (SIGLA),                                             \n"
					+ " UNIQUE (SIGLA, NOME)                                            \n" + ");");

			comandoSql.execute("CREATE TABLE IF NOT EXISTS CONDICAO (               \n"
					+ "TEXTO        VARCHAR (100)       NOT NULL,                       \n"
					+ "TIPO         VARCHAR (3)         NOT NULL,                       \n"
					+ "PRIMARY KEY (TEXTO,TIPO)                                         \n" + ");");

			comandoSql.execute("CREATE TABLE IF NOT EXISTS CONFIGURACAO (           \n"
					+ "ID           INT                 NOT NULL    DEFAULT (1997),     \n"
					+ "TriarAntigo  BOOLEAN             NOT NULL    DEFAULT (false),    \n"
					+ "TipoTriagem  VARCHAR (3)         NOT NULL    DEFAULT ('COM'),    \n"
					+ "JuntManual   BOOLEAN             NOT NULL    DEFAULT (false),     \n"
					+ "LaudoPericial BOOLEAN            NOT NULL    DEFAULT (false),    \n"
					+ "PeticaoInicial BOOLEAN           NOT NULL    DEFAULT (true),     \n"
					+ "Login        STRING                          DEFAULT (''),       \n"
					+ "Senha        STRING                          DEFAULT (''),       \n"
					+ "PRIMARY KEY (ID)                                                 \n" + ");");

			comandoSql.execute("CREATE TABLE IF NOT EXISTS CONTADOR (               \n"
					+ "ID           INT                 NOT NULL    DEFAULT (1997),     \n"
					+ "ContTotal    INT                 NOT NULL    DEFAULT (0),        \n"
					+ "ContNao      INT                 NOT NULL    DEFAULT (0),        \n"
					+ "ContDoc      INT                 NOT NULL    DEFAULT (0),        \n"
					+ "ContSeq      INT                 NOT NULL    DEFAULT (0),        \n"
					+ "ContErro     INT                 NOT NULL    DEFAULT (0),        \n"
					+ "PRIMARY KEY (ID)                                                 \n" + ");");

			comandoSql.execute("CREATE TABLE IF NOT EXISTS ETIQUETAS (              \n"
					+ "PALAVRACHAVE VARCHAR (45)        NOT NULL,                       \n"
					+ "COMPLEMENTO  VARCHAR (100)       NOT NULL,                       \n"
					+ "ETIQUETA     VARCHAR (100)       NOT NULL,                       \n"
					+ "TIPO         STRING              NOT NULL    DEFAULT ('MOV'), \n"
					+ "[PRIORIDADE] VARCHAR (100)       NOT NULL    DEFAULT (0),        \n"
					+ "BANCO        VARCHAR (3)         NOT NULL,                       \n"
					+ "PRIMARY KEY (PALAVRACHAVE,COMPLEMENTO,BANCO)                     \n" + ");");

			//Verifica se o banco foi criado pelo codigo acima, caso tenha sido, o codigo a baixo implementará as linhas de configuração e contador para o correto funcionamento das demais classes
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM CONFIGURACAO");
			ResultSet resultSet = stmt.executeQuery();
			int linhasBanco = 0;
			while (resultSet.next()) {
				linhasBanco++;
			}
			if (linhasBanco == 0) {
				comandoSql.execute("INSERT INTO CONTADOR (ID,ContTotal, ContNao, ContDoc, ContSeq, ContErro)       \n"
						+ "VALUES (1997,0,0,0,0,0);");
				comandoSql.execute(
						"INSERT INTO CONFIGURACAO (ID,TriarAntigo, TipoTriagem, JuntManual, LaudoPericial, PeticaoInicial, Login, Senha)  \n"
								+ "VALUES (1997,'false','COM','true','false','true','','');");
			}

			//Desconecta com o banco de dados, garantindo assim a integridade do dados
			connection.close();
		} catch (SQLException erro) {
			Aviso aviso = new Aviso();
			aviso.aviso(erro.getMessage());
		}
	}

	//Insere etiquetas dentro do banco na tabela ETIQUETAS
	public void inserirEtiquetas(Chaves_Banco chave) {
		Aviso aviso = new Aviso();
		String textoAviso = "";
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
			Statement comandoSql = connection.createStatement();
			comandoSql
					.execute("INSERT INTO ETIQUETAS (PALAVRACHAVE,COMPLEMENTO,ETIQUETA,TIPO,PRIORIDADE,BANCO) VALUES ("
							+ "'" + chave.getPALAVRACHAVE() + "'," + "'" + chave.getCOMPLEMENTO() + "'," + "'"
							+ chave.getETIQUETA() + "'," + "'" + chave.getTIPO() + "'," + "'" + chave.getPRIORIDADE()
							+ "'," + "'" + chave.getBANCO() + "');");
			//Desconecta com o banco de dados, garantindo assim a integridade do dados
			connection.close();
			textoAviso = "Etiqueta inserida com sucesso!";
			aviso.aviso(textoAviso);
		} catch (SQLException erro) {
			if (erro.getMessage().contains("UNIQUE constraint")) {
				erro.printStackTrace();
				textoAviso = "Não foi possível inserir o registro:\n" + "O registro já esta cadastrado!";
				aviso.aviso(textoAviso);
			} else {
				Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, erro);
				aviso.aviso(erro.getMessage() + "\nCódigo do Erro: " + erro.getErrorCode());
			}
		}
	}

	//Altera etiquetas dentro do banco na tabela ETIQUETAS
	public void alterarEtiquetas(Chaves_Banco chave, String PalavraChave, String Complemento, String Etiqueta,
			String Tipo, String Prioridade) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
			Statement comandoSql = connection.createStatement();
			comandoSql.execute("UPDATE ETIQUETAS SET " + "PALAVRACHAVE = '" + PalavraChave + "', " + "COMPLEMENTO = '"
					+ Complemento + "', " + "ETIQUETA = '" + Etiqueta + "', " + "TIPO = '" + Tipo + "', "
					+ "PRIORIDADE = '" + Prioridade + "' " + "WHERE PALAVRACHAVE = '" + chave.getPALAVRACHAVE() + "' "
					+ "AND COMPLEMENTO = '" + chave.getCOMPLEMENTO() + "' " + "AND BANCO = '" + chave.getBANCO()
					+ "';");
			//Desconecta com o banco de dados, garantindo assim a integridade do dados
			connection.close();
		} catch (SQLException erro) {
			Aviso aviso = new Aviso();
			if (erro.getMessage().contains("UNIQUE constraint")) {
				String textoAviso = "Não foi possível inserir o registro:\n" + "O registro já esta cadastrado!";
				aviso.aviso(textoAviso);
			} else {
				Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, erro);
				aviso.aviso(erro.getMessage() + "\nCódigo do Erro: " + erro.getErrorCode());
			}
		}
	}

	//Exclui etiquetas dentro do banco na tabela ETIQUETAS
	public void excluirEtiquetas(Chaves_Banco chave) {
		Aviso aviso = new Aviso();
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
			Statement comandoSql = connection.createStatement();
			comandoSql.execute("DELETE FROM ETIQUETAS WHERE" + "(PALAVRACHAVE = '" + chave.getPALAVRACHAVE() + "') AND "
					+ "(COMPLEMENTO = '" + chave.getCOMPLEMENTO() + "') AND " + "(BANCO = '" + chave.getBANCO()
					+ "');");
			connection.close();
			String textoAviso = "Etiqueta deletada!";
			aviso.aviso(textoAviso);
		} catch (SQLException erro) {
			Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, erro);
			aviso.aviso(erro.getMessage() + "\nCódigo do Erro: " + erro.getErrorCode());
		}
	}

	//Insere dados dentro da tabela PROVJURI e CABECALHO
	public void inserirCondicao(Chaves_Condicao chave) {
		Aviso aviso = new Aviso();
		String textoAviso = "";
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
			Statement comandoSql = connection.createStatement();
			//Inserindo
			comandoSql.execute("INSERT INTO CONDICAO VALUES(" + "'"
					+ chave.getTEXTO().toUpperCase().replaceAll("'", "").replaceAll("\"", "").trim() + "','"
					+ chave.getTIPO() + "');");
			//Desconecta com o banco de dados, garantindo assim a integridade do dados
			connection.close();
			if (chave.getTIPO().equals("PRO")) {
				textoAviso = "Providência adicionada!";
				aviso.aviso(textoAviso);
			} else {
				textoAviso = "Cabeçalho adicionado!";
				aviso.aviso(textoAviso);
			}
		} catch (SQLException erro) {
			if (erro.getMessage().contains("UNIQUE constraint")) {
				textoAviso = "Não foi possível inserir o registro:\n" + "O registro já esta cadastrado!";
				aviso.aviso(textoAviso);
			} else {
				Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, erro);
				aviso.aviso(erro.getMessage() + "\nCódigo do Erro: " + erro.getErrorCode());
			}
		}
	}

	//Altera dados dentro da tabela PROVJURI e CABECALHO
	public void alterarCondicao(Chaves_Condicao chave, String texto) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
			Statement comandoSql = connection.createStatement();
			comandoSql.execute("UPDATE CONDICAO SET TEXTO = '" + texto + "'" + " WHERE TEXTO = '" + chave.getTEXTO()
					+ "'" + " AND TIPO = '" + chave.getTIPO() + "';");
			//Desconecta com o banco de dados, garantindo assim a integridade do dados
			connection.close();
		} catch (SQLException erro) {
			Aviso aviso = new Aviso();
			if (erro.getMessage().contains("UNIQUE constraint")) {
				String textoAviso = "Não foi possível inserir o registro:\n" + "O registro já esta cadastrado!";
				aviso.aviso(textoAviso);
			} else {
				Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, erro);
				aviso.aviso(erro.getMessage() + "\nCódigo do Erro: " + erro.getErrorCode());
			}
		}
	}

	//Exclui dados dentro da tabela PROVJURI e CABECALHO
	public void excluirCondicao(Chaves_Condicao chave) {
		Aviso aviso = new Aviso();
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
			Statement comandoSql = connection.createStatement();
			//Excluir
			comandoSql.execute("DELETE FROM CONDICAO WHERE TIPO ='" + chave.getTIPO() + "' AND TEXTO = '"
					+ chave.getTEXTO() + "';");
			//Desconecta com o banco de dados, garantindo assim a integridade do dados
			connection.close();

			String textoAviso = "Condição deletada!";
			aviso.aviso(textoAviso);
		} catch (SQLException erro) {
			Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, erro);
			aviso.aviso(erro.getMessage() + "\nCódigo do Erro: " + erro.getErrorCode());
		}
	}

	public void salvarAvancadas(boolean antigo, String triagem, boolean juntadamanual) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
			Statement comandoSql = connection.createStatement();
			comandoSql.execute("UPDATE CONFIGURACAO SET" + " TriarAntigo = '" + antigo + "'          \n"
					+ ", TipoTriagem = '" + triagem + "'        \n" + ", JuntManual = '" + juntadamanual + "'   \n"
					+ " WHERE ID = 1997;");
			//Desconecta com o banco de dados, garantindo assim a integridade do dados
			connection.close();
		} catch (SQLException erro) {
			Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, erro);
			Aviso aviso = new Aviso();
			aviso.aviso(erro.getMessage() + "\nCódigo do Erro: " + erro.getErrorCode());
		}
	}

	//Altera o valor da unica linha da coluna LaudoPericial na tabela CONFIG para alterar o processo de Triagem
	public void salvarEspecificas(boolean laudoPericial, boolean peticaoInicial) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
			Statement comandoSql = connection.createStatement();
			comandoSql.execute("UPDATE CONFIGURACAO SET" + " LaudoPericial = '" + laudoPericial + "'   \n"
					+ ", PeticaoInicial = '" + peticaoInicial + "' \n" + " WHERE ID = 1997;");
			//Desconecta com o banco de dados, garantindo assim a integridade do dados
			connection.close();
		} catch (SQLException erro) {
			Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, erro);
			Aviso aviso = new Aviso();
			aviso.aviso(erro.getMessage() + "\nCódigo do Erro: " + erro.getErrorCode());
		}
	}

	public void salvarSenha(Usuario usuario) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
			Statement comandoSql = connection.createStatement();
			comandoSql.execute("UPDATE CONFIGURACAO SET Login = '" + usuario.getLogin() + "'   \n" + ", Senha = '"
					+ usuario.getSenha() + "' " + "WHERE ID = 1997;");
			//Desconecta com o banco de dados, garantindo assim a integridade do dados
			connection.close();
		} catch (SQLException erro) {
			Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, erro);
			Aviso aviso = new Aviso();
			aviso.aviso(erro.getMessage() + "\nCódigo do Erro: " + erro.getErrorCode());
		}
	}

	//Metodos para realizar a incrementação dos contadores
	public void contarNao() throws SQLException {
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
			Statement comandoSql = connection.createStatement();

			comandoSql.execute("UPDATE CONTADOR SET" + " ContNao = ContNao + 1" + ", ContTotal = ContTotal + 1"
					+ " WHERE ID = 1997;");
			connection.close();
		} catch (SQLException erro) {
			Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, erro);
			Aviso aviso = new Aviso();
			aviso.aviso(erro.getMessage() + "\nCódigo do Erro: " + erro.getErrorCode());
		}
	}

	public void contarDoc() {
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
			Statement comandoSql = connection.createStatement();
			comandoSql.execute("UPDATE CONTADOR SET" + " ContDoc = ContDoc + 1" + ", ContTotal = ContTotal + 1"
					+ " WHERE ID = 1997;");
			//Desconecta com o banco de dados, garantindo assim a integridade do dados
			comandoSql.close();
			connection.close();
		} catch (SQLException erro) {
			Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, erro);
			Aviso aviso = new Aviso();
			aviso.aviso(erro.getMessage() + "\nCódigo do Erro: " + erro.getErrorCode());
		}
	}

	public void contarMov() {
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
			Statement comandoSql = connection.createStatement();
			comandoSql.execute("UPDATE CONTADOR SET" + " ContSeq = ContSeq + 1" + ", ContTotal = ContTotal + 1"
					+ " WHERE ID = 1997;");
			//Desconecta com o banco de dados, garantindo assim a integridade do dados
			comandoSql.close();
			connection.close();
		} catch (SQLException erro) {
			Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, erro);
			Aviso aviso = new Aviso();
			aviso.aviso(erro.getMessage() + "\nCódigo do Erro: " + erro.getErrorCode());
		}
	}

	public Chaves_Configuracao pegarConfiguracao(Chaves_Configuracao config) {
		try {
			boolean TriarAntigo;
			boolean JuntManual;
			boolean LaudoPericial;
			boolean PeticaoInicial;

			Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM CONFIGURACAO WHERE ID = 1997");
			ResultSet resultadoBanco = stmt.executeQuery();
			while (resultadoBanco.next()) {
				String TriarAntigoString = resultadoBanco.getString("TriarAntigo");
				if (TriarAntigoString.contains("false")) {
					TriarAntigo = false;
				} else {
					TriarAntigo = true;
				}

				String TipoTriagemLocal = resultadoBanco.getString("TipoTriagem");

				String JuntManualString = resultadoBanco.getString("JuntManual");
				if (JuntManualString.contains("false")) {
					JuntManual = false;
				} else {
					JuntManual = true;
				}

				String LaudoPericialString = resultadoBanco.getString("LaudoPericial");
				if (LaudoPericialString.contains("false")) {
					LaudoPericial = false;
				} else {
					LaudoPericial = true;
				}

				String PeticaoInicialString = resultadoBanco.getString("PeticaoInicial");
				if (PeticaoInicialString.contains("false")) {
					PeticaoInicial = false;
				} else {
					PeticaoInicial = true;
				}

				config.setTriarAntigo(TriarAntigo);
				config.setTipoTriagem(TipoTriagemLocal);
				config.setJuntManual(JuntManual);
				config.setLaudoPericial(LaudoPericial);
				config.setPeticaoInicial(PeticaoInicial);
			}
			connection.close();
		} catch (Exception erro) {
			Aviso aviso = new Aviso();
			aviso.aviso(erro.getMessage());
			erro.printStackTrace();
			Logger.getLogger(Chaves_Configuracao.class.getName()).log(Level.SEVERE, null, erro);
		}
		return config;
	}

	public void inserirBancos(Chaves_Condicao chave) {
		Aviso aviso = new Aviso();
		String textoAviso = "";
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
			Statement comandoSql = connection.createStatement();
			//Inserindo
			comandoSql.execute("INSERT INTO BANCOS VALUES(" + "'"
					+ chave.getTIPO().toUpperCase().replaceAll("'", "").replaceAll("\"", "").trim() + "','"
					+ chave.getTEXTO().toUpperCase().replaceAll("'", "").replaceAll("\"", "").trim() + "');");
			//Desconecta com o banco de dados, garantindo assim a integridade do dados
			connection.close();
			textoAviso = "Banco inserido com sucesso!";
			aviso.aviso(textoAviso);
		} catch (SQLException erro) {
			if (erro.getMessage().contains("UNIQUE constraint")) {
				textoAviso = "Não foi possível inserir o registro:\n" + "O registro já esta cadastrado!";
				aviso.aviso(textoAviso);
			} else {
				Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, erro);
				aviso.aviso(erro.getMessage() + "\nCódigo do Erro: " + erro.getErrorCode());
			}
		}
	}

	public void excluirBancos(Chaves_Condicao chave) {
		Aviso aviso = new Aviso();
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
			Statement comandoSql = connection.createStatement();
			//Excluir
			comandoSql.execute(
					"DELETE FROM BANCOS WHERE SIGLA ='" + chave.getTIPO() + "' AND NOME = '" + chave.getTEXTO() + "';");
			//Desconecta com o banco de dados, garantindo assim a integridade do dados
			connection.close();

			String textoAviso = "Banco deletado!";
			aviso.aviso(textoAviso);
		} catch (SQLException erro) {
			Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, erro);
			aviso.aviso(erro.getMessage() + "\nCódigo do Erro: " + erro.getErrorCode());
		}
	}

	public ObservableList<String> setarBanco() {
		ObservableList<String> Lista = null;
		try {
			List<String> lista = new ArrayList<>();
			lista.add("TODOS OS BANCOS");
			Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM BANCOS ORDER BY NOME");
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				lista.add(resultSet.getString("SIGLA") + " - " + resultSet.getString("NOME"));
			}
			Lista = FXCollections.observableArrayList(lista);
			connection.close();
		} catch (SQLException erro) {
			Aviso aviso = new Aviso();
			aviso.aviso(erro.getMessage() + "\nCódigo do Erro: " + erro.getErrorCode());
		}
		return Lista;
	}

	public String selecionarBanco(String bancoSelecionadoLocal) throws SQLException {
		String bancoSelecionado = bancoSelecionadoLocal;
		if (bancoSelecionado.contains("TODOS OS BANCOS")) {
			try {
				Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
				PreparedStatement stmt = connection.prepareStatement("SELECT * FROM BANCOS ORDER BY NOME");
				ResultSet resultSet = stmt.executeQuery();
				while (resultSet.next()) {
					if (bancoSelecionadoLocal.contains(resultSet.getString("SIGLA"))
							&& bancoSelecionadoLocal.contains(resultSet.getString("NOME"))) {
						bancoSelecionado = resultSet.getString("SIGLA");
						connection.close();
						return bancoSelecionado;
					}
				}
				connection.close();
			} catch (SQLException erro) {
				Aviso aviso = new Aviso();
				aviso.aviso(erro.getMessage() + "\nCódigo do Erro: " + erro.getErrorCode());
			}
		}
		return bancoSelecionado;
	}

	public List<String> selecionarIdentificadoresDePeticao() {
		return null;
	}
}