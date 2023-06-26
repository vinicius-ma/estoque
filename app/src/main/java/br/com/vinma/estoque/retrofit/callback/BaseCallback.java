package br.com.vinma.estoque.retrofit.callback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class BaseCallback <T> implements Callback<T> {
    private final BaseResponseCallback<T> callback;

    public BaseCallback(BaseResponseCallback<T> callback) {
        this.callback = callback;
    }

    @Override
    @EverythingIsNonNull
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()){
            T body = response.body();
            if(body != null) callback.onSuccess(body);
        } else {
            callback.onFailure("Resposta falhou!");
        }
    }

    @Override
    @EverythingIsNonNull
    public void onFailure(Call<T> call, Throwable t) {
        callback.onFailure("Resposta falhou!");
    }

    public interface BaseResponseCallback <T> {
        void onSuccess(T body);
        void onFailure(String errorMessage);
    }
}
