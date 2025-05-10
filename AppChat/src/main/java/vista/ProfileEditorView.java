package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import dominio.controlador.ChatController;
import dominio.controlador.ChatControllerException;
import dominio.modelo.Usuario;
import utils.Utils;

/**
 * Diálogo para editar el perfil del usuario (imagen y saludo)
 */
public class ProfileEditorView extends JDialog {

    private static final long serialVersionUID = 1L;
    private static final Color COLOR_PRIMARIO = new Color(25, 118, 210);
    private static final Color COLOR_SECUNDARIO = new Color(239, 246, 255);
    private static final Color COLOR_TEXTO = new Color(33, 33, 33);
    private static final Color COLOR_FONDO = new Color(250, 250, 250);
    private static final Font FUENTE_TITULO = new Font("Arial", Font.BOLD, 24);
    private static final Font FUENTE_SUBTITULO = new Font("Arial", Font.BOLD, 18);
    private static final Font FUENTE_LABEL = new Font("Arial", Font.BOLD, 14);
    private static final Font FUENTE_NORMAL = new Font("Arial", Font.PLAIN, 14);
    private static final Font FUENTE_BUTTON = new Font("Arial", Font.BOLD, 13);

    private JPanel contentPane;
    private JLabel lblImagen;
    private JEditorPane txtSaludo;
    private File archivoImagen;
    private String rutaFotoPerfil;
    private Usuario usuario;
    private ChatController controlador;
    private boolean cambiosRealizados = false;
    
    /**
     * Constructor con el frame padre y el usuario a editar
     */
    public ProfileEditorView(JFrame parent, Usuario usuario) {
        super(parent, "Editar Perfil", true);
        this.usuario = usuario;
        this.rutaFotoPerfil = usuario.getFotoPerfil();
        try {
			this.controlador = ChatController.getUnicaInstancia();
		} catch (ChatControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // En caso de error, continuamos con el Look and Feel por defecto
        }
        initComponents();
    }
    
    /**
     * Inicializa los componentes del diálogo
     */
    private void initComponents() {
        // Configurar la ventana basada en la resolución de la pantalla
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = Math.min(650, (int)(screenSize.width * 0.6));
        int height = Math.min(750, (int)(screenSize.height * 0.8));
        
        setResizable(true);
        setBounds(100, 100, width, height);
        setMinimumSize(new Dimension(500, 600));
        getContentPane().setBackground(COLOR_FONDO);
        getContentPane().setLayout(new BorderLayout());
        
        // Panel principal sin scroll
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_FONDO);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        
        // Panel de contenido
        contentPane = new JPanel();
        contentPane.setBackground(COLOR_FONDO);
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        mainPanel.add(contentPane, BorderLayout.CENTER);
        
        // Título
        JLabel lblTitulo = new JLabel("Mi Perfil");
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(COLOR_PRIMARIO);
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        contentPane.add(lblTitulo);
        
        // Nombre del usuario
        JLabel lblNombre = new JLabel(usuario.getNombre());
        lblNombre.setFont(FUENTE_SUBTITULO);
        lblNombre.setAlignmentX(CENTER_ALIGNMENT);
        lblNombre.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        contentPane.add(lblNombre);
        
        // Panel para la imagen con tamaño ajustable
        JPanel panelImagen = new JPanel(new BorderLayout());
        panelImagen.setBackground(COLOR_FONDO);
        panelImagen.setMaximumSize(new Dimension(width, 400));
        panelImagen.setPreferredSize(new Dimension(width - 100, 350));
        
        // Contenedor para centrar la imagen
        JPanel contenedorImagen = new JPanel(new FlowLayout(FlowLayout.CENTER));
        contenedorImagen.setBackground(COLOR_FONDO);
        
        // Label para mostrar la imagen
        lblImagen = new JLabel();
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagen.setPreferredSize(new Dimension(320, 300));
        lblImagen.setMinimumSize(new Dimension(250, 230));
        
        // Borde para la imagen que sea visible pero sutil
        lblImagen.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(COLOR_PRIMARIO.getRed(), COLOR_PRIMARIO.getGreen(), COLOR_PRIMARIO.getBlue(), 150), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Cargar la imagen actual
        actualizarImagenPerfil();
        
        contenedorImagen.add(lblImagen);
        panelImagen.add(contenedorImagen, BorderLayout.CENTER);
        
        // Añadir panel de imagen al contenedor principal
        contentPane.add(panelImagen);
        contentPane.add(Box.createRigidArea(new Dimension(0, 5))); // Reducido de 15 a 5
        
        // Botón para cambiar la imagen
        JPanel panelBotonImagen = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotonImagen.setBackground(COLOR_FONDO);
        
