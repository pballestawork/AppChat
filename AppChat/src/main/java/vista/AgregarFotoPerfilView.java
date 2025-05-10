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
import utils.EstiloApp;
import utils.Utils;

public class AgregarFotoPerfilView extends JDialog {

    private static final long serialVersionUID = 1L;
    
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
        getContentPane().setBackground(EstiloApp.COLOR_FONDO);
        
        // Panel principal
        contentPane = new JPanel();
        contentPane.setBackground(EstiloApp.COLOR_FONDO);
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        getContentPane().add(contentPane, BorderLayout.CENTER);
        
        lblArchivoSubido = new JLabel();
        lblArchivoSubido.setVisible(false);
        
        // Título
        JLabel lblTitulo = new JLabel("Seleccionar imagen");
        lblTitulo.setFont(EstiloApp.FUENTE_TITULO_MEDIO);
        lblTitulo.setForeground(EstiloApp.COLOR_PRIMARIO);
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        contentPane.add(lblTitulo);
        
        // Instrucciones
        JLabel lblInstrucciones = new JLabel("Puedes arrastrar una imagen aquí, seleccionarla desde tu ordenador o cargarla desde URL");
        lblInstrucciones.setFont(EstiloApp.FUENTE_NORMAL);
        lblInstrucciones.setAlignmentX(CENTER_ALIGNMENT);
        lblInstrucciones.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        contentPane.add(lblInstrucciones);
        
        // Panel para la previsualización de la imagen
        JPanel panelImagen = new JPanel(new BorderLayout());
        panelImagen.setBackground(EstiloApp.COLOR_FONDO);
        panelImagen.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloApp.COLOR_PRIMARIO, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panelImagen.setPreferredSize(new Dimension(300, 220));
        panelImagen.setMaximumSize(new Dimension(500, 220));
        
        // Etiqueta para mostrar la imagen
        imagenLabel = new JLabel("Arrastra una imagen aquí");
        imagenLabel.setFont(EstiloApp.FUENTE_NORMAL);
        imagenLabel.setForeground(Color.GRAY);
        imagenLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imagenLabel.setVerticalAlignment(SwingConstants.CENTER);
        panelImagen.add(imagenLabel, BorderLayout.CENTER);
        
        // Añadir panel a un contenedor para centrarlo
        JPanel panelCentrador = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelCentrador.setBackground(EstiloApp.COLOR_FONDO);
        panelCentrador.add(panelImagen);
        contentPane.add(panelCentrador);
        
