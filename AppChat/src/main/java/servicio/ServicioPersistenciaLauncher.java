package servicio;

import java.io.IOException;
import java.nio.file.Paths;

public class ServicioPersistenciaLauncher {

    public static void lanzarServicio() {
        try {
            String jarPath = Paths.get("lib", "driverPersistencia.jar").toString();

            ProcessBuilder builder = new ProcessBuilder("java", "-jar", jarPath);
            builder.inheritIO(); // Para ver la salida del servicio en consola
            builder.start();

        } catch (IOException e) {
            System.err.println("Error al lanzar el servicio:");
            e.printStackTrace();
        }
    }
}
