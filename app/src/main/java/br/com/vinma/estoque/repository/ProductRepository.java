package br.com.vinma.estoque.repository;

import java.util.List;

import br.com.vinma.estoque.asynctask.BaseAsyncTask;
import br.com.vinma.estoque.database.dao.ProductDAO;
import br.com.vinma.estoque.model.Produto;
import br.com.vinma.estoque.retrofit.EstoqueRetrofit;
import br.com.vinma.estoque.retrofit.callback.BaseCallback;
import br.com.vinma.estoque.retrofit.service.ProductService;
import retrofit2.Call;

public class ProductRepository {

    private final ProductDAO dao;
    private final ProductService service;

    public ProductRepository(ProductDAO dao) {
        this.dao = dao;
        this.service = new EstoqueRetrofit().getProductService();
    }

    public void findProducts(DataDownloadedCallback<List<Produto>> callback) {
        findProductsInternal(callback);
    }

    private void findProductsInternal(DataDownloadedCallback<List<Produto>> callback) {
        new BaseAsyncTask<>(dao::findAll,
                result -> {
                    callback.onSuccess(result);
                    findProductsOnApi(callback);
                }).execute();
    }

    private void findProductsOnApi(DataDownloadedCallback<List<Produto>> callback) {
        Call<List<Produto>> call = service.findAll();
        call.enqueue(new BaseCallback<>(new BaseCallback.BaseResponseCallback<List<Produto>>() {
            @Override
            public void onSuccess(List<Produto> productsDownloaded) {
                updateInternal(productsDownloaded, callback);
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure(errorMessage);
            }
        }));
    }

    private void updateInternal(List<Produto> products,
                                DataDownloadedCallback<List<Produto>> callback) {
        new BaseAsyncTask<>(() -> {
                dao.save(products);
                return dao.findAll();
        }, callback::onSuccess).execute();
    }

    public void save(Produto product, DataDownloadedCallback<Produto> callback) {
        saveInApi(product, callback);
    }

    private void saveInApi(Produto product, DataDownloadedCallback<Produto> callback) {
        Call<Produto> call = service.save(product);
        call.enqueue(new BaseCallback<>(new BaseCallback.BaseResponseCallback<Produto>() {
            @Override
            public void onSuccess(Produto productReceived) {
                callback.onSuccess(productReceived);
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure(errorMessage);
            }
        }));
    }

    public void edit(Produto product, DataDownloadedCallback<Produto> callback) {

        Call<Produto> call = service.edit(product.getId(), product);
        call.enqueue(new BaseCallback<>(new BaseCallback.BaseResponseCallback<Produto>() {
            @Override
            public void onSuccess(Produto result) {
                new BaseAsyncTask<>(() -> {
                    dao.update(product);
                    return product;
                }, callback::onSuccess).execute();
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure(errorMessage);
            }
        }));
    }

    private void saveInternal(Produto productReceived, DataDownloadedCallback<Produto> callback) {
        new BaseAsyncTask<>(() -> {
            long id = dao.save(productReceived);
            return dao.findProductById(id);
        }, callback::onSuccess)
                .execute();
    }

    public interface DataDownloadedCallback<T> {
        void onSuccess(T result);
        void onFailure(String error);
    }
}
