
import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.TreeSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alejandro
 */
    //Prioridad Preemptiva
public class Prioridad extends EstrategiaPriorizadaPreemptiva {

    public Prioridad(int tip, int tcp, int tfp) {
        super(tip, tcp, tfp);
        pNuevos = new PriorityQueue(10, new TIncorpYPrioComparator());
        pBloqueados = new PriorityQueue(10, new HDesbloqueoComparator());
        pListos = new TreeSet(new PrioridadComparator());
    }

    public Prioridad() {
        super(0,0,0);
        pNuevos = new PriorityQueue(10, new TIncorpYPrioComparator());
        pBloqueados = new PriorityQueue(10, new HDesbloqueoComparator());
        pListos = new TreeSet(new PrioridadComparator());
    }

    
    
    @Override
    public void ejecutar(File f) {
        contador = 0;
        this.leerArchivo(f);
        while (!((pNuevos.isEmpty())&&(pListos.isEmpty())&&(pBloqueados.isEmpty())&&(pEjecutando==null))){
            System.out.println("Unidad de tiempo actual: " + contador);
            if(!pNuevos.isEmpty()){
                while((!pNuevos.isEmpty()) && (contador >= pNuevos.peek().getTIncorp())){  //ESTO NO ESTA TAN CORRECTO.
                    Proceso p = pNuevos.peek();
                    if(this.comprobarPrioPEjecutando(p)){
                        System.out.println("Proceso " + p.getNombre() + " cumplió su TIP y procede a ser ejecutado");
                        if(pEjecutando!=null){
                            System.out.println("Proceso " + pEjecutando.getNombre() + " deja de ser ejecutado y es enlistado");
                            System.out.println("Se produce cambio de contexto");
                            this.enlistarProcesoEjecutando();
                        }
                        if(ultimoProcE.isEmpty()){
                            System.out.println("No había ultimo prcoeso");
                            this.ejecutarPrimerProceso(p);
                        }
                        else{
                            this.ejecutarProceso(p);
                        }
                        pNuevos.remove(p);
                    }
                    else{
                        System.out.println("Proceso " + p.getNombre() + " cumplió su TIP y fue enlistado");
                        this.enlistarProcesoNuevo();                        
                    }
                }
            }
            if(!pBloqueados.isEmpty()){
                while((!pBloqueados.isEmpty()) && (contador >= pBloqueados.peek().getHDesbloq())){
                    Proceso p = pBloqueados.peek();
                    if(this.comprobarPrioPEjecutando(p)){
                        System.out.println("Proceso " + p.getNombre() + " terminó su ES y procede a ser ejecutado");
                        if(pEjecutando!=null){
                            System.out.println("Proceso " + pEjecutando.getNombre() + " deja de ser ejecutado y es enlistado");
                            System.out.println("Se produce cambio de contexto");
                            this.enlistarProcesoEjecutando();
                        }
                        this.ejecutarProceso(p);          
                        pBloqueados.remove(p);
                    }
                    else{
                        System.out.println("Orden antes de enlistar proceso de los procesos bloqueados: ");
                        Iterator<Proceso> iterador = pBloqueados.iterator();
                        while(iterador.hasNext()){
                            Proceso e = iterador.next();
                            System.out.println("    " + e.getNombre());
                        }
                        System.out.println("Orden despues de enlistar proceso de los procesos listos: ");
                        Iterator<Proceso> iterator = pListos.iterator();
                        while(iterator.hasNext()){
                            Proceso e = iterator.next();
                            System.out.println("    " + e.getNombre());
                        }
                        System.out.println("Proceso " + p.getNombre() + " terminó su ES y fue enlistado");
                        System.out.println("Rafagas restantes de " + p.getNombre() +": " + p.getRafagasRestantes());
                        this.enlistarProceso(p);
                        System.out.println("Orden despues de enlistar proceso de los procesos bloqueados: ");
                        iterador = pBloqueados.iterator();
                        while(iterador.hasNext()){
                            Proceso e = iterador.next();
                            System.out.println("    " + e.getNombre());
                        }
                        System.out.println("Orden despues de enlistar proceso de los procesos listos: ");
                        iterator = pListos.iterator();
                        while(iterator.hasNext()){
                            Proceso e = iterator.next();
                            System.out.println("    " + e.getNombre());
                        }
                    }
                }
            }
            if((pEjecutando!=null)){
                if(pEjecutando.getTRafagaR()==0){
                    if(pEjecutando.getRafagasRestantes()==0){
                        System.out.println("Proceso " + pEjecutando.getNombre() + " terminó todas sus ráfagas");
                        this.finalizarProceso();
                        if(pNuevos.isEmpty() && pListos.isEmpty() && pBloqueados.isEmpty()){
                            this.ultimoTfp = contador + tfp;
                        }
                    }
                    else{
                        System.out.println("Proceso " + pEjecutando.getNombre() + " fue bloqueado durante " + pEjecutando.getDuracionES() + " unidades de tiempo.");
                        this.bloquearProceso();
                    }
                }
                else{
                    pEjecutando.decrementarTRafagaR();
                    //System.out.println("RAFAGAS RESTANTES DE " + pEjecutando.getNombre() + " = " + pEjecutando.getTRafagaR());
                }
            }
            if(pEjecutando == null){
                if(!pListos.isEmpty()){
                    Proceso p = pListos.first();
                    System.out.println("Proceso " + p.getNombre() + " procede a ser ejecutado");
                    if(ultimoProcE.isEmpty() || p.getNombre() == ultimoProcE){
                        this.ejecutarProcesoAnterior();
                        System.out.println("No se produce cambio de contexto");
                    }
                    else{
                        this.ejecutarProceso();
                        System.out.println("Se produce cambio de contexto");
                    }
                    pEjecutando.decrementarTRafagaR();  //SE CARGA A EJECUTAR Y EJECUTA UNA UNIDAD DE TIEMPO DE LA RAFAGA
                    this.incrementarTListoProcesos();
                }
                else{
                    this.cpuDesocupada++;
                }
            }
            else if(!pListos.isEmpty()){
                this.incrementarTListoProcesos();
            }
            contador++;
        }
        Collections.sort(pFinalizados, new TIncorporacionComparator());
        this.imprimirResultados();
    }

}
