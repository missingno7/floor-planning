/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timemeasure;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author J_Vestfal
 */
public class TimeMeasure {

    private static TimeMeasure instance = null;

    protected TimeMeasure() {
        // Exists only to defeat instantiation.
        cells = new HashMap<String, TimeCell>();
    }

    public static TimeMeasure getInstance() {
        if (instance == null) {
            instance = new TimeMeasure();
        }
        return instance;
    }

    HashMap<String, TimeCell> cells;

    public void startTimer(String key) {
        TimeCell cell = cells.get(key);

        if (cell == null) {
            cell = new TimeCell();
            cells.put(key, cell);
        } else if (cell.running) {
            return;
        }

        cell.running = true;
        cell.startTime = System.nanoTime();

    }

    public void stopTimer(String key) {
        long stopTime = System.nanoTime();

        TimeCell cell = cells.get(key);
        if (cell == null) {
            return;
        }

        if (!cell.running) {
            return;
        }

        cell.totalTime += stopTime - cell.startTime;
        cell.running = false;

    }

    public long getTotal(String key) {
        TimeCell cell = cells.get(key);
        if (cell == null) {
            return 0;
        }
        return cell.totalTime;
    }

    public void print() {
        /* Display content using Iterator*/
        Set set = cells.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            HashMap.Entry mentry = (HashMap.Entry) iterator.next();
            System.out.print(mentry.getKey() + " -- " + ((TimeCell) mentry.getValue()).totalTime);
            System.out.println();
        }

    }

}
