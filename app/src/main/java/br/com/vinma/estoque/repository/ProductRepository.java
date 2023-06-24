package br.com.vinma.estoque.repository;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import br.com.vinma.estoque.asynctask.BaseAsyncTask;
import br.com.vinma.estoque.database.EstoqueDatabase;
import br.com.vinma.estoque.database.dao.ProductDAO;
import br.com.vinma.estoque.model.Produto;
import br.com.vinma.estoque.retrofit.EstoqueRetrofit;
import br.com.vinma.estoque.retrofit.service.ProductService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class ProductRepository {

    private final ProductDAO dao;
    private final ProductService service;

    public ProductRepository(ProductDAO dao) {
        this.dao = dao;
        this.service = new EstoqueRetrofit().getProductService();
    }

    public void findProducts(DataDownloadedListener<List<Produto>> listener) {
        findProductsInternal(listener);
    }

    private void findProductsInternal(DataDownloadedListener<List<Produto>> listener) {
        new BaseAsyncTask<>(dao::findAll,
                result -> {
                    listener.onDownloaded(result);
                    findProductsOnApi(listener);
                }).execute();
    }

    private void findProductsOnApi(DataDownloadedListener<List<Produto>> listener) {
        Call<List<Produto>> call = new EstoqueRetrofit().getProductService().findAll();
        new BaseAsyncTask<>(() ->{
            try {
                Response<List<Produto>> response = call.execute();
                List<Produto> productList = response.body();
                dao.save(productList);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return dao.findAll();
        }, listener::onDownloaded).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void save(Produto product, DataDownloadedCallback<Produto> callback) {
        saveInApi(product, callback);
    }

    private void saveInApi(Produto product, DataDownloadedCallback<Produto> callback) {
        Call<Produto> call = service.save(product);
        call.enqueue(new Callback<Produto>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Produto> call, Response<Produto> response) {
                Produto productReceived = response.body();
                if(response.isSuccessful()) {
                    if (productReceived != null) saveInternal(productReceived, callback);
                } else callback.onFailure("Falha no recebimento dos dados");
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<Produto> call, Throwable t) {
                callback.onFailure("Falha na comunicação: " + t.getMessage());
            }
        });
    }

    private void saveInternal(Produto productReceived, DataDownloadedCallback<Produto> callback) {
        new BaseAsyncTask<>(() -> {
            long id = dao.save(productReceived);
            return dao.findProductById(id);
        }, callback::onSuccess)
                .execute();
    }

    public interface DataDownloadedListener<T> {
        void onDownloaded(T result);
    }

    public interface DataDownloadedCallback<T> {
        void onSuccess(T result);
        void onFailure(String error);
    }
}
