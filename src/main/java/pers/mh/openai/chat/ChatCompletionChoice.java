package pers.mh.openai.chat;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * example:
 *
 * {"id":"chatcmpl-7nNwYWPaiza2IbtPdHuFpWyCDIjud","object":"chat.completion.chunk","created":1692004466,"model":"gpt-3.5-turbo-0613","choices":[{"index":0,"delta":{"role":"assistant","content":""},"finish_reason":null}]}
 * {"id":"chatcmpl-7nNwYWPaiza2IbtPdHuFpWyCDIjud","object":"chat.completion.chunk","created":1692004466,"model":"gpt-3.5-turbo-0613","choices":[{"index":0,"delta":{"content":"Hello"},"finish_reason":null}]}
 * {"id":"chatcmpl-7nNwYWPaiza2IbtPdHuFpWyCDIjud","object":"chat.completion.chunk","created":1692004466,"model":"gpt-3.5-turbo-0613","choices":[{"index":0,"delta":{"content":"!"},"finish_reason":null}]}
 * {"id":"chatcmpl-7nNwYWPaiza2IbtPdHuFpWyCDIjud","object":"chat.completion.chunk","created":1692004466,"model":"gpt-3.5-turbo-0613","choices":[{"index":0,"delta":{"content":" How"},"finish_reason":null}]}
 * {"id":"chatcmpl-7nNwYWPaiza2IbtPdHuFpWyCDIjud","object":"chat.completion.chunk","created":1692004466,"model":"gpt-3.5-turbo-0613","choices":[{"index":0,"delta":{"content":" can"},"finish_reason":null}]}
 * {"id":"chatcmpl-7nNwYWPaiza2IbtPdHuFpWyCDIjud","object":"chat.completion.chunk","created":1692004466,"model":"gpt-3.5-turbo-0613","choices":[{"index":0,"delta":{"content":" I"},"finish_reason":null}]}
 * {"id":"chatcmpl-7nNwYWPaiza2IbtPdHuFpWyCDIjud","object":"chat.completion.chunk","created":1692004466,"model":"gpt-3.5-turbo-0613","choices":[{"index":0,"delta":{"content":" assist"},"finish_reason":null}]}
 * {"id":"chatcmpl-7nNwYWPaiza2IbtPdHuFpWyCDIjud","object":"chat.completion.chunk","created":1692004466,"model":"gpt-3.5-turbo-0613","choices":[{"index":0,"delta":{"content":" you"},"finish_reason":null}]}
 * {"id":"chatcmpl-7nNwYWPaiza2IbtPdHuFpWyCDIjud","object":"chat.completion.chunk","created":1692004466,"model":"gpt-3.5-turbo-0613","choices":[{"index":0,"delta":{"content":" today"},"finish_reason":null}]}
 * {"id":"chatcmpl-7nNwYWPaiza2IbtPdHuFpWyCDIjud","object":"chat.completion.chunk","created":1692004466,"model":"gpt-3.5-turbo-0613","choices":[{"index":0,"delta":{"content":"?"},"finish_reason":null}]}
 * {"id":"chatcmpl-7nNwYWPaiza2IbtPdHuFpWyCDIjud","object":"chat.completion.chunk","created":1692004466,"model":"gpt-3.5-turbo-0613","choices":[{"index":0,"delta":{},"finish_reason":"stop"}]}
 * [DONE]
 */
@Data
public class ChatCompletionChoice {

    Integer index;

    // 'message' when using non-stream mode, 'delta' when in stream mode
    @JsonAlias("delta")
    ChatMessage message;

    @JsonProperty("finish_reason")
    String finishReason;
}
