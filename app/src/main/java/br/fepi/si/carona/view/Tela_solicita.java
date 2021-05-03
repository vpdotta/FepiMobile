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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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

import java.util.Locale;

import br.fepi.si.carona.controller.Conexao;
import br.fepi.si.academico.R;

import static br.fepi.si.academico.view.LoginActivity.NOME_PREFERENCE;
import static br.fepi.si.academico.view.LoginActivity.VALOR_IP;

public class Tela_solicita extends AppCompatActivity implements LocationListener{

    private ConstraintLayout constraintLayout;
    private Button btnSolicitar, btnProcurar;
    private EditText editMatriculaMotorista;
    private TextView txtDados;

    private LocationSettingsRequest.Builder builder;
    private LocationManager locationManager;
    private final int REQUEST_CHECK_CODE = 5;

    private Double latitude, longitude;

    private String url, parametros, placa;
    private String matriculaMotorista;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_solicita);


        constraintLayout = findViewById(R.id.box_opcoes_2);
        btnSolicitar = findViewById(R.id.btn_solicitar);
        btnProcurar = findViewById(R.id.btn_procurar);
        editMatriculaMotorista =  findViewById(R.id.input_matricula);
        txtDados = findViewById(R.id.txt_info_motorista);

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
                                resolvableApiException.startResolutionForResult(Tela_solicita.this, REQUEST_CHECK_CODE);
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

        if (ContextCompat.checkSelfPermission(Tela_solicita.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Tela_solicita.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }

        getLocation();

        // Fim da rotina para obtenção da localizacao

        final SharedPreferences prefs = getSharedPreferences(NOME_PREFERENCE, MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();

        String nome = prefs.getString("nome", null);
        final String matriculaPassageiro = prefs.getString("login", null);

        btnProcurar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                matriculaMotorista = editMatriculaMotorista.getText().toString();

                editor.putString("matricula_motorista", matriculaMotorista);//salvando a matricula do motorista
                //editor.commit();//salvando as preferências
                editor.apply();//salvando as preferências

                url = "http://" + VALOR_IP + "/webservice/consultaMotorista.php?matricula=" + matriculaMotorista;

                StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {//response = JSON gerado do script php
                        try {

                            JSONObject jsonObject;//cria objeto JSON
                            JSONArray result = new JSONArray(response);//cria um Array de JSON


                            //para cada JSON do JSONArray, é extraido os dados e armazenados em um string
                            for (int i = 0; i < result.length(); i++) {
                                jsonObject = new JSONObject(result.getString(i));//cria um objeto JSON de cada JSON do JSONArray

                                /*motorista = new Motorista();
                                motorista.setMatricula(Integer.parseInt(jsonObject.getString("matricula")));
                                motorista.setNome(jsonObject.getString("nome"));
                                motorista.setSexo(jsonObject.getString("sexo"));
                                motorista.setTelefone(jsonObject.getString("telefone"));
                                motorista.setEmail(jsonObject.getString("email"));
                                motorista.setNascimento(jsonObject.getString("nascimento"));
                                motorista.setCurso(jsonObject.getString("curso"));
                                motorista.setPeriodo(Integer.parseInt(jsonObject.getString("periodo")));
                                motorista.setSituacao(jsonObject.getString("situacao"));
                                motorista.setCnh(Integer.parseInt(jsonObject.getString("cnh")));
                                motorista.setCategoria(jsonObject.getString("categoria"));
                                motorista.setValidade(jsonObject.getString("validade"));
                                motorista.setStatus(Integer.parseInt(jsonObject.getString("idstatus")));*/

                                txtDados.setText("Nome: " + jsonObject.getString("nome") +
                                        "\nEmail: " + jsonObject.getString("email") +
                                        "\nTelefone: " + jsonObject.getString("telefone") +
                                        "\nCNH: " + jsonObject.getString("cnh"));

                               /* cnh = jsonObject.getString("cnh");
                                categoria = jsonObject.getString("categoria");
                                validade = jsonObject.getString("validade");*/

                                placa = jsonObject.getString("placa");

                            }
                        } catch (JSONException e) {
                            Toast.makeText(Tela_solicita.this, "Erro na consulta. (Verifique a matrícula.)", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }//fecha onResponse
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Tela_solicita.this, "Erro na consulta.", Toast.LENGTH_LONG).show();
                            }
                        }
                );

                //requisição para consulta do tipo 'StringRequest'
                RequestQueue requestQueue = Volley.newRequestQueue(Tela_solicita.this);//cria uma nova requisição
                requestQueue.add(stringRequest);//adiciona a requisição

                /*ControllerMotorista cm = new ControllerMotorista();

                motorista = cm.buscaMotorista(matriculaMotorista, Tela_solicita.this);

                txtDados.setText("Nome: " + motorista.getNome() +
                        "\nEmail: " + motorista.getEmail() +
                        "\nTelefone: " + motorista.getTelefone() +
                        "\nCNH: " + motorista.getCnh());*/

                constraintLayout.setVisibility(View.VISIBLE);
            }
        });

        btnSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);//criando a conexão
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();//criando o network para verificar o estado da rede

                if (networkInfo != null && networkInfo.isConnected()) {//verifica estado da rede

                //    int matriculaMotorista2 = p.getMatricula();
                  //  String matriculaCarona = matriculaPassageiro;

                    /*solicitacao = new Solicitacao(Integer.parseInt(matriculaPassageiro), Integer.parseInt(matriculaMotorista));
                    solicitacao.setPlaca(placa);
                    solicitacao.setPaslat(latitude);
                    solicitacao.setPaslong(longitude);
                    motorista.getSolicitacoes().add(solicitacao);*/

                    url = "http://" + VALOR_IP + "/webservice/solicitacaoPassageiro.php";//url para requisição de dados

                    parametros = "matpas=" + matriculaPassageiro +
                            "&matmot=" + matriculaMotorista +
                            "&placa=" + placa +
                            "&paslat=" + latitude +
                            "&paslong=" + longitude;//parametros utilizados na requisição

                    new SolicitaDados().execute(url);//solicitando dados

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
            if(resultado.contains("registro_ok")) {//se o registro foi feito com sucesso

                Toast.makeText(getApplicationContext(), "Solicitação feita com sucesso.\n  Aguarde a confirmação.", Toast.LENGTH_LONG).show();//mensagem de confirmação do registro

                Intent abreInicio = new Intent(Tela_solicita.this, Tela_tipo_usuario.class);//Intent para abrir a tela principal
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5, Tela_solicita.this);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, ""+location.getLatitude()+","+location.getLongitude(), Toast.LENGTH_SHORT).show();
        try {
            Geocoder geocoder = new Geocoder(Tela_solicita.this, Locale.getDefault());
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

    //método de pausa do app
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
