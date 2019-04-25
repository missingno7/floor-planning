/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cellularevoalg;

import java.util.Random;

/**
 *
 * @author vestfal
 */
public class Other {

    public static int nextInt(Random rnd, int min, int max) {
        int range = max - min + 1;
        return rnd.nextInt(range) + min;
    }

    public static float tanh(float x) {
        return x / (1 + (Math.abs(x)));
    }
    
    public static float sigm(float x) {
        return ((x / (1 + (Math.abs(x))))+1)/2;
    }
    
    
   // x / (1 + Math.abs(x))

    public static void LABtoRGB(int[] lab, int[] rgb) {

        float r, g, b;
        float y = (lab[0] + 16f) / 116f;
        float x = lab[1] / 500f + y;
        float z = y - lab[2] / 200f;

        x = 0.95047f * ((x * x * x > 0.008856f) ? x * x * x : (x - 16f / 116f) / 7.787f);
        y = 1.00000f * ((y * y * y > 0.008856f) ? y * y * y : (y - 16f / 116f) / 7.787f);
        z = 1.08883f * ((z * z * z > 0.008856f) ? z * z * z : (z - 16f / 116f) / 7.787f);

        r = x * 3.2406f + y * -1.5372f + z * -0.4986f;
        g = x * -0.9689f + y * 1.8758f + z * 0.0415f;
        b = x * 0.0557f + y * -0.2040f + z * 1.0570f;

        r = (float) ((r > 0.0031308f) ? (1.055f * Math.pow(r, 1f / 2.4f) - 0.055f) : 12.92f * r);
        g = (float) ((g > 0.0031308f) ? (1.055f * Math.pow(g, 1f / 2.4f) - 0.055f) : 12.92f * g);
        b = (float) ((b > 0.0031308f) ? (1.055f * Math.pow(b, 1f / 2.4f) - 0.055f) : 12.92f * b);

        rgb[0] = (int) (Math.max(0, Math.min(1, r)) * 255);
        rgb[1] = (int) (Math.max(0, Math.min(1, g)) * 255);
        rgb[2] = (int) (Math.max(0, Math.min(1, b)) * 255);
    }

    public static class FastTanh {

        public static float eTilde(float x) {
            final float x2 = x * x;
            final float x4 = x2 * x2;
            return (float) (1 + Math.abs(x) + 0.5658 * x2 + 0.143 * x4);
        }

        public static float compute(float x) {
            // check Appendices C.1. in paper https://arxiv.org/pdf/1702.07825.pdf
            final float eTilde = eTilde(x);
            final float eTilde1 = 1 / eTilde;
            final float epsilon = 0.000001f;
            final int sign = (x > epsilon) ? 1 : (x > -epsilon) ? 0 : -1;
            return sign * (eTilde - eTilde1) / (eTilde + eTilde1);
        }
    }
    
    

}
