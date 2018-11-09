
import java.util.Comparator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alejandro
 */
public class PrioridadComparator implements Comparator<Proceso> {

    @Override
    public int compare(Proceso o1, Proceso o2) {
        return o2.getPrioridad() - o1.getPrioridad();
    }
    
}
