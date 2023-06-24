package br.com.vinma.estoque.database.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import br.com.vinma.estoque.model.Produto;

@Dao
public interface ProductDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long save(Produto product);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(List<Produto> products);

    @Update
    void update(Produto product);

    @Query("SELECT * FROM Produto")
    List<Produto> findAll();

    @Query("SELECT * FROM Produto WHERE id = :id")
    Produto findProductById(long id);

    @Delete
    void remove(Produto product);
}
