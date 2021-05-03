package br.fepi.si.carona.view;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.fepi.si.carona.controller.Conexao;
import br.fepi.si.carona.model.Veiculo;
import br.fepi.si.academico.R;

import static br.fepi.si.academico.view.LoginActivity.VALOR_IP;

public class Tela_veiculo extends AppCompatActivity {

    EditText edtCnh, edtPlaca, edtMarca, edtModelo, edtCapacidade;
    TextView txtUser;
    Button btnRegistrar;
    String parametros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_veiculo);

        edtCnh = findViewById(R.id.input_cnh);
        edtPlaca = findViewById(R.id.input_placa);
        edtMarca = findViewById(R.id.input_marca);
        edtModelo =  findViewById(R.id.input_modelo);
        edtCapacidade = findViewById(R.id.input_capacidade);

        txtUser = findViewById(R.id.txtUser);
        btnRegistrar = findViewById(R.id.btn_cadastrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {

                    String cnh = edtCnh.getText().toString();
                    String placa = edtPlaca.getText().toString();
                    String marca = edtMarca.getText().toString();
                    String modelo = edtModelo.getText().toString();
                    String capacidade = edtCapacidade.getText().toString();

                    Veiculo veiculo = new Veiculo();

                    veiculo.setCnh(Integer.parseInt(cnh));
                    veiculo.setPlaca(placa);
                    veiculo.setMarca(marca);
                    veiculo.setModelo(modelo);
                    veiculo.setCapacidade(Integer.parseInt(capacidade));

                    if(cnh.isEmpty() ||placa.isEmpty() || marca.isEmpty() || modelo.isEmpty() ||capacidade.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Nenhum campo pode estar vazio.", Toast.LENGTH_LONG).show();
                    } else {

                        String url = "http://" + VALOR_IP + "/webservice/registraVeiculo.php";

                        parametros = "&cnh=" + veiculo.getCnh() +
                                "&placa=" + veiculo.getPlaca() +
                                "&marca=" + veiculo.getMarca() +
                                "&modelo=" + veiculo.getModelo() +
                                "&capacidade=" + veiculo.getCapacidade();//parametros utilizados na requisição

                        new SolicitaDados().execute(url);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Nenhuma conexão encontrada", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    class SolicitaDados extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            //através da classe Conexao, os dados são recuperados pelo método 'postDados'
            return Conexao.postDados(urls[0], parametros);
        }

        @Override
        protected void onPostExecute(String resultado) {
            if(resultado.contains("registro_ok")) {//se o registro foi feito com sucesso

                Toast.makeText(getApplicationContext(), "Registro concluído com sucesso.", Toast.LENGTH_LONG).show();//mensagem de confirmação do registro

                Intent abreInicio = new Intent(Tela_veiculo.this, Tela_tipo_usuario.class);//Intent para abrir a tela principal
                startActivity(abreInicio);//abrindo tela
            } else {//se ocorreu algum problema com o registro
                Toast.makeText(getApplicationContext(), "Ocorreu um erro.", Toast.LENGTH_LONG).show();//mensagem de erro no registro
            }
        }
    }
}
