
import java.io.File;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alejandro
 */


public class MainTest {
    
    public static void main (String[] args){
        /* Las estrategias implementadas son: FCFS, SJF (Es lo mismo que SPN), SRT, RR (Round Robin), Prioridad */
        Estrategia estrategia = new FCFS(1,1,1); //TIP, TCP, TFP.
        File f = new File("tanda1.txt");        //En caso de Round Robin, instanciar la clase de la forma: TIP, TCP, TFP, Quantum.
        estrategia.ejecutar(f);
    }
    
}
