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
import java.util.ArrayList;
import java.util.List;

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
        setBounds(100, 100, 500, 550); // Aumentando la altura de 450 a 550 píxeles
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
        JLabel lblInstrucciones = new JLabel("Puedes arrastrar una imagen aquí o seleccionarla desde tu ordenador");
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
        
        // Panel para el botón de selección
        JPanel panelBotonSeleccionar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotonSeleccionar.setBackground(COLOR_FONDO);
        panelBotonSeleccionar.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
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
            }
        });
        
        panelBotonSeleccionar.add(botonElegir);
        contentPane.add(panelBotonSeleccionar);

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
     * Muestra el diálogo y devuelve la lista de archivos seleccionados
     * @return Lista de archivos seleccionados (generalmente solo uno)
     */
    public List<File> showDialog() {
        this.setVisible(true);
        return archivosSubidos;
    }
}
