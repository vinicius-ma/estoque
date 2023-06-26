package br.com.vinma.estoque.retrofit.callback;

import br.com.vinma.estoque.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class BaseCallback <T> implements Callback<T> {
    private final ResponseCallback<T> callback;

    public BaseCallback(ResponseCallback<T> callback) {
        this.callback = callback;
    }

    @Override
    @EverythingIsNonNull
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()){
            T body = response.body();
            if(body != null) callback.onSuccess(body);
        } else {
            callback.onFailure(R.string.retrofit_callback_onResponse_fail);
        }
    }

    @Override
    @EverythingIsNonNull
    public void onFailure(Call<T> call, Throwable t) {
        callback.onFailure(R.string.retrofit_callback_onFailure);
    }

    public interface ResponseCallback<T> {
        void onSuccess(T body);
        void onFailure(int errorMessageId);
    }
}
