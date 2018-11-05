/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alejandro
 */
public class Proceso {
    
    private String nombre;
    private int tArribo;
    private int rafagasTotales;
    private int rafagasRestantes;
    private int duracionRafaga;
    private int duracionES;
    private int prioridad;
    private int tRetorno;
    private int tRetornoNorm;
    private int tListo;
    private int tServicio;
    private int tIncorp;
    private int tRafagaR;
    private int hDesbloq;
    private int hFinal;
    

    public Proceso(String nombre, int tArribo, int rafagasTotales, int duracionRafaga, int duracionES, int prioridad) {
        this.nombre = nombre;
        this.tArribo = tArribo;
        this.rafagasTotales = rafagasTotales;
        rafagasRestantes = rafagasTotales;
        this.duracionRafaga = duracionRafaga;
        this.duracionES = duracionES;
        this.prioridad = prioridad;
        tServicio = rafagasTotales * duracionRafaga;
    }

    public int getTIncorp() {
        return tIncorp;
    }

    public void setTIncorp(int tip) {
        tIncorp = tArribo + tip;
    }

    public int getHFinal() {
        return hFinal;
    }

    public void setHFinal(int hFinal) {
        this.hFinal = hFinal;
    }

    public int getDuracionES() {
        return duracionES;
    }

    public String getNombre() {
        return nombre;
    }
    
    public int getRafagasRestantes() {
        return rafagasRestantes;
    }
    
    public int getTArribo(){
        return tArribo;
    }

    public int getTRetorno() {
        return tRetorno;
    }

    public void setTRetorno(int tRetorno) {
        hFinal = tRetorno;
        this.tRetorno = tRetorno - tIncorp;
    }
    
    public double calcularTRetornoNorm() {
        tRetornoNorm = tRetorno / tServicio;
        return tRetornoNorm;
    }

    public int getTListo() {
        return tListo;
    }
    
    public void disminuirRafagasRestantes(){
        this.rafagasRestantes--;
    }
    
    public void incrementarTListo(){
        this.tListo++;
    }
    
    public void decrementarTRafagaR(){
        this.tRafagaR--;
    }
    
    public int getTRafagaR(){
        return tRafagaR;
    }
    
    public void nuevaRafaga(){
        tRafagaR = duracionRafaga;
    }
    
    public void nuevaRafaga(int tcp){
        tRafagaR = duracionRafaga + tcp;
    }

    public int getHDesbloq() {
        return hDesbloq;
    }

    public void setHDesbloq(int contador) {
        this.hDesbloq = this.duracionES + contador;
    }
    
    public void sumarTListoTfp(int tfp){
        tListo += tfp;
    }

    
    
}
