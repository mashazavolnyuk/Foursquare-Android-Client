package com.mashazavolnyuk.client.utils;

import com.mashazavolnyuk.client.data.Price;

public class ConverterForPrice {
    public static String getFormatMessageByPrice(Price price) {
        Integer tire = price.getTier();
        String formatPrice = null;
        switch (tire) {
            case 1:
                formatPrice = "$";
                break;
            case 2:
                formatPrice = "$$";
                break;
            case 3:
                formatPrice = "$$$";
                break;
            case 4:
                formatPrice = "$$$$";
                break;
        }
        return formatPrice;
    }
}
