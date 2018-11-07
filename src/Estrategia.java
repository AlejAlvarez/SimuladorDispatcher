
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alejandro
 */
public abstract class Estrategia {
    
    protected static int tip; //Tiempo para aceptar nuevos procesos
    protected static int tfp; //Tiempo para finalizar los procesos
    protected static int tcp; //Tiempo de conmutación entre procesos
    protected static int cpuDesocupada;
    protected static int cpuSO;
    protected static int cpuProcesos;
    protected static int ultimoTfp;
    protected static int cantProcesos;
    protected static int contador;
    //protected static int proximaEjec;
    protected static PriorityQueue<Proceso> pBloqueados;
    protected static PriorityQueue<Proceso> pNuevos;
    protected static LinkedList<Proceso> pListos;  //Esta variable se encontrará "Hidden", o escondida, en las clases Prioridad, SJF y SRT
    protected static LinkedList<Proceso> pFinalizados;
    protected static Proceso pEjecutando;
    protected static String ultimoProcE;
    

    public Estrategia(int tip, int tcp, int tfp) {
        this.tip = tip;
        this.tcp = tcp;
        this.tfp = tfp;
        //proximaEjec = 0;
        cpuDesocupada = 0;
        cpuSO = 0;
        cpuProcesos = 0;
        ultimoTfp = 0;
        ultimoProcE = new String();
        cantProcesos = 0;
        pFinalizados = new LinkedList<Proceso>();
        pListos = new LinkedList<Proceso>();
        pEjecutando = null;
    }
    
    public Estrategia(){
        tip = 0;
        tcp = 0;
        tfp = 0;
        cpuDesocupada = 0;
        cpuSO = 0;
        cpuProcesos = 0;
        ultimoTfp = 0;
        ultimoProcE = new String();
        cantProcesos = 0;
        pFinalizados = new LinkedList<Proceso>();
        pListos = new LinkedList<Proceso>();
        pEjecutando = null;
    }
    
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
    
    protected void enlistarProceso(){
        pListos.add(pBloqueados.poll());
    }
    
    protected void enlistarProcesoNuevo(){
        pListos.add(pNuevos.poll());
    }
    
    protected void ejecutarProceso(){
        cpuSO += tcp;
        pEjecutando = pListos.poll();
        ultimoProcE = pEjecutando.getNombre();
        pEjecutando.nuevaRafaga(tcp);
        pEjecutando.disminuirRafagasRestantes();
    }
    
    protected void finalizarProceso(){
        pEjecutando.setTRetorno(contador + tfp);
        ultimoProcE = pEjecutando.getNombre();
        pEjecutando.sumarTListoTfp(tfp);
        pFinalizados.add(pEjecutando);
        pEjecutando = null;
    }
    
    protected void bloquearProceso(){
        pEjecutando.setHDesbloq(contador);
        ultimoProcE = pEjecutando.getNombre();
        pBloqueados.add(pEjecutando);
        pEjecutando = null;
    }
    
    protected void incrementarTListoProcesos(){
        for (int i = 0; i < pListos.size(); i++){
            Proceso p = pListos.get(i);
            p.incrementarTListo();
        }
    }
    
    protected void ejecutarProcesoAnterior(){
        pEjecutando = pListos.poll();
        ultimoProcE = pEjecutando.getNombre();
        pEjecutando.nuevaRafaga();
        pEjecutando.disminuirRafagasRestantes();       
    }
    
    protected double calcularTMedioRetorno(){
        int suma = 0;
        for (int i = 0; i < pFinalizados.size(); i++){
            Proceso p = pFinalizados.get(i);
            suma += p.getHFinal();
        }
        return suma / cantProcesos;
    }
    
    protected double calcularPorcentajeUsoCPU(){
        return ((cpuProcesos / ultimoTfp)*100);
    }
    
    protected int calcularCpuProcesos(){
        Iterator<Proceso> iterator = pFinalizados.iterator();
        while(iterator.hasNext()){
            Proceso p = iterator.next();
            cpuProcesos += p.getDuracionRafaga() * p.getRafagasTotales();
        }
        return cpuProcesos;
    }
    
    public abstract void ejecutar(File f);
    
    public void imprimirResultados(){
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
    
    
    
}
