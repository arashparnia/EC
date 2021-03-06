

import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


public class player13 implements ContestSubmission
{
	Random rnd_;
	ContestEvaluation evaluation_;
    private int evaluations_limit_;
    private long theseed;
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
		this.theseed = seed;
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
    int insest_count = 0;

//*************************************************************************************************

    private double evaluate(){
        double best_fitness = 0;
        for (Genome genome : population) {
            // Check fitness of unknown fuction
            if (!genome.isEvaluated() && evals < evaluations_limit_) {
                double fitness = (double) evaluation_.evaluate(genome.getAlleles());
//                if (fitness == 10.0) {System.out.println("Score: 10.0"); System.exit(10);}
                fitness += punish(genome,-1);
                genome.setFitness(fitness);
                genome.setEvaluated(true);
                if (fitness > best_fitness) {
                    best_fitness = fitness;
                    elite.setAlleles(genome.getAlleles());
                    elite.setMutation_rate(genome.getMutation_rate());
                    elite.setMutation_step(genome.getMutation_step());
                    elite.setFitness(fitness);
                    elite.setEvaluated(true);
                }
//                mutation_step *= 1 - cooling_rate;
                evals++;
            }
        }
    return best_fitness;
    }

    public void run() {


        if (!isMultimodal) {  // Bentcigar Fucntion

            population_size = 300;
            init_range = 1;

            genome_mutation_chance = 0.5;
            allele_mutation_chance = 0.1;
            mutation_step = 0.1;
            cooling_rate = 0.001;

            tournamentSelection_slice = 20;

            race_limit = 1;

            cutoff = 16;

        }

        if (isMultimodal && hasStructure) { //SchaffersEvaluation
//            905 , 0.01 , 1.0E-13 , 45 , 1.0E-4 , 40 , 5 , 0

            population_size = 905;
            init_range = 1;


            genome_mutation_chance = 0.001;
            allele_mutation_chance = 1;
            mutation_step = 0.0000000000001;

            cooling_rate = 0.0001;

            tournamentSelection_slice = 45;

            race_limit =5;
            cutoff = 40;
        }

        if(isMultimodal && !hasStructure) { //KatsuuraEvaluation

            population_size = 1003;
            init_range = 2;


            genome_mutation_chance = 0.01;
            allele_mutation_chance = 1;
            mutation_step = 0.000000000000001;

            cooling_rate = 0.00000001;

            tournamentSelection_slice = 30;

            race_limit = 6;
            cutoff = 40;
        }




        population_size = Integer.parseInt(System.getProperty("ps"));
        genome_mutation_chance = Double.parseDouble(System.getProperty("mutchance"));
        mutation_step = Double.parseDouble(System.getProperty("mutstep"));
        tournamentSelection_slice = Integer.parseInt(System.getProperty("slice"));
        cooling_rate = Double.parseDouble(System.getProperty("coolrate"));
        cutoff = Integer.parseInt(System.getProperty("cut"));
        race_limit = Integer.parseInt(System.getProperty("race"));
        System.out.println(population_size + " , " + genome_mutation_chance + " , " + mutation_step + " , " + tournamentSelection_slice + " , " + cooling_rate+ " , " + cutoff + " , " + race_limit + " , " + theseed );




        initPopulation(genome_size,init_range,mutation_step,allele_mutation_chance);
        splitPopulation(race_limit);

        // calculate fitness

        elite = new Genome();

        while (evals < evaluations_limit_) {

            double best_fitness = evaluate();

            if (round(best_fitness,3) == last_fitness) same_fitness++;
            last_fitness = round(best_fitness,3);


            int counters[] = new int[10];
//            int related_count = 0;
            for (Genome g : population){
//                for(Genome gg : population){
//                    if (isRelated(g.parents,gg.parents))  related_count++;
//                }
                for(int i = 0 ; i < 10; i++){
                    if (g.getRace()[0] == i ) counters[i]++;

                }
            }
//            System.out.println(Arrays.toString(counters) + " , " + best_fitness);

//            System.out.println("Generation:" + generations +
//                    " population:" + population_size+  " mutation_step:" + String.format("%.20f",mutation_step) +
//                    " evaluations:" + evals +" fitness:" + String.format("%.20f",best_fitness)  );
//
////            if (evals % 1000 == 0 )
//                System.out.println("Generation:" + generations +
//                        " population:" + population_size +
//                        " evaluations:" + evals + " fitness:" + String.format("%.20f", best_fitness));

//                System.out.println(generations +
//                        "," + String.format("%.20f",mutation_step) +
//                        "," + evals +"," + String.format("%.20f",best_fitness)+  " , " + Arrays.toString(counters) + "," + String.format("%.10f",best_fitness/evals)   );

//                System.out.println(Arrays.toString(elite.getAlleles()));


//  -------------------------------------------------------------------------------------------------------------
            if (!isMultimodal) { //BentCigar
                removeSublist(cutoff);

//              if( same_fitness > 2 && best_fitness >9.99 )  {
//                same_fitness=0;
//                cooling_rate *= 0.999;
//                }

                int[] parents_;
                parents_ = tournamentSelection(cutoff, tournamentSelection_slice,true);
                int i=0;
                while (i < cutoff) {
                    int[] parents_positions = {parents_[i + 0], parents_[i + 1]};
                    i+=2;
//                        crossover3(population.get(parents_positions[0]),
//                                population.get(parents_positions[1]),
//                                population.get(parents_positions[2]));
                        crossoverAverage2(population.get(parents_positions[0]),
                                population.get(parents_positions[1]));
//                    differentialCrossover(population.get(parents_positions[0]),
//                            population.get(parents_positions[1]),
//                            population.get(parents_positions[2]),
//                            population.get(parents_positions[3]),
//                            0.1);
                }
//                    if (best_fitness >8.0) mutation_step += -0.0000003;
//                if (best_fitness >9.99) cooling_rate = 0.002;
//                    if (best_fitness >9.999) tournamentSelection_slice -= 1;
//                    population.add(elite);population_size++;

            } else {
                //  ----------------------------------------------------------------------------------------------
                if (hasStructure) { //SchaffersEvaluation
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
                        differentialCrossover(population.get(parents_positions[0]),
                                population.get(parents_positions[1]),
                                population.get(parents_positions[2]),
                                population.get(parents_positions[3]),
                                0.75);
                    }
                } else {
                    //  ------------------------------------------------------------------------------------------
                    //KatsuuraEvaluation
//                        fittnessSharing(100);
                    if( same_fitness > 100 && best_fitness < 9 && best_fitness > 1)  {
                        same_fitness=0;
                        population_size--;
                    }

                    removeSublist(cutoff);
                    int[] parents_;
                    int race_ = 0;
                    if (generations % 1000 == 0) {
                        parents_ = tournamentSelection(cutoff, tournamentSelection_slice,true);
                        same_fitness = 0;
                    } else {
                        parents_ = tournamentSelection(cutoff, tournamentSelection_slice, race_,true);
                        if(race_ > race_limit)  race_ = 0;
                        else race_++;
                    }
                    int i = 0;
                    while (i < cutoff) {
                        int[] parents_positions = {parents_[i + 0], parents_[i + 1], parents_[i + 2], parents_[i + 3]};
                        i += 4;
                        differentialCrossover(population.get(parents_positions[0]),
                                population.get(parents_positions[1]),
                                population.get(parents_positions[2]),
                                population.get(parents_positions[3]),
                                0.9);
                    }
                }

            }
            mutatePopulation(genome_mutation_chance);




            generations++;
//                for (Genome g : population.getPopulation()) g.age();


//

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
                genome.setMutation_step_atIndex((rnd_.nextDouble() - 0.5) * mutation_step,j);
            }
//            genome.setMutation_rate( rnd_.nextDouble() * allele_mutation_chance);
//             genome.setMutation_step( (rnd_.nextDouble() - 0.5)   * mutation_step);
//
//            if (!isMultimodal) { // init bent cigar with best result
//                double[] a = {-0.8948347951638531, 3.99143693361479, 0.16449645927069387, -3.797251817454455, -0.45864942924049423, -2.085553637762654, 1.3816851497277238, -0.7350044005653567, 1.1497733679854212, -0.3053867475027876};
//                double[] a = {-0.894831, 3.991451, 0.164601, -3.797297, -0.458794, -2.08558, 1.38167, -0.73503, 1.14971, -0.30540};
//                double[] a = {-0.8948314584364004, 3.9914519259247703, 0.1646013222310741, -3.7972974764829286, -0.4587949667954735, -2.085584673861254, 1.381670815748054, -0.7350303559658339, 1.1497196890553316, -0.3054065498012922};
//                genome.setAlleles(a);
//            }
            if(isMultimodal && !hasStructure) { //KatsuuraEvaluation
//                double[] a = {-1.5678936170936817, -1.9048141681962176, 1.0643441126603221, 0.017262885573568, 1.0364991068867213, 0.31286631872572823, -0.28940432110507824, -1.0319460510269634, 0.7396639908101722, 1.796098700657845};
//                genome.setAlleles(a);
            }
            if (isMultimodal && hasStructure) { //SchaffersEvaluation
//                double[] a = {3.6559999992823213, 2.5496000001565333, -1.5296000015148958, 1.4695999981160883, 1.395999999031597, -1.9079999972541253, 3.501599995867545, -2.3503999999962897, -0.38400000396343514, -2.0360000008311308};
//                genome.setAlleles(a);
            }

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
    // stolen from : https://math.stackexchange.com/questions/484395/how-to-generate-a-cauchy-random-variable
    public static double nextCauchy(Random rnd) {
            double p = rnd.nextDouble();

                    while (p == 0. || p == 1.) {
                    p = rnd.nextDouble();
                }

            return Math.tan(Math.PI * (rnd.nextDouble() - .5));
    }

