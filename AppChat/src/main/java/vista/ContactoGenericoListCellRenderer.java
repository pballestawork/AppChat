package vista;

import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

import dominio.modelo.Contacto;
import dominio.modelo.ContactoIndividual;
import dominio.modelo.Grupo;

import java.awt.*;
import java.io.IOException;

public class ContactoGenericoListCellRenderer extends JPanel implements ListCellRenderer<Contacto> {
    private static final long serialVersionUID = 1L;
    
    // Constantes para la interfaz
    private static final Color COLOR_PRIMARIO = new Color(25, 118, 210);
    private static final Color COLOR_SECUNDARIO = new Color(239, 246, 255);
    private static final Color COLOR_TEXTO = new Color(33, 33, 33);
    private static final Color COLOR_SELECCION = new Color(184, 207, 229);
    private static final Font FUENTE_NOMBRE = new Font("Arial", Font.BOLD, 14);
    private static final Font FUENTE_DETALLE = new Font("Arial", Font.PLAIN, 12);
    private static final Font FUENTE_SALUDO = new Font("Arial", Font.ITALIC, 12);
    
    private static final Border SELECCIONADO = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 4, COLOR_PRIMARIO),  // Barra lateral derecha
            BorderFactory.createEmptyBorder(8, 10, 8, 10)  // Padding interior
    );
    private static final Border NO_SELECCIONADO = BorderFactory.createEmptyBorder(8, 10, 8, 10);

    private JLabel lblImagen;
    private JLabel lblNombre;
    private JLabel lblDetalle;
    private JLabel lblSaludo;
    private JPanel panelTexto;

    public ContactoGenericoListCellRenderer() {
        setLayout(new BorderLayout(10, 0)); // Espaciado horizontal entre imagen y texto
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Margen exterior

        lblImagen = new JLabel();
        lblImagen.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0)); // Margen izquierdo para la imagen
        
        lblNombre = new JLabel();
        lblNombre.setFont(FUENTE_NOMBRE);
        lblNombre.setForeground(COLOR_TEXTO);
        
        lblDetalle = new JLabel();
        lblDetalle.setFont(FUENTE_DETALLE);
        lblDetalle.setForeground(new Color(100, 100, 100)); // Gris oscuro
        
        lblSaludo = new JLabel();
        lblSaludo.setFont(FUENTE_SALUDO);
        lblSaludo.setForeground(new Color(120, 120, 120)); // Gris medio
        
        // Panel para organizar textos con espaciado vertical
        panelTexto = new JPanel();
        panelTexto.setLayout(new GridLayout(3, 1, 0, 2));
        panelTexto.setOpaque(false);
        panelTexto.add(lblNombre);
        panelTexto.add(lblDetalle);
        panelTexto.add(lblSaludo);

        add(lblImagen, BorderLayout.WEST);  // Imagen a la izquierda
        add(panelTexto, BorderLayout.CENTER);  // Texto a la derecha
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Contacto> listacontactos, Contacto contacto, int index,
            boolean isSelected, boolean cellHasFocus) {
        
        // Configuración específica según el tipo de contacto
        if (contacto instanceof ContactoIndividual) {
            // Es un contacto individual
            ContactoIndividual contactoIndividual = (ContactoIndividual) contacto;
            
            // Configuración de la imagen con borde circular
            String fotoUsuario = contactoIndividual.getUsuario().getFotoPerfil();
            cargarImagen(fotoUsuario);

            // Configuración del texto
            lblNombre.setText(contactoIndividual.getNombre());
            lblDetalle.setText("Tel: " + contactoIndividual.getUsuario().getTelefono());
            
            // Mostrar el saludo o un texto predeterminado si es nulo o vacío
            String saludo = contactoIndividual.getUsuario().getSaludo();
            lblSaludo.setText(saludo != null && !saludo.isEmpty() ? saludo : "Sin saludo");
            
        } else if (contacto instanceof Grupo) {
            // Es un grupo
            Grupo grupo = (Grupo) contacto;
            
            // Configuración de la imagen del grupo
            String imagenGrupo = grupo.getImagen();
            cargarImagen(imagenGrupo);
            
            // Configuración del texto
            lblNombre.setText(grupo.getNombre());
            lblDetalle.setText("Grupo: " + grupo.getMiembros().size() + " miembros");
            lblSaludo.setText(""); // Los grupos no tienen saludo
        } else {
            // Si es otro tipo de contacto (no debería ocurrir)
            lblNombre.setText(contacto.getNombre());
            lblDetalle.setText("");
            lblSaludo.setText("");
            lblImagen.setIcon(null);
        }

        // Configuración de colores para selección con efecto mejorado
        if (isSelected) {
            setBackground(COLOR_SELECCION);
            setBorder(SELECCIONADO);
            lblNombre.setForeground(COLOR_PRIMARIO); // Destacar el nombre cuando está seleccionado
        } else {
            setBackground(Color.WHITE);
            setBorder(NO_SELECCIONADO);
            lblNombre.setForeground(COLOR_TEXTO);
        }

        // Crear un ligero efecto de separación entre filas
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)), // Borde inferior sutil
                isSelected ? SELECCIONADO : NO_SELECCIONADO
        ));

        setOpaque(true);
        return this;
    }
    
    /**
     * Carga una imagen desde la ruta indicada y la establece en el JLabel correspondiente
     * @param rutaImagen Ruta a la imagen a cargar
     */
    private void cargarImagen(String rutaImagen) {
        URL url = getClass().getResource(rutaImagen);
        if (url != null && rutaImagen != null && !rutaImagen.isEmpty()) {
            try {
                Image imagenOriginal = ImageIO.read(url);
                int dimension = 50;
                Image imagenEscalada = imagenOriginal.getScaledInstance(dimension, dimension, Image.SCALE_SMOOTH);
                ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);        
                lblImagen.setIcon(iconoEscalado);
            } catch (IOException e) {
                e.printStackTrace();
                lblImagen.setIcon(null);
            }
        } else {
            System.err.println("No se pudo cargar la imagen: " + rutaImagen);
            // Establecer un icono predeterminado o nulo cuando no hay imagen
            lblImagen.setIcon(null);
        }
    }
}