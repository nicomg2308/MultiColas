package SJF;

import javax.swing.JOptionPane;

public class HiloColaBloqueo extends Thread{
	private Proceso proceso;
	private int time;
	public HiloColaBloqueo(Proceso proceso, int time) {
		this.proceso = proceso;
		this.proceso.setTiempoFinal(this.proceso.getTiempoFinal()+time);
		this.time = time;
	}
	public void run() {
		try {
			sleep(time*1000);
			JOptionPane.showMessageDialog(null, "Me demore "+Integer.toString(time)+ " sg");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
