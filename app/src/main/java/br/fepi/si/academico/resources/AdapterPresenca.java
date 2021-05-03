package br.fepi.si.academico.resources;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import br.fepi.si.academico.R;
import br.fepi.si.academico.model.Disciplina;

public class AdapterPresenca extends BaseAdapter {

    private final Activity act;
    private final List<Disciplina> disciplinas;

    public AdapterPresenca(Activity act, List<Disciplina> disciplinas) {
        this.act = act;
        this.disciplinas = disciplinas;
    }

    @Override
    public int getCount() {
        return disciplinas.size();
    }

    @Override
    public Object getItem(int position) {
        return disciplinas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater().inflate(R.layout.lista_presenca_personalizada, parent, false);

            Disciplina disciplina = disciplinas.get(position);
            List<String> meses = new ArrayList<>();

            meses.addAll(disciplina.getPresencas().keySet());

            TextView materia = view.findViewById(R.id.nome_materia);
            materia.setText(disciplina.getNome());

            TextView mes1 = view.findViewById(R.id.titulo_mes1);
            TextView mes2 = view.findViewById(R.id.titulo_mes2);
            TextView mes3 = view.findViewById(R.id.titulo_mes3);
            TextView mes4 = view.findViewById(R.id.titulo_mes4);
            TextView mes5 = view.findViewById(R.id.titulo_mes5);
            TextView mes6 = view.findViewById(R.id.titulo_mes6);

            TextView total = view.findViewById(R.id.qtd_total);

            //FIXO
            mes1.setText(meses.get(1).substring(0,3));
            mes2.setText(meses.get(2).substring(0,3));
            mes3.setText(meses.get(3).substring(0,3));
            mes4.setText(meses.get(4).substring(0,3));
            mes5.setText(meses.get(5).substring(0,3));

            try {
                mes6.setText(meses.get(6).substring(0,3));
            }
            catch (Exception e){
                mes6.setVisibility(View.GONE);
            }

            TextView faltasMes1 = view.findViewById(R.id.faltas_mes1);
            TextView faltasMes2 = view.findViewById(R.id.faltas_mes2);
            TextView faltasMes3 = view.findViewById(R.id.faltas_mes3);
            TextView faltasMes4 = view.findViewById(R.id.faltas_mes4);
            TextView faltasMes5 = view.findViewById(R.id.faltas_mes5);
            TextView faltasMes6 = view.findViewById(R.id.faltas_mes6);

            faltasMes1.setText(format(disciplina.getPresencas().get(meses.get(1))));
            faltasMes2.setText(format(disciplina.getPresencas().get(meses.get(2))));
            faltasMes3.setText(format(disciplina.getPresencas().get(meses.get(3))));
            faltasMes4.setText(format(disciplina.getPresencas().get(meses.get(4))));
            faltasMes5.setText(format(disciplina.getPresencas().get(meses.get(5))));

            if (meses.size() == 7){
                faltasMes6.setText(format(disciplina.getPresencas().get(meses.get(6))));

            }
            else {
                faltasMes6.setVisibility(View.GONE);
            }

            total.setText(format(disciplina.getPresencas().get(meses.get(0))));

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
