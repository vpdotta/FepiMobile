package br.fepi.si.carona.model;

import java.util.ArrayList;

public class Pessoa {

    private int matricula, status, periodo, solicitacao, carona;
    private String nome, telefone, email, sexo, nascimento, curso, situacao;

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getNascimento() {
        return nascimento;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public int getSolicitacao() {
        return solicitacao;
    }

    public void setSolicitacao(int solicitacao) {
        this.solicitacao = solicitacao;
    }

    public int getCarona() {
        return carona;
    }

    public void setCarona(int carona) {
        this.carona = carona;
    }

    //método para concatenar todas as informações presentes na lista de pessoas
    public String[] getListaPessoa(ArrayList<Pessoa> listaPessoa){

        int i = 0;
        String dados[] = new String[listaPessoa.size()];//string que será exibida na lista

        //percorre a lista de pessoas e preenche cada posição do vetor 'dados' com uma pessoa
        for(Pessoa aux: listaPessoa){
            dados[i] =
                   // "\n\t\t\t\t\t\tMatricula: " + aux.getMatricula() +
                            "\n\t\t\t\t\t\tNome: " + aux.getNome() +
                         //   "\n\t\t\t\t\t\tCurso....: " + aux.getCurso() +
                            "\n\t\t\t\t\t\tTelefone: " + aux.getTelefone() +
                            "\n\t\t\t\t\t\tNº da solicitacao: " + aux.getSolicitacao();
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

    public String[] getPessoa(ArrayList<Pessoa> listaPessoa){

        int i = 0;
        String dados[] = new String[listaPessoa.size()];//string que será exibida na lista

        //percorre a lista de pessoas e preenche cada posição do vetor 'dados' com uma pessoa
        for(Pessoa aux: listaPessoa){
            dados[i] =
                    "\n\t\t\t\t\t\tMatricula: " + aux.getMatricula() +
                            "\n\t\t\t\t\t\tNome: " + aux.getNome() +
                            "\n\t\t\t\t\t\tNº da carona: " + aux.getCarona();
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
