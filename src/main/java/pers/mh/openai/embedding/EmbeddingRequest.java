package pers.mh.openai.embedding;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmbeddingRequest {

    String model;
    @NonNull
    List<String> input;
    String user;
}
