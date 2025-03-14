package factoriaPruebas;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import dominio.modelo.Contacto;
import dominio.modelo.ContactoIndividual;
import dominio.modelo.Grupo;
import dominio.modelo.Mensaje;
import dominio.modelo.Usuario;

public class FactoriaPruebas {
    private static final Random random = new Random();

   
    public static Usuario crearUsuarioCompleto() {
    	Usuario usuario = crearUsuario();
    		
    	ContactoIndividual contactoIndividual = crearContactoIndividual(usuario);
    	Grupo grupo = crearGrupo(List.of(contactoIndividual));
    	
    	contactoIndividual.addMensaje(crearMensaje(usuario, generarMensaje()));
    	grupo.addMensaje(crearMensaje(usuario, generarMensaje()));
    	
    	LinkedList<Contacto> contactos = (LinkedList<Contacto>) usuario.getContactos();
    	
    	contactos.add(contactoIndividual);
    	contactos.add(grupo);
    	usuario.setContactos(contactos);
    	
		return usuario;
    }
    
    public static Usuario crearUsuario() {
        String nombre = generarNombre();
        Usuario usuario = new Usuario();
        usuario.setId(0);
        usuario.setNombre(nombre);
        usuario.setTelefono(generarTelefono());
        usuario.setEmail(generarEmail(nombre));
        usuario.setContrasena(generarContrasena());
        usuario.setFotoPerfil("foto_" + usuario.getId() + ".jpg");
        usuario.setSaludo("Hola, soy " + nombre);
        usuario.setEsPremium(random.nextBoolean());
        usuario.setContactos(new LinkedList<>());

        return usuario;
    }

    public static ContactoIndividual crearContactoIndividual(Usuario usuario) {
        ContactoIndividual contacto = new ContactoIndividual();
        contacto.setId(0);
        contacto.setNombre(usuario.getNombre());
        contacto.setUsuario(usuario);
        contacto.setMensajes(new ArrayList<>());
        return contacto;
    }

    public static Grupo crearGrupo(List<ContactoIndividual> miembros) {
        Grupo grupo = new Grupo();
        grupo.setId(0);
        grupo.setNombre("Grupo " + 0);
        grupo.setImagen("grupo_" + grupo.getId() + ".jpg");
        grupo.setMiembros(miembros);
        grupo.setMensajes(new ArrayList<>());
        return grupo;
    }

    public static Mensaje crearMensaje(Usuario emisor, String contenido) {
        Mensaje mensaje = new Mensaje();
        mensaje.setId(0);
        mensaje.setEmisor(emisor);
        mensaje.setContenido(contenido);
        mensaje.setFechaEnvio(LocalDateTime.now());
        mensaje.setTipo(true);
        return mensaje;
    }
    
    //PRIVATE STATIC //////////////////////////////////////////////////////////////////////////////
    
    private static String generarNombre() {
        String[] nombres = {"Pablo","Alvaro","Juan", "Ana", "Carlos", "Laura", "Pedro", "Sofia"};
        String[] apellidos = {"Gomez", "Perez", "Lopez", "Fernandez", "Diaz"};
        return nombres[random.nextInt(nombres.length)] + " " + apellidos[random.nextInt(apellidos.length)];
    }

    private static String generarEmail(String nombre) {
        return nombre.toLowerCase().replace(" ", ".") + "@example.com";
    }

    private static String generarTelefono() {
        return "+34 " + (600000000 + random.nextInt(99999999));
    }

    private static String generarMensaje() {
        String[] mensajes = {"Hola","Hey", "Como estas?", "Nos vemos luego", "Buen dia", "Llamame cuando puedas.", "Adios","Chao"};
        return mensajes[random.nextInt(mensajes.length)];
    }
    
    private static String generarContrasena() {
    	return "password"+ random.nextInt(999);
    }
    
}
