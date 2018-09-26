package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class ActivityProductList extends AppCompatActivity{

    private ProductoRepository productoRepository = new ProductoRepository();
    private Producto productoSeleccionado;
    private Spinner categorias;
    private ListView productos;
    private EditText edtCantidad;
    private Button btnAgregar;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        i = getIntent();
        Bundle extras = i.getExtras();
        int nuevoPedido=0;
        if(extras!=null){
            nuevoPedido = extras.getInt("NUEVO_PEDIDO");
        }

        categorias = findViewById(R.id.cmbProductosCategoria);
        ArrayAdapter<Categoria> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, productoRepository.getCategorias() );
        categorias.setAdapter(categoryAdapter);

        productos = findViewById(R.id.lstProductos);
        final ArrayAdapter<Producto> productAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, new ArrayList<Producto>() );
        productos.setAdapter(productAdapter);
        productos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                btnAgregar.setEnabled(true);
                productoSeleccionado = (Producto) productos.getItemAtPosition(position);
            }
        });

        categorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productAdapter.clear();
                productAdapter.addAll(productoRepository.buscarPorCategoria((Categoria) categorias.getSelectedItem()));
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        edtCantidad = findViewById(R.id.edtProdCantidad);
        btnAgregar = findViewById(R.id.btnProdAddPedido);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cantidad = edtCantidad.getText().toString();
                i.putExtra("cantidad", cantidad);
                Integer idProducto = productoSeleccionado.getId();
                i.putExtra("idProducto",idProducto);
            }
        });

        if(nuevoPedido == 0){
            edtCantidad.setEnabled(false);
            btnAgregar.setEnabled(false);
        }
        if(productoSeleccionado == null){
            btnAgregar.setEnabled(false);
        }

    }
}
