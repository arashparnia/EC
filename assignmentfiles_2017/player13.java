

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
    private static final int genome_size  = 10;
	private double init_range = 5;
	private int life_expectancy = 100;

	private double allele_mutation_chance = 0.01;
	private double mutation_step = 1;
    private double  genome_mutation_chance = 0.6;

    private double cooling_rate = 0.0001;

    int tournamentSelection_slice = 40;
    int cutoff = 4;

    int race_limit = 1;
//    private int tournoment_size = 2;
    private List<Genome> population = new ArrayList<>(population_size) ;
	private Genome elite;
//    private Genome  previous_elite;
    private double last_fitness=0;
    private int same_fitness = 0;
    int generations = 0;
    int evals = 0;

//*************************************************************************************************

    private double evaluate(){
        double best_fitness = 0;
        for (Genome genome : population) {
            // Check fitness of unknown fuction
            if (!genome.isEvaluated() && evals < evaluations_limit_) {
                double fitness = (double) evaluation_.evaluate(genome.getAlleles());
                fitness += punish(genome,-1);
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

                population_size = 100;
                init_range = 2;

                genome_mutation_chance = 0.02;
                allele_mutation_chance = 0.1;
                mutation_step = 1.6;

                cooling_rate = 0.001;

                tournamentSelection_slice = 10;

                race_limit = 1;

                cutoff = 12;
            }

            if (isMultimodal && hasStructure) { //SchaffersEvaluation

                    population_size = 1000;
                    init_range = 1;


                    genome_mutation_chance = 0.01;
                    allele_mutation_chance = 0.1;
                    mutation_step = 0.0005;

                    cooling_rate = 0.000000001;

                    tournamentSelection_slice = 40;

                    race_limit =5;
                    cutoff = 40;
            }

            if(isMultimodal && !hasStructure) { //KatsuuraEvaluation

                population_size = 1000;
                init_range = 1;


                genome_mutation_chance = 0.01;
                allele_mutation_chance = 0.1;
                mutation_step = 0.0001;

                cooling_rate = 0.00000000000001;

                tournamentSelection_slice = 40;

                race_limit = 5;
                cutoff = 40;
            }



            initPopulation(genome_size,init_range,mutation_step,allele_mutation_chance);
            splitPopulation(race_limit);

            // calculate fitness

            elite = new Genome();

            while (evals < evaluations_limit_) {

                double best_fitness = evaluate();

                if (best_fitness == last_fitness) same_fitness++;


//            System.out.println("Generation:" + generations +
//                    " population:" + population_size+  " mutation_step:" + String.format("%.20f",mutation_step) +
//                    " evaluations:" + evals +" fitness:" + String.format("%.20f",best_fitness) );
//
////            if (evals % 1000 == 0 )
//                System.out.println("Generation:" + generations +
//                        " population:" + population_size +
//                        " evaluations:" + evals + " fitness:" + String.format("%.20f", best_fitness));


                if (!isMultimodal) { //BentCigar
                    removeSublist(cutoff);
                    int[] parents_;
                    parents_ = tournamentSelection(cutoff, tournamentSelection_slice,true);
                    int i=0;
                    while (i < cutoff) {
                        int[] parents_positions = {parents_[i+0], parents_[i+1], parents_[i+2], parents_[i+3]};
                        i+=4;
                        differentialCrossover(population.get(parents_positions[0]),
                                population.get(parents_positions[1]),
                                population.get(parents_positions[2]),
                                population.get(parents_positions[3]),
                                0.009);
                    }
//                    population.add(elite);population_size++;

                } else {
                    if (hasStructure) { //SchaffersEvaluation
//                        fittnessSharing(10);
                        removeSublist(cutoff);
                        int[] parents_;
                        int race_ = 0;
                        if (generations % 100 == 0) {
                            parents_ = tournamentSelection(cutoff, tournamentSelection_slice,true);
                            same_fitness = 0;
                        } else{

                            parents_ = tournamentSelection(cutoff, tournamentSelection_slice, race_,true);
                            if(race_ > race_limit)  race_ = 0;
                            else race_++;

                        }

                        int i = 0;
                        while (i < cutoff) {
                            int[] parents_positions = {parents_[i + 0], parents_[i + 1], parents_[i + 2], parents_[i + 3]};

                            i += 4;

//                          if(!isRelated(parents_positions))
                            differentialCrossover(population.get(parents_positions[0]),
                                    population.get(parents_positions[1]),
                                    population.get(parents_positions[2]),
                                    population.get(parents_positions[3]),
                                    0.9);

//                            population.add(elite);population_size++;
                        }
                    } else { //KatsuuraEvaluation
//                        fittnessSharing(100);
                        removeSublist(cutoff);
                        int[] parents_;
                        int race_ = 0;
                        if (generations % 1000 == 0) {
                            parents_ = tournamentSelection(cutoff, tournamentSelection_slice,true);
                            same_fitness = 0;
                        } else
                        {

                            parents_ = tournamentSelection(cutoff, tournamentSelection_slice, race_,true);
                            if(race_ > race_limit)  race_ = 0;
                            else race_++;

                        }
                        int i = 0;
                        while (i < cutoff) {
                            int[] parents_positions = {parents_[i + 0], parents_[i + 1], parents_[i + 2], parents_[i + 3]};

                            i += 4;

//                          if(!isRelated(parents_positions))
                            differentialCrossover(population.get(parents_positions[0]),
                                    population.get(parents_positions[1]),
                                    population.get(parents_positions[2]),
                                    population.get(parents_positions[3]),
                                    0.9);

//                            population.add(elite);population_size++;


                        }
                    }

                }
                mutatePopulation(genome_mutation_chance);




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
//            genome.setMutation_rate( rnd_.nextDouble() * allele_mutation_chance);
//             genome.setMutation_step( (rnd_.nextDouble() - 0.5)   * mutation_step);
//            double[] a = {-0.9858244722505388, 3.999176232240111, -0.042124887755302494, -3.6947898099101364, -0.15798503947882903, -2.0025428067422912, 1.313564593538192, -0.7562533345677892, 1.2623663203163324, -0.349182894330935};
//            double[] b = {-0.93048292515929, 3.9948649712850046, 0.0849646806798547, -3.7573004022882786, -0.34321694328776, -2.0536289069143745, 1.354830646277407, -0.7428873166019453, 1.1918768257314292, -0.3224792439100581};
//            genome.setAlleles(b);

        }
    }

//todo: make punishment on fitneess
    private double punish(Genome genome, double rate){
        double score = 0 ;
        for (int i = 0 ; i < 10;i++){
            double allele = genome.getAlleleAtIndex(i);
            if (allele > 5 || allele < -5)  score += rate;
        }

	    return  score;
    }



    public void mutatePopulation(double genome_mutation_chance) {

        for (Genome genome : population) {
            if (rnd_.nextDouble() < genome_mutation_chance) {

                for (int i =0;i < 10;i++){
                    double allele = genome.getAlleleAtIndex(i);
//            allele += (rnd_.nextDouble()-0.5) * genome.getMutation_step_atIndex(i);
                    allele += (rnd_.nextDouble()-0.5) * mutation_step ;

                    genome.setAlleleAtIndex(allele,i);
                }
                genome.setAge(0);
                genome.setEvaluated(false);
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


    private int[] tournamentSelection(int number_of_parents,int slice,boolean insest){
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
                if(isRelated(parents_positions[i],parent_index) && !insest) unique = false;
            }
            if (unique) {
                parents_positions[k] = parent_index;
                k++;
            }
        }
        return parents_positions;
    }

    private int[] tournamentSelection(int number_of_parents,int slice,int race,boolean insest){
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
                if(isRelated(parents_positions[i],parent_index) && !insest) unique = false;
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

    private void differentialCrossover(Genome parent0 , Genome parent1,Genome parent2, Genome parent3,double scaling_factor){


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

    private boolean isRelated(int parents_position1 , int parents_position2){
        boolean related = true;
        if (
                !isRelated(population.get(parents_position1).parents, population.get(parents_position2).parents) &&
                        !isRelated(population.get(parents_position2).parents, population.get(parents_position1).parents)
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






    public void sortPopulation(boolean reverse){
        Collections.sort(population);
        if (reverse) Collections.reverseOrder();
    }
    public void shufflePopulation(){
        Collections.shuffle(population);
    }
    public void removeSublist(int range){
        sortPopulation(false);
        population.subList(0,range).clear();
        population_size -= range;
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


//    private void removeWorst(int size){
////        Iterator<Genome> i = population.iterator();
////        while (i.hasNext()) {
////            Genome g = i.next();
////            if(g.getAge() > life_expectancy && population_size > 10) {
////                i.remove();population_size--;
////                System.out.println("removed");
////            }
////        }
//
//        Genome worst = new Genome();
//        double low = 11 ;
//        for (int k = 0 ; k < size;k++) {
//            for (Genome g : population){
//                if (g.getFitness() < low) {worst = g;low = g.getFitness();}
//            }
//            population.remove(worst); population_size--;
//        }
//    }


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

