package br.fepi.si.academico.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.fepi.si.academico.model.Aluno;
import br.fepi.si.academico.model.Boleto;
import br.fepi.si.academico.model.Disciplina;

public class FormataDados {
    private static List<String> meses = new ArrayList<String>();

    public FormataDados() {
    }

    public List<Disciplina> formataNotas(Document doc) {
        Elements tabela = doc.getElementsByAttributeValueStarting("id", "ctl24_xgvNotasFilial_DXDataRow");
        List<Disciplina> notas = new ArrayList<Disciplina>();

        for (Element el : tabela) {
            Disciplina tmpDisciplina = new Disciplina();

            // 1 -> 1 BIM
            // 2 -> 2 BIM
            // 3 -> EXAME
            // 4 -> MED FINAL

            try {
                tmpDisciplina.setNome(el.getElementsByClass("dxgv").eachText().get(2));
                tmpDisciplina.setSituacao(el.getElementsByClass("dxgv").eachText().get(3));
                tmpDisciplina.getNotas().put(1, helperIn(el.getElementsByClass("dxgv").eachText().get(5)));
                tmpDisciplina.getNotas().put(2, helperIn(el.getElementsByClass("dxgv").eachText().get(6)));
                tmpDisciplina.getNotas().put(3, helperIn(el.getElementsByClass("dxgv").eachText().get(7)));
                tmpDisciplina.getNotas().put(4, helperIn(el.getElementsByClass("dxgv").eachText().get(4)));
            } catch (Exception e) {
            }

            notas.add(tmpDisciplina);
        }

        return notas;
    }

    public List<Disciplina> formataPresenca(Document doc) {
        formatMesesPresenca(doc);

        Elements tabela = doc.getElementsByAttributeValueStarting("id", "ctl24_xgvFaltasFilial_DXDataRow");
        List<Disciplina> faltas = new ArrayList<Disciplina>();

        for (Element el : tabela) {
            Disciplina tmpDisciplina = new Disciplina();

            try {
                tmpDisciplina.setNome(el.getElementsByClass("dxgv").eachText().get(2));
                tmpDisciplina.setSituacao(el.getElementsByClass("dxgv").eachText().get(3));
                tmpDisciplina.getPresencas().put(meses.get(0), helperIn(el.getElementsByClass("dxgv").eachText().get(4))); // total
                tmpDisciplina.getPresencas().put(meses.get(1), helperIn(el.getElementsByClass("dxgv").eachText().get(5)));
                tmpDisciplina.getPresencas().put(meses.get(2), helperIn(el.getElementsByClass("dxgv").eachText().get(6)));
                tmpDisciplina.getPresencas().put(meses.get(3), helperIn(el.getElementsByClass("dxgv").eachText().get(7)));
                tmpDisciplina.getPresencas().put(meses.get(4), helperIn(el.getElementsByClass("dxgv").eachText().get(8)));
                tmpDisciplina.getPresencas().put(meses.get(5), helperIn(el.getElementsByClass("dxgv").eachText().get(9)));
                tmpDisciplina.getPresencas().put(meses.get(6), helperIn(el.getElementsByClass("dxgv").eachText().get(10)));
            }
            catch (Exception e) {
            }

            faltas.add(tmpDisciplina);
        }

        return faltas;
    }

    private void formatMesesPresenca(Document doc) {
        Elements tabela = doc.getElementsByAttributeValueStarting("id", "ctl24_xgvFaltasFilial_col");

        meses = tabela.eachText();
        meses = meses.subList(5, meses.size()); // deixa apenas os meses e o total de faltas

        for (int i = 0; i < meses.size(); i++) {
            if (meses.get(i).matches("(.*)-(.*)")) {
                meses.set(i, meses.get(i).substring(4));
            }
        }
    }

