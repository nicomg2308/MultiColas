package colas;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JOptionPane;

public class Procesador extends Canvas {
	private Graphics gra;
	private Vista vista;
	private int tiempo;
	private ArrayList<Map<String, Integer>> bloqueados;
	private ArrayList<Map<String, Integer>> TablaBloqueos;
	private ArrayList<Map<String, Integer>> A;
	private ArrayList<Map<String, Integer>> B;
	private ArrayList<Map<String, Integer>> C;
	private ArrayList<Map<String, Integer>> cola;

	private int quantum;
	private int longMax;
	private boolean ordenar;
	private boolean semaforo;

	public Procesador(Nodo nodos, Vista aThis) {
		vista = aThis;
		this.tiempo = 0;
		this.quantum = -1;
		this.longMax = 5;
		this.ordenar = true;
		this.bloqueados = new ArrayList<Map<String, Integer>>();
		this.TablaBloqueos = new ArrayList<Map<String, Integer>>();
		this.A = new ArrayList<Map<String, Integer>>();
		this.B = new ArrayList<Map<String, Integer>>();
		this.C = new ArrayList<Map<String, Integer>>();
		this.cola = new ArrayList<Map<String, Integer>>();
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
			nodo.put("tiempoBloqueado", 0);
			nodo.put("prioridad", nodos.prioridad);
			cola.add(nodo);
			System.out.println("nodo asignado " + nodo.get("proceso"));
			nodos = nodos.sig;
		}
	}

	public void clonar(Map<String, Integer> nodo, ArrayList<Map<String, Integer>> proxima) {
		Map<String, Integer> clon = new HashMap<String, Integer>();
		clon.put("proceso", nodo.get("proceso"));
		clon.put("tiempoRafaga", nodo.get("tiempoRafaga"));
		clon.put("tiempoLlegada", nodo.get("tiempoLlegada"));
		clon.put("tiempoEspera", nodo.get("tiempoEspera"));
		clon.put("tiempoComienzo", nodo.get("tiempoComienzo"));
		clon.put("tiempoRetorno", nodo.get("tiempoRetorno"));
		clon.put("tiempoFinalizacion", nodo.get("tiempoFinalizacion"));
		clon.put("tiempoBloqueado", 0);
		clon.put("prioridad", nodo.get("prioridad"));
		proxima.add(clon);
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
		gra = g;
		g.setColor(Color.white);
		g.fillRect(0, 0, getMaximumSize().width, getMaximumSize().height);
		pintar(g);
		crearTablaBloqueos();
	}

	public void pintarSemaforo(boolean semaforo) {
		Graphics g = gra;
		int x = getSize().width - 45;
		g.setColor(Color.black);
		g.fillRect(x, 15, 40, 85);
		if (semaforo) {
			g.setColor(Color.green);
			g.fillOval(x + 3, 18, 34, 34);
		} else {
			g.setColor(Color.red);
			g.fillOval(x + 3, 60, 34, 34);
		}
	}

	public void pintarCambio() {
		Graphics g = gra;
		int x = getSize().width - 45;
		g.setColor(Color.black);
		g.fillRect(x, 15, 40, 85);
		g.setColor(Color.green);
		g.fillOval(x + 3, 18, 34, 34);
		try {
			Thread.sleep(80);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		g.setColor(Color.black);
		g.fillRect(x, 15, 40, 85);
		g.setColor(Color.red);
		g.fillOval(x + 3, 60, 34, 34);

	}

	public void pintar(Graphics g) {
		int aumento = 5;
		int y = 20;

		while (6 * tiempo + 36 < getSize().width - 50) {
			for (Map<String, Integer> nodo : cola) {
				if (nodo.get("tiempoLlegada") == tiempo) {
					System.out.print("proceso " + nodo.get("proceso"));
					switch (nodo.get("prioridad")) {
					case 1:
						clonar(nodo, A);
						// A.add(nodo);
						System.out.println(" agregado a la cola A");
						break;
					case 2:
						clonar(nodo, B);
						// B.add(nodo);
						System.out.println(" agregado a la cola B");
						break;
					case 3:
						clonar(nodo, C);
						// C.add(nodo);
						System.out.println(" agregado a la cola C");
						break;
					}
				}
			}

			analizar();
			semaforo = true;

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
					semaforo = false;
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
					semaforo = false;
					g.setColor(Color.GRAY);
					g.fillRect(6 * tiempo + 36, y * i + 25, 6, 6);
					nodo.put("tiempoEspera", nodo.get("tiempoEspera") - 1);
					int espera = cola.get(nodo.get("proceso")).get("tiempoEspera");
					if (espera == 0 && cola.get(nodo.get("proceso")).get("tiempoBloqueado") == 0) {
						pintarCambio();
						cola.get(nodo.get("proceso")).put("tiempoFinalizacion", tiempo);
						vista.tabla.setValueAt(
								vista.tabla.getValueAt(nodo.get("proceso"), 5) + ""
										+ cola.get(nodo.get("proceso")).get("tiempoFinalizacion") + ", ",
								nodo.get("proceso"), 5);
					}
					cola.get(nodo.get("proceso")).put("tiempoBloqueado", 0);
					cola.get(nodo.get("proceso")).put("tiempoEspera", espera + 1);
				} else if (nodo.get("tiempoRafaga") > 0) {
					g.setColor(Color.YELLOW);
					g.fillRect(6 * tiempo + 36, y * i + 21, 6, 12);
					nodo.put("tiempoRafaga", nodo.get("tiempoRafaga") - 1);
					if (cola.get(nodo.get("proceso")).get("tiempoEspera") > 0
							|| cola.get(nodo.get("proceso")).get("tiempoBloqueado") > 0) {
						cola.get(nodo.get("proceso")).put("tiempoComienzo", tiempo);
						vista.tabla.setValueAt(
								vista.tabla.getValueAt(nodo.get("proceso"), 4) + ""
										+ cola.get(nodo.get("proceso")).get("tiempoComienzo") + ", ",
								nodo.get("proceso"), 4);
						vista.tabla.setValueAt(
								vista.tabla.getValueAt(nodo.get("proceso"), 7) + ""
										+ cola.get(nodo.get("proceso")).get("tiempoEspera") + ", ",
								nodo.get("proceso"), 7);
						cola.get(nodo.get("proceso")).put("tiempoEspera", 0);
						cola.get(nodo.get("proceso")).put("tiempoBloqueado", 0);
					}
				}
			}

			for (Map<String, Integer> nodo : C) {
				int i = nodo.get("proceso");
				g.setColor(Color.BLACK);
				g.drawString(nodo.get("proceso") + "", 5, y * i + 35);
				if (nodo.get("tiempoEspera") > 0) {
					semaforo = false;
					g.setColor(Color.GRAY);
					g.fillRect(6 * tiempo + 36, y * i + 25, 6, 6);
					nodo.put("tiempoEspera", nodo.get("tiempoEspera") - 1);
					int espera = cola.get(nodo.get("proceso")).get("tiempoEspera");
					if (espera == 0 && cola.get(nodo.get("proceso")).get("tiempoBloqueado") == 0) {
						pintarCambio();
						cola.get(nodo.get("proceso")).put("tiempoFinalizacion", tiempo);
						vista.tabla.setValueAt(
								vista.tabla.getValueAt(nodo.get("proceso"), 5) + ""
										+ cola.get(nodo.get("proceso")).get("tiempoFinalizacion") + ", ",
								nodo.get("proceso"), 5);
					}
					cola.get(nodo.get("proceso")).put("tiempoBloqueado", 0);
					cola.get(nodo.get("proceso")).put("tiempoEspera", espera + 1);
				} else if (nodo.get("tiempoRafaga") > 0) {
					g.setColor(Color.BLUE);
					g.fillRect(6 * tiempo + 36, y * i + 21, 6, 12);
					nodo.put("tiempoRafaga", nodo.get("tiempoRafaga") - 1);
					if (cola.get(nodo.get("proceso")).get("tiempoEspera") > 0
							|| cola.get(nodo.get("proceso")).get("tiempoBloqueado") > 0) {
						cola.get(nodo.get("proceso")).put("tiempoComienzo", tiempo);
						vista.tabla.setValueAt(
								vista.tabla.getValueAt(nodo.get("proceso"), 4) + ""
										+ cola.get(nodo.get("proceso")).get("tiempoComienzo") + ", ",
								nodo.get("proceso"), 4);
						vista.tabla.setValueAt(
								vista.tabla.getValueAt(nodo.get("proceso"), 7) + ""
										+ cola.get(nodo.get("proceso")).get("tiempoEspera") + ", ",
								nodo.get("proceso"), 7);
						cola.get(nodo.get("proceso")).put("tiempoEspera", 0);
						cola.get(nodo.get("proceso")).put("tiempoBloqueado", 0);
					}
				}
			}

			for (Map<String, Integer> nodo : bloqueados) {
				int i = nodo.get("proceso");
				if (nodo.get("tiempoBloqueado") > 0) {
					g.setColor(Color.RED);
					g.fillRect(6 * tiempo + 36, y * i + 25, 6, 6);
					// if()
					nodo.put("tiempoBloqueado", nodo.get("tiempoBloqueado") - 1);
					int tiempoBloqueado = cola.get(nodo.get("proceso")).get("tiempoBloqueado");
					cola.get(nodo.get("proceso")).put("tiempoBloqueado", tiempoBloqueado + 1);
				}
			}

			tiempo++;
			pintarSemaforo(semaforo);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void analizar() {
		envejecer();
		desbloquear();
		if (A.size() > 0) {
			System.out.println("se planifica RoundRobin");
			bloquear(A);
			if (A.size() > 0) {
				planificarA(A);
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
			bloquear(B);
			if (B.size() > 0) {
				planificarB(B);
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
			bloquear(C);
			if (C.size() > 0) {
				planificarC(C);
				aplazar(C, 1);
			} else {
				analizar();
			}
		}
	}

	public void envejecer() {
		for (Map<String, Integer> nodo : cola) {
			if (nodo.get("tiempoEspera") + 1 >= 20) {
				System.out.println("el proceso " + nodo.get("proceso") + " debe envejecer");
				if (nodo.get("prioridad") == 3) {
					for (Map<String, Integer> nodoAux : C) {
						if (nodoAux.get("proceso") == nodo.get("proceso")
								&& nodoAux.get("prioridad") == nodo.get("prioridad")) {
							nodoAux.put("prioridad", 2);
							// nodo.put("prioridad", 2);
							C.remove(nodoAux);
							B.add(nodoAux);
							ordenar = true;
							String mensaje = "Se mueve el proceso " + nodo.get("proceso") + " de la cola C a la cola B";
							// JOptionPane.showMessageDialog(null, mensaje);
							System.out.println(mensaje);
							break;
						}
					}
				} else if (nodo.get("prioridad") == 2) {
					for (Map<String, Integer> nodoAux : B) {
						if (nodoAux.get("proceso") == nodo.get("proceso")
								&& nodoAux.get("prioridad") == nodo.get("prioridad")) {
							nodoAux.put("prioridad", 1);

							B.remove(nodoAux);
							A.add(nodoAux);
							String mensaje = "Se mueve el proceso " + nodo.get("proceso") + " de la cola B a la cola A";

							System.out.println(mensaje);
							System.out.println(mensaje);
							break;
						}
					}
				}
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
					return;
				}
			}
		}
	}

	public void planificarA(ArrayList<Map<String, Integer>> listos) {
		if (quantum == 0 && listos.size() > 0) {
			Map<String, Integer> nodo = listos.get(0);

			pintarCambio();
			nodo.put("tiempoFinalizacion", tiempo);
			vista.tabla.setValueAt(
					vista.tabla.getValueAt(nodo.get("proceso"), 5) + "" + nodo.get("tiempoFinalizacion") + ", ",
					nodo.get("proceso"), 5);

			listos.remove(nodo);
			if (nodo.get("tiempoRafaga") > 0) {
				listos.add(nodo);
				System.out.println("se envia el proceso " + nodo.get("proceso") + " al final de la cola");
			} else {
				System.out.println("se elimina el proceso " + nodo.get("proceso") + " de la cola");
				vista.tabla.setValueAt(nodo.get("tiempoFinalizacion") - nodo.get("tiempoLlegada"), nodo.get("proceso"),
						6);
			}

			for (Map<String, Integer> nod : listos) {
				System.out.print(nod.get("proceso") + " - ");
			}
			System.out.println("");
		}

		if (quantum <= 0 && listos.size() > 0) {
			Map<String, Integer> nodo = listos.get(0);
			System.out.println("\nel quantum lo tiene el proceso " + nodo.get("proceso"));
			nodo.put("tiempoComienzo", tiempo);
			vista.tabla.setValueAt(
					vista.tabla.getValueAt(nodo.get("proceso"), 4) + "" + nodo.get("tiempoComienzo") + ", ",
					nodo.get("proceso"), 4);
			vista.tabla.setValueAt(
					vista.tabla.getValueAt(nodo.get("proceso"), 7) + ""
							+ (nodo.get("tiempoComienzo") - nodo.get("tiempoFinalizacion")) + ", ",
					nodo.get("proceso"), 7);

			if (nodo.get("tiempoRafaga") < longMax) {
				quantum = nodo.get("tiempoRafaga");
			} else {
				quantum = longMax;
			}

			System.out.println("quantum: " + quantum);
		}

		if (quantum > 0 && listos.size() > 0) {
			for (int i = 1; i < listos.size(); i++) {
				listos.get(i).put("tiempoEspera", 1);
				System.out.println(
						"el nodo " + listos.get(i).get("proceso") + " se aplaza " + listos.get(i).get("tiempoEspera"));
			}
		}
		quantum--;
	}

	public void planificarB(ArrayList<Map<String, Integer>> listos) {
		if (listos.size() > 0) {
			// eliminar nodos ejecutados
			Map<String, Integer> nodo = listos.get(0);
			if (nodo.get("tiempoRafaga") == 0) {
				listos.remove(nodo);
				System.out.println("se elimina el proceso " + nodo.get("proceso") + " de la cola");
				nodo.put("tiempoFinalizacion", tiempo);
				vista.tabla.setValueAt(
						vista.tabla.getValueAt(nodo.get("proceso"), 5) + "" + nodo.get("tiempoFinalizacion") + ", ",
						nodo.get("proceso"), 5);
				vista.tabla.setValueAt(nodo.get("tiempoFinalizacion") - nodo.get("tiempoLlegada"), nodo.get("proceso"),
						6);

			}

			// ordenar nodos en SJF Expulsivo
			if (ordenar && listos.size() > 0) {
				nodo = listos.get(0);
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

				nodo = listos.get(0);
				nodo.put("tiempoEspera", 0);

			}

			// mostrar proximo orden en los nodos
			for (Map<String, Integer> nod : listos) {
				System.out.print(nod.get("proceso") + " - ");
			}
			System.out.println("");

			// aumenta tiempo de espera
			for (int i = 1; i < listos.size(); i++) {
				listos.get(i).put("tiempoEspera", 1);
				System.out.println(
						"el nodo " + listos.get(i).get("proceso") + " se aplaza " + listos.get(i).get("tiempoEspera"));
			}
		}
	}

	public void planificarC(ArrayList<Map<String, Integer>> listos) {
		Map<String, Integer> nodo = listos.get(0);
		if (nodo.get("tiempoRafaga") == 0) {
			listos.remove(nodo);
			System.out.println("se elimina el proceso " + nodo.get("proceso") + " de la cola");
			nodo.put("tiempoFinalizacion", tiempo);
			vista.tabla.setValueAt(
					vista.tabla.getValueAt(nodo.get("proceso"), 5) + "" + nodo.get("tiempoFinalizacion") + ", ",
					nodo.get("proceso"), 5);
			vista.tabla.setValueAt(nodo.get("tiempoFinalizacion") - nodo.get("tiempoLlegada"), nodo.get("proceso"), 6);

		}
	}

	public void bloquear(ArrayList<Map<String, Integer>> listos) {
		Map<String, Integer> nodo = listos.get(0);
		if (tiempo - nodo.get("tiempoComienzo") > 0 && nodo.get("tiempoRafaga") > 0) {
			Random rnd = new Random();
			int probabilidad = rnd.nextInt(5);
			if (probabilidad == 1) {
				int tiempoBloqueado = rnd.nextInt(4) + 1;

				nodo.put("tiempoBloqueado", tiempoBloqueado);
				System.out.println("\nse bloquea el proceso " + nodo.get("proceso") + " por " + tiempoBloqueado
						+ " en el tiempo " + tiempo);

				bloqueados.add(nodo);
				listos.remove(nodo);

				if (nodo.get("prioridad") == 1) {
					quantum = -1;
				}
				nodo.put("tiempoFinalizacion", tiempo);
				vista.tabla.setValueAt(
						vista.tabla.getValueAt(nodo.get("proceso"), 5) + "" + nodo.get("tiempoFinalizacion") + ", ",
						nodo.get("proceso"), 5);

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
			Map<String, Integer> nodo = listos.get(i);
			if (nodo.get("tiempoRafaga") == 0) {
				listos.remove(nodo);
				System.out.println("se elimina el proceso " + nodo.get("proceso") + " de la cola");
				nodo.put("tiempoFinalizacion", tiempo);
				vista.tabla.setValueAt(
						vista.tabla.getValueAt(nodo.get("proceso"), 5) + "" + nodo.get("tiempoFinalizacion") + ", ",
						nodo.get("proceso"), 5);
				vista.tabla.setValueAt(nodo.get("tiempoFinalizacion") - nodo.get("tiempoLlegada"), nodo.get("proceso"),
						6);

			} else {
				nodo.put("tiempoEspera", 1);
				System.out.println(
						"el nodo " + listos.get(i).get("proceso") + " se aplaza " + listos.get(i).get("tiempoEspera"));
			}
		}
	}
}
