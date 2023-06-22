package br.com.vinma.estoque.database.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import br.com.vinma.estoque.model.Product;

@Dao
public interface ProductDAO {

    @Insert
    long save(Product product);

    @Update
    void update(Product product);

    @Query("SELECT * FROM Product")
    List<Product> findAll();

    @Query("SELECT * FROM Product WHERE id = :id")
    Product findProductById(long id);

    @Delete
    void remove(Product product);
}
