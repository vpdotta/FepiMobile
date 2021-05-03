package br.fepi.si.carona.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.fepi.si.carona.controller.Conexao;
import br.fepi.si.academico.R;

import static br.fepi.si.academico.view.LoginActivity.NOME_PREFERENCE;
import static br.fepi.si.academico.view.LoginActivity.VALOR_IP;

public class Tela_cnh extends AppCompatActivity {

    EditText edtCnh, edtCategoria, edtValidade;
    Button btnRegistrar;
    String url, parametros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cnh);

        edtCnh = findViewById(R.id.input_cnh);
        edtCategoria = findViewById(R.id.input_categoria);
        edtValidade = findViewById(R.id.input_validade);

        btnRegistrar = findViewById(R.id.btn_cadastrar);

        SharedPreferences prefs = getSharedPreferences(NOME_PREFERENCE, MODE_PRIVATE);
        final String matriculaMotorista = prefs.getString("login", null);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);//criando a conexão
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();//criando o network para verificar o estado da rede

                if (networkInfo != null && networkInfo.isConnected()) {//verifica estado da rede

                    String cnh = edtCnh.getText().toString();
                    String categoria = edtCategoria.getText().toString();
                    String validade = edtValidade.getText().toString();

                    if(cnh.isEmpty() || categoria.isEmpty() || validade.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Nenhum campo pode estar vazio.", Toast.LENGTH_LONG).show();
                    } else {

                        url = "http://" + VALOR_IP + "/webservice/registraMotorista.php";//url para requisição de dados

                        parametros = "cnh=" + cnh +
                                "&categoria=" + categoria +
                                "&matriculaMotorista=" + matriculaMotorista +
                                "&validade=" + validade;

                        new SolicitaDados().execute(url);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Nenhuma conexão encontrada", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    class SolicitaDados extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            //através da classe Conexao, os dados são recuperados pelo método 'postDados'
            return Conexao.postDados(urls[0], parametros);

        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String resultado) {

            //verificando o retorno da requisição
            if(resultado.contains("registro_ok")) {//se o registro foi feito com sucesso

                Toast.makeText(getApplicationContext(), "Registro concluído com sucesso.", Toast.LENGTH_LONG).show();//mensagem de confirmação do registro

                Intent abreInicio = new Intent(Tela_cnh.this, Tela_tipo_usuario.class);//Intent para abrir a tela principal
                startActivity(abreInicio);//abrindo tela
            } else {
                Toast.makeText(getApplicationContext(), "Ocorreu um erro.", Toast.LENGTH_LONG).show();//mensagem de erro no registro
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
