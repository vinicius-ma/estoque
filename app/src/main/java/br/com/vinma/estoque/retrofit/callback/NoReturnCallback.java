package br.com.vinma.estoque.retrofit.callback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class NoReturnCallback implements Callback<Void> {

    private final ResponseCallback callback;

    public NoReturnCallback(ResponseCallback callback) {
        this.callback = callback;
    }

    @Override
    @EverythingIsNonNull
    public void onResponse(Call<Void> call, Response<Void> response) {
        if (response.isSuccessful()){
            callback.onSuccess();
        } else {
            callback.onFailure("Resposta falhou!");
        }
    }

    @Override
    @EverythingIsNonNull
    public void onFailure(Call<Void> call, Throwable t) {
        callback.onFailure("Resposta falhou!");
    }

    public interface ResponseCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }
}
