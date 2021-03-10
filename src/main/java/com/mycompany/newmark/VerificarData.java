package com.mycompany.newmark;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class VerificarData
{

    public String ConverteData(int mes)
    {
        String mesConvert = null;
        switch (mes)
        {
            case (1):
                mesConvert = "JANEIRO";
                break;
            case (2):
                mesConvert = "FEVEREIRO";
                break;
            case (3):
                mesConvert = "MARÇO";
                break;
            case (4):
                mesConvert = "ABRIL";
                break;
            case (5):
                mesConvert = "MAIO";
                break;
            case (6):
                mesConvert = "JUNHO";
                break;
            case (7):
                mesConvert = "JULHO";
                break;
            case (8):
                mesConvert = "AGOSTO";
                break;
            case (9):
                mesConvert = "SETEMBRO";
                break;
            case (10):
                mesConvert = "OUTUBRO";
                break;
            case (11):
                mesConvert = "NOVEMBRO";
                break;
            case (12):
                mesConvert = "DEZEMBRO";
                break;
        }

        return mesConvert;
    }

    public boolean mesatualExtenso(String Processo)
    {
        Calendar calendar = new GregorianCalendar();
        int mes = calendar.get(Calendar.MONTH);
        int ano = calendar.get(Calendar.YEAR);
        String mesatuala = ConverteData(mes) + " DE " + (ano - 2000);
        String mesatual = ConverteData(mes + 1) + " DE " + ano;
        if (Processo.contains(mesatual) || Processo.contains(mesatuala))
        {
            return true;
        }
        return false;
    }

    public boolean mesAnteriorExtenso(String Processo)
    {
        Calendar calendar = new GregorianCalendar();
        int mes = calendar.get(Calendar.MONTH);
        int ano = calendar.get(Calendar.YEAR);
        String mesatual = ConverteData(mes) + " DE " + ano;
        String mesatuala = ConverteData(mes) + " DE " + (ano - 2000);
        if (Processo.contains(mesatual) || Processo.contains(mesatuala))
        {
            return true;
        }
        return false;
    }

    public boolean mesatual(String Processo)
    {
        Calendar calendar = new GregorianCalendar();
        int mes = calendar.get(Calendar.MONTH);
        int ano = calendar.get(Calendar.YEAR);
        String mesatual = mes + 1 + "-" + ano;
        String mesAtual = mes + 1 + "/" + ano;
        String mesatuala = mes + 1 + "-" + (ano - 2000);
        String mesAtuala = mes + 1 + "/" + (ano - 2000);
        if (Processo.contains(mesatual) || Processo.contains(mesatuala))
        {
            return true;
        }
        else if (Processo.contains(mesAtual) || Processo.contains(mesAtuala))
        {
            return true;
        }
        return false;
    }

    public boolean mesanterior(String Processo)
    {
        int dia;
        Calendar calendar = new GregorianCalendar();
        int mes = calendar.get(Calendar.MONTH);
        int ano = calendar.get(Calendar.YEAR);
        if (mes == 0)
        {
            dia = 1;
        }
        else
        {
            dia = 1;
        }

        while (dia <= 31)
        {
            String mesanterior;
            String mesAnterior;
            String mesanteriora;
            String mesAnteriora;
            if (mes < 10)
            {

                mesanterior = dia + "-" + 0+"" + mes + "-" + ano;
                mesAnterior = dia + "/" + 0+"" + mes + "/" + ano;
                mesanteriora = dia + "-" + 0+"" + mes + "-" + (ano - 2000);
                mesAnteriora = dia + "/" +0+""+ mes + "/" + (ano - 2000);
            }
            else
            {

                mesanterior = dia + "-" + mes + "-" + ano;
                mesAnterior = dia + "/" + mes + "/" + ano;
                mesanteriora = dia + "-" + mes + "-" + (ano - 2000);
                mesAnteriora = dia + "/" + mes + "/" + (ano - 2000);
            }
            // condição prevendo recesso judiciario onde processos ficam parados
            if (mes == 0)
            {
                ano = ano - 1;
                mes = mes + 12;
                mesanterior = dia + "-" + mes + "-" + ano;
                mesAnterior = dia + "/" + mes + "/" + ano;
                mesanteriora = dia + "-" + mes + "-" + (ano - 2000);
                mesAnteriora = dia + "/" + mes + "/" + (ano - 2000);
                if (Processo.contains(mesanterior) || Processo.contains(mesanteriora))
                {
                    return true;
                }
                else if (Processo.contains(mesAnterior) || Processo.contains(mesAnteriora))
                {
                    return true;
                }
            }
            else if (Processo.contains(mesanterior) || Processo.contains(mesanteriora))
            {
                return true;
            }
            else if (Processo.contains(mesAnterior) || Processo.contains(mesAnteriora))
            {
                return true;
            }
            dia++;
        }
        return false;
    }

    public boolean Verificar(String Processo)
    {
        boolean atual;
        boolean anterior;
        boolean atualExtenso;
        boolean anteriorExtenso;
        atualExtenso = mesatualExtenso(Processo);
        anteriorExtenso = mesAnteriorExtenso(Processo);
        atual = mesatual(Processo);
        anterior = mesanterior(Processo);
        if (atual)
        {
            return atual;
        }
        else if (anterior)
        {
            return anterior;
        }
        else if (atualExtenso)
        {
            return atualExtenso;
        }
        else if (anteriorExtenso)
        {
            return anteriorExtenso;
        }
        else
        {
            return false;
        }

    }
}
