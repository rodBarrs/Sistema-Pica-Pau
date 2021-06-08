package com.mycompany.newmark.controllers;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.mycompany.newmark.Aviso;
import com.mycompany.newmark.Controller_Login;
import com.mycompany.newmark.DAO.UsuarioLocalDAO;
import com.mycompany.newmark.entities.UsuarioLocal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.shape.SVGPath;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginLocal{

	@FXML
	private SVGPath iconeOlho;
	@FXML
	private JFXTextField labelUsuario;
	@FXML
	private JFXPasswordField passwordUsuario;
	@FXML
	private JFXButton btnLogar;

	
	public void abrirPopupLogin() throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginLocal.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Login");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(new Image("/fxml/Imagens/iconeMark.png"));
        stage.setResizable(false);
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root));
        stage.show();
	}
	
	@FXML
	private void logarUsuario(ActionEvent event) throws IOException {
		String usuario = labelUsuario.getText();
		String senha = passwordUsuario.getText();
		Boolean credencialCorreta = new UsuarioLocalDAO().logarUsuario(usuario, senha);
		Stage stage = (Stage) btnLogar.getScene().getWindow();
        stage.close();
        if(credencialCorreta) {
        	UsuarioLocal.setEstaLogado(true);
        	new Controller_Login().abrirJanelaAdministracao();
        } else {
        	new Aviso().aviso("Credenciais inválidas");
        }
	}
	
	@FXML
	private void toggleSenha() {
		iconeOlho.setContent("M288 144a110.94 110.94 0 0 0-31.24 5 55.4 55.4 0 0 1 7.24 27 56 56 0 0 1-56 56 55.4 55.4 0 0 1-27-7.24A111.71 111.71 0 1 0 288 144zm284.52 97.4C518.29 135.59 410.93 64 288 64S57.68 135.64 3.48 241.41a32.35 32.35 0 0 0 0 29.19C57.71 376.41 165.07 448 288 448s230.32-71.64 284.52-177.41a32.35 32.35 0 0 0 0-29.19zM288 400c-98.65 0-189.09-55-237.93-144C98.91 167 189.34 112 288 112s189.09 55 237.93 144C477.1 345 386.66 400 288 400z");
	}

	

}
