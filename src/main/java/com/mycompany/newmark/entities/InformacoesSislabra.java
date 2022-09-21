package com.mycompany.newmark.entities;

import java.util.ArrayList;
import java.util.List;

public class InformacoesSislabra {

    List<InfVeiculo> infVeiculo;
    List<String> situacaoEmpresa = new ArrayList<>();
    List<InfImovel> informacoesImoveis;

    public List<String> getSituacaoEmpresa() {
        return situacaoEmpresa;
    }

    public void setSituacaoEmpresa(List<String> situacaoEmpresa) {
        this.situacaoEmpresa = situacaoEmpresa;
    }

    public List<InfVeiculo> getInfVeiculo() {
        return infVeiculo;
    }

    public void setInfVeiculo(List<InfVeiculo> infVeiculo) {
        this.infVeiculo = infVeiculo;
    }

    public static class InfVeiculo{
        String modelo;
        String tipo;

        public InfVeiculo(String modelo, String tipo) {
            this.modelo = modelo;
            this.tipo = tipo;
        }

        public String getModelo() {
            return modelo;
        }

        public String getTipo() {
            return tipo;
        }

    }

    public List<InfImovel> getInformacoesImoveis() {
        return informacoesImoveis;
    }

    public void setInformacoesImoveis(List<InfImovel> informacoesImoveis) {
        this.informacoesImoveis = informacoesImoveis;
    }
}

