package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import dominio.controlador.ChatController;
import dominio.controlador.ChatControllerException;
import dominio.modelo.Usuario;
import utils.EstiloApp;


/**
 * Diálogo que muestra las opciones disponibles para usuarios Premium.
 */
public class PremiumOptionsDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    
    // Controlador
    private ChatController controlador;
    private Usuario usuario;
    
    /**
     * Crea un nuevo diálogo de opciones Premium.
     * 
     * @param parent Frame padre
     * @param usuario Usuario actual
     */
    public PremiumOptionsDialog(JFrame parent, Usuario usuario) {
        super(parent, "Opciones Premium", true);
        try {
			this.controlador = ChatController.getUnicaInstancia();
		} catch (ChatControllerException e) {
			e.printStackTrace();
		}
        this.usuario = usuario;
        initComponents();
        setLocationRelativeTo(parent);
    }
    
    /**
     * Inicializa los componentes del diálogo.
     */
    private void initComponents() {
        setResizable(false);
        setSize(500, 420);
        getContentPane().setBackground(EstiloApp.COLOR_FONDO);
        setLayout(new BorderLayout());
        
        // Panel superior con título
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBackground(EstiloApp.COLOR_ORO);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Título
        JLabel lblTitulo = new JLabel("Acceso Premium");
        lblTitulo.setFont(EstiloApp.FUENTE_TITULO);
        lblTitulo.setForeground(EstiloApp.COLOR_TEXTO);
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
        
        // Subtítulo
        JLabel lblSubtitulo = new JLabel("¡Gracias por ser usuario Premium!");
        lblSubtitulo.setFont(EstiloApp.FUENTE_SUBTITULO);
        lblSubtitulo.setForeground(EstiloApp.COLOR_TEXTO);
        lblSubtitulo.setAlignmentX(CENTER_ALIGNMENT);
        
        panelSuperior.add(lblTitulo);
        panelSuperior.add(Box.createRigidArea(new Dimension(0, 5)));
        panelSuperior.add(lblSubtitulo);
        
        add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central con opciones
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBackground(EstiloApp.COLOR_FONDO);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Opción de generar PDF
        JPanel panelOpcionPDF = new JPanel(new BorderLayout(10, 0));
        panelOpcionPDF.setBackground(EstiloApp.COLOR_FONDO);
        panelOpcionPDF.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloApp.COLOR_PRIMARIO, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panelOpcionPDF.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        
        JPanel panelDescripcionPDF = new JPanel();
        panelDescripcionPDF.setLayout(new BoxLayout(panelDescripcionPDF, BoxLayout.Y_AXIS));
        panelDescripcionPDF.setBackground(EstiloApp.COLOR_FONDO);
        
        JLabel lblTituloPDF = new JLabel("Generar PDF de contactos y mensajes");
        lblTituloPDF.setFont(EstiloApp.FUENTE_NORMAL);
        lblTituloPDF.setForeground(EstiloApp.COLOR_PRIMARIO);
        
        JLabel lblDescripcionPDF = new JLabel(
                "<html>Genera un documento PDF con todos tus contactos,<br>" +
                "grupos y mensajes organizados en un formato elegante.<br>" +
                "Ideal para tener un respaldo de tus conversaciones.</html>");
        lblDescripcionPDF.setFont(EstiloApp.FUENTE_NORMAL);
        
        panelDescripcionPDF.add(lblTituloPDF);
        panelDescripcionPDF.add(Box.createRigidArea(new Dimension(0, 5)));
        panelDescripcionPDF.add(lblDescripcionPDF);
        
        JButton btnGenerarPDF = new JButton("Generar PDF");
        btnGenerarPDF.setFont(EstiloApp.FUENTE_BUTTON);
        btnGenerarPDF.setBackground(EstiloApp.COLOR_PRIMARIO);
        btnGenerarPDF.setForeground(EstiloApp.COLOR_PRIMARIO);
        btnGenerarPDF.setFocusPainted(false);
        btnGenerarPDF.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloApp.COLOR_PRIMARIO),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        btnGenerarPDF.setPreferredSize(new Dimension(120, 40));
        btnGenerarPDF.addActionListener(e -> generarPDF());
        
        panelOpcionPDF.add(panelDescripcionPDF, BorderLayout.CENTER);
        panelOpcionPDF.add(btnGenerarPDF, BorderLayout.EAST);
        
        panelCentral.add(panelOpcionPDF);
        
        // Espacio para futuras opciones Premium
        panelCentral.add(Box.createRigidArea(new Dimension(0, 20)));
        
        add(panelCentral, BorderLayout.CENTER);
        
        // Panel inferior con botón de cerrar
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelInferior.setBackground(EstiloApp.COLOR_FONDO);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(0, 30, 20, 30));
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(EstiloApp.FUENTE_BUTTON);
        btnCerrar.setBackground(EstiloApp.COLOR_SECUNDARIO);
        btnCerrar.setForeground(EstiloApp.COLOR_TEXTO);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloApp.COLOR_TEXTO, 1),
                BorderFactory.createEmptyBorder(7, 15, 7, 15)
        ));
        btnCerrar.addActionListener(e -> dispose());
        
        panelInferior.add(btnCerrar);
        
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    /**
     * Genera un PDF con la información de contactos y mensajes.
     */
    private void generarPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar informe de contactos");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos PDF (*.pdf)", "pdf"));
        fileChooser.setSelectedFile(new File("Informe_Contactos.pdf"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            
            // Si el archivo no termina en .pdf, añadimos la extensión
            if (!fileToSave.getAbsolutePath().toLowerCase().endsWith(".pdf")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".pdf");
            }
            
            // Comprobar si el archivo ya existe
            if (fileToSave.exists()) {
                int overwrite = JOptionPane.showConfirmDialog(this,
                        "El archivo ya existe. ¿Desea sobrescribirlo?",
                        "Confirmar sobrescritura",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                
                if (overwrite != JOptionPane.YES_OPTION) {
                    generarPDF(); // Volver a mostrar el diálogo
                    return;
                }
            }
            
            boolean success = controlador.generarInformePDF(fileToSave.getAbsolutePath());
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                        "El informe PDF ha sido generado correctamente en:\n" + fileToSave.getAbsolutePath(),
                        "Informe generado",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Ha ocurrido un error al generar el informe PDF.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}