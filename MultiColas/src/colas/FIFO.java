package colas;

import java.util.ArrayList;
import java.util.Map;

public class FIFO {

	Vista vista;

	public FIFO(Vista aThis) {
		vista = aThis;
	}

	public void planificar(ArrayList<Map<String, Integer>> listos) {
		Map<String, Integer> nodo = listos.get(0);
		if (nodo.get("tiempoRafaga") == 0) {
			listos.remove(nodo);
			System.out.println("se elimina el proceso " + nodo.get("proceso") + " de la cola");
		}
	}

}
