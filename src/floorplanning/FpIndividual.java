/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floorplanning;

import cellularevoalg.IndData;
import cellularevoalg.Individual;
import cellularevoalg.Other;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author J_Vestfal
 */
public class FpIndividual extends Individual {

    FpIndividual(int scWidth, int scHeight, int squares) {
        this.scWidth = scWidth;
        this.scHeight = scHeight;
        x = new float[squares];
        y = new float[squares];
        flip = new boolean[squares];

    }

    @Override
    public Individual makeBlank() {
        return new FpIndividual(scWidth, scHeight, x.length);
    }

    @Override
    public void randomize(IndData data, Random rnd) {

        int maxX = scWidth;
        int maxY = scHeight;

        FpData fpData = (FpData) data;

        float absX[] = new float[x.length];
        float absY[] = new float[x.length];

        int attempts = 0;

        for (int i = 0; i < x.length; i++) {

            boolean again = false;
            do {

                flip[i] = rnd.nextBoolean();

                x[i] = Other.nextInt(rnd, 100, maxX - 100);
                y[i] = Other.nextInt(rnd, 100, maxY - 100);
                //System.out.println(x[i]+"AND"+y[i]);

                if (flip[i]) {

                    absX[i] = fpData.height[i] + x[i];
                    absY[i] = fpData.width[i] + y[i];

                } else {
                    absX[i] = fpData.width[i] + x[i];
                    absY[i] = fpData.height[i] + y[i];
                }

                again = false;
                // Colisions

                if (attempts < 10000) {

                    for (int j = 0; j < i; j++) {
                        if (overlap2D(x[i], y[i], absX[i], absY[i], x[j], y[j], absX[j], absY[j])) {
                            //System.out.println("("+x[i]+","+ y[i]+"-"+ absX[i]+","+ absY[i]+"),("+ x[j]+","+ y[j]+"-"+ absX[j]+","+ absY[j]+")");
                            again = true;
                            attempts++;

                        }

                    }
                } else {
                    System.out.println("Space is too small");
                }

            } while (again);
            //System.out.println("DONE - "+attempts);

        }
        //System.out.println("done - " + attempts);

        /*mutamount = rnd.nextFloat() * 20;
        mutprob = rnd.nextFloat();*/
    }

