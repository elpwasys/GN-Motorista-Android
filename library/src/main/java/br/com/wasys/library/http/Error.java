package br.com.wasys.library.http;

import org.apache.commons.collections4.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * Error
 * 31 de jul de 2016 18:38:36
 * @autor Everton Luiz Pascke
 */
public class Error {

	public Set<String> messages;
	
	public void addMessage(String message) {
		if (messages == null) {
			messages = new HashSet<>();
		}
		messages.add(message);
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