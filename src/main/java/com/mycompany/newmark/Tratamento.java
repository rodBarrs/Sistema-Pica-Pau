/**
 * @author Felipe Marques, Gabriel Ramos, Rafael Henrique e Adriano Vilhena 
 * 
 */
package com.mycompany.newmark;

public class Tratamento {

    public String tratamento(String processo) {
        processo = processo.toUpperCase();
        //processo = Normalizer.normalize(processo, Normalizer.Form.NFD);
        //processo = processo.replaceAll("[^\\p{ASCII}]", "");
        //processo = processo.replaceAll(",", "");
        //processo = processo.replaceAll(":", "");
        //processo = processo.replaceAll("-", "");
        //processo = processo.replaceAll(".", "");
        //processo = processo.replaceAll("\"", "");
        //processo = processo.replaceAll("'", "");
        processo = processo.replaceAll(" ", "");
        //processo = processo.replaceAll("\n", "");
        //processo = processo.replaceAll("\r", "");
        return processo;
    }
}
