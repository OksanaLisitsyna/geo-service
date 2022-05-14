import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;
import java.util.stream.Stream;


public class LocalizationServiceImplTest {
    public static Stream<Arguments> methodSourceForLocale() {
        return Stream.of(
                Arguments.of(Country.RUSSIA, "Добро пожаловать"),
                Arguments.of(Country.USA, "Welcome"),
                Arguments.of(Country.GERMANY, "Welcome")
        );
    }

    //тест для проверки возвращаемого текста (класс LocalizationServiceImpl)
    @ParameterizedTest
    @MethodSource("methodSourceForLocale")
    public void localeIfRussiaTest(Country country, String expectedText) {
        LocalizationService localizationService = new LocalizationServiceImpl();
        String actualText = localizationService.locale(country);

        Assertions.assertEquals(expectedText, actualText);
    }
}
