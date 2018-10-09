package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class EstadoPedidoReceiver extends BroadcastReceiver {

    private PedidoRepository pedidoRepository = new PedidoRepository();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == "ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_ACEPTADO"){
            Pedido p = pedidoRepository.buscarPorId(intent.getExtras().getInt("idPedido"));
            Toast.makeText(context, "Pedido para " + p.getMailContacto() + " ha cambiado de estado a ACEPTADO", Toast.LENGTH_LONG).show();
        }
    }
}
