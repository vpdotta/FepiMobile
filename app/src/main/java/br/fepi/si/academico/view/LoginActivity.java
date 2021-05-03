package br.fepi.si.academico.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.fepi.si.academico.R;
import br.fepi.si.academico.model.DadosAcesso;
import br.fepi.si.academico.model.Aluno;
import br.fepi.si.academico.util.Autenticacao;

public class LoginActivity extends AppCompatActivity {
    private Aluno aluno;
    private ProgressDialog dialog;
    private EditText edtMatricula, edtSenha;
    private Button btnEntrar;
    private CheckBox chk_credenciais;

    public static final String VALOR_IP = "IP DO SV CARONA/";
    public static final String NOME_PREFERENCE = "INFORMACOES_LOGIN_AUTOMATICO";
    public static final DadosAcesso acesso = new DadosAcesso();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtMatricula = findViewById(R.id.input_matricula);
        edtSenha = findViewById(R.id.input_senha);
        btnEntrar = findViewById(R.id.btn_acessar);
        chk_credenciais = findViewById(R.id.chk_salvar_credenciais);

        SharedPreferences prefs = getSharedPreferences(NOME_PREFERENCE, MODE_PRIVATE);

        String matricula = prefs.getString("matricula", null);
        String senha = prefs.getString("senha", null);

        try {
            edtMatricula.setText(matricula);
            edtSenha.setText(senha);

            if (matricula.isEmpty() == false && senha.isEmpty() == false){
                chk_credenciais.setVisibility(View.GONE);
            }
        }
        catch (Exception e){

        }

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String matricula, senha;

                matricula = edtMatricula.getText().toString();
                senha = edtSenha.getText().toString();

                if (chk_credenciais.isChecked()){
                    SharedPreferences.Editor editor = getSharedPreferences(NOME_PREFERENCE, MODE_PRIVATE).edit();

                    editor.putString("matricula", matricula);
                    editor.putString("senha", senha);
                    editor.commit();
                }

                validarDados(matricula, senha);
            }
        });
    }

    private void validarDados(String matricula, String senha) {

        // Fepi Carona:
        final SharedPreferences prefs = getSharedPreferences(NOME_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();//criando um 'Editor' para salvar as preferências do usuário
        editor.putString("login", matricula);//salvando a matricula
        editor.apply();
        //Fim Fepi Carona

        if (matricula.equals("")) {
            edtMatricula.setError("Informe sua matricula.");
        }

        else if (senha.equals("")) {
            edtSenha.setError("Informe sua senha.");
        }

        else {
            progressDialog();
            aluno = new Aluno(matricula, senha);

            new AutenticacaoTask().execute();
        }
    }

    public void progressDialog() {
        //PopUp de progresso
        this.dialog = new ProgressDialog(LoginActivity.this);
        this.dialog.setTitle("Autenticando");
        this.dialog.setMessage("Aguarde...");
        this.dialog.show();
    }

    public void stopProgressDialog() {
        this.dialog.cancel();
    }

    private class AutenticacaoTask extends AsyncTask {
        private Autenticacao autenticacao;
        private boolean acessou;

        public AutenticacaoTask() {
            this.acessou = false;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            this.autenticacao = Autenticacao.getAutenticacao();

            try {
                this.acessou = this.autenticacao.login(aluno);
            }
            catch (NullPointerException e){
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            LoginActivity.this.stopProgressDialog();

            if (acessou) {
                acesso.setData(this.autenticacao.getDadosAcesso().getData());
                acesso.setCookies(this.autenticacao.getDadosAcesso().getCookies());

                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);

                intent.putExtra("acesso", acesso);

                startActivity(intent);
            }

            else {
                Toast toast = Toast.makeText(LoginActivity.this, "Verifique seus dados e tente novamente.", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
