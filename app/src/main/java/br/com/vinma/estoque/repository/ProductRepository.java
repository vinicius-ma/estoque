package br.com.vinma.estoque.repository;

import android.app.Activity;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

import br.com.vinma.estoque.asynctask.BaseAsyncTask;
import br.com.vinma.estoque.database.EstoqueDatabase;
import br.com.vinma.estoque.database.dao.ProductDAO;
import br.com.vinma.estoque.model.Produto;
import br.com.vinma.estoque.retrofit.EstoqueRetrofit;
import retrofit2.Call;
import retrofit2.Response;

public class ProductRepository {

    private final ProductDAO dao;

    public ProductRepository(Activity activity) {
        this.dao = EstoqueDatabase.getInstance(activity).getProductDAO();
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

    public void save(Produto product, DataDownloadedListener<Produto> listener) {
        new BaseAsyncTask<>(() -> {
            long id = dao.save(product);
            return dao.findProductById(id);
        }, listener::onDownloaded).execute();
    }

    public interface DataDownloadedListener<T> {
        void onDownloaded(T result);
    }
}
