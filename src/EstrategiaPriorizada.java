
import java.util.Iterator;
import java.util.TreeSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alejandro
 */
public abstract class EstrategiaPriorizada extends Estrategia {
    
    protected static TreeSet<Proceso> pListos;

    public EstrategiaPriorizada(int tip, int tcp, int tfp) {
        super(tip, tcp, tfp);
    }

    public EstrategiaPriorizada() {
        super();
    }
    
    //Metodos para enlistar en pListos, que en este caso es PriorityQueue y no LinkedList
    @Override
    protected void enlistarProceso(){
        /*Proceso p = pBloqueados.poll();  Esto fue algo que se me ocurrio, generar una nueva rafaga para comparar el TRestante, pero resulta innecesario
        p.nuevaRafaga();                   debido a que se puede comparar directamente la duracion de la rafaga de CPU*/
        pListos.add(pBloqueados.poll());
    }
    
    @Override
    protected void enlistarProcesoNuevo(){
        /*Proceso p = pNuevos.poll();      Idem anterior
        p.nuevaRafaga();*/
        pListos.add(pNuevos.poll());
    }
    
    @Override
    protected void ejecutarProcesoAnterior(){
        pEjecutando = pListos.pollFirst();
        ultimoProcE = pEjecutando.getNombre();
        pEjecutando.nuevaRafaga();
        pEjecutando.disminuirRafagasRestantes();       
    }
    
    @Override
    protected void ejecutarProceso(){
        cpuSO += tcp;
        pEjecutando = pListos.pollFirst();
        ultimoProcE = pEjecutando.getNombre();
        pEjecutando.nuevaRafaga(tcp);
        pEjecutando.disminuirRafagasRestantes();
    }
    
    @Override
    protected void incrementarTListoProcesos(){
        Iterator<Proceso> iterator = pListos.iterator();
        while(iterator.hasNext()){
            Proceso p = iterator.next();
            p.incrementarTListo();
        }
    }
    
}
