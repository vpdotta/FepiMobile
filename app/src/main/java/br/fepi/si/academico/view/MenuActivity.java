package br.fepi.si.academico.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import br.fepi.si.academico.helper.FormataDados;
import br.fepi.si.academico.model.Aluno;
import br.fepi.si.academico.model.DadosAcesso;
import br.fepi.si.academico.util.Contexto;
import br.fepi.si.carona.view.Tela_tipo_usuario;
import br.fepi.si.academico.R;

public class MenuActivity extends AppCompatActivity {
    private DadosAcesso dadosAcesso;
    private ImageView img_user;
    private TextView nomeAluno;
    private ConstraintLayout optNotas, optFrequencia, optFinanceiro, optConfig, optFepiCarona, optSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Coleta dos cookies e datas obtidos na conexao:
        dadosAcesso = (DadosAcesso) getIntent().getSerializableExtra("acesso");

        new ObterDadosTask().execute();

        img_user = findViewById(R.id.fotoAluno);
        nomeAluno = findViewById(R.id.nome_aluno);

        optNotas = findViewById(R.id.opcao_notas);
        optFrequencia = findViewById(R.id.opcao_presenca);
        optFinanceiro = findViewById(R.id.opcao_boleto);
        optConfig = findViewById(R.id.opcao_configuracoes);
        optFepiCarona = findViewById(R.id.opcao_fepi_carona);
        optSair = findViewById(R.id.opcao_sair);


        optNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, NotasActivity.class);
                intent.putExtra("acesso", dadosAcesso);

                startActivity(intent);
            }
        });

        optFrequencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, PresencaActivity.class);
                intent.putExtra("acesso", dadosAcesso);

                startActivity(intent);
            }
        });

        optFinanceiro.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, FinanceiroActivity.class);
                intent.putExtra("acesso", dadosAcesso);

                startActivity(intent);
            }
        });

        optFepiCarona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MenuActivity.this, Tela_tipo_usuario.class);
                //startActivity(intent);
            }
        });


        optSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class ObterDadosTask extends AsyncTask {
        private Contexto innerContexto;
        private Aluno tmpAluno;
        private String urlFoto;

        @Override
        protected Object doInBackground(Object[] objects) {
            this.innerContexto = new Contexto();
            this.innerContexto.setDadosAcesso(dadosAcesso);
            this.innerContexto.coletarPeriodos();
            this.innerContexto.entrarNoContexto((String) this.innerContexto.getPeriodosDisponiveis().keySet().toArray()[0]);
            this.urlFoto = this.innerContexto.obterFotoAluno();
            tmpAluno = this.innerContexto.obterDadosAluno();

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            nomeAluno.setText(FormataDados.capitalizeWord(this.tmpAluno.getNome()));

            new FotoAluno(urlFoto).execute();
        }
    }

    private class FotoAluno extends AsyncTask {

        private String urlFoto;
        private Bitmap aluno;

        private FotoAluno(String urlFoto){
            this.urlFoto = urlFoto;
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            try {
                URL url = new URL(this.urlFoto);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                InputStream input = connection.getInputStream();
                Bitmap foto = BitmapFactory.decodeStream(input);

                this.aluno = foto;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (aluno.getByteCount() > 10000)
                img_user.setImageBitmap(aluno);
        }
    }
}