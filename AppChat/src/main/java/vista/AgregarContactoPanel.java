package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import dominio.controlador.ChatControllerException;
import utils.ChatControllerStub;

public class AgregarContactoPanel extends JPanel {
    // Constantes para la interfaz (coinciden con las de LoginView/RegisterView)
    private static final Color COLOR_PRIMARIO = new Color(25, 118, 210);
    private static final Color COLOR_SECUNDARIO = new Color(239, 246, 255);
    private static final Color COLOR_TEXTO = new Color(33, 33, 33);
    private static final Color COLOR_FONDO = new Color(250, 250, 250);
    private static final Font FUENTE_TITULO = new Font("Arial", Font.BOLD, 18);
    private static final Font FUENTE_LABEL = new Font("Arial", Font.BOLD, 14);
    private static final Font FUENTE_NORMAL = new Font("Arial", Font.PLAIN, 14);
    
    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JButton btnAgregar;
    private JButton btnCancelar;
    private ChatControllerStub controlador;

    public AgregarContactoPanel() {
        controlador = ChatControllerStub.getUnicaInstancia();
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(0, 15));
        setBackground(COLOR_FONDO);
        setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        
        // Panel de título
        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setBackground(COLOR_FONDO);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JLabel lblTitulo = new JLabel("Agregar Nuevo Contacto");
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(COLOR_PRIMARIO);
        panelTitulo.add(lblTitulo, BorderLayout.CENTER);
        
        add(panelTitulo, BorderLayout.NORTH);
        
        // Panel para los campos de entrada
        JPanel panelCampos = new JPanel(new GridLayout(2, 2, 10, 15));
        panelCampos.setBackground(COLOR_FONDO);
        
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(FUENTE_LABEL);
        lblNombre.setForeground(COLOR_TEXTO);
        panelCampos.add(lblNombre);
        
        txtNombre = new JTextField();
        txtNombre.setFont(FUENTE_NORMAL);
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        panelCampos.add(txtNombre);
        
        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setFont(FUENTE_LABEL);
        lblTelefono.setForeground(COLOR_TEXTO);
        panelCampos.add(lblTelefono);
        
        txtTelefono = new JTextField();
        txtTelefono.setFont(FUENTE_NORMAL);
        txtTelefono.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        panelCampos.add(txtTelefono);
        
        // Añadir panel de campos con márgenes
        JPanel contenedorCampos = new JPanel(new BorderLayout());
        contenedorCampos.setBackground(COLOR_FONDO);
        contenedorCampos.add(panelCampos, BorderLayout.CENTER);
        contenedorCampos.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        add(contenedorCampos, BorderLayout.CENTER);
        
        // Panel para los botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.setBackground(COLOR_FONDO);
        
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(FUENTE_NORMAL);
        btnCancelar.setBackground(Color.WHITE);
        btnCancelar.setForeground(COLOR_TEXTO);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        btnAgregar = new JButton("Agregar Contacto");
        btnAgregar.setFont(FUENTE_NORMAL);
        btnAgregar.setBackground(COLOR_PRIMARIO);
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        panelBotones.add(btnCancelar);
        panelBotones.add(btnAgregar);
        add(panelBotones, BorderLayout.SOUTH);
        
        // Acción del botón Agregar
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarContacto();
            }
        });
        
        // Acción del botón Cancelar: cierra el diálogo padre
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
                // Cerrar el diálogo padre
                Window window = SwingUtilities.getWindowAncestor(AgregarContactoPanel.this);
                if (window instanceof JDialog) {
                    ((JDialog) window).dispose();
                }
            }
        });
    }
    
    private void agregarContacto() {
        String nombre = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();
        
        if(nombre.isEmpty() || telefono.isEmpty()){
            mostrarDialogoEstilizado("Por favor, complete todos los campos.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            controlador.agregarContacto(nombre, telefono);
            mostrarDialogoEstilizado("Contacto agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            // Cerrar el diálogo después de agregar exitosamente
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JDialog) {
                ((JDialog) window).dispose();
            }
        } catch (ChatControllerException ex) {
            mostrarDialogoEstilizado("Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limpiarCampos() {
        txtNombre.setText("");
        txtTelefono.setText("");
    }
    
    // Método para mostrar diálogos con estilo mejorado
    private void mostrarDialogoEstilizado(String mensaje, String titulo, int tipo) {
        // Personalizar los diálogos
        UIManager.put("OptionPane.background", COLOR_FONDO);
        UIManager.put("Panel.background", COLOR_FONDO);
        UIManager.put("OptionPane.messageForeground", COLOR_TEXTO);
        UIManager.put("OptionPane.messageFont", FUENTE_NORMAL);
        UIManager.put("OptionPane.buttonFont", FUENTE_NORMAL);
        UIManager.put("Button.background", COLOR_SECUNDARIO);
        UIManager.put("Button.foreground", COLOR_PRIMARIO);
        UIManager.put("Button.focus", COLOR_SECUNDARIO);
        
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipo);
    }
}