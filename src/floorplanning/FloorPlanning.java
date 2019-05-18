/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floorplanning;

import cellularevoalg.PopConfig;
import cellularevoalg.Population;
import java.util.Random;

/**
 *
 * @author J_Vestfal
 */
public class FloorPlanning {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here

        String indPath = "out\\";

        PopConfig cfg = PopConfig.getInstance();

        // Custom parameter
        cfg.reg.newInt("squares");
        cfg.reg.newFloat("flipprob");
        cfg.reg.newFloat("switchprob");

        cfg.LoadConfig("config.txt");

        FpData tstData = new FpData("squares.txt", cfg);
        int perfectFitness = tstData.getPerfection();

        FpIndividual tstInd = new FpIndividual(tstData.scWidth, tstData.scHeight, tstData.height.length);

        Population pop = new Population(tstInd, tstData, cfg);
        pop.Randomize(new Random());

        FpIndividual bestOne = (FpIndividual) pop.getBest();

        while (true) {
            FpIndividual bestInd = (FpIndividual) pop.getBest();

            if (bestInd.fitness > bestOne.fitness) {
                bestOne = (FpIndividual) bestInd.clone();
                bestInd.Draw(tstData, indPath + "IGEN" + Integer.toString(pop.getGen()) + ".png");

                if (cfg.drawpop) {
                    pop.paintPop(indPath + "GEN" + Integer.toString(pop.getGen()) + ".png");
                }
            }

            System.out.println("GENERATION " + pop.getGen());
            System.out.println(bestInd.toString(tstData));
            System.out.println("GEN BEST FITNESS: " + (perfectFitness * 100 / -bestInd.fitness) + " %");
            System.out.println("AVG FITNESS: " + (perfectFitness * 100 / -pop.avgFitness()) + " %");
            System.out.println("ALLBEST FITNESS: " + (perfectFitness * 100 / -bestOne.fitness) + " %");

            pop.nextGen();
        }

    }

}
