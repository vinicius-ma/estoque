package br.com.vinma.estoque.retrofit.service;

import java.util.List;

import br.com.vinma.estoque.model.Produto;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductService {
    @GET("produto")
    Call<List<Produto>> findAll();
}
