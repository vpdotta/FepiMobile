package br.fepi.si.academico.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import br.fepi.si.academico.model.DadosAcesso;

import java.io.IOException;

/* Criado por: Lucas Pereira
 * Data de alteração: 03/07/2020
 * Situação: Fechada
 * */

public abstract class Sessao {
    private DadosAcesso dadosAcesso;

    public Sessao(){
        this.dadosAcesso = new DadosAcesso();
    }

    public DadosAcesso getDadosAcesso() {
        return dadosAcesso;
    }

    public void setDadosAcesso(DadosAcesso acesso) {
        this.dadosAcesso = acesso;
    }

    private void addDadosAcesso(String key, Element el) {
        this.dadosAcesso.getData().put(key, el.attr("value"));
    }

    protected void atualizarDados(Document doc) {
        addDadosAcesso("__EVENTTARGET", doc.select("input[name=__EVENTTARGET]").first());
        addDadosAcesso("__EVENTARGUMENT", doc.select("input[name=__EVENTARGUMENT]").first());
        addDadosAcesso("__VIEWSTATE", doc.select("input[name=__VIEWSTATE]").first());
        addDadosAcesso("__VIEWSTATEGENERATOR", doc.select("input[name=__VIEWSTATEGENERATOR]").first());
        addDadosAcesso("__EVENTVALIDATION", doc.select("input[name=__EVENTVALIDATION]").first());
    }

    protected void obterDadosAcesso() {
        Connection.Response dc = null;

        final String webSite = "http://portal.fepi.br:8080/Corpore.Net/Main.aspx?SelectedMenuIDKey=Inicio";

        try {
            dc = Jsoup.connect(webSite)
                    .method(Connection.Method.GET)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Document responseDocument = null;
        try {
            responseDocument = dc.parse();
            atualizarDados(responseDocument);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
