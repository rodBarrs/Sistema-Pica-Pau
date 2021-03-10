/**
 * @author Felipe Marques, João Paulo, Gabriel Ramos, Rafael Henrique
 * 
 * Responsável por controlar a Janela de configuração de triagem do Mark
 */
package com.mycompany.newmark;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Controller_Configuracao implements Initializable {
	
    private Chaves_Condicao chave = new Chaves_Condicao();
  
    public Controller_Configuracao(){
        this.chave = chave;
    }
    
    @FXML
    TableView<Chaves_Condicao> tabelaIdentificador;
    @FXML
    TableColumn<Chaves_Condicao, String> colunaIdentificador;
    @FXML
    TableView<Chaves_Condicao> tabelaCabecalho;
    @FXML
    TableColumn<Chaves_Condicao, String> colunaCabecalho;
    @FXML
    TableView<Chaves_Condicao> tabelaProvidencia;
    @FXML
    TableColumn<Chaves_Condicao, String> colunaProvidencia;
    @FXML
    JFXTextField textoCabecalho, textoProv, textoPet,
            contTotal, contNao, contDoc, contSeq,
            pesquisaCabecalho, pesquisaProvidencia, pesquisaIdentificador;
    @FXML
    JFXButton limparCabecalho, limparProv,
            inserirCabecalho, excluirCabecalho, voltarCabecalho,
            inserirProv, excluirProv, voltarProv,
            atualizarCabecalho, atualizarProvidencia,
            salvarAvancada, voltarAvancada,
            salvarEspecifica, voltarEspecifica,
            voltarContador,
            botaoBuscaCabecalho, botaoBuscaProvidencia, buscaIdentificador;
    @FXML
    RadioButton verificaData, triarAntigo,
            tipoCOM, tipoDOC, tipoMOV,
            html, pdf, 
            desativadoPericial, ativadoPericial,
            desativadoPeticaoInicial, ativadoPeticaoInicial;
    
    final ToggleGroup grupoTriarAntigo = new ToggleGroup();
    final ToggleGroup grupoTipo = new ToggleGroup();
    final ToggleGroup grupoJuntada = new ToggleGroup();
    final ToggleGroup grupoPericial = new ToggleGroup();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        atualizar();
    }
    
    public void atualizar(){
        //Inicialização da tabela de cabeçalho
        colunaCabecalho.setCellValueFactory(new PropertyValueFactory<Chaves_Condicao, String>("TEXTO"));
        ObservableList<Chaves_Condicao> cabecalho = FXCollections.observableArrayList(Cabecalho());
        tabelaCabecalho.setItems(cabecalho);
        //Inicialização da tabela de Providência Juridica
        colunaProvidencia.setCellValueFactory(new PropertyValueFactory<Chaves_Condicao, String>("TEXTO"));
        ObservableList<Chaves_Condicao> providencia = FXCollections.observableArrayList(ProvJuri());
        tabelaProvidencia.setItems(providencia);
        //Inicialização da tabela de Identificador de Petição Inicial
        colunaIdentificador.setCellValueFactory(new PropertyValueFactory<Chaves_Condicao, String>("TEXTO"));
        ObservableList<Chaves_Condicao> identPeticao = FXCollections.observableArrayList(identificadorPeticao());
        tabelaIdentificador.setItems(identPeticao);
        
        
        Chaves_Configuracao configuracao = new Chaves_Configuracao();
        Banco banco = new Banco();
        configuracao = banco.pegarConfiguracao(configuracao);
        if(configuracao.isTriarAntigo()){
            triarAntigo.setSelected(true);
        } else {
            verificaData.setSelected(true);
        }
        if(configuracao.isJuntManual()){
            pdf.setSelected(true);
        } else {
            html.setSelected(true);
        }
        switch(configuracao.getTipoTriagem()){
            case "COM":
                tipoCOM.setSelected(true);
            break;
            case "DOC":
                tipoDOC.setSelected(true);
            break;
            case "MOV":
                tipoMOV.setSelected(true);
            break;
        }
        if(configuracao.isLaudoPericial()){
            ativadoPericial.setSelected(true);
        } else {
            desativadoPericial.setSelected(true);
        }
        if(configuracao.isPeticaoInicial()){
            ativadoPeticaoInicial.setSelected(true);
        } else {
            desativadoPeticaoInicial.setSelected(true);
        }
        contadores();
    }
    
    public List<Chaves_Condicao> identificadorPeticao(){
    	List<Chaves_Condicao> identPet = new ArrayList<>();
    	try {
    		Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
    		PreparedStatement stmt = connection.prepareStatement("SELECT * FROM CONDICAO WHERE TIPO = 'PET' ORDER BY TEXTO");
    		ResultSet resultSet = stmt.executeQuery();
    		while(resultSet.next()) {
    			Chaves_Condicao key = new Chaves_Condicao();
    			key.setTIPO("PET");
    			key.setTEXTO(resultSet.getString("TEXTO"));
    			identPet.add(key);
    		}
    		connection.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	} 
    	return identPet;
    }
    
    @FXML
    public void inserirPet() {
    	Chaves_Condicao elemento = new Chaves_Condicao();
    	String texto = textoPet.getText();
    	Aviso aviso = new Aviso();
    	if ((texto.equals(null)) || texto.equals("") || texto.equals(" ")) {
    		String textoAviso = "O campo \"Identificador de Petição Inicial\" não pode ser vazio!";
            aviso.aviso(textoAviso);
    	} else {
    		elemento.setTIPO("PET");
    		elemento.setTEXTO(texto);
    		inserirCondicao(elemento.getTIPO(), elemento.getTEXTO());
    		colunaIdentificador.setCellValueFactory(new PropertyValueFactory<Chaves_Condicao, String>("TEXTO"));
    		ObservableList<Chaves_Condicao> coluna = FXCollections.observableArrayList(identificadorPeticao());
            tabelaIdentificador.setItems(coluna);
            limpar();
    	}
    }
    
    @FXML
    public void excluirPet() {
    	if(textoPet.getText().equals("")) {
    		// 
    	} else {
    		Chaves_Condicao chave = new Chaves_Condicao();
    		chave.setTEXTO(tabelaIdentificador.getSelectionModel().getSelectedItem().getTEXTO());
    		chave.setTIPO("PET");
    		Aviso aviso = new Aviso();
    		aviso.confirmacaoCondicao(chave);
            textoPet.clear();
    	}
    }
    
    public List<Chaves_Condicao> ProvJuri() {
        List<Chaves_Condicao> prov = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM CONDICAO WHERE TIPO = 'PRO' ORDER BY TEXTO");
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                Chaves_Condicao key = new Chaves_Condicao();
                key.setTIPO("PRO");
                key.setTEXTO(resultSet.getString("TEXTO"));
                prov.add(key);
            }
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(Controller_Configuracao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return prov;
    }
 
    @FXML
    public void inserirProv() {
        Chaves_Condicao elemento = new Chaves_Condicao();
        String texto = textoProv.getText();
        Aviso aviso = new Aviso();
        if ((texto.equals(null)) || texto.equals("") || texto.equals(" ")) {
            String textoAviso = "O campo \"Providência Jurídica\" não pode ser vazio!";
            aviso.aviso(textoAviso);
        } else {
            elemento.setTIPO("PRO");
            elemento.setTEXTO(texto);
            inserirCondicao(elemento.getTIPO(), elemento.getTEXTO());
            colunaProvidencia.setCellValueFactory(new PropertyValueFactory<Chaves_Condicao, String>("TEXTO"));
            ObservableList<Chaves_Condicao> coluna = FXCollections.observableArrayList(ProvJuri());
            tabelaProvidencia.setItems(coluna);
            limpar();
        }
    }
    
    @FXML
    public void excluirProvJuri(ActionEvent event){
        if(textoProv.getText().equals("")){
            //Não faz nada
        } else {
            Chaves_Condicao chave = new Chaves_Condicao();
            chave.setTEXTO(tabelaProvidencia.getSelectionModel().getSelectedItem().getTEXTO());
            chave.setTIPO("PRO");
            Aviso aviso = new Aviso();
            aviso.confirmacaoCondicao(chave);
            textoProv.clear();
        }
    }
    
    public List<Chaves_Condicao> Cabecalho() {
        List<Chaves_Condicao> cabecalho = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM CONDICAO WHERE TIPO = 'CAB' ORDER BY TEXTO");
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                Chaves_Condicao key = new Chaves_Condicao();
                key.setTIPO("TIPO");
                key.setTEXTO(resultSet.getString("TEXTO"));
                cabecalho.add(key);
            }
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(Controller_Configuracao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cabecalho;
    }
    
    @FXML
    public void inserirCabecalho() {
        Chaves_Condicao chave = new Chaves_Condicao();
        String texto = textoCabecalho.getText().toUpperCase();
        Aviso aviso = new Aviso();
        if ((texto.equals(null)) || texto.equals("") || texto.equals(" ")) {
            String textoAviso = "O campo \"Cabeçalho do documento\" não pode ser vazio!";
            aviso.aviso(textoAviso);
        } else {
            chave.setTIPO("CAB");
            chave.setTEXTO(texto);
            inserirCondicao(chave.getTIPO(), chave.getTEXTO());
            colunaCabecalho.setCellValueFactory(new PropertyValueFactory<Chaves_Condicao, String>("TEXTO"));
            ObservableList<Chaves_Condicao> genericos = FXCollections.observableArrayList(Cabecalho());
            tabelaCabecalho.setItems(genericos);
            limpar();
        }
    }

    @FXML
    public void excluirCabecalho() {
        if(textoCabecalho.getText().equals("")){
            //Não faz nada
        } else {
            Chaves_Condicao chave = new Chaves_Condicao();
            chave.setTEXTO(tabelaCabecalho.getSelectionModel().getSelectedItem().getTEXTO());
            chave.setTIPO("CAB");
            Aviso aviso = new Aviso();
            aviso.confirmacaoCondicao(chave);
            textoCabecalho.clear();
        }
    }
    
    public void alterarIdentificador() throws IOException, SQLException {
    	String tipo = "PET";
    	alterarCondicao(tipo);
    }
    
    public void alterarCabecalho() throws IOException, SQLException{
        String tipo = "CAB";
        alterarCondicao(tipo);
    }
    
    public void alterarProvidencia() throws IOException, SQLException{
        String tipo = "PRO";
        alterarCondicao(tipo);
    }
    
    public void alterarCondicao(String tipo) throws IOException, SQLException {
        try {
            Chaves_Condicao chave = new Chaves_Condicao();
            chave.setTIPO(tipo);
            switch (tipo){
                case "CAB":
                    chave.setTEXTO(tabelaCabecalho.getSelectionModel().getSelectedItem().getTEXTO().replace("'", "").replace("´", ""));
                break;
                case "PRO":
                    chave.setTEXTO(tabelaProvidencia.getSelectionModel().getSelectedItem().getTEXTO().replace("'", "").replace("´", ""));
                break;
                case "PET":
                	chave.setTEXTO(tabelaIdentificador.getSelectionModel().getSelectedItem().getTEXTO().replace("'", "").replace("´", ""));
                break;
            }
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TagEdicaoConfiguracao.fxml"));
            loader.setController(new Controller_TagEdicaoCondicao(chave));
            Parent root = loader.load();
            Stage stage = new Stage();
            
            stage.setTitle("Editar Condição");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception erro) {
            erro.printStackTrace();
        }
    }
    
    public void salvarAvancada(ActionEvent event) {
        boolean antigoTria, juntadaTria;
        String tipoTria;
        Banco banco = new Banco();
        
        if(verificaData.isSelected()){
            //Irá verificar a data, considerará apenas os ultimos 30 dias
            antigoTria = false;
        } else {
            //NÃO irá verififcar data e considerará as ulitmas 10 movimentações
            antigoTria = true;
        }
        
        if(tipoCOM.isSelected()){
            //Realizará a Triagem COMPLETA
            tipoTria = "COM";
        } else if(tipoDOC.isSelected()){
            //Realizará a Triagem APENAS nos Documentos
            tipoTria = "DOC";
        } else {
            //Realizará a Triagem APENAS na Movimentação
            tipoTria = "MOV";
        }
        
        if(html.isSelected()) {
            // Irá ler somente movimentações presente no HTML
            juntadaTria = false; 
        }else {
            //Irá ler movimentações que possuem PDF e HTML
            juntadaTria = true;
        }
        
        banco.salvarAvancadas(antigoTria, tipoTria, juntadaTria);
        Aviso aviso = new Aviso();
        String textoAviso = "Configuração salva com sucesso!";
        aviso.aviso(textoAviso);
    }
    
    public void salvarEspecificas(ActionEvent event) {
        boolean pericial;
        boolean peticao;
        Banco banco = new Banco();
        
        if(ativadoPericial.isSelected()){
            // Irá realiziar a busca pelo laudo pericial na movimentação
            pericial = true;
        } else {
            // Irá ignorar o laudo pericial na movimentação 
            pericial = false;
        }
        
        if(ativadoPeticaoInicial.isSelected()){
            peticao = true;
        } else {
            peticao = false;
        }
        
        Aviso aviso = new Aviso();
        String textoAviso = "";
        if(ativadoPericial.isSelected() && ativadoPeticaoInicial.isSelected()){
            textoAviso = "Não é possivel realizar dois modos de pesquisa específicos simultaneamente, ative apenas uma opção!";
            aviso.aviso(textoAviso);
        } else {
            banco.salvarEspecificas(pericial,peticao);
            textoAviso = "Configuração salva com sucesso!";
            aviso.aviso(textoAviso);
        }
    }
    
    public void contadores(){
        try{
            Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM CONTADOR");
            ResultSet resultSet = stmt.executeQuery();
            contTotal.setText(resultSet.getString("ContTotal"));
            contNao.setText(resultSet.getString("ContNao"));
            contDoc.setText(resultSet.getString("ContDoc"));
            contSeq.setText(resultSet.getString("ContSeq"));
            connection.close();
        } catch (SQLException erro){
            Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, erro);
            Aviso aviso = new Aviso();
            aviso.aviso(erro.getMessage() + "\nCódigo do Erro: " + erro.getErrorCode());
        }
    }
    
    @FXML
    public void limpar() {
        textoCabecalho.clear();
        textoProv.clear();
        textoPet.clear();
    }
       
    @FXML
    void RetornaMenu(ActionEvent event) {
        Node node = (Node) event.getSource();

        Stage stage = (Stage) node.getScene().getWindow();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        } catch (IOException erro) {
        }
        Scene scene = new Scene(root);
        stage.setMinWidth(900);
        stage.setMinHeight(500);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
        stage.setTitle("New Mark");
    }
    
     private boolean inserirCondicao(String tipo, String texto) {
        Banco banco = new Banco();
        Chaves_Condicao chave = new Chaves_Condicao();
        chave.setTIPO(tipo);
        chave.setTEXTO(texto);
        banco.inserirCondicao(chave);
        return true;
    }
    
    @FXML
    public void selecionarIdentificador() {
    	textoPet.clear();
    	textoPet.setText(tabelaIdentificador.getSelectionModel().getSelectedItem().getTEXTO());
    }
     
    @FXML
    public void selecionarCabecalho() {
        textoCabecalho.clear();
        textoCabecalho.setText(tabelaCabecalho.getSelectionModel().getSelectedItem().getTEXTO());
    }
    
    @FXML
    public void selecionarProv() {
        textoProv.clear();
        textoProv.setText(tabelaProvidencia.getSelectionModel().getSelectedItem().getTEXTO());
    }
    
    @FXML
    public void buscaIdentificador() throws SQLException {
    	List<Chaves_Condicao> chaves = new ArrayList<>(); 
    	Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
    	PreparedStatement stmt = connection.prepareStatement("SELECT * FROM CONDICAO WHERE TEXTO like '%"
    			+ pesquisaIdentificador.getText().toUpperCase().trim().replace("'", "").replace("´", "") + "%' "
    			+ " AND TIPO = 'PET' ORDER BY TEXTO");
    	ResultSet resultSet = stmt.executeQuery();
    	while(resultSet.next()) {
    		Chaves_Condicao key = new Chaves_Condicao();
    		key.setTEXTO(resultSet.getString("TEXTO"));
    		key.setTIPO("PET");
    		chaves.add(key);
    	}
    	colunaIdentificador.setCellValueFactory(new PropertyValueFactory<Chaves_Condicao, String>("TEXTO"));
        ObservableList<Chaves_Condicao> cabecalho = FXCollections.observableArrayList(chaves);
        tabelaIdentificador.setItems(cabecalho);
        connection.close();
    }
    
    @FXML
    public void buscaCabecalho() throws SQLException {
        List<Chaves_Condicao> chaves = new ArrayList<>();
        Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM CONDICAO WHERE TEXTO like '%"
                + pesquisaCabecalho.getText().toUpperCase().trim().replace("'", "").replace("´", "") + "%' "
                + " AND TIPO = 'CAB' ORDER BY TEXTO");
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            Chaves_Condicao key = new Chaves_Condicao();
            key.setTIPO("CAB");
            key.setTEXTO(resultSet.getString("TEXTO"));
            chaves.add(key);
        }
        colunaCabecalho.setCellValueFactory(new PropertyValueFactory<Chaves_Condicao, String>("TEXTO"));
        ObservableList<Chaves_Condicao> cabecalho = FXCollections.observableArrayList(chaves);
        tabelaCabecalho.setItems(cabecalho);
        connection.close();
    }

    @FXML
    public void buscaProvidencia() throws SQLException {
        List<Chaves_Condicao> chaves = new ArrayList<>();
        Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoEtiquetasMark.db");
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM CONDICAO WHERE TEXTO like '%"
                + pesquisaProvidencia.getText().toUpperCase().trim().replace("'", "").replace("´", "") + "%' "
                + " AND TIPO = 'PRO' ORDER BY TEXTO");
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            Chaves_Condicao key = new Chaves_Condicao();
            key.setTIPO("PRO");
            key.setTEXTO(resultSet.getString("TEXTO"));
            chaves.add(key);
        }
        colunaProvidencia.setCellValueFactory(new PropertyValueFactory<Chaves_Condicao, String>("TEXTO"));
        ObservableList<Chaves_Condicao> providencia = FXCollections.observableArrayList(chaves);
        tabelaProvidencia.setItems(providencia);
        connection.close();
    }
    
}