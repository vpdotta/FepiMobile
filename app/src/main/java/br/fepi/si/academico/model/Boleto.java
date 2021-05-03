package br.fepi.si.academico.model;

import java.io.Serializable;

public class Boleto implements Serializable {
    private String competencia, codBarra, caminho;
    private Double valorOriginal, valorBaixado, juros,
            multa, desconto, bolsaCondicional, bolsa;
    private String vencimento, dataBaixa;
    private boolean baixado;

    public String getCompetencia() {
        return competencia;
    }

    public void setCompetencia(String competencia) {
        this.competencia = competencia;
    }

    public Double getValorOriginal() {
        return valorOriginal;
    }

    public void setValorOriginal(Double valorOriginal) {
        this.valorOriginal = valorOriginal;
    }

    public Double getJuros() {
        return juros;
    }

    public void setJuros(Double juros) {
        this.juros = juros;
    }

    public Double getMulta() {
        return multa;
    }

    public void setMulta(Double multa) {
        this.multa = multa;
    }

    public Double getDesconto() {
        return desconto;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }

    public Double getBolsaCondicional() {
        return bolsaCondicional;
    }

    public void setBolsaCondicional(Double bolsaCondicional) {
        this.bolsaCondicional = bolsaCondicional;
    }

    public Double getBolsa() {
        return bolsa;
    }

    public void setBolsa(Double bolsa) {
        this.bolsa = bolsa;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public String getDataBaixa() {
        return dataBaixa;
    }

    public void setDataBaixa(String dataBaixa) {
        this.dataBaixa = dataBaixa;
    }

    public boolean isBaixado() {
        return baixado;
    }

    public void setBaixado(boolean baixado) {
        this.baixado = baixado;
    }

    public Double getValorBaixado() {
        return valorBaixado;
    }

    public void setValorBaixado(Double valorBaixado) {
        this.valorBaixado = valorBaixado;
    }

    @Override
    public String toString() {
        return "Boleto [competencia=" + competencia + ", valorOriginal=" + valorOriginal + ", valorBaixado="
                + valorBaixado + ", juros=" + juros + ", multa=" + multa + ", desconto=" + desconto
                + ", bolsaCondicional=" + bolsaCondicional + ", bolsa=" + bolsa + ", vencimento=" + vencimento
                + ", dataBaixa=" + dataBaixa + ", baixado=" + baixado + "]";
    }

    public String getCodBarra() {
        return codBarra;
    }

    public void setCodBarra(String codBarra) {
        this.codBarra = codBarra;
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }


}