        JButton btnCambiarImagen = new JButton("Cambiar Imagen");
        btnCambiarImagen.setFont(FUENTE_BUTTON);
        btnCambiarImagen.setBackground(COLOR_SECUNDARIO);
        btnCambiarImagen.setForeground(COLOR_PRIMARIO);
        btnCambiarImagen.setFocusPainted(false);
        btnCambiarImagen.setContentAreaFilled(true);
        btnCambiarImagen.setBorderPainted(true);
        btnCambiarImagen.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARIO),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        btnCambiarImagen.addActionListener(e -> {
            AgregarFotoPerfilView selectorImagen = new AgregarFotoPerfilView(this);
            List<File> imagenes = selectorImagen.showDialog();
            if (imagenes != null && !imagenes.isEmpty()) {
                archivoImagen = imagenes.get(0);
                try {
                    rutaFotoPerfil = Utils.getRutaResourceFromFile(archivoImagen);
                    actualizarImagenPerfil();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, 
                            "Error al cargar la imagen: " + ex.getMessage(), 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
        
        panelBotonImagen.add(btnCambiarImagen);
        contentPane.add(panelBotonImagen);
        contentPane.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Panel para el saludo
        JPanel panelSaludo = new JPanel(new BorderLayout(0, 10));
        panelSaludo.setBackground(COLOR_FONDO);
        panelSaludo.setMaximumSize(new Dimension(width - 80, 150));
        
        JLabel lblSaludoTitulo = new JLabel("Saludo de perfil:");
        lblSaludoTitulo.setFont(FUENTE_LABEL);
        panelSaludo.add(lblSaludoTitulo, BorderLayout.NORTH);
        
        txtSaludo = new JEditorPane();
        txtSaludo.setFont(FUENTE_NORMAL);
        txtSaludo.setText(usuario.getSaludo());
        txtSaludo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        panelSaludo.add(txtSaludo, BorderLayout.CENTER);
        
        contentPane.add(panelSaludo);
        contentPane.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Panel de botones (Guardar y Cancelar)
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBotones.setBackground(COLOR_FONDO);
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(FUENTE_BUTTON);
        btnCancelar.setBackground(COLOR_SECUNDARIO);
        btnCancelar.setForeground(COLOR_TEXTO);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setContentAreaFilled(true);
        btnCancelar.setBorderPainted(true);
        btnCancelar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_TEXTO),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        btnCancelar.addActionListener(e -> dispose());
        
        JButton btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setFont(FUENTE_BUTTON);
        btnGuardar.setBackground(COLOR_SECUNDARIO);
        btnGuardar.setForeground(COLOR_PRIMARIO);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setContentAreaFilled(true);
        btnGuardar.setBorderPainted(true);
        btnGuardar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARIO.darker()),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        btnGuardar.addActionListener(e -> guardarCambios());
        
        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);
        contentPane.add(panelBotones);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Centrar diálogo en la pantalla
        setLocationRelativeTo(getOwner());
    }
    
    /**
     * Guarda los cambios realizados en el perfil
     */
    private void guardarCambios() {
        try {
            boolean fotoActualizada = false;
            boolean saludoActualizado = false;
            
            // Actualizar la foto de perfil si ha cambiado
            String nuevoSaludo = txtSaludo.getText().trim();
            if (!rutaFotoPerfil.equals(usuario.getFotoPerfil())) {
                fotoActualizada = true;
            }
            if (!nuevoSaludo.equals(usuario.getSaludo())) {
            	saludoActualizado = true;
            }

            // Marcar que se realizaron cambios si se actualizó algún campo
            cambiosRealizados = fotoActualizada || saludoActualizado;
            
            if (cambiosRealizados) {
            	controlador.actualizarPerfil(nuevoSaludo, rutaFotoPerfil);
                JOptionPane.showMessageDialog(this, 
                        "Perfil actualizado correctamente", 
                        "Éxito", 
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                        "No se detectaron cambios en el perfil", 
                        "Información", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
            
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                    "Error al guardar el perfil: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    /**
     * Actualiza la imagen del perfil en la interfaz
     */
    private void actualizarImagenPerfil() {
        try {
            // Cargar y mostrar la imagen actual del perfil
            ImageIcon iconoImagen = new ImageIcon(getClass().getResource(rutaFotoPerfil));
            
            // Calcular dimensiones para que se mantenga la proporción pero quepa en el espacio
            int anchoOriginal = iconoImagen.getIconWidth();
            int altoOriginal = iconoImagen.getIconHeight();
            
            // Tamaño máximo para la imagen (más grande que antes)
            int anchoMax = 300;
            int altoMax = 280;
            
            // Si la imagen es muy pequeña, no escalarla hacia arriba
            if (anchoOriginal <= anchoMax && altoOriginal <= altoMax) {
                lblImagen.setIcon(iconoImagen);
                lblImagen.setText("");
                return;
            }
            
            // Calcular escala manteniendo proporción
            double escalaAncho = (double) anchoMax / anchoOriginal;
            double escalaAlto = (double) altoMax / altoOriginal;
            double escala = Math.min(escalaAncho, escalaAlto);
            
            // Nuevas dimensiones
            int anchoNuevo = (int) (anchoOriginal * escala);
            int altoNuevo = (int) (altoOriginal * escala);
            
            // Escalar la imagen con alta calidad
            Image imagenEscalada = iconoImagen.getImage().getScaledInstance(
                    anchoNuevo, altoNuevo, Image.SCALE_SMOOTH);
            
            // Mostrar la imagen
            lblImagen.setIcon(new ImageIcon(imagenEscalada));
            lblImagen.setText("");
        } catch (Exception ex) {
            lblImagen.setIcon(null);
            lblImagen.setText("No se pudo cargar la imagen");
            ex.printStackTrace();
        }
    }
    
    /**
     * Indica si se realizaron cambios que requieren actualizar la interfaz
     * @return true si se realizaron cambios
     */
    public boolean isCambiosRealizados() {
        return cambiosRealizados;
    }
}