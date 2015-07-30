package aa.experiment;

public class GeneticAlgorithm {

	/* GA parameters */
    private static final double uniformRate = 0.5;
    private static final double mutationRate = 0.05; //0.015;//0.05;
    private static final int tournamentSize = 3;
    private static final boolean elitism = true;
    
    // Evolve a population
    public static Population evolvePopulation(int type, Population pop, AALogger log) {
        Population newPopulation = new Population(type, pop.getSize(), log);

        // Keep our best individual
        if (elitism) {
            newPopulation.saveIndividual(0, pop.getFittest(false));
        }

        // Crossover population
        
        // Loop over the population size and create new individuals with
        // crossover
        for (int i = (elitism ? 1 : 0); i < pop.getSize(); i++) {
            Individual indiv1 = tournamentSelection(type, pop, log);
            Individual indiv2 = tournamentSelection(type, pop, log);
            Individual newIndiv = crossover(type, indiv1, indiv2);
            newPopulation.saveIndividual(i, newIndiv);
        }

        // Mutate population
        for (int i = (elitism ? 1 : 0); i < newPopulation.getSize(); i++) {
            mutate(newPopulation.getIndividual(i));
        }

        return newPopulation;
    }

    // Crossover individuals
    private static Individual crossover(int type, Individual indiv1, Individual indiv2) {
        Individual newSol = new Individual(type);
        // Loop through genes
        for (int i = 0; i < indiv1.size(); i++) {
            // Crossover
            if (Math.random() <= uniformRate) {
                newSol.setGene(i, indiv1.getGene(i));
            } else {
                newSol.setGene(i, indiv2.getGene(i));
            }
        }
        return newSol;
    }

    // Mutate an individual
    private static void mutate(Individual indiv) {
        // Loop through genes
        for (int i = 0; i < indiv.size(); i++) {
            if (Math.random() <= mutationRate) {
                // Create random gene
                byte gene = (byte) Math.round(Math.random());
                indiv.setGene(i, gene);
            }
        }
    }

    // Select individuals for crossover
    private static Individual tournamentSelection(int type, Population pop, AALogger log) {
        // Create a tournament population
        Population tournament = new Population(type, tournamentSize, log);
        // For each place in the tournament get a random individual
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.getSize());
            tournament.saveIndividual(i, pop.getIndividual(randomId));
        }
        // Get the fittest
        Individual fittest = tournament.getFittest(false);
        return fittest;
    }
}
