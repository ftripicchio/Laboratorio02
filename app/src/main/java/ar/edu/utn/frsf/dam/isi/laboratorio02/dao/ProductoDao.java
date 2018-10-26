package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

@Dao
public interface ProductoDao {
    @Query("SELECT * FROM Producto")
    List<Categoria> getAll();

    @Insert
    long insert(Producto prod);

    @Update
    void update(Producto prod);

    @Delete
    void delete(Producto prod);
}
