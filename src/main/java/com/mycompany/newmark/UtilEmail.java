package com.mycompany.newmark;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javafx.scene.control.Alert;

public class UtilEmail {
	String mensagem;
	
	public UtilEmail(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public void enviarEmail() {
		String host = "smtp.gmail.com";
		final String user = "markdev413@gmail.com";//change accordingly  
		final String password = "markaguemail";//change accordingly  

		String to = "joao-paulocosta@live.com";//change accordingly  

		//Get the session object  
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});

		//Compose the message  
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(user));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject("RELATÓRIO DE ERRO DO MARK");
			message.setText(mensagem);

			//send the message  
			Transport.send(message);

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
