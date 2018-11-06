package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoConDetalles;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;
@Dao
public interface PedidoDao {
    @Query("SELECT * FROM Pedido")
    List<Pedido> getAll();

    @Insert
    long insert(Pedido p);

    @Update
    void update(Pedido p);

    @Delete
    void delete(Pedido p);

    @Query("SELECT * FROM Pedido WHERE idPedido = :pIdPedido")
    List<Pedido> buscarPorId(long pIdPedido);

    @Query("SELECT * FROM Pedido WHERE idPedido = :pIdPedido")
    List<PedidoConDetalles> buscarPorIdConDetalle(long pIdPedido);

    @Query("SELECT * FROM Pedido")
    List<PedidoConDetalles> buscarTodosConDetalle();
}
