
package SJF;

/**
 *
 * AUTORES ALEJANDRO MORALES, NICOLÁS MENESES
 */
public class Proceso {
    
    private int proceso;
    private int tiempoCmienzoEjecucion;
    private int tiempoInicial;
    private int tiempoRafaga;
    private int tiempoFinal;
    private int tiempoRetorno;
    private int tiempoEespera;
    private int rafagaRestante;
    private int tiempoEnCPU;
    
    private int prioridad;
    private Proceso sig;

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    
   

    public int getTiempoEnCPU() {
        return tiempoEnCPU;
    }

    public void setTiempoEnCPU(int tiempoEnCPU) {
        this.tiempoEnCPU = tiempoEnCPU;
    }

    
    
    public int getRafagaRestante() {
        return rafagaRestante;
    }

    public void setRafagaRestante(int rafagaRestante) {
        this.rafagaRestante = rafagaRestante;
    }
    
    
    
    public int getEjecucion() {
        return tiempoCmienzoEjecucion;
    }

    public void setEjecucion(int ejecucion) {
        this.tiempoCmienzoEjecucion = ejecucion;
    }
    
    public int getProceso() {
        return proceso;
    }

    public void setProceso(int proceso) {
        this.proceso = proceso;
    }

    public int getTiempoInicial() {
        return tiempoInicial;
    }

    public void setTiempoInicial(int tiempoInicial) {
        this.tiempoInicial = tiempoInicial;
    }

    public int getTiempoRafaga() {
        return tiempoRafaga;
    }

    public void setTiempoRafaga(int tiempoRafaga) {
        this.tiempoRafaga = tiempoRafaga;
    }

    public Proceso getSig() {
        return sig;
    }

    public void setSig(Proceso sig) {
        this.sig = sig;
    }

    public int getTiempoEjecucion() {
        return tiempoCmienzoEjecucion;
    }

    public void setTiempoEjecucion(int tiempoEjecucion) {
        this.tiempoCmienzoEjecucion = tiempoEjecucion;
    }

    public int getTiempoFinal() {
        return tiempoFinal;
    }

    public void setTiempoFinal(int tiempoFinal) {
        this.tiempoFinal = tiempoFinal;
    }

    public int getTiempoRetorno() {
        return tiempoRetorno;
    }

    public void setTiempoRetorno(int tiempoRetorno) {
        this.tiempoRetorno = tiempoRetorno;
    }

    public int getTiempoEespera() {
        return tiempoEespera;
    }

    public void setTiempoEespera(int tiempoEespera) {
        this.tiempoEespera = tiempoEespera;
    }
    
    
}
