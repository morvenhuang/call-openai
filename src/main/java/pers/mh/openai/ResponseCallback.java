package pers.mh.openai;

import io.reactivex.FlowableEmitter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ResponseCallback implements Callback<ResponseBody> {
    private static final String RESPONSE_PREFIX = "data:";
    private static final String DONE_MARK = "[DONE]";
    private final FlowableEmitter<String> emitter;

    public ResponseCallback(FlowableEmitter<String> emitter) {
        this.emitter = emitter;
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (!response.isSuccessful()) {
            throw new HttpException(response);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream(), StandardCharsets.UTF_8))) {
            String line;
            while (!emitter.isCancelled() && (line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                if (!line.startsWith(RESPONSE_PREFIX)) {
                    throw new RuntimeException("Invalid response string: " + line);
                }
                //
                line = line.substring(5).trim();
                if (DONE_MARK.equals(line)) {
                    break;
                }
                emitter.onNext(line);
            }
            emitter.onComplete();
        } catch (Throwable t) {
            onFailure(call, t);
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        emitter.onError(t);
    }
}
