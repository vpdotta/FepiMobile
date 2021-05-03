package br.fepi.si.academico.resources;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.fepi.si.academico.R;
import br.fepi.si.academico.model.Disciplina;

public class AdapterNotas extends BaseAdapter {

    private final Activity act;
    private final List<Disciplina> notas;
    private TextView nomeDisciplina, bim1, bim2, mediaFinal, titulo_exame, valor_exame, situacao;

    public AdapterNotas(Activity act, List<Disciplina> notas) {
        this.act = act;
        this.notas = notas;
    }

    @Override
    public int getCount() {
        return notas.size();
    }

    @Override
    public Object getItem(int position) {
        return notas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater().inflate(R.layout.lista_notas_personalizada, parent, false);
        Disciplina disciplina = notas.get(position);

        nomeDisciplina = view.findViewById(R.id.nome_disciplina);
        bim1 = view.findViewById(R.id.nota_1bim);
        bim2 = view.findViewById(R.id.nota_2bim);
        mediaFinal = view.findViewById(R.id.media_final);
        titulo_exame = view.findViewById(R.id.titulo_exame);
        valor_exame = view.findViewById(R.id.notas_nota_exame);
        situacao = view.findViewById(R.id.situacao);

        nomeDisciplina.setText(disciplina.getNome());
        bim1.setText(format(disciplina.getNotas().get(1)));
        bim2.setText(format(disciplina.getNotas().get(2)));
        mediaFinal.setText(format(disciplina.getNotas().get(4)));

        String tmp = format(disciplina.getNotas().get(3));
        valor_exame.setText(tmp);

        if (tmp.equals("")){
            titulo_exame.setVisibility(View.GONE);
            valor_exame.setVisibility(View.GONE);
        }

        situacao.setText(disciplina.getSituacao());

        return view;
    }

    private String format(Integer value){
        try {
            return value.toString();
        }
        catch (Exception e){
            return "";
        }
    }
}
