package colas;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

public class LienzoNodo extends Canvas {

	private Nodo nodos;
	Color[] colores = { Color.GREEN, Color.YELLOW, Color.BLUE };
	Graphics gra;

	public LienzoNodo(Nodo nodos) {
		this.nodos = nodos;
	}

	public void paint(Graphics g) {
		gra = g;
		g.setColor(Color.lightGray);
		g.fillRect(0, 0, getMaximumSize().width, getMaximumSize().height);
		pintarNodos();
	}

	public void pintarNodos() {
		Graphics g = gra;
		int x = 30;
		int y = 45;
		g.setColor(Color.darkGray);
		for (int j = 1; j <= 3; j++) {
			char letra = (char) (64 + j);
			g.drawString("COLA " + letra, 6, y * j + 48);
			Nodo nodo = nodos;
			int i = 0;
			while (nodo != null) {
				if (nodo.prioridad == j) {
					g.drawRect(x * i + 5, y * j + 50, 20, 20);
					g.setColor(colores[j - 1]);
					g.fillRect(x * i + 5, y * j + 50, 20, 20);
					g.setColor(Color.darkGray);
					g.drawString(nodo.tiempoLlegada + "", x * i + 6, y * j + 65);
					g.drawLine(x * i + 20, y * j + 50, x * i + 20, y * j + 70);
					g.drawLine(x * i + 23, y * j + 60, x * i + 35, y * j + 60);
					i++;
				}
				nodo = nodo.sig;
			}
		}
	}

}
