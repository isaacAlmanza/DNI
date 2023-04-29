package com.fjd.dni.Controllers;

import static androidx.core.content.ContextCompat.getColor;
import static androidx.core.content.ContextCompat.getDrawable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fjd.dni.Models.ArrayMain;
import com.fjd.dni.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class DynamicSelect {

    Context context;
    LinearLayout layoutPrincipal, layoutPuntoPago;
    TextView label, tv_punto_pago, tv_fecha_pago;
    String punto_pago, fecha_pago, valor_pago;
    EditText et_valor_pago;
    Button save_modal, cancel;
    ArrayList<String> lista_data;
    ArrayList<ArrayMain> lista_dependencias;
    String TAG = "Error_Create_Selects", ORDEN,USER,TIPO_ORDEN;
    View layout;
    FloatingActionButton fbtn_client;

    public DynamicSelect(Context context){
        this.context =  context;
      //  layoutPrincipal =  ((Activity) context).findViewById(R.id.formatos);
        //layout =  ((Activity) context).findViewById(R.id.layout_gestion);
        lista_dependencias = new ArrayList<>();
        Params();
    }
    void Params(){
        USER = GetSharedPreferences.getSharedData(context).get(0);
        ORDEN = DataOrder.getOrden(context);
        TIPO_ORDEN= DataOrder.getTipoOrden(context);
    }
    @SuppressLint({"UseCompatLoadingForDrawables", "Recycle", "Range"})
    public void CreateComponent(){
        try {
            resetTable();
            SQLiteDatabase db = Connections.Database(context);
            ContentValues cv = new ContentValues();
            Cursor fila  = db.rawQuery("select * from ENCABEZADO where  CREADO like 'N' ORDER BY CPG_ORDEN",null);
            for (int i = 0; i < fila.getCount(); i++) {

                fila.moveToPosition(i);
                // MOSTRAR SIN SON TIPO DEPENDIENTES
                ///////////////////////////////////////////////////
                label = new TextView(context);
                label.setText(fila.getString(1));
                label.setTextColor(getColor(context, R.color.text));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                params.topMargin =20;
                params.leftMargin =10;
                label.setLayoutParams(params);
                ///////////////////////////////////////////////////
                TextView textView = new TextView(context);
                textView.setTag(fila.getString(fila.getColumnIndex("CPG_ID"))+
                        "-"+fila.getString(fila.getColumnIndex("CPG_DEPENDIENTE"))+
                        "-"+fila.getString(fila.getColumnIndex("CPG_DESCRIPCION"))+
                        "-"+fila.getString(fila.getColumnIndex("CPG_ORDEN"))+
                        "-"+fila.getString(fila.getColumnIndex("DESPLIEGA")));

                textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_search_24_blue, 0);
                textView.setTextColor(getColor(context,R.color.text));
                textView.setHint(R.string.seleccione);
                textView.setPadding(15,15,15,15);
                LinearLayout.LayoutParams paramseditex = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                paramseditex.weight =1;
                textView.setLayoutParams(paramseditex);
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setId(Integer.parseInt(fila.getString(0)));
                textView.setBackground(getDrawable(context,R.drawable.box_text));
               // textView.setHint(fila.getString(1));
                ///////////////////////////////////////////////////

                textView.setOnClickListener(v -> {
                    Params();
                    if (ORDEN.isEmpty()){
                        //ViewSnackBar.SnackBarDanger(layout, context.getString(R.string.orden_vacia), context);
                        return;
                    }
                    String[] tag = String.valueOf(v.getTag()).trim().split("-");
                    ComponentSelect( String.valueOf(v.getId()), tag[1],  tag[2], Integer.parseInt(tag[3]), tag[4] );
                });
                cv.put("CREADO", "S");
                db.update("ENCABEZADO", cv, "CPG_ID=?", new String[]{fila.getString(0)});
                layoutPrincipal.addView(label);
                layoutPrincipal.addView(textView);

            }
            for (int i = 0; i < fila.getCount(); i++) {
                fila.moveToPosition(i);
                TextView textView = layoutPrincipal.findViewWithTag(
                        fila.getString(fila.getColumnIndex("CPG_ID"))+
                        "-"+fila.getString(fila.getColumnIndex("CPG_DEPENDIENTE"))+
                        "-"+fila.getString(fila.getColumnIndex("CPG_DESCRIPCION"))+
                        "-"+fila.getString(fila.getColumnIndex("CPG_ORDEN"))+
                        "-"+fila.getString(fila.getColumnIndex("DESPLIEGA")));
                llenar(textView,i);
            }
        }catch (Exception e){
            Log.e(TAG, "CreateComponent " + e.fillInStackTrace());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }
    @SuppressLint({"Recycle", "Range"})
    void ComponentSelect( String componente, String depende, String title, int consecutivo, String despliega){
        try {
            SQLiteDatabase db = Connections.Database(context);
            lista_data = new ArrayList<>();
            Dialog dialog = new Dialog(context);
            //dialog.setContentView(R.layout.item);
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            Log.e(TAG, "ComponentSelect: "+ componente + " "+ depende + " "+ consecutivo);
            if ( consecutivo>1 && depende.equalsIgnoreCase("S") ){
                String filtro = "null";
                int buscador = consecutivo-1;
                Cursor cursor = db.rawQuery("SELECT CPG_ID FROM ENCABEZADO where CPG_ORDEN LIKE '"+buscador+"' ", null);
                cursor.moveToPosition(0);
                //TextView tv_anterior = layoutPrincipal.findViewById(cursor.getInt(0));
                Cursor res =  db.rawQuery("SELECT * FROM RESPUESTAS_ENCA WHERE GR_ORDEN = '"+ORDEN+"'", null);
                if (res.getCount()>0){
                    while(res.moveToNext()){
                        String dependencia = res.getString(res.getColumnIndex("GR_ID_RESPUESTA"));
                        Log.e(TAG, "ComponentSelect: " + dependencia );
                        Cursor pgc = db.rawQuery("SELECT * FROM PARAMETROS_GC WHERE PGC_ID_PREGUNTA ='"+componente+"' AND PGC_DEPENDENCIA = '"+dependencia+"'", null);
                        if (pgc.getCount()>0){
                            pgc.moveToPosition(0);
                            filtro = pgc.getString(pgc.getColumnIndex("PGC_DEPENDENCIA"));
                        }
                    }
                }
                CargarListaData(filtro, componente, consecutivo);
            }
            else {
                if (consecutivo==1){
                    Cursor interno = db.rawQuery("SELECT TIPO_ORDEN FROM ORDENES WHERE ORDEN ='"+ORDEN+"'", null);
                    if (interno.getCount()>0){
                        interno.moveToPosition(0);
                        CargarListaData(interno.getString(0), componente, consecutivo);
                    }

                }else {
                    CargarListaData("", componente, consecutivo);
                }


            }
            TextView titulo = dialog.findViewById(R.id.titulo);
            titulo.setText(title);
            EditText et_text = dialog.findViewById(R.id.et_text);
            et_text.setHint(R.string.hintSearch);
            ListView listView = dialog.findViewById(R.id.list_view_item);
            ArrayAdapter<String> adapter_item = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,lista_data);
            listView.setAdapter(adapter_item);
            et_text.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter_item.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            TextView textView = layoutPrincipal.findViewById(Integer.parseInt(componente));
            listView.setOnItemClickListener((parent, view, position, id) -> {
                try {
                    textView.setText(adapter_item.getItem(position));
                    String respuesta =textView.getText().toString().trim().split(" - ")[0];
                    String pregunta = String.valueOf(textView.getTag()).split("-")[0];
                    Lega_Respuestas_Enca(pregunta,respuesta);
                    if (depende.equalsIgnoreCase("S")){
                        VerificarOpciones(consecutivo, respuesta, despliega);
                    }
                    dialog.dismiss();
                }catch (Exception e){
                    Log.e(TAG, "ComponentSelect: " + e.fillInStackTrace() );
                }
            });
        }catch (Exception e){
            Log.e(TAG, "ComponentSelect: " + e.fillInStackTrace() );
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    void VerificarOpciones( int consecutivo, String respuesta, String despliega){
        SQLiteDatabase db  = Connections.Database(context);
        // LIMPIAMOS LOS DATOS SIN CAMBIAMOS DE OPCIONES
        Cursor cursor = db.rawQuery("SELECT * FROM ENCABEZADO", null);
        if (cursor.getCount()>0) {
            for (int i=consecutivo; i < cursor.getCount();i++){
                cursor.moveToPosition(i);
                TextView textView1 = layoutPrincipal.findViewById(Integer.parseInt(cursor.getString(0)));
                textView1.setText("");
            }
        }
        cursor.close();
        if (despliega.equalsIgnoreCase("S")){
            // VERIFICAMOS SI CADA ITEM REQUIERE MODAL DE PAGOS
            Cursor extras =  db.rawQuery("SELECT *  FROM PARAMETROS_GC WHERE PGC_ID = '"+respuesta+"' AND PGC_REQUIERE_FECHA_PAGO ='S'", null);
            if (extras.getCount()>0){
                extras.moveToPosition(0);
                @SuppressLint("Range") String punto_pago =  extras.getString(extras.getColumnIndex("PGC_REQUIERE_PUNTO_PAGO"));
                if (punto_pago.equalsIgnoreCase("S")){
                    layoutPuntoPago.setVisibility(View.VISIBLE);
                }else {
                    layoutPuntoPago.setVisibility(View.GONE);
                }
            }
            extras.close();
            // VERIFICAMOS SI CADA ITEM REQUIERE MODAL DE INFO CLIENTE
            Cursor cliente = db.rawQuery("SELECT * FROM PARAMETROS_GC WHERE PGC_ID = '"+respuesta+"' AND PGC_REQUIERE_INFO_CLIENTE ='S' ", null);
            if (cliente.getCount()>0){
                fbtn_client.setEnabled(true);
                DataOrder.setRequireCliente(context, 1);
            }else {
                fbtn_client.setEnabled(false);
                DataOrder.setRequireCliente(context, 0);
            }
            //// VERIFICAMOS SI CADA ITEM REQUIERE FOTO
            cliente.close();
            Cursor foto = db.rawQuery("SELECT * FROM PARAMETROS_GC WHERE PGC_ID = '"+respuesta+"' AND PGC_REQUIERE_FOTO ='N' ", null);
            if (foto.getCount()>0){
                DataOrder.setRequireFoto(context, 1);
            }else {
                DataOrder.setRequireFoto(context, 0);
            }
            foto.close();
        }
        db.close();
    }
    void Lega_Respuestas_Enca(String  req, String res){
        SQLiteDatabase db = Connections.Database(context);
        ContentValues cv = new ContentValues();
        cv.put("GR_ORDEN",ORDEN );
        cv.put("GR_ID_PREGUNTA", req);
        cv.put("GR_ID_RESPUESTA", res);
        cv.put("GR_GESTOR",USER);
        @SuppressLint("Recycle") Cursor fila = db.rawQuery("select * from RESPUESTAS_ENCA where  GR_ORDEN = '"+  ORDEN
                +"' and GR_ID_PREGUNTA like '"+req+"' AND ESTADO = 'N'", null);
        if (fila.getCount()==0) {
            db.insert("RESPUESTAS_ENCA", null, cv);
            // Snackbar.make(layout, "Respuesta Guardada", Snackbar.LENGTH_LONG).setBackgroundTint(getColor(R.color.success)).show();
            ViewSnackBar.SnackBarSuccess(layout, "Respuesta Guardada", context);
        }else {
            db.update("RESPUESTAS_ENCA",cv,"GR_ORDEN=? and GR_ID_PREGUNTA =? AND ESTADO =?" ,new String[]{ORDEN, req, "N"});
            //   Snackbar.make(layout, "Respuesta Guardada", Snackbar.LENGTH_LONG).setBackgroundTint(getColor(R.color.success)).show();
            ViewSnackBar.SnackBarSuccess(layout, "Respuesta Guardada", context);
        }


        db.close();
    }

    @SuppressLint({"Recycle", "Range"})
    void CargarListaData(String tag, String componente, int consecutivo) {
        try {
            SQLiteDatabase db =Connections.Database(context);
            Log.e(TAG, "CargarListaData: tag" + tag );
            Cursor cursor;
            if (consecutivo==1){
                cursor = db.rawQuery("SELECT * FROM PARAMETROS_GC WHERE  PGG_CODIGO_EXTERNO LIKE '"+tag+"' ", null);
            }else {
                cursor = db.rawQuery("SELECT * FROM PARAMETROS_GC WHERE  PGC_ID_PREGUNTA LIKE '"+componente+"' ", null);
            }
            if (!tag.isEmpty() && consecutivo > 1){
                cursor = db.rawQuery("SELECT * FROM PARAMETROS_GC WHERE PGC_DEPENDENCIA LIKE '"+tag+"'AND PGC_ID_PREGUNTA= '"+componente+"'", null);
            }
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    lista_data.add(cursor.getString(2) + " - " + cursor.getString(4));
                }

            }
            db.close();
        }catch (Exception e){
            Log.e(TAG, "CargarListaData: " + e.fillInStackTrace() );
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    void resetTable(){
        try {
            SQLiteDatabase db =Connections.Database(context);
            ContentValues cv = new ContentValues();
            cv.put("CREADO","N");
            String[]  args = new String[]{"S"};
            db.update("ENCABEZADO",cv, "CREADO=?",args );
            db.close();
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    void DialogFecha(){
        final Calendar c= Calendar.getInstance();
        int dia = c.get(Calendar.DAY_OF_MONTH);
        int mes = c.get(Calendar.MONTH);
        int ano = c.get(Calendar.YEAR);
        tv_fecha_pago.setOnClickListener(v -> {
            @SuppressLint("SetTextI18n") DatePickerDialog dialog = new DatePickerDialog(context, (view, year, month, dayOfMonth) ->
                    tv_fecha_pago.setText((month+1)+"/"+dayOfMonth+"/"+year), ano, mes, dia);
            dialog.show();
        });
    }

    @SuppressLint({"Range", "SetTextI18n"})
    void llenar(TextView textView, int i  ) {
        String TAG = "#$%#%$";
        String pregunta, respuesta, tipo, propiedad2, tension2, exclu;
        SQLiteDatabase db = Connections.Database(context);
        Cursor cursor, cursor1;
        cursor = db.rawQuery("SELECT * FROM RESPUESTAS_ENCA WHERE GR_ORDEN= '" + ORDEN + "' AND ESTADO ='N' ORDER BY GR_ID_PREGUNTA", null);
        if (i>cursor.getCount()-1){
            return;
        }
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(i);
            pregunta = cursor.getString(cursor.getColumnIndex("GR_ID_PREGUNTA"));
            respuesta = cursor.getString(cursor.getColumnIndex("GR_ID_RESPUESTA"));

            cursor1 = db.rawQuery("SELECT * FROM PARAMETROS_GC WHERE PGC_ID_PREGUNTA = '" + pregunta + "'" +
                    " AND PGC_ID= '" + respuesta + "'  ", null);
            if (cursor1.getCount() > 0) {
                while (cursor1.moveToNext()) {
                    textView.setText(cursor1.getString(cursor1.getColumnIndex("PGC_ID")) +" - "+
                            cursor1.getString(cursor1.getColumnIndex("PGC_DESCRIPCION")));
                }
            }
        }
        cursor.close();
        db.close();
    }
}
