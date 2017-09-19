import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;


import java.lang.reflect.Array;
import java.util.*;


public class player13 implements ContestSubmission
{
	Random rnd_;
	ContestEvaluation evaluation_;
    private int evaluations_limit_;

	public player13()
	{
		rnd_ = new Random();
	}
	public static void main(String[] args){

		System.out.print("Start");

	}


	public void setSeed(long seed)
	{
		// Set seed of algortihms random process
		rnd_.setSeed(seed);
	}

	public void setEvaluation(ContestEvaluation evaluation)
	{
		// Set evaluation problem used in the run
		evaluation_ = evaluation;

		// Get evaluation properties
		Properties props = evaluation.getProperties();
        // Get evaluation limit
        evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));
		// Property keys depend on specific evaluation
		// E.g. double param = Double.parseDouble(props.getProperty("property_name"));
        isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
        hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));

        System.out.println("***********************************************************************************");
        System.out.println("Algorithm properties");
        System.out.println("***********************************************************************************");

        System.out.println("properties " + props.toString());
        System.out.println("evaluations_limit_ " + evaluations_limit_);
        System.out.println("isMultimodal " + isMultimodal);
        System.out.println("hasStructure " + hasStructure);
        System.out.println("isSeparable "   + isSeparable);

        System.out.println("***********************************************************************************");
        System.out.println("***********************************************************************************");

        // Do sth with property values, e.g. specify relevant settings of your algorithm

    }
//*************************************************************************************************
	// private double child[] = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
	//hardcoded sizes
    boolean isMultimodal ;
    boolean hasStructure ;
    boolean isSeparable ;

	private int population_size = 10;
    private static final int genome_size  = 10;
	private double init_range = 5;

	private double allele_mutation_chance = 0.01;
	private double mutation_step = 1;
    private double  genome_mutation_chance = 0.6;

    private double cooling_rate = 0.0001;

    int tournamentSelection_slice = 40;

    private int tournoment_size = 2;
    private List<Genome> population=new ArrayList<Genome>();
	private Genome elite;
    private Genome  previous_elite;

