/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cellevotests;

import cellularevoalg.IndData;
import cellularevoalg.Individual;
import java.util.Random;

/**
 *
 * @author J_Vestfal
 */
public class TestInd extends Individual {

    @Override
    public Individual makeBlank() {
        return new TestInd();
    }

    @Override
    public void randomize(IndData data, Random rnd) {
        val = rnd.nextInt()%1000;
        //System.out.println(val);

    }

    @Override
    public Individual fromFile(String filename) {
        System.out.println("DUMMY file READ");
        TestInd newInd = new TestInd();
        newInd.val = 43;
        return newInd;
    }

    @Override
    public void toFile(String filename) {
        System.out.println("DUMMY file WRITE");
    }

    @Override
    public void mutate(float amount, float probability, Random rnd) {
        val += (int) ((rnd.nextFloat()%1000) * amount);
      
        
    }

    @Override
    public void crossoverTo(Individual secondOne, Individual ind, Random rnd) {
        TestInd cInd = (TestInd) ind;
        TestInd cSecondOne = (TestInd) secondOne;
        
        float left=rnd.nextFloat();
        float right=1-left;
        
        cInd.val=(int)(left*val+right*cSecondOne.val);
        
        
                
    }

    @Override
    public void mutateTo(float amount, float probability, Individual ind, Random rnd) {
        TestInd cInd = (TestInd) ind;
        
        int prev=cInd.val;
        cInd.val = val + (int) ((rnd.nextInt()%1000) * amount);
        
        
        //if(cInd.val<prev)System.out.println(prev+","+cInd.val);
    }

    @Override
    public void copyTo(Individual ind) {
        TestInd cInd = (TestInd) ind;
        cInd.val = val;
        cInd.fitness = fitness;
        cInd.colX = colX;
        cInd.colY = colY;
    }

    @Override
    public void countFitness(IndData data) {
        fitness = (int) val * ((TestIndData) data).data;
    }

    @Override
    protected Individual clone() {
        TestInd newInd = new TestInd();
        newInd.val = val;
        newInd.fitness = fitness;
        newInd.colX = colX;
        newInd.colY = colY;

        return newInd;
    }

    @Override
    public String toString(IndData data) {
        return Integer.toString(val) + " * " + ((TestIndData) data).data;
    }

    @Override
    public void countColor() {
        colX = val * val;
        colY = val;
    }

    public int val;

}
