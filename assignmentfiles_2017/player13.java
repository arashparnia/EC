

import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;


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

//        System.out.println("***********************************************************************************");
//        System.out.println("Algorithm properties");
//        System.out.println("***********************************************************************************");
//
//        System.out.println("properties " + props.toString());
//        System.out.println("evaluations_limit_ " + evaluations_limit_);
//        System.out.println("isMultimodal " + isMultimodal);
//        System.out.println("hasStructure " + hasStructure);
//        System.out.println("isSeparable "   + isSeparable);
//
//        System.out.println("***********************************************************************************");
//        System.out.println("***********************************************************************************");

        // Do sth with property values, e.g. specify relevant settings of your algorithm

    }
//*************************************************************************************************
	// private double child[] = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
	//hardcoded sizes
    boolean isMultimodal ;
    boolean hasStructure ;
    boolean isSeparable ;

	private int population_size = 100;
	private int population_max = 100;
    private static final int genome_size  = 10;
	private double init_range = 5;
	private int life_expectancy = 100;

	private double allele_mutation_chance = 0.01;
	private double mutation_step = 1;
    private double  genome_mutation_chance = 0.6;

    private double cooling_rate = 0.0001;

    int tournamentSelection_slice = 40;
    int cutoff = population_max/2;

    int race_limit = 1;
//    private int tournoment_size = 2;
    private Population population ;
	private Genome elite;
//    private Genome  previous_elite;
    private double last_fitness=0;
    private int same_fitness = 0;
    int generations = 0;
    int evals = 0;

