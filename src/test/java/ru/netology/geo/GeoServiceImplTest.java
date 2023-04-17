package ru.netology.geo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.sender.MessageSenderImpl;

class GeoServiceImplTest {

    @Test
    void byIpShouldReturnCorrectLocation() {
        GeoService geoService = new GeoServiceImpl();
        Location expectedLocation = new Location("", Country.RUSSIA, "", 0);
        Country expectedCountry = expectedLocation.getCountry();
        Country resultTestCountry = geoService.byIp(GeoServiceImpl.MOSCOW_IP).getCountry();
        Assertions.assertEquals(expectedCountry, resultTestCountry);
    }
}