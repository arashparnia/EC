import java.util.Arrays;

/**
 * Created by arash on 9/14/17.
 */
public class Genome  implements Comparable<Genome> {

    private double[] alleles;
    private double fitness;
    private boolean evaluated;

    public Genome(double[] alleles, double fitness, boolean evaluated) {
        this.alleles = alleles;
        this.fitness = fitness;
        this.evaluated = evaluated;
    }

    public Genome(){
        alleles = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        this.alleles = alleles;
        this.fitness = -999999999;
        this.evaluated = false;
    }

    @Override
    public int compareTo(Genome o) {
        return Double.compare(fitness , o.getFitness()) ;
    }



    public double[] getAlleles() {
        return alleles;
    }
    public double getAlleleAtIndex(int index) {
        return alleles[index];
    }

    public void setAlleles(double[] alleles) {
        this.alleles = alleles;
    }
    public void setAlleleAtIndex(double allele, int index) {
        this.alleles[index] = allele;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public boolean isEvaluated() {
        return evaluated;
    }

    public void setEvaluated(boolean evaluated) {
        this.evaluated = evaluated;
    }

    @Override
    public String toString() {
        String s = "";
        s = "Genome{" +
                "genome=" + Arrays.toString(alleles) +
                ", fitness=" + fitness +
//                ", mutation_rate=" + mutation_rate +
//                ", mutation_pobability=" + mutation_probability_genome +
                '}';

        return s;
    }
}
