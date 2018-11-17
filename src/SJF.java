
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
    //Shortest Job First - Shortest Process Next
public class SJF extends EstrategiaPriorizada {

    public SJF(int tip, int tcp, int tfp) {
        super(tip, tcp, tfp);
        pBloqueados = new PriorityQueue(10, new HDesbloqueoComparator());
        pNuevos = new PriorityQueue(10, new TIncorporacionYCpuComparator());
        pListos = new PriorityQueue(10, new RafagaCpuComparator());
    }

    public SJF() {
        pBloqueados = new PriorityQueue(10, new HDesbloqueoComparator());
        pNuevos = new PriorityQueue(10, new TIncorporacionYCpuComparator());
        pListos = new PriorityQueue(10, new RafagaCpuComparator());
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
                    else{
                        pEjecutando.decrementarTRafagaR();
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

    /*@Override
    public void imprimirResultados() {
        System.out.println("");
        System.out.println("");
        System.out.println("-----------------------------------");
        System.out.println("");
        System.out.println("INFORMACIÓN DE PROCESOS INDIVIDUALES");
        System.out.println("");
        for (int i = 0; i < pFinalizados.size(); i++){
            Proceso p = pFinalizados.get(i);
            System.out.println("PROCESO: " + p.getNombre());
            System.out.println("Tiempo de Retorno de " + p.getNombre() +": " + p.getTRetorno());
            System.out.println("Tiempo de Retorno Normalizado de " + p.getNombre() +": " + p.calcularTRetornoNorm());
            System.out.println("Hora de Finalización de " + p.getNombre() +": " + p.getHFinal());
            System.out.println("Tiempo en Estado de Listo de " + p.getNombre() +": " + p.getTListo());
            System.out.println("-----------------------------------");
        }
        System.out.println("");
        System.out.println("INFORMACIÓN TANDA DE PROCESOS");
        System.out.println("");
        System.out.println("Tiempo de Retorno de la Tanda: " + ultimoTfp);
        System.out.println("Tiempo Medio de Retorno: " + this.calcularTMedioRetorno());
        System.out.println("");
        System.out.println("-----------------------------------");
        System.out.println("");
        System.out.println("INFORMACIÓN USO DEL CPU");
        System.out.println("");
        System.out.println("Tiempo de CPU desocupada: " + cpuDesocupada);
        System.out.println("Tiempo de CPU usada por SO: " + cpuSO);
        System.out.println("Tiempo de CPU usada por los procesos: " + this.calcularCpuProcesos());
        System.out.println("Ultimo TFP: " + this.ultimoTfp);
        System.out.println("Porcentaje de uso de los procesos de CPU: " + this.calcularPorcentajeUsoCPU() + "%");
        System.out.println("");
        System.out.println("");
        System.out.println("FIN.");
    }
    */
}
