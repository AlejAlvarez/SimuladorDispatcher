
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
        Estrategia fcfs = new FCFS(0, 0, 0);
        File f = new File("tanda4.txt");
        fcfs.ejecutar(f);
    }
    
}
