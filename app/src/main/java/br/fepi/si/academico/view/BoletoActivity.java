package br.fepi.si.academico.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import br.fepi.si.academico.R;
import br.fepi.si.academico.model.Boleto;
import br.fepi.si.academico.model.DadosAcesso;
import br.fepi.si.academico.util.Contexto;

public class BoletoActivity extends AppCompatActivity {
    private DadosAcesso dadosAcesso;
    private Boleto boleto;
    private TextView valorCompetencia, valorBruto, valorBolsa, valorBolsaCond,
            valorJuros, valorMulta, valorDesconto, valorSituacao, valorVencimento;
    private Button btnCodBarra, btncVisualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boleto);

        boleto = (Boleto) getIntent().getSerializableExtra("boleto");
        dadosAcesso = (DadosAcesso) getIntent().getSerializableExtra("acesso");

        valorCompetencia = findViewById(R.id.valor_competencia);
        valorBruto = findViewById(R.id.valor_valor_bruto);
        valorBolsa = findViewById(R.id.valor_bolsa);
        valorBolsaCond = findViewById(R.id.valor_bolsa_cond);

        valorJuros = findViewById(R.id.valor_juros);
        valorMulta = findViewById(R.id.valor_multa);
        valorDesconto = findViewById(R.id.valor_desconto);

        valorSituacao = findViewById(R.id.valor_situacao);
        valorVencimento = findViewById(R.id.valor_vencimento);

        btnCodBarra = findViewById(R.id.btn_cod_barras);
        btncVisualizar = findViewById(R.id.btn_visualizar_boleto);

        valorCompetencia.setText(boleto.getCompetencia());
        valorBruto.setText(format(boleto.getValorOriginal()));
        valorBolsa.setText(format(boleto.getBolsa()));
        valorBolsaCond.setText(format(boleto.getBolsaCondicional()));

        valorJuros.setText(format(boleto.getJuros()));
        valorMulta.setText(format(boleto.getMulta()));
        valorDesconto.setText(format(boleto.getDesconto()));

        valorSituacao.setText("EM ABERTO");
        valorVencimento.setText(boleto.getVencimento());
    }

    private String format(Double value) {
        try {
            return value.toString();
        } catch (Exception e) {
            return "";
        }
    }

    private class BoletoTask extends AsyncTask {
        //private PDFBox pdf;
        private String urlBoleto;
        private Contexto contexto;

        @Override
        protected Object doInBackground(Object[] objects) {

            contexto = new Contexto();
            this.urlBoleto = contexto.obterUrlBoleto();
            contexto.obterBoleto(this.urlBoleto);

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

        }
    }
}
