package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoConDetalles;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class AppRepository {
    private static AppRepository _REPO = null;
    private CategoriaDao categoriaDao;
    private ProductoDao productoDao;
    private PedidoDao pedidoDao;
    private PedidoDetalleDao pedidoDetalleDao;

    private AppRepository(Context ctx) {
        AppDatabase db = Room.databaseBuilder(ctx,
                AppDatabase.class, "dam-pry-2018").fallbackToDestructiveMigration()
                .build();
        categoriaDao = db.categoriaDao();
        productoDao = db.productoDao();
        pedidoDao = db.pedidoDao();
        pedidoDetalleDao = db.pedidoDetalleDao();
    }

    public static AppRepository getInstance(Context ctx) {
        if (_REPO == null) _REPO = new AppRepository(ctx);
        return _REPO;
    }

    public void crearCategoria(Categoria c){
        categoriaDao.insert(c);
    }
    public void actualizarCategoria(Categoria c){
        categoriaDao.update(c);
    }
    public List<Categoria> getAllCategorias(){
        return categoriaDao.getAll();
    }
    public void eliminarCategoria(Categoria c){
        categoriaDao.delete(c);
    }

    public void crearProducto(Producto p){
        productoDao.insert(p);
    }
    public void actualizarProducto(Producto p){
        productoDao.update(p);
    }
    public List<Producto> getAllProductos(){
        return productoDao.getAll();
    }
    public void eliminarProducto(Producto p){
        productoDao.delete(p);
    }
    public List<Producto> buscarProductoPorId (long idProducto){ return productoDao.buscarProductoPorId(idProducto);}

    public long crearPedido(Pedido p){
        return pedidoDao.insert(p);
    }
    public void actualizarPedido(Pedido p){
        pedidoDao.update(p);
    }
    public List<Pedido> getAllPedidos(){
        return pedidoDao.getAll();
    }
    public List<Pedido> buscarPedidoPorId(long id) {return pedidoDao.buscarPorId(id);}
    public void eliminarPedido(Pedido p){
        pedidoDao.delete(p);
    }
    public List<PedidoConDetalles> getPedidoConDetalle(long idPedido){
        return pedidoDao.buscarPorIdConDetalle(idPedido);
    }
    public List<PedidoConDetalles> getPedidosConDetalle(){
        return pedidoDao.buscarTodosConDetalle();
    }

    public void crearPedidoDetalle(PedidoDetalle pd){
        pedidoDetalleDao.insert(pd);
    }
    public void actualizarPedidoDetalle(PedidoDetalle pd){
        pedidoDetalleDao.update(pd);
    }
    public List<PedidoDetalle> getAllPedidoDetalle(){
        return pedidoDetalleDao.getAll();
    }
    public void eliminarPedidoDetalle(PedidoDetalle pd){
        pedidoDetalleDao.delete(pd);
    }
}