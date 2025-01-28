package dominio.controlador;

import java.awt.EventQueue;

import javax.swing.UIManager;

public class Launcher {
	public static void main(final String[] args){
		EventQueue.invokeLater(new Runnable() {
			public void run() { 
				//TODO INVOCAR LA VENTANA PRINCIPAL
				/*try {
					UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
					VentanaMain ventana = new VentanaMain(); //AQUI VA LA VENTA
					ventana.mostrarVentana();
				} catch (Exception e) {				
					e.printStackTrace();
				}*/
			}
		});
	}
}
