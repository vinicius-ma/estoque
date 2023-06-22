package br.com.vinma.estoque.retrofit;

import br.com.vinma.estoque.retrofit.service.ProductService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EstoqueRetrofit {

    private final ProductService productService;

    public EstoqueRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.174:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        productService = retrofit.create(ProductService.class);
    }

    public ProductService getProductService() {
        return productService;
    }
}
