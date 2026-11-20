package pers.mh.openai.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatCompletionRequest {

    String model;

    List<ChatMessage> messages;

    /**
     * 0 ~ 2. Default 1. <br>
     * 取样温度。该值越高，答案的随机性也越高，或者说答案的创新程度（可能）也越高。
     */
    Double temperature;

    /**
     * 0 ~ 1. Default 1. <br>
     * 与取样温度类似，数值越大，答案的随机性也越高，通常不建议同时更改temperature与topP。
     */
    @JsonProperty("top_p")
    Double topP;

    /**
     * Default 1. <br>
     * 返回多少个备选答案。
     */
    Integer n;

    /**
     * Default false. <br>
     * 是否是有流式方式返回答案，如是，则以server-sent-events模式逐字返回，并以[DONE]消息标志流的结束。
     */
    Boolean stream;

    /**
     * 可以指定最多4个单词，当模型在输出时，如果遇到这些单词，则会终止输出。
     */
    List<String> stop;

    /**
     * 模型返回的最大token数量，默认不做限制。
     */
    @JsonProperty("max_tokens")
    Integer maxTokens;

    /**
     * -2.0 ~ 2.0. Default 0. <br>
     * 存在惩罚，根据单词在本次答案中是否存在进行惩罚，数值越高，答案中出现相同单词的概率越低。
     */
    @JsonProperty("presence_penalty")
    Double presencePenalty;

    /**
     * -2.0 ~ 2.0. Default 0. <br>
     * 频率惩罚，根据单词在本次答案中的出现频率进行惩罚，数值越高，答案中出现相同单词的概率越低。
     */
    @JsonProperty("frequency_penalty")
    Double frequencyPenalty;

    /**
     * 对数偏差，某个单词的值越大，其出现在答案中的概率越高。
     */
    @JsonProperty("logit_bias")
    Map<String, Integer> logitBias;

    /**
     * 可以在发送请求时，附上一个用户标识，这有助于OpenAI监控该用户在使用过程中是否存在滥用的情况。
     */
    String user;

}