    @Override
    public Individual fromFile(String filename
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void toFile(String filename
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mutate(float amount, float probability, Random rnd
    ) {

        /*if (rnd.nextFloat() < probability) {
            float value = rnd.nextFloat() * amount*10;
            mutamount += value;
            if (mutamount < 0) {
                mutamount = -mutamount;
            }
        }
        if (rnd.nextFloat() < probability) {
            float value = rnd.nextFloat() * amount;
            mutprob+=value;
            mutprob=Math.abs(mutprob)%1;
        }
         if (rnd.nextFloat() < probability) {
            float value = rnd.nextFloat() * amount;
            mutprobswitch+=value;
            mutprobswitch=Math.abs(mutprobswitch)%1;
        }
          if (rnd.nextFloat() < probability) {
            float value = rnd.nextFloat() * amount;
            mutprobflip+=value;
            mutprobflip=Math.abs(mutprobflip)%1;
        }*/
        for (int i = 0; i < x.length; i++) {
            //if (rnd.nextFloat() < mutprob) {
            if (rnd.nextFloat() < probability) {

                float mutX = (float) rnd.nextGaussian() * amount;

                if (x[i] + mutX >= scWidth || x[i] + mutX < 0) {

                    x[i] -= mutX;
                } else {
                    x[i] += mutX;
                }

                float mutY = (float) (rnd.nextGaussian() * amount);

                if (y[i] + mutY >= scHeight || y[i] + mutY < 0) {

                    y[i] -= mutY;
                } else {
                    y[i] += mutY;
                }
            }

            //if (rnd.nextFloat() < mutprobflip) {
            if (rnd.nextFloat() < probability / 5) {
                flip[i] = !flip[i];
            }

            //Switch
            //if (rnd.nextFloat() < mutprobswitch) {
            if (rnd.nextFloat() < probability / 5) {
                int sw = Other.nextInt(rnd, 0, x.length - 1);

                float tmp;
                boolean tmpb;

                tmp = x[i];
                x[i] = x[sw];
                x[sw] = tmp;

                tmp = y[i];
                y[i] = y[sw];
                y[sw] = tmp;

                tmpb = flip[i];
                flip[i] = flip[sw];
                flip[sw] = tmpb;

            }

        }
    }

    @Override
    public void crossoverTo(Individual secondOne, Individual ind,
            Random rnd
    ) {
        FpIndividual cInd = (FpIndividual) ind;
        FpIndividual cSecondOne = (FpIndividual) secondOne;

        for (int i = 0; i < x.length; i++) {

            /*
            float amount = rnd.nextFloat();

            
            cInd.x[i] = (int) (x[i] * amount + cSecondOne.x[i] * (1 - amount));

            amount = rnd.nextFloat();

            cInd.y[i] = (int) (y[i] * amount + cSecondOne.y[i] * (1 - amount));
             */
            if (rnd.nextBoolean()) {
                cInd.flip[i] = flip[i];
                cInd.x[i] = x[i];
                cInd.y[i] = y[i];
            } else {
                cInd.flip[i] = cSecondOne.flip[i];
                cInd.x[i] = cSecondOne.x[i];
                cInd.y[i] = cSecondOne.y[i];
            }
        }
        /*
        float amount = rnd.nextFloat();
        cInd.mutamount=amount*cInd.mutamount+(1-amount)*cSecondOne.mutamount;
        amount = rnd.nextFloat();
        cInd.mutprob=amount*cInd.mutprob+(1-amount)*cSecondOne.mutprob;
        amount = rnd.nextFloat();
        cInd.mutprobflip=amount*cInd.mutprobflip+(1-amount)*cSecondOne.mutprobflip;
        amount = rnd.nextFloat();
        cInd.mutprobswitch=amount*cInd.mutprobswitch+(1-amount)*cSecondOne.mutprobswitch;
         */

    }

    @Override
    public void mutateTo(float amount, float probability, Individual ind,
            Random rnd
    ) {
        copyTo(ind);
        ind.mutate(amount, probability, rnd);
    }

    @Override
    public void copyTo(Individual ind
    ) {
        FpIndividual fpInd = (FpIndividual) ind;
        System.arraycopy(x, 0, fpInd.x, 0, x.length);
        System.arraycopy(y, 0, fpInd.y, 0, y.length);
        System.arraycopy(flip, 0, fpInd.flip, 0, flip.length);
        fpInd.fitness = fitness;
        fpInd.colX = colX;
        fpInd.colY = colY;
        /*fpInd.mutamount=mutamount;
        fpInd.mutprob=mutprob;
        fpInd.mutprobflip=mutprobflip;
        fpInd.mutprobswitch=mutprobswitch;*/

    }

    private boolean overlap2D(float ax1, float ay1, float ax2, float ay2, float bx1, float by1, float bx2, float by2) {
        //return false;

        return !(bx2 <= ax1 || ax2 <= bx1 || by2 <= ay1 || ay2 <= by1);

    }

    public void moveToCenter() {

        int sumX = 0, sumY = 0;

        for (int i = 0; i < x.length; i++) {
            sumX += x[i];
            sumY += y[i];
        }

        sumX = sumX / x.length;
        sumY = sumY / x.length;

        int centerX = scWidth / 2;
        int centerY = scHeight / 2;

        int moveX = sumX - centerX;
        int moveY = sumY - centerY;

        for (int i = 0; i < x.length; i++) {
            x[i] -= moveX;
            y[i] -= moveY;
        }

    }

    @Override
    public void countFitness(IndData data) {
        // TODO COUNT FITNESS
        FpData fpData = (FpData) data;

        moveToCenter();

        fitness = 0;

        // Prepare flips
        float absX[] = new float[x.length];
        float absY[] = new float[x.length];
        for (int i = 0; i < x.length; i++) {
            if (flip[i]) {

                absX[i] = fpData.height[i] + x[i];
                absY[i] = fpData.width[i] + y[i];

            } else {
                absX[i] = fpData.width[i] + x[i];
                absY[i] = fpData.height[i] + y[i];
            }

        }

        // Colisions
        for (int i = 0; i < x.length; i++) {
            for (int j = i + 1; j < x.length; j++) {
                if (overlap2D(x[i], y[i], absX[i], absY[i], x[j], y[j], absX[j], absY[j])) {

                    //System.out.println("collision");
                    float penalty = fpData.width[i] * fpData.height[i] + fpData.width[j] * fpData.height[j];
                    fitness -= penalty * 3;

                    /*randomize(data,new Random());
                    countFitness(data);
                    return;*/
                    //fitness -= scWidth*scHeight;
                    //return;
                }
            }

        }

        // bounding box surface
        float maxX = absX[0];
        float maxY = absY[0];
        float minX = x[0];
        float minY = y[0];

        for (int i = 1; i < x.length; i++) {
            if (maxX < absX[i]) {
                maxX = absX[i];
            }

            if (minX > x[i]) {
                minX = x[i];
            }

            if (maxY < absY[i]) {
                maxY = absY[i];
            }

            if (minY > y[i]) {
                minY = y[i];
            }

        }
        fitness -= (maxX - minX) * (maxY - minY);
        //System.out.println(fitness);
    }

    @Override
    public void countColor() {
        colX = 0;
        colY = 0;

        float cX = 0, cY = 0;
        for (int i = 0; i < x.length; i++) {

            cX = x[i] / (float) scWidth;
            cY = y[i] / (float) scHeight;

            //if(x[i]!=0)System.out.println(x[i]+","+scWidth);
            if (flip[i]) {
                colX -= cX;
                colY -= cY;
            } else {
                colX += cX;
                colY += cY;
            }

        }
        colX = colX / x.length;
        colY = colY / x.length;

        colX = Other.tanh(colX);
        colY = Other.tanh(colY);
        //if(colX!=0.0)System.out.println(colX+"//"+colY);
    }

    /*
    @Override
    public void countFitness(IndData data) {
        // TODO COUNT FITNESS
        FpData fpData = (FpData) data;

        //moveToCenter();
        fitness = 0;

        // Prepare flips
        int absX[] = new int[x.length];
        int absY[] = new int[x.length];
        for (int i = 0; i < x.length; i++) {
            if (flip[i]) {

                absX[i] = fpData.height[i] + x[i];
                absY[i] = fpData.width[i] + y[i];

            } else {
                absX[i] = fpData.width[i] + x[i];
                absY[i] = fpData.height[i] + y[i];
            }

        }

        // Colisions
        for (int i = 0; i < x.length; i++) {
            for (int j = i + 1; j < x.length; j++) {
                if (overlap2D(x[i], y[i], absX[i], absY[i], x[j], y[j], absX[j], absY[j])) {

                    
                    // Replace with new
                    //randomize(data,new Random());
                    //countFitness(data);
                    //return;
                     
                    fitness = -100000000;
                    return;

                    //System.out.println("collision");
                    //float penalty = fpData.width[i] * fpData.height[i] + fpData.width[j] * fpData.height[j];
                    //fitness -= penalty * 4;
                    //fitness -= scWidth*scHeight;
                    //return;
                }
            }

        }

             
        // Distance from center
        int centerX = scWidth / 2;
        int centerY = scHeight / 2;

        int avgX = 0, avgY = 0;

        for (int i = 0; i < x.length; i++) {

            avgX += Math.abs(x[i] - centerX);

            avgY += Math.abs(y[i] - centerY);

        }

        fitness = -(avgX * avgX + avgY * avgY);

        //System.out.println((avgX*avgX+avgY*avgY)+","+fitness);
    }*/
    @Override
    protected Individual clone() {
        FpIndividual fpInd = new FpIndividual(scWidth, scHeight, x.length);
        copyTo(fpInd);

        return fpInd;
    }

    @Override
    public String toString(IndData data) {
        String res = "";

        for (int i = 0; i < x.length; i++) {
            res += Integer.toString((int) x[i]) + "," + Integer.toString((int) y[i]) + " - " + Boolean.toString(flip[i]) + "\n";
        }

        //res+="amount: "+Float.toString(mutamount)+", prob: "+Float.toString(mutprob)+", flip: "+Float.toString(mutprobflip)+", switch: "+Float.toString(mutprobswitch)+"\n";
        return res;
    }

    public void Draw(IndData data, String filename) {
        FpData fpData = (FpData) data;

        // Prepare flips
        float absX[] = new float[x.length];
        float absY[] = new float[x.length];
        for (int i = 0; i < x.length; i++) {
            if (flip[i]) {

                absX[i] = fpData.height[i] + x[i];
                absY[i] = fpData.width[i] + y[i];

            } else {
                absX[i] = fpData.width[i] + x[i];
                absY[i] = fpData.height[i] + y[i];
            }

        }
        
        // bounding box surface
        float maxX = absX[0];
        float maxY = absY[0];
        float minX = x[0];
        float minY = y[0];

        for (int i = 1; i < x.length; i++) {
            if (maxX < absX[i]) {
                maxX = absX[i];
            }

            if (minX > x[i]) {
                minX = x[i];
            }

            if (maxY < absY[i]) {
                maxY = absY[i];
            }

            if (minY > y[i]) {
                minY = y[i];
            }
        }
        
        
        BufferedImage image = new BufferedImage(scWidth, scHeight, BufferedImage.TYPE_INT_RGB);

        Graphics g = image.getGraphics();

        for (int i = 0; i < x.length; i++) {

            int strWidth = g.getFontMetrics().stringWidth(Integer.toString(i));

            if (flip[i]) {
                g.setColor(Color.white);
                g.fillRect((int) x[i], (int) y[i], (int) fpData.height[i], (int) fpData.width[i]);
                g.setColor(Color.red);
                g.drawRect((int) x[i], (int) y[i], (int) fpData.height[i], (int) fpData.width[i]);
                g.drawString(Integer.toString(i), (int) (x[i] + fpData.height[i] / 2 - strWidth / 2), (int) (y[i] + (fpData.width[i] / 2) + 4));
            } else {
                g.setColor(Color.white);
                g.fillRect((int) x[i], (int) y[i], (int) fpData.width[i], (int) fpData.height[i]);
                g.setColor(Color.red);
                g.drawRect((int) x[i], (int) y[i], (int) fpData.width[i], (int) fpData.height[i]);
                g.drawString(Integer.toString(i), (int) (x[i] + fpData.width[i] / 2 - strWidth / 2), (int) (y[i] + (fpData.height[i] / 2) + 4));

            }
            
            // Bounding box
            g.setColor(Color.yellow);
            g.drawRect((int) minX, (int) minY, (int) (maxX-minX), (int) (maxY-minY));

        }

        try {
            ImageIO.write(image, "png", new File(filename));
        } catch (IOException ex) {
            Logger.getLogger(FpIndividual.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    float[] x;
    float[] y;
    boolean[] flip;
    int scWidth, scHeight;

    //float mutamount, mutprob,mutprobflip,mutprobswitch;
}
