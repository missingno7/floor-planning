/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floorplanning;

import cellularevoalg.IndData;
import cellularevoalg.PopConfig;
import java.util.Random;

/**
 *
 * @author J_Vestfal
 */
public class FpData extends IndData {

    float[] width;
    float[] height;
    int scWidth;
    int scHeight;

    FpData(String filename, PopConfig cfg) {
        scWidth = 1000;
        scHeight = 1000;

        
        int squares = cfg.reg.getInt("squares")[0];

        width = new float[squares];
        height = new float[squares];
        Random rnd = new Random();

        for (int i = 0; i < squares; i++) {
            width[i] = 10 + Math.abs(rnd.nextInt() % 100);
            height[i] = 10 + Math.abs(rnd.nextInt() % 100);
            
        }

        /*for(int i=0;i<squares;i++){
            width[i]=5+rnd.nextInt()%15;
            height[i]=5+rnd.nextInt()%15;
        }*/
 
        /*
        width[0]=10;
        height[0]=20;
        
        width[1]=10;
        height[1]=50;
        
        width[2]=20;
        height[2]=30;
        
        width[3]=40;
        height[3]=10;
        
        width[4]=30;
        height[4]=20;
        
        width[5]=30;
        height[5]=10;
        
        width[6]=40;
        height[6]=10;
        
        width[7]=40;
        height[7]=20;
        
        width[8]=20;
        height[8]=10;
        
        width[9]=20;
        height[9]=30;
        
        width[10]=10;
        height[10]=30;
        
        width[11]=10;
        height[11]=20;
        
        width[12]=10;
        height[12]=50;
        
        width[13]=20;
        height[13]=30;
        
        width[14]=40;
        height[14]=10;
        
        width[15]=30;
        height[15]=20;
        
        width[16]=30;
        height[16]=10;
        
        width[17]=40;
        height[17]=10;
        
        width[18]=40;
        height[18]=20;
        
        width[19]=20;
        height[19]=10;
        
        width[20]=20;
        height[20]=30;
        
        width[21]=10;
        height[21]=30;
        */
         
 /*for (int i = 0; i < squares; i++) {
            width[i] = 50;
            height[i] = 50;
        }*/
    }

    int getPerfection() {
        int sum = 0;
        for (int i = 0; i < width.length; i++) {
            sum += width[i] * height[i];
        }

        return sum;
    }

}
