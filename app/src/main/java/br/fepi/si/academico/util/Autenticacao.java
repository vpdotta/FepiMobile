package br.fepi.si.academico.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import br.fepi.si.academico.model.Aluno;

import java.io.IOException;

/* Criado por: Lucas Pereira
 * Data de alteração: 03/07/2020
 * Situação: Fechada
 * */

public class Autenticacao extends Sessao {
    public static Autenticacao instancia;

    protected Autenticacao(){
        obterDadosAcesso();
    }

    public static Autenticacao getAutenticacao() {
        if(instancia == null) {
            instancia = new Autenticacao();
        }

        return instancia;
    }

    public boolean login(Aluno al) {
        Connection.Response response = null;

        Aluno aluno = al;

        final String webSite = "http://portal.fepi.br:8080/Corpore.Net/Login.aspx?ReturnUrl=/Corpore.Net/Main.aspx?ActionID=EduNotaEtapaActionWeb&SelectedMenuIDKey=mnNotasEtapa";

        System.out.println(this.getDadosAcesso().getData());

        try {
            response = Jsoup.connect(webSite)
                    .method(Connection.Method.POST)
                    .data(this.getDadosAcesso().getData())
                    .data("txtUser", aluno.getMatricula())
                    .data("txtPass", aluno.getSenha()).data("ddlAlias", "CorporeRM")
                    .data("btnLogin", "Acessar")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.getDadosAcesso().setCookies(response.cookies());

        if (verficarAcesso()) {
            return true;
        }
        return false;
    }

    private boolean verficarAcesso() {
        Document doc = null;

        final String webSite = "http://portal.fepi.br:8080/Corpore.Net/Main.aspx?SelectedMenuIDKey=Inicio";

        try {
            doc = Jsoup.connect(webSite)
                    .cookies(this.getDadosAcesso().getCookies())
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (doc.getElementById("lbContextInfo").text().endsWith("ITAJUBA"))
            return true;
        return false;
    }
}
