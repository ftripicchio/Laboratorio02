package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.AppRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRetrofit;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.RestClient;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoConDetalles;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Math.toIntExact;

public class ActivityNewOrder extends AppCompatActivity {

    private Pedido unPedido;
    private PedidoRepository repositorioPedido = new PedidoRepository();
    private ProductoRepository repositorioProducto = new ProductoRepository();
    private AppRepository appRepository;

    private RadioGroup radioGroupEnvio;
    private RadioButton optEnvio, optRetiro;
    private EditText edtDireccion, editHora, editCorreo;
    private ListView items;
    private Button agregarProducto, hacerPedido, quitarProducto, volver;
    private TextView totalPedido;

    private ArrayAdapter<PedidoDetalle> itemsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        appRepository = AppRepository.getInstance(this);

        optEnvio = findViewById(R.id.optPedidoEnviar);
        optRetiro = findViewById(R.id.optPedidoRetiro);
        edtDireccion = findViewById(R.id.edtPedidoDireccion);
        editCorreo = findViewById(R.id.edtPedidoCorreo);
        editHora = findViewById(R.id.edtPedidoHoraEntrega);
        radioGroupEnvio = findViewById(R.id.optPedidoModoEntrega);
        items = findViewById(R.id.lstPedidoItems);
        agregarProducto = findViewById(R.id.btnPedidoAgregarProducto);
        totalPedido = findViewById(R.id.lblTotalPedido);
        hacerPedido = findViewById(R.id.btnPedidoHacerPedido);
        quitarProducto = findViewById(R.id.btnPedidoQuitarProducto);
        volver = findViewById(R.id.btnPedidoVolver);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent i= getIntent();
        int idPedido = -1;
        if(i.getExtras()!=null){
            idPedido = i.getExtras().getInt("idPedidoSeleccionado");
        }
        if(idPedido>=0){
            final int idPedidoBuscar = idPedido;
            Runnable r1 = new Runnable() {
                @Override
                public void run() {
                    PedidoConDetalles pd = appRepository.getPedidoConDetalle(idPedidoBuscar).get(0);
                    unPedido = pd.getPedido();
                    unPedido.setDetalle(pd.getDetalles());
                    Log.d("detalle pedido", unPedido.getDetalle().toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            editCorreo.setText(unPedido.getMailContacto());
                            editCorreo.setEnabled(false);
                            edtDireccion.setText(unPedido.getDireccionEnvio());
                            edtDireccion.setEnabled(false);
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            editHora.setText(sdf.format(unPedido.getFecha()));
                            editHora.setEnabled(false);
                            optEnvio.setChecked(!unPedido.getRetirar());
                            optEnvio.setEnabled(false);
                            optRetiro.setChecked(unPedido.getRetirar());
                            optRetiro.setEnabled(false);
                            itemsAdapter = new ArrayAdapter<>(ActivityNewOrder.this, android.R.layout.simple_list_item_single_choice, unPedido.getDetalle());
                            items.setAdapter(itemsAdapter);
                            items.setEnabled(false);
                            Double total = unPedido.total();
                            totalPedido.setText("Total del pedido: $" + total);
                            agregarProducto.setEnabled(false);
                            hacerPedido.setEnabled(false);
                            quitarProducto.setEnabled(false);
                        }
                    });
                }
            };
            Thread t1 = new Thread(r1);
            t1.start();

        }else {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String correo = sharedPreferences.getString("correo","");
            Boolean retiro = sharedPreferences.getBoolean("retiro",false);

            unPedido = new Pedido();

            editCorreo.setText(correo);
            optRetiro.setChecked(retiro);
            optEnvio.setChecked(!retiro);

            if(retiro) edtDireccion.setEnabled(false);
            else edtDireccion.setEnabled(true);

            radioGroupEnvio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.optPedidoRetiro:
                            edtDireccion.setEnabled(false);
                            break;
                        case R.id.optPedidoEnviar:
                            edtDireccion.setEnabled(true);
                            break;
                    }
                }
            });

            itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, new ArrayList<PedidoDetalle>());
            items.setAdapter(itemsAdapter);


            agregarProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ActivityNewOrder.this, ActivityProductList.class);
                    i.putExtra("NUEVO_PEDIDO", 1);
                    startActivityForResult(i, 1);


                }
            });

            hacerPedido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Control Correo
                    String correo = editCorreo.getText().toString();
                    Log.v("correo", correo);
                    if (correo.equals("")) {
                        Toast.makeText(ActivityNewOrder.this, "Debe ingresar un correo", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        unPedido.setMailContacto(correo);
                    }

                    //Control forma entrega
                    if (optEnvio.isChecked() || optRetiro.isChecked()) {
                        if (optEnvio.isChecked()) {
                            unPedido.setRetirar(false);
                            //Control Dirección
                            String direccion = edtDireccion.getText().toString();
                            if (direccion.equals("")) {
                                Toast.makeText(ActivityNewOrder.this, "Debe ingresar una dirección para el envío a domicilio",
                                        Toast.LENGTH_LONG).show();
                                return;
                            } else {
                                unPedido.setDireccionEnvio(direccion);
                            }
                        }
                        if (optRetiro.isChecked()) {
                            unPedido.setRetirar(true);
                        }
                    } else {
                        Toast.makeText(ActivityNewOrder.this, "Debe seleccionar una forma de entrega", Toast.LENGTH_LONG).show();
                        return;
                    }

                    //Control Hora
                    String horaIngresada = editHora.getText().toString();
                    if (horaIngresada.equals("")) {
                        Toast.makeText(ActivityNewOrder.this, "Debe ingresar una hora de entrega", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        String[] horaIngresadaSplit = horaIngresada.split(":");
                        GregorianCalendar hora = new GregorianCalendar();
                        int valorHora = Integer.valueOf(horaIngresadaSplit[0]);
                        int valorMinuto = Integer.valueOf(horaIngresadaSplit[1]);
                        if (valorHora < 0 || valorHora > 23) {
                            Toast.makeText(ActivityNewOrder.this,
                                    "La hora ingresada (" + valorHora + ") es incorrecta",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (valorMinuto < 0 || valorMinuto > 59) {
                            Toast.makeText(ActivityNewOrder.this,
                                    "Los minutos (" + valorMinuto + ") son incorrectos",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        hora.set(Calendar.HOUR_OF_DAY, valorHora);
                        hora.set(Calendar.MINUTE, valorMinuto);
                        hora.set(Calendar.SECOND, Integer.valueOf(0));
                        unPedido.setFecha(hora.getTime());
                    }

                    //Control Lista Productos
                    if (unPedido.getDetalle().size() == 0) {
                        Toast.makeText(ActivityNewOrder.this, "Debe seleccionar al menos un producto", Toast.LENGTH_LONG).show();
                        return;
                    }

                    unPedido.setEstado(Pedido.Estado.REALIZADO);

                    //Hacer Pedido
                    Runnable r1 = new Runnable() {
                        @Override
                        public void run() {
                            long idPedido = appRepository.crearPedido(unPedido);
                            Pedido pedidoCreado = appRepository.buscarPedidoPorId(idPedido).get(0);
                            for(PedidoDetalle pd : unPedido.getDetalle()){
                                pd.setPedido(pedidoCreado);
                                appRepository.crearPedidoDetalle(pd);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ActivityNewOrder.this, "Pedido realizado con éxito",
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    };
                    Thread t1 = new Thread(r1);
                    t1.start();

                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.currentThread().sleep(10000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            // buscar pedidos no aceptados y aceptarlos utomáticamente
                            List<Pedido> lista = appRepository.getAllPedidos();
                            for(Pedido p:lista){
                                if(p.getEstado().equals(Pedido.Estado.REALIZADO)) {
                                    p.setEstado(Pedido.Estado.ACEPTADO);
                                    appRepository.actualizarPedido(p);

                                    Intent i = new Intent();
                                    i.putExtra("idPedido", p.getIdPedido());
                                    i.setAction("ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_ACEPTADO");
                                    sendBroadcast(i);
                                }
                            }/*
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ActivityNewOrder.this, "Informacion de pedidos actualizada!",
                                    Toast.LENGTH_LONG).show();
                                }
                            });*/
                        }
                    };
                    Thread unHilo = new Thread(r);
                    unHilo.start();


                    Intent i = new Intent(ActivityNewOrder.this, ActivityHistory.class);
                    startActivity(i);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Integer idProducto = data.getExtras().getInt("idProducto");
            final Integer cantidad = data.getExtras().getInt("cantidad");

            Runnable r1 = new Runnable() {
                @Override
                public void run() {
                    final Producto productoSeleccionado = appRepository.buscarProductoPorId(idProducto).get(0);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PedidoDetalle pedidoDetalle = new PedidoDetalle(cantidad, productoSeleccionado);
                            pedidoDetalle.setPedido(unPedido);

                            itemsAdapter.clear();
                            itemsAdapter.addAll(unPedido.getDetalle());
                            itemsAdapter.notifyDataSetChanged();

                            Double total = unPedido.total();
                            totalPedido.setText("Total del pedido: $" + total);
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
            Call<Producto> altaCall= clienteRest.buscarProductoPorId(idProducto);
            altaCall.enqueue(new Callback<Producto>() {
                @Override
                public void onResponse(Call<Producto> call,
                                       Response<Producto> resp) {
                    Producto producto = resp.body();
                    PedidoDetalle pedidoDetalle = new PedidoDetalle(cantidad, producto);
                    pedidoDetalle.setPedido(unPedido);

                    itemsAdapter.clear();
                    itemsAdapter.addAll(unPedido.getDetalle());
                    itemsAdapter.notifyDataSetChanged();

                    Double total = unPedido.total();
                    totalPedido.setText("Total del pedido: $" + total);
                }
                @Override
                public void onFailure(Call<Producto> call, Throwable t) {
                }
            });*/


        }
    }
}
