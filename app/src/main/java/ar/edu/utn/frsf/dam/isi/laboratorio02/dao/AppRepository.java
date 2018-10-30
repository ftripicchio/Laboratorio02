package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class AppRepository {
    private static AppRepository _REPO = null;
    private CategoriaDao categoriaDao;
    private ProductoDao productoDao;

    private AppRepository(Context ctx) {
        AppDatabase db = Room.databaseBuilder(ctx,
                AppDatabase.class, "dam-pry-2018").fallbackToDestructiveMigration()
                .build();
        categoriaDao = db.categoriaDao();
        productoDao = db.productoDao();
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
    public List<Categoria> getAllProductos(){
        return productoDao.getAll();
    }
    public void eliminarProducto(Producto p){
        productoDao.delete(p);
    }
}