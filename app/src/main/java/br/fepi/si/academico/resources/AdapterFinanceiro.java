package br.fepi.si.academico.resources;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import br.fepi.si.academico.R;
import br.fepi.si.academico.model.Boleto;

public class AdapterFinanceiro extends BaseAdapter {

    private final Activity act;
    private final List<Boleto> boletos;
    private TextView valorCompetencia, valorBruto, valorBolsa, valorBolsaCond,
            valorJuros, valorMulta, valorDesconto, valorSituacao, valorinfo;
    private LinearLayout layout_titulo, layout_valores;

    public AdapterFinanceiro(Activity act, List<Boleto> boletos) {
        this.act = act;
        this.boletos = boletos;
    }

    @Override
    public int getCount() {
        return boletos.size();
    }

    @Override
    public Object getItem(int position) {
        return boletos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater().inflate(R.layout.lista_financeiro_personalizado, parent, false);

        Boleto boleto = boletos.get(position);

        valorCompetencia = view.findViewById(R.id.valor_competencia);
        valorBruto = view.findViewById(R.id.valor_valor_bruto);
        valorBolsa  = view.findViewById(R.id.valor_bolsa);
        valorBolsaCond = view.findViewById(R.id.valor_bolsa_cond);

        layout_titulo = view.findViewById(R.id.layout_titulo_alteracoes);
        layout_valores = view.findViewById(R.id.layout_valores_alteracoes);

        valorJuros = view.findViewById(R.id.valor_juros);
        valorMulta = view.findViewById(R.id.valor_multa);
        valorDesconto = view.findViewById(R.id.valor_desconto);

        valorSituacao = view.findViewById(R.id.valor_situacao);
        valorinfo = view.findViewById(R.id.valor_info);

        valorCompetencia.setText(boleto.getCompetencia());
        valorBruto.setText(format(boleto.getValorOriginal()));
        valorBolsa.setText(format(boleto.getBolsa()));
        valorBolsaCond.setText(format(boleto.getBolsaCondicional()));
        valorJuros.setText(format(boleto.getJuros()));
        valorMulta.setText(format(boleto.getMulta()));
        valorDesconto.setText(format(boleto.getDesconto()));

        if (boleto.isBaixado()){
            valorSituacao.setText("BAIXADO");
            valorinfo.setText(boleto.getDataBaixa());
        }
        else {
            valorSituacao.setText("EM ABERTO");
            valorinfo.setText(boleto.getVencimento());
        }

        return view;
    }

    private String format(Double value){
        try {
            return value.toString();
        }
        catch (Exception e){
            return "";
        }
    }

    public void expandirInformacoes(){
        layout_titulo.setVisibility(View.GONE);
        layout_valores.setVisibility(View.GONE);
    }
}
