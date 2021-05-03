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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;

import br.fepi.si.carona.controller.Conexao;
import br.fepi.si.academico.R;

import static br.fepi.si.academico.view.LoginActivity.NOME_PREFERENCE;
import static br.fepi.si.academico.view.LoginActivity.VALOR_IP;

public class Tela_carona extends AppCompatActivity implements LocationListener {

    private String matricula, nome, tipo, idcarona, nome_pas;
    private TextView txtUser, txtName;

    private ImageButton play, stop;

    private LocationSettingsRequest.Builder builder;
    private LocationManager locationManager;
    private final int REQUEST_CHECK_CODE = 5;

    private Double longitude, latitude;

    private String url, parametros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_carona);

        txtUser = findViewById(R.id.txt_usuario);
        txtName = findViewById(R.id.txt_info);

        play = findViewById(R.id.btn_iniciar);
        stop = findViewById(R.id.btn_parar);

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
                                resolvableApiException.startResolutionForResult(Tela_carona.this, REQUEST_CHECK_CODE);
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

        if (ContextCompat.checkSelfPermission(Tela_carona.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Tela_carona.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }

        getLocation();
        // Fim da rotina para obtenção da localizacao

        //criando uma preferência pra verificação
        final SharedPreferences prefs = getSharedPreferences(NOME_PREFERENCE, MODE_PRIVATE);
        matricula = prefs.getString("login", null);
        nome = prefs.getString("nome", null);
        tipo = prefs.getString("tipo", null);
        idcarona = prefs.getString("idcarona", null);
        nome_pas = prefs.getString("nome_pas", null);

        txtUser.setText(nome);//nome do usuario

        if(tipo.contains("passageiro")) {
            txtName.setText("MOTORISTA\n\n" + nome_pas); //TODO: Verificar no layout como inserir os estilos para o nome do passageiro
        } else {
            txtName.setText("PASSAGEIRO\n\n" + nome_pas);
        }

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);//criando a conexão
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();//criando o network para verificar o estado da rede

                if (networkInfo != null && networkInfo.isConnected()) {//verifica estado da rede

                    if(tipo.contains("passageiro")) {

                        url = "http://" + VALOR_IP + "/webservice/caronaPassageiro.php";//url para requisição de dados

                        parametros = "&tipo=" + 0 +
                                "&idcarona=" + idcarona +
                                "&paslat=" + latitude +
                                "&paslong=" + longitude +
                                "&idstatus=" + 1;//parametros utilizados na requisição
                    } else {

                        url = "http://" + VALOR_IP + "/webservice/caronaMotorista.php";//url para requisição de dados

                        parametros = "&tipo=" + 0 +
                                "&idcarona=" + idcarona +
                                "&motlat=" + latitude +
                                "&motlong=" + longitude +
                                "&idstatus=" + 1;//parametros utilizados na requisição
                    }

                    new SolicitaDados().execute(url);//solicitando dados

                } else {//se não tiver conexão com a rede
                    Toast.makeText(getApplicationContext(), "Nenhuma conexão encontrada", Toast.LENGTH_LONG).show();
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);//criando a conexão
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();//criando o network para verificar o estado da rede

                if (networkInfo != null && networkInfo.isConnected()) {//verifica estado da rede
                    if(tipo.contains("passageiro")) {

                        url = "http://" + VALOR_IP + "/webservice/caronaPassageiro.php";//url para requisição de dados

                        parametros = "&tipo=" + 1 +
                                "&idcarona=" + idcarona +
                                "&paslat=" + latitude +
                                "&paslong=" + longitude +
                                "&idstatus=" + 1;//parametros utilizados na requisição
                    } else {

                        url = "http://" + VALOR_IP + "/webservice/caronaMotorista.php";//url para requisição de dados

                        parametros = "&tipo=" + 1 +
                                "&idcarona=" + idcarona +
                                "&motlat=" + latitude +
                                "&motlong=" + longitude +
                                "&idstatus=" + 1;//parametros utilizados na requisição
                    }

                    new SolicitaDados().execute(url);

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
            if(resultado.contains("registro_ok_inicio")) {//se o registro foi feito com sucesso

                // TODO: Corrigir pois é possível finalizar uma carona antes de inicializá-la
               // Toast.makeText(getApplicationContext(), "Carona iniciada. \nFinalize-a ao término para valida-la", Toast.LENGTH_LONG).show();//mensagem de confirmação do registro

                Intent abreInicio = new Intent(Tela_carona.this, Tela_msg_inicio.class);//Intent para abrir a tela principal
                startActivity(abreInicio);//abrindo tela

            } else if(resultado.contains("registro_ok_fim")) {

                Intent abreInicio = new Intent(Tela_carona.this, Tela_msg_fim.class);//Intent para abrir a tela principal
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5, Tela_carona.this);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, ""+location.getLatitude()+","+location.getLongitude(), Toast.LENGTH_SHORT).show();
        try {
            Geocoder geocoder = new Geocoder(Tela_carona.this, Locale.getDefault());
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
