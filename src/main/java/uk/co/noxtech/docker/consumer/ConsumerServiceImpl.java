package uk.co.noxtech.docker.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import uk.co.noxtech.docker.data.Telephone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ConsumerServiceImpl implements ConsumerService {

    private static final Logger LOGGER = LogManager.getLogger(ConsumerServiceImpl.class);

    private static final Map<Integer, List<Telephone>> consumedTelephones = new ConcurrentHashMap<>();

    @Override
    public void consumeMessage(String message) {
        LOGGER.info("Received <" + message + ">");
        addMessageToTelephoneMap(message);
    }

    private void addMessageToTelephoneMap(String message) {
        Runnable telephoneTask = () -> {
            try {
                Telephone telephone = Telephone.parseJsonAsTelephone(message);
                int countryCode = telephone.getPhoneNumber().getCountryCode();
                if (consumedTelephones.containsKey(countryCode)) {
                    consumedTelephones.get(countryCode).add(telephone);
                } else {
                    List<Telephone> newList = Collections.synchronizedList(new ArrayList<>());
                    consumedTelephones.put(countryCode, newList);
                    consumedTelephones.get(countryCode).add(telephone);
                }
            } catch (IOException e) {
                LOGGER.error(e);
            }
        };
        new Thread(telephoneTask).start();
    }

    @Override
    public String getConsumedTelephoneStatistics() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, List<Telephone>> entry : consumedTelephones.entrySet()) {
            sb.append("Country Code: ");
            sb.append(entry.getKey());
            sb.append(" Occurrences: ");
            sb.append(entry.getValue().size());
            sb.append("<br/>");
        }
        return sb.toString();
    }

    @Override
    public String getConsumedTelephoneJson() {
        try {
            return new ObjectMapper().writeValueAsString(consumedTelephones);
        } catch (JsonProcessingException e) {
            return e.toString();
        }
    }

}