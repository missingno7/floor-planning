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
public class Variable {

    public enum Type {
        INT,LONG,FLOAT,DOUBLE,BOOL
    }

    Variable() {
        hasVal = false;
    }

    public Type tp;
    boolean hasVal;
}
