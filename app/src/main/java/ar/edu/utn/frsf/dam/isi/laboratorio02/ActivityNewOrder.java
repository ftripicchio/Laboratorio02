package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class ActivityNewOrder extends AppCompatActivity {

    private Pedido unPedido;
    private PedidoRepository repositorioPedido = new PedidoRepository();
    private ProductoRepository repositorioProducto = new ProductoRepository();

    private RadioGroup radioGroupEnvio;
    private RadioButton optEnvio, optRetiro;
    private EditText edtDireccion, editHora, editCorreo;
    private ListView items;
    private Button agregarProducto, hacerPedido;
    private TextView totalPedido;

    private ArrayAdapter<PedidoDetalle> itemsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        unPedido = new Pedido();

        optEnvio = findViewById(R.id.optPedidoEnviar);
        optRetiro = findViewById(R.id.optPedidoRetiro);

        edtDireccion = findViewById(R.id.edtPedidoDireccion);
        edtDireccion.setEnabled(false);
        editCorreo = findViewById(R.id.edtPedidoCorreo);
        editHora = findViewById(R.id.edtPedidoHoraEntrega);

        radioGroupEnvio = findViewById(R.id.optPedidoModoEntrega);
        radioGroupEnvio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.optPedidoRetiro: edtDireccion.setEnabled(false); break;
                    case R.id.optPedidoEnviar: edtDireccion.setEnabled(true); break;
                }
            }
        });

        items = findViewById(R.id.lstPedidoItems);
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, new ArrayList<PedidoDetalle>());
        items.setAdapter(itemsAdapter);

        agregarProducto = findViewById(R.id.btnPedidoAgregarProducto);
        agregarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityNewOrder.this, ActivityProductList.class);
                i.putExtra("NUEVO_PEDIDO", 1);
                startActivityForResult(i,1);


            }
        });

        totalPedido = findViewById(R.id.lblTotalPedido);

        hacerPedido = findViewById(R.id.btnPedidoHacerPedido);
        hacerPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Control Correo
                String correo = editCorreo.getText().toString();
                Log.v("correo", correo);
                if (correo.equals("")) {
                    Toast.makeText(ActivityNewOrder.this,"Debe ingresar un correo", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    unPedido.setMailContacto(correo);
                }

                //Control forma entrega
                if(optEnvio.isChecked() || optRetiro.isChecked()){
                    if(optEnvio.isChecked()){
                        unPedido.setRetirar(false);
                        //Control Dirección
                        String direccion = edtDireccion.getText().toString();
                        if (direccion.equals("")) {
                            Toast.makeText(ActivityNewOrder.this,"Debe ingresar una dirección para el envío a domicilio",
                                    Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            unPedido.setDireccionEnvio(direccion);
                        }
                    }
                    if(optRetiro.isChecked()) {
                        unPedido.setRetirar(true);
                    }
                } else {
                    Toast.makeText(ActivityNewOrder.this,"Debe seleccionar una forma de entrega", Toast.LENGTH_LONG).show();
                    return;
                }

                //Control Hora
                String horaIngresada = editHora.getText().toString();
                if (horaIngresada.equals("")) {
                    Toast.makeText(ActivityNewOrder.this,"Debe ingresar una hora de entrega", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    String[] horaIngresadaSplit = horaIngresada.split(":");
                    GregorianCalendar hora = new GregorianCalendar();
                    int valorHora = Integer.valueOf(horaIngresadaSplit[0]);
                    int valorMinuto = Integer.valueOf(horaIngresadaSplit[1]);
                    if(valorHora<0 || valorHora>23){
                        Toast.makeText(ActivityNewOrder.this,
                                "La hora ingresada ("+valorHora+") es incorrecta",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(valorMinuto <0 || valorMinuto >59){
                        Toast.makeText(ActivityNewOrder.this,
                                "Los minutos ("+valorMinuto+") son incorrectos",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    hora.set(Calendar.HOUR_OF_DAY,valorHora);
                    hora.set(Calendar.MINUTE,valorMinuto);
                    hora.set(Calendar.SECOND,Integer.valueOf(0));
                    unPedido.setFecha(hora.getTime());
                }

                //Control Lista Productos
                if(unPedido.getDetalle().size() == 0){
                    Toast.makeText(ActivityNewOrder.this,"Debe seleccionar al menos un producto", Toast.LENGTH_LONG).show();
                    return;
                }

                unPedido.setEstado(Pedido.Estado.REALIZADO);

                repositorioPedido.guardarPedido(unPedido);

                Intent i = new Intent(ActivityNewOrder.this, ActivityHistory.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            Integer idProducto = data.getExtras().getInt("idProducto");
            Integer cantidad = data.getExtras().getInt("cantidad");
            Producto producto = repositorioProducto.buscarPorId(idProducto);
            PedidoDetalle pedidoDetalle = new PedidoDetalle(cantidad, producto);
            pedidoDetalle.setPedido(unPedido);

            itemsAdapter.clear();
            itemsAdapter.addAll(unPedido.getDetalle());
            itemsAdapter.notifyDataSetChanged();

            Double total = calcularTotal(unPedido);
            totalPedido.setText("Total del pedido: $" + total);
        }
    }

    private Double calcularTotal(Pedido pedido) {
        Double total = 0.0;
        for(PedidoDetalle pedidoDetalle : pedido.getDetalle()){
            total += pedidoDetalle.getCantidad() * pedidoDetalle.getProducto().getPrecio();
        }
        return total;
    }
}
