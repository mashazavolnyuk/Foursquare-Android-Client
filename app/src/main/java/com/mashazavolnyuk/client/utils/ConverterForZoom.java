package com.mashazavolnyuk.client.utils;

import java.util.HashMap;
import java.util.Map;

public class ConverterForZoom {
    private static Map<Integer, Double> mapPosition;

    static {
        mapPosition = new HashMap<>();
        mapPosition.put(0, 0.5);
        mapPosition.put(1, 1.0);
        mapPosition.put(2, 3.0);
        mapPosition.put(3, 5.0);
        mapPosition.put(4, 10.0);
        mapPosition.put(5, 15.0);
        mapPosition.put(6, 25.0);
        mapPosition.put(7, 50.0);
        mapPosition.put(8, 60.0);
        mapPosition.put(9, 70.0);
        mapPosition.put(10, 100.0);
    }

    public static double getKmByPosition(int position) {
        return mapPosition.get(position);
    }
}
