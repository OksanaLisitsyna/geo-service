import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderImpl;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class MessageSenderImplTest {

    public static Stream<Arguments> methodSourceForSend() {
        return Stream.of(
                Arguments.of("172.0.32.11", "Добро пожаловать"),
                Arguments.of("172.0.0.0", "Добро пожаловать"),
                Arguments.of("96.44.183.149", "Welcome"),
                Arguments.of("96.0.0.0", "Welcome")
        );
    }

    // проверим российских адресов ("172." отправляется текст на русском,
    // для американских адресов ("96.") - на английском
    // без заглушек
    @ParameterizedTest
    @MethodSource("methodSourceForSend")
    public void sendTestRussia(String ip, String expectedText) {

        GeoService geoService = new GeoServiceImpl();
        LocalizationService localizationService = new LocalizationServiceImpl();

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);
        String actualText = messageSender.send(headers);

        Assertions.assertEquals(expectedText, actualText);
    }

    // сделаем заглушки для GeoService и LocalizationService
    // для американского ip
    @Test
    public void sendTestUsa() {
        String expectedText = "Welcome";

        Map<String, String> headers = new HashMap<>();
        headers.put("x-real-ip", "96.44.183.149");

        GeoService geoService = Mockito.mock(GeoService.class);
        Mockito.when(geoService.byIp("96.44.183.149"))
                .thenReturn(new Location("New York", Country.USA, " 10th Avenue", 32));

        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(Country.USA))
                .thenReturn("Welcome");

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
        String actualText = messageSender.send(headers);

        Assertions.assertEquals(expectedText, actualText);
    }

    // сделаем заглушки для GeoService и LocalizationService
    // для русского ip
    @Test
    public void sendTestRussia() {
        String expectedText = "Добро пожаловать";

        Map<String, String> headers = new HashMap<>();
        headers.put("x-real-ip", "172.0.32.11");

        GeoService geoService = Mockito.mock(GeoService.class);
        Mockito.when(geoService.byIp("172.0.32.11"))
                .thenReturn(new Location("Moscow", Country.RUSSIA, "Lenina", 15));

        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(Country.RUSSIA))
                .thenReturn("Добро пожаловать");

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
        String actualText = messageSender.send(headers);

        Assertions.assertEquals(expectedText, actualText);
    }
}