        // Configurar el DropTarget para arrastrar imágenes
        panelImagen.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);

                    if (!droppedFiles.isEmpty()) {
                        File archivo = droppedFiles.get(0);
                        
                        // Verificar si el archivo está dentro de src/main/resources
                        // Si no está, copiarlo a la carpeta adecuada
                        String rutaAbsoluta = archivo.getAbsolutePath();
                        if (!rutaAbsoluta.contains("src" + File.separator + "main" + File.separator + "resources")) {
                            // Crear el directorio FotosPerfil si no existe
                            Path directorioFotos = Paths.get("src/main/resources/FotosPerfil");
                            if (!Files.exists(directorioFotos)) {
                                Files.createDirectories(directorioFotos);
                            }
                            
                            // Generar nombre único para la imagen
                            String extension = archivo.getName().substring(
                                archivo.getName().lastIndexOf('.'));
                            String nuevoNombre = "perfil_drop_" + UUID.randomUUID().toString().substring(0, 8) + extension;
                            
                            // Copiar archivo a resources
                            File archivoDestino = new File(directorioFotos.toFile(), nuevoNombre);
                            Files.copy(archivo.toPath(), archivoDestino.toPath());
                            
                            // Usar el archivo copiado
                            archivo = archivoDestino;
                        }
                        
                        archivosSubidos.clear();
                        archivosSubidos.add(archivo);
                        
                        // Convertir la ruta absoluta a ruta relativa usando Utils
                        String rutaRelativa = Utils.getRutaResourceFromFile(archivo);
                        
                        // Cargar la imagen directamente desde el archivo (más simple y directo)
                        try {
                            ImageIcon icon = new ImageIcon(archivo.getAbsolutePath());
                            Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                            imagenLabel.setIcon(new ImageIcon(img));
                            imagenLabel.setText("");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(AgregarFotoPerfilView.this, 
                                "No se pudo mostrar la vista previa de la imagen.", 
                                "Advertencia", 
                                JOptionPane.WARNING_MESSAGE);
                        }
                        
                        if (lblArchivoSubido != null) {
                            lblArchivoSubido.setText(archivo.getAbsolutePath());
                            lblArchivoSubido.setVisible(true);
                        }
                        
                        // Limpiar URL si existía alguna
                        imagenUrl = null;
                        txtUrl.setText("");
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
        
        
        // Panel para el botón de selección y URL
        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSeleccion.setBackground(EstiloApp.COLOR_FONDO);
        panelSeleccion.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        // Botón para seleccionar imagen del ordenador
        JButton botonElegir = new JButton("Seleccionar de tu ordenador");
        botonElegir.setFont(EstiloApp.FUENTE_BUTTON);
        botonElegir.setBackground(EstiloApp.COLOR_SECUNDARIO);
        botonElegir.setForeground(EstiloApp.COLOR_PRIMARIO);
        botonElegir.setFocusPainted(false);
        botonElegir.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloApp.COLOR_PRIMARIO),
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
                
                try {
                    // Verificar si el archivo está dentro de src/main/resources
                    // Si no está, copiarlo a la carpeta adecuada
                    String rutaAbsoluta = archivoSeleccionado.getAbsolutePath();
                    if (!rutaAbsoluta.contains("src" + File.separator + "main" + File.separator + "resources")) {
                        // Crear el directorio FotosPerfil si no existe
                        Path directorioFotos = Paths.get("src/main/resources/FotosPerfil");
                        if (!Files.exists(directorioFotos)) {
                            Files.createDirectories(directorioFotos);
                        }
                        
                        // Generar nombre único para la imagen
                        String extension = archivoSeleccionado.getName().substring(
                            archivoSeleccionado.getName().lastIndexOf('.'));
                        String nuevoNombre = "perfil_local_" + UUID.randomUUID().toString().substring(0, 8) + extension;
                        
                        // Copiar archivo a resources
                        File archivoDestino = new File(directorioFotos.toFile(), nuevoNombre);
                        Files.copy(archivoSeleccionado.toPath(), archivoDestino.toPath());
                        
                        // Usar el archivo copiado
                        archivoSeleccionado = archivoDestino;
                    }
                    
                    archivosSubidos.clear(); // Limpia la lista actual
                    archivosSubidos.add(archivoSeleccionado); // Agrega el archivo seleccionado a la lista
                    
                    // Convertir la ruta absoluta a ruta relativa usando Utils
                    String rutaRelativa = Utils.getRutaResourceFromFile(archivoSeleccionado);
                    
                    // Cargar la imagen directamente desde el archivo (más simple y directo)
                    try {
                        ImageIcon icon = new ImageIcon(archivoSeleccionado.getAbsolutePath());
                        Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                        imagenLabel.setIcon(new ImageIcon(img));
                        imagenLabel.setText("");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, 
                            "No se pudo mostrar la vista previa de la imagen.", 
                            "Advertencia", 
                            JOptionPane.WARNING_MESSAGE);
                    }

                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, 
                        "Error al cargar la imagen: " + ex.getMessage(), 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
                
                // Limpiar URL si existía alguna
                imagenUrl = null;
                txtUrl.setText("");
            }
        });
        
        panelSeleccion.add(botonElegir);
        
        // Panel para la URL
        JPanel panelUrl = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelUrl.setBackground(EstiloApp.COLOR_FONDO);
        panelUrl.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Campo de texto para la URL
        txtUrl = new JTextField(20);
        txtUrl.setFont(EstiloApp.FUENTE_NORMAL);
        txtUrl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloApp.COLOR_PRIMARIO),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtUrl.setToolTipText("Introduce la URL de una imagen");
        
        // Botón para cargar desde URL
        JButton btnCargarUrl = new JButton("Cargar URL");
        btnCargarUrl.setFont(EstiloApp.FUENTE_BUTTON);
        btnCargarUrl.setBackground(EstiloApp.COLOR_SECUNDARIO);
        btnCargarUrl.setForeground(EstiloApp.COLOR_PRIMARIO);
        btnCargarUrl.setFocusPainted(false);
        btnCargarUrl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloApp.COLOR_PRIMARIO),
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
                
                // Usar el método en Utils para descargar la imagen
                File archivoImagenLocal = Utils.descargarImagenDesdeURL(url, "src/main/resources/FotosPerfil");
                
                if (archivoImagenLocal != null && archivoImagenLocal.exists()) {
                    // Añadir el archivo descargado a la lista
                    archivosSubidos.add(archivoImagenLocal);
                    
                    // Guardar la URL para usarla después (si fuera necesario)
                    imagenUrl = url;
                    
                    // Convertir a ruta relativa para resources
                    String rutaRelativa = Utils.getRutaResourceFromFile(archivoImagenLocal);
                    
                    // Cargar la imagen directamente desde el archivo (más simple y directo)
                    try {
                        ImageIcon icon = new ImageIcon(archivoImagenLocal.getAbsolutePath());
                        Image imgEscalada = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                        imagenLabel.setIcon(new ImageIcon(imgEscalada));
                        imagenLabel.setText("");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, 
                            "No se pudo mostrar la vista previa de la imagen.", 
                            "Advertencia", 
                            JOptionPane.WARNING_MESSAGE);
                    }

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
        panelBotones.setBackground(EstiloApp.COLOR_FONDO);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Botón Aceptar
        btnAceptar = new JButton("Aceptar");
        btnAceptar.setFont(EstiloApp.FUENTE_BUTTON);
        btnAceptar.setBackground(EstiloApp.COLOR_PRIMARIO);
        btnAceptar.setForeground(EstiloApp.COLOR_PRIMARIO);
        btnAceptar.setFocusPainted(false);
        btnAceptar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloApp.COLOR_PRIMARIO),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        
        // Botón Cancelar
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(EstiloApp.FUENTE_BUTTON);
        btnCancelar.setBackground(EstiloApp.COLOR_SECUNDARIO);
        btnCancelar.setForeground(EstiloApp.COLOR_PRIMARIO);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloApp.COLOR_PRIMARIO),
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
        return Utils.descargarImagenDesdeURL(urlStr, "src/main/resources/FotosPerfil");
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
