package utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import dominio.controlador.ChatControllerException;
import dominio.modelo.Contacto;
import dominio.modelo.ContactoIndividual;
import dominio.modelo.Grupo;
import dominio.modelo.Usuario;

public class PruebaTest {
	private static Usuario u;

	public static void main(String[] args) throws ChatControllerException {
		ChatControllerStub controlador = ChatControllerStub.getUnicaInstancia();

		// 1.Registrar nuevo usuario
		System.out.println("1.Registrar nuevo usuario");
		controlador.registrarUsuario("a", LocalDate.now(), "pablo@gmail.com", null, "6", "123","saludo");

		// 2.Iniciar sesión
		System.out.println("2.Iniciar sesión");
		u = controlador.iniciarSesion("6", "123");
		System.out.println(u);

		// 3.Cambiar saludo
		System.out.println("3.Cambiar saludo");
		controlador.actualizarPerfil("Nuevo saludo", "Nueva imagen");
		System.out.println(u);
		//
		// 4.Añadir contacto
		System.out.println("4.Añadir contacto");
		controlador.agregarContacto("aa", "1");
		System.out.println(u);

		// 5.Eliminar contacto (grupo también)
		System.out.println("5.Eliminar contacto (grupo también)");
		controlador.eliminarContacto(u.getContactos().get(0));
		System.out.println(u);

		//

		// 6.Crear grupo // Crearemos 2 contactos para ello
		System.out.println("6.Crear grupo // Crearemos 2 contactos para ello");
		controlador.agregarContacto("c1", "1");
		controlador.agregarContacto("c2", "2");

		List<ContactoIndividual> miembrosGrupo = u.getContactos().stream()
				.map(c -> (ContactoIndividual) c)
				.collect(Collectors.toList());
		
		controlador.crearGrupo(miembrosGrupo, "Molones", null);
		System.out.println(u);
		System.out.println((Grupo) u.getContactos().get(2));

		// 7.Añadir al grupo
		System.out.println("7.Añadir al grupo");
		controlador.agregarContacto("c3", "3");
		controlador.agregarContactoAGrupo((Grupo) u.getContactos().get(2),(ContactoIndividual) u.getContactos().get(3));
		System.out.println((Grupo) u.getContactos().get(2));
		
		// 8.Eliminar del grupo
		System.out.println("8.Añadir al grupo");
		controlador.eliminarContactoDeGrupo((Grupo) u.getContactos().get(2), (ContactoIndividual) u.getContactos().get(1));
		System.out.println((Grupo) u.getContactos().get(2));
		
		
		// 9.Cambiar nombre o foto
		System.out.println("9.Cambiar nombre o foto");
		controlador.actualizarGrupo((Grupo) u.getContactos().get(2), "NIGAS", null);
		System.out.println((Grupo) u.getContactos().get(2));
		System.out.println(u);
		
		// 9.1 Eliminar grupo
		controlador.eliminarContacto((Grupo) u.getContactos().get(2));
		System.out.println(u);
		
		
		// 10.Enviar/recibir contactos
		controlador.enviarMensaje(u.getContactos().get(0), "Hola");
		
		// 11.Enviar/recibir usuarios (no contacto)
		
		// 12.Enviar/recibir grupo
		
		// 13.Convertir a premium
		System.out.println("13.Convertir a premium");
		boolean resultadoPremium = controlador.actualizarUsuarioAPremium();
		System.out.println("¿Usuario actualizado a premium correctamente? " + resultadoPremium);
		System.out.println("Estado premium del usuario: " + u.isEsPremium());
		
		// 14.Generar PDF
		System.out.println("14.Generar PDF");
		// Crear un archivo PDF en la carpeta temporal del sistema
		String rutaPDF = System.getProperty("java.io.tmpdir") + "informe_contactos_test.pdf";
		boolean resultadoPDF = controlador.generarInformePDF(rutaPDF);
		System.out.println("¿PDF generado correctamente? " + resultadoPDF);
		System.out.println("Ruta del PDF generado: " + rutaPDF);
		
		// Enviar mensajes adicionales para probar que se incluyen en el PDF
		System.out.println("15.Enviar mensajes adicionales para probar el PDF");
		controlador.enviarMensaje(u.getContactos().get(0), "Este es un mensaje de prueba para el PDF");
		controlador.enviarMensaje(u.getContactos().get(0), "Otro mensaje para verificar que se incluye en el PDF");
		
		// Generar otro PDF con los nuevos mensajes
		String rutaPDF2 = System.getProperty("java.io.tmpdir") + "informe_contactos_test_con_mensajes.pdf";
		boolean resultadoPDF2 = controlador.generarInformePDF(rutaPDF2);
		System.out.println("¿Segundo PDF generado correctamente? " + resultadoPDF2);
		System.out.println("Ruta del segundo PDF: " + rutaPDF2);
		
		// Mostrar mensaje de finalización
		System.out.println("\nPruebas completadas correctamente. Verifique los archivos PDF generados.");
	}

	/*
	 * private static ServicioPersistencia servicioPersistencia; private static
	 * IAdaptadorUsuarioDAO usuarioDAO; private static
	 * IAdaptadorContactoIndividualDAO contactoDao; private static
	 * IAdaptadorGrupoDAO grupoDao; private static IAdaptadorMensajeDAO mensajeDAO;
	 * public static void inicializarAdaptadores() { servicioPersistencia =
	 * FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	 * 
	 * List<Entidad> entidades = servicioPersistencia.recuperarEntidades();//TODO
	 * quitar o se borrara todo entidades.stream().forEach(e ->
	 * servicioPersistencia.borrarEntidad(e));
	 * 
	 * try { FactoriaDAO factoriaDAO =
	 * FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
	 * 
	 * mensajeDAO = factoriaDAO.getMensajeDAO(); grupoDao =
	 * factoriaDAO.getGrupoDAO(); contactoDao =
	 * factoriaDAO.getContactoIndividualDAO(); usuarioDAO =
	 * factoriaDAO.getUsuarioDAO();
	 * 
	 * } catch (DAOException e) { System.out.println(e.getMessage()); } }
	 */

}
