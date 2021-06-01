package com.mycompany.newmark.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.mycompany.newmark.Aviso;
import com.mycompany.newmark.Banco;
import com.mycompany.newmark.Chaves_Banco;
import com.mycompany.newmark.Chaves_Condicao;
import com.mycompany.newmark.Chaves_GrupoEtiquetas;
import com.mycompany.newmark.Controller_TagEdicaoCondicao;
import com.mycompany.newmark.Controller_TagEdicaoMateria;
import com.mycompany.newmark.DAO.BancosDAO;
import com.mycompany.newmark.DAO.EtiquetaDAO;
import com.mycompany.newmark.DAO.IdentificadorMateriaDAO;
import com.mycompany.newmark.DAO.IdentificadorPeticaoInicialDAO;
import com.mycompany.newmark.DAO.TipoMovimentacaoDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Administracao implements Initializable {

	@FXML
	private JFXTextField numeroEtiquetas, numeroTipoMovimentacao, numeroIdentificadorMateria, numeroPeticaoInicial,
			pedido, complementoPedido, buscaIdentificadorMateriaID, buscaIdentificadorMateria,
			identificadorPeticaoInicial, pesquisaIdentificador;
	@FXML
	private JFXComboBox<String> comboBoxBancos, comboBoxNucleo;
	@FXML
	private TableView<Chaves_GrupoEtiquetas> tabelaBancos;
	@FXML
	private TableView<Chaves_Banco> tabelaIdentificadorMateria, tabelaEtiquetas;
	@FXML
	private TableView<Chaves_Condicao> tabelaTipoMovimento, tabelaIdentificadorPeticao;
	@FXML
	private TableColumn<Chaves_Banco, String> identificadorMateriaID, identificadorMateriaPedido,
			identificadorMateriaComplemento, identificadorMateriaSubnucleo, identificadorMateriaPeso, etiquetaID,
			etiquetaFraseChave, etiquetaComplemento, etiquetaEtiqueta, etiquetaPeso, etiquetaTipo;
	@FXML
	private TableColumn<Chaves_Condicao, String> colunaTipoMovimento, colunaIdentificadorPeticao,
			colunaIdentificadorPeticaoInicial;
	@FXML
	private TableColumn<Chaves_GrupoEtiquetas, String> bancoSigla, bancoNome, bancoNumeroDeEtiquetas;
	@FXML
	private RadioButton identificadorMateriaP1, identificadorMateriaP2, identificadorMateriaP3, identificadorMateriaP4;
	@FXML
	private JFXButton inserirIdentificadorMateria;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		inicializarMenuPeticaoInicial();
		inicializarMenuTriagemPadrao();
		inicializarBancoDeDados();
	}

	/* Inicializações */

	public void inicializarMenuPeticaoInicial() {
		/* Inicializa a tabela Identificador de Matérias */
		ObservableList<String> subnucleos = FXCollections.observableArrayList("SSEAS", "SBI", "SCC");
		comboBoxNucleo.setItems(subnucleos);

		List<Chaves_Banco> listaIdentificadoresMateria = new IdentificadorMateriaDAO().getTabelaIdentificadorMateria();

		identificadorMateriaID.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("ID"));
		identificadorMateriaPedido.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("PALAVRACHAVE"));
		identificadorMateriaComplemento
				.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("COMPLEMENTO"));
		identificadorMateriaSubnucleo.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("ETIQUETA"));
		identificadorMateriaPeso.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("PRIORIDADE"));
		ObservableList<Chaves_Banco> identificadoresDeMateria = FXCollections
				.observableArrayList(listaIdentificadoresMateria);
		tabelaIdentificadorMateria.setItems(identificadoresDeMateria);
		numeroIdentificadorMateria.setText(String.valueOf(tabelaIdentificadorMateria.getItems().size()));

		/* Inicializa a tabela Identificador de Petição Inicial */
		List<Chaves_Condicao> listaIdentificadorPeticao = new IdentificadorPeticaoInicialDAO()
				.getTabelaIdentificadorPeticaoInicial();

		colunaIdentificadorPeticaoInicial
				.setCellValueFactory(new PropertyValueFactory<Chaves_Condicao, String>("TEXTO"));
		ObservableList<Chaves_Condicao> identPeticao = FXCollections.observableArrayList(listaIdentificadorPeticao);
		tabelaIdentificadorPeticao.setItems(identPeticao);
		numeroPeticaoInicial.setText(String.valueOf(tabelaIdentificadorPeticao.getItems().size()));
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
		/* Inicializa a tabela Banco de Dados */
		List<Chaves_GrupoEtiquetas> chaves = new BancosDAO().getTabelaBancoDeDados();
		bancoSigla.setCellValueFactory(new PropertyValueFactory<Chaves_GrupoEtiquetas, String>("sigla"));
		bancoNome.setCellValueFactory(new PropertyValueFactory<Chaves_GrupoEtiquetas, String>("nome"));
		bancoNumeroDeEtiquetas
				.setCellValueFactory(new PropertyValueFactory<Chaves_GrupoEtiquetas, String>("qntEtiquetas"));
		ObservableList<Chaves_GrupoEtiquetas> genericos = FXCollections.observableArrayList(chaves);
		tabelaBancos.setItems(genericos);
	}

	/* Global */
	@FXML
	public void retornaMenu(ActionEvent event) {
		Node node = (Node) event.getSource();
		Stage stage = (Stage) node.getScene().getWindow();
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
		} catch (IOException erro) {
			erro.getMessage();
		}
		Scene scene = new Scene(root);
		stage.setMinWidth(900);
		stage.setMinHeight(500);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
		stage.setTitle("Sistema de Triagem Mark");
	}

	/* Identificador de Matéria */

	@FXML
	public void inserirIdentificadorMateria() {
		Boolean pedidoNaoEstaVazio = !pedido.getText().trim().isEmpty() || pedido != null;
		Boolean subnucleoFoiSelecionado = !comboBoxNucleo.getSelectionModel().isEmpty();
		Integer pesoSelecionado;

		if (identificadorMateriaP1.isSelected()) {
			pesoSelecionado = 1;
		} else if (identificadorMateriaP2.isSelected()) {
			pesoSelecionado = 2;
		} else if (identificadorMateriaP3.isSelected()) {
			pesoSelecionado = 3;
		} else {
			pesoSelecionado = 4;
		}

		if (pedidoNaoEstaVazio && subnucleoFoiSelecionado) {
			new IdentificadorMateriaDAO().inserirIdentificadorMateria(pedido.getText(), complementoPedido.getText(),
					comboBoxNucleo.getSelectionModel().getSelectedItem(), pesoSelecionado);
			inicializarMenuPeticaoInicial();
		} else {
			new Aviso().aviso("Reviso os campos preenchidos");
		}

		inicializarMenuTriagemPadrao();

	}

	@FXML
	public void alterarIdentificadorMateria() throws IOException {
		Chaves_Banco chave = new Chaves_Banco();
		chave.setID(tabelaIdentificadorMateria.getSelectionModel().getSelectedItem().getID());
		chave.setPALAVRACHAVE(tabelaIdentificadorMateria.getSelectionModel().getSelectedItem().getPALAVRACHAVE());
		chave.setCOMPLEMENTO(tabelaIdentificadorMateria.getSelectionModel().getSelectedItem().getCOMPLEMENTO());
		chave.setETIQUETA(tabelaIdentificadorMateria.getSelectionModel().getSelectedItem().getETIQUETA());
		chave.setTIPO(tabelaIdentificadorMateria.getSelectionModel().getSelectedItem().getTIPO());
		chave.setPRIORIDADE(tabelaIdentificadorMateria.getSelectionModel().getSelectedItem().getPRIORIDADE());
		chave.setBANCO("PET");

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TagEdicaoMateria.fxml"));
		loader.setController(new Controller_TagEdicaoMateria(chave));
		Parent root = loader.load();
		Stage stage = new Stage();
		stage.setTitle("Editar Etiqueta");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setScene(new Scene(root));
		stage.show();

	}

	@FXML
	public void excluirIdentificadorMateria() {
		Integer idSelecionado = tabelaIdentificadorMateria.getSelectionModel().getSelectedItem().getID();
		Boolean identificadorExcluido = new IdentificadorMateriaDAO().removerIdentificadorMateria(idSelecionado);
		if (identificadorExcluido) {
			new Aviso().aviso("Item excluído");
			inicializarMenuPeticaoInicial();
		} else {
			new Aviso().aviso("Não foi possível excluir o item");
		}
	}

	public void limparIdentificadorMateria() {
		pedido.clear();
		complementoPedido.clear();
		comboBoxNucleo.getSelectionModel().clearSelection();
	}

	public void selecionarIdentificadorMateria() {
		pedido.setText(tabelaIdentificadorMateria.getSelectionModel().getSelectedItem().getPALAVRACHAVE());
		complementoPedido.setText(tabelaIdentificadorMateria.getSelectionModel().getSelectedItem().getCOMPLEMENTO());
		comboBoxNucleo.getSelectionModel()
				.select(tabelaIdentificadorMateria.getSelectionModel().getSelectedItem().getETIQUETA());
	}

	public void buscaIDIdentificadorMateria() {
		String id = buscaIdentificadorMateriaID.getText();
		if (id.isEmpty()) {
			inicializarMenuPeticaoInicial();
		} else {
			List<Chaves_Banco> listaIdentificadoresMateria = new IdentificadorMateriaDAO().buscaIdentificadorPorID(id);
			identificadorMateriaID.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("ID"));
			identificadorMateriaPedido
					.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("PALAVRACHAVE"));
			identificadorMateriaComplemento
					.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("COMPLEMENTO"));
			identificadorMateriaSubnucleo
					.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("ETIQUETA"));
			identificadorMateriaPeso.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("PRIORIDADE"));
			ObservableList<Chaves_Banco> identificadoresDeMateria = FXCollections
					.observableArrayList(listaIdentificadoresMateria);
			tabelaIdentificadorMateria.setItems(identificadoresDeMateria);
			numeroIdentificadorMateria.setText(String.valueOf(tabelaIdentificadorMateria.getItems().size()));
		}
	}

	public void buscaIdentificadorMateria() {
		String textoBusca = buscaIdentificadorMateria.getText().toUpperCase();
		if (textoBusca.isEmpty()) {
			inicializarMenuPeticaoInicial();
		} else {
			List<Chaves_Banco> listaIdentificadoresMateria = new IdentificadorMateriaDAO()
					.buscaIdentificador(textoBusca);
			identificadorMateriaID.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("ID"));
			identificadorMateriaPedido
					.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("PALAVRACHAVE"));
			identificadorMateriaComplemento
					.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("COMPLEMENTO"));
			identificadorMateriaSubnucleo
					.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("ETIQUETA"));
			identificadorMateriaPeso.setCellValueFactory(new PropertyValueFactory<Chaves_Banco, String>("PRIORIDADE"));
			ObservableList<Chaves_Banco> identificadoresDeMateria = FXCollections
					.observableArrayList(listaIdentificadoresMateria);
			tabelaIdentificadorMateria.setItems(identificadoresDeMateria);
			numeroIdentificadorMateria.setText(String.valueOf(tabelaIdentificadorMateria.getItems().size()));
		}
	}

	/* Identificador de Petição Inicial */

	public void inserirIdentificadorPeticao() {
		String identificadorPeticao = identificadorPeticaoInicial.getText().toUpperCase();
		if (identificadorPeticao.isEmpty()) {
			new Aviso().aviso("Revise os campos");
		} else {
			new IdentificadorPeticaoInicialDAO().inserirIdentificadorPeticaoInicial(identificadorPeticao);
		}
		inicializarMenuPeticaoInicial();

	}

	public void excluirIdentificadorPeticao() {
		String texto = tabelaIdentificadorPeticao.getSelectionModel().getSelectedItem().getTEXTO();
		new IdentificadorPeticaoInicialDAO().removerIdentificadorPeticaoInicial(texto);
		inicializarMenuPeticaoInicial();
	}

	@FXML
	public void buscaIdentificadorPeticao() {
		String textoBusca = pesquisaIdentificador.getText().toUpperCase();
		System.out.println(textoBusca);
		if (textoBusca.isEmpty()) {
			inicializarMenuPeticaoInicial();
		} else {
			List<Chaves_Condicao> itensIdentificadoresPeticao = new IdentificadorPeticaoInicialDAO()
					.buscarIdentificadorPeticao(textoBusca);
			colunaIdentificadorPeticaoInicial
					.setCellValueFactory(new PropertyValueFactory<Chaves_Condicao, String>("TEXTO"));
			ObservableList<Chaves_Condicao> identPeticao = FXCollections
					.observableArrayList(itensIdentificadoresPeticao);
			tabelaIdentificadorPeticao.setItems(identPeticao);
			numeroPeticaoInicial.setText(String.valueOf(tabelaIdentificadorPeticao.getItems().size()));
		}
	}

	public void alterarIdentificadorPeticao() throws IOException {
		Chaves_Condicao chave = new Chaves_Condicao();
		chave.setTEXTO(tabelaIdentificadorPeticao.getSelectionModel().getSelectedItem().getTEXTO());
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TagEdicaoConfiguracao.fxml"));
		loader.setController(new Controller_TagEdicaoCondicao(chave));
		Parent root = loader.load();
		Stage stage = new Stage();

		stage.setTitle("Editar Condição");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setScene(new Scene(root));
		stage.show();
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