    public void mutatePopulation(double genome_mutation_chance) {

        for (Genome genome : population) {
            if (rnd_.nextDouble() < genome_mutation_chance && !genome.isEvaluated()) {

                for (int i =0;i < 10;i++){
                    double allele = genome.getAlleleAtIndex(i);
                    if (isMultimodal)
                        allele += nextCauchy(rnd_) * mutation_step ;
                    else {
//                        allele += (rnd_.nextGaussian()-0.5) * mutation_step ;
//                        allele += (rnd_.nextDouble()-0.5) * mutation_step ;
//                        allele += nextCauchy(rnd_)* mutation_step ;
                        genome.setMutation_step_atIndex(genome.getMutation_step_atIndex(i) + (rnd_.nextGaussian() - 0.5) * mutation_step, i);
                        allele += genome.getMutation_step_atIndex(i);
                    }
                    genome.setAlleleAtIndex(allele,i);
                }
                genome.setAge(0);
                genome.setEvaluated(false);
            }
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
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
//                if(isRelated(parents_positions[i],parent_index) ) {System.out.print("*INSEST*" + insest_count);insest_count++;}
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
//                if(isRelated(parents_positions[i],parent_index) ) {System.out.print("*INSEST*"+insest_count);insest_count++;}
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
        sortPopulation(true);
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

