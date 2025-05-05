package launcher;

import servicio.ServicioPersistenciaLauncher;
import utils.Utils;
import vista.LoginView;

public class Launcher {

	public static void main(String[] args) {
		ServicioPersistenciaLauncher.lanzarServicio();
		Utils.borrarBaseDatos();
		LoginView.main(null);
	}
}
