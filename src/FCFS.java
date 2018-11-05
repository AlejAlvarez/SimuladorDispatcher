
import java.io.File;
import java.util.Collections;
import java.util.Comparator;
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
 //First Come First Served
public class FCFS extends Estrategia {

    Comparator<Proceso> hDesblComp = new HDesbloqueoComparator();
    Comparator<Proceso> tIncorpComp = new TIncorporacionComparator();
    
    FCFS(int tip, int tfp, int tcp) {
        super(tip, tfp, tcp);
        pBloqueados = new PriorityQueue(5, hDesblComp);
        pNuevos = new PriorityQueue(5, tIncorpComp);
    }
    
    FCFS() {
        super();
        pBloqueados = new PriorityQueue(5, hDesblComp);
        pNuevos = new PriorityQueue(5, tIncorpComp);
    }

    @Override
    public void ejecutar(File f) {
        contador = 0;
        this.leerArchivo(f);
        while (!((pNuevos.isEmpty())&&(pListos.isEmpty())&&(pBloqueados.isEmpty())&&(pEjecutando==null))){
            System.out.println("Unidad de tiempo actual: " + contador);
            if(!pNuevos.isEmpty()){
                while((!pNuevos.isEmpty()) && (contador >= pNuevos.peek().getTIncorp())){
                    Proceso p = pNuevos.peek();
                    System.out.println("Proceso " + p.getNombre() + " cumplió su TIP y fue enlistado");
                    this.enlistarProcesoNuevo();
                }
            }
            if((pEjecutando==null)){
                if(!(pListos.isEmpty())){
                    Proceso p = pListos.peek();
                    System.out.println("Proceso " + p.getNombre() + " procede a ser ejecutado en el tiempo " + contador);
                    this.ejecutarProceso();
                    pEjecutando.decrementarTRafagaR();
                }
                else{
                    this.cpuDesocupada++;
                }
            }
            else{
                pEjecutando.decrementarTRafagaR();
                cpuProcesos++;
                if(pEjecutando.getTRafagaR()==0){
                    if(pEjecutando.getRafagasRestantes()==0){
                        System.out.println("Proceso " + pEjecutando.getNombre() + " terminó todas sus ráfagas en el momento: " + contador);
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
            }
            if(!(pListos.isEmpty())){
                this.incrementarTListoProcesos();
            }
            if(!pBloqueados.isEmpty()){
                while((!pBloqueados.isEmpty()) && (contador >= pBloqueados.peek().getHDesbloq())){
                    Proceso p = pBloqueados.peek();
                    System.out.println("Proceso " + p.getNombre() + " terminó su ES y fue enlistado");
                    this.enlistarProceso();
                }
            }/*
            if(pListos.isEmpty()){
                System.out.println("LISTA PROCESOS LISTOS VACIA!!!");
            }
            if(pBloqueados.isEmpty()){
                System.out.println("LISTA PROCESOS BLOQUEADOS VACIA!!!");
            }
            if(pNuevos.isEmpty()){
                System.out.println("LISTA PROCESOS NUEVOS VACIA!!!");
            }
            else{
                for (int i = 0; i < pNuevos.size(); i++){
                    Proceso p = pNuevos.peek();
                    System.out.println("PROCESO " + p.getNombre() + " TODAVIA ESTA EN COLA DE NUEVOS CON T DE INCORPORACION: " + p.getTIncorp());
                }
            }
            if(pEjecutando==null){
                System.out.println("NO HAY PROCESO EJECUTANDO!!!");
            }*/
            contador++;
        }
        Collections.sort(pFinalizados, tIncorpComp);
        this.imprimirResultados();
    }

    @Override
    public void imprimirResultados() {
        System.out.println("");
        System.out.println("");
        System.out.println("-----------------------------------");
        System.out.println("");
        System.out.println("INFORMACIÓN DE PROCESOS INDIVIDUALES");
        System.out.println("");
        for (int i = 0; i < pFinalizados.size(); i++){
            Proceso p = pFinalizados.get(i);
            System.out.println("Tiempo de Retorno de " + p.getNombre() +": " + p.getTRetorno());
            System.out.println("Tiempo de Retorno Normalizado de " + p.getNombre() +": " + p.calcularTRetornoNorm());
            System.out.println("Tiempo en Estado de Listo de " + p.getNombre() +": " + p.getTListo());
        }
        System.out.println("");
        System.out.println("-----------------------------------");
        System.out.println("");
        System.out.println("INFORMACIÓN TANDA DE PROCESOS");
        System.out.println("");
        System.out.println("Tiempo de Retorno de la Tanda: " + ultimoTfp);
        System.out.println("Tiempo Medio de Retorno: " + this.calcularTMedioRetorno());
        System.out.println("");
        System.out.println("");
        System.out.println("-----------------------------------");
        System.out.println("");
        System.out.println("INFORMACIÓN USO DEL CPU");
        System.out.println("");
        System.out.println("Tiempo de CPU desocupada: " + cpuDesocupada);
        System.out.println("Tiempo de CPU usada por SO: " + cpuSO);
        System.out.println("Tiempo de CPU usada por los procesos: " + cpuProcesos);
        double porcentaje = ((cpuProcesos / ultimoTfp)*100);
        System.out.println("Porcentaje de uso de los procesos de CPU: " + porcentaje + "%");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("FIN.");
    }
    
}
