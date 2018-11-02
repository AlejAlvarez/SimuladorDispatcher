
import java.util.LinkedList;
import java.util.List;
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
public abstract class Estrategia {
    
    protected int tip; //Tiempo para aceptar nuevos procesos
    protected int tfp; //Tiempo para finalizar los procesos
    protected int tcp; //Tiempo de conmutación entre procesos
    protected int cpuDesocupada;
    protected int cpuSO;
    protected int cpuProcesos;
    protected int ultimoTfp;
    protected int cantProcesos;
    protected int contador;
    protected PriorityQueue<Proceso> pBloqueados;
    protected PriorityQueue<Proceso> pNuevos;
    protected List<Proceso> pListos;  //Esta variable se encontrará "Hidden", o escondida, en las clases Prioridad, SJF y SRT
    protected List<Proceso> pFinalizados;
    protected Proceso pEjecutando;
    

    public Estrategia(int tip, int tfp, int tcp) {
        this.tip = tip;
        this.tfp = tfp;
        this.tcp = tcp;
        cpuDesocupada = 0;
        cpuSO = 0;
        cpuProcesos = 0;
        ultimoTfp = 0;
        cantProcesos = 0;
        pFinalizados = new LinkedList<Proceso>();
        pListos = new LinkedList<Proceso>();
        pBloqueados = new PriorityQueue<Proceso>();
        pNuevos = new PriorityQueue<Proceso>();
    }
    
    public Estrategia(){
        tip = 0;
        tfp = 0;
        tcp = 0;
        cpuDesocupada = 0;
        cpuSO = 0;
        cpuProcesos = 0;
        ultimoTfp = 0;
        cantProcesos = 0;
        pFinalizados = new LinkedList<Proceso>();
        pListos = new LinkedList<Proceso>();
        pBloqueados = new PriorityQueue<Proceso>();
        pNuevos = new PriorityQueue<Proceso>();
    }
    
    protected void leerArchivo(){
        //IMPLEMENTAR: LEER SOBRE ARCHIVOS JAVA
        //LEER SOBRE STRINGTOKENIZER
    }
    
    protected void enlistarProceso(Proceso p){
        pListos.add(p);
    }
    
    protected void ejecutarProceso(Proceso p){
        pListos.remove(p);
        pEjecutando = p;
    }
    
    protected void finalizarProceso(){
        pFinalizados.add(pEjecutando);
        pEjecutando = null;
    }
    
    public abstract void ejecutar(/*ACA VA EL ARCHIVO BASE DE PROCESOS*/);
    
    public abstract void imprimirResultados(); //LEER SOBRE ARCHIVOS Y ESCRITURA DE LOS MISMOS
    
    
}
