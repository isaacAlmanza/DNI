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


public class AdapterRecyclerPendientes extends RecyclerView.Adapter<AdapterRecyclerPendientes.ViewHolder> {
    Context context;
    ArrayList<CamposR_1> lista;
    ItemOnClickListener listener;
    ArrayList<CamposR_1> copia;
    String  DPTO;
    String  MPIO;
    View layout;
    String  CORREG, CENSO;
    public AdapterRecyclerPendientes(Context context, ArrayList<CamposR_1> lista, ItemOnClickListener listener, View layout) {
        this.context = context;
        this.lista = lista;
        this.listener = listener;
        this.layout = layout;
        copia =  new ArrayList<>();
        copia.addAll(lista);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.model_list_reca, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        holder.apoyo.setText(lista.get(i).getCampo0());

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
            apoyo = itemView.findViewById(R.id.titulo);
            eliminar = itemView.findViewById(R.id.eliminar_m_reca);
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
                    filter(i -> (i.getCampo0() + " " + i.getCampo1() + " " + i.getCampo2()).
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
