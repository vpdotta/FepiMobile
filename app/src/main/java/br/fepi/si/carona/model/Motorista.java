package br.fepi.si.carona.model;

import java.util.ArrayList;

public class Motorista extends Pessoa{

    private int cnh, matricula;
    private String categoria, validade;
    private ArrayList<Solicitacao> solicitacoes;
    private Veiculo veiculo;

    public Motorista() {
        solicitacoes = new ArrayList<>();
    }

    public Motorista(int matricula) {
        this.setMatricula(matricula);
    }

    public int getCnh() {
        return cnh;
    }

    public void setCnh(int cnh) {
        this.cnh = cnh;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public ArrayList<Solicitacao> getSolicitacoes() {
        return solicitacoes;
    }

    public void setSolicitacoes(ArrayList<Solicitacao> solicitacoes) {
        this.solicitacoes = solicitacoes;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }
}
