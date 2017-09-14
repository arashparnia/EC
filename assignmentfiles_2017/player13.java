import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.util.Properties;
import java.util.Vector;

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
        boolean isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
        boolean hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        boolean isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));

		// Do sth with property values, e.g. specify relevant settings of your algorithm
        if(isMultimodal){
            // Do sth
        }else{
            // Do sth else
        }
    }

	// private double child[] = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
	//hardcoded sizes
	private int population_size = 1000;
    private static final int genome_size  = 10;
	private double init_range = 1;
	private double mutation_chance = 0.2;
	private double mutation_step = 0.2;
    private int tournoment_size = 2;
    private Vector<double[]> population=new Vector<double[]>();
//	private double[][] population = new double[population_size][genome_size];
	private double[] population_fitness = new double[population_size];

	public void run()
	{
		// Run your algorithm here
		int evals = 0;
    // init population
		//declaring populaton
		initPopulation();
        
		// calculate fitness
		double best_fitness = -9999999.0;
		while(evals< evaluations_limit_){
			// for every genome in population loop
            for (double[] child : population){
				// Check fitness of unknown fuction
				double fitness = (double) evaluation_.evaluate(child);
				if (fitness > best_fitness)
				{
					best_fitness = fitness;
				}
				System.out.println("evaluation " + evals +"  fitness of " + String.format("%.20f",fitness) );
				evals++;
			}


			// Select parents
			// Apply crossover / mutation operators

			// Select survivors


//			mutatePopulation();

		}
				// System.out.println("best " + bsest_fitness);

	}


	public void initPopulation()
	{
		// randomly initilaizing all population
//        for (Object obj : vector) {
//            if (obj instanceof Method) {
//                list.add(obj);
//            }
//        }s
        for (int i = 0 ; i < population_size ; i++){
            double[] child = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
            population.add(child);
        }
        for (double[] genome: population) {
            for (int j = 0; j < genome_size; j++) {
                genome[j] = init_range * rnd_.nextDouble();
            }
        }

	}

	public void mutatePopulation(){
        for (double[] genome: population) {
			for (int j = 0; j < genome_size ;j++){
				if (rnd_.nextDouble() < mutation_chance){
                    genome[j] = genome[j] + ((1 - rnd_.nextDouble()) * mutation_step);
					// System.out.println(rnd_.nextDouble());
				}
			}
		}
	}


//    public void parentSelection(){
////        double[][] selection = new double[population_size][genome_size];
//        for(int i = 0 ; i < population_size ; i ++){
//
//        }
//    }
//
//	public void tournometSelection(){
////		double[][] selection = new double[population_size][genome_size];
//		for (int i = 0 ; i < tournoment_size ; i ++){
//
//		}
//
//	}









}
