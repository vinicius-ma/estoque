package br.com.vinma.estoque.database.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import br.com.vinma.estoque.model.Produto;

@Dao
public interface ProductDAO {

    @Insert
    long save(Produto product);

    @Update
    void update(Produto product);

    @Query("SELECT * FROM Produto")
    List<Produto> findAll();

    @Query("SELECT * FROM Produto WHERE id = :id")
    Produto findProductById(long id);

    @Delete
    void remove(Produto product);
}
