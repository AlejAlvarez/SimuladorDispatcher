
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
public class TRestanteComparator implements Comparator<Proceso> {

    @Override
    public int compare(Proceso o1, Proceso o2) {
        return o1.getTRafagaR() - o2.getTRafagaR();
    }
    
}
