import java.util.Arrays;
import java.util.Random;

/**
 * Created by arash on 9/14/17.
 */
public class Genome  implements Comparable<Genome> {

    private double[] alleles;
    private double fitness;
    private boolean evaluated;
    private double mutation_rate;
    private double[] mutation_step = new double[10];
    private int age;
    public Genome[] parents = new Genome[4];

    public Genome(double[] alleles, double fitness, boolean evaluated,double mutation_rate,double[] mutation_step) {
        this.alleles = alleles;
        this.fitness = fitness;
        this.evaluated = evaluated;
        this.mutation_rate = mutation_rate;
        this.mutation_step = mutation_step;
        this.age = 0;

    }

    public Genome(){
        alleles = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        this.alleles = alleles;
        this.fitness = 0;
        this.evaluated = false;
        this.mutation_rate = 0;
        this.mutation_step = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        this.age = 0;
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

    public double getMutation_rate() {
        return mutation_rate;
    }

    public void setMutation_rate(double mutation_rate) {
        this.mutation_rate = mutation_rate;
    }

    public double getMutation_step_atIndex(int index) {
        return mutation_step[index];
    }

    public void setMutation_step_atIndex(double mutation_step,int index) {
        this.mutation_step[index] = mutation_step;
    }
    public double[] getMutation_step() {
        return mutation_step;
    }

    public void setMutation_step(double[] mutation_step) {
        this.mutation_step = mutation_step;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void age(){
        this.age++;
    }

    public void mutateAllele(int index){
        alleles[index] += mutation_step[index];
    }


    public void  resetAge(){
        this.age=0;
        this.parents = new Genome[4];
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
