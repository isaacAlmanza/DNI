package com.fjd.dni.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fjd.dni.Models.CamposR_1;
import com.fjd.dni.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class AdapterRecyclerInformes extends RecyclerView.Adapter<AdapterRecyclerInformes.ViewHolder> {
    Context context;
    ArrayList<CamposR_1> lista;
    ItemOnClickListener listener;
    ArrayList<CamposR_1> copia;
    public AdapterRecyclerInformes(Context context, ArrayList<CamposR_1> lista, ItemOnClickListener listener) {
        this.context = context;
        this.lista = lista;
        this.listener = listener;
        copia =  new ArrayList<>();
        copia.addAll(lista);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.model_informes, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.placa.setText(lista.get(i).getCampo0());
        holder.apoyo.setText(lista.get(i).getCampo1());
        holder.dir.setText(lista.get(i).getCampo2());
        holder.kva.setText(lista.get(i).getCampo3());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public interface ItemOnClickListener{
         void OnclickItem(CamposR_1 camposR_1);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView placa, apoyo, dir, kva ;
        ImageButton eliminar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            apoyo = itemView.findViewById(R.id.tv2);
            placa = itemView.findViewById(R.id.tv1);
            dir = itemView.findViewById(R.id.tv3);
            kva = itemView.findViewById(R.id.tv4);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public Boolean Filtrado_Codigo(final String buscar){
        boolean r = false;
        Log.e("TAG", "Filtrado_Codigo: enra" + buscar );
        if (buscar.length()==0) {
            lista.clear();
            lista.addAll(copia);
        }else {
            lista.clear();
            List<CamposR_1> collect = copia.stream().
                    filter(i -> (i.getCampo0() + " " + i.getCampo1() + " " + i.getCampo2() + " " + i.getCampo3()).
                            toLowerCase().contains(buscar))
                    .collect(Collectors
                            .toList());
            lista.addAll(collect);
            r= true;
        }
        notifyDataSetChanged();
        return r;
    }



}
