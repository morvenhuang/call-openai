package pers.mh.openai.chat;

import lombok.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    @NonNull
    String role;
    String content;
}