package com.bootcamp.desafioquality.service.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RoundUtil {
    public static double roundTwoDecimals(double d) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        return bd.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
