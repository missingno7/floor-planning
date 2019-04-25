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
public class VFloat extends Variable {

    VFloat() {
        super();
        tp=Type.FLOAT;
    }
    
    public void setVal(float[] val){
        this.val=val;
        hasVal=true;
    }
    
    public float[] getVal(){
        if(!hasVal)throw new java.lang.UnsupportedOperationException("Value was not assigned.");
        return val;
    }
    
    float[] val;

}
