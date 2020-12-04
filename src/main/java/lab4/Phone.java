package lab4;

import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static util.CalculationUtil.roundNumber;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Phone {
    private String phoneName;
    private double price;
    private double year;
    private double batteryCapacity;
    private double rating;
}
