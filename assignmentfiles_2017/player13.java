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
	private int population_size = 10;
	public void run()
	{
		// Run your algorithm here

        int evals = 0;
        // init population
				double child[] = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};

				double[][] population = new double[][] { child, child, child, child, child,child, child, child, child, child  };


				for (int i = 0; i < population_size ;i++){
					for (int j = 0; j < 10 ;j++){
						population[i][j] = rnd_.nextDouble();
					}
				}
        // calculate fitness
        while(evals<evaluations_limit_){
            // Select parents
            // Apply crossover / mutation operators




						for (int i = 0 ; i < population_size ; i++){
							child = population[i];
            	// Check fitness of unknown fuction
            	Double fitness = (double) evaluation_.evaluate(child);
							System.out.println("evaluation " + evals +"  fitness of " + fitness );
							evals++;
						}
            // Select survivors
        }


	}
}
