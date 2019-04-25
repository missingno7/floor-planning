/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package variableregister;

/**
 *
 * @author J_Vestfal
 */
public class VDouble extends Variable {

    VDouble() {
        super();
        tp=Type.DOUBLE;
    }
    
    public void setVal(double[] val){
        this.val=val;
        hasVal=true;
    }
    
    public double[] getVal(){
        if(!hasVal)throw new java.lang.UnsupportedOperationException("Value was not assigned.");
        return val;
    }
    
    double[] val;

}
