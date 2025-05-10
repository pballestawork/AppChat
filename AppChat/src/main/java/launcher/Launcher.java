package launcher;

import servicio.ServicioPersistenciaLauncher;
import utils.Utils;
import vista.LoginView;

public class Launcher {

	public static void main(String[] args) {		
		LoginView.main(null);
	}

	private static void lanzarServicioPesistencia() {
		ServicioPersistenciaLauncher.lanzarServicio();
	}
	
	private static void reiniciar() {
		Utils.borrarBaseDatos();
		Utils.datosPrueba();
	}
	private static void mostrarBaseDatos() {
		Utils.mostrarBaseDatos();
	}
	
	
}
