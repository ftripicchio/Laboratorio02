package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;

public class AppRepository {
    private static AppRepository _REPO = null;
    private CategoriaDao categoriaDao;

    private AppRepository(Context ctx) {
        AppDatabase db = Room.databaseBuilder(ctx,
                AppDatabase.class, "dam-pry-2018").fallbackToDestructiveMigration()
                .build();
        categoriaDao = db.categoriaDao();
    }

    public static AppRepository getInstance(Context ctx) {
        if (_REPO == null) _REPO = new AppRepository(ctx);
        return _REPO;
    }

    public void crearCategoria(Categoria c){
        categoriaDao.insert(c);
    }
    public void actualizarProyecto(Categoria c){
        categoriaDao.update(c);
    }
    public List<Categoria> getAll(){
        return categoriaDao.getAll();
    }
    public void eliminarCategoria(Categoria c){
        categoriaDao.delete(c);
    }
}