//*************************************************************************************************

    private double evaluate(Population population){
        double best_fitness = 0;
        for (Genome genome : population.getPopulation()) {
            // Check fitness of unknown fuction
            if (!genome.isEvaluated() && evals < evaluations_limit_) {
                double fitness = (double) evaluation_.evaluate(genome.getAlleles());
                genome.setFitness(fitness);
                genome.setEvaluated(true);
                if (fitness > best_fitness) {
                    best_fitness = fitness;
                    elite.setAlleles(genome.getAlleles());
                    elite.setMutation_rate(genome.getMutation_rate());
                    elite.setMutation_step(genome.getMutation_step());
                    elite.setFitness(genome.getFitness());
                    elite.setEvaluated(true);
                }
                mutation_step *= 1 - cooling_rate;
                evals++;
            }
        }
    return best_fitness;
    }
	public void run() {

            if (!isMultimodal) {  // Bentcigar Fucntion

                population_size = 1000;
                population_max = 1000;
                life_expectancy = 1000;
                init_range = 2;

                genome_mutation_chance = 0.01;
                allele_mutation_chance = 0.1;
                mutation_step = 1;

                cooling_rate = 0.0000001;

                tournamentSelection_slice = 100;

                race_limit = 2;

                cutoff = population_max/10;
            }

            if (isMultimodal && hasStructure) { //SchaffersEvaluation

                    population_size = 1000;
                    population_max = 1000;
                    life_expectancy = 1000;
                    init_range = 2;


                    genome_mutation_chance = 0.1;
                    allele_mutation_chance = 0.1;
                    mutation_step = 0.01;

                    cooling_rate = 0.00001;

                    tournamentSelection_slice = 50;

                    race_limit = 2;
                cutoff = population_max/2;
            }

            if(isMultimodal && !hasStructure) { //KatsuuraEvaluation

                population_size = 1000;
                population_max = 1000;
                life_expectancy = 1000;
                init_range = 2;


                genome_mutation_chance = 0.1;
                allele_mutation_chance = 0.1;
                mutation_step = 0.01;

                cooling_rate = 0.0001;

                tournamentSelection_slice = 50;

                race_limit = 2;
                cutoff = population_max/2;
            }



            population = new Population(population_max);

//         evaluations_limit_ = 100;

            // Run your algorithm here

            // init population
            //declaring populaton
            population.initPopulation(genome_size,init_range,mutation_step,allele_mutation_chance);
            population.splitPopulation(race_limit);

            // calculate fitness

            elite = new Genome();

            while (evals < evaluations_limit_) {

                double best_fitness = evaluate(population);

                if (best_fitness == last_fitness) same_fitness++;
//                double f =
//            System.out.println("Generation:" + generations +
//                    " population:" + population_size+  " mutation_step:" + String.format("%.20f",mutation_step) +
//                    " evaluations:" + evals +" fitness:" + String.format("%.20f",best_fitness) );

//            if (evals % 1000 == 0 )
                System.out.println("Generation:" + generations +
                        " population:" + population_size +
                        " evaluations:" + evals + " fitness:" + String.format("%.20f", best_fitness));


                if (!isMultimodal) { //BentCigar
//                fittnessSharing(10);
//                    removeOld();
                        int[] parents_;
                        if (same_fitness == 1000) {
                            parents_ = tournamentSelection(cutoff, tournamentSelection_slice);
                            same_fitness =0;
                        }
                        else
                            parents_ = tournamentSelection(cutoff, tournamentSelection_slice,randInt(0,race_limit-1));

                        int i=0;
                        while (i < cutoff) {
                            int[] parents_positions = {parents_[i+0], parents_[i+1], parents_[i+2], parents_[i+3]};

                            i+=4;

                        if (
                                !isRelated(population.get(parents_positions[0]).parents, population.get(parents_positions[1]).parents) &&
                                        !isRelated(population.get(parents_positions[0]).parents, population.get(parents_positions[2]).parents) &&
                                        !isRelated(population.get(parents_positions[0]).parents, population.get(parents_positions[3]).parents) &&
                                        !isRelated(population.get(parents_positions[1]).parents, population.get(parents_positions[2]).parents) &&
                                        !isRelated(population.get(parents_positions[1]).parents, population.get(parents_positions[3]).parents) &&
                                        !isRelated(population.get(parents_positions[2]).parents, population.get(parents_positions[3]).parents)

                                        &&

                                        !isRelated(population.get(parents_positions[0]).parents, population.get(parents_positions[1])) &&
                                        !isRelated(population.get(parents_positions[0]).parents, population.get(parents_positions[2])) &&
                                        !isRelated(population.get(parents_positions[0]).parents, population.get(parents_positions[3])) &&
                                        !isRelated(population.get(parents_positions[1]).parents, population.get(parents_positions[2])) &&
                                        !isRelated(population.get(parents_positions[1]).parents, population.get(parents_positions[3])) &&
                                        !isRelated(population.get(parents_positions[2]).parents, population.get(parents_positions[3]))
                                )
                            differentialCrossover(population.getPopulation(),population.get(parents_positions[0]),
                                    population.get(parents_positions[1]),
                                    population.get(parents_positions[2]),
                                    population.get(parents_positions[3]),
                                    1);
                    }




                } else {
                    if (hasStructure) { //SchaffersEvaluation
//
//                        removeOld();
//
//                        fittnessSharing(10);

                        int[] parents_;
                        if (same_fitness == 100) {
                            parents_ = tournamentSelection(cutoff, tournamentSelection_slice);
                            same_fitness =0;
                        }
                        else
                            parents_ = tournamentSelection(cutoff, tournamentSelection_slice,randInt(0,race_limit-1));
                        int i=0;
                        while (i < cutoff) {
                            int[] parents_positions = {parents_[i+0], parents_[i+1], parents_[i+2], parents_[i+3]};

                            i+=4;

                          if(!isRelated(parents_positions))
                                differentialCrossover(population.getPopulation(),population.get(parents_positions[0]),
                                        population.get(parents_positions[1]),
                                        population.get(parents_positions[2]),
                                        population.get(parents_positions[3]),
                                        1);

//                                population.remove(population.get(parents_[0]));population_size--;
//                            population.remove(population.get(parents_[1]));population_size--;
//                            population.remove(population.get(parents_[2]));population_size--;
//                            population.remove(population.get(parents_[3]));population_size--;
//                        else
//                            System.out.println("INSEST ALERT!!!!!");
                            population.add(elite);population_size++;
//
//                            mutatePopulation();

                        }
                    } else { //KatsuuraEvaluation

//                        removeOld();

//                        fittnessSharing(10);

                        int cutoff = population_max/2;
                            int[] parents_;
                            if (same_fitness == 100) {
                                parents_ = tournamentSelection(cutoff, tournamentSelection_slice);
                                same_fitness =0;
                            }
                            else
                                parents_ = tournamentSelection(cutoff, tournamentSelection_slice,randInt(0,race_limit-1));
                        int i=0;
                            while (i < cutoff) {
                            int[] parents_positions = {parents_[i+0], parents_[i+1], parents_[i+2], parents_[i+3]};

                            i+=4;

                            if (
                                    !isRelated(population.get(parents_positions[0]).parents, population.get(parents_positions[1]).parents) &&
                                            !isRelated(population.get(parents_positions[0]).parents, population.get(parents_positions[2]).parents) &&
                                            !isRelated(population.get(parents_positions[0]).parents, population.get(parents_positions[3]).parents) &&
                                            !isRelated(population.get(parents_positions[1]).parents, population.get(parents_positions[2]).parents) &&
                                            !isRelated(population.get(parents_positions[1]).parents, population.get(parents_positions[3]).parents) &&
                                            !isRelated(population.get(parents_positions[2]).parents, population.get(parents_positions[3]).parents)

                                            &&

                                            !isRelated(population.get(parents_positions[0]).parents, population.get(parents_positions[1])) &&
                                            !isRelated(population.get(parents_positions[0]).parents, population.get(parents_positions[2])) &&
                                            !isRelated(population.get(parents_positions[0]).parents, population.get(parents_positions[3])) &&
                                            !isRelated(population.get(parents_positions[1]).parents, population.get(parents_positions[2])) &&
                                            !isRelated(population.get(parents_positions[1]).parents, population.get(parents_positions[3])) &&
                                            !isRelated(population.get(parents_positions[2]).parents, population.get(parents_positions[3]))
                                    )
                                differentialCrossover(population.getPopulation(),population.get(parents_positions[0]),
                                        population.get(parents_positions[1]),
                                        population.get(parents_positions[2]),
                                        population.get(parents_positions[3]),
                                        0.1);

//                                population.remove(population.get(parents_[0]));population_size--;
//                            population.remove(population.get(parents_[1]));population_size--;
//                            population.remove(population.get(parents_[2]));population_size--;
//                            population.remove(population.get(parents_[3]));population_size--;
//                        else
//                            System.out.println("INSEST ALERT!!!!!");
                    population.add(elite);population_size++;
//
//                            mutatePopulation();
                        }
                    }
                }


                population.mutatePopulation(genome_mutation_chance);


                population.removeSublist(population_max/2);

                generations++;
//                for (Genome g : population.getPopulation()) g.age();


            }


//            System.out.println(Arrays.toString(elite.getAlleles()));
//            System.out.println(elite.getFitness());
//            System.out.println(elite.getMutation_rate());
        }
        //*************************************************************************************************






	//todo: add mutation addition vector




