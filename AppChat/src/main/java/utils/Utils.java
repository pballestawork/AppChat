package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import beans.Entidad;
import beans.Propiedad;
import persistencia.dao.Identificable;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

public class Utils {

	public static String getRutaResourceFromFile(File archivoImagen) {
		// Define la ruta base del proyecto que debe apuntar a "src/main/resources"
		Path rutaBase = Paths.get("src/main/resources").toAbsolutePath();

		// Obtén la ruta absoluta del archivo
		Path rutaArchivo = archivoImagen.toPath().toAbsolutePath();
		
		try {
			// Calcula la ruta relativa desde "src/main/resources" hasta el archivo
			Path rutaRelativa = rutaBase.relativize(rutaArchivo);

			// Devuelve la ruta en formato compatible con getResource()
			return "/" + rutaRelativa.toString().replace("\\", "/");
		} catch (IllegalArgumentException e) {
			// Si no se puede calcular la ruta relativa (por ejemplo, si están en unidades diferentes)
			// Intentar extraer la parte después de "resources" si existe
			String rutaStr = rutaArchivo.toString().replace("\\", "/");
			int indexResources = rutaStr.indexOf("/resources/");
			
			if (indexResources >= 0) {
				// Tomar la parte después de "/resources/"
				String rutaRelativa = rutaStr.substring(indexResources + "/resources".length());
				return rutaRelativa; // Ya incluye la barra inicial
			} else {
				// Si no contiene "/resources/", devolver una ruta basada en el nombre del archivo
				// Esto asume que el archivo está en la carpeta raíz de recursos
				return "/" + archivoImagen.getName();
			}
		}
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
	 * Descarga una imagen desde una URL y la guarda como un archivo local en la carpeta especificada
	 * 
	 * @param urlStr URL de la imagen a descargar
	 * @param directorioDestino Directorio donde guardar la imagen
	 * @return El archivo donde se guardó la imagen o null si hubo un error
	 * @throws IOException Si ocurre un error al descargar o guardar la imagen
	 */
	public static File descargarImagenDesdeURL(String urlStr, String directorioDestino) throws IOException {
		// Crear directorio si no existe
		Path directorio = Paths.get(directorioDestino);
		if (!Files.exists(directorio)) {
			Files.createDirectories(directorio);
		}
		
		// Generar un nombre único para el archivo
		String nombreArchivo = "img_" + UUID.randomUUID().toString().substring(0, 8);
		
		// Determinar extensión del archivo basada en la URL
		String extension = ".jpg"; // Extensión por defecto
		if (urlStr.toLowerCase().endsWith(".png")) {
			extension = ".png";
		} else if (urlStr.toLowerCase().endsWith(".gif")) {
			extension = ".gif";
		} else if (urlStr.toLowerCase().endsWith(".jpeg") || urlStr.toLowerCase().endsWith(".jpg")) {
			extension = ".jpg";
		}
		
		// Crear ruta del archivo local
		File archivoLocal = new File(directorio.toFile(), nombreArchivo + extension);
		
		// Descargar la imagen
		URL url = new URL(urlStr);
		try (InputStream in = url.openStream(); 
			 FileOutputStream out = new FileOutputStream(archivoLocal)) {
			
			byte[] buffer = new byte[4096];
			int bytesRead;
			
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
		}
		
		// Verificar que el archivo es una imagen válida
		try {
			if (ImageIO.read(archivoLocal) == null) {
				archivoLocal.delete();
				throw new IOException("El archivo descargado no es una imagen válida");
			}
		} catch (Exception e) {
			archivoLocal.delete();
			throw new IOException("Error al verificar el archivo de imagen: " + e.getMessage());
		}
		
		return archivoLocal;
	}

	/**
	 * Metodo que crea una cadena de texto con los id separados por coma ',' de los
	 * objetos referidos.
	 * 
	 * @param objetosIdentificables Lista de objetos que implementan Identificable
	 * @return devuelve cadena con id
	 */
	public static String concatenarIds(List<? extends Identificable> objetosIdentificables) {
		return objetosIdentificables.stream().map(obj -> String.valueOf(obj.getId())).collect(Collectors.joining(","));
	}

	/**
	 * Metodo que crea una lista de los ids dentro de una cadena separados por ','.
	 * 
	 * @param cadenaIds Cadena con ids contactenados
	 * @return lista de ids
	 */
	public static List<Integer> getIdsByCadena(String cadenaIds) {
		if (cadenaIds == null || cadenaIds.isEmpty()) {
	        return new LinkedList<Integer>(); // Devuelve una lista vacía si la cadena es nula o vacía
	    }
		
		return Arrays.stream(cadenaIds.split(","))
				.filter(s -> !s.isEmpty())
				.map(Integer::parseInt) // Convertir cada elemento a int
				.collect(Collectors.toList());
	}
	
	public static void borrarBaseDatos() {
		ServicioPersistencia servicioPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		List<Entidad> entidades = servicioPersistencia.recuperarEntidades();
		entidades.forEach(e -> servicioPersistencia.borrarEntidad(e));
	}

	public static void mostrarBaseDatos() {
		ServicioPersistencia servicioPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		List<Entidad> entidades = servicioPersistencia.recuperarEntidades();
		
		for (Entidad e : entidades) {
			System.out.println("---" + e.getNombre());
			List<Propiedad> propiedades = e.getPropiedades();
			for (Propiedad p : propiedades) {
				System.out.println(p.getNombre() + ": " + p.getValor());
			}
		}
		
	}
}
