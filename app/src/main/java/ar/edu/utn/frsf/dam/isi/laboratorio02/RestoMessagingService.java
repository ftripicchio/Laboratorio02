package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class RestoMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("mensaje","mensaje");
        if (remoteMessage.getData().size() > 0) {
            int idPedido = Integer.valueOf(remoteMessage.getData().get("ID_PEDIDO"));
            String estado = remoteMessage.getData().get("NUEVO_ESTADO");

            PedidoRepository repositorioPedido = new PedidoRepository();
            Pedido pedido = repositorioPedido.buscarPorId(idPedido);

            if(estado.toLowerCase().equals("realizado")){
                pedido.setEstado(Pedido.Estado.REALIZADO);

            }else if(estado.toLowerCase().equals("aceptado")){
                pedido.setEstado(Pedido.Estado.ACEPTADO);
                Intent i = new Intent();
                i.putExtra("idPedido",idPedido);
                i.setAction("ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_ACEPTADO");
                sendBroadcast(i);

            }else if(estado.toLowerCase().equals("rechazado")){
                pedido.setEstado(Pedido.Estado.RECHAZADO);

            }else if(estado.toLowerCase().equals("en_preparacion")){
                pedido.setEstado(Pedido.Estado.EN_PREPARACION);
                Intent i = new Intent();
                i.putExtra("idPedido",idPedido);
                i.setAction("ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_EN_PREPARACION");
                sendBroadcast(i);

            }else if(estado.toLowerCase().equals("listo")){
                pedido.setEstado(Pedido.Estado.LISTO);
                Intent i = new Intent();
                i.putExtra("idPedido",idPedido);
                i.setAction("ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_LISTO");
                sendBroadcast(i);

            }else if(estado.toLowerCase().equals("entregado")){
                pedido.setEstado(Pedido.Estado.ENTREGADO);

            }else if(estado.toLowerCase().equals("cancelado")){
                pedido.setEstado(Pedido.Estado.CANCELADO);
                Intent i = new Intent();
                i.putExtra("idPedido",idPedido);
                i.setAction("ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_CANCELADO");
                sendBroadcast(i);
            }
        }

    }
}
