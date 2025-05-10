package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import dominio.controlador.ChatController;
import dominio.controlador.ChatControllerException;
import dominio.repositorio.EntidadNoEncontrada;
import dominio.repositorio.RepositorioException;
import utils.EstiloApp;


public class AgregarContactoPanel extends JPanel {
   
    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JButton btnAgregar;
    private JButton btnCancelar;
    private ChatController controlador;

    public AgregarContactoPanel() {
        try {
			controlador = ChatController.getUnicaInstancia();
		} catch (ChatControllerException e) {
			e.printStackTrace();
		}
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(0, 15));
        setBackground(EstiloApp.COLOR_FONDO);
        setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        
        // Panel de título
        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setBackground(EstiloApp.COLOR_FONDO);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JLabel lblTitulo = new JLabel("Agregar Nuevo Contacto");
        lblTitulo.setFont(EstiloApp.FUENTE_TITULO_MEDIO);
        lblTitulo.setForeground(EstiloApp.COLOR_PRIMARIO);
        panelTitulo.add(lblTitulo, BorderLayout.CENTER);
        
        add(panelTitulo, BorderLayout.NORTH);
        
        // Panel para los campos de entrada
        JPanel panelCampos = new JPanel(new GridLayout(2, 2, 10, 15));
        panelCampos.setBackground(EstiloApp.COLOR_FONDO);
        
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(EstiloApp.FUENTE_LABEL);
        lblNombre.setForeground(EstiloApp.COLOR_TEXTO);
        panelCampos.add(lblNombre);
        
        txtNombre = new JTextField();
        txtNombre.setFont(EstiloApp.FUENTE_NORMAL);
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloApp.COLOR_PRIMARIO.darker()),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        panelCampos.add(txtNombre);
        
        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setFont(EstiloApp.FUENTE_LABEL);
        lblTelefono.setForeground(EstiloApp.COLOR_TEXTO);
        panelCampos.add(lblTelefono);
        
        txtTelefono = new JTextField();
        txtTelefono.setFont(EstiloApp.FUENTE_NORMAL);
        txtTelefono.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloApp.COLOR_PRIMARIO.darker()),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        panelCampos.add(txtTelefono);
        
        // Añadir panel de campos con márgenes
        JPanel contenedorCampos = new JPanel(new BorderLayout());
        contenedorCampos.setBackground(EstiloApp.COLOR_FONDO);
        contenedorCampos.add(panelCampos, BorderLayout.CENTER);
        contenedorCampos.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        add(contenedorCampos, BorderLayout.CENTER);
        
        // Panel para los botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.setBackground(EstiloApp.COLOR_FONDO);
        
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(EstiloApp.FUENTE_BUTTON);
        btnCancelar.setBackground(EstiloApp.COLOR_SECUNDARIO);
        btnCancelar.setForeground(EstiloApp.COLOR_TEXTO);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloApp.COLOR_TEXTO),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        btnAgregar = new JButton("Agregar Contacto");
        btnAgregar.setFont(EstiloApp.FUENTE_BUTTON);
        btnAgregar.setBackground(EstiloApp.COLOR_SECUNDARIO);
        btnAgregar.setForeground(EstiloApp.COLOR_PRIMARIO);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloApp.COLOR_PRIMARIO),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
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
        } catch (ChatControllerException | RepositorioException | EntidadNoEncontrada ex) {
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
        UIManager.put("OptionPane.background", EstiloApp.COLOR_FONDO);
        UIManager.put("Panel.background", EstiloApp.COLOR_FONDO);
        UIManager.put("OptionPane.messageForeground", EstiloApp.COLOR_TEXTO);
        UIManager.put("OptionPane.messageFont", EstiloApp.FUENTE_NORMAL);
        UIManager.put("OptionPane.buttonFont", EstiloApp.FUENTE_BUTTON);
        UIManager.put("Button.background", EstiloApp.COLOR_SECUNDARIO);
        UIManager.put("Button.foreground", EstiloApp.COLOR_PRIMARIO);
        UIManager.put("Button.focus", EstiloApp.COLOR_SECUNDARIO);
        
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipo);
    }
}