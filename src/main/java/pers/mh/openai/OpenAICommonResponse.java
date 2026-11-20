package pers.mh.openai;

import lombok.Data;

import java.util.List;

/**
 * Several responses have similar json structure, which has a string object and a list
 *
 */
@Data
public class OpenAICommonResponse<T> {
    public String object;
    public List<T> data;
}
