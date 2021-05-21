package me.caponetto;

import java.time.Duration;
import java.util.Optional;
import java.util.Random;

import javax.enterprise.context.ApplicationScoped;

import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

/**
 * After `startAfter` seconds, this bean produces decision requests with some random inputs every `frequency` seconds.
 * The decision requests are written to a Kafka topic.
 * The Kafka configuration is specified in the application configuration.
 */
@ApplicationScoped
public class DecisionGenerator {

    @ConfigProperty(name = "producer.name")
    String name;

    @ConfigProperty(name = "producer.start_after")
    int startAfter;

    @ConfigProperty(name = "producer.frequency")
    int frequency;

    private static final Random RANDOM = new Random();
    private int id = 1;

    @Broadcast
    @Outgoing("kogito_outgoing_stream")
    public Multi<String> generateTrafficViolation() {
        final String path = "request.json.template";
        final Optional<String> requestTemplate = Utils.readResource(path);
        if (requestTemplate.isEmpty()) {
            throw new IllegalStateException("Can't read resource " + path);
        }

        return Multi.createFrom()
                .ticks()
                .startingAfter(Duration.ofSeconds(startAfter))
                .every(Duration.ofSeconds(frequency))
                .onOverflow()
                .drop()
                .map(tick -> requestTemplate.get()
                        .replace("<ID>", name + "_" + id++)
                        .replace("<AGE>", String.valueOf(RANDOM.nextInt(72) + 18)) // [18, 90]
                        .replace("<SPEED>", String.valueOf(RANDOM.nextInt(40) + 110)) // [110, 150]
                        .replace("<POINTS>", String.valueOf(RANDOM.nextInt(19)))); // [0, 19]
    }
}
