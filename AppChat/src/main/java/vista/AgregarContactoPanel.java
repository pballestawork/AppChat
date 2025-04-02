package vista;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dominio.controlador.ChatControllerException;
import utils.ChatControllerStub;
import javax.swing.JSeparator;
import java.awt.Component;
import javax.swing.Box;

public class AgregarContactoPanel extends JPanel {
    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JButton btnAgregar;
    private JButton btnCancelar;
    private ChatControllerStub controlador;
    private Component verticalStrut;
    private Component horizontalStrut;
    private Component horizontalStrut_1;

    public AgregarContactoPanel() {
        controlador = ChatControllerStub.getUnicaInstancia();
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Panel para los campos de entrada
        JPanel panelCampos = new JPanel(new GridLayout(2, 2, 5, 5));
        panelCampos.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelCampos.add(txtNombre);
        
        panelCampos.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        panelCampos.add(txtTelefono);
        
        add(panelCampos, BorderLayout.CENTER);
        
        // Panel para los botones
        JPanel panelBotones = new JPanel();
        btnAgregar = new JButton("Agregar Contacto");
        btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnAgregar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
        
        verticalStrut = Box.createVerticalStrut(20);
        add(verticalStrut, BorderLayout.NORTH);
        
        horizontalStrut = Box.createHorizontalStrut(20);
        add(horizontalStrut, BorderLayout.WEST);
        
        horizontalStrut_1 = Box.createHorizontalStrut(20);
        add(horizontalStrut_1, BorderLayout.EAST);
        
        // Acción del botón Agregar
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarContacto();
            }
        });
        
        // Acción del botón Cancelar: puede cerrar el diálogo o limpiar los campos
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });
    }
    
    private void agregarContacto() {
        String nombre = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();
        
        if(nombre.isEmpty() || telefono.isEmpty()){
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            controlador.agregarContacto(nombre, telefono);
            JOptionPane.showMessageDialog(this, "Contacto agregado exitosamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            // Aquí podrías notificar a MainView para que actualice la lista de contactos.
        } catch (ChatControllerException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limpiarCampos() {
        txtNombre.setText("");
        txtTelefono.setText("");
    }
}