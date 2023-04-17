package ru.netology.i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.netology.entity.Country;

class LocalizationServiceImplTest {

    @Test
    void localeShouldReturnCorrectMessage() {
        LocalizationService localizationService = new LocalizationServiceImpl();
        String expectedText = "Добро пожаловать";
        Assertions.assertEquals(expectedText, localizationService.locale(Country.RUSSIA));
    }
}