package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;

public class EstadoPedidoReceiver extends BroadcastReceiver {

    private PedidoRepository pedidoRepository = new PedidoRepository();

    @Override
    public void onReceive(Context context, Intent intent) {
        int idPedido = intent.getExtras().getInt("idPedido");
        Pedido p = pedidoRepository.buscarPorId(idPedido);

        Intent pedidoDetalleIntent = new Intent(context, ActivityNewOrder.class);
        pedidoDetalleIntent.putExtra("idPedidoSeleccionado", idPedido);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(pedidoDetalleIntent);
        PendingIntent pedidoDetallePendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification();

        if (intent.getAction() == "ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_ACEPTADO"){
            notification = new NotificationCompat.Builder(context, "CANAL01")
                    .setSmallIcon(R.drawable.envio)
                    .setContentTitle("Tu Pedido fue aceptado")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("El costo será de $" + p.total() +
                                    "\nPrevisto el envío para " + p.getFecha().getHours() + ":" + p.getFecha().getMinutes()))
                    .setContentIntent(pedidoDetallePendingIntent)
                    .build();
        }
        else if (intent.getAction() == "ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_EN_PREPARACION"){
            notification = new NotificationCompat.Builder(context, "CANAL01")
                    .setSmallIcon(R.drawable.envio)
                    .setContentTitle("Tu Pedido está siendo preparado")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("El costo será de $" + p.total() +
                                    "\nPrevisto el envío para " + p.getFecha().getHours() + ":" + p.getFecha().getMinutes()))
                    .setContentIntent(pedidoDetallePendingIntent)
                    .build();
        }
        else if (intent.getAction() == "ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_LISTO"){
            notification = new NotificationCompat.Builder(context, "CANAL01")
                    .setSmallIcon(R.drawable.envio)
                    .setContentTitle("Tu Pedido está Listo")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("El costo será de $" + p.total() +
                                    "\nPrevisto el envío para " + p.getFecha().getHours() + ":" + p.getFecha().getMinutes()))
                    .setContentIntent(pedidoDetallePendingIntent)
                    .build();
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, notification);
    }
}
