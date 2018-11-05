
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
    protected static PriorityQueue<Proceso> pBloqueados;
    protected static PriorityQueue<Proceso> pNuevos;
    protected static LinkedList<Proceso> pListos;  //Esta variable se encontrará "Hidden", o escondida, en las clases Prioridad, SJF y SRT
    protected static LinkedList<Proceso> pFinalizados;
    protected static Proceso pEjecutando;
    

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
                p.setRafagaytcp(tcp);
                pNuevos.add(p);
                cantProcesos++;
            }
            System.out.println("Lectura del archivo completada con Éxito!");
            cpuSO += tip*cantProcesos + tfp*cantProcesos;
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    
    protected void enlistarProceso(Proceso p){
        pListos.add(p);
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
        pEjecutando.nuevaRafaga();
        pEjecutando.disminuirRafagasRestantes();
    }
    
    protected void finalizarProceso(){
        pEjecutando.setTRetorno(contador + tfp);
        pEjecutando.sumarTListo(tfp);
        pFinalizados.add(pEjecutando);
        pEjecutando = null;
    }
    
    protected void bloquearProceso(){
        pEjecutando.setHDesbloq(contador);
        pBloqueados.add(pEjecutando);
        pEjecutando = null;
    }
    
    protected void incrementarTListoProcesos(){
        for (int i = 0; i < pListos.size(); i++){
            Proceso p = pListos.get(i);
            p.incrementarTListo();
        }
    }
    
    protected double calcularTMedioRetorno(){
        int suma = 0;
        for (int i = 0; i < pFinalizados.size(); i++){
            Proceso p = pFinalizados.get(i);
            suma += p.getHFinal();
        }
        return suma / cantProcesos;
    }
    
    public abstract void ejecutar(File f);
    
    public abstract void imprimirResultados(); //LEER SOBRE ARCHIVOS Y ESCRITURA DE LOS MISMOS
    
    
}
