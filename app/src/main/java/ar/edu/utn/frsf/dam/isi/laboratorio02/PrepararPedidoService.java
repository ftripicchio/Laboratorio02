package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.AppRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class PrepararPedidoService extends IntentService {
    AppRepository appRepository;

    public PrepararPedidoService() {
        super("PrepararPedidoService");
    }

    @Override
    protected void onHandleIntent(Intent intent){
        appRepository = AppRepository.getInstance(this);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // buscar pedidos aceptados y prepararlos

                List<Pedido> lista = appRepository.getAllPedidos();
                for(Pedido p:lista){
                    if(p.getEstado().equals(Pedido.Estado.ACEPTADO))
                        p.setEstado(Pedido.Estado.EN_PREPARACION);
                        appRepository.actualizarPedido(p);

                    Intent i = new Intent();
                    i.putExtra("idPedido",p.getIdPedido());
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
