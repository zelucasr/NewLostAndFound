package br.ufrn.stronda.newlostandfound.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import br.ufrn.stronda.newlostandfound.Model.PerdiObjeto;
import br.ufrn.stronda.newlostandfound.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by STRONDA on 22/11/2016.
 */

public class PerdiAdapter extends ArrayAdapter<PerdiObjeto> {
    private final Context context;
    private final ArrayList<PerdiObjeto> elementos;

    public PerdiAdapter(Context context, ArrayList<PerdiObjeto> elemento) {
        super(context, R.layout.objdes,elemento);
        this.context = context;
        this.elementos= elemento;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.objdes,parent,false);

        TextView nomeObjetoTxt = (TextView) rowView.findViewById(R.id.descTxt);
        TextView localizacaoTxt = (TextView) rowView.findViewById(R.id.catTxt);
        TextView categoriaTxt = (TextView) rowView.findViewById(R.id.locTxt);
        CircleImageView imagem = (CircleImageView) rowView.findViewById(R.id.imgobj);

        nomeObjetoTxt.setText(elementos.get(position).getNomeDoObjeto());
        localizacaoTxt.setText(elementos.get(position).getCategoria());
        categoriaTxt.setText(elementos.get(position).getLocalizacao());

        Glide.with(context).load(elementos.get(position).getImagem()).into(imagem);



        return rowView;
    }


}
