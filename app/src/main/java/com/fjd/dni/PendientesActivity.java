package com.fjd.dni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.fjd.dni.Adapters.AdapterRecyclerPendientes;
import com.fjd.dni.Controllers.Connections;
import com.fjd.dni.Models.CamposR_1;

import java.util.ArrayList;

public class PendientesActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {


    FrameLayout layout;
    Toolbar toolbar;
    ImageButton back;
    RecyclerView recycler;
    TextView tv_info, tv_footer;
    SwipeRefreshLayout refreshLayout;
    AdapterRecyclerPendientes adapter;
    SearchView searchView;
    Spinner spinner;
    String DPTO,MPIO,CORREG, TRAFO,APOYO, CENSO, D_DPTO, D_MPIO, D_CORREG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendientes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Views();
        searchView.setOnQueryTextListener(this);
        recycler.setLayoutManager( new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        CargarDatos();
    }
    void Views(){
        recycler = findViewById(R.id.recycler_lr);
        searchView = findViewById(R.id.filtro);
    }

    @SuppressLint("Range")
    void CargarDatos(){
        ArrayList<CamposR_1> lista= new ArrayList<>();
        SQLiteDatabase db = Connections.Database(this);
        Cursor cursor= db.rawQuery("SELECT * FROM ORDENES where ESTADO = 'N' AND CUMPLIDA ='S'", null);
        if (cursor.getCount()>0){
            CamposR_1 data;
            while (cursor.moveToNext()){
                data = new CamposR_1();
                data.setCampo0(cursor.getString(cursor.getColumnIndex("DESCRIPCION")));
              /*  data.setCampo1(cursor.getString(cursor.getColumnIndex("")));
                data.setCampo2(cursor.getString(cursor.getColumnIndex("")));
                data.setCampo3("");*/
                lista.add(data);
            }

        }
        db.close();
        cursor.close();
        adapter = new AdapterRecyclerPendientes(this, lista, null, layout);
        recycler.setAdapter(adapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.Filtrado_Codigo(newText);
        return false;
    }

}