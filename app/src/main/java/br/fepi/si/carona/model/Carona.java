package br.fepi.si.carona.model;

import java.security.Timestamp;
import java.util.ArrayList;

public class Carona {

    private int idcarona, idsolicitacao, matpas, matmot, idstatus;
    private Timestamp pasTmpInicio, pasTmpFim, motTmpInicio, motTmpFim;
    private double pasLatInicio, pasLongInicio, pasLatFim, pasLongFim, motLatInicio, motLongInicio, motLatFim, motLongFim;

    public Carona(int idsolicitacao, int matpas, int matmot) {
        this.setIdsolicitacao(idsolicitacao);
        this.setMatpas(matpas);
        this.setMatmot(matmot);
    }

    public int getIdcarona() {
        return idcarona;
    }

    public void setIdcarona(int idcarona) {
        this.idcarona = idcarona;
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

    public Timestamp getPasTmpInicio() {
        return pasTmpInicio;
    }

    public void setPasTmpInicio(Timestamp pasTmpInicio) {
        this.pasTmpInicio = pasTmpInicio;
    }

    public Timestamp getPasTmpFim() {
        return pasTmpFim;
    }

    public void setPasTmpFim(Timestamp pasTmpFim) {
        this.pasTmpFim = pasTmpFim;
    }

    public Timestamp getMotTmpInicio() {
        return motTmpInicio;
    }

    public void setMotTmpInicio(Timestamp motTmpInicio) {
        this.motTmpInicio = motTmpInicio;
    }

    public Timestamp getMotTmpFim() {
        return motTmpFim;
    }

    public void setMotTmpFim(Timestamp motTmpFim) {
        this.motTmpFim = motTmpFim;
    }

    public double getPasLatInicio() {
        return pasLatInicio;
    }

    public void setPasLatInicio(double pasLatInicio) {
        this.pasLatInicio = pasLatInicio;
    }

    public double getPasLongInicio() {
        return pasLongInicio;
    }

    public void setPasLongInicio(double pasLongInicio) {
        this.pasLongInicio = pasLongInicio;
    }

    public double getPasLatFim() {
        return pasLatFim;
    }

    public void setPasLatFim(double pasLatFim) {
        this.pasLatFim = pasLatFim;
    }

    public double getPasLongFim() {
        return pasLongFim;
    }

    public void setPasLongFim(double pasLongFim) {
        this.pasLongFim = pasLongFim;
    }

    public double getMotLatInicio() {
        return motLatInicio;
    }

    public void setMotLatInicio(double motLatInicio) {
        this.motLatInicio = motLatInicio;
    }

    public double getMotLongInicio() {
        return motLongInicio;
    }

    public void setMotLongInicio(double motLongInicio) {
        this.motLongInicio = motLongInicio;
    }

    public double getMotLatFim() {
        return motLatFim;
    }

    public void setMotLatFim(double motLatFim) {
        this.motLatFim = motLatFim;
    }

    public double getMotLongFim() {
        return motLongFim;
    }

    public void setMotLongFim(double motLongFim) {
        this.motLongFim = motLongFim;
    }

    //método para concatenar todas as informações presentes na lista de pessoas
    public String[] getListaCarona(ArrayList<Carona> listaCarona){

        int i = 0;
        String dados[] = new String[listaCarona.size()];//string que será exibida na lista

        //percorre a lista de pessoas e preenche cada posição do vetor 'dados' com uma pessoa
        for(Carona aux: listaCarona){
            dados[i] =
                    "\n\t\t\t\t\t\tNº da carona: " + aux.getIdcarona();
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