    public Aluno formataDadosAluno(Document[] doc) {

        Aluno aluno = new Aluno();
        String nome = null;

        try {
            //DADOS PESSOAIS
            nome = doc[0].getElementsByClass("tb_UserTD").text();
            String[]  n = nome.substring(9, nome.indexOf("|") - 1).split(" ");
            nome = n[0] + " " + n[n.length - 1];

            aluno.setNome(nome);
            aluno.setNascimento(new SimpleDateFormat("dd/MM/yyyy")
                    .parse(doc[1].getElementById("ctl24_ctl03_xpnlDadosPessoais_fvDadosAluno_xpgcDadosPessoais_dpNascimento_txtDate").val()));
            aluno.setTelefone(doc[1].getElementById("ctl24_ctl03_xpnlDadosPessoais_fvDadosAluno_xpgcDadosPessoais_mtTel2").val());
            aluno.setEmail(doc[1].getElementById("ctl24_ctl03_xpnlDadosPessoais_fvDadosAluno_xpgcDadosPessoais_tbEMAIL").val());
            aluno.setCurso(doc[0].getElementById("ctl24_ctl03_fvHistorico_RMWTextBox5").val());
            aluno.setCoefRendimento(Double.parseDouble(doc[0].getElementById("ctl24_ctl03_fvHistorico_RMWTextBox2").val().substring(0, 5).replace(",", ".")));
            aluno.setSituacao(doc[0].getElementById("ctl24_ctl03_fvHistorico_RMWTextBox7").val());
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return aluno;
    }

    public List<Boleto> formataBoletos(Document doc) {
        Elements boletosCabecalho = doc.getElementsByAttributeValueMatching("cellpadding", "0px");
        Elements boletosInformacao = doc.getElementsByClass("EduGridMain");

        List<Boleto> boletos = new ArrayList<>();

        Iterator<Element> i1 = boletosCabecalho.iterator();
        Iterator<Element> i2 = boletosInformacao.iterator();

        while (i1.hasNext() && i2.hasNext()) {

            Boleto bol = new Boleto();

            Element atualI1 = i1.next();
            Element atualI2 = i2.next();
            try {
                bol.setBaixado((atualI1.getElementsByAttributeValue("width", "33%").eachText().get(2).substring(10).equals("Baixado"))?true:false);
                bol.setVencimento(atualI1.getElementsByAttributeValue("width", "33%").eachText().get(0).substring(12));
                bol.setCompetencia(atualI2.getElementsByAttribute("align").eachText().get(3));
                bol.setDataBaixa(atualI2.getElementsByAttribute("align").eachText().get(4));
                bol.setValorOriginal(helperDo(atualI2.getElementsByAttribute("align").eachText().get(5).replace(",", ".")));
                bol.setJuros(helperDo(atualI2.getElementsByAttribute("align").eachText().get(6).replace(",", ".")));
                bol.setMulta(helperDo(atualI2.getElementsByAttribute("align").eachText().get(7).replace(",", ".")));
                bol.setDesconto(helperDo(atualI2.getElementsByAttribute("align").eachText().get(8).replace(",", ".")));
                bol.setValorBaixado(helperDo(atualI2.getElementsByAttribute("align").eachText().get(9).replace(",", ".")));
                bol.setBolsaCondicional(helperDo(atualI2.getElementsByAttribute("align").eachText().get(10).replace(",", ".")));
                bol.setBolsa(helperDo(atualI2.getElementsByAttribute("align").eachText().get(11).replace(",", ".")));
            }
            catch (Exception e) {
            }

            boletos.add(bol);
        }

        return boletos;
    }

    public String formataUrlBoleto(Document doc) {
        Element tabela = doc.getElementById("MainContainer");
        Elements conteudo = tabela.getElementsByAttributeValue("title", "Imprimir boleto");

        String dados = conteudo.attr("onclick").toString();
        dados = dados.substring(dados.indexOf("(")+1).replace("'", "");

        String [] attr = dados.split(",");

        String URL = "";

        try {
            URL = "http://portal.fepi.br:8080/Corpore.Net/source//Edu-Educacional//RM.Edu.Controls//EduBoletoDotNetVisWebForm.aspx?PDFReader=true&Param=".concat(attr[1].trim());
        }
        catch (ArrayIndexOutOfBoundsException e) {
        }

        return URL;
    }

    private Integer helperIn(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return null;
        }
    }

    private Double helperDo(String value) {
        try {
            return Double.parseDouble(value);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static String capitalizeWord(String str){
        str = str.toLowerCase();
        String words[]=str.split("\\s");
        String capitalizeWord="";

        for(String w:words){
            String first=w.substring(0,1);
            String afterfirst=w.substring(1);
            capitalizeWord+=first.toUpperCase()+afterfirst+" ";
        }

        return capitalizeWord.trim();
    }
}

