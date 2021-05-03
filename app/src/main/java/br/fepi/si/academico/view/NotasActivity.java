package br.fepi.si.academico.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.fepi.si.academico.R;
import br.fepi.si.academico.model.DadosAcesso;
import br.fepi.si.academico.model.Disciplina;
import br.fepi.si.academico.resources.AdapterNotas;
import br.fepi.si.academico.util.Contexto;


public class NotasActivity extends AppCompatActivity {
    private DadosAcesso dadosAcesso;

    private Spinner spinnerPeriodos;
    private ArrayAdapter<String> adapterPeriodos;

    private ListView listaDeNotas;
    private AdapterNotas adapterListaDeNotas;

    private Map<String, String> periodosDisponiveis = new HashMap<>();
    private ArrayList<String> periodos = new ArrayList<>(); //Contem os periodos obtidos

    private ArrayList<Disciplina> notas = new ArrayList<>();

    private boolean selecionado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semestre);

        dadosAcesso = (DadosAcesso) getIntent().getSerializableExtra("acesso");

        //Criação da lista de periodos (Spinner)
        spinnerPeriodos = findViewById(R.id.spinner_periodo);

        //Inicio da inclusão do tamanho do popUp;
        try {
            Field popUp = Spinner.class.getDeclaredField("mPopup");
            popUp.setAccessible(true);

            ListPopupWindow popupWindow = (ListPopupWindow) popUp.get(spinnerPeriodos);

            popupWindow.setHeight(480); //Tamanho maximo do PopUp de periodos (em pixels)

        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e){
            e.getMessage();
        }
        //Fim da inclusão do tamanho do popUp;

        this.periodos = new ArrayList<>();
        this.periodos.add("-----");

        this.adapterPeriodos = new ArrayAdapter<>(this, R.layout.spinner_fepi, periodos);
        this.adapterPeriodos.setDropDownViewResource(R.layout.spinner_dropdown_fepi);

        spinnerPeriodos.setAdapter(adapterPeriodos);

        new CarregaPeriodosTask().execute();

        AdapterView.OnItemSelectedListener escolha = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (selecionado) {
                    String item = spinnerPeriodos.getSelectedItem().toString();
                    new CarregaNotasTask(periodosDisponiveis, item).execute();
                }
                else {
                    selecionado = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        spinnerPeriodos.setOnItemSelectedListener(escolha);
        // Fim da lista;

        listaDeNotas = findViewById(R.id.lista_conteudo);
        adapterListaDeNotas = new AdapterNotas(this, notas);
        listaDeNotas.setAdapter(adapterListaDeNotas);
    }

    public void doUpdate(Map<String, String> periodosDisponiveis){
        this.periodosDisponiveis.putAll(periodosDisponiveis); //Mantem o obtido para poder finalmente acessar um.
        this.periodos.addAll(periodosDisponiveis.keySet()); //Adiciona os periodos obtidos com o acesso junto ao "selecione..."
        Collections.sort(this.periodos.subList(1, this.periodos.size())); //Mantem o selecione o periodo como primeiro elemento
        adapterPeriodos.notifyDataSetChanged(); //Atualiza a lista deixando ordenada conforme feito o .sort()
    }

    public void attNotas(List<Disciplina> notas){
        this.notas.clear();
        this.notas.addAll(notas);
        this.adapterListaDeNotas.notifyDataSetChanged();
    }

    private class CarregaPeriodosTask extends AsyncTask {
        private Contexto innerContexto;

        public CarregaPeriodosTask() {
            this.innerContexto = new Contexto();
            this.innerContexto.setDadosAcesso(dadosAcesso);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            this.innerContexto.coletarPeriodos();

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            NotasActivity.this.doUpdate(this.innerContexto.getPeriodosDisponiveis());
        }
    }

    private class CarregaNotasTask extends AsyncTask {
        private Contexto innerContexto;
        private String innerOpcaoSelecionada;
        private Map<String, String> innerPeriodosDisponiveis;
        private List<Disciplina> innerNotas;

        public CarregaNotasTask(Map<String, String> periodosDisponiveis, String opcao){
            this.innerPeriodosDisponiveis = periodosDisponiveis;
            this.innerOpcaoSelecionada = opcao;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            this.innerContexto = new Contexto();
            this.innerContexto.setDadosAcesso(dadosAcesso);

            this.innerContexto.setPeriodosDisponiveis(this.innerPeriodosDisponiveis);
            try {
                this.innerContexto.entrarNoContexto(this.innerOpcaoSelecionada);
                this.innerNotas = this.innerContexto.obterNotas();
            }
            catch (Exception e){
                this.innerNotas = null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (this.innerNotas != null)
                attNotas(this.innerNotas);
            else {
                Toast toast = Toast.makeText(NotasActivity.this, "Verifique o período selecionado.", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}