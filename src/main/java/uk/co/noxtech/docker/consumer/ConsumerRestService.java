package uk.co.noxtech.docker.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerRestService {

    @Autowired
    private ConsumerService consumerService;

    @RequestMapping("/")
    public String index() {
        return consumerService.getConsumedTelephoneStatistics();
    }

    @RequestMapping("/json")
    public String json() {
        return consumerService.getConsumedTelephoneJson();
    }
}
