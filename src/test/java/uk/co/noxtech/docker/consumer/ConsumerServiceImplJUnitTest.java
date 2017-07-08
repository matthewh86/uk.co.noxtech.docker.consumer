package uk.co.noxtech.docker.consumer;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import uk.co.noxtech.docker.data.Telephone;

import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ConsumerServiceImplJUnitTest {

    private ConsumerServiceImpl testSubject = new ConsumerServiceImpl();

    @Before
    public void before() throws Exception {
        testSubject.flushData();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldAddConsumedMessageToInternalMap() throws Exception {
        // Given
        Telephone telephone = new Telephone(Telephone.createPhoneNumber("55555", "GB"));
        String message = telephone.toJsonString();

        // When
        testSubject.consumeMessage(message);
        Thread.sleep(1000); // need to sleep because a new thread is created to add it onto the map

        // Then
        Map<Integer, List<Telephone>> consumedTelephone = (Map<Integer, List<Telephone>>) ReflectionTestUtils.getField(testSubject, "consumedTelephones");
        assertThat(consumedTelephone.size(), is(1));
        assertThat(consumedTelephone.get(44).get(0), is(telephone));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldGetConsumedMessageStatistics() throws Exception {
        // Given
        Telephone telephone = new Telephone(Telephone.createPhoneNumber("55555", "GB"));
        String message = telephone.toJsonString();
        testSubject.consumeMessage(message);
        Thread.sleep(1000); // need to sleep because a new thread is created to add it onto the map

        // When
        String result = testSubject.getConsumedTelephoneStatistics();

        // Then
        assertThat(result, is("Country Code: 44 Occurrences: 1<br/>"));
    }

}
