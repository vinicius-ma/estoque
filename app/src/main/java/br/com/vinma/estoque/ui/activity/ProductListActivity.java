package br.com.vinma.estoque.ui.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import br.com.vinma.estoque.R;
import br.com.vinma.estoque.model.Produto;
import br.com.vinma.estoque.repository.ProductRepository;
import br.com.vinma.estoque.ui.dialog.ProductEditDialog;
import br.com.vinma.estoque.ui.dialog.ProductSaveDialog;
import br.com.vinma.estoque.ui.recyclerview.adapter.ProductsListAdapter;

public class ProductListActivity extends AppCompatActivity {

    private ProductsListAdapter adapter;
    private ProductRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        setTitle(getString(R.string.app_name));

        configureProductsList();
        configureSaveProductFab();

        repository = new ProductRepository(this);
        findProducts();
    }

    private void findProducts() {
        repository.findProducts(new ProductRepository.DataDownloadedCallback<List<Produto>>() {
            @Override
            public void onSuccess(List<Produto> result) {
                adapter.update(result);
            }

            @Override
            public void onFailure(int errorMessageId) {
                toast(R.string.activity_product_list_message_find_fail);
            }
        });
    }

    private void configureProductsList() {
        RecyclerView products = findViewById(R.id.activity_products_list_listview);
        adapter = new ProductsListAdapter(this, this::openEditProductForm);
        products.setAdapter(adapter);
        adapter.setOnItemClickRemoveContextMenuListener(this::remove);
    }

    private void configureSaveProductFab() {
        FloatingActionButton saveButton = findViewById(R.id.activity_product_list_new_product_fab);
        saveButton.setOnClickListener(v -> openFormSaveProduct());
    }

    private void openFormSaveProduct() {
        new ProductSaveDialog(this,this::save).show();
    }

    private void openEditProductForm(int position, Produto product) {
        new ProductEditDialog(this, product,
                productCreated -> edit(position, productCreated)).show();
    }

    private void save(Produto product) {
        repository.save(product, new ProductRepository.DataDownloadedCallback<Produto>() {
            @Override
            public void onSuccess(Produto savedProduct) {
                adapter.add(savedProduct);
                toast(R.string.activity_product_List_message_save_success);
            }

            @Override
            public void onFailure(int errorMessageId) {
                toast(R.string.activity_product_list_Message_save_fail);
            }
        });
    }

    private void edit(int position, Produto product) {
        repository.edit(product, new ProductRepository.DataDownloadedCallback<Produto>() {
            @Override
            public void onSuccess(Produto productEdited) {
                adapter.edit(position, productEdited);
                toast(R.string.activity_product_list_message_edit_success);
            }

            @Override
            public void onFailure(int errorMessageId) {
                toast(R.string.activity_product_list_message_edit_fail);
            }
        });
    }

    private void remove(int position, Produto product) {
        repository.remove(product, new ProductRepository.DataDownloadedCallback<Void>() {
            @Override
            public void onSuccess(Void body) {
                adapter.remove(position);
                toast(R.string.activity_product_list_message_remove_success);
            }

            @Override
            public void onFailure(int errorMessageId) {
                toast(R.string.activity_product_list_message_remove_fail);
            }
        });
    }

    private void toast(int stringId){
        toast(getString(stringId));
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
