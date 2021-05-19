/**
 * @author Felipe Marques, Gabriel Ramos, Rafael Henrique e Adriano Vilhena 
 * 
 */
package com.mycompany.newmark;

import org.openqa.selenium.WebDriver;

public class Chaves_Resultado {
	public static String nomePeticao;
    private String PalavraChave = "";
    private String Complemento = "";
    private String Etiqueta = "";
    private String Local = "";
    private String Petição = "";
    private WebDriver driver;
    private boolean grid;
    
    public static String getNomePeticao() {
		return nomePeticao;
	}

	public static void setNomePeticao(String nomePeticao) {
		Chaves_Resultado.nomePeticao = nomePeticao;
	}

	public String getPalavraChave() {
        return PalavraChave;
    }

    public void setPalavraChave(String PalavraChave) {
        this.PalavraChave = PalavraChave;
    }

    public String getComplemento() {
        return Complemento;
    }

    public void setComplemento(String Complemento) {
        this.Complemento = Complemento;
    }

    public String getEtiqueta() {
        return Etiqueta;
    }

    public void setEtiqueta(String Etiqueta) {
        this.Etiqueta = Etiqueta;
    }

    public String getLocal() {
        return Local;
    }

    public void setLocal(String Local) {
        this.Local = Local;
    } 

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isGrid() {
        return grid;
    }

    public void setGrid(boolean grid) {
        this.grid = grid;
    }

	public String getPetição() {
		return Petição;
	}

	public void setPetição(String localArquivo) {
		Petição = localArquivo;
	}
    
}