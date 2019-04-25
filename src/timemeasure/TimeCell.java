/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timemeasure;

/**
 *
 * @author J_Vestfal
 */
public class TimeCell {
    
    TimeCell(){
        totalTime=0;
        running=false;
    }
    
    public boolean running;
    public long totalTime;
    public long startTime;
}
