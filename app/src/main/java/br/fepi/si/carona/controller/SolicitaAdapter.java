package br.fepi.si.carona.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.fepi.si.carona.model.Pessoa;
import br.fepi.si.academico.R;

public class SolicitaAdapter extends BaseAdapter {

    private Context context;
    private List<Pessoa> pessoaList;

    public SolicitaAdapter(Context context, List<Pessoa> pessoalist) {
        this.context = context;
        this.pessoaList = pessoalist;
    }

    @Override
    public int getCount() {
        return pessoaList.size();
    }

    @Override
    public Object getItem(int position) {
        return pessoaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Recupera o estado da posição atual
        Pessoa pessoa = pessoaList.get(position);

        // Cria uma instância do layout XML para os objetos correspondentes na View
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_adapter, null);

        // Nome - Email
        TextView textPessoa = (TextView)view.findViewById(R.id.txtUser);
        textPessoa.setText(pessoa.getNome() + "\n" + pessoa.getTelefone());

        // Imagem
        //ImageView img = (ImageView)view.findViewById(R.id.imgUser);
        //img.setImageResource(pessoa.getBanner());

        return view;
    }
}
