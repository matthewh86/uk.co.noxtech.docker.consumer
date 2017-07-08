package uk.co.noxtech.docker.consumer;

public interface ConsumerService {

    void consumeMessage(String message);

    String getConsumedTelephoneStatistics();

    String getConsumedTelephoneJson();

    void flushData();
}
