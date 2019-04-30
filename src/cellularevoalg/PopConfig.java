/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cellularevoalg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import variableregister.VBool;
import variableregister.VDouble;
import variableregister.VFloat;
import variableregister.VInteger;
import variableregister.VLong;
import variableregister.VariReg;
import variableregister.Variable;
import variableregister.Variable.Type;

/**
 *
 * @author J_Vestfal
 */
public class PopConfig {

    public PopConfig() {
        reg = new VariReg();
    }

    // static method to create instance of Singleton class 
    public static PopConfig getInstance() 
    { 
        if (single_instance == null) 
            single_instance = new PopConfig(); 
  
        return single_instance; 
    } 
    
    /*
    // For backwards compatibility
    public void initialize(String filename) {
        reg = new VariReg();
        LoadConfig(filename);
    }*/

    public void LoadConfig(String filename) {
        String line = "";
        String cvsSplitBy = " ";

        cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Detected threads: " + cores);

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] strVals = line.split(cvsSplitBy);

                if (strVals.length < 2) {
                    continue;
                }

                switch (strVals[0]) {
                    case "xpopsize":
                        xpopsize = Integer.parseInt(strVals[1]);
                        System.out.println("xpopsize = " + xpopsize);
                        break;
                    case "ypopsize":
                        ypopsize = Integer.parseInt(strVals[1]);
                        System.out.println("ypopsize = " + ypopsize);
                        break;

                    case "threads":
                        cores = Integer.parseInt(strVals[1]);
                        System.out.println("threads = " + cores);
                        break;

                    case "crossrate":
                        crossrate = Float.parseFloat(strVals[1]);
                        System.out.println("crossrate = " + crossrate);
                        break;

                    case "mutamount":
                        mutamount = Float.parseFloat(strVals[1]);
                        System.out.println("mutamount = " + mutamount);
                        break;
                    case "mutprob":
                        mutprob = Float.parseFloat(strVals[1]);
                        System.out.println("mutprob = " + mutprob);
                        break;
                    case "gennew":
                        gennew = Boolean.parseBoolean(strVals[1]);
                        System.out.println("gennew = " + gennew);
                        break;
                    case "drawpop":
                        drawpop = Boolean.parseBoolean(strVals[1]);
                        System.out.println("drawpop = " + drawpop);
                        break;
                    default:
                        Variable var = reg.items.get(strVals[0]);

                        if (var != null) {
                            if (strVals.length < 2) {
                                throw new java.lang.UnsupportedOperationException("Variabla " + strVals[0] + " has bad size " + strVals.length);
                            }
                            switch (var.tp) {
                                case INT:
                                    int[] iVals = new int[strVals.length - 1];
                                    for (int i = 1; i < strVals.length; i++) {
                                        iVals[i - 1] = Integer.parseInt(strVals[i]);

                                    }
                                    ((VInteger) var).setVal(iVals);
                                    break;
                                case LONG:
                                    long[] lVals = new long[strVals.length - 1];
                                    for (int i = 1; i < strVals.length; i++) {
                                        lVals[i - 1] = Long.parseLong(strVals[i]);
                                    }
                                    ((VLong) var).setVal(lVals);
                                    break;
                                case FLOAT:
                                    float[] fVals = new float[strVals.length - 1];
                                    for (int i = 1; i < strVals.length; i++) {
                                        fVals[i - 1] = Float.parseFloat(strVals[i]);

                                    }
                                    ((VFloat) var).setVal(fVals);
                                    break;
                                case DOUBLE:
                                    double[] dVals = new double[strVals.length - 1];
                                    for (int i = 1; i < strVals.length; i++) {
                                        dVals[i - 1] = Double.parseDouble(strVals[i]);
                                    }
                                    ((VDouble) var).setVal(dVals);
                                    break;
                                case BOOL:
                                    boolean[] bVals = new boolean[strVals.length - 1];
                                    for (int i = 1; i < strVals.length; i++) {
                                        bVals[i - 1] = Boolean.parseBoolean(strVals[i]);
                                    }
                                    ((VBool) var).setVal(bVals);
                                    break;
                            }
                        }

                        System.out.print(strVals[0] + " = ");
                        for (int i = 1; i < strVals.length; i++) {
                            if (i != strVals.length - 1) {
                                System.out.print(strVals[i] + ", ");
                            } else {
                                System.out.print(strVals[i]);
                            }
                        }
                        System.out.println("");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public VariReg reg;
    public int xpopsize, ypopsize;
    public int tsize;
    public float mutamount, mutprob, crossrate;
    public int cores;
    public boolean gennew, drawpop;
    
    // static variable single_instance of type Singleton 
    private static PopConfig single_instance = null; 
    
}
