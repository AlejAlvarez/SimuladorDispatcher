
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
public class HDesbloqueoComparator implements Comparator<Proceso> {

    @Override
    public int compare(Proceso p1, Proceso p2) {
        return p1.getHDesbloq() - p2.getHDesbloq();
    }
    
}
