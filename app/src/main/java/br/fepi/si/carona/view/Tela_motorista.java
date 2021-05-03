package br.fepi.si.carona.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import br.fepi.si.academico.R;

public class Tela_motorista extends AppCompatActivity {

    ImageButton btnValidacao, btnSolicitacao, btnCadVeiculo, btnCnh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_motorista);

        btnValidacao = findViewById(R.id.btnValidacao);
        btnSolicitacao = findViewById(R.id.btnSolicitacao);
        btnCadVeiculo = findViewById(R.id.btnCadVeiculo);
        btnCnh = findViewById(R.id.btnCnh);

        btnCnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abre = new Intent(Tela_motorista.this, Tela_cnh.class);
                startActivity(abre);
            }
        });

        btnValidacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent abre = new Intent(Tela_motorista.this, Tela_validacao.class);
                startActivity(abre);
            }
        });

        btnSolicitacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent abre = new Intent(Tela_motorista.this, Tela_solicitacoes_pendentes.class);
                startActivity(abre);

            }
        });

        btnCadVeiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abre = new Intent(Tela_motorista.this, Tela_veiculo.class);
                startActivity(abre);
            }
        });
    }
}
