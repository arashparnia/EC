
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by arash on 9/30/17.
 */
public class Population {
    private ArrayList<Genome> population = new ArrayList<>();
    private int population_size  = 0;
    private Random rnd_ = new Random();
    public Population(ArrayList<Genome> population,int population_size) {
        this.population = population;
        this.population_size = population_size;
    }

    public Population(int population_size) {
        this.population = new ArrayList<>();
        this.population_size = population_size;
//        rnd_.setSeed(seed);
    }

    public ArrayList<Genome> getPopulation() {
        return population;
    }

    public void setPopulation(ArrayList<Genome> population) {
        this.population = population;
    }

    public int getPopulation_size() {
        return population_size;
    }

    public void setPopulation_size(int population_size) {
        this.population_size = population_size;
    }
    public void add(Genome genome){
        population.add(genome);
    }
    public Genome get(int index){
       return  population.get(index);
    }

    public void sortPopulation(boolean reverse){
        Collections.sort(population);
        if (reverse) Collections.reverseOrder();
    }
    public void shufflePopulation(){
        Collections.shuffle(population);
    }
    public void removeSublist(int range){
        sortPopulation(true);
        population.subList(0,range).clear();
    }


    public void initPopulation(int genome_size,double init_range, double mutation_step, double allele_mutation_chance )
    {
        for (int i = 0 ; i < population_size ; i++){
            population.add(new Genome());
        }
        for (Genome genome: population) {
            for (int j = 0; j < genome_size; j++) {

                genome.setAlleleAtIndex((init_range * ( rnd_.nextDouble() -0.5)),j);
                genome.setMutation_step_atIndex((rnd_.nextDouble() - 0.5)   * mutation_step,j);
            }
            genome.setMutation_rate( rnd_.nextDouble() * allele_mutation_chance);
//             genome.setMutation_step( (rnd_.nextDouble() - 0.5)   * mutation_step);
//            double[] a = {-0.9858244722505388, 3.999176232240111, -0.042124887755302494, -3.6947898099101364, -0.15798503947882903, -2.0025428067422912, 1.313564593538192, -0.7562533345677892, 1.2623663203163324, -0.349182894330935};
//            double[] b = {-0.93048292515929, 3.9948649712850046, 0.0849646806798547, -3.7573004022882786, -0.34321694328776, -2.0536289069143745, 1.354830646277407, -0.7428873166019453, 1.1918768257314292, -0.3224792439100581};
//            genome.setAlleles(b);

        }
    }


    public void splitPopulation(int race_limit){

        int[] population_races = new int[population_size];
        int n =0;
        for (int i= 0 ; i < population_size/race_limit-1;i++){
            for (int j = 0 ; j < race_limit;j++){
                n++;
                population_races[n]=j;

            }
        }

        shuffleArray(population_races);

        n=0;
        for (Genome genome: population) {
            int[] r = {population_races[n]};
            genome.setRace(r);
            n++;
        }
    }




    // Implementing Fisher–Yates shuffle
    private void shuffleArray(int[] ar)
    {
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd_.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public void mutatePopulation(double genome_mutation_chance) {

        for (Genome genome : population) {
            if (rnd_.nextDouble() < genome_mutation_chance) {
                genome.mutate();
            }
        }
    }


    private void fittnessSharing(double σ){

        for (Genome i : population){
            double sum = 0;
            for (Genome j : population){
                sum = sum +  sh(i.getAlleles(),j.getAlleles(),σ);
            }
            i.setFitness(i.getFitness() / sum);
        }
    }

    private double sh(double[] i ,double[] j,double σ){

        double d  = distance(i,j);
        if (d <= σ)
            return  1 - (d /σ);
        else
            return 0;
    }

    private double distance(double[] p, double[] q){
        double d = 0;
        for (int i = 0 ; i < 10 ; i++){
            d = d + Math.pow(p[i] - q[i],2);
        }
        return Math.sqrt(d);
    }


    private void removeWorst(int size){
//        Iterator<Genome> i = population.iterator();
//        while (i.hasNext()) {
//            Genome g = i.next();
//            if(g.getAge() > life_expectancy && population_size > 10) {
//                i.remove();population_size--;
//                System.out.println("removed");
//            }
//        }

        Genome worst = new Genome();
        double low = 11 ;
        for (int k = 0 ; k < size;k++) {
            for (Genome g : population){
                if (g.getFitness() < low) {worst = g;low = g.getFitness();}
            }
            population.remove(worst); population_size--;
        }
    }

}
