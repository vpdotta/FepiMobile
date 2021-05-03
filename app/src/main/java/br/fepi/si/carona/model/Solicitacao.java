package br.fepi.si.carona.model;

import java.security.Timestamp;
import java.util.ArrayList;

public class Solicitacao {

    private int idsolicitacao, matpas, matmot, idstatus;
    private String placa;
    private double paslat, paslong, motlat, motlong;
    private Timestamp pastmp, mottmp;

    public Solicitacao(int idsolicitacao) {
        this.idsolicitacao = idsolicitacao;
    }

    public int getIdsolicitacao() {
        return idsolicitacao;
    }

    public void setIdsolicitacao(int idsolicitacao) {
        this.idsolicitacao = idsolicitacao;
    }

    public int getMatpas() {
        return matpas;
    }

    public void setMatpas(int matpas) {
        this.matpas = matpas;
    }

    public int getMatmot() {
        return matmot;
    }

    public void setMatmot(int matmot) {
        this.matmot = matmot;
    }

    public int getIdstatus() {
        return idstatus;
    }

    public void setIdstatus(int idstatus) {
        this.idstatus = idstatus;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public double getPaslat() {
        return paslat;
    }

    public void setPaslat(double paslat) {
        this.paslat = paslat;
    }

    public double getPaslong() {
        return paslong;
    }

    public void setPaslong(double paslong) {
        this.paslong = paslong;
    }

    public double getMotlat() {
        return motlat;
    }

    public void setMotlat(double motlat) {
        this.motlat = motlat;
    }

    public double getMotlong() {
        return motlong;
    }

    public void setMotlong(double motlong) {
        this.motlong = motlong;
    }

    public Timestamp getPastmp() {
        return pastmp;
    }

    public void setPastmp(Timestamp pastmp) {
        this.pastmp = pastmp;
    }

    public Timestamp getMottmp() {
        return mottmp;
    }

    public void setMottmp(Timestamp mottmp) {
        this.mottmp = mottmp;
    }

    //método para concatenar todas as informações presentes na lista de pessoas
    public String[] getListaSolicitacao(ArrayList<Solicitacao> listaSolicitacao){

        int i = 0;
        String dados[] = new String[listaSolicitacao.size()];//string que será exibida na lista

        //percorre a lista de pessoas e preenche cada posição do vetor 'dados' com uma pessoa
        for(Solicitacao aux: listaSolicitacao){
            dados[i] =
                    "\n\t\t\t\t\t\tNº da solicitação: " + aux.getIdsolicitacao();
                            /*"\nEmail....: " + aux.getEmail() +
                            "\nSexo....: " + aux.getSexo() +
                            "\nNascimento....: " + aux.getNascimento() +
                            "\nPeríodo....: " + aux.getPeriodo() +
                            "\nStatus...: " + aux.getStatus() +
                            "\nSituação..: " + aux.getSituacao();*/
            i++;
        }
        return dados;
    }
}
