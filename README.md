## Сервис отправки локализованных сообщений

В этом задании отрабатывались навыки тестирования кода, в частности использование заглушек.
</br>



Работа *MessageSender* завист результатов *GeoService* и *LocalizationService*. Поэтому для теста создадим заглушки с помощью средств библиотеки **Mockito**

 ```
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
```

</br>

Для проверки правильности работы классов *GeoServiceImpl* и *LocalizationServiceImpl* используем параметризованные тесты. Например:

```
    @ParameterizedTest
    @MethodSource("methodSource")
    public void locationByIpTest(String ip, String expectedCity) {
        GeoService geoService = new GeoServiceImpl();
        Location actualLocation = geoService.byIp(ip);
        String actualCity = actualLocation.getCity();
        Assertions.assertEquals(expectedCity, actualCity);
    }
    
    public static Stream<Arguments> methodSource() {
        return Stream.of(
                Arguments.of("172.0.32.11", "Moscow"),
                Arguments.of("172.0.0.0", "Moscow"),
                Arguments.of("96.44.183.149", "New York"),
                Arguments.of("96.0.0.0", "New York"),
                Arguments.of("127.0.0.1", null)
        );
    }
```
    
    
    
