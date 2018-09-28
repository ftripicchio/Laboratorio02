package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PedidoHolder {

    public TextView tvMailPedido;
    public TextView tvHoraEntrega;
    public TextView tvCantidadItems;
    public TextView tvPrecio;
    public TextView estado;
    public ImageView tipoEntrega;
    public Button btnCancelar;
    public Button btnVerDetalle;

    PedidoHolder(View base) {
        tvMailPedido = base.findViewById(R.id.historialContacto);
        tvHoraEntrega = base.findViewById(R.id.historialFechaEntrega);
        tvCantidadItems = base.findViewById(R.id.historialItems);
        tvPrecio = base.findViewById(R.id.historialAPagar);
        estado = base.findViewById(R.id.historialEstado);
        tipoEntrega = base.findViewById(R.id.historialTipoEntrega);
        btnCancelar = base.findViewById(R.id.btnHistorialCancelar);
        btnVerDetalle = base.findViewById(R.id.btnHistorialVerDetalle);
    }

}
