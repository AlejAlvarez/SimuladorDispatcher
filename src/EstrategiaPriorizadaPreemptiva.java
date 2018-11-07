
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
public abstract class EstrategiaPriorizadaPreemptiva extends EstrategiaPriorizada{

    
    public EstrategiaPriorizadaPreemptiva(int tip, int tcp, int tfp) {
        super(tip, tcp, tfp);
    }

    public EstrategiaPriorizadaPreemptiva() {
    }
    
    
    @Override
    protected void enlistarProceso(){
        Proceso p = pBloqueados.poll();
        p.nuevaRafaga(tcp);
        pListos.add(p);
    }
    
    @Override
    protected void enlistarProcesoNuevo(){
        Proceso p = pNuevos.poll();
        p.nuevaRafaga(tcp);
        pListos.add(p);
    }
    
    protected void enlistarProcesoEjecutando(){
        pListos.add(pEjecutando);
        pEjecutando = null;
    }
    
    @Override
    protected void ejecutarProceso(){
        cpuSO += tcp;
        pEjecutando = pListos.pollFirst();
        ultimoProcE = pEjecutando.getNombre();
        pEjecutando.disminuirRafagasRestantes();
    }
    
    protected void ejecutarProceso(Proceso p){
        cpuSO += tcp;
        pEjecutando = p;
        ultimoProcE = pEjecutando.getNombre();
        pEjecutando.disminuirRafagasRestantes();
    }
    
    @Override
    protected void ejecutarProcesoAnterior(){
        pEjecutando = pListos.pollFirst();
        ultimoProcE = pEjecutando.getNombre();
        pEjecutando.restarTcp(tcp);
        pEjecutando.disminuirRafagasRestantes();
    }
    
    protected boolean comprobarPEjecutando(Proceso p){
        if(pEjecutando == null || pEjecutando.getTRafagaR() > p.getTRafagaR()){
            return true;
        }
        return false;
    }
    
    
}
