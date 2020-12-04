package util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculationUtil {

    public static double roundNumber(double value) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
