package pers.mh.openai.embedding;

import lombok.Data;

import java.util.List;

@Data
public class Embedding {

    String object;
    List<Double> embedding;
    Integer index;
}