//    private void removeOld(){
//        Iterator<Genome> i = population.iterator();
//        while (i.hasNext()) {
//            Genome g = i.next();
//            if(g.getAge() > life_expectancy ) {
//                i.remove();population_size--;
//            }
//        }
////        while (population_size < population_max){
////            Genome genome = elite;
////            Genome genome = new Genome();
////
////            for (int j = 0; j < genome_size; j++) {
////
////                genome.setAlleleAtIndex((init_range * (rnd_.nextDouble() -0.5)),j);
////                genome.setMutation_step_atIndex((rnd_.nextDouble() - 0.5)   * mutation_step,j);
////            }
////            genome.setMutation_rate( rnd_.nextDouble() * allele_mutation_chance);
////            population.add(genome);
////            population_size++;
////        }
//	}



//todo: make punishment on fitneess
    private double checkAllele(double allele){
	    double r = allele;
	    if (allele > 5 ) r = 5;
	    if (allele < -5) r =  -5;
	    return  r;
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
                if (fitnesses[i] > best_fit && fitnesses[i] > 0) {
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

    private int[] tournamentSelection(int number_of_parents,int slice,int race){
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
                if (fitnesses[i] > best_fit && fitnesses[i] > 0) {
                    best_fit = fitnesses[i];
                    parent_index = indexes[i];
                }
            }
            boolean unique = true;
            for (int  i = 0 ; i < number_of_parents;i++){
                if(parents_positions[i] == parent_index || population.get(parent_index).getRace()[0] != race) unique = false;
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

        double r =  parent0.getMutation_rate() + parent1.getMutation_rate() ;
        Genome child0 = new Genome(child0_array,0,false,parent0.getMutation_rate(),parent0.getMutation_step(),parent0.getRace());
        Genome child1 = new Genome(child1_array,0,false,parent1.getMutation_rate(),parent1.getMutation_step(),parent1.getRace());

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

        Genome child0 = new Genome(child0_array,0,false,parent0.getMutation_rate(),parent0.getMutation_step(),parent0.getRace());
        Genome child1 = new Genome(child1_array,0,false,parent1.getMutation_rate(),parent1.getMutation_step(),parent1.getRace());

        population.add(child0);population_size++;
        population.add(child1);population_size++;
    }
    private void crossover3(ArrayList<Genome> population,Genome parent0 , Genome parent1,Genome parent2){
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

        Genome child0 = new Genome(child0_array,0,false,parent0.getMutation_rate(),parent0.getMutation_step(),parent0.getRace());
        Genome child1 = new Genome(child1_array,0,false,parent1.getMutation_rate(),parent1.getMutation_step(),parent1.getRace());
        Genome child2 = new Genome(child2_array,0,false,parent2.getMutation_rate(),parent2.getMutation_step(),parent2.getRace());


        population.add(child0);population_size++;
        population.add(child1);population_size++;
        population.add(child2);population_size++;
    }

    private void uniformCrossover(Genome parent1, Genome parent2){
        for (int i = 0 ; i < genome_size ; i++){
            if (rnd_.nextBoolean()) {
                parent1.setAlleleAtIndex(parent2.getAlleleAtIndex(i),i);
            } else {
                parent2.setAlleleAtIndex(parent1.getAlleleAtIndex(i),i);
            }
        }
    }
    private void differentialCrossover(Genome parent0 , Genome parent1,double scaling_factor){


        double[] parent0_array = parent0.getAlleles();
        double[] parent1_array = parent1.getAlleles();

        double[] child0_array = new double[genome_size];
        double[] child1_array = new double[genome_size];

        for (int i = 0; i < genome_size; i++) {
            if (rnd_.nextBoolean()) {
                child0_array[i] = parent0_array[i];
                child1_array[i] = parent1_array[i];
            }
            else {
                child0_array[i] = parent1_array[i];
                child1_array[i] = parent0_array[i];

            }

            child0_array[i] = child0_array[i] + ((parent0_array[i] - parent1_array[i]) * scaling_factor);
            child1_array[i] = child1_array[i] + ((parent1_array[i] - parent0_array[i]) * scaling_factor);
        }

//        parent0.setMutation_step(parent0.getMutation_step()*mutation_step);
//        parent1.setMutation_step(parent1.getMutation_step()*mutation_step);

        Genome child0 = new Genome(child0_array,0,false,parent0.getMutation_rate(),parent0.getMutation_step(),parent0.getRace());
        Genome child1 = new Genome(child1_array,0,false,parent1.getMutation_rate(),parent1.getMutation_step(),parent1.getRace());


        population.add(child0);population_size++;
        population.add(child1);population_size++;
    }

    private void differentialCrossover(ArrayList<Genome> population,Genome parent0 , Genome parent1,Genome parent2, Genome parent3,double scaling_factor){


        double[] parent0_array = parent0.getAlleles();
        double[] parent1_array = parent1.getAlleles();
        double[] parent2_array = parent2.getAlleles();
        double[] parent3_array = parent3.getAlleles();

        double[] child0_array = new double[genome_size];
        double[] child1_array = new double[genome_size];
        double[] child2_array = new double[genome_size];
        double[] child3_array = new double[genome_size];

        for (int i = 0; i < genome_size; i++) {
            if (rnd_.nextBoolean()) {
                child0_array[i] = parent0_array[i];
                child1_array[i] = parent1_array[i];
                child2_array[i] = parent0_array[i];
                child3_array[i] = parent1_array[i];
            }
            else {
                child0_array[i] = parent1_array[i];
                child1_array[i] = parent0_array[i];
                child2_array[i] = parent1_array[i];
                child3_array[i] = parent0_array[i];

            }

            child0_array[i] = child0_array[i] + ((parent2_array[i] - parent3_array[i]) * scaling_factor);
            child1_array[i] = child1_array[i] + ((parent3_array[i] - parent2_array[i]) * scaling_factor);
            child2_array[i] = child2_array[i] + ((parent2_array[i] - parent3_array[i]) * scaling_factor);
            child3_array[i] = child3_array[i] + ((parent3_array[i] - parent2_array[i]) * scaling_factor);
        }

        parent0.setMutation_step(parent0.getMutation_step());
        parent1.setMutation_step(parent1.getMutation_step());
        parent2.setMutation_step(parent2.getMutation_step());
        parent3.setMutation_step(parent3.getMutation_step());

        Genome child0 = new Genome(child0_array,0,false,parent0.getMutation_rate(),parent0.getMutation_step(),parent0.getRace());
        Genome child1 = new Genome(child1_array,0,false,parent1.getMutation_rate(),parent1.getMutation_step(),parent1.getRace());
        Genome child2 = new Genome(child0_array,0,false,parent2.getMutation_rate(),parent2.getMutation_step(),parent2.getRace());
        Genome child3 = new Genome(child1_array,0,false,parent3.getMutation_rate(),parent3.getMutation_step(),parent3.getRace());


        child0.parents[0]= parent0;
        child0.parents[1]= parent1;
        child0.parents[2]= parent2;
        child0.parents[3]= parent3;

        child1.parents[0]= parent0;
        child1.parents[1]= parent1;
        child1.parents[2]= parent2;
        child1.parents[3]= parent3;

        child2.parents[0]= parent0;
        child2.parents[1]= parent1;
        child2.parents[2]= parent2;
        child2.parents[3]= parent3;

        child3.parents[0]= parent0;
        child3.parents[1]= parent1;
        child3.parents[2]= parent2;
        child3.parents[3]= parent3;



        population.add(child0);population_size++;
        population.add(child1);population_size++;
        population.add(child2);population_size++;
        population.add(child3);population_size++;
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


        Genome child0 = new Genome(child_array,0,false,genomes[0].getMutation_rate(),genomes[0].getMutation_step(),genomes[0].getRace());


        population.add(child0);population_size++;
    }


    private boolean isRelated(Genome[] parent1,Genome[] parent2){
        boolean related = false;
        if (parent1[0] == null || parent2[0] == null ) return related;
        if( Arrays.deepEquals(parent1,parent2) ) related = true;
        return related;
    }

    private boolean isRelated(Genome[] parent1,Genome parent2){
        boolean related = false;
        if (parent1[0] == null || parent2 == null ) return related;
        if (parent1[0] == parent2 || parent1[1] == parent2 || parent1[2] == parent2 || parent1[3] == parent2 ) related = true;
        return related;
    }

    private boolean isRelated(int[] parents_positions){
        boolean related = true;
        if (
                !isRelated(population.get(parents_positions[0]).parents, population.get(parents_positions[1]).parents) &&
                        !isRelated(population.get(parents_positions[0]).parents, population.get(parents_positions[2]).parents) &&
                        !isRelated(population.get(parents_positions[0]).parents, population.get(parents_positions[3]).parents) &&
                        !isRelated(population.get(parents_positions[1]).parents, population.get(parents_positions[2]).parents) &&
                        !isRelated(population.get(parents_positions[1]).parents, population.get(parents_positions[3]).parents) &&
                        !isRelated(population.get(parents_positions[2]).parents, population.get(parents_positions[3]).parents)

                        &&

                        !isRelated(population.get(parents_positions[0]).parents, population.get(parents_positions[1])) &&
                        !isRelated(population.get(parents_positions[0]).parents, population.get(parents_positions[2])) &&
                        !isRelated(population.get(parents_positions[0]).parents, population.get(parents_positions[3])) &&
                        !isRelated(population.get(parents_positions[1]).parents, population.get(parents_positions[2])) &&
                        !isRelated(population.get(parents_positions[1]).parents, population.get(parents_positions[3])) &&
                        !isRelated(population.get(parents_positions[2]).parents, population.get(parents_positions[3]))
                ) related = false;
        return related;
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


/*
exam answer

graph coloring problem

connected nodes should have different colors

constraint satisfaction problem
x<x1,x2,x3,...,xn> = <r,b,r,g,b,y,r,..>
E={(1,2) , (1,3) , (2,4) , ...} and calculate the connected nodes if they have same color or not
2
4
you have more information because yu have more edges than nodes so the first one is better
string array
single point , multipoint , uniform
random reset,
tournament
random
when solution found

 from permutatin  try to fill  each node by rgb





 */

