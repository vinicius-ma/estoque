package br.com.vinma.estoque.ui.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;

import br.com.vinma.estoque.R;
import br.com.vinma.estoque.asynctask.BaseAsyncTask;
import br.com.vinma.estoque.database.EstoqueDatabase;
import br.com.vinma.estoque.database.dao.ProductDAO;
import br.com.vinma.estoque.model.Produto;

import br.com.vinma.estoque.retrofit.EstoqueRetrofit;
import br.com.vinma.estoque.ui.dialog.ProductEditDialog;
import br.com.vinma.estoque.ui.dialog.ProductSaveDialog;
import br.com.vinma.estoque.ui.recyclerview.adapter.ProductsListAdapter;
import retrofit2.Call;
import retrofit2.Response;

public class ProductListActivity extends AppCompatActivity {

    private static final String TITLE_APPBAR = "Lista de produtos";
    private ProductsListAdapter adapter;
    private ProductDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        setTitle(TITLE_APPBAR);

        configureProductsList();
        configureSaveProductFab();

        EstoqueDatabase db = EstoqueDatabase.getInstance(this);
        dao = db.getProductDAO();

        findProducts();
    }

    private void findProducts() {
        Call<List<Produto>> call = new EstoqueRetrofit().getProductService().findAll();
        new BaseAsyncTask<>(() ->{
            try {
                Response<List<Produto>> response = call.execute();
                List<Produto> productList = response.body();
                return productList;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }, productList -> {
                if(productList != null){
                    adapter.update(productList);
                } else{
                    toast("Não foi possível buscar os produtos do servidor!");
                }
        }).execute();

//        new BaseAsyncTask<>(dao::findAll,
//                result -> adapter.update(result))
//                .execute();
    }

    private void configureProductsList() {
        RecyclerView products = findViewById(R.id.activity_products_list_listview);
        adapter = new ProductsListAdapter(this, this::openEditProductForm);
        products.setAdapter(adapter);
        adapter.setOnItemClickRemoveContextMenuListener(this::remove);
    }

    private void remove(int position, Produto productRemoved) {
        new BaseAsyncTask<>(() -> {
            dao.remove(productRemoved);
            return null;
        }, result -> adapter.remove(position)
        ).execute();
    }

    private void configureSaveProductFab() {
        FloatingActionButton saveButton = findViewById(R.id.activity_product_list_new_product_fab);
        saveButton.setOnClickListener(v -> openSaveProductForm());
    }

    private void openSaveProductForm() {
        new ProductSaveDialog(this, this::save).show();
    }

    private void save(Produto product) {
        new BaseAsyncTask<>(() -> {
            long id = dao.save(product);
            return dao.findProductById(id);
        },
                productSaved ->adapter.add(productSaved)
        ).execute();
    }

    private void openEditProductForm(int position, Produto product) {
        new ProductEditDialog(this, product,
                productEdited -> edit(position, productEdited))
                .show();
    }

    private void edit(int position, Produto product) {
        new BaseAsyncTask<>(() -> {
            dao.update(product);
            return product;
        }, productEdited ->
                adapter.edit(position, productEdited))
                .execute();
    }

    private void toast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}
