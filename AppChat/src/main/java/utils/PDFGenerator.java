package utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import dominio.modelo.Contacto;
import dominio.modelo.ContactoIndividual;
import dominio.modelo.Grupo;
import dominio.modelo.Mensaje;
import dominio.modelo.Usuario;

/**
 * Clase para generar documentos PDF con información de contactos y grupos.
 * Esta funcionalidad está disponible solo para usuarios premium.
 */
public class PDFGenerator {
    
    // Constantes para formato del PDF
    private static final Font TITULO_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
    private static final Font SUBTITULO_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.DARK_GRAY);
    private static final Font TEXTO_NORMAL_FONT = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
    private static final Font TEXTO_PEQUEÑO_FONT = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.DARK_GRAY);
    private static final Font CABECERA_TABLA_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
    private static final Font PIE_PAGINA_FONT = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
    private static final Font MENSAJE_EMISOR_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.BLUE);
    private static final Font MENSAJE_RECEPTOR_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, new BaseColor(0, 128, 0));
    private static final Font MENSAJE_CONTENIDO_FONT = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
    private static final Font MENSAJE_FECHA_FONT = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.GRAY);
    
    // Formato para fechas
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    /**
     * Genera un informe PDF con la lista de contactos y grupos del usuario.
     * 
     * @param usuario Usuario actual
     * @param rutaDestino Ruta donde se guardará el archivo PDF
     * @return true si el PDF se generó correctamente, false en caso contrario
     */
    public static boolean generarInformeContactos(Usuario usuario, String rutaDestino) {
        if (usuario == null) {
            return false;
        }
        
        // Verificar que el usuario es premium
        if (!usuario.isEsPremium()) {
            return false;
        }
        
        Document documento = new Document();
        
        try {
            PdfWriter.getInstance(documento, new FileOutputStream(rutaDestino));
            documento.open();
            
            // Título del documento
            Paragraph titulo = new Paragraph("Informe de Contactos y Grupos", TITULO_FONT);
            titulo.setAlignment(Paragraph.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            documento.add(titulo);
            
            // Información del usuario
            Paragraph infoUsuario = new Paragraph("Usuario: " + usuario.getNombre(), SUBTITULO_FONT);
            infoUsuario.setSpacingAfter(5);
            documento.add(infoUsuario);
            
            Paragraph infoTelefono = new Paragraph("Teléfono: " + usuario.getTelefono(), TEXTO_NORMAL_FONT);
            infoTelefono.setSpacingAfter(5);
            documento.add(infoTelefono);
            
            Paragraph infoEmail = new Paragraph("Email: " + usuario.getEmail(), TEXTO_NORMAL_FONT);
            infoEmail.setSpacingAfter(20);
            documento.add(infoEmail);
            
            // Separar contactos individuales y grupos
            List<Contacto> contactos = usuario.getContactos();
            List<ContactoIndividual> contactosIndividuales = contactos.stream()
                    .filter(c -> c instanceof ContactoIndividual)
                    .map(c -> (ContactoIndividual) c)
                    .toList();
            
            List<Grupo> grupos = contactos.stream()
                    .filter(c -> c instanceof Grupo)
                    .map(c -> (Grupo) c)
                    .toList();
            
            // Tabla de contactos individuales
            documento.add(new Paragraph("Contactos Individuales", SUBTITULO_FONT));
            if (contactosIndividuales.isEmpty()) {
                documento.add(new Paragraph("No hay contactos individuales registrados.", TEXTO_NORMAL_FONT));
            } else {
                PdfPTable tablaContactos = new PdfPTable(3); // 3 columnas
                tablaContactos.setWidthPercentage(100);
                tablaContactos.setSpacingBefore(10f);
                tablaContactos.setSpacingAfter(20f);
                
                // Establecer el ancho relativo de las columnas
                float[] anchos = {1f, 2f, 1.5f};
                tablaContactos.setWidths(anchos);
                
                // Cabecera de la tabla
                agregarCeldaCabecera(tablaContactos, "ID");
                agregarCeldaCabecera(tablaContactos, "Nombre");
                agregarCeldaCabecera(tablaContactos, "Teléfono");
                
                // Datos de contactos
                for (ContactoIndividual contacto : contactosIndividuales) {
                    tablaContactos.addCell(String.valueOf(contacto.getId()));
                    tablaContactos.addCell(contacto.getNombre());
                    tablaContactos.addCell(contacto.getUsuario().getTelefono());
                }
                
                documento.add(tablaContactos);
                
                // Añadir mensajes de cada contacto individual
                for (ContactoIndividual contacto : contactosIndividuales) {
                    List<Mensaje> mensajes = contacto.getMensajes();
                    if (!mensajes.isEmpty()) {
                        Paragraph tituloMensajes = new Paragraph("Mensajes con " + contacto.getNombre(), SUBTITULO_FONT);
                        tituloMensajes.setSpacingBefore(15f);
                        tituloMensajes.setSpacingAfter(10f);
                        documento.add(tituloMensajes);
                        
                        // Tabla para los mensajes
                        PdfPTable tablaMensajes = new PdfPTable(3); // Dirección, Contenido, Fecha
                        tablaMensajes.setWidthPercentage(100);
                        float[] anchosMensajes = {1f, 4f, 1.5f};
                        tablaMensajes.setWidths(anchosMensajes);
                        
                        // Cabeceras
                        agregarCeldaCabecera(tablaMensajes, "Dirección");
                        agregarCeldaCabecera(tablaMensajes, "Contenido");
                        agregarCeldaCabecera(tablaMensajes, "Fecha");
                        
                        // Ordenar mensajes por fecha
                        mensajes.sort((m1, m2) -> m1.getFechaEnvio().compareTo(m2.getFechaEnvio()));
                        
                        // Añadir cada mensaje
                        for (Mensaje mensaje : mensajes) {
                            // Dirección del mensaje
                            PdfPCell celdaDireccion = new PdfPCell();
                            if (mensaje.getTipo()) {
                                // Mensaje enviado
                                Paragraph direccion = new Paragraph("Enviado a", MENSAJE_EMISOR_FONT);
                                celdaDireccion.addElement(direccion);
                            } else {
                                // Mensaje recibido
                                Paragraph direccion = new Paragraph("Recibido de", MENSAJE_RECEPTOR_FONT);
                                celdaDireccion.addElement(direccion);
                            }
                            tablaMensajes.addCell(celdaDireccion);
                            
                            // Contenido del mensaje
                            String contenido = mensaje.getContenido();
                            if (contenido.startsWith("EMOJI:")) {
                                contenido = "[Emoji] " + contenido.substring(6);
                            }
                            tablaMensajes.addCell(new Paragraph(contenido, MENSAJE_CONTENIDO_FONT));
                            
                            // Fecha del mensaje
                            String fecha = mensaje.getFechaEnvio().format(FORMATO_FECHA);
                            tablaMensajes.addCell(new Paragraph(fecha, MENSAJE_FECHA_FONT));
                        }
                        
                        documento.add(tablaMensajes);
                    }
                }
            }
            
            // Tabla de grupos
            documento.add(new Paragraph("Grupos", SUBTITULO_FONT));
            if (grupos.isEmpty()) {
                documento.add(new Paragraph("No hay grupos registrados.", TEXTO_NORMAL_FONT));
            } else {
                for (Grupo grupo : grupos) {
                    Paragraph nombreGrupo = new Paragraph("Grupo: " + grupo.getNombre(), SUBTITULO_FONT);
                    nombreGrupo.setSpacingBefore(20f);
                    nombreGrupo.setSpacingAfter(5f);
                    documento.add(nombreGrupo);
                    
                    documento.add(new Paragraph("ID: " + grupo.getId(), TEXTO_NORMAL_FONT));
                    documento.add(new Paragraph("Miembros:", TEXTO_NORMAL_FONT));
                    
                    // Tabla de miembros del grupo
                    PdfPTable tablaMiembros = new PdfPTable(2); // 2 columnas
                    tablaMiembros.setWidthPercentage(90);
                    tablaMiembros.setSpacingBefore(5f);
                    tablaMiembros.setSpacingAfter(15f);
                    
                    // Cabecera de la tabla
                    agregarCeldaCabecera(tablaMiembros, "Nombre");
                    agregarCeldaCabecera(tablaMiembros, "Teléfono");
                    
                    // Datos de miembros
                    for (ContactoIndividual miembro : grupo.getMiembros()) {
                        tablaMiembros.addCell(miembro.getNombre());
                        tablaMiembros.addCell(miembro.getUsuario().getTelefono());
                    }
                    
                    documento.add(tablaMiembros);
                    
                    // Mensajes del grupo
                    List<Mensaje> mensajesGrupo = grupo.getMensajes();
                    if (!mensajesGrupo.isEmpty()) {
                        Paragraph tituloMensajesGrupo = new Paragraph("Mensajes del grupo", TEXTO_NORMAL_FONT);
                        tituloMensajesGrupo.setSpacingBefore(10f);
                        tituloMensajesGrupo.setSpacingAfter(5f);
                        documento.add(tituloMensajesGrupo);
                        
                        // Tabla para los mensajes del grupo
                        PdfPTable tablaMensajesGrupo = new PdfPTable(3); // Emisor, Contenido, Fecha
                        tablaMensajesGrupo.setWidthPercentage(100);
                        float[] anchosMensajesGrupo = {1.5f, 3.5f, 1.5f};
                        tablaMensajesGrupo.setWidths(anchosMensajesGrupo);
                        
                        // Cabeceras
                        agregarCeldaCabecera(tablaMensajesGrupo, "Emisor");
                        agregarCeldaCabecera(tablaMensajesGrupo, "Contenido");
                        agregarCeldaCabecera(tablaMensajesGrupo, "Fecha");
                        
                        // Ordenar mensajes por fecha
                        mensajesGrupo.sort((m1, m2) -> m1.getFechaEnvio().compareTo(m2.getFechaEnvio()));
                        
                        // Añadir cada mensaje
                        for (Mensaje mensaje : mensajesGrupo) {
                            // Emisor del mensaje
                            String nombreEmisor = mensaje.getEmisor().getNombre();
                            tablaMensajesGrupo.addCell(new Paragraph(nombreEmisor, TEXTO_PEQUEÑO_FONT));
                            
                            // Contenido del mensaje
                            String contenido = mensaje.getContenido();
                            if (contenido.startsWith("EMOJI:")) {
                                contenido = "[Emoji] " + contenido.substring(6);
                            }
                            tablaMensajesGrupo.addCell(new Paragraph(contenido, MENSAJE_CONTENIDO_FONT));
                            
                            // Fecha del mensaje
                            String fecha = mensaje.getFechaEnvio().format(FORMATO_FECHA);
                            tablaMensajesGrupo.addCell(new Paragraph(fecha, MENSAJE_FECHA_FONT));
                        }
                        
                        documento.add(tablaMensajesGrupo);
                    }
                }
            }
            
            // Pie de página
            documento.add(Chunk.NEWLINE);
            documento.add(Chunk.NEWLINE);
            Paragraph piePagina = new Paragraph("Informe generado el " + 
                    LocalDateTime.now().format(FORMATO_FECHA) + 
                    " - Usuario Premium", PIE_PAGINA_FONT);
            piePagina.setAlignment(Paragraph.ALIGN_CENTER);
            documento.add(piePagina);
            
            documento.close();
            return true;
            
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
            if (documento.isOpen()) {
                documento.close();
            }
            return false;
        }
    }
    
    /**
     * Método auxiliar para agregar celdas de cabecera a una tabla.
     */
    private static void agregarCeldaCabecera(PdfPTable tabla, String texto) {
        PdfPCell celda = new PdfPCell(new Paragraph(texto, CABECERA_TABLA_FONT));
        celda.setBackgroundColor(BaseColor.DARK_GRAY);
        celda.setPadding(5);
        tabla.addCell(celda);
    }
}