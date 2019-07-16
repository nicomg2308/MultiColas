package colas;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import colas.Colas;

public class Vista implements ActionListener {

	JFrame jFrame;
	JPanel pVentana, pSuperior, pIzquierdo, pInferior, pCrear, pBloqueados, pTabla, pGrafico;
	JButton crear, eliminar, graficar;
	JTable tabla;
	DefaultTableModel modelo;
	JScrollPane sTabla;
	JScrollPane sTablaBloqueos;
	Colas colas;
	LienzoNodo lienzoIniciales;
	Procesador lienzoGantt;

	public Vista() {
		colas = new Colas();
		lienzoIniciales = new LienzoNodo(null);
		lienzoGantt = new Procesador(null, this);
		construirSuperior();
		construirInferior();
		construirVentana();
	}

	private void construirInferior() {
		pInferior = new JPanel();
		pInferior.setLayout(new GridLayout(1, 1, 5, 5));

		pGrafico = new JPanel();
		pGrafico.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY), "Diagrama de Gantt"));
		pGrafico.setLayout(null);
		pGrafico.setBackground(Color.WHITE);
		lienzoGantt.setBounds(10, 20, pGrafico.getSize().width - 10, pGrafico.getSize().height - 10);
		pGrafico.add(lienzoGantt);

		pInferior.add(pGrafico);

	}

	private void construirSuperior() {
		pSuperior = new JPanel();
		pSuperior.setLayout(new GridLayout(1, 2, 5, 5));
		pIzquierdo = new JPanel();
		pIzquierdo.setLayout(new GridLayout(1, 2, 5, 5));

		// Mitad
		pCrear = new JPanel();
		pCrear.setBorder(new TitledBorder(new LineBorder(Color.GRAY), "Procesos Primarios"));
		pCrear.setLayout(null);
		pCrear.setBackground(Color.WHITE);
		crear = new JButton("CREAR");
		crear.setBounds(126, 316, 80, 25);
		crear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				colas.procesos();
				colas.imprimir();
				System.out.println("num: " + colas.numNodosPrimarios + "\n");
				actualizarDatos();
			}
		});
		pCrear.add(crear);
		lienzoIniciales.setBounds(10, 20, pCrear.getSize().width - 10, pCrear.getSize().height - 60);
		pCrear.add(lienzoIniciales);

		pBloqueados = new JPanel();
		pBloqueados.setBorder(new TitledBorder(new LineBorder(Color.GRAY), "Tabla Bloqueados"));
		pBloqueados.setLayout(null);
		pBloqueados.setBackground(Color.WHITE);
		sTablaBloqueos = new JScrollPane();
		pBloqueados.add(sTablaBloqueos);

		pTabla = new JPanel();
		pTabla.setBorder(new TitledBorder(new LineBorder(Color.GRAY), "Tabla Bloqueados"));
		pTabla.setLayout(null);
		pTabla.setBackground(Color.WHITE);
		sTabla = new JScrollPane();
		pTabla.add(sTabla);

		pIzquierdo.setBackground(Color.WHITE);
		pSuperior.setBackground(Color.WHITE);
		pSuperior.add(pTabla);
		pIzquierdo.add(pCrear);
		pIzquierdo.add(pBloqueados);
		pSuperior.add(pIzquierdo);

	}

	private void construirVentana() {
		jFrame = new JFrame("Proyecto Final");
		jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
		jFrame.add(pInferior);
		jFrame.add(pSuperior);
		jFrame.setBackground(Color.WHITE);
		jFrame.pack();
		jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		jFrame.setVisible(true);
		jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	}

	public void actualizarDatos() {
		pCrear.remove(lienzoIniciales);
		lienzoIniciales = new LienzoNodo(colas.nodosPrimarios);
		lienzoIniciales.setBounds(10, 20, pCrear.getSize().width - 20, pCrear.getSize().height - 60);
		pCrear.add(lienzoIniciales);

		crearTabla();

		pGrafico.remove(lienzoGantt);
		lienzoGantt = new Procesador(colas.nodosPrimarios, this);
		lienzoGantt.setBounds(10, 20, pGrafico.getSize().width - 20, pGrafico.getSize().height - 30);
		pGrafico.add(lienzoGantt);
		pGrafico.setBackground(Color.WHITE);
	}

	public void crearBloqueados(Object[][] datos) {
		String[] nombresColumnas = { "Proceso", "T.Ejecucion", "T.Bloqueado" };
		pBloqueados.remove(sTablaBloqueos);
		modelo = new DefaultTableModel(datos, nombresColumnas);
		tabla = new JTable(modelo);
		tabla.setBackground(Color.WHITE);
		sTablaBloqueos = new JScrollPane(tabla);
		sTablaBloqueos.setBackground(Color.WHITE);
		sTablaBloqueos.setBounds(20, 20, pBloqueados.getSize().width - 30, pBloqueados.getSize().height - 40);
		pBloqueados.add(sTablaBloqueos, BorderLayout.CENTER);
	}

	public void crearTabla() {
		String[] nombresColumnas = { "Proceso", "Cola", "T.Llegada", "T.Rafaga", "T.Comienzo", "T.Finalizacion",
				"T.Retorno", "T.Espera" };
		Object[][] datos = new Object[colas.numNodosPrimarios][nombresColumnas.length];
		Nodo nodo = colas.nodosPrimarios;
		for (int i = 0; i < datos.length; i++) {
			datos[i][0] = nodo.proceso;
			char num = (char) (64 + nodo.prioridad);
			datos[i][1] = num;
			datos[i][2] = nodo.tiempoLlegada;
			datos[i][3] = nodo.tiempoRafaga;
			datos[i][4] = "";
			datos[i][5] = "";
			datos[i][6] = "";
			datos[i][7] = "";
			nodo = nodo.sig;
		}
		pTabla.remove(sTabla);
		modelo = new DefaultTableModel(datos, nombresColumnas);
		tabla = new JTable(modelo);
		tabla.setBackground(Color.WHITE);
		sTabla = new JScrollPane(tabla);
		sTabla.setBackground(Color.WHITE);
		sTabla.setBounds(20, 20, pTabla.getSize().width - 30, pTabla.getSize().height - 40);
		pTabla.add(sTabla, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Colas obj = new Colas();
		if (e.getSource() == crear) {
			try {
				obj.procesos();
				obj.imprimir();
			} catch (Exception excep) {
				obj.terminar();
			}
		}
	}

}
