package com.fjd.dni.Controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.fjd.dni.R;

import java.util.ArrayList;

public interface SelectComponent {
    @SuppressLint("Range")
    static  void Select(Context context, TextView textView, String table, String col1, String col2 ){
        String DPTO, MPIO, CORREG;
        DPTO =  GetSharedPreferences.getSharedEncuesta(context).get(0);
        MPIO= GetSharedPreferences.getSharedEncuesta(context).get(1);
        CORREG =GetSharedPreferences.getSharedEncuesta(context).get(2);
        ArrayList<String> lista = new ArrayList<>();
        ArrayAdapter<String> adapter_item;
        TextView correg = ((Activity) context).findViewById(R.id.tv_correg);
        TextView tv_barrio = ((Activity) context).findViewById(R.id.tv_barrio);
        Cursor cursor;
        SQLiteDatabase db = Connections.Database(context);
        cursor = db.rawQuery("SELECT * FROM "+table , null);
        if (table.equalsIgnoreCase("BARRIOS")){
             cursor = db.rawQuery("SELECT * FROM "+table+" WHERE CORREG = '"+correg.getTag()+"'", null);
        }

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                lista.add(cursor.getString(cursor.getColumnIndex(col1)) + " - " + cursor.getString(cursor.getColumnIndex(col2)));
            }
        }
        db.close();
        cursor.close();
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.item);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        EditText et_text = dialog.findViewById(R.id.et_text);
        TextView titulo =  dialog.findViewById(R.id.titulo);
        titulo.setText(context.getText(R.string.app_name));
        ListView listView = dialog.findViewById(R.id.list_view_item);
        adapter_item = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, lista);
        listView.setAdapter(adapter_item);
        et_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter_item.getFilter().filter(s);
                Log.e("TAG", "onTextChanged: " + s);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String tag = adapter_item.getItem(position);

            textView.setText(tag);
            textView.setTag(tag.split(" - ")[0]);

            if (table.equalsIgnoreCase("CORREGIMIENTOS")){
                tv_barrio.setText(context.getString(R.string.seleccione_barrio));
                tv_barrio.setTag(null);
            }
            dialog.dismiss();
        });
    }
}
