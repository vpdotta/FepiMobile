package br.fepi.si.academico.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Disciplina {
    private String nome, situacao;
    private Map<Integer, Integer> notas;
    private Map<String, Integer> presencas;

    public Disciplina() {
        this.notas = new HashMap<Integer, Integer>();
        this.presencas = new LinkedHashMap<String, Integer>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nomeDisciplina) {
        this.nome = nomeDisciplina;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Map<Integer, Integer> getNotas() {
        return notas;
    }

    public void setNotas(Map<Integer, Integer> notaBim) {
        this.notas = notaBim;
    }

    public Map<String, Integer> getPresencas() {
        return presencas;
    }

    public void setPresencas(Map<String, Integer> presencas) {
        this.presencas = presencas;
    }
}