package br.fepi.si.carona.controller;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.fepi.si.carona.model.Motorista;
import br.fepi.si.carona.model.Veiculo;

import static br.fepi.si.academico.view.LoginActivity.VALOR_IP;

public class ControllerMotorista {

    public Motorista buscaMotorista(String matricula, Context context) {

        String url = "http://" + VALOR_IP + "/AppFepi/consultaMotorista.php?matricula=" + matricula;
        final Motorista[] motorista = new Motorista[1];

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//response = JSON gerado do script php
                try {

                    JSONObject jsonObject;//cria objeto JSON
                    JSONArray result = new JSONArray(response);//cria um Array de JSON

                    //para cada JSON do JSONArray, é extraido os dados e armazenados em um string
                    for (int i = 0; i < result.length(); i++) {
                        jsonObject = new JSONObject(result.getString(i));//cria um objeto JSON de cada JSON do JSONArray

                                motorista[0] = new Motorista();
                                motorista[0].setMatricula(Integer.parseInt(jsonObject.getString("matricula")));
                                motorista[0].setNome(jsonObject.getString("nome"));
                                motorista[0].setSexo(jsonObject.getString("sexo"));
                                motorista[0].setTelefone(jsonObject.getString("telefone"));
                                motorista[0].setEmail(jsonObject.getString("email"));
                                motorista[0].setNascimento(jsonObject.getString("nascimento"));
                                motorista[0].setCurso(jsonObject.getString("curso"));
                                motorista[0].setPeriodo(Integer.parseInt(jsonObject.getString("periodo")));
                                motorista[0].setSituacao(jsonObject.getString("situacao"));
                                motorista[0].setCnh(Integer.parseInt(jsonObject.getString("cnh")));
                                motorista[0].setCategoria(jsonObject.getString("categoria"));
                                motorista[0].setValidade(jsonObject.getString("validade"));
                                motorista[0].setStatus(Integer.parseInt(jsonObject.getString("idstatus")));

                                Veiculo v = new Veiculo();
                                v.setCnh(motorista[0].getCnh());
                                v.setPlaca(jsonObject.getString("placa"));

                                motorista[0].setVeiculo(v);

                               /* cnh = jsonObject.getString("cnh");
                                categoria = jsonObject.getString("categoria");
                                validade = jsonObject.getString("validade");*/
                    }
                } catch (JSONException e) {
                    //Toast.makeText(context, "Erro na consulta.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }//fecha onResponse
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                     //   Toast.makeText(context, "Erro na consulta.", Toast.LENGTH_LONG).show();
                    }
                }
        );

        //requisição para consulta do tipo 'StringRequest'
        RequestQueue requestQueue = Volley.newRequestQueue(context);//cria uma nova requisição
        requestQueue.add(stringRequest);//adiciona a requisição

        return motorista[0];
    }
}
