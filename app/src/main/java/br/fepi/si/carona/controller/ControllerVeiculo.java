package br.fepi.si.carona.controller;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.fepi.si.carona.model.Veiculo;
import br.fepi.si.carona.view.Tela_tipo_usuario;

import static br.fepi.si.academico.view.LoginActivity.VALOR_IP;

public class ControllerVeiculo extends AppCompatActivity {

    String  parametros;
    Context context;


    public void registraVeiculo(Veiculo veiculo, Context context) {

        context = context;
        String url = "http://" + VALOR_IP + "/AppFepi/registraVeiculo.php";

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);//criando a conexão
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            parametros = "cnh=" + veiculo.getCnh() +
                    "&placa=" + veiculo.getPlaca() +
                    "&marca=" + veiculo.getMarca() +
                    "&modelo=" + veiculo.getModelo() +
                    "&capacidade=" + veiculo.getCapacidade();//parametros utilizados na requisição

            new SolicitaDados().execute(url);
        } else {//se não tiver conexão com a rede
        Toast.makeText(getApplicationContext(), "Nenhuma conexão encontrada", Toast.LENGTH_LONG).show();
    }
    }

    //método para solicitar dados através da url e dos parametros enviados
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

                Intent abreInicio = new Intent(context, Tela_tipo_usuario.class);//Intent para abrir a tela principal
                startActivity(abreInicio);//abrindo tela
            } else {//se ocorreu algum problema com o registro
                Toast.makeText(getApplicationContext(), "Ocorreu um erro.", Toast.LENGTH_LONG).show();//mensagem de erro no registro
            }
        }
    }
}
