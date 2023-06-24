package br.com.vinma.estoque.retrofit.service;

import java.util.List;

import br.com.vinma.estoque.model.Produto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ProductService {
    @GET("produto")
    Call<List<Produto>> findAll();

    @POST("produto")
    Call<Produto> save(@Body Produto produto);
}
