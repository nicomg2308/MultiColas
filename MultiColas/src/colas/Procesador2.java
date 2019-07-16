package colas;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Procesador2 extends Canvas {

	private Vista vista;
	private int tiempo;
	private RoundRobin planificadorA;
	private SJFExpulsivo planificadorB;
	private FIFO planificadorC;
	private ArrayList<Map<String, Integer>> bloqueados;
	private ArrayList<Map<String, Integer>> TablaBloqueos;
	private ArrayList<Map<String, Integer>> A;
	private ArrayList<Map<String, Integer>> B;
	private ArrayList<Map<String, Integer>> C;
	private ArrayList<Map<String, Integer>> cola;

	public Procesador2(Nodo nodos, Vista aThis) {
		vista = aThis;
		this.tiempo = 0;
		this.bloqueados = new ArrayList<Map<String, Integer>>();
		this.TablaBloqueos = new ArrayList<Map<String, Integer>>();
		this.A = new ArrayList<Map<String, Integer>>();
		this.B = new ArrayList<Map<String, Integer>>();
		this.C = new ArrayList<Map<String, Integer>>();
		this.cola = new ArrayList<Map<String, Integer>>();
		this.planificadorA = new RoundRobin(vista);
		this.planificadorB = new SJFExpulsivo(vista);
		this.planificadorC = new FIFO(vista);
		asignar(nodos);
	}

	public void asignar(Nodo nodos) {
		while (nodos != null) {
			Map<String, Integer> nodo = new HashMap<String, Integer>();
			nodo.put("proceso", nodos.proceso);
			nodo.put("tiempoRafaga", nodos.tiempoRafaga);
			nodo.put("tiempoLlegada", nodos.tiempoLlegada);
			nodo.put("tiempoEspera", nodos.tiempoEspera);
			nodo.put("tiempoComienzo", nodos.tiempoComienzo);
			nodo.put("tiempoRetorno", nodos.tiempoRetorno);
			nodo.put("tiempoFinalizacion", nodos.tiempoFinalizacion);
			nodo.put("prioridad", nodos.prioridad);
			cola.add(nodo);
			System.out.println("nodo asignado " + nodo.get("proceso"));
			nodos = nodos.sig;
		}
	}

	public void crearTablaBloqueos() {
		Object[][] datos = new Object[TablaBloqueos.size()][3];
		for (int i = 0; i < TablaBloqueos.size(); i++) {
			datos[i][0] = TablaBloqueos.get(i).get("proceso");
			datos[i][1] = TablaBloqueos.get(i).get("tEjecucion");
			datos[i][2] = TablaBloqueos.get(i).get("tBloqueado");
		}
		vista.crearBloqueados(datos);
	}

	public void paint(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getMaximumSize().width, getMaximumSize().height);
		pintar(g);
		crearTablaBloqueos();
	}

	public void pintar(Graphics g) {
		int aumento = 5;
		int y = 20;

		while (6 * tiempo + 36 < getSize().width) {
			for (Map<String, Integer> nodo : cola) {
				if (nodo.get("tiempoLlegada") == tiempo) {
					System.out.print("proceso " + nodo.get("proceso"));
					switch (nodo.get("prioridad")) {
					case 1:
						A.add(nodo);
						System.out.println(" agregado a la cola A");
						break;
					case 2:
						B.add(nodo);
						System.out.println(" agregado a la cola B");
						break;
					case 3:
						C.add(nodo);
						System.out.println(" agregado a la cola C");
						break;
					}
				}
			}

			analizar();

			if (tiempo % 5 == 0) {
				g.setColor(Color.black);
				g.drawString(aumento * tiempo / 5 + "", 6 * tiempo + 36, 10);
				g.setColor(Color.black);
				g.drawLine(6 * tiempo + 36, 15, 6 * tiempo + 36, getSize().height - 10);
			}

			for (Map<String, Integer> nodo : A) {
				int i = nodo.get("proceso");
				g.setColor(Color.BLACK);
				g.drawString(nodo.get("proceso") + "", 5, y * i + 35);
				if (nodo.get("tiempoEspera") > 0) {
					g.setColor(Color.GRAY);
					g.fillRect(6 * tiempo + 36, y * i + 25, 6, 6);
					nodo.put("tiempoEspera", nodo.get("tiempoEspera") - 1);
				} else if (nodo.get("tiempoRafaga") > 0) {
					g.setColor(Color.GREEN);
					g.fillRect(6 * tiempo + 36, y * i + 21, 6, 12);
					nodo.put("tiempoRafaga", nodo.get("tiempoRafaga") - 1);
				}
			}

			for (Map<String, Integer> nodo : B) {
				int i = nodo.get("proceso");
				g.setColor(Color.BLACK);
				g.drawString(nodo.get("proceso") + "", 5, y * i + 35);
				if (nodo.get("tiempoEspera") > 0) {
					g.setColor(Color.GRAY);
					g.fillRect(6 * tiempo + 36, y * i + 25, 6, 6);
					nodo.put("tiempoEspera", nodo.get("tiempoEspera") - 1);
				} else if (nodo.get("tiempoRafaga") > 0) {
					g.setColor(Color.YELLOW);
					g.fillRect(6 * tiempo + 36, y * i + 21, 6, 12);
					nodo.put("tiempoRafaga", nodo.get("tiempoRafaga") - 1);
				}
			}

			for (Map<String, Integer> nodo : C) {
				int i = nodo.get("proceso");
				g.setColor(Color.BLACK);
				g.drawString(nodo.get("proceso") + "", 5, y * i + 35);
				if (nodo.get("tiempoEspera") > 0) {
					g.setColor(Color.GRAY);
					g.fillRect(6 * tiempo + 36, y * i + 25, 6, 6);
					nodo.put("tiempoEspera", nodo.get("tiempoEspera") - 1);
				} else if (nodo.get("tiempoRafaga") > 0) {
					g.setColor(Color.BLUE);
					g.fillRect(6 * tiempo + 36, y * i + 21, 6, 12);
					nodo.put("tiempoRafaga", nodo.get("tiempoRafaga") - 1);
				}
			}

			for (Map<String, Integer> nodo : bloqueados) {
				int i = nodo.get("proceso");
				if (nodo.get("tiempoBloqueado") > 0) {
					g.setColor(Color.RED);
					g.fillRect(6 * tiempo + 36, y * i + 25, 6, 6);
					nodo.put("tiempoBloqueado", nodo.get("tiempoBloqueado") - 1);
				}
			}

			tiempo++;

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void analizar() {
		desbloquear();
		if (A.size() > 0) {
			System.out.println("se planifica RoundRobin");
			// bloquear(A);
			if (A.size() > 0) {
				planificadorA.planificar(A);
				if (A.size() > 0) {
					aplazar(A, 1);
					aplazar(B, 0);
					aplazar(C, 0);
				} else {
					analizar();
				}
			} else {
				analizar();
			}
		} else if (B.size() > 0) {
			System.out.println("se planifica SJFExpulsivo");
			// bloquear(B);
			if (B.size() > 0) {
				planificadorB.planificar(B);
				if (B.size() > 0) {
					aplazar(B, 1);
					aplazar(C, 0);
				} else {
					analizar();
				}
			} else {
				analizar();
			}
		} else if (C.size() > 0) {
			System.out.println("se planifica FIFO");
			// bloquear(C);
			if (C.size() > 0) {
				planificadorC.planificar(C);
				aplazar(C, 1);
			} else {
				analizar();
			}
		}
	}

	public void desbloquear() {
		if (bloqueados.size() > 0) {
			for (Map<String, Integer> nodo : bloqueados) {
				if (nodo.get("tiempoBloqueado") == 0) {
					if (nodo.get("prioridad") == 1) {
						A.add(nodo);
					} else if (nodo.get("prioridad") == 2) {
						B.add(nodo);
					} else if (nodo.get("prioridad") == 3) {
						C.add(nodo);
					}
					bloqueados.remove(nodo);
					System.out.println("el proceso " + nodo.get("proceso") + " vuelve a la cola en " + tiempo);
					break;
				}
			}
		}
	}

	public void bloquear(ArrayList<Map<String, Integer>> listos) {
		Map<String, Integer> nodo = listos.get(0);
		if (nodo.get("tiempoRafaga") > 0) {
			Random rnd = new Random();
			int probabilidad = rnd.nextInt(5);
			if (probabilidad == 1) {
				// int tiempoBloq = rnd.nextInt(5)+1;
				int tiempoBloqueado = 4;
				nodo.put("tiempoBloqueado", tiempoBloqueado);
				System.out.println("\nse bloquea el proceso " + nodo.get("proceso") + " por " + tiempoBloqueado
						+ " en el tiempo " + tiempo);

				bloqueados.add(nodo);
				listos.remove(nodo);

				if (nodo.get("prioridad") == 1) {
					planificadorA.quantum = -1;
				} else if (nodo.get("prioridad") == 2) {
					planificadorB.ordenar = true;
				}

				nodo.put("tiempoFinalizacion", tiempo);
				vista.tabla.setValueAt(
						vista.tabla.getValueAt(nodo.get("proceso"), 4) + "" + nodo.get("tiempoFinalizacion") + ", ",
						nodo.get("proceso"), 4);

				Map<String, Integer> bloq = new HashMap<String, Integer>();
				bloq.put("proceso", nodo.get("proceso"));
				bloq.put("tEjecucion", tiempo);
				bloq.put("tBloqueado", tiempoBloqueado);
				TablaBloqueos.add(bloq);
			}
		}
	}

	public void aplazar(ArrayList<Map<String, Integer>> listos, int n) {
		for (int i = n; i < listos.size(); i++) {
			listos.get(i).put("tiempoEspera", 1);
			System.out.println(
					"el nodo " + listos.get(i).get("proceso") + " se aplaza " + listos.get(i).get("tiempoEspera"));
		}
	}
}
