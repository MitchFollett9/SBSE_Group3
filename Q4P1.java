import org.moeaframework.algorithm.NSGAII;
import org.moeaframework.algorithm.MOEAD;
import org.moeaframework.core.Problem;
import org.moeaframework.problem.ZDT.ZDT1;
import org.moeaframework.analysis.plot.Plot;

public class Example1 {

    public static void main(String[] args) {
        int repetitions = 10;
        int populationSize = 20;
        int evaluations = 10000;
        //initialisation of problem
        Problem problem = new ZDT1(1);

        System.out.println("Problem: " + problem.getName());

        System.out.println("Population Size: " + populationSize);

        double[] nsgaIIResults = new double[repetitions];
        double[] moeadResults = new double[repetitions];

        for (int j = 0; j < repetitions; j++) {
            System.out.println("Repetition: " + (j + 1));

            NSGAII nsgaII = new NSGAII(problem);
            MOEAD moead = new MOEAD(problem);

            // Run NSGA-II
            nsgaII.run(evaluations);

            // Run MOEA/D
            moead.run(evaluations);
        }

        // Calculate and display average and standard deviation for NSGA-II
        double nsgaIIAvg = calculateAverage(nsgaIIResults);
        double nsgaIIStdDev = calculateStandardDeviation(nsgaIIResults);
        System.out.println("NSGA-II - Average: " + nsgaIIAvg + ", StdDev: " + nsgaIIStdDev);

        // Calculate and display average and standard deviation for MOEA/D
        double moeadAvg = calculateAverage(moeadResults);
        double moeadStdDev = calculateStandardDeviation(moeadResults);
        System.out.println("MOEA/D - Average: " + moeadAvg + ", StdDev: " + moeadStdDev);

        // Plot results
        Plot plot = new Plot()
            .scatter("NSGA-II", nsgaIIResults, moeadResults)
            .setXLabel("Repetition")
            .setYLabel("Metric Value")
            .setTitle("Comparison of NSGA-II and MOEA/D");

        plot.show();
    }

    // Function to calculate the average of an array of doubles
    private static double calculateAverage(double[] values) {
        double sum = 0.0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.length;
    }

    // Function to calculate the standard deviation of an array of doubles
    private static double calculateStandardDeviation(double[] values) {
        double average = calculateAverage(values);
        double sumSquaredDiff = 0.0;
        for (double value : values) {
            sumSquaredDiff += Math.pow(value - average, 2);
        }
        return Math.sqrt(sumSquaredDiff / values.length);
    }
}
