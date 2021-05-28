package com.mycompany.newmark.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.mycompany.newmark.Banco;
import com.mycompany.newmark.Chaves_Banco;
import com.mycompany.newmark.Chaves_Condicao;
import com.mycompany.newmark.DAO.EtiquetaDAO;
import com.mycompany.newmark.DAO.IdentificadorMateriaDAO;
import com.mycompany.newmark.DAO.TipoMovimentacaoDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Administracao implements Initializable {

	@FXML
	private JFXTextField numeroEtiquetas, numeroTipoMovimentacao, numeroIdentificadorMateria, numeroPeticaoInicial;
	@FXML
	private JFXComboBox<String> comboBoxBancos;
	@FXML
	private TableView<Chaves_Banco> tabelaIdentificadorMateria, tabelaEtiquetas;
	@FXML
	private TableView<Chaves_Condicao> tabelaTipoMovimento, tabelaIdentificadorPeticao;
	@FXML
	private TableColumn<Chaves_Banco, String> identificadorMateriaID, identificadorMateriaPedido,
			identificadorMateriaComplemento, identificadorMateriaSubnucleo, identificadorMateriaPeso, etiquetaID,
			etiquetaFraseChave, etiquetaComplemento, etiquetaEtiqueta, etiquetaPeso, etiquetaTipo;
	@FXML
	private TableColumn<Chaves_Condicao, String> colunaTipoMovimento, colunaIdentificadorPeticao;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		inicializarMenuPeticaoInicial();
		inicializarMenuTriagemPadrao();
		inicializarBancoDeDados();
	}

	/* Inicializações */
	
	public void inicializarMenuPeticaoInicial() {
		/* Iniciliza a tabela Identificador de Matérias */
		List<Chaves_Banco> listaIdentificadoresMateria = new IdentificadorMateriaDAO().getTabelaIdentificadorMateria();
		
		identificadorMateriaID.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("ID"));
		identificadorMateriaPedido.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("PALAVRACHAVE"));
		identificadorMateriaComplemento.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("COMPLEMENTO"));
		identificadorMateriaSubnucleo.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("ETIQUETA"));
		identificadorMateriaPeso.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("PRIORIDADE"));
		ObservableList<Chaves_Banco> identificadoresDeMateria = FXCollections.observableArrayList(listaIdentificadoresMateria);
		tabelaIdentificadorMateria.setItems(identificadoresDeMateria);
		numeroIdentificadorMateria.setText(String.valueOf(tabelaIdentificadorMateria.getItems().size()));
	}

	public void inicializarMenuTriagemPadrao() {
		/* Inicializa os campos do comboBox */
		comboBoxBancos.setItems(new Banco().setarBanco());		
		try {
			comboBoxBancos.getItems().remove(0);
			comboBoxBancos.getSelectionModel().selectFirst();
		} catch (Exception e) {

		}

		/* Inicializa a tabela Etiquetas */
		String bancoSelecionado = comboBoxBancos.getSelectionModel().getSelectedItem().substring(0, 3);
		
		List<Chaves_Banco> listaTabelaEtiquetas = new EtiquetaDAO().getTabelaEtiqueta(bancoSelecionado);

		etiquetaID.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("ID"));
		etiquetaFraseChave.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("PALAVRACHAVE"));
		etiquetaComplemento.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("COMPLEMENTO"));
		etiquetaEtiqueta.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("ETIQUETA"));
		etiquetaPeso.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("PRIORIDADE"));
		etiquetaTipo.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("TIPO"));
		ObservableList<Chaves_Banco> genericos = FXCollections.observableArrayList(listaTabelaEtiquetas);

		tabelaEtiquetas.setItems(genericos);
		numeroEtiquetas.setText(String.valueOf(tabelaEtiquetas.getItems().size()));

		/* Inicializa tabela Tipos de Movimentação */
		List<Chaves_Condicao> listaTiposMovimentacao = new TipoMovimentacaoDAO().getTabelaTipoMovimentacao();
		colunaTipoMovimento.setCellValueFactory(new PropertyValueFactory<Chaves_Condicao, String>("TEXTO"));
		ObservableList<Chaves_Condicao> tiposMovimentacao = FXCollections.observableArrayList(listaTiposMovimentacao);
		tabelaTipoMovimento.setItems(tiposMovimentacao);
		numeroTipoMovimentacao.setText(String.valueOf(tabelaTipoMovimento.getItems().size()));

	}

	public void inicializarBancoDeDados() {

	}

	/* Global */

	public void retornaMenu() {

	}

	/* Identificador de Matéria */

	public void inserirIdentificadorMateria() {

	}

	public void alterarIdentificadorMateria() {

	}

	public void excluirIdentificadorMateria() {

	}

	public void limparIdentificadorMateria() {

	}

	public void selecionarIdentificadorMateria() {

	}

	public void buscaIDIdentificadorMateria() {

	}

	public void buscaIdentificadorMateria() {

	}

	public void atualizarIdentificadorMateria() {

	}

	/* Identificador de Petição Inicial */

	public void inserirIdentificadorPeticao() {

	}

	public void excluirIdentificadorPeticao() {

	}

	public void buscaIdentificadorPeticao() {

	}

	public void alterarIdentificadorPeticao() {

	}

	public void selecionarIdentificadorPeticao() {

	}

	/* Etiquetas */

	public void selecionarBancoEtiqueta() {

	}

	public void buscaEtiqueta() {

	}

	public void buscaEtiquetaID() {

	}

	public void limparEtiqueta() {

	}

	public void atualizarTabelaEtiqueta() {

	}

	public void inserirEtiqueta() {

	}

	public void excluirEtiqueta() {

	}

	public void selecionarEtiqueta() {

	}

	public void alterarEtiqueta() {

	}

	/* Tipos de Movimentação */

	public void buscaTipoMovimentacao() {

	}

	public void excluirTipoMovimentacao() {

	}

	public void inserirTipoMovimentacao() {

	}

	public void selecionarTipoMovimentacao() {

	}

	public void alterarTipoMovimentacao() {

	}

	/* Bancos de Dados */

	public void inserirBanco() {

	}

	public void excluirBanco() {

	}

	public void atualizarTabelaBanco() {

	}

	public void selecionarBanco() {

	}

	/* Usuários */

	public void inserirUsuario() {

	}

	public void excluirUsuario() {

	}

	public void atualizarUsuarios() {

	}

}
