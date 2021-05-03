package br.fepi.si.carona.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import br.fepi.si.academico.R;

import static br.fepi.si.academico.view.LoginActivity.NOME_PREFERENCE;

public class Tela_passageiro extends AppCompatActivity {

    ImageButton btnValidarCarona, btnSolicitarCarona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_passageiro);

        btnValidarCarona = findViewById(R.id.btnValidarCarona);
        btnSolicitarCarona = findViewById(R.id.btnSolicitarCarona2);

        //criando uma preferência pra verificação
        final SharedPreferences prefs = getSharedPreferences(NOME_PREFERENCE, MODE_PRIVATE);
        String nome = prefs.getString("nome", null);

        btnValidarCarona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abre = new Intent(Tela_passageiro.this, Tela_validacao.class);
                startActivity(abre);
            }
        });

        btnSolicitarCarona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abre = new Intent(Tela_passageiro.this, Tela_solicita.class);
                startActivity(abre);
            }
        });
    }
}
