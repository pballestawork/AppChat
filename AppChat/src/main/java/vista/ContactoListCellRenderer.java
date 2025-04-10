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

import dominio.modelo.ContactoIndividual;

import java.awt.*;
import java.io.IOException;

public class ContactoListCellRenderer extends JPanel implements ListCellRenderer<ContactoIndividual> {
    // Constantes para la interfaz (coinciden con las de LoginView/RegisterView)
    private static final Color COLOR_PRIMARIO = new Color(25, 118, 210);
    private static final Color COLOR_SECUNDARIO = new Color(239, 246, 255);
    private static final Color COLOR_TEXTO = new Color(33, 33, 33);
    private static final Color COLOR_SELECCION = new Color(184, 207, 229);
    private static final Font FUENTE_NOMBRE = new Font("Arial", Font.BOLD, 14);
    private static final Font FUENTE_TELEFONO = new Font("Arial", Font.PLAIN, 12);
    private static final Font FUENTE_SALUDO = new Font("Arial", Font.ITALIC, 12);
    
	private static final Border SELECCIONADO = BorderFactory.createCompoundBorder(
	        BorderFactory.createMatteBorder(0, 0, 0, 4, COLOR_PRIMARIO),  // Barra lateral derecha
	        BorderFactory.createEmptyBorder(8, 10, 8, 10)  // Padding interior
	);
    private static final Border NO_SELECCIONADO = BorderFactory.createEmptyBorder(8, 10, 8, 10);

	private JLabel lblImagen;
	private JLabel lblNombre;
	private JLabel lblTelefono;
	private JLabel lblSaludo;
	private JPanel panelTexto;

	public ContactoListCellRenderer() {
		setLayout(new BorderLayout(10, 0)); // Espaciado horizontal entre imagen y texto
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Margen exterior

		lblImagen = new JLabel();
		lblImagen.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0)); // Margen izquierdo para la imagen
		
		lblNombre = new JLabel();
		lblNombre.setFont(FUENTE_NOMBRE);
		lblNombre.setForeground(COLOR_TEXTO);
		
		lblTelefono = new JLabel();
		lblTelefono.setFont(FUENTE_TELEFONO);
		lblTelefono.setForeground(new Color(100, 100, 100)); // Gris oscuro
		
		lblSaludo = new JLabel();
		lblSaludo.setFont(FUENTE_SALUDO);
		lblSaludo.setForeground(new Color(120, 120, 120)); // Gris medio
		
		// Panel para organizar textos con espaciado vertical
		panelTexto = new JPanel();
		panelTexto.setLayout(new GridLayout(3, 1, 0, 2));
		panelTexto.setOpaque(false);
		panelTexto.add(lblNombre);
		panelTexto.add(lblTelefono);
		panelTexto.add(lblSaludo);

		add(lblImagen, BorderLayout.WEST);  // Imagen a la izquierda
		add(panelTexto, BorderLayout.CENTER);  // Texto a la derecha
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends ContactoIndividual> listacontactos, ContactoIndividual contacto, int index,
			boolean isSelected, boolean cellHasFocus) {
		// Configuración de la imagen con borde circular
		String fotoUsuario = contacto.getUsuario().getFotoPerfil();
		URL url = getClass().getResource(fotoUsuario);
		if (url != null && !fotoUsuario.isEmpty()) {
			try {
				Image imagenOriginal = ImageIO.read(url);
				int dimension = 50;
		        Image imagenEscalada = imagenOriginal.getScaledInstance(dimension, dimension, Image.SCALE_SMOOTH);
		        ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);		
		        lblImagen.setIcon(iconoEscalado);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
		    System.err.println("No se pudo cargar la imagen: " + fotoUsuario);
		    // Establecer un icono predeterminado o nulo cuando no hay imagen
		    lblImagen.setIcon(null);
		}

		// Configuración del texto
		lblNombre.setText(contacto.getNombre());
		lblTelefono.setText("Tel: " + contacto.getUsuario().getTelefono());
		
		// Mostrar el saludo o un texto predeterminado si es nulo o vacío
		String saludo = contacto.getUsuario().getSaludo();
		lblSaludo.setText(saludo != null && !saludo.isEmpty() ? saludo : "Sin saludo");

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
}
