import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import java.util.stream.Stream;


public class GeoServiceImplTest {

    public static Stream<Arguments> methodSource() {
        return Stream.of(
                Arguments.of("172.0.32.11", "Moscow"),
                Arguments.of("172.0.0.0", "Moscow"),
                Arguments.of("96.44.183.149", "New York"),
                Arguments.of("96.0.0.0", "New York"),
                Arguments.of("127.0.0.1", null)
        );
    }

    // тест для проверки определения локации по ip (класс GeoServiceImpl)
    @ParameterizedTest
    @MethodSource("methodSource")
    public void locationByIpTest(String ip, String expectedCity) {
        GeoService geoService = new GeoServiceImpl();
        Location actualLocation = geoService.byIp(ip);
        String actualCity = actualLocation.getCity();
        Assertions.assertEquals(expectedCity, actualCity);
    }

    //тест для проверки выброса RuntimeException при определении локации по координатам
    @Test
    public void locationByCoordinatesTest() {
        GeoService geoService = new GeoServiceImpl();
        Assertions.assertThrows(RuntimeException.class, () -> geoService.byCoordinates(1.1, 2.2));
    }
}
