
import java.io.File;
import java.util.Collections;
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
      
    
    @Override
    public void ejecutar(File f) {
        contador = 0;
        this.leerArchivo(f);
        while (!((pNuevos.isEmpty())&&(pListos.isEmpty())&&(pBloqueados.isEmpty())&&(pEjecutando==null))){
            System.out.println("Unidad de tiempo actual: " + contador);
            if(!pNuevos.isEmpty()){
                while((!pNuevos.isEmpty()) && (contador >= pNuevos.peek().getTIncorp())){  //ESTO ESTA CORRECTO.
                    Proceso p = pNuevos.peek();
                    System.out.println("Proceso " + p.getNombre() + " cumplió su TIP y fue enlistado");
                    this.enlistarProcesoNuevo();
                }
            }
            if(!pBloqueados.isEmpty()){
                while((!pBloqueados.isEmpty()) && (contador >= pBloqueados.peek().getHDesbloq())){
                    Proceso p = pBloqueados.peek();
                    System.out.println("Proceso " + p.getNombre() + " terminó su ES y fue enlistado");
                    this.enlistarProceso();
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
                    Proceso p = pListos.peek();
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
