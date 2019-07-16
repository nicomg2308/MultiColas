/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Vista.Ventana;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import SJF.Cola;
import SJF.Proceso;

/**
 *
 * AUTORES ALEJANDOR MORALES, NICOLÁS MENESES
 */
public class SJF extends Thread {

    public Cola listos;
    public Cola atendidos;

    private Ventana interfaz;

    public SJF(Cola cola, Ventana interfaz) {
        this.interfaz = interfaz;
        listos = new Cola(interfaz.getTxaConsola());
        this.listos = cola;
    }


    public void run() {

        atendidos = new Cola(interfaz.getTxaConsola());

        Proceso enEejecucion;

        listos.imprimir(interfaz.getjTableProcesos());

        while ((listos.colaVacia() == false)) {
            try {
                sleep(250);
                if (listos.colaVacia() == false) {

                    enEejecucion = this.CompararPrioridad(listos.getCab().getSig());
                    if (enEejecucion.getRafagaRestante() > 0) {
                        interfaz.getProcesos_en_ejecucion().setText(enEejecucion.getProceso() + "");
                        int f = enEejecucion.getRafagaRestante();
                        enEejecucion.setTiempoEjecucion(Integer.parseInt(interfaz.getTiempo_real().getText()));
                        listos.imprimir(interfaz.getjTableProcesos());
                        for (int i = 1; i <= f; i++) {
                            sleep(250);
                            interfaz.getTiempo_real().setText((Integer.parseInt(interfaz.getTiempo_real().getText()) + 1) + "");
                            enEejecucion.setTiempoEnCPU(enEejecucion.getTiempoEnCPU() + 1);
                          //  interfaz.getTiempo_cpu().setText(enEejecucion.getTiempoEnCPU() + "");
                            interfaz.getProcesos_en_ejecucion().setText(enEejecucion.getProceso() + "");
                            //interfaz.getCpu_nuevo().setText(enEejecucion.getRafagaRestante() + "");
                            enEejecucion.setRafagaRestante(enEejecucion.getRafagaRestante() - 1);

                            if (this.CompararPrioridad(enEejecucion).getRafagaRestante() < enEejecucion.getRafagaRestante()) {
                                Proceso au = listos.buscarProceso(enEejecucion);
                                au = enEejecucion;
                                break;
                            }

                            for (int j = 1; j <= enEejecucion.getTiempoInicial(); j++) {
                                diagramaGantt(enEejecucion, false, j);
                            }

                            listos.imprimir(interfaz.getjTableProcesos());
                            diagramaGantt(enEejecucion, true, Integer.parseInt(interfaz.getTiempo_real().getText()));

                            if (enEejecucion.getTiempoEnCPU() == enEejecucion.getTiempoRafaga()) {

                                

                               // interfaz.getTiempo_cpu().setText(enEejecucion.getTiempoEnCPU() + "");
                                sleep(1000);
                                atendidos.insertar(enEejecucion.getProceso(), enEejecucion.getTiempoInicial(), enEejecucion.getTiempoRafaga(), enEejecucion.getPrioridad());
                                Proceso aux = listos.buscarProceso(enEejecucion);
                                int tfinal = Integer.parseInt(interfaz.getTiempo_real().getText());
                                aux.setTiempoFinal(tfinal);
                                int tretorno = tfinal - enEejecucion.getTiempoInicial();
                                aux.setTiempoRetorno(tretorno);
                                int tespera = tretorno - enEejecucion.getTiempoRafaga();
                                aux.setTiempoEespera(tespera);

                                listos.imprimir(interfaz.getjTableProcesos());
                                listos.eliminarElemento(enEejecucion, listos);

                                sleep(1000);//2000
                                i = f;
                             
                                interfaz.getProcesos_en_ejecucion().setText("");
                                
                            }

                        }

                    }
                }

            } catch (InterruptedException ex) {
                Logger.getLogger(SJF.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        JOptionPane.showMessageDialog(null, "Ejecución Finalizada");

    }

    public Proceso CompararPrioridad(Proceso entrante) {

        Proceso comparado = entrante;
        Proceso aux_inicial = listos.getCab().getSig();
        while (aux_inicial != null) {
            if (entrante.getPrioridad() > aux_inicial.getPrioridad()) {
                comparado = aux_inicial;
            }
            aux_inicial = aux_inicial.getSig();
        }
        return comparado;
    }

    public void diagramaGantt(Proceso actual, boolean estado, int transcurrido) {//actualiza diagrama de gantt desde la interfaz grafica
        String fase;
        if (estado) {
            fase = "X";
        } else {
            fase = "E";
        }
        interfaz.getjTable1().setValueAt(fase, actual.getProceso() - 1, transcurrido);
        interfaz.getjTable1().setDefaultRenderer(Object.class, new Gantt1());
    }

  

    int conteo_tiempo = 0;//variable usada para graficar el primer diagrama de gantt
    String diagramaGant1 = "";//variable usada para graficar el primer diagrama de gantt
    String diagramaGant2 = "0";//variable usada para graficar el segundo diagrama de gantt
}
