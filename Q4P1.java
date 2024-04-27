import org.moeaframework.algorithm.NSGAII;
import org.moeaframework.algorithm.MOEAD;
import org.moeaframework.core.Problem;
import org.moeaframework.problem.ZDT.ZDT1;
import org.moeaframework.problem.ZDT.ZDT2;
import org.moeaframework.problem.DTLZ.DTLZ3;
import org.moeaframework.problem.DTLZ.DTLZ4;

public class Example1 {

    public static void main(String[] args) {
        int repetitions = 10;
        int populationSize = 20;
        //array of problems
        Problem problems[] = {new ZDT1(2), new ZDT2(2), new DTLZ3(3), new DTLZ4(3)};

        for (Problem problem : problems) {
            //print out results
            System.out.println("Results for " + problem.getName() + " with population size " + populationSize + " after " + repetitions + " repetitions:");
            for (int i = 0; i < populationSize; i++) {
                NSGAII algorithm = new NSGAII(problem);
                MOEAD algorithm2 = new MOEAD(problem);

                for (int j = 0; j < repetitions; j++) {
                    algorithm.run(10);
                    algorithm2.run(10);
                }
                System.out.println("NSGAII Results:");
                algorithm.getResult().display();
                System.out.println("MOEAD Results:");
                algorithm2.getResult().display();
            }
        }
    }
}
