package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class PrepararPedidoService extends IntentService {
    public PrepararPedidoService() {
        super("PrepararPedidoService");
    }

    @Override
    protected void onHandleIntent(Intent intent){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // buscar pedidos aceptados y prepararlos
                PedidoRepository repositorioPedido = new PedidoRepository();
                List<Pedido> lista = repositorioPedido.getLista();
                for(Pedido p:lista){
                    if(p.getEstado().equals(Pedido.Estado.ACEPTADO))
                        p.setEstado(Pedido.Estado.EN_PREPARACION);

                    Intent i = new Intent();
                    i.putExtra("idPedido",p.getId());
                    i.setAction("ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_EN_PREPARACION");
                    sendBroadcast(i);
                }
            }
        };
        Thread unHilo = new Thread(r);
        unHilo.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
