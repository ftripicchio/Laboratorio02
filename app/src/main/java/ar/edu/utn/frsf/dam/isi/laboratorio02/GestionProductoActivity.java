package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.AppRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.CategoriaRest;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRetrofit;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.RestClient;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GestionProductoActivity extends AppCompatActivity {
    private Button btnMenu;
    private Button btnGuardar;
    private Spinner comboCategorias;
    private EditText nombreProducto;
    private EditText descProducto;
    private EditText precioProducto;
    private ToggleButton opcionNuevoBusqueda;
    private EditText idProductoBuscar;
    private Button btnBuscar;
    private Button btnBorrar;
    private Boolean flagActualizacion;
    private ArrayAdapter<Categoria> comboAdapter;
    List<Categoria> listaCategorias;
    private CategoriaRest categoriaRest = new CategoriaRest();
    private int idProductoBuscado;
    private AppRepository appRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_producto);
        appRepository = AppRepository.getInstance(this);

        flagActualizacion = false;
        opcionNuevoBusqueda = (ToggleButton)
                findViewById(R.id.abmProductoAltaNuevo);
        idProductoBuscar = (EditText)
                findViewById(R.id.abmProductoIdBuscar);
        nombreProducto = (EditText)
                findViewById(R.id.abmProductoNombre);
        descProducto = (EditText)
                findViewById(R.id.abmProductoDescripcion);
        precioProducto = (EditText)
                findViewById(R.id.abmProductoPrecio);
        comboCategorias = (Spinner)
                findViewById(R.id.abmProductoCategoria);
        btnMenu = (Button) findViewById(R.id.btnAbmProductoVolver);
        btnGuardar = (Button)
                findViewById(R.id.btnAbmProductoCrear);
        btnBuscar = (Button)
                findViewById(R.id.btnAbmProductoBuscar);
        btnBorrar= (Button)
                findViewById(R.id.btnAbmProductoBorrar);
        opcionNuevoBusqueda.setChecked(false);
        btnBuscar.setEnabled(false);
        btnBorrar.setEnabled(false);
        idProductoBuscar.setEnabled(false);


        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                listaCategorias = appRepository.getAllCategorias();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        comboAdapter = new ArrayAdapter<>(GestionProductoActivity.this, android.R.layout.simple_spinner_item, listaCategorias);
                        comboCategorias.setAdapter(comboAdapter);
                    }
                });
            }
        };
        Thread t1 = new Thread(r1);
        t1.start();
        /*Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    listaCategorias = categoriaRest.listarTodas();
                    comboAdapter = new ArrayAdapter<>(GestionProductoActivity.this, android.R.layout.simple_spinner_item, listaCategorias);
                    comboCategorias.setAdapter(comboAdapter);
                }catch (Exception e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GestionProductoActivity.this, "Error al cargar categorías",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        };
        Thread unHilo = new Thread(r);
        unHilo.start();*/

        opcionNuevoBusqueda.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton buttonView,
                                            boolean isChecked) {
                   flagActualizacion =isChecked;
                   btnBuscar.setEnabled(isChecked);
                   btnBorrar.setEnabled(isChecked);
                   idProductoBuscar.setEnabled(isChecked);
               }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Producto p = new Producto(nombreProducto.getText().toString(), descProducto.getText().toString(),
                        Double.parseDouble(precioProducto.getText().toString()), (Categoria) comboCategorias.getSelectedItem());
                if(opcionNuevoBusqueda.isChecked()){
                    p.setId(idProductoBuscado);
                    Runnable r1 = new Runnable() {
                        @Override
                        public void run() {
                            appRepository.actualizarProducto(p);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(GestionProductoActivity.this, "Producto actualizado con éxito",
                                            Toast.LENGTH_LONG).show();
                                    nombreProducto.setText("");
                                    descProducto.setText("");
                                    precioProducto.setText("");
                                    idProductoBuscar.setText("");
                                    comboCategorias.setSelection(0);
                                }
                            });
                        }
                    };
                    Thread t1 = new Thread(r1);
                    t1.start();
                    /*ProductoRetrofit clienteRest =
                            RestClient.getInstance()
                                    .getRetrofit()
                                    .create(ProductoRetrofit.class);
                    Call<Producto> altaCall= clienteRest.actualizarProducto(idProductoBuscado, p);
                    altaCall.enqueue(new Callback<Producto>() {
                        @Override
                        public void onResponse(Call<Producto> call,
                                               Response<Producto> resp) {
                            Toast.makeText(GestionProductoActivity.this, "Producto actualizado con éxito",
                                    Toast.LENGTH_LONG).show();
                            nombreProducto.setText("");
                            descProducto.setText("");
                            precioProducto.setText("");
                            idProductoBuscar.setText("");
                            comboCategorias.setSelection(0);
                        }
                        @Override
                        public void onFailure(Call<Producto> call, Throwable t) {
                            Toast.makeText(GestionProductoActivity.this, "Error al actualizar el producto",
                                    Toast.LENGTH_LONG).show();
                        }
                    });*/
                } else {
                    Runnable r1 = new Runnable() {
                        @Override
                        public void run() {
                            appRepository.crearProducto(p);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(GestionProductoActivity.this, "Producto creado con éxito",
                                            Toast.LENGTH_LONG).show();
                                    nombreProducto.setText("");
                                    descProducto.setText("");
                                    precioProducto.setText("");
                                    comboCategorias.setSelection(0);
                                }
                            });
                        }
                    };
                    Thread t1 = new Thread(r1);
                    t1.start();
                    /*ProductoRetrofit clienteRest =
                            RestClient.getInstance()
                                    .getRetrofit()
                                    .create(ProductoRetrofit.class);
                    Call<Producto> altaCall= clienteRest.crearProducto(p);
                    altaCall.enqueue(new Callback<Producto>() {
                        @Override
                        public void onResponse(Call<Producto> call,
                                               Response<Producto> resp) {
                            Toast.makeText(GestionProductoActivity.this, "Producto creado con éxito",
                                    Toast.LENGTH_LONG).show();
                            nombreProducto.setText("");
                            descProducto.setText("");
                            precioProducto.setText("");
                            comboCategorias.setSelection(0);
                        }
                        @Override
                        public void onFailure(Call<Producto> call, Throwable t) {
                            Toast.makeText(GestionProductoActivity.this, "Error al crear el producto",
                                    Toast.LENGTH_LONG).show();
                        }
                    });*/
                }

            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idProductoBuscado = Integer.parseInt(idProductoBuscar.getText().toString());
                Runnable r1 = new Runnable() {
                    @Override
                    public void run() {
                        final Producto producto = appRepository.buscarProductoPorId(idProductoBuscado).get(0);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                nombreProducto.setText(producto.getNombre());
                                descProducto.setText(producto.getDescripcion());
                                precioProducto.setText(producto.getPrecio().toString());
                                comboCategorias.setSelection(comboAdapter.getPosition(producto.getCategoria()));
                            }
                        });
                    }
                };
                Thread t1 = new Thread(r1);
                t1.start();
                /*ProductoRetrofit clienteRest =
                        RestClient.getInstance()
                                .getRetrofit()
                                .create(ProductoRetrofit.class);
                Call<Producto> altaCall= clienteRest.buscarProductoPorId(idProductoBuscado);
                altaCall.enqueue(new Callback<Producto>() {
                    @Override
                    public void onResponse(Call<Producto> call,
                                           Response<Producto> resp) {
                        Producto producto = resp.body();
                        nombreProducto.setText(producto.getNombre());
                        descProducto.setText(producto.getDescripcion());
                        precioProducto.setText(producto.getPrecio().toString());
                        comboCategorias.setSelection(comboAdapter.getPosition(producto.getCategoria()));
                    }
                    @Override
                    public void onFailure(Call<Producto> call, Throwable t) {
                        Toast.makeText(GestionProductoActivity.this, "Error al buscar el producto",
                                Toast.LENGTH_LONG).show();
                    }
                });*/
            }
        });

        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable r1 = new Runnable() {
                    @Override
                    public void run() {
                        appRepository.eliminarProducto(appRepository.buscarProductoPorId(idProductoBuscado).get(0));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GestionProductoActivity.this, "Producto borrado con éxito",
                                        Toast.LENGTH_LONG).show();
                                nombreProducto.setText("");
                                descProducto.setText("");
                                precioProducto.setText("");
                                idProductoBuscar.setText("");
                                comboCategorias.setSelection(0);
                            }
                        });
                    }
                };
                Thread t1 = new Thread(r1);
                t1.start();
                /*ProductoRetrofit clienteRest =
                        RestClient.getInstance()
                                .getRetrofit()
                                .create(ProductoRetrofit.class);
                Call<Producto> altaCall= clienteRest.borrar(idProductoBuscado);
                altaCall.enqueue(new Callback<Producto>() {
                    @Override
                    public void onResponse(Call<Producto> call,
                                           Response<Producto> resp) {
                        Toast.makeText(GestionProductoActivity.this, "Producto borrado con éxito",
                                Toast.LENGTH_LONG).show();
                        nombreProducto.setText("");
                        descProducto.setText("");
                        precioProducto.setText("");
                        idProductoBuscar.setText("");
                        comboCategorias.setSelection(0);
                    }
                    @Override
                    public void onFailure(Call<Producto> call, Throwable t) {
                        Toast.makeText(GestionProductoActivity.this, "Error al borrar el producto",
                                Toast.LENGTH_LONG).show();
                    }
                });*/
            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GestionProductoActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

    }
}
