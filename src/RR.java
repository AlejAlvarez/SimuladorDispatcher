
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alejandro
 */
    //Round-Robin
public class RR extends EstrategiaPreemptiva {

    private int quantum;
    private int contadorQ;
    
    public RR(int tip, int tcp, int tfp, int quantum) {
        super(tip, tcp, tfp);
        this.quantum = quantum;
        pNuevos = new PriorityQueue(10, new TIncorporacionComparator());
        pBloqueados = new PriorityQueue(10, new HDesbloqueoComparator());
        pListos = new LinkedList();
    }

    public RR() {
        pNuevos = new PriorityQueue(10, new TIncorporacionComparator());
        pBloqueados = new PriorityQueue(10, new HDesbloqueoComparator());
        pListos = new LinkedList();
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
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
        contadorQ = 0;
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
                    while((!pNuevos.isEmpty()) && (contador >= pNuevos.peek().getTIncorp())){  //ESTO ESTA CORRECTO.
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
                        this.enlistarProceso();
                    }
                }
                if((pEjecutando!=null)){
                    //System.out.println("Tiempo de rafaga restante de " + pEjecutando.getNombre() + ": " + pEjecutando.getTRafagaR());
                    if(pEjecutando.getTRafagaR()==0){
                        if(pEjecutando.getRafagasRestantes()==0){
                            System.out.println("Proceso " + pEjecutando.getNombre() + " terminó todas sus ráfagas");
                            bw.write("Proceso " + pEjecutando.getNombre() + " terminó todas sus ráfagas");
                            bw.newLine();
                            this.finalizarProceso();
                            contadorQ = 0;
                            if(pNuevos.isEmpty() && pListos.isEmpty() && pBloqueados.isEmpty()){
                                this.ultimoTfp = contador + tfp;
                            }
                        }
                        else{
                            System.out.println("Proceso " + pEjecutando.getNombre() + " fue bloqueado durante " + pEjecutando.getDuracionES() + " unidades de tiempo.");
                            bw.write("Proceso " + pEjecutando.getNombre() + " fue bloqueado durante " + pEjecutando.getDuracionES() + " unidades de tiempo.");                            
                            bw.newLine();
                            this.bloquearProceso();
                            contadorQ = 0;
                        }
                    }
                    else if(contadorQ == quantum){
                        System.out.println("Proceso " + pEjecutando.getNombre() + " completo su tiempo de quantum y es enlistado");
                        bw.write("Proceso " + pEjecutando.getNombre() + " completo su tiempo de quantum y es enlistado");
                        bw.newLine();
                        this.enlistarProcesoEjecutando();
                        contadorQ = 0;
                    }
                    else{
                        pEjecutando.decrementarTRafagaR();
                        contadorQ++;
                        System.out.println("Proceso Ejecutando: " + pEjecutando.getNombre());
                        bw.write("Proceso Ejecutando: " + pEjecutando.getNombre()); 
                        bw.newLine();
                        System.out.println("Tiempo de rafaga restante de " + pEjecutando.getNombre() + ": " + pEjecutando.getTRafagaR());
                        bw.write("Tiempo de rafaga restante de " + pEjecutando.getNombre() + ": " + pEjecutando.getTRafagaR()); 
                        bw.newLine();
                        System.out.println("Tiempo de quantum usado de " + pEjecutando.getNombre() + ": " + contadorQ);
                        bw.write("Tiempo de quantum usado de " + pEjecutando.getNombre() + ": " + contadorQ); 
                        bw.newLine();
                        //System.out.println("RAFAGAS RESTANTES DE " + pEjecutando.getNombre() + " = " + pEjecutando.getTRafagaR());
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
                            contadorQ = contadorQ - tcp;
                        }
                        pEjecutando.decrementarTRafagaR();  //SE CARGA A EJECUTAR Y EJECUTA UNA UNIDAD DE TIEMPO DE LA RAFAGA
                        contadorQ++;
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
