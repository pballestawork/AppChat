package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class AgregarFotoPerfilView extends JDialog {

    private static final long serialVersionUID = 1L;
    private static final Color COLOR_PRIMARIO = new Color(25, 118, 210);
    private static final Color COLOR_SECUNDARIO = new Color(239, 246, 255);
    private static final Color COLOR_FONDO = new Color(250, 250, 250);
    private static final Font FUENTE_TITULO = new Font("Arial", Font.BOLD, 22);
    private static final Font FUENTE_NORMAL = new Font("Arial", Font.PLAIN, 14);
    private static final Font FUENTE_BUTTON = new Font("Arial", Font.BOLD, 13);
    
    private JPanel contentPane;
    private List<File> archivosSubidos = new ArrayList<File>();
    private JLabel lblArchivoSubido;
    private JButton btnAceptar;
    private JButton btnCancelar;
    private JLabel imagenLabel;
    private JTextField txtUrl;
    private String imagenUrl;

    /**
     * Constructor para usar con JFrame
     */
    public AgregarFotoPerfilView(JFrame owner) {
        super(owner, "Seleccionar imagen", true);
        initComponents();
    }
    
    /**
     * Constructor para usar con JDialog
     */
    public AgregarFotoPerfilView(JDialog owner) {
        super(owner, "Seleccionar imagen", true);
        initComponents();
    }
    
    /**
     * Inicializa los componentes de la interfaz
     */
    private void initComponents() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 500, 600); // Aumentando la altura para acomodar el nuevo campo
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_FONDO);
        
        // Panel principal
        contentPane = new JPanel();
        contentPane.setBackground(COLOR_FONDO);
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        getContentPane().add(contentPane, BorderLayout.CENTER);
        
        // Título
        JLabel lblTitulo = new JLabel("Seleccionar imagen");
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(COLOR_PRIMARIO);
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        contentPane.add(lblTitulo);
        
        // Instrucciones
        JLabel lblInstrucciones = new JLabel("Puedes arrastrar una imagen aquí, seleccionarla desde tu ordenador o cargarla desde URL");
        lblInstrucciones.setFont(FUENTE_NORMAL);
        lblInstrucciones.setAlignmentX(CENTER_ALIGNMENT);
        lblInstrucciones.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        contentPane.add(lblInstrucciones);
        
        // Panel para la previsualización de la imagen
        JPanel panelImagen = new JPanel(new BorderLayout());
        panelImagen.setBackground(COLOR_FONDO);
        panelImagen.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARIO, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panelImagen.setPreferredSize(new Dimension(300, 220));
        panelImagen.setMaximumSize(new Dimension(500, 220));
        
        // Etiqueta para mostrar la imagen
        imagenLabel = new JLabel("Arrastra una imagen aquí");
        imagenLabel.setFont(FUENTE_NORMAL);
        imagenLabel.setForeground(Color.GRAY);
        imagenLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imagenLabel.setVerticalAlignment(SwingConstants.CENTER);
        panelImagen.add(imagenLabel, BorderLayout.CENTER);
        
        // Añadir panel a un contenedor para centrarlo
        JPanel panelCentrador = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelCentrador.setBackground(COLOR_FONDO);
        panelCentrador.add(panelImagen);
        contentPane.add(panelCentrador);
        
        // Configurar el DropTarget para arrastrar imágenes
        panelImagen.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);

                    if (!droppedFiles.isEmpty()) {
                        File file = droppedFiles.get(0);
                        archivosSubidos.clear();
                        archivosSubidos.add(file);
                        
                        // Cargar la imagen en el JLabel
                        ImageIcon icon = new ImageIcon(file.getAbsolutePath());
                        Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                        imagenLabel.setIcon(new ImageIcon(img));
                        imagenLabel.setText("");
                        
                        if (lblArchivoSubido != null) {
                            lblArchivoSubido.setText(file.getAbsolutePath());
                            lblArchivoSubido.setVisible(true);
                        }
                        
                        // Limpiar URL si existía alguna
                        imagenUrl = null;
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            AgregarFotoPerfilView.this, 
                            "Error al procesar el archivo: " + ex.getMessage(), 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
                evt.dropComplete(true);
            }
        });
        
        // Etiqueta invisible para la ruta del archivo
        lblArchivoSubido = new JLabel();
        lblArchivoSubido.setVisible(false);
        contentPane.add(lblArchivoSubido);
        
        // Panel para el botón de selección y URL
        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSeleccion.setBackground(COLOR_FONDO);
        panelSeleccion.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        // Botón para seleccionar imagen del ordenador
        JButton botonElegir = new JButton("Seleccionar de tu ordenador");
        botonElegir.setFont(FUENTE_BUTTON);
        botonElegir.setBackground(COLOR_SECUNDARIO);
        botonElegir.setForeground(COLOR_PRIMARIO);
        botonElegir.setFocusPainted(false);
        botonElegir.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARIO),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        botonElegir.addActionListener(e -> {
            // Define la carpeta inicial (src/main/resources)
            File directorioInicial = new File("src/main/resources");
            
            // Configura el JFileChooser para abrir en esa carpeta
            JFileChooser fileChooser = new JFileChooser(directorioInicial);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); // Solo permite seleccionar archivos

            // Filtro para permitir solo imágenes
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Archivos de imagen", "png", "jpg", "jpeg", "gif"));

            int resultado = fileChooser.showOpenDialog(this); // Abre el diálogo de selección

            if (resultado == JFileChooser.APPROVE_OPTION) {
                // Si se selecciona un archivo
                File archivoSeleccionado = fileChooser.getSelectedFile();
                archivosSubidos.clear(); // Limpia la lista actual
                archivosSubidos.add(archivoSeleccionado); // Agrega el archivo seleccionado a la lista

                // Carga la imagen en el JLabel para previsualizarla
                ImageIcon icon = new ImageIcon(archivoSeleccionado.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                imagenLabel.setIcon(new ImageIcon(img));
                imagenLabel.setText("");
                
                // Muestra la ruta del archivo en el label (invisible)
                lblArchivoSubido.setText(archivoSeleccionado.getAbsolutePath());
                lblArchivoSubido.setVisible(true);
                
                // Limpiar URL si existía alguna
                imagenUrl = null;
                txtUrl.setText("");
            }
        });
        
        panelSeleccion.add(botonElegir);
        
        // Panel para la URL
        JPanel panelUrl = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelUrl.setBackground(COLOR_FONDO);
        panelUrl.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Campo de texto para la URL
        txtUrl = new JTextField(20);
        txtUrl.setFont(FUENTE_NORMAL);
        txtUrl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARIO),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtUrl.setToolTipText("Introduce la URL de una imagen");
        
        // Botón para cargar desde URL
        JButton btnCargarUrl = new JButton("Cargar URL");
        btnCargarUrl.setFont(FUENTE_BUTTON);
        btnCargarUrl.setBackground(COLOR_SECUNDARIO);
        btnCargarUrl.setForeground(COLOR_PRIMARIO);
        btnCargarUrl.setFocusPainted(false);
        btnCargarUrl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARIO),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        btnCargarUrl.addActionListener(e -> {
            String url = txtUrl.getText().trim();
            if (url.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, introduce una URL válida", "URL vacía", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                // Limpiar archivos subidos si había alguno
                archivosSubidos.clear();
                
                // Cargar imagen desde URL y guardarla como archivo local
                File archivoImagenLocal = descargarImagenDesdeURL(url);
                
                if (archivoImagenLocal != null && archivoImagenLocal.exists()) {
                    // Añadir el archivo descargado a la lista
                    archivosSubidos.add(archivoImagenLocal);
                    
                    // Guardar la URL para usarla después (si fuera necesario)
                    imagenUrl = url;
                    
                    // Cargar y mostrar la imagen en la interfaz
                    ImageIcon icon = new ImageIcon(archivoImagenLocal.getAbsolutePath());
                    Image imgEscalada = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    imagenLabel.setIcon(new ImageIcon(imgEscalada));
                    imagenLabel.setText("");
                    
                    // Mostrar la ruta del archivo en el label (invisible)
                    lblArchivoSubido.setText(archivoImagenLocal.getAbsolutePath());
                    lblArchivoSubido.setVisible(true);
                    
                    JOptionPane.showMessageDialog(this, 
                            "Imagen descargada correctamente", 
                            "Éxito", 
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo cargar la imagen desde la URL proporcionada", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar la imagen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        
        panelUrl.add(txtUrl);
        panelUrl.add(btnCargarUrl);
        
        contentPane.add(panelSeleccion);
        contentPane.add(panelUrl);

        // Panel para los botones Aceptar y Cancelar
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotones.setBackground(COLOR_FONDO);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Botón Aceptar
        btnAceptar = new JButton("Aceptar");
        btnAceptar.setFont(FUENTE_BUTTON);
        btnAceptar.setBackground(COLOR_PRIMARIO);
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.setFocusPainted(false);
        btnAceptar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARIO),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        
        // Botón Cancelar
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(FUENTE_BUTTON);
        btnCancelar.setBackground(COLOR_SECUNDARIO);
        btnCancelar.setForeground(COLOR_PRIMARIO);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARIO),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));

        // Acción del botón Aceptar - cierra el diálogo y retorna la lista
        btnAceptar.addActionListener(e -> dispose());

        // Acción del botón Cancelar - limpia la lista y cierra el diálogo
        btnCancelar.addActionListener(e -> {
            archivosSubidos.clear(); // Limpia la lista si se cancela
            imagenUrl = null; // Limpia la URL si se cancela
            dispose();
        });

        // Añadir los botones al panel
        panelBotones.add(btnCancelar);
        panelBotones.add(btnAceptar);
        contentPane.add(panelBotones);
        
        // Centrar el diálogo respecto a su propietario
        setLocationRelativeTo(getOwner());
    }
    
    /**
     * Descarga una imagen desde una URL y la guarda como un archivo local en la carpeta
     * src/main/resources/FotosPerfil
     * 
     * @param urlStr URL de la imagen a descargar
     * @return El archivo donde se guardó la imagen o null si hubo un error
     * @throws IOException Si ocurre un error al descargar o guardar la imagen
     */
    private File descargarImagenDesdeURL(String urlStr) throws IOException {
        // Crear directorio FotosPerfil si no existe
        Path directorioFotos = Paths.get("src/main/resources/FotosPerfil");
        if (!Files.exists(directorioFotos)) {
            Files.createDirectories(directorioFotos);
        }
        
        // Generar un nombre único para el archivo
        String nombreArchivo = "perfil_url_" + UUID.randomUUID().toString().substring(0, 8);
        
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
        File archivoLocal = new File(directorioFotos.toFile(), nombreArchivo + extension);
        
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
     * Muestra el diálogo y devuelve la lista de archivos seleccionados
     * @return Lista de archivos seleccionados (generalmente solo uno)
     */
    public List<File> showDialog() {
        this.setVisible(true);
        return archivosSubidos;
    }
    
    /**
     * Devuelve la URL de la imagen seleccionada (si se eligió una URL)
     * @return URL de la imagen o null si no se seleccionó por URL
     */
    public String getImagenUrl() {
        return imagenUrl;
    }
}
