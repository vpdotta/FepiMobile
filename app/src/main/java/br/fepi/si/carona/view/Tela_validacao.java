package br.fepi.si.carona.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.fepi.si.carona.model.Pessoa;
import br.fepi.si.academico.R;

import static br.fepi.si.academico.view.LoginActivity.NOME_PREFERENCE;
import static br.fepi.si.academico.view.LoginActivity.VALOR_IP;

public class Tela_validacao extends AppCompatActivity {

    ListView listCaronas;
    ArrayList<Pessoa> lista;
    String url, parametros, idcarona, idstatus;
    TextView txtDados, txtUser;
    Pessoa p;
    EditText editCarona;
    String matriculaPassageiro;
    Button btnVer;
    String matricula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_validacao);

        listCaronas = findViewById(R.id.lista_conteudo);
        editCarona = findViewById(R.id.input_solicitacao);
        btnVer = findViewById(R.id.btn_ver_carona);
        txtUser = findViewById(R.id.txtUser);

        lista = new ArrayList<>();

        //criando uma preferência pra verificação
        final SharedPreferences prefs = getSharedPreferences(NOME_PREFERENCE, MODE_PRIVATE);
        matricula = prefs.getString("login", null);
        String nome = prefs.getString("nome", null);
        String tipo = prefs.getString("tipo", null);

        if(tipo.contains("motorista")) {
            url = "http://" + VALOR_IP + "/webservice/consultaCarona.php?tipo=" + 0 + "&matricula=" + matricula;
        } else {
            url = "http://" + VALOR_IP + "/webservice/consultaCarona.php?tipo=" + 1 + "&matricula=" + matricula;
        }

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//response = JSON gerado do script php
                try {

                    JSONObject jsonObject;//cria objeto JSON
                    JSONArray result = new JSONArray(response);//cria um Array de JSON

                    //para cada JSON do JSONArray, é extraido os dados e armazenados em um string
                    for(int i = 0; i < result.length(); i++){
                        jsonObject = new JSONObject(result.getString(i));//cria um objeto JSON de cada JSON do JSONArray

                        // idsolicitacao = jsonObject.getString("idsolicitacao");

                        p = new Pessoa();

                        p.setMatricula(Integer.parseInt(jsonObject.getString("matricula")));
                        p.setNome(jsonObject.getString("nome"));
                        p.setCarona(Integer.parseInt(jsonObject.getString("idcarona")));
                        p.setStatus(Integer.parseInt(jsonObject.getString("idstatus")));

                        lista.add(p);
                    }

                    //criando um ArrayAdapter para preencher a listView
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Tela_validacao.this,android.R.layout.simple_list_item_1,p.getPessoa(lista));
                    listCaronas.setAdapter(adapter);



                } catch (JSONException e) {
                      Toast.makeText(Tela_validacao.this, "Erro na consulta.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }//fecha catch

            }//fecha onResponse
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                         Toast.makeText(Tela_validacao.this, "Erro na consulta.", Toast.LENGTH_LONG).show();
                    }
                }
        );

        //requisição para consulta do tipo 'StringRequest'
        RequestQueue requestQueue = Volley.newRequestQueue(this);//cria uma nova requisição
        requestQueue.add(stringRequest);//adiciona a requisição

        btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                idcarona = editCarona.getText().toString();

                for(Pessoa p: lista) {
                    if(p.getCarona() == Integer.parseInt(idcarona)) {

                        String nome = p.getNome();

                        //gravando informações no arquivo
                        final SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("idcarona", idcarona);
                        editor.putString("nome_pas", nome);
                        editor.commit();//salvando as preferências
                    }
                }

                Intent abre = new Intent(Tela_validacao.this, Tela_carona.class);
                startActivity(abre);
            }
        });

    }
}
