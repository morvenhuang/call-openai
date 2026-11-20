package pers.mh.openai;

import pers.mh.openai.OpenAIClient.Builder;
import pers.mh.openai.chat.ChatCompletionRequest;
import pers.mh.openai.chat.ChatCompletionResponse;
import pers.mh.openai.chat.ChatMessage;
import pers.mh.openai.chat.enums.ChatRole;
import pers.mh.openai.embedding.Embedding;
import pers.mh.openai.embedding.EmbeddingRequest;
import pers.mh.openai.model.Model;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class OpenAIClientTest {

    @Test
    public void test_list_models() {
        String key = "huangmaoyang";
        String baseUrl = "http://104.160.47.31:8080/";
        OpenAIClient openAIClient = new Builder().setBaseUrl(baseUrl).setKey(key).build();
        List<Model> models = openAIClient.listModels();
        for (Model model : models) {
            System.out.println(model.id + " " + model.root + " " + model.parent);
        }
    }

    @Test
    public void test_stream_chat() {
        String key = "huangmaoyang";
        String baseUrl = "http://104.160.47.31:8080/";
        OpenAIClient openAIClient = new Builder().setBaseUrl(baseUrl).setKey(key).build();
        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = new ChatMessage(ChatRole.SYSTEM.value(), "You are a dog.");
        final ChatMessage userMessage = new ChatMessage(ChatRole.USER.value(), "Hello.");
        messages.add(systemMessage);
        messages.add(userMessage);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .n(1)
                .maxTokens(50)
                .logitBias(new HashMap<>())
                .build();

        openAIClient.callChatCompletionStream(chatCompletionRequest)
                .doOnError(Throwable::printStackTrace)
                .blockingForEach(System.out::println);

        openAIClient.shutdownExecutor();
    }

    @Test
    public void test_non_stream_chat() {
        String key = "huangmaoyang";
        String baseUrl = "http://104.160.47.31:8080/";
        OpenAIClient openAIClient = new Builder().setBaseUrl(baseUrl).setKey(key).build();
        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = new ChatMessage(ChatRole.SYSTEM.value(), "You are a dog.");
        final ChatMessage userMessage = new ChatMessage(ChatRole.USER.value(), "Hello.");
        messages.add(systemMessage);
        messages.add(userMessage);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .n(1)
                .maxTokens(50)
                .logitBias(new HashMap<>())
                .build();

        ChatCompletionResponse response = openAIClient.callChatCompletion(chatCompletionRequest);
        System.out.println(response); // output: Woof! Hello, human! How can I assist you today?

        openAIClient.shutdownExecutor();
    }

    @Test
    public void test_embedding() {
        String key = "huangmaoyang";
        String baseUrl = "http://104.160.47.31:8080/";
        OpenAIClient openAIClient = new Builder().setBaseUrl(baseUrl).setKey(key).build();

        //List<String> input = Arrays.asList("i have no idea", "i don't know", "let me make some more", "how to calculate the similarity of 2 vectors");
        List<String> input = Arrays.asList("China", "Beijing", "France", "Paris");
        // text-embedding-ada-002 dimensions 1536
        EmbeddingRequest embeddingRequest = EmbeddingRequest.builder().model("text-embedding-ada-002").input(input).build();
        List<Embedding> response = openAIClient.callEmbedding(embeddingRequest);
        /*for(Embedding embedding : response){
            for(Double v : embedding.getEmbedding()){
                System.out.println(v);
            }
            //String joined = embedding.getEmbedding().stream().map(String::valueOf).collect(Collectors.joining(", "));
            //System.out.println(joined);
            System.out.println("-------------------------");
        }*/

        double chinaBeijing = calculateCosineSimilarity(response.get(0).getEmbedding(), response.get(1).getEmbedding());
        double franceParis = calculateCosineSimilarity(response.get(2).getEmbedding(), response.get(3).getEmbedding());
        double chinaParis = calculateCosineSimilarity(response.get(0).getEmbedding(), response.get(3).getEmbedding());
        double franceBeijing = calculateCosineSimilarity(response.get(2).getEmbedding(), response.get(1).getEmbedding());
        System.out.println("China-Beijing: " + chinaBeijing);
        System.out.println("France-Paris: " + franceParis);
        System.out.println("China-Paris: " + chinaParis);
        System.out.println("France-Beijing: " + franceBeijing);
    }

    private static double calculateCosineSimilarity(List<Double> vector1, List<Double> vector2) {
        if (vector1.size() != vector2.size()) {
            throw new IllegalArgumentException("Vector dimensions don't match");
        }

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < vector1.size(); i++) {
            dotProduct += vector1.get(i) * vector2.get(i);
            normA += Math.pow(vector1.get(i), 2);
            normB += Math.pow(vector2.get(i), 2);
        }

        if (normA == 0.0 || normB == 0.0) {
            throw new IllegalArgumentException("One or both vectors have zero magnitude");
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
