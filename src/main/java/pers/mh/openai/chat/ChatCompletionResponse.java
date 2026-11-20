package pers.mh.openai.chat;

import lombok.Data;

import java.util.List;

@Data
public class ChatCompletionResponse {
    String id;
    String object;
    long created;
    String model;
    List<ChatCompletionChoice> choices;
}
