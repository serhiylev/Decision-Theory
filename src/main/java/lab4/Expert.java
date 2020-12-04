package lab4;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Map;

@Getter
@AllArgsConstructor
public class Expert {
    private final String name;
    private final Map<String, Double> preferences;
    private final Map<String, Double> coefficients;
    private final ArrayList<Double> phoneSum;
}
