package colas;

import java.util.ArrayList;
import java.util.Map;

public class SJFExpulsivo {

	public boolean ordenar;
	Vista vista;

	public SJFExpulsivo(Vista aThis) {
		ordenar = true;
		this.vista = aThis;
	}

	public void planificar(ArrayList<Map<String, Integer>> listos) {
		// eliminar nodos ejecutados
		Map<String, Integer> nodo = listos.get(0);
		if (nodo.get("tiempoRafaga") == 0) {
			listos.remove(nodo);
			System.out.println("se elimina el proceso " + nodo.get("proceso") + " de la cola");
			ordenar = true;
		}

		// ordenar nodos en SJF Expulsivo
		if (ordenar && listos.size() > 0) {
			for (int i = 0; i < listos.size(); i++) {
				for (int j = 0; j < listos.size() - i - 1; j++) {
					if (listos.get(j).get("tiempoRafaga") > listos.get(j + 1).get("tiempoRafaga")) {
						Map<String, Integer> aux = listos.get(j);
						listos.set(j, listos.get(j + 1));
						listos.set(j + 1, aux);
					}
				}
			}

			ordenar = false;
		}

		// mostrar proximo orden en los nodos
		for (Map<String, Integer> nod : listos) {
			System.out.print(nod.get("proceso") + " - ");
		}
		System.out.println("");
	}
}
