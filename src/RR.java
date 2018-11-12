
import java.io.File;
import java.util.Collections;
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
        
    @Override
    public void ejecutar(File f) {
        contador = 0;
        contadorQ = 0;
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
                //System.out.println("Tiempo de rafaga restante de " + pEjecutando.getNombre() + ": " + pEjecutando.getTRafagaR());
                if(pEjecutando.getTRafagaR()==0){
                    if(pEjecutando.getRafagasRestantes()==0){
                        System.out.println("Proceso " + pEjecutando.getNombre() + " terminó todas sus ráfagas");
                        this.finalizarProceso();
                        contadorQ = 0;
                        if(pNuevos.isEmpty() && pListos.isEmpty() && pBloqueados.isEmpty()){
                            this.ultimoTfp = contador + tfp;
                        }
                    }
                    else{
                        System.out.println("Proceso " + pEjecutando.getNombre() + " fue bloqueado durante " + pEjecutando.getDuracionES() + " unidades de tiempo.");
                        this.bloquearProceso();
                        contadorQ = 0;
                    }
                }
                else if(contadorQ == quantum){
                    System.out.println("Proceso " + pEjecutando.getNombre() + " completo su tiempo de quantum y es enlistado");
                    this.enlistarProcesoEjecutando();
                    contadorQ = 0;
                }
                else{
                    pEjecutando.decrementarTRafagaR();
                    contadorQ++;
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
    }
    
}
