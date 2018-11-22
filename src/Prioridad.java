
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
        pListos = new PriorityQueue(10, new PrioridadComparator());
    }

    public Prioridad() {
        super(0,0,0);
        pNuevos = new PriorityQueue(10, new TIncorpYPrioComparator());
        pBloqueados = new PriorityQueue(10, new HDesbloqueoComparator());
        pListos = new PriorityQueue(10, new PrioridadComparator());
    }

    public static void setTip(int tip) {
        Estrategia.tip = tip;
    }

    public static void setTfp(int tfp) {
        Estrategia.tfp = tfp;
    }

    public static void setTcp(int tcp) {
        Estrategia.tcp = tcp;
    }
    
    

    @Override
    public void ejecutar(File f) {
        contador = 0;
        this.leerArchivo(f);
        try{
            this.abrirArchivoEscritura();
            while (!((pNuevos.isEmpty())&&(pListos.isEmpty())&&(pBloqueados.isEmpty())&&(pEjecutando==null))){
                System.out.println("");
                bw.write("");
                bw.newLine();
                System.out.println("--------------------------------");
                bw.write("--------------------------------");
                bw.newLine();
                System.out.println("Unidad de tiempo actual: " + contador);
                bw.write("Unidad de tiempo actual: " + contador);
                bw.newLine();
                System.out.println("");
                bw.write("");
                bw.newLine();
                if(!pNuevos.isEmpty()){
                    System.out.println("Estado de la lista de los procesos nuevos:");
                    bw.write("Estado de la lista de los procesos nuevos:");
                    bw.newLine();
                    Iterator<Proceso> it = pNuevos.iterator();
                    while(it.hasNext()){
                        Proceso p = it.next();
                        System.out.println("      " + p.getNombre());
                        bw.write("      " + p.getNombre());
                        bw.newLine();
                    }
                    while((!pNuevos.isEmpty()) && (contador >= pNuevos.peek().getTIncorp())){
                        Proceso p = pNuevos.peek();
                        System.out.println("Proceso " + p.getNombre() + " cumplió su TIP y fue enlistado");
                        bw.write("Proceso " + p.getNombre() + " cumplió su TIP y fue enlistado");
                        bw.newLine();
                        this.enlistarProcesoNuevo();
                    }
                }
                if(!pBloqueados.isEmpty()){
                    System.out.println("Estado de la lista de los procesos bloqueados:");
                    bw.write("Estado de la lista de los procesos bloqueados:");
                    bw.newLine();
                    Iterator<Proceso> it = pBloqueados.iterator();
                    while(it.hasNext()){
                        Proceso p = it.next();
                        System.out.println("      " + p.getNombre());
                        bw.write("      " + p.getNombre());
                        bw.newLine();
                    }
                    while((!pBloqueados.isEmpty()) && (contador >= pBloqueados.peek().getHDesbloq())){
                        Proceso p = pBloqueados.peek();
                        System.out.println("Proceso " + p.getNombre() + " terminó su ES y fue enlistado");
                        bw.write("Proceso " + p.getNombre() + " terminó su ES y fue enlistado");
                        bw.newLine();
                        this.enlistarProceso(p);
                    }
                }
                if((pEjecutando!=null)){
                    if(pEjecutando.getTRafagaR()==0){
                        if(pEjecutando.getRafagasRestantes()==0){
                            System.out.println("Proceso " + pEjecutando.getNombre() + " terminó todas sus ráfagas");
                            bw.write("Proceso " + pEjecutando.getNombre() + " terminó todas sus ráfagas");
                            bw.newLine();
                            this.finalizarProceso();
                            if(pNuevos.isEmpty() && pListos.isEmpty() && pBloqueados.isEmpty()){
                                this.ultimoTfp = contador + tfp;
                            }
                        }
                        else{
                            System.out.println("Proceso " + pEjecutando.getNombre() + " fue bloqueado durante " + pEjecutando.getDuracionES() + " unidades de tiempo.");
                            bw.write("Proceso " + pEjecutando.getNombre() + " fue bloqueado durante " + pEjecutando.getDuracionES() + " unidades de tiempo.");
                            bw.newLine();
                            this.bloquearProceso();
                        }
                    }
                    else if(!pListos.isEmpty()){
                        if(this.comprobarPrioPEjecutando(pListos.peek())){
                            System.out.println("Estado de la lista de los procesos listos:");
                            bw.write("Estado de la lista de los procesos listos:");
                            bw.newLine();
                            Iterator<Proceso> it = pListos.iterator();
                            while(it.hasNext()){
                                Proceso p = it.next();
                                System.out.println("      " + p.getNombre());
                                bw.write("      " + p.getNombre());
                                bw.newLine();
                            }
                            System.out.println("Proceso " + pEjecutando.getNombre() + " deja de ejecutarse y es enlistado");
                            bw.write("Proceso " + pEjecutando.getNombre() + " deja de ejecutarse y es enlistado");
                            bw.newLine();
                            System.out.println("Proceso " + pListos.peek().getNombre() + " posee mas prioridad y procede a ser ejecutado");
                            bw.write("Proceso " + pListos.peek().getNombre() + " posee mas prioridad y procede a ser ejecutado");
                            bw.newLine();
                            System.out.println("Se produce cambio de contexto");
                            bw.write("Se produce cambio de contexto");
                            bw.newLine();
                            this.enlistarProcesoEjecutando();
                            this.ejecutarProceso();
                            pEjecutando.decrementarTRafagaR();                        
                        }
                        else{
                            pEjecutando.decrementarTRafagaR();
                            System.out.println("Proceso Ejecutando: " + pEjecutando.getNombre());
                            bw.write("Proceso Ejecutando: " + pEjecutando.getNombre());
                            bw.newLine();
                            System.out.println("Tiempo de rafaga restante de " + pEjecutando.getNombre() + ": " + pEjecutando.getTRafagaR());
                            bw.write("Tiempo de rafaga restante de " + pEjecutando.getNombre() + ": " + pEjecutando.getTRafagaR()); 
                            bw.newLine();                        
                        }
                    }
                    else{
                        pEjecutando.decrementarTRafagaR();
                        System.out.println("Proceso Ejecutando: " + pEjecutando.getNombre());
                        bw.write("Proceso Ejecutando: " + pEjecutando.getNombre());
                        bw.newLine();
                        System.out.println("Tiempo de rafaga restante de " + pEjecutando.getNombre() + ": " + pEjecutando.getTRafagaR());
                        bw.write("Tiempo de rafaga restante de " + pEjecutando.getNombre() + ": " + pEjecutando.getTRafagaR());    
                        bw.newLine();                 
                    }
                }
                if(pEjecutando == null){
                    if(!pListos.isEmpty()){
                        System.out.println("Estado de la lista de los procesos listos:");
                        bw.write("Estado de la lista de los procesos listos:");
                        bw.newLine();
                        Iterator<Proceso> it = pListos.iterator();
                        while(it.hasNext()){
                            Proceso p = it.next();
                            System.out.println("      " + p.getNombre());
                            bw.write("      " + p.getNombre());
                            bw.newLine();
                        } 
                        Proceso p = pListos.peek();
                        System.out.println("Proceso " + p.getNombre() + " procede a ser ejecutado");
                        bw.write("Proceso " + p.getNombre() + " procede a ser ejecutado");
                        bw.newLine();
                        if(ultimoProcE.isEmpty() || p.getNombre() == ultimoProcE){
                            this.ejecutarProcesoAnterior();
                            System.out.println("No se produce cambio de contexto");
                            bw.write("No se produce cambio de contexto");
                            bw.newLine();
                        }
                        else{
                            this.ejecutarProceso();
                            System.out.println("Se produce cambio de contexto");
                            bw.write("Se produce cambio de contexto");
                            bw.newLine();
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
            bw.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }      
    }
        

}