//*************************************************************************************************

	public void run()
	{
        if(isMultimodal){ //KatsuuraEvaluation
            population_size = 1000;
            init_range = 1;


            genome_mutation_chance = 0.01;
            allele_mutation_chance = 0.1;
            mutation_step = 0.5;

            cooling_rate = 0.00001;

            tournamentSelection_slice = 400;

            if (hasStructure){ //SchaffersEvaluation
                population_size = 1000;
                init_range = 1;

                genome_mutation_chance = 0.01;
                allele_mutation_chance = 0.9;
                mutation_step = 0.5;

                cooling_rate = 0.00001;

                tournamentSelection_slice = 100;
            }
        }else{ // Bentcigar Fucntion
            population_size = 100;
            init_range = 1;

            genome_mutation_chance = 0.01;
            allele_mutation_chance = 0.05;
            mutation_step = 0.6;

            cooling_rate = 0.00001;

            tournamentSelection_slice = 40;
        }


        // evaluations_limit_ = 1;

		// Run your algorithm here
        int generations = 0;
		int evals = 0;
         // init population
		//declaring populaton
		initPopulation();


		previous_elite = population.get(0);



		// calculate fitness
		double best_fitness = 0;
		while(evals< evaluations_limit_){
//            if (elite != null)population.remove(elite);population_size--;
			// for every Genome in population loop
            for (Genome genome : population){
				// Check fitness of unknown fuction
                if (!genome.isEvaluated() && evals< evaluations_limit_) {
                    // System.out.println(Arrays.toString(genome.getAlleles()));
                    double fitness = (double) evaluation_.evaluate(genome.getAlleles());
                    genome.setFitness(fitness);
                    genome.setEvaluated(true);
                    if (fitness > best_fitness) {
                        best_fitness = fitness;
                        elite = genome;
                    }
                    mutation_step *= 1 - cooling_rate;
                    evals++;
                }
			}
            System.out.println("Generation:" + generations +
                    " population:" + population_size+  " mutation_step:" + String.format("%.20f",mutation_step) +
                    " evaluations:" + evals +" fitness:" + String.format("%.20f",best_fitness) );


            if (!isMultimodal) {
                int[] parents_positions = tournamentSelection(3, tournamentSelection_slice);


                if (!related(population.get(parents_positions[0]), population.get(parents_positions[1]))
                        &&
                        !related(population.get(parents_positions[1]), population.get(parents_positions[2]))
                        &&
                        !related(population.get(parents_positions[0]), population.get(parents_positions[2]))

                        ) {
                    crossover3(population.get(parents_positions[0]),
                            population.get(parents_positions[1]),
                            population.get(parents_positions[2]));

                    population.remove(population.get(parents_positions[0]));
                    population_size--;
                    population.remove(population.get(parents_positions[1]));
                    population_size--;
                    population.remove(population.get(parents_positions[2]));
                    population_size--;
                }
            }else{
                if (hasStructure){
//                    int[] parents_positions = rouletteWheelSelection(2);
//                    crossoverAverage2(population.get(parents_positions[0]), population.get(parents_positions[1]));
//                System.out.println("----------------------------------------------------------------------------------");
                    int[] parents_positions = tournamentSelection(3, tournamentSelection_slice);
                    if (!related(population.get(parents_positions[0]), population.get(parents_positions[1]))
                            &&
                            !related(population.get(parents_positions[1]), population.get(parents_positions[2]))
                            &&
                            !related(population.get(parents_positions[0]), population.get(parents_positions[2]))
                            ) {
                        crossover3(population.get(parents_positions[0]),
                                population.get(parents_positions[1]),
                                population.get(parents_positions[2]));

                        population.remove(population.get(parents_positions[0]));
                        population_size--;
                        population.remove(population.get(parents_positions[1]));
                        population_size--;
                        population.remove(population.get(parents_positions[2]));
                        population_size--;
                    }
                }else {
                    int[] parents_positions = tournamentSelection(3, tournamentSelection_slice);
                    if (!related(population.get(parents_positions[0]), population.get(parents_positions[1]))
                            &&
                            !related(population.get(parents_positions[1]), population.get(parents_positions[2]))
                            &&
                            !related(population.get(parents_positions[0]), population.get(parents_positions[2]))
                            ) {
                        crossover3(population.get(parents_positions[0]),
                                population.get(parents_positions[1]),
                                population.get(parents_positions[2]));

                        population.remove(population.get(parents_positions[0]));
                        population_size--;
                        population.remove(population.get(parents_positions[1]));
                        population_size--;
                        population.remove(population.get(parents_positions[2]));
                        population_size--;
                    }
                }
            }


//            int[] parents_positions = tournamentSelection(10, tournamentSelection_slice);
////            int[] parents_positions = rouletteWheelSelection(10);
//                Genome[] genomes = {
//                        population.get(parents_positions[0]) ,
//                    population.get(parents_positions[1]),
//                    population.get(parents_positions[2]),
//                    population.get(parents_positions[3]),
//                    population.get(parents_positions[4]),
//                    population.get(parents_positions[5]),
//                    population.get(parents_positions[6]),
//                    population.get(parents_positions[7]),
//                    population.get(parents_positions[8]),
//                    population.get(parents_positions[9])
//
//                };
//
//                crossoverAverageN(genomes ,10);





            population.add(elite);
            population_size++;



//            mutation_step = mutation_step - cooling_rate ;
            mutatePopulation();


            generations++;



		}



	}
    //*************************************************************************************************

	public void initPopulation()
	{
        for (int i = 0 ; i < population_size ; i++){

            population.add(new Genome());
        }
        for (Genome genome: population) {
            for (int j = 0; j < genome_size; j++) {

                genome.setAlleleAtIndex((init_range * (1-rnd_.nextDouble())),j);
            }
        }

	}

    public void mutatePopulation() {
        boolean mutated = false;
        for (Genome genome : population) {
            if (rnd_.nextDouble() < genome_mutation_chance) {
                for (int j = 0; j < genome_size; j++) {
                    if (rnd_.nextDouble() < allele_mutation_chance) {
                        if (rnd_.nextBoolean())
                            genome.setAlleleAtIndex(genome.getAlleleAtIndex(j) + ((rnd_.nextDouble()) * mutation_step), j);
                        else
                            genome.setAlleleAtIndex(genome.getAlleleAtIndex(j) - ((rnd_.nextDouble()) * mutation_step), j);
                        mutated = true;
                    }
                }
                if (mutated) genome.setEvaluated(false);
                mutated = false;
            }
        }
    }





    private int[] rouletteWheelSelection(int number_of_parents){
        int[] parents_positions = new int[number_of_parents];
        int k = 0;
        while( k < number_of_parents){
            double[] fitnesses = new double[population_size];
            double sum_fitness = 0;

            for (int i = 0; i < population_size; i++) {
                double fitness = population.get(i).getFitness();
                fitnesses[i] = fitness;
                sum_fitness = sum_fitness + fitness;
            }

            double[] relative_fitnesses = new double[population_size];
            for (int i = 0; i < population_size; i++) {
                relative_fitnesses[i] = fitnesses[i] / sum_fitness;
            }

            double[] agg_pop = new double[population_size];
            agg_pop[0] = relative_fitnesses[0];
            for (int i = 1; i < population_size; i++) {
                agg_pop[i] = agg_pop[i - 1] + relative_fitnesses[i];
            }


            double random_number = rnd_.nextDouble();

            int position = 0;
            for (int i = 0; i < population_size; i++) {
                if (agg_pop[i] > random_number) {
                    position = i;
                    break;
                }
            }


            if (!Arrays.asList(parents_positions).contains(position)) {
                parents_positions[k] = position;
                k++;
            }

        }
    return parents_positions;

    }

    private Integer[] makeRandomArray(int count){
        List<Integer> a = new ArrayList<>(population_size);
        for (int i = 0; i < population_size; i++){ //to generate from 0-10 inclusive.
            //For 0-9 inclusive, remove the = on the <=
            a.add(i);
        }
        Collections.shuffle(a);
        a = a.subList(0,count);
        Integer[] array = a.toArray (new Integer [count]);
        return array;
    }


    private int[] tournamentSelection(int number_of_parents,int slice){
        int[] parents_positions = new int[number_of_parents];

        int parent_index = 0;
        int k =0;
        while( k < number_of_parents) {
            double[] fitnesses = new double[slice];
            int[] indexes = new int[slice];
            for (int i = 0; i < slice; i++) {
                indexes[i] = randInt(0, population_size);
                fitnesses[i] = population.get(indexes[i]).getFitness();
            }
            double best_fit = 0;
            for (int i = 0; i < slice; i++) {
                if (fitnesses[i] > best_fit) {
                    best_fit = fitnesses[i];
                    parent_index = indexes[i];
                }
            }
            boolean unique = true;
            for (int  i = 0 ; i < number_of_parents;i++){
                if(parents_positions[i] == parent_index) unique = false;
            }
            if (unique) {
                parents_positions[k] = parent_index;
                k++;
            }
        }
        return parents_positions;
    }


    private void crossoverSinglePoint2(Genome parent0 , Genome parent1){
        int crossing_point = randInt(0,genome_size);

        double[] parent0_array = parent0.getAlleles();
        double[] parent1_array = parent1.getAlleles();


        double[] child0_array = new double[genome_size];
        double[] child1_array = new double[genome_size];



        for (int i = 0; i < genome_size; i++) {
            if (i < crossing_point) {
                child0_array[i] = parent0_array[i];
                child1_array[i] = parent1_array[i];
            } else {
                child0_array[i] = parent1_array[i];
                child1_array[i] = parent0_array[i];
            }
        }


        Genome child0 = new Genome(child0_array,0,false);
        Genome child1 = new Genome(child1_array,0,false);

        population.add(child0);population_size++;
        population.add(child1);population_size++;

    }

    private void crossoverAverage2(Genome parent0 , Genome parent1){
        int crossing_point_1 = randInt(0,genome_size);
        int crossing_point_2 = randInt(0,genome_size);


        double[] parent0_array = parent0.getAlleles();
        double[] parent1_array = parent1.getAlleles();


        double[] child0_array = new double[genome_size];
        double[] child1_array = new double[genome_size];

        double alpha = 0.95+ (0.1 * rnd_.nextDouble() );
        double beta = 0.95 + (0.1 * rnd_.nextDouble() );
        alpha = 1;
        beta =1;

        for (int i = 0; i < genome_size; i++) {
            if (i < crossing_point_1) {
                child0_array[i] = parent0_array[i] * alpha ;
                child1_array[i] = parent1_array[i] * alpha;
            } else if (i > crossing_point_2) {
                child0_array[i] = parent1_array[i] * beta;
                child1_array[i] = parent0_array[i] * beta;
            } else {
                child0_array[i] = parent0_array[i] + parent1_array[i] / 2 ;
                child1_array[i] = parent0_array[i] + parent1_array[i] / 2 ;
            }
        }

        Genome child0 = new Genome(child0_array,0,false);
        Genome child1 = new Genome(child1_array,0,false);

        population.add(child0);population_size++;
        population.add(child1);population_size++;
    }
    private void crossover3(Genome parent0 , Genome parent1,Genome parent2){
        int crossing_point_1 = randInt(0,genome_size);
        int crossing_point_2 = randInt(0,genome_size);

        double[] parent0_array = parent0.getAlleles();
        double[] parent1_array = parent1.getAlleles();
        double[] parent2_array = parent2.getAlleles();


        double[] child0_array = new double[genome_size];
        double[] child1_array = new double[genome_size];
        double[] child2_array = new double[genome_size];

        double alpha = 0.99+ (0.1 * rnd_.nextDouble() );
        double beta = 0.99 + (0.1 * rnd_.nextDouble() );


        for (int i = 0; i < genome_size; i++) {
            if (i < crossing_point_1) {
                child0_array[i] = parent0_array[i]  ;
                child1_array[i] = parent1_array[i] ;
                child2_array[i] = parent1_array[i] ;
            } else if (i > crossing_point_2) {
                child0_array[i] = parent2_array[i] ;
                child1_array[i] = parent0_array[i] ;
                child2_array[i] = parent1_array[i] ;
            } else {
                child0_array[i] = parent1_array[i] ;
                child1_array[i] = parent2_array[i] ;
                child2_array[i] = parent0_array[i] ;
            }
        }

        Genome child0 = new Genome(child0_array,0,false);
        Genome child1 = new Genome(child1_array,0,false);
        Genome child2 = new Genome(child2_array,0,false);

        population.add(child0);population_size++;
        population.add(child1);population_size++;
        population.add(child2);population_size++;
    }

    private void crossoverAverageN(Genome[] genomes, int N){

        double[] child_array = new double[genome_size];

        double avg =0;
        for (int i = 0; i < genome_size; i++) {
            for (int j = 0 ; j < N;j++){
                avg = avg + genomes[j].getAlleleAtIndex(i);
            }
            avg = avg /N;
            child_array[i] = avg;
        }

        Genome child0 = new Genome(child_array,0,false);

        population.add(child0);population_size++;
    }


    private int randInt(int low, int  high){
        int result = rnd_.nextInt(high-low) + low;
        return result;
    }


    private boolean related(Genome parent1 , Genome parent2){
        int  count = 0;
        for (int i = 0 ; i <10 ; i++){
            if (parent1.getAlleleAtIndex(i) == parent2.getAlleleAtIndex(i))
                count++;
        }
        if (count > 10)
            return true;
        else
            return false;
    }


}


