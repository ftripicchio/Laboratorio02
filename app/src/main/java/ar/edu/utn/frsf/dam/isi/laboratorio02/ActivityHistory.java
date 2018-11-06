package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.AppRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoConDetalles;

public class ActivityHistory extends AppCompatActivity {

    Button btnNuevo, btnMenu;
    ListView listaPedidos;
    AppRepository appRepository;
    List<PedidoConDetalles> pedidosConDetalles;
    List<Pedido> pedidos = new ArrayList<>();

    PedidoRepository pedidoRepository = new PedidoRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        appRepository = AppRepository.getInstance(this);

        btnMenu = findViewById(R.id.btnHistorialMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityHistory.this, MainActivity.class);
                startActivity(i);
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

        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                pedidosConDetalles = appRepository.getPedidosConDetalle();
                for(PedidoConDetalles pd : pedidosConDetalles){
                    Pedido p = pd.getPedido();
                    p.setDetalle(pd.getDetalles());
                    pedidos.add(p);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PedidoAdapter pedidoAdapter = new PedidoAdapter(ActivityHistory.this, pedidos);
                        listaPedidos.setAdapter(pedidoAdapter);
                    }
                });
            }
        };
        Thread t1 = new Thread(r1);
        t1.start();

    }
}
