/**
 * @author Felipe Marques, João Paulo, Gabriel Ramos, Rafael Henrique
 * 
 * Responsável por Verificar as condições do processo (Cabeçalho e Providência Juridica)
 */

package com.mycompany.newmark;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class Triagem_Condicao {
    
    public boolean verificaCondicao(String processo, String tipo) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:BancoPicaPau.db");
        PreparedStatement stmt;
        ResultSet resultSet;
        stmt = connection.prepareStatement("SELECT * FROM condicao WHERE tipo = '" + tipo + "'");
        resultSet = stmt.executeQuery();
        while (resultSet.next()){
            String texto = resultSet.getString("texto");
           
            if (processo.contains(texto)) {
                connection.close();
                return true;
            }
        }
        connection.close();
        return false;
    }
    
}