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
        genom_ = new Rectangle[squares];
        x_sorted_ = new int[squares];
        for (int i = 0; i < squares; i++) {
            genom_[i] = new Rectangle();
            x_sorted_[i] = i;
        }
    }

    @Override
    public Individual makeBlank() {
        return new FpIndividual(scWidth, scHeight, genom_.length);
    }

    @Override
    public void randomize(IndData data, Random rnd) {

        int maxX = scWidth;
        int maxY = scHeight;

        FpData fpData = (FpData) data;

        int attempts = 0;

        for (int i = 0; i < genom_.length; i++) {

            boolean again = false;
            do {
                boolean flip = rnd.nextBoolean();

                genom_[i].x1 = Other.nextInt(rnd, 100, maxX - 100);
                genom_[i].y1 = Other.nextInt(rnd, 100, maxY - 100);
                //System.out.println(x[i]+"AND"+y[i]);

                if (flip) {

                    genom_[i].x2 = genom_[i].x1 + fpData.height[i];
                    genom_[i].y2 = genom_[i].y1 + fpData.width[i];

                } else {
                    genom_[i].x2 = genom_[i].x1 + fpData.width[i];
                    genom_[i].y2 = genom_[i].y1 + fpData.height[i];
                }

                again = false;
                // Colisions

                if (attempts < 10000) {
                    for (int j = 0; j < i; j++) {
                        if (overlap2D(genom_[i].x1, genom_[i].y1, genom_[i].x2, genom_[i].y2, genom_[j].x1, genom_[j].y1, genom_[j].x2, genom_[j].y2)) {
                            again = true;
                            attempts++;
                        }
                    }
                } else {
                    System.out.println("Space is too small");
                }

            } while (again);
        }
    }

    @Override
    public Individual fromFile(String filename) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void toFile(String filename) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mutate(float amount, float probability, Random rnd) {
        for (int i = 0; i < genom_.length; i++) {
            
            // Move
            if (rnd.nextFloat() < probability) {

                float mutX = (float) rnd.nextGaussian() * amount;

                if (genom_[i].x1 + mutX >= scWidth || genom_[i].x1 + mutX < 0) {
                    genom_[i].x1 -= mutX;
                    genom_[i].x2 -= mutX;
                } else {
                    genom_[i].x1 += mutX;
                    genom_[i].x2 += mutX;
                }

                float mutY = (float) (rnd.nextGaussian() * amount);

                if (genom_[i].y1 + mutY >= scHeight || genom_[i].y1 + mutY < 0) {

                    genom_[i].y1 -= mutY;
                    genom_[i].y2 -= mutY;
                } else {
                    genom_[i].y1 += mutY;
                    genom_[i].y2 += mutY;
                }
            }

            // Flip
            if (rnd.nextFloat() < probability / 5) {
                genom_[i].flip();
            }

            //Switch
            if (rnd.nextFloat() < probability / 5) {
                int sw = Other.nextInt(rnd, 0, genom_.length - 1);

                int tmpx = genom_[i].x1 - genom_[sw].x1;
                genom_[i].x1 = genom_[i].x1 - tmpx;
                genom_[i].x2 = genom_[i].x2 - tmpx;

                int tmpy = genom_[i].y1 - genom_[sw].y1;
                genom_[i].y1 = genom_[i].y1 - tmpy;
                genom_[i].y2 = genom_[i].y2 - tmpy;
            }
        }
    }

    void sortRects() {
        // Insertion sort by x axis from left to right
        for (int i = 1; i <= x_sorted_.length - 1; i++) {
            int tmp = x_sorted_[i];
            int j = i - 1;

            while ((genom_[tmp].x1 < genom_[x_sorted_[j]].x1) && (j >= 0)) {
                x_sorted_[j + 1] = x_sorted_[j];    //moves element forward
                j = j - 1;
            }
            x_sorted_[j + 1] = tmp;    //insert element in proper place
        }
    }

    @Override
    public void crossoverTo(Individual secondOne, Individual ind,
            Random rnd) {
        FpIndividual cInd = (FpIndividual) ind;
        FpIndividual cSecondOne = (FpIndividual) secondOne;

        for (int i = 0; i < genom_.length; i++) {
            if (rnd.nextBoolean()) {
                cInd.genom_[i].x1 = genom_[i].x1;
                cInd.genom_[i].x2 = genom_[i].x2;
                cInd.genom_[i].y1 = genom_[i].y1;
                cInd.genom_[i].y2 = genom_[i].y2;
            } else {
                cInd.genom_[i].x1 = cSecondOne.genom_[i].x1;
                cInd.genom_[i].x2 = cSecondOne.genom_[i].x2;
                cInd.genom_[i].y1 = cSecondOne.genom_[i].y1;
                cInd.genom_[i].y2 = cSecondOne.genom_[i].y2;
            }
        }
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
        for (int i = 0; i < genom_.length; i++) {
            genom_[i].copyTo(fpInd.genom_[i]);
            System.arraycopy(fpInd.x_sorted_, 0, x_sorted_, 0, x_sorted_.length);
        }

        fpInd.fitness = fitness;
        fpInd.colX = colX;
        fpInd.colY = colY;
    }

    private boolean overlap2D(float ax1, float ay1, float ax2, float ay2, float bx1, float by1, float bx2, float by2) {
        //return false;

        return !(bx2 <= ax1 || ax2 <= bx1 || by2 <= ay1 || ay2 <= by1);

    }

    public void moveToCenter() {

        int sumX = 0, sumY = 0;

        for (int i = 0; i < genom_.length; i++) {
            sumX += genom_[i].x1;
            sumY += genom_[i].y1;
        }

        sumX = sumX / genom_.length;
        sumY = sumY / genom_.length;

        int centerX = scWidth / 2;
        int centerY = scHeight / 2;

        int moveX = sumX - centerX;
        int moveY = sumY - centerY;

        for (int i = 0; i < genom_.length; i++) {
            genom_[i].x1 -= moveX;
            genom_[i].y1 -= moveY;
            genom_[i].x2 -= moveX;
            genom_[i].y2 -= moveY;
        }

    }

    @Override
    public void countFitness(IndData data) {
        // TODO COUNT FITNESS
        FpData fpData = (FpData) data;

        moveToCenter();
        fitness = 0;

        // Colisions
        for (int i = 0; i < genom_.length; i++) {
            for (int j = i + 1; j < genom_.length; j++) {
                if (overlap2D(genom_[i].x1, genom_[i].y1, genom_[i].x2, genom_[i].y2, genom_[j].x1, genom_[j].y1, genom_[j].x2, genom_[j].y2)) {
                    float penalty = fpData.width[i] * fpData.height[i] + fpData.width[j] * fpData.height[j];
                    fitness -= penalty * 3;
                }
            }
        }

        // bounding box surface
        float maxX = genom_[0].x2;
        float maxY = genom_[0].y2;
        float minX = genom_[0].x1;
        float minY = genom_[0].y1;

        for (int i = 1; i < genom_.length; i++) {
            if (maxX < genom_[i].x2) {
                maxX = genom_[i].x2;
            }

            if (minX > genom_[i].x1) {
                minX = genom_[i].x1;
            }

            if (maxY < genom_[i].y2) {
                maxY = genom_[i].y2;
            }

            if (minY > genom_[i].y1) {
                minY = genom_[i].y1;
            }

        }
        fitness -= (maxX - minX) * (maxY - minY);
    }

    @Override
    public void countColor() {
        colX = 0;
        colY = 0;

        float cX, cY;
        for (int i = 0; i < genom_.length; i++) {

            cX = (float) genom_[i].x1 / (float) scWidth;
            cY = (float) genom_[i].y1 / (float) scHeight;

            if (i % 2 == 0) {
                colX += cX;
                colY += cY;
            } else {
                colX -= cX;
                colY -= cY;
            }
        }

        colX = Other.tanh(colX / genom_.length);
        colY = Other.tanh(colY / genom_.length);
    }

    @Override
    protected Individual clone() {
        FpIndividual fpInd = new FpIndividual(scWidth, scHeight, genom_.length);
        copyTo(fpInd);

        return fpInd;
    }

    @Override
    public String toString(IndData data) {
        String res = "";

        for (int i = 0; i < genom_.length; i++) {
            res += Integer.toString((int) genom_[i].x1) + "," + Integer.toString((int) genom_[i].y1) + "\n";
        }

        return res;
    }

    public void Draw(IndData data, String filename) {
        FpData fpData = (FpData) data;

        // bounding box surface
        float maxX = genom_[0].x2;
        float maxY = genom_[0].y2;
        float minX = genom_[0].x1;
        float minY = genom_[0].y2;

        for (int i = 1; i < genom_.length; i++) {
            if (maxX < genom_[i].x2) {
                maxX = genom_[i].x2;
            }

            if (minX > genom_[i].x1) {
                minX = genom_[i].x1;
            }

            if (maxY < genom_[i].y2) {
                maxY = genom_[i].y2;
            }

            if (minY > genom_[i].y1) {
                minY = genom_[i].y1;
            }
        }

        BufferedImage image = new BufferedImage(scWidth, scHeight, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        for (int i = 0; i < genom_.length; i++) {

            int strWidth = g.getFontMetrics().stringWidth(Integer.toString(i));
            g.setColor(Color.white);
            g.fillRect((int) genom_[i].x1, (int) genom_[i].y1, (int) genom_[i].x2 - genom_[i].x1, (int) genom_[i].y2 - genom_[i].y1);
            g.setColor(Color.red);
            g.drawRect((int) genom_[i].x1, (int) genom_[i].y1, (int) genom_[i].x2 - genom_[i].x1, (int) genom_[i].y2 - genom_[i].y1);
            g.drawString(Integer.toString(i), ((genom_[i].x1 + genom_[i].x2) / 2 - strWidth / 2), (((genom_[i].y1 + genom_[i].y2) / 2) + 4));

            // Bounding box
            g.setColor(Color.yellow);
            g.drawRect((int) minX, (int) minY, (int) (maxX - minX), (int) (maxY - minY));
        }

        try {
            ImageIO.write(image, "png", new File(filename));
        } catch (IOException ex) {
            Logger.getLogger(FpIndividual.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    int scWidth, scHeight;

    Rectangle[] genom_;
    int[] x_sorted_;
}
