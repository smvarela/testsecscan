package es.smvarela;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class implements a genetic algorithm that starts with a
 * base population generated randomly.
 * The genetic algorithm generates new individuals with the combination
 * of two parents and adds some random mutation.
 * Iterates until a certain number of iterations or the most fit string is found.
 */
public class Eden {

    // Global variables
    private int POPULATION = 40;
    private int MAX_ITERATIONS = 1000000;
    private double MUTATION_RATE = 0.05;
    private float FITNESS = 1f;
    private static int NEW_INDIVIDUALS = 25;

    private String OBJECTIVE;
    private int GENOME_SIZE;

    // ASCII values
    private static int ASCII_MIN = 32;
    private static int ASCII_MAX = 126;

    private int newRandomGen;
    private List<Individual> population;
    private List<Float> log;

    // Constructors
    public Eden(String objective) {
        this.OBJECTIVE = objective;
        this.GENOME_SIZE = OBJECTIVE.length();
    }

    public Eden(int population, int iterations, double mutation_rate, float fitness, String objective) {
        POPULATION = population;
        MAX_ITERATIONS = iterations;
        MUTATION_RATE = mutation_rate;
        FITNESS = fitness;
        this.OBJECTIVE = objective;
        this.GENOME_SIZE = OBJECTIVE.length();
    }


    // Methods

    /**
     * Initialize the first generation of Individuals
     */
    private void initialize_eden() {
        population = new ArrayList<Individual>();

        log = new ArrayList<>();

        for (int i = 0; i < POPULATION; i++) {
            population.add(generateCandidate());
        }

        // Sort individuals in populations using its fitness value
        Collections.sort(population);
    }


    /**
     * Generate a new Individual
     */
    private Individual generateCandidate() {
        String genome = generate_genome();
        float fitness = fitness(genome);

        return new Individual(fitness, genome);
    }

    /**
     * Generate a new genome
     */
    private String generate_genome() {
        StringBuilder stringBuffer = new StringBuilder();

        for (int i = 0; i < GENOME_SIZE; i++) {
            stringBuffer.append(generate_gen());
        }

        return stringBuffer.toString();
    }

    /**
     * Generate a single random gen
     */
    private char generate_gen() {
        newRandomGen = ThreadLocalRandom.current().nextInt(ASCII_MIN, ASCII_MAX + 1);

        return (char) newRandomGen;
    }


    /**
     * Run the genetic algorithm
     */
    public void evolve() {
        int i = 0;

        initialize_eden();

        while (i < MAX_ITERATIONS) {

            System.out.println("Iteration: " + i + " -- Fitness: " + population.get(0).getFitness() + " genome: " + population.get(0).getGenome());

            // Log statistics
            log.add(population.get(0).getFitness());

            // Check if best Individual fitness is good enough
            if (population.get(0).getFitness() >= FITNESS) {
                break;
            }

            // Allow to the best Individuals to produce new Individuals
            for (int k = 0; k < NEW_INDIVIDUALS; k++) {
                // Combine genome from best parents in population
                Individual newIndividual = combineGenome(population.get(k), population.get(k + 1));

                // Check if new mutation occurs
                newIndividual = mutateGenome(newIndividual);

                // Add new Individual to the population
                population.add(newIndividual);
            }

            // Sort population by its fitness value
            Collections.sort(population);

            // Remove the worst Individuals to keep the same population size
            for (int k = 0; k < NEW_INDIVIDUALS; k++) {
                population.remove(population.size() - 1);
            }

            i++;
        }

        System.out.println("End of evolution:  Fitness: " + FITNESS +
                " Objetive " + OBJECTIVE + " candidate_fitness: " + population.get(0).getFitness() +
                " candidate_genoma: " + population.get(0).getGenome());

        // Write fitness to CSV file
        // Uncomment this lines to write fitness values to a file
        CSVUtil csvUtil = new CSVUtil();
        try {
            csvUtil.writeToFile(log);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Combine the genome from two parents.
     * Returns a new Individual
     */
    private Individual combineGenome(Individual parent1, Individual parent2) {
        String parent1_genome = parent1.getGenome();
        String parent2_genome = parent2.getGenome();
        StringBuilder recombination = new StringBuilder();

        String parent11 = parent1_genome.substring(0, parent1_genome.length() / 2);
        String parent12 = parent1_genome.substring(parent1_genome.length() / 2);

        String parent21 = parent2_genome.substring(0, parent2_genome.length() / 2);
        String parent22 = parent2_genome.substring(parent2_genome.length() / 2);

        int selection = ThreadLocalRandom.current().nextInt(0, 2);

        if (selection == 0) {
            recombination.append(parent11).append(parent22);
        } else {
            recombination.append(parent21).append(parent12);
        }

        String newGenome = recombination.toString();

        return new Individual(fitness(newGenome), newGenome);
    }

    /**
     * Modifies some genes if mutation occurs when two parents combine their genome,
     */
    private Individual mutateGenome(Individual individual) {
        String currentGenome = individual.getGenome();
        StringBuilder newGenome = new StringBuilder();

        for (int j = 0; j < individual.getGenome().length(); j++) {
            double mutate = ThreadLocalRandom.current().nextDouble((long) 0.0, (long) 1.0);

            if (mutate <= MUTATION_RATE) {
                newGenome.append(generate_gen());
            } else {
                newGenome.append(currentGenome.charAt(j));
            }

        }
        individual.setGenome(newGenome.toString());
        individual.setFitness(fitness(newGenome.toString()));

        return individual;
    }

    /**
     * Function that determines the fitness of one candidate
     * using "Levenshtein Distance"
     */
    private float fitness(String genome) {
        float distance = 0;

        // calculate Levenshtein Distance
        for (int i = 0; i < genome.length(); i++) {
            char obj = genome.charAt(i);
            char tes = OBJECTIVE.charAt(i);
            if (obj != tes) distance++;
        }

        return (1f - (distance / (float) OBJECTIVE.length()));
    }
}
