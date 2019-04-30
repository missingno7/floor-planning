/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cellularevoalg;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author J_Vestfal
 */
public class Population {

    public static enum ThrAction {
        INIT, RANDOMIZE, NEXTGEN
    }

    class ThrTask {

        ThrAction act;
        public int from, to;

    }

    // Function that evaluate queue of commands, consumer thread
    public void consume() throws InterruptedException {
        ThrTask val;
        Random rnd = new Random();

        while (m_taskList.size() != 0) {
            synchronized (m_taskList) {
                // Get value
                if (m_taskList.size() == 0) {
                    break;
                }
                val = m_taskList.removeFirst();
            }

           
            switch (val.act) {
                // Initialize population
                case INIT:
                    for (int i = val.from; i < val.to; i++) {
                        m_currGenInds[i] = m_templateInd.clone();
                        m_nextGenInds[i] = m_templateInd.makeBlank(); // Temporary can be blank
                    }
                    break;

                // Randomize each initialized individual
                case RANDOMIZE:
                    for (int i = val.from; i < val.to; i++) {
                        m_currGenInds[i].randomize(m_indData, rnd);
                        m_currGenInds[i].countFitness(m_indData);
                        if (m_cfg.drawpop) {
                            m_nextGenInds[i].countColor();
                        }
                    }

                    break;

                // Next generation - copy the best to new population and mutate them.
                case NEXTGEN:
                    Individual[] rinds = new Individual[2];

                    for (int i = val.from; i < val.to; i++) {
                        if (rnd.nextFloat() < m_cfg.crossrate) {
                            dualTournamentL5(i / m_popHeight, i % m_popHeight, rinds);

                            if (rinds[1] != null) {
                                rinds[0].crossoverTo(rinds[1], m_nextGenInds[i], rnd);
                            } else {
                                rinds[0].mutateTo(rnd.nextFloat() * m_cfg.mutamount, m_cfg.mutprob, m_nextGenInds[i], rnd);
                            }
                        } else {
                            tournamentL5(i / m_popHeight, i % m_popHeight).mutateTo(rnd.nextFloat() * m_cfg.mutamount, m_cfg.mutprob, m_nextGenInds[i], rnd);
                        }

                        m_nextGenInds[i].countFitness(m_indData); // TRAIN AND TEST SPLIT
                        if (m_cfg.drawpop) {
                            m_nextGenInds[i].countColor();
                        }
                    }
                    break;
            }

        }
    }

    public Population(Individual srcInd, IndData data, PopConfig cfg) throws InterruptedException {
        this.m_cfg = cfg;
        this.m_indData = data;

        this.m_popWidth = cfg.xpopsize;
        this.m_popHeight = cfg.ypopsize;

        this.m_templateInd = srcInd;

        m_threads = new Thread[cfg.cores];

        m_currGenInds = new Individual[m_popWidth * m_popHeight];
        m_nextGenInds = new Individual[m_popWidth * m_popHeight];

        srcInd.countFitness(data);
        if (cfg.drawpop) {
            srcInd.countColor();
        }

        m_taskList.clear();
        int step = m_currGenInds.length / (m_threads.length * 128);

        int from = 0;
        while (from + step < m_currGenInds.length) {
            ThrTask tsk = new ThrTask();
            tsk.from = from;
            tsk.to = from + step;
            tsk.act = ThrAction.INIT;
            m_taskList.add(tsk);
            from += step;
        }

        if (from != m_currGenInds.length) {
            ThrTask tsk = new ThrTask();
            tsk.from = from;
            tsk.to = m_currGenInds.length;
            tsk.act = ThrAction.INIT;
            m_taskList.add(tsk);
        }

        for (int i = 0; i < m_threads.length; i++) {

            final int tmp = i;

            m_threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        consume();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            // Start both threads
            m_threads[i].start();

        }

        for (int i = 0; i < m_threads.length; i++) {
            // t1 finishes before t2
            m_threads[i].join();
        }

        m_gensCount = 0;
        //rnd = new Random();
    }

    public void nextGen() throws InterruptedException {

        Individual[] rinds = new Individual[2];

        m_taskList.clear();
        int step = m_currGenInds.length / (m_threads.length * 128);

        int from = 0;
        while (from + step < m_currGenInds.length) {
            ThrTask tsk = new ThrTask();
            tsk.from = from;
            tsk.to = from + step;
            tsk.act = ThrAction.NEXTGEN;
            m_taskList.add(tsk);
            from += step;
        }

        if (from != m_currGenInds.length) {
            ThrTask tsk = new ThrTask();
            tsk.from = from;
            tsk.to = m_currGenInds.length;
            tsk.act = ThrAction.NEXTGEN;
            m_taskList.add(tsk);
        }

        for (int i = 0; i < m_threads.length; i++) {

            final int tmp = i;

            m_threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        consume();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            // Start both threads
            m_threads[i].start();

        }

        for (int i = 0; i < m_threads.length; i++) {
            // t1 finishes before t2
            m_threads[i].join();
        }

        Individual[] tmp = m_currGenInds;
        m_currGenInds = m_nextGenInds;
        m_nextGenInds = tmp;

        m_gensCount++;
    }

