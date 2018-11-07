
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
public class TIncorporacionYCpuComparator implements Comparator<Proceso> {

    @Override
    public int compare(Proceso o1, Proceso o2) {
        int resultado = o1.getTIncorp() - o2.getTIncorp();
        if(resultado == 0){
            return o1.getDuracionRafaga() - o2.getDuracionRafaga();
        }
        return resultado;
    }
    
}
