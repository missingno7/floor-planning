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
public class VLong extends Variable {

    VLong() {
        super();
        tp = Type.LONG;
    }

    public void setVal(long[] val) {
        this.val = val;
        hasVal = true;
    }

    public long[] getVal() {
        if (!hasVal) {
            throw new java.lang.UnsupportedOperationException("Value was not assigned.");
        }
        return val;
    }

    long[] val;

}
