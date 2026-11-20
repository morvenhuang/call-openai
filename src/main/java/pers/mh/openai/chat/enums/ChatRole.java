package pers.mh.openai.chat.enums;

/**
 * system: gives high level instructions for the conversation
 * user: you, who asks the question
 * assistant: the model, who answers the question
 */
public enum ChatRole {
    SYSTEM("system"),
    USER("user"),
    ASSISTANT("assistant");

    private final String value;

    ChatRole(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
