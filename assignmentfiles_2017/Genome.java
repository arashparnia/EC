/**
 * Created by arash on 9/14/17.
 */
public class Genome {
    private double[] alleles;
    private double fittness;
    private boolean evaluated;

    public Genome(double[] alleles, double fittness, boolean evaluated) {
        this.alleles = alleles;
        this.fittness = fittness;
        this.evaluated = evaluated;
    }

    public Genome(){
        alleles = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        this.alleles = alleles;
        this.fittness = -999999999;
        this.evaluated = false;
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

    public double getFittness() {
        return fittness;
    }

    public void setFittness(double fittness) {
        this.fittness = fittness;
    }

    public boolean isEvaluated() {
        return evaluated;
    }

    public void setEvaluated(boolean evaluated) {
        this.evaluated = evaluated;
    }
}
