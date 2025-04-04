package launcher;

import servicio.ServicioPersistenciaLauncher;
import vista.LoginView;

public class Launcher {

	public static void main(String[] args) {
		ServicioPersistenciaLauncher.lanzarServicio();
		LoginView.main(null);
	}
}
