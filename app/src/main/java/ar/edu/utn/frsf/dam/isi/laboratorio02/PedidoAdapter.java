package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;

public class PedidoAdapter extends ArrayAdapter<Pedido> {

    private Context ctx;
    private List<Pedido> datos;

    public PedidoAdapter(Context context, List<Pedido> objects) {
        super(context, 0, objects);
        this.ctx = context;
        this.datos = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View filaHistorial = convertView;
        if (filaHistorial == null) {
            filaHistorial = LayoutInflater.from(this.ctx).inflate(R.layout.fila_historial, parent, false);
        }
        PedidoHolder holder = (PedidoHolder) filaHistorial.getTag();
        if (holder == null) {
            holder = new PedidoHolder(filaHistorial);

            holder.btnCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int indice = (int) v.getTag();
                    Pedido pedidoSeleccionado = datos.get(indice);
                    if( pedidoSeleccionado.getEstado().equals(Pedido.Estado.REALIZADO)||
                            pedidoSeleccionado.getEstado().equals(Pedido.Estado.ACEPTADO)||
                            pedidoSeleccionado.getEstado().equals(Pedido.Estado.EN_PREPARACION)){
                        pedidoSeleccionado.setEstado(Pedido.Estado.CANCELADO);
                        PedidoAdapter.this.notifyDataSetChanged();
                        return;
                    }
                }
            });

            filaHistorial.setTag(holder);
        }

        Pedido pedido = this.datos.get(position);

        holder.tvMailPedido.setText("Contacto: " + pedido.getMailContacto());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        holder.tvHoraEntrega.setText("Fecha de Entrega: " + sdf.format(pedido.getFecha()));
        holder.tvCantidadItems.setText("Items: " + pedido.getDetalle().size());
        holder.tvPrecio.setText("  A pagar: $" + calcularTotal(pedido));
        holder.estado.setText("Estado: " + pedido.getEstado());

        if(pedido.getRetirar()){
            holder.tipoEntrega.setImageResource(R.drawable.retira);
        } else {
            holder.tipoEntrega.setImageResource(R.drawable.envio);
        }

        switch (pedido.getEstado()){
            case LISTO:
                holder.estado.setTextColor(Color.DKGRAY);
                break;
            case ENTREGADO:
                holder.estado.setTextColor(Color.BLUE);
                break;
            case CANCELADO:
            case RECHAZADO:
                holder.estado.setTextColor(Color.RED);
                break;
            case ACEPTADO:
                holder.estado.setTextColor(Color.GREEN);
                break;
            case EN_PREPARACION:
                holder.estado.setTextColor(Color.MAGENTA);
                break;
            case REALIZADO:
                holder.estado.setTextColor(Color.BLUE);
                break;
        }

        holder.btnCancelar.setTag(position);

        return filaHistorial;
    }

    private Double calcularTotal(Pedido pedido) {
        Double total = 0.0;
        for (PedidoDetalle pedidoDetalle : pedido.getDetalle()) {
            total += pedidoDetalle.getCantidad() * pedidoDetalle.getProducto().getPrecio();
        }
        return total;
    }
}