    public void Randomize(Random rnd) throws InterruptedException {

        m_taskList.clear();
        int step = m_currGenInds.length / (m_threads.length * 128);

        int from = 0;
        while (from + step < m_currGenInds.length) {
            ThrTask tsk = new ThrTask();
            tsk.from = from;
            tsk.to = from + step;
            tsk.act = ThrAction.RANDOMIZE;
            m_taskList.add(tsk);
            from += step;
        }

        if (from != m_currGenInds.length) {
            ThrTask tsk = new ThrTask();
            tsk.from = from;
            tsk.to = m_currGenInds.length;
            tsk.act = ThrAction.RANDOMIZE;
            m_taskList.add(tsk);
        }

        for (int i = 0; i < m_threads.length; i++) {

            final int tmp = i;

            m_threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        consume();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            // Start both threads
            m_threads[i].start();

        }

        for (int i = 0; i < m_threads.length; i++) {
            // t1 finishes before t2
            m_threads[i].join();
        }
    }

    public Individual getBest() {
        Individual best = m_currGenInds[0];

        for (int i = 1; i < m_currGenInds.length; i++) {

            if (best.fitness < m_currGenInds[i].fitness) {
                best = m_currGenInds[i];
            }
        }

        return best;
    }

    public Individual getWorst() {
        Individual worst = m_currGenInds[0];

        for (int i = 0; i < m_currGenInds.length; i++) {

            if (worst.fitness > m_currGenInds[i].fitness) {
                worst = m_currGenInds[i];
            }
        }

        return worst;
    }

    public float avgFitness() {
        float avgFit = 0;

        for (int i = 0; i < m_currGenInds.length; i++) {
            avgFit += m_currGenInds[i].fitness;
        }
        return avgFit / (m_cfg.xpopsize * m_cfg.ypopsize);
    }

