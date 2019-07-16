package colas;

import java.util.Random;
import javax.swing.JOptionPane;

public class Colas {
	Nodo nodosPrimarios;
	Nodo nodoInicio;
	Nodo nodosEliminados;
	Nodo nodoEliminado;
	int llegada;
	int numNodosPrimarios;
	int numNodosEliminados;

	public Colas() {
		numNodosPrimarios = 0;
		numNodosEliminados = 0;
		llegada = 0;
	}

	public void procesos() {
		Random rnd = new Random();
		// int num = rnd.nextInt(7)+1;
		int num = 11;
		for (int i = 1; i <= num; i++) {
			if (nodoInicio != null && nodosPrimarios != null) {
				nodoInicio.sig = new Nodo();
				nodoInicio = nodoInicio.sig;
			} else {
				nodoInicio = new Nodo();
				nodosPrimarios = nodoInicio;
			}

			nodoInicio.proceso = numNodosPrimarios + numNodosEliminados;
			nodoInicio.prioridad = rnd.nextInt(3) + 1;
			nodoInicio.tiempoRafaga = rnd.nextInt(20) + 1;

			if (numNodosPrimarios == 0)
				nodoInicio.tiempoLlegada = nodoInicio.proceso * 3;
			else
				nodoInicio.tiempoLlegada = rnd.nextInt(5) + llegada;

			nodoInicio.tiempoComienzo = nodoInicio.tiempoLlegada;
			nodoInicio.tiempoFinalizacion = nodoInicio.tiempoLlegada;

			llegada = nodoInicio.tiempoLlegada;
			numNodosPrimarios++;
		}
	}

	public void imprimir() {
		Nodo nodo = nodosPrimarios;
		while (nodo != null) {
			System.out.println(nodo.proceso + " tiempo de Rafaga: " + nodo.tiempoRafaga + " tiempo de Llegada : "
					+ nodo.tiempoLlegada);
			nodo = nodo.sig;
		}
	}

	public void terminar() {
		if (nodosPrimarios == null) {
			JOptionPane.showMessageDialog(null, "No Hay M�s Procesos En Cola", "Mensaje De Finalizaci�n de Cola",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			if (nodoEliminado != null) {
				nodoEliminado.sig = new Nodo();
				nodoEliminado = nodoEliminado.sig;
			} else {
				nodoEliminado = new Nodo();
				nodosEliminados = nodoEliminado;
			}

			nodoEliminado.proceso = nodosPrimarios.proceso;
			nodoEliminado.tiempoRafaga = nodosPrimarios.tiempoRafaga;
			nodoEliminado.tiempoLlegada = nodosPrimarios.tiempoLlegada;
			numNodosEliminados++;

			nodosPrimarios = nodosPrimarios.sig;
			numNodosPrimarios--;
		}
	}
}
