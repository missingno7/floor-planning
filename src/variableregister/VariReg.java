/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package variableregister;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author J_Vestfal
 */
public class VariReg {

    public VariReg(){
        this.items=new HashMap<>();
    }
    
    public void newInt(String name) {
        items.put(name, new VInteger());
    }

    public void newLong(String name) {
        items.put(name, new VLong());
    }

    public void newFloat(String name) {
        items.put(name, new VFloat());
    }

    public void newDouble(String name) {
        items.put(name, new VDouble());
    }

    public void newBool(String name) {
        items.put(name, new VBool());
    }

    public int[] getInt(String name) {
        Variable item = items.get(name);
        if (item == null) {
            throw new java.lang.UnsupportedOperationException("Value does not exist.");
        }
        return ((VInteger) item).getVal();
    }

    public long[] getLong(String name) {
        Variable item = items.get(name);
        if (item == null) {
            throw new java.lang.UnsupportedOperationException("Value does not exist.");
        }
        return ((VLong) item).getVal();
    }

    public float[] getFloat(String name) {
        Variable item = items.get(name);
        if (item == null) {
            throw new java.lang.UnsupportedOperationException("Value does not exist.");
        }
        return ((VFloat) item).getVal();
    }

    public double[] getDouble(String name) {
        Variable item = items.get(name);
        if (item == null) {
            throw new java.lang.UnsupportedOperationException("Value does not exist.");
        }
        return ((VDouble) item).getVal();
    }

    public boolean[] getBool(String name) {
        Variable item = items.get(name);
        if (item == null) {
            throw new java.lang.UnsupportedOperationException("Value does not exist.");
        }
        return ((VBool) item).getVal();
    }
    
    

    public Map<String, Variable> items;
}
