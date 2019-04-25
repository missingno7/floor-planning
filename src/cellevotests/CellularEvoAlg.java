/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cellevotests;

import cellularevoalg.IndData;
import cellularevoalg.Individual;
import cellularevoalg.PopConfig;
import cellularevoalg.Population;
import java.util.Random;
import timemeasure.TimeMeasure;

/**
 *
 * @author J_Vestfal
 */
public class CellularEvoAlg {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here

        TimeMeasure meas=TimeMeasure.getInstance();
        
        meas.startTimer("RUNTIME");
        
        String indPath = "out\\";
        
        PopConfig cfg = new PopConfig();
        
        cfg.reg.newInt("tstint");
                
        cfg.LoadConfig("config.txt");
        
        int[] tstint=cfg.reg.getInt("tstint");
        
        System.out.println(tstint.length+" - "+tstint[1]);
        

        TestInd tstInd = new TestInd();
        tstInd.val = 3;

        IndData tstData = new TestIndData();

        
        
        Population pop = new Population(tstInd, tstData, cfg);
        pop.Randomize(new Random());

        
        
        for (int i = 0; i < 100; i++) {
            Individual bestInd = pop.getBest();
            System.out.println("GENERATION " + pop.getGen());
            System.out.println(bestInd.toString(tstData));
            System.out.println("BEST FITNESS: " + bestInd.fitness);
            System.out.println("AVG FITNESS: " + pop.avgFitness());
            if(cfg.drawpop)pop.paintPop(indPath + "GEN" + Integer.toString(i) + ".png");
            pop.nextGen();
        }

        meas.stopTimer("RUNTIME");
        meas.print();
    }

}
