package pers.mh.openai;

import pers.mh.openai.chat.ChatCompletionRequest;
import pers.mh.openai.chat.ChatCompletionResponse;
import pers.mh.openai.embedding.Embedding;
import pers.mh.openai.embedding.EmbeddingRequest;
import pers.mh.openai.model.Model;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;

public interface OpenAIHttpWrapper {

    @GET("v1/models")
    Single<OpenAICommonResponse<Model>> listModels();

    @POST("/v1/chat/completions")
    Single<ChatCompletionResponse> callChatCompletion(@Body ChatCompletionRequest request);

    @Streaming
    @POST("/v1/chat/completions")
    Call<ResponseBody> callChatCompletionStream(@Body ChatCompletionRequest request);

    @POST("/v1/embeddings")
    Single<OpenAICommonResponse<Embedding>> callEmbeddings(@Body EmbeddingRequest request);

}
