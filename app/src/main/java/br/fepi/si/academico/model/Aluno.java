package br.fepi.si.academico.model;

import java.util.List;

public class Aluno extends Pessoa {
    private String matricula, senha, curso, situacao;
    private int periodo;
    private double coefRendimento;
    private List<Disciplina> disciplinas;

    public Aluno(String matricula, String senha) {
        this.matricula = matricula;
        this.senha = senha;
    }

    public Aluno() {

    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public double getCoefRendimento() {
        return coefRendimento;
    }

    public void setCoefRendimento(double coefRendimento) {
        this.coefRendimento = coefRendimento;
    }
}