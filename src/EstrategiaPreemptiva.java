
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alejandro
 */
public abstract class EstrategiaPreemptiva extends Estrategia {

    public EstrategiaPreemptiva(int tip, int tcp, int tfp) {
        super(tip, tcp, tfp);
    }

    public EstrategiaPreemptiva() {
    }
    
    
    @Override
    protected void enlistarProcesoNuevo(){
        Proceso p = pNuevos.poll();
        p.nuevaRafaga();
        pListos.add(p);
    }
    
    @Override
    protected void enlistarProceso(){
        Proceso p = pBloqueados.poll();
        p.nuevaRafaga();
        pListos.add(p);
    }
    
    protected void enlistarProcesoEjecutando(){
        pEjecutando.aumentarRafagasRestantes();
        pListos.add(pEjecutando);
        pEjecutando = null;        
    }
    
    @Override
    protected void ejecutarProcesoAnterior(){
        pEjecutando = pListos.poll();
        ultimoProcE = pEjecutando.getNombre();
        pEjecutando.disminuirRafagasRestantes();        
    }
    
    protected void ejecutarProceso(){
        pEjecutando = pListos.poll();
        ultimoProcE = pEjecutando.getNombre();
        pEjecutando.sumarTcp(tcp);
        pEjecutando.disminuirRafagasRestantes();
    }
    
    
}
