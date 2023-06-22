package br.com.vinma.estoque.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import br.com.vinma.estoque.R;
import br.com.vinma.estoque.asynctask.BaseAsyncTask;
import br.com.vinma.estoque.database.EstoqueDatabase;
import br.com.vinma.estoque.database.dao.ProductDAO;
import br.com.vinma.estoque.model.Product;
import br.com.vinma.estoque.ui.dialog.ProductEditDialog;
import br.com.vinma.estoque.ui.dialog.ProductSaveDialog;
import br.com.vinma.estoque.ui.recyclerview.adapter.ProductsListAdapter;

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
        new BaseAsyncTask<>(dao::findAll,
                result -> adapter.update(result))
                .execute();
    }

    private void configureProductsList() {
        RecyclerView products = findViewById(R.id.activity_products_list_listview);
        adapter = new ProductsListAdapter(this, this::openEditProductForm);
        products.setAdapter(adapter);
        adapter.setOnItemClickRemoveContextMenuListener(this::remove);
    }

    private void remove(int position, Product productRemoved) {
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

    private void save(Product product) {
        new BaseAsyncTask<>(() -> {
            long id = dao.save(product);
            return dao.findProductById(id);
        },
                productSaved ->adapter.add(productSaved)
        ).execute();
    }

    private void openEditProductForm(int position, Product product) {
        new ProductEditDialog(this, product,
                productEdited -> edit(position, productEdited))
                .show();
    }

    private void edit(int position, Product product) {
        new BaseAsyncTask<>(() -> {
            dao.update(product);
            return product;
        }, productEdited ->
                adapter.edit(position, productEdited))
                .execute();
    }


}
