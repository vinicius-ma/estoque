package br.com.vinma.estoque.ui.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import br.com.vinma.estoque.R;
import br.com.vinma.estoque.asynctask.BaseAsyncTask;
import br.com.vinma.estoque.database.EstoqueDatabase;
import br.com.vinma.estoque.database.dao.ProductDAO;
import br.com.vinma.estoque.model.Produto;

import br.com.vinma.estoque.repository.ProductRepository;
import br.com.vinma.estoque.ui.dialog.ProductEditDialog;
import br.com.vinma.estoque.ui.dialog.ProductSaveDialog;
import br.com.vinma.estoque.ui.recyclerview.adapter.ProductsListAdapter;

public class ProductListActivity extends AppCompatActivity {

    private static final String TITLE_APPBAR = "Lista de produtos";
    private ProductsListAdapter adapter;
    private ProductDAO dao;
    private ProductRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        setTitle(TITLE_APPBAR);

        configureProductsList();
        configureSaveProductFab();

        EstoqueDatabase db = EstoqueDatabase.getInstance(this);
        dao = db.getProductDAO();

        repository = new ProductRepository(dao);
        repository.findProducts(adapter::update);
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
        saveButton.setOnClickListener(v -> openFormSaveProduct());
    }

    private void openFormSaveProduct() {
        new ProductSaveDialog(this,
                productSaved -> repository.save(productSaved,
                        new ProductRepository.DataDownloadedCallback<Produto>() {
                            @Override
                            public void onSuccess(Produto savedProduct) {
                                adapter.add(savedProduct);
                                toast("Produto salvo com sucesso!");
                            }

                            @Override
                            public void onFailure(String error) {
                                toast("Falha ao salvar produto: " + error);
                            }
                        })).show();
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

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
