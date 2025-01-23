package utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import persistencia.dao.Identificable;

public class Utils {

	public static String getRutaResourceFromFile(File archivoImagen) {
		// Define la ruta base del proyecto que debe apuntar a "src/main/resources"
		Path rutaBase = Paths.get("src/main/resources").toAbsolutePath();

		// Obtén la ruta absoluta del archivo
		Path rutaArchivo = archivoImagen.toPath().toAbsolutePath();

		// Calcula la ruta relativa desde "src/main/resources" hasta el archivo
		Path rutaRelativa = rutaBase.relativize(rutaArchivo);

		// Devuelve la ruta en formato compatible con getResource()
		return "/" + rutaRelativa.toString().replace("\\", "/");
	}

	public static String getRutaResourceFromString(String source) {
		String target = "";
		if (source.contains("src\\main\\resources\\")) {
			target = source.substring(source.indexOf("src\\main\\resources\\") + "src\\main\\resources\\".length());
			// Cambia las barras de Windows (\) por barras de URL (/)
			target = "/" + target.replace("\\", "/");
		}
		return target;
	}

	/**
	 * Metodo que crea una cadena de texto con los id separados por coma ',' de los
	 * objetos referidos.
	 * 
	 * @param objetosIdentificables Lista de objetos que implementan Identificable
	 * @return devuelve cadena con id
	 */
	public static String concatenarIds(List<? extends Identificable> objetosIdentificables) {
		return objetosIdentificables.stream()
				.map(obj -> String.valueOf(obj.getId()))
				.collect(Collectors.joining(",")); 
	}

	/**
	 * Metodo que crea una lista de los ids dentro de una cadena separados por ','.
	 *  
	 * @param cadenaIds Cadena con ids contactenados
	 * @return lista de ids
	 */
	public static List<Integer> getIdsByCadena(String cadenaIds) {
		return Arrays.stream(cadenaIds.split(","))
				.map(Integer::parseInt) // Convertir cada elemento a int
				.collect(Collectors.toList());
	}

}
