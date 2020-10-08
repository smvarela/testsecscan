package es.smvarela;

/**
 * Class that defines an Individual
 */
public class Individual implements Comparable<Individual> {
    private Float fitness;
    private String genome;

    // Constructor
    Individual(float fitness, String genome) {
        this.fitness = fitness;
        this.genome = genome;
    }

    // Getters and setters
    public float getFitness() {
        return fitness;
    }

    public void setFitness(float fitness) {
        this.fitness = fitness;
    }

    String getGenome() {
        return genome;
    }

    public void setGenome(String genome) {
        this.genome = genome;
    }

    /**
     * Print the fitness and genome
     */
    @Override
    public String toString() {
        return "Individual{" +
                "fitness=" + fitness +
                ", genome='" + genome + '\'' +
                '}';
    }

    /**
     * Method that compares two objects by its fitness value
     */
    public int compareTo(Individual o) {
        // Sort by descending order using fitness property
        int result = 0;

        if (this.fitness < o.getFitness()) {
            result = 1;
        } else if (this.fitness > o.getFitness()) {
            result = -1;
        } else {
            result = 0;
        }
        return result;
    }
}
