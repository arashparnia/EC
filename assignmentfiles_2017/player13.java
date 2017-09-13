import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.util.Properties;

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
	private int population_size = 10;
	private int genome_size = 10;
	private double mutation_chance = 0.5;
	public void run()
	{
		// Run your algorithm here

        int evals = 0;
        // init population
        double child[] ;//= {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
				//declaring populaton
				double[][] population = new double[population_size][genome_size];

				// randomly initilaizing all population
				for (int i = 0; i < population_size ;i++){
					for (int j = 0; j < genome_size ;j++){
						population[i][j] = rnd_.nextDouble();
					}
				}

        // calculate fitness
				double best_fitness = -9999999.0;
        while(evals< 500 ){//evaluations_limit_){
						for (int i = 0; i < population_size ;i++){
							for (int j = 0; j < genome_size ;j++){
								if (rnd_.nextDouble() > mutation_chance){
									population[i][j] = population[i][j] + (1 - rnd_.nextDouble());
									// System.out.println(rnd_.nextDouble());
								}
							}
						}
            // Select parents
            // Apply crossover / mutation operators
						// for every genome in population loop
						for (int i = 0 ; i < population_size ; i++){
							child = population[i];
            	// Check fitness of unknown fuction
            	Double fitness = (double) evaluation_.evaluate(child);
							if (fitness > best_fitness) {
								best_fitness = fitness;
							}
							System.out.println("evaluation " + evals +"  fitness of " + fitness );
							evals++;
						}
            // Select survivors
        }
				// System.out.println("best " + bsest_fitness);


	}
}
