package br.fepi.si.carona.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import br.fepi.si.carona.controller.Conexao;
import br.fepi.si.carona.model.Pessoa;
import br.fepi.si.academico.R;

import static br.fepi.si.academico.view.LoginActivity.NOME_PREFERENCE;
import static br.fepi.si.academico.view.LoginActivity.VALOR_IP;

public class Tela_solicitacoes_pendentes extends AppCompatActivity implements LocationListener {

    private LocationSettingsRequest.Builder builder;
    private LocationManager locationManager;
    private final int REQUEST_CHECK_CODE = 5;
    Double longitude, latitude, altitude;
    ListView listSolicitacoes;
    ArrayList<Pessoa> lista;
    String url, url2, parametros, matricula;
    Pessoa p;
    EditText editPassageiro;
    String idsolicitacao;
    ImageButton btnConfirmar, btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_solicitacoes_pendentes);

        listSolicitacoes = findViewById(R.id.lista_conteudo);
        editPassageiro = findViewById(R.id.input_solicitacao);
        btnConfirmar = findViewById(R.id.btn_confirmar);
        btnCancelar = findViewById(R.id.btn_cancelar);

        lista = new ArrayList<>();

        // Rotina para obtenção da localizacao
        LocationRequest request = new LocationRequest()
                .setFastestInterval(1500)
                .setInterval(3000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(request);

        Task<LocationSettingsResponse> result =
                LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    task.getResult(ApiException.class);
                } catch (ApiException e) {
                    switch (e.getStatusCode())
                    {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(Tela_solicitacoes_pendentes.this, REQUEST_CHECK_CODE);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            } catch (ClassCastException ex){

                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        {
                            break;
                        }
                    }
                }
            }
        });

        if (ContextCompat.checkSelfPermission(Tela_solicitacoes_pendentes.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Tela_solicitacoes_pendentes.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }

        getLocation();
        // Fim da rotina para obtenção da localizacao

        //criando uma preferência pra verificação
        final SharedPreferences prefs = getSharedPreferences(NOME_PREFERENCE, MODE_PRIVATE);
        matricula = prefs.getString("login", null);
        String nome = prefs.getString("nome", null);
        String tipo = prefs.getString("tipo", null);

        if(tipo.contains("motorista")) {
            url = "http://" + VALOR_IP + "/webservice/consultaSolicitacao.php?tipo=" + 0 + "&matricula=" + matricula;
        } else {
            url = "http://" + VALOR_IP + "/webservice/consultaSolicitacao.php?tipo=" + 1 + "&matricula=" + matricula;
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
                        p.setCurso(jsonObject.getString("curso"));
                        p.setTelefone(jsonObject.getString("telefone"));
                        p.setSolicitacao(Integer.parseInt(jsonObject.getString("idsolicitacao")));


                      //  p.setEmail(jsonObject.getString("email"));
                       // p.setBanner(images[i]);

                        lista.add(p);
                    }

                    //criando um ArrayAdapter para preencher a listView
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Tela_solicitacoes_pendentes.this,android.R.layout.simple_list_item_1,p.getListaPessoa(lista));
                    listSolicitacoes.setAdapter(adapter);

                    // setListAdapter(new SolicitaAdapter(getApplicationContext(), lista));

                    //text.setText(dados);//mostra os dados na TextView

                } catch (JSONException e) {
                    //  Toast.makeText(Carona.this, "Erro na consulta", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }//fecha catch

            }//fecha onResponse
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(Carona.this, "Erro na consulta (v)", Toast.LENGTH_LONG).show();
                    }
                }
        );

        //requisição para consulta do tipo 'StringRequest'
        RequestQueue requestQueue = Volley.newRequestQueue(this);//cria uma nova requisição
        requestQueue.add(stringRequest);//adiciona a requisição

        // CONFIRMAR A CARONA
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                idsolicitacao = editPassageiro.getText().toString();

                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);//criando a conexão
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();//criando o network para verificar o estado da rede

                if (networkInfo != null && networkInfo.isConnected()) {//verifica estado da rede

                    url2 = "http://" + VALOR_IP + "/webservice/solicitacaoMotorista.php";//url para requisição de dados

                    parametros = "idsolicitacao=" + idsolicitacao +
                            "&motlat=" + latitude +
                            "&motlong=" + longitude +
                            "&idstatus=" + 12;//parametros utilizados na requisição

                    new SolicitaDados().execute(url2);//solicitando dados


                } else {//se não tiver conexão com a rede
                    Toast.makeText(getApplicationContext(), "Nenhuma conexão encontrada", Toast.LENGTH_LONG).show();
                }
            }
        });

        //CANCELAR A CARONA
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                idsolicitacao = editPassageiro.getText().toString();

                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);//criando a conexão
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();//criando o network para verificar o estado da rede

                if (networkInfo != null && networkInfo.isConnected()) {//verifica estado da rede

                    url2 = "http://" + VALOR_IP + "/webservice/solicitacaoMotorista.php";//url para requisição de dados

                    parametros = "idsolicitacao=" + idsolicitacao +
                            "&motlat=" + latitude +
                            "&motlong=" + longitude +
                            "&idstatus=" + 13;//parametros utilizados na requisição

                    new SolicitaDados().execute(url2);



                } else {//se não tiver conexão com a rede
                    Toast.makeText(getApplicationContext(), "Nenhuma conexão encontrada", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    //método para solicitar dados através da url e dos parametros enviados
    private class SolicitaDados extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            //através da classe Conexao, os dados são recuperados pelo método 'postDados'
            return Conexao.postDados(urls[0], parametros);

        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String resultado) {

            //verificando o retorno da requisição
            if(resultado.contains("registro_ok_12")) {//se o registro foi feito com sucesso

                Toast.makeText(getApplicationContext(), "Pedido de carona aceito.", Toast.LENGTH_LONG).show();

                Intent abreInicio = new Intent(Tela_solicitacoes_pendentes.this, Tela_tipo_usuario.class);//Intent para abrir a tela principal
                startActivity(abreInicio);//abrindo tela

            } else if(resultado.contains("registro_ok_13")) {

                Toast.makeText(getApplicationContext(), "Pedido de carona negado.", Toast.LENGTH_LONG).show();

                Intent abreInicio = new Intent(Tela_solicitacoes_pendentes.this, Tela_tipo_usuario.class);//Intent para abrir a tela principal
                startActivity(abreInicio);//abrindo tela

            } else {//se ocorreu algum problema com o registro
                Toast.makeText(getApplicationContext(), "Ocorreu um erro.", Toast.LENGTH_LONG).show();//mensagem de erro no registro
            }

        }
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5, Tela_solicitacoes_pendentes.this);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, ""+location.getLatitude()+","+location.getLongitude(), Toast.LENGTH_SHORT).show();
        try {
            Geocoder geocoder = new Geocoder(Tela_solicitacoes_pendentes.this, Locale.getDefault());
            //List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1); //Endereçamento
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
