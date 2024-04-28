import org.moeaframework.algorithm.NSGAII;
import org.moeaframework.algorithm.MOEAD;
import org.moeaframework.core.Problem;
import org.moeaframework.problem.ZDT.ZDT1;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;

public class Example1 {

    public static void main(String[] args) {
        int repetitions = 10;
        int populationSize = 20;
        int evaluations = 10000;
        //initialization of problem
        Problem problem = new ZDT1(2);

        System.out.println("Problem: " + problem.getName());

        System.out.println("Population Size: " + populationSize);

        for (int i = 0; i < repetitions; i++) {
            System.out.println("Repetition: " + (i + 1));

            NSGAII nsgaII = new NSGAII(problem);
            MOEAD moead = new MOEAD(problem);

            // Run NSGA-II
            nsgaII.run(evaluations);
            NondominatedPopulation nsgaIIPopulation = nsgaII.getResult();

            double[] sumObjectives = new double[nsgaIIPopulation.get(0).getNumberOfObjectives()];

            // Calculate the sum of objective values
            for (Solution solution : nsgaIIPopulation) {
                for (int j = 0; j < solution.getNumberOfObjectives(); j++) {
                    sumObjectives[j] += solution.getObjective(j);
                }
            }
            
            int totalObjectiveValues = nsgaIIPopulation.size() * problem.getNumberOfObjectives();

            // Calculate the average of all objective values
            double[] averageObjectives = new double[sumObjectives.length];
            for (int k = 0; k < sumObjectives.length; k++) {
            	averageObjectives[k] = sumObjectives[k] / totalObjectiveValues;
            }
            
            // Calculate the standard deviation of objective values
            double[] squaredDifferences = new double[sumObjectives.length];
            for (Solution solution : nsgaIIPopulation) {
                for (int l = 0; l < solution.getNumberOfObjectives(); l++) {
                    squaredDifferences[l] += Math.pow(solution.getObjective(l) - averageObjectives[l], 2);
                }
            }
            
            double[] standardDeviations = new double[squaredDifferences.length];
            for (int m = 0; m < squaredDifferences.length; m++) {
                standardDeviations[m] = Math.sqrt(squaredDifferences[m] / populationSize);
            }

            // Print or use the average objective values as needed
            System.out.println("Average Objective Values:");
            for (double avg : averageObjectives) {
                System.out.println(avg);
            }
            
            //Print or use the std deviation values as needed
            System.out.println("Std Deviation Values: ");
            for(double std: standardDeviations) {
            	System.out.println(std);
            }
        }
    }
}
