package ar.edu.utn.frsf.dam.isi.laboratorio02.modelo;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class PedidoDetalle {

    private static int ID_DETALLE =1;
    @PrimaryKey(autoGenerate = true)
    private Integer idDetalle;
    private Integer cantidad;
    @Embedded
    private Producto producto;
    @Embedded
    private Pedido pedido;

    public PedidoDetalle(){}

    public PedidoDetalle(Integer cantidad, Producto producto) {
        this.cantidad = cantidad;
        this.producto = producto;
    }

    public Integer getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
        pedido.agregarDetalle(this);
    }

    @Override
    public String toString() {
        return producto.getNombre() + "( $"+producto.getPrecio()+")"+ cantidad;
    }
}
