package lab2;

import lombok.*;

@Data
public class TreeNode {

    private String variant;
    private Integer factoryWorth;
    private Integer annualIncome;
    private Integer annualLoss;
    private Double incomeProbability;
    private Double lossProbability;
    private Integer years;
    private Double buildingProbability;
    private Double notBuildingProbability;

    @Override
    public String toString() {
        return "TreeNode{" +
                "variant='" + variant + '\'' +
                ", factoryWorth=" + factoryWorth +
                ", annualIncome=" + annualIncome +
                ", annualLoss=" + annualLoss +
                ", incomeProbability=" + incomeProbability +
                ", lossProbability=" + lossProbability +
                ", years=" + years +
                '}';
    }
}
