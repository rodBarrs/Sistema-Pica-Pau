package com.mycompany.newmark.entities;

public class InformacoesUrbano {
    String vinculo;
    String dataInicio;
    String dataFim;

    public InformacoesUrbano (String vinculo, String dataInicio, String dataFim) {
        this.vinculo = vinculo;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }
    public String getVinculo() {
        return vinculo;
    }

    public void setVinculo(String vinculo) {
        this.vinculo = vinculo;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }
}
