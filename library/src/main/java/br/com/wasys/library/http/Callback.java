package br.com.wasys.library.http;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;

import br.com.wasys.library.enumerator.HttpStatus;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by pascke on 03/08/16.
 */
public abstract class Callback<T extends Result> implements retrofit2.Callback<T> {

    private static final String TAG = Callback.class.getSimpleName();

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        int code = response.code();
        HttpStatus status = HttpStatus.valueOf(code);
        if (status.is2xxSuccessful()) {
            T result = response.body();
            if (Result.Status.SUCCESS.equals(result.status)) {
                onSuccess(result);
            }
            else {
                Error error = new Error();
                error.messages = result.messages;
                onError(error);
            }
        }
        else if (status.is4xxClientError() || status.is5xxServerError()) {
            Error error = new Error();
            ResponseBody errorBody = response.errorBody();
            try {
                String responseText = errorBody.string();
                if (StringUtils.isNotBlank(responseText)) {
                    error.addMessage(responseText);
                }
            } catch (IOException e) {
                Log.e(TAG, "Error getting responseText!", e);
            }
            onError(error);
        }
        else {
            throw new IllegalStateException("Status series [" + status.series().name() + "] not implemented!");
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable throwable) {
        Log.e(TAG, "Communication failure!", throwable);
        onFailure(throwable);
    }

    public void onFailure(Throwable throwable) {
        String message = null;
        if (throwable != null){
            if (throwable instanceof SocketTimeoutException) {
                message = "Request Timeout.";
            }
            else {
                message = throwable.getMessage();
                Throwable root = ExceptionUtils.getRootCause(throwable);
                if (root != null) {
                    message = root.getMessage();
                }
            }
        }
        if (StringUtils.isEmpty(message)) {
            message = "Unknown error.";
        }
        Error error = new Error();
        error.addMessage(message);
        onError(error);
    }

    public abstract void onSuccess(T result);
    public abstract void onError(Error error);
}