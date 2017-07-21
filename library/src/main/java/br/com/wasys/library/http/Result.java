package br.com.wasys.library.http;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Set;

/**
 * Created by pascke on 18/08/16.
 */
public class Result {

    public Status status;
    public Set<String> messages;

    public enum Status {
        FAILURE,
        SUCCESS
    }

    public String getFormattedErrorMessage() {
        StringBuilder builder = new StringBuilder();
        if (CollectionUtils.isNotEmpty(messages)) {
            for (String message : messages) {
                if (builder.length() > 0) {
                    builder.append("\n");
                }
                builder.append(message);
            }
        }
        return builder.toString();
    }
}
