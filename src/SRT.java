
import java.io.File;
import java.util.PriorityQueue;
import java.util.TreeSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alejandro
 */
    //Shortest Remaining Time
public class SRT extends EstrategiaPriorizadaPreemptiva {

    public SRT(int tip, int tcp, int tfp) {
        super(tip, tcp, tfp);
        pBloqueados = new PriorityQueue(10, new HDesbloqueoComparator());
        pNuevos = new PriorityQueue(10, new TIncorporacionYCpuComparator());
        pListos = new TreeSet(new TRestanteComparator());
    }

    public SRT() {
        super();
        pBloqueados = new PriorityQueue(10, new HDesbloqueoComparator());
        pNuevos = new PriorityQueue(10, new TIncorporacionYCpuComparator());
        pListos = new TreeSet(new TRestanteComparator());
    }

    @Override
    public void ejecutar(File f) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void imprimirResultados() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
