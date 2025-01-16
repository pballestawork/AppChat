package persistencia.prueba;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

public class PruebaPersistencia {

	public static void main(String[] argv) {
		//Obtener servidor
		ServicioPersistencia s = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		
		//Mostramos el contenido de la base de datos inicial
		mostrarDataBase(s);
			
		//Creamos una entidad Estacion
		Entidad e1 = new Entidad();
		/*
		 * - id
		 * - nombre
		 * - propiedades
		 */
		//Definimos las propiedades de la clase
		e1.setNombre("Estacion");
		e1.setPropiedades(
				new ArrayList<Propiedad>(
						Arrays.asList(
								new Propiedad("nombre2","estacion1"),
								new Propiedad("plazas","2")
						)		
				)	
		);
		
		System.out.println(e1.getId());
		
		
		//añadimos la entidad
		e1 = s.registrarEntidad(e1); //Devuelve null si e1 ya estaba registrada
		
		//Mostramos el id que se le ha asignado a la entidad
		if(e1 != null)
			System.out.println(e1.getId());
	
		//Mostramos la database con la entidad
		mostrarDataBase(s);
		
		
		//Borramos todas las entidades
		borrarDataBase(s);
		
		//Mostramos la database vacía
		mostrarDataBase(s);
	}
	
	
	private static void borrarDataBase(ServicioPersistencia s) {
		List<Entidad> lst =  s.recuperarEntidades();
		
		for (Entidad entidad : lst) {
			s.borrarEntidad(entidad);
		}
	}


	private static void mostrarDataBase(ServicioPersistencia s) {
		//Recuperar todas las entidades
		List<Entidad> lst =  s.recuperarEntidades();
		
		//Mostrar las entidades (vacio la primera vez)
		System.out.println("Mostramos las entidades: {");
		for (Entidad e : lst) {
			System.out.println("\t"+e.getNombre()+": {");
			for (Propiedad p : e.getPropiedades()) {
				System.out.println("\t\t"+p.getNombre() + ": "+p.getValor());
			}
			System.out.println("\t}");
			
		}
		System.out.println("}");
	}
}

