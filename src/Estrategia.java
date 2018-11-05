
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
    
    public abstract void ejecutar(File f);
    
    public abstract void imprimirResultados(); //LEER SOBRE ARCHIVOS Y ESCRITURA DE LOS MISMOS
    
    
}
