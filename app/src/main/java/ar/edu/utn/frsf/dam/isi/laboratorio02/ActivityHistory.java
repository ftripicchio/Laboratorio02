package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;

public class ActivityHistory extends AppCompatActivity {

    Button btnNuevo, btnMenu;
    ListView listaPedidos;

    PedidoRepository pedidoRepository = new PedidoRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        btnMenu = findViewById(R.id.btnHistorialMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnNuevo = findViewById(R.id.btnHistorialNuevo);
        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityHistory.this, ActivityNewOrder.class);
                startActivity(i);
            }
        });

        listaPedidos = findViewById(R.id.lstHistorialPedidos);
        PedidoAdapter pedidoAdapter = new PedidoAdapter(this, pedidoRepository.getLista());
        listaPedidos.setAdapter(pedidoAdapter);
    }
}
