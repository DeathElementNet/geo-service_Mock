package ru.netology.sender;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;

import java.util.HashMap;
import java.util.Map;

class MessageSenderImplTest {
    private MessageSender messageSender;
    GeoService geoService;
    LocalizationService localizationService;
    Map<String, String> headers = new HashMap<>();
    String expectedText;

    @BeforeEach
    void createStubsForExternalServices() {
        headers = new HashMap<>();
        geoService = Mockito.mock(GeoServiceImpl.class);
        localizationService = Mockito.mock(LocalizationServiceImpl.class);
        messageSender = new MessageSenderImpl(geoService, localizationService);
    }

    @Test
    @DisplayName("Всегда отправляется русский текст, если ip из российского сегмента адресов")
    void sendShouldReturnRussianTextForRussianIP() {
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, GeoServiceImpl.MOSCOW_IP);
        expectedText = "Добро пожаловать";

        Mockito.when(geoService.byIp(GeoServiceImpl.MOSCOW_IP))
                .thenReturn(new Location("Moscow", Country.RUSSIA, null, 0));
        Mockito.when(localizationService.locale(Country.RUSSIA))
                .thenReturn("Добро пожаловать");

        Assertions.assertEquals(expectedText, messageSender.send(headers));
    }

    @Test
    @DisplayName("Всегда отправляется английский текст, если ip не из российского сегмента адресов")
    void sendShouldReturnEnglishTextForNotRussianIP() {
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, GeoServiceImpl.NEW_YORK_IP);
        expectedText = "Welcome";

        Mockito.when(geoService.byIp(GeoServiceImpl.NEW_YORK_IP))
                .thenReturn(new Location("Boston", Country.USA, null, 0));
        Mockito.when(localizationService.locale(Country.USA))
                .thenReturn("Welcome");

        Assertions.assertEquals(expectedText, messageSender.send(headers));
    }

    // реализация кода без mockito
    @ParameterizedTest
    @CsvSource(value = {
            GeoServiceImpl.MOSCOW_IP + "," + "Добро пожаловать",
            GeoServiceImpl.NEW_YORK_IP + "," + "Welcome"
    })
    void sendShouldReturnTextDependingOnIP(String ip, String expectedText) {
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);
        geoService = new GeoServiceImpl();
        localizationService = new LocalizationServiceImpl();
        messageSender = new MessageSenderImpl(geoService, localizationService);
        Assertions.assertEquals(expectedText, messageSender.send(headers));
    }
}