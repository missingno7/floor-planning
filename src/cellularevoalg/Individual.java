/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cellularevoalg;

import java.util.Random;

/**
 *
 * @author J_Vestfal
 */
public abstract class Individual {

    public abstract Individual makeBlank();

    public abstract void randomize(IndData data,Random rnd);

    public abstract Individual fromFile(String filename);

    public abstract void toFile(String filename);

    public abstract void mutate(float amount, float probability, Random rnd);

    public abstract void crossoverTo(Individual secondOne, Individual ind, Random rnd);

    public abstract void mutateTo(float amount, float probability, Individual ind, Random rnd);

    public abstract void copyTo(Individual ind);

    public abstract void countFitness(IndData data);

    public abstract void countColor();
        
    @Override
    protected abstract Individual clone();

    public abstract String toString(IndData data);

    public float getFitness() {
        return fitness;
    }

    public static boolean isPrintable() {
        return false;
    }

    public static boolean isDrawable() {
        return false;
    }
    
    

    public float fitness;
    public float colX,colY;
    
}
