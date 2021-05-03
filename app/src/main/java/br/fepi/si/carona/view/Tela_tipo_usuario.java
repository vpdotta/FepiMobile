package br.fepi.si.carona.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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

import br.fepi.si.carona.model.Pessoa;
import br.fepi.si.academico.R;

import static br.fepi.si.academico.view.LoginActivity.NOME_PREFERENCE;
import static br.fepi.si.academico.view.LoginActivity.VALOR_IP;

public class Tela_tipo_usuario extends AppCompatActivity {

    ImageButton btnMotorista, btnCarona;
    Pessoa p;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_tipo_usuario);

        btnCarona = findViewById(R.id.btnCarona);
        btnMotorista = findViewById(R.id.btnMotorista);

        final SharedPreferences prefs = getSharedPreferences(NOME_PREFERENCE, MODE_PRIVATE);
        String login = prefs.getString("login", null);

        url = "http://" + VALOR_IP + "/webservice/consultaPessoa.php?matricula=" + login;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//response = JSON gerado do script php
                try {
                    JSONObject jsonObject;//cria objeto JSON
                    JSONArray result = new JSONArray(response);//cria um Array de JSON

                    //Log.e("note", result.toString());

                    //para cada JSON do JSONArray, é extraido os dados e armazenados em um string
                    for (int i = 0; i < result.length(); i++) {
                        jsonObject = new JSONObject(result.getString(i));//cria um objeto JSON de cada JSON do JSONArray

                        p = new Pessoa();
                        p.setMatricula(Integer.parseInt(jsonObject.getString("matricula")));
                        p.setNome(jsonObject.getString("nome"));
                        p.setSexo(jsonObject.getString("sexo"));
                        p.setTelefone(jsonObject.getString("telefone"));
                    }

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("nome", p.getNome());
                    editor.commit();

                } catch (JSONException e) {
                    Toast.makeText(Tela_tipo_usuario.this, "Matricula Inexistente.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }//fecha onResponse
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Tela_tipo_usuario.this, "Erro na consulta.", Toast.LENGTH_LONG).show();
                    }
                }
        );


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        final SharedPreferences.Editor editor = prefs.edit();//criando um 'Editor' para salvar as preferências do usuário

        btnMotorista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("tipo", "motorista");
                editor.commit();

                Intent abreMotorista = new Intent(Tela_tipo_usuario.this, Tela_motorista.class);
                startActivity(abreMotorista);
            }
        });

        btnCarona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("tipo", "passageiro");
                editor.commit();

                Intent abreCarona = new Intent(Tela_tipo_usuario.this, Tela_passageiro.class);
                startActivity(abreCarona);
            }
        });

    }
}
