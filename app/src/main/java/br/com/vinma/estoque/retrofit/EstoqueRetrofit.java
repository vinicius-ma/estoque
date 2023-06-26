package br.com.vinma.estoque.retrofit;

import br.com.vinma.estoque.retrofit.service.ProductService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EstoqueRetrofit {

    private final ProductService productService;

    public EstoqueRetrofit() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.174:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        productService = retrofit.create(ProductService.class);
    }

    public ProductService getProductService() {
        return productService;
    }
}
