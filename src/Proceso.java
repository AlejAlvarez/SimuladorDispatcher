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

    public Proceso(String nombre, int tArribo, int rafagasTotales, int duracionRafaga, int duracionES, int prioridad) {
        this.nombre = nombre;
        this.tArribo = tArribo;
        this.rafagasTotales = rafagasTotales;
        this.duracionRafaga = duracionRafaga;
        this.duracionES = duracionES;
        this.prioridad = prioridad;
        tServicio = rafagasTotales * duracionRafaga;
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
        this.tRetorno = tRetorno - tArribo;
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
    
    
}
