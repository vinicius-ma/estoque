package br.com.vinma.estoque.retrofit;

import androidx.annotation.NonNull;

import br.com.vinma.estoque.retrofit.service.ProductService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EstoqueRetrofit {

    private static final String BASE_URL = "http://192.168.0.174:8080/";
    private final ProductService productService;

    public EstoqueRetrofit() {

        OkHttpClient client = configClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        productService = retrofit.create(ProductService.class);
    }

    @NonNull
    private static OkHttpClient configClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        return client;
    }

    public ProductService getProductService() {
        return productService;
    }
}
