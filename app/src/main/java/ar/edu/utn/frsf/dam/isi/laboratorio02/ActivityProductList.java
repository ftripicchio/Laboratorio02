package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Activity;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.AppRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.CategoriaRest;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRetrofit;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.RestClient;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityProductList extends AppCompatActivity{

    private ProductoRepository productoRepository = new ProductoRepository();
    private CategoriaRest categoriaRest = new CategoriaRest();
    private Producto productoSeleccionado;
    private Spinner categorias;
    private ListView productos;
    private EditText edtCantidad;
    private Button btnAgregar;
    private Intent i;
    int nuevoPedido = 0;
    List<Categoria> listaCategorias;
    List<Producto> listaProductos;
    ArrayAdapter<Producto> productAdapter;
    AppRepository appRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        appRepository = AppRepository.getInstance(this);

        i = getIntent();
        Bundle extras = i.getExtras();
        if(extras!=null){
            nuevoPedido = extras.getInt("NUEVO_PEDIDO");
        }

        categorias = findViewById(R.id.cmbProductosCategoria);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                listaCategorias = appRepository.getAllCategorias();
                ArrayAdapter<Categoria> categoryAdapter = new ArrayAdapter<>(ActivityProductList.this, android.R.layout.simple_spinner_item, listaCategorias);
                categorias.setAdapter(categoryAdapter);

                listaProductos = appRepository.getAllProductos();

                /*ProductoRetrofit clienteRest =
                        RestClient.getInstance()
                                .getRetrofit()
                                .create(ProductoRetrofit.class);
                Call<List<Producto>> altaCall= clienteRest.listarProductos();
                altaCall.enqueue(new Callback<List<Producto>>() {
                    @Override
                    public void onResponse(Call<List<Producto>> call,
                                           Response<List<Producto>> resp) {
                        listaProductos = resp.body();
                        Log.d("Lista de productos", listaProductos.toString());
                    }
                    @Override
                    public void onFailure(Call<List<Producto>> call, Throwable t) {
                        Toast.makeText(ActivityProductList.this, "Error al cargar productos",
                                Toast.LENGTH_LONG).show();
                    }
                });*/

                categorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        productAdapter.clear();
                        productAdapter.addAll(filtrarProductos(listaProductos, (Categoria) categorias.getSelectedItem()));
                        //productAdapter.addAll(productoRepository.buscarPorCategoria((Categoria) categorias.getSelectedItem()));
                        productAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        };
        Thread unHilo = new Thread(r);
        unHilo.start();

        productos = findViewById(R.id.lstProductos);
        productAdapter = new ArrayAdapter<>(ActivityProductList.this, android.R.layout.simple_list_item_single_choice, new ArrayList<Producto>() );
        productos.setAdapter(productAdapter);
        productos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(nuevoPedido == 1) btnAgregar.setEnabled(true);
                productoSeleccionado = (Producto) productos.getItemAtPosition(position);
            }
        });

        edtCantidad = findViewById(R.id.edtProdCantidad);
        btnAgregar = findViewById(R.id.btnProdAddPedido);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer cantidad = Integer.valueOf(edtCantidad.getText().toString());
                if(cantidad <= 0){
                    Toast.makeText(ActivityProductList.this,"La cantidad debe ser mayor a 0", Toast.LENGTH_LONG).show();
                    return;
                }
                i.putExtra("cantidad", cantidad);
                Integer idProducto = productoSeleccionado.getId();
                i.putExtra("idProducto",idProducto);

                setResult(Activity.RESULT_OK,i);
                finish();
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

    private List<Producto> filtrarProductos(List<Producto> productos, Categoria categoria) {
        List<Producto> retorno = new ArrayList<>();
        for (Producto producto : productos){
            if(producto.getCategoria().getId() == categoria.getId()){
                retorno.add(producto);
            }
        }
        return retorno;
    }
}
