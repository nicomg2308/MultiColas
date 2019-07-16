package colas;

import java.util.ArrayList;
import java.util.Map;

public class RoundRobin {

	public int quantum;
	private int longMax;
	Vista vista;

	public RoundRobin(Vista aThis) {
		quantum = -1;
		longMax = 5;
		this.vista = aThis;
	}

	public void planificar(ArrayList<Map<String, Integer>> listos) {
		// eliminar nodos ejecutados o sin quantum
		if (quantum == 0 && listos.size() > 0) {
			Map<String, Integer> nodo = listos.get(0);

			listos.remove(nodo);
			if (nodo.get("tiempoRafaga") > 0) {
				listos.add(nodo);
				System.out.println("se envia el proceso " + nodo.get("proceso") + " al final de la cola");
			} else {
				System.out.println("se elimina el proceso " + nodo.get("proceso") + " de la cola");

			}

			for (Map<String, Integer> nod : listos) {
				System.out.print(nod.get("proceso") + " - ");
			}
			System.out.println("");
		}

		// ordenar nodos en RoundRobin
		if (quantum <= 0 && listos.size() > 0) {
			Map<String, Integer> nodo = listos.get(0);
			System.out.println("\nel quantum lo tiene el proceso " + nodo.get("proceso"));

			if (nodo.get("tiempoRafaga") < longMax) {
				quantum = nodo.get("tiempoRafaga");
			} else {
				quantum = longMax;
			}

			System.out.println("quantum: " + quantum);
		}

		quantum--;
	}

}
