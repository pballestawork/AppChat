package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import dominio.modelo.Usuario;
import utils.ChatControllerStub;
import utils.Descuento;
import utils.DescuentoFactory;

/**
 * Dialog that allows users to upgrade to premium with discount selection.
 */
public class PremiumDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    
    // Constantes para la interfaz (coinciden con las de MainView)
    private static final Color COLOR_PRIMARIO = new Color(25, 118, 210);
    private static final Color COLOR_SECUNDARIO = new Color(239, 246, 255);
    private static final Color COLOR_TEXTO = new Color(33, 33, 33);
    private static final Color COLOR_FONDO = new Color(250, 250, 250);
    private static final Color COLOR_ORO = new Color(255, 215, 0);
    private static final Font FUENTE_TITULO = new Font("Arial", Font.BOLD, 20);
    private static final Font FUENTE_SUBTITULO = new Font("Arial", Font.BOLD, 16);
    private static final Font FUENTE_LABEL = new Font("Arial", Font.BOLD, 14);
    private static final Font FUENTE_NORMAL = new Font("Arial", Font.PLAIN, 14);
    private static final Font FUENTE_BUTTON = new Font("Arial", Font.BOLD, 13);
    
    // Constantes específicas
    private static final double PRECIO_PREMIUM = 9.99;
    
    // Componentes de la interfaz
    private JComboBox<String> comboDescuentos;
    private JLabel lblPrecioFinal;
    private JLabel lblDescuentoAplicado;
    private Descuento descuentoSeleccionado;
    private final DecimalFormat formatoMoneda = new DecimalFormat("0.00 €");
    
    // Controlador
    private ChatControllerStub controlador;
    private Usuario usuario;
    
    /**
     * Creates a new premium upgrade dialog.
     * 
     * @param parent The parent frame
     * @param usuario The current user
     */
    public PremiumDialog(JFrame parent, Usuario usuario) {
        super(parent, "Actualizar a Premium", true);
        this.controlador = ChatControllerStub.getUnicaInstancia();
        this.usuario = usuario;
        initComponents();
        setLocationRelativeTo(parent);
    }
    
    /**
     * Initializes the dialog components.
     */
    private void initComponents() {
        setResizable(false);
        setSize(450, 500);
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout());
        
        // Panel superior con título y descripción
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBackground(COLOR_ORO);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Título
        JLabel lblTitulo = new JLabel("¡Actualiza a Premium!");
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
        
        // Subtítulo
        JLabel lblSubtitulo = new JLabel("Disfruta de ventajas exclusivas");
        lblSubtitulo.setFont(FUENTE_SUBTITULO);
        lblSubtitulo.setForeground(COLOR_TEXTO);
        lblSubtitulo.setAlignmentX(CENTER_ALIGNMENT);
        
        panelSuperior.add(lblTitulo);
        panelSuperior.add(Box.createRigidArea(new Dimension(0, 5)));
        panelSuperior.add(lblSubtitulo);
        
        add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central con beneficios y selección de descuento
        JPanel panelCentral = new JPanel(new BorderLayout(0, 15));
        panelCentral.setBackground(COLOR_FONDO);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Panel de beneficios premium
        JPanel panelBeneficios = new JPanel();
        panelBeneficios.setLayout(new BorderLayout());
        panelBeneficios.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARIO),
                "Beneficios Premium"
        ));
        panelBeneficios.setBackground(COLOR_FONDO);
        
        // Beneficio principal: Generar PDF con contactos y grupos
        JPanel panelBeneficioPDF = new JPanel();
        panelBeneficioPDF.setLayout(new BoxLayout(panelBeneficioPDF, BoxLayout.Y_AXIS));
        panelBeneficioPDF.setBackground(COLOR_FONDO);
        panelBeneficioPDF.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblBeneficioPDF = new JLabel("- Generar documento PDF con contactos y grupos", JLabel.LEFT);
        lblBeneficioPDF.setFont(FUENTE_NORMAL);
        lblBeneficioPDF.setForeground(COLOR_TEXTO);
        lblBeneficioPDF.setAlignmentX(LEFT_ALIGNMENT);
        
        JLabel lblDescripcionPDF = new JLabel(
                "<html>Genera un PDF con tus contactos, grupos y sus mensajes.<br>"
                + "Incluye información detallada de cada conversación.</html>", 
                JLabel.LEFT);
        lblDescripcionPDF.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDescripcionPDF.setForeground(COLOR_TEXTO);
        lblDescripcionPDF.setAlignmentX(LEFT_ALIGNMENT);
        
        panelBeneficioPDF.add(lblBeneficioPDF);
        panelBeneficioPDF.add(Box.createRigidArea(new Dimension(0, 10)));
        panelBeneficioPDF.add(lblDescripcionPDF);
        
        panelBeneficios.add(panelBeneficioPDF, BorderLayout.CENTER);
        
        panelCentral.add(panelBeneficios, BorderLayout.NORTH);
        
        // Panel de selección de descuento
        JPanel panelDescuento = new JPanel(new GridBagLayout());
        panelDescuento.setBackground(COLOR_FONDO);
        panelDescuento.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(COLOR_PRIMARIO),
                        "Seleccionar descuento"),
                BorderFactory.createEmptyBorder(10, 5, 10, 5)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(8, 10, 8, 5);
        gbc.weightx = 0.4;
        
        // Precio original
        JLabel lblPrecioOriginal = new JLabel("Precio original:");
        lblPrecioOriginal.setFont(FUENTE_LABEL);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelDescuento.add(lblPrecioOriginal, gbc);
        
        JLabel lblPrecioValor = new JLabel(formatoMoneda.format(PRECIO_PREMIUM));
        lblPrecioValor.setFont(FUENTE_NORMAL);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.6;
        panelDescuento.add(lblPrecioValor, gbc);
        
        // Selector de descuento
        JLabel lblDescuento = new JLabel("Descuento:");
        lblDescuento.setFont(FUENTE_LABEL);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.4;
        panelDescuento.add(lblDescuento, gbc);
        
        comboDescuentos = new JComboBox<>(DescuentoFactory.getDescuentosDisponibles());
        comboDescuentos.setFont(FUENTE_NORMAL);
        comboDescuentos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarDescuento();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelDescuento.add(comboDescuentos, gbc);
        
        // Descuento aplicado
        JLabel lblDescuentoLabel = new JLabel("Descuento aplicado:");
        lblDescuentoLabel.setFont(FUENTE_LABEL);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.4;
        panelDescuento.add(lblDescuentoLabel, gbc);
        
        lblDescuentoAplicado = new JLabel(formatoMoneda.format(0.0));
        lblDescuentoAplicado.setFont(FUENTE_NORMAL);
        lblDescuentoAplicado.setForeground(new Color(46, 125, 50)); // Verde
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.6;
        panelDescuento.add(lblDescuentoAplicado, gbc);
        
        // Precio final
        JLabel lblPrecioFinalLabel = new JLabel("Precio final:");
        lblPrecioFinalLabel.setFont(FUENTE_LABEL);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.4;
        panelDescuento.add(lblPrecioFinalLabel, gbc);
        
        lblPrecioFinal = new JLabel(formatoMoneda.format(PRECIO_PREMIUM));
        lblPrecioFinal.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 0.6;
        panelDescuento.add(lblPrecioFinal, gbc);
        
        panelCentral.add(panelDescuento, BorderLayout.CENTER);
        
        add(panelCentral, BorderLayout.CENTER);
        
        // Panel inferior con botones
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelInferior.setBackground(COLOR_FONDO);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(0, 30, 20, 30));
        
        // Botón Cancelar
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(FUENTE_BUTTON);
        btnCancelar.setBackground(COLOR_SECUNDARIO);
        btnCancelar.setForeground(COLOR_TEXTO);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_TEXTO, 1),
                BorderFactory.createEmptyBorder(7, 15, 7, 15)
        ));
        btnCancelar.addActionListener(e -> dispose());
        
        // Botón Aceptar
        JButton btnAceptar = new JButton("Actualizar a Premium");
        btnAceptar.setFont(FUENTE_BUTTON);
        btnAceptar.setBackground(COLOR_ORO);
        btnAceptar.setForeground(COLOR_TEXTO);
        btnAceptar.setFocusPainted(false);
        btnAceptar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_TEXTO, 1),
                BorderFactory.createEmptyBorder(7, 15, 7, 15)
        ));
        btnAceptar.addActionListener(e -> realizarActualizacionPremium());
        
        panelInferior.add(btnCancelar);
        panelInferior.add(btnAceptar);
        
        add(panelInferior, BorderLayout.SOUTH);
        
        // Inicializar el descuento
        actualizarDescuento();
    }
    
    /**
     * Updates the discount information based on the selected discount.
     */
    private void actualizarDescuento() {
        String descuentoNombre = (String) comboDescuentos.getSelectedItem();
        descuentoSeleccionado = DescuentoFactory.createDescuento(descuentoNombre);
        
        double descuentoAplicado = descuentoSeleccionado.calcularDescuento(PRECIO_PREMIUM);
        double precioFinal = descuentoSeleccionado.calcularPrecioFinal(PRECIO_PREMIUM);
        
        lblDescuentoAplicado.setText(formatoMoneda.format(descuentoAplicado));
        lblPrecioFinal.setText(formatoMoneda.format(precioFinal));
    }
    
    /**
     * Performs the premium upgrade operation and offers to generate a PDF.
     */
    private void realizarActualizacionPremium() {
        // Aquí se realizaría la lógica de pago real en una implementación completa
        
        // Actualizar al usuario a premium
        boolean resultado = controlador.actualizarUsuarioAPremium();
        
        if (resultado) {
            int opcion = JOptionPane.showConfirmDialog(this,
                    "¡Felicidades! Tu cuenta ha sido actualizada a Premium.\nDescuento aplicado: " + 
                            descuentoSeleccionado.getNombre() + "\nPrecio final: " + lblPrecioFinal.getText() + 
                            "\n\n¿Deseas generar ahora un informe PDF con tus contactos?",
                    "Actualización Exitosa",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            
            if (opcion == JOptionPane.YES_OPTION) {
                generarPDF();
            } else {
                dispose();
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Ha ocurrido un error al actualizar a Premium.\nPor favor, inténtalo de nuevo más tarde.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Generates a PDF report with contacts and groups information.
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