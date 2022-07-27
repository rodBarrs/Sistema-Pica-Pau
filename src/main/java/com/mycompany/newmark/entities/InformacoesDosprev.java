package com.mycompany.newmark.entities;



public class InformacoesDosprev {

    private String dataDeAjuizamento;
    private String sexo;
    private String nbProcessoIndeferido;
    private String dataInicioIndeferido;
    private boolean existeProcessoINSS;

    public String getDataDeAjuizamento() {
        return dataDeAjuizamento;
    }

    public void setDataDeAjuizamento(String dataDeAjuizamento) {
        this.dataDeAjuizamento = dataDeAjuizamento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getNbProcessoIndeferido() {
        return nbProcessoIndeferido;
    }

    public void setNbProcessoIndeferido(String nbProcessoIndeferido) {
        this.nbProcessoIndeferido = nbProcessoIndeferido;
    }

    public String getDataInicioIndeferido() {
        return dataInicioIndeferido;
    }

    public void setDataInicioIndeferido(String dataInicioIndeferido) {
        this.dataInicioIndeferido = dataInicioIndeferido;
    }

    public boolean isExisteProcessoINSS() {
        return existeProcessoINSS;
    }

    public void setExisteProcessoINSS(boolean existeProcessoINSS) {
        this.existeProcessoINSS = existeProcessoINSS;
    }
}
