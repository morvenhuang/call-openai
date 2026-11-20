package pers.mh.openai;

import pers.mh.openai.auth.AuthenticationInterceptor;
import pers.mh.openai.chat.ChatCompletionRequest;
import pers.mh.openai.chat.ChatCompletionResponse;
import pers.mh.openai.embedding.Embedding;
import pers.mh.openai.embedding.EmbeddingRequest;
import pers.mh.openai.model.Model;
import pers.mh.openai.util.JsonHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class OpenAIClient {
    private static final Logger logger = LoggerFactory.getLogger(OpenAIClient.class);
    private static final ObjectMapper JSON_MAPPER = JsonHelper.defaultObjectMapper();
    private final OpenAIHttpWrapper openAIHttpWrapper;
    private final ExecutorService executorService;

    private OpenAIClient(String key, String baseUrl, Long timeoutMs, Integer poolMaxIdleConnections, Long poolKeepAliveMs) {
        OkHttpClient okHttpClient = getOkHttpClient(key, timeoutMs, poolMaxIdleConnections, poolKeepAliveMs);
        Retrofit retrofit = getRetrofit(baseUrl, okHttpClient);
        this.openAIHttpWrapper = retrofit.create(OpenAIHttpWrapper.class);
        this.executorService = okHttpClient.dispatcher().executorService();
    }

    public void shutdownExecutor() {
        this.executorService.shutdown();
    }

    public List<Model> listModels() {
        return execute(openAIHttpWrapper.listModels()).data;
    }

    public List<Embedding> callEmbedding(EmbeddingRequest request) {
        return execute(openAIHttpWrapper.callEmbeddings(request)).data;
    }

    public ChatCompletionResponse callChatCompletion(ChatCompletionRequest request) {
        return execute(openAIHttpWrapper.callChatCompletion(request));
    }

    public Flowable<ChatCompletionResponse> callChatCompletionStream(ChatCompletionRequest request) {
        request.setStream(true);
        return stream(openAIHttpWrapper.callChatCompletionStream(request), ChatCompletionResponse.class);
    }

    private <T> T execute(Single<T> response) {
        try {
            return response.blockingGet();
        } catch (HttpException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get data from response");
        }
    }

    private Flowable<String> stream(Call<ResponseBody> response) {
        return Flowable.create(emitter -> response.enqueue(new ResponseCallback(emitter)), BackpressureStrategy.BUFFER);
    }

    private <T> Flowable<T> stream(Call<ResponseBody> response, Class<T> clazz) {
        return stream(response).map(sse -> JSON_MAPPER.readValue(sse, clazz));
    }

    private Retrofit getRetrofit(String baseUrl, OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(JSON_MAPPER))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private OkHttpClient getOkHttpClient(String key, Long timeoutMs, int poolMaxIdleConnections, long poolKeepAliveMs) {
        return new OkHttpClient.Builder()
                .addInterceptor(new AuthenticationInterceptor(key))
                .connectionPool(new ConnectionPool(poolMaxIdleConnections, poolKeepAliveMs, TimeUnit.MILLISECONDS))
                .readTimeout(timeoutMs, TimeUnit.MILLISECONDS)
                .build();
    }

    public static class Builder {
        private static final String DEFAULT_BASE_URL = "https://api.openai.com/";
        private static final Long DEFAULT_TIMEOUT_MS = 10000L;
        private static final Integer DEFAULT_POOL_MAX_IDLE_CONNECTIONS = 5;
        private static final Long DEFAULT_POOL_KEEP_ALIVE_MS = 1000L;

        private String key;
        private String baseUrl;
        private Long timeoutMs;
        private Integer poolMaxIdleConnections;
        private Long poolKeepAliveMs;

        public Builder setKey(String key) {
            this.key = key;
            return this;
        }

        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder setTimeoutMs(Long timeoutMs) {
            this.timeoutMs = timeoutMs;
            return this;
        }

        public Builder setPoolMaxIdleConnections(Integer poolMaxIdleConnections) {
            this.poolMaxIdleConnections = poolMaxIdleConnections;
            return this;
        }

        public Builder setPoolKeepAliveMs(Long poolKeepAliveMs) {
            this.poolKeepAliveMs = poolKeepAliveMs;
            return this;
        }

        public OpenAIClient build() {
            if (key == null || key.isEmpty()) {
                throw new RuntimeException("Missing OpenAI API Key setting");
            }
            String trueBaseUrl = baseUrl == null ? DEFAULT_BASE_URL : baseUrl;
            Long trueTimeoutMs = timeoutMs == null ? DEFAULT_TIMEOUT_MS : timeoutMs;
            Integer truePoolMaxIdleConnections = poolMaxIdleConnections == null ? DEFAULT_POOL_MAX_IDLE_CONNECTIONS : poolMaxIdleConnections;
            Long truePoolKeepAliveMs = poolKeepAliveMs == null ? DEFAULT_POOL_KEEP_ALIVE_MS : poolKeepAliveMs;
            logger.info("Build OpenAI Client: Base URL = {}, Timeout = {}, Pool Max Idle Connections = {}, Pool Keep Alive Millisecond = {}",
                    trueBaseUrl,
                    trueTimeoutMs,
                    truePoolMaxIdleConnections,
                    truePoolKeepAliveMs);
            return new OpenAIClient(key,
                    trueBaseUrl,
                    trueTimeoutMs,
                    truePoolMaxIdleConnections,
                    truePoolKeepAliveMs);
        }
    }
}