    public void paintPop(String imgName) {

        //System.out.println("PAINTIN");
        int[] lab = new int[3];
        int[] rgb = new int[3];

        Individual best = getBest();
        Individual worst = getWorst();
        float max = best.fitness;
        float min = worst.fitness;

        /*float max = 255;
        float min = 0;*/
        float diff = max - min;

        
         float max_x=m_currGenInds[0].colX;
         float min_x=m_currGenInds[0].colX;
         
         float max_y=m_currGenInds[0].colY;
         float min_y=m_currGenInds[0].colY;
         
         for (int i = 1; i < m_currGenInds.length; i++) {
            if(m_currGenInds[i].colX>max_x)max_x=m_currGenInds[i].colX;
            if(m_currGenInds[i].colY>max_y)max_y=m_currGenInds[i].colY;
            if(m_currGenInds[i].colX<min_x)min_x=m_currGenInds[i].colX;
            if(m_currGenInds[i].colY<min_y)min_y=m_currGenInds[i].colY;
         }
          float diff_x = max_x - min_x;
          float diff_y = max_y - min_y;
        
        
        BufferedImage image = new BufferedImage(m_popWidth, m_popHeight, BufferedImage.TYPE_INT_RGB);
        int width = image.getWidth();
        int height = image.getHeight();
        WritableRaster raster = image.getRaster();

        for (int i = 0; i < m_currGenInds.length; i++) {

            int xx = i / height;
            int yy = i % height;

            int[] pixels = raster.getPixel(xx, yy, (int[]) null);
            int pix_intensity = (int) (((m_currGenInds[i].fitness - min) * 100) / diff);
            int pix_color_x = (int) (((m_currGenInds[i].colX - min_x) * 100) / diff_x);
            int pix_color_y = (int) (((m_currGenInds[i].colY - min_y) * 100) / diff_y);
            
                      
            lab[0] = pix_intensity;
            lab[1] = pix_color_x-50;
            lab[2] = pix_color_y-50;

            Other.LABtoRGB(lab, pixels);

            raster.setPixel(xx, yy, pixels);
        }

        try {
            ImageIO.write(image, "png", new File(imgName));
        } catch (IOException ex) {
            Logger.getLogger(Population.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Individual tournamentL5(int x, int y) {
        //System.out.println(x+", "+y);
        Individual bestInd = m_currGenInds[x * m_popHeight + y];
        //System.out.println(xpopsize+",,"+ypopsize);
        int xa, ya;

        xa = x - 1;
        if (xa < 0) {
            xa += m_cfg.xpopsize;
        }

        if (m_currGenInds[xa * m_popHeight + y].fitness > bestInd.fitness) {
            bestInd = m_currGenInds[xa * m_popHeight + y];
        }

        xa = x + 1;

        if (xa >= m_cfg.xpopsize) {
            xa -= m_cfg.xpopsize;
        }

        if (m_currGenInds[xa * m_popHeight + y].fitness > bestInd.fitness) {
            bestInd = m_currGenInds[xa * m_popHeight + y];
        }

        ya = y - 1;

        if (ya < 0) {
            ya += m_cfg.ypopsize;
        }

        if (m_currGenInds[x * m_popHeight + ya].fitness > bestInd.fitness) {
            bestInd = m_currGenInds[x * m_popHeight + ya];
        }

        ya = y + 1;

        if (ya >= m_cfg.ypopsize) {
            ya -= m_cfg.ypopsize;
        }

        if (m_currGenInds[x * m_popHeight + ya].fitness > bestInd.fitness) {
            bestInd = m_currGenInds[x * m_popHeight + ya];
        }

        return bestInd;
    }

    private void dualTournamentL5(int x, int y, Individual[] rinds) {
        //System.out.println(x+", "+y + " - "+ x*height+y);

        rinds[0] = m_currGenInds[x * m_popHeight + y];
        rinds[1] = null;

        //System.out.println(xpopsize+",,"+ypopsize);
        int xa, ya;

        xa = x - 1;
        if (xa < 0) {
            xa += m_cfg.xpopsize;
        }

        if (m_currGenInds[xa * m_popHeight + y].fitness > rinds[0].fitness) {
            rinds[1] = rinds[0];
            rinds[0] = m_currGenInds[xa * m_popHeight + y];
        } else if (m_currGenInds[xa * m_popHeight + y].fitness < rinds[0].fitness) {

            if (rinds[1] != null) {
                if (m_currGenInds[xa * m_popHeight + y].fitness > rinds[1].fitness) {
                    rinds[1] = m_currGenInds[xa * m_popHeight + y];
                }
            } else {
                rinds[1] = m_currGenInds[xa * m_popHeight + y];
            }
        }

        xa = x + 1;

        if (xa >= m_cfg.xpopsize) {
            xa -= m_cfg.xpopsize;
        }

        if (m_currGenInds[xa * m_popHeight + y].fitness > rinds[0].fitness) {
            rinds[1] = rinds[0];
            rinds[0] = m_currGenInds[xa * m_popHeight + y];
        } else if (m_currGenInds[xa * m_popHeight + y].fitness < rinds[0].fitness) {

            if (rinds[1] != null) {
                if (m_currGenInds[xa * m_popHeight + y].fitness > rinds[1].fitness) {
                    rinds[1] = m_currGenInds[xa * m_popHeight + y];
                }
            } else {
                rinds[1] = m_currGenInds[xa * m_popHeight + y];
            }
        }

        ya = y - 1;

        if (ya < 0) {
            ya += m_cfg.ypopsize;
        }

        if (m_currGenInds[x * m_popHeight + ya].fitness > rinds[0].fitness) {
            rinds[1] = rinds[0];
            rinds[0] = m_currGenInds[x * m_popHeight + ya];
        } else if (m_currGenInds[x * m_popHeight + ya].fitness < rinds[0].fitness) {

            if (rinds[1] != null) {
                if (m_currGenInds[x * m_popHeight + ya].fitness > rinds[1].fitness) {
                    rinds[1] = m_currGenInds[x * m_popHeight + ya];
                }
            } else {
                rinds[1] = m_currGenInds[x * m_popHeight + ya];
            }
        }

        ya = y + 1;

        if (ya >= m_cfg.ypopsize) {
            ya -= m_cfg.ypopsize;
        }

        if (m_currGenInds[x * m_popHeight + ya].fitness > rinds[0].fitness) {
            rinds[1] = rinds[0];
            rinds[0] = m_currGenInds[x * m_popHeight + ya];
        } else if (m_currGenInds[x * m_popHeight + ya].fitness < rinds[0].fitness) {

            if (rinds[1] != null) {
                if (m_currGenInds[x * m_popHeight + ya].fitness > rinds[1].fitness) {
                    rinds[1] = m_currGenInds[x * m_popHeight + ya];
                }
            } else {
                rinds[1] = m_currGenInds[x * m_popHeight + ya];
            }
        }


        /*if(rinds[1]==null){
            rinds[1]=inds[xa][ya];
        }*/
    }

    public int getGen() {
        return m_gensCount;
    }

    private Individual m_templateInd;
    private Individual[] m_currGenInds;
    private Individual[] m_nextGenInds;
    private int m_popWidth, m_popHeight;

    private int m_gensCount;

    PopConfig m_cfg;
    IndData m_indData;

    //Random rnd;

    Thread[] m_threads;
    
    
    LinkedList<ThrTask> m_taskList = new LinkedList<>();
}
