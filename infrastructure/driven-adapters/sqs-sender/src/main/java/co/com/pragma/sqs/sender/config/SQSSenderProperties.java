package co.com.pragma.sqs.sender.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "adapter.sqs")
public record SQSSenderProperties(
        String region,
        String endpoint,

        // Propiedades específicas
        @NestedConfigurationProperty
        SenderProperties sender,

        @NestedConfigurationProperty
        ListenerProperties listener
) {
    public record SenderProperties(
            String notificationQueueUrl,
            String automaticValidationQueueUrl,
            String acceptedPetitionsQueueUrl
    ) {
    }

    public record ListenerProperties(
            String responseValidationQueueUrl,
            int maxMessages,
            int waitTime,
            int concurrency
    ) {
    }
}
