
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

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
    protected void leerArchivo(File f){
        try{
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String cadena;
            while((cadena=br.readLine())!=null){
                StringTokenizer st = new StringTokenizer(cadena);
                String nombre = (st.nextToken());
                int tArribo = Integer.parseInt(st.nextToken());
                int rafagasTotales = Integer.parseInt(st.nextToken());
                int duracionRafaga = Integer.parseInt(st.nextToken());
                int duracionES = Integer.parseInt(st.nextToken());
                int prioridad = Integer.parseInt(st.nextToken());
                Proceso p = new Proceso(nombre, tArribo, rafagasTotales, duracionRafaga, duracionES, prioridad); //Lectura e instancia del proceso
                p.setTIncorp(tip);
                p.nuevaRafaga();
                pNuevos.add(p);
                cantProcesos++;
            }
            System.out.println("Lectura del archivo completada con Éxito!");
            cpuSO += tip*cantProcesos + tfp*cantProcesos;
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            System.err.println("Archivo de texto no encontrado!");
        }
        catch (IOException e){
            e.printStackTrace();
            System.err.println("Error en la Entrada/Salida!");
        }
    }    

    @Override
    protected void bloquearProceso(){
        pEjecutando.setHDesbloq(contador);
        pEjecutando.nuevaRafaga();
        pBloqueados.add(pEjecutando);
        pEjecutando = null;
    }
    
    @Override
    protected void enlistarProceso(){
        //Proceso p = pBloqueados.poll();
        pListos.add(pBloqueados.poll());
    }
    
    protected void enlistarProceso(Proceso p){
        pListos.add(p);
        pBloqueados.remove(p);
    }
    
    @Override
    protected void enlistarProcesoNuevo(){
        Proceso p = pNuevos.poll();
        p.nuevaRafaga();
        pListos.add(p);
    }
    
    protected void enlistarProcesoEjecutando(){
        pEjecutando.aumentarRafagasRestantes();
        pListos.add(pEjecutando);
        pEjecutando = null;
    }
    
    @Override
    protected void ejecutarProceso(){
        cpuSO += tcp;
        pEjecutando = pListos.poll();
        ultimoProcE = pEjecutando.getNombre();
        pEjecutando.sumarTcp(tcp);
        pEjecutando.disminuirRafagasRestantes();
    }
    
    protected void ejecutarProceso(Proceso p){
        cpuSO += tcp;
        pEjecutando = p;
        ultimoProcE = pEjecutando.getNombre();
        pEjecutando.sumarTcp(tcp);
        pEjecutando.disminuirRafagasRestantes();
    }
    
    @Override
    protected void ejecutarProcesoAnterior(){
        pEjecutando = pListos.poll();
        ultimoProcE = pEjecutando.getNombre();
        pEjecutando.disminuirRafagasRestantes();
    }
    
    protected void ejecutarPrimerProceso(Proceso p){
        pEjecutando = p;
        ultimoProcE = pEjecutando.getNombre();
        pEjecutando.disminuirRafagasRestantes();
        
    }
    
    protected boolean comprobarTRPEjecutando(Proceso p){
        if(pEjecutando == null || pEjecutando.getTRafagaR() > p.getTRafagaR()){
            return true;
        }
        return false;
    }
    
    protected boolean comprobarPrioPEjecutando(Proceso p){
        if(pEjecutando == null || pEjecutando.getPrioridad() < p.getPrioridad()){
            return true;
        }
        return false;
    }
    
    protected void sumarTcp(int tcp){
        if(pEjecutando != null){
            cpuSO += tcp;
            pEjecutando.sumarTcp(tcp);
        }
    }
    
    protected void ejecutarProcesoSinTcp(Proceso p){
        pEjecutando = pListos.poll();
        ultimoProcE = pEjecutando.getNombre();
        pEjecutando.disminuirRafagasRestantes();        
    }
    
    
}
