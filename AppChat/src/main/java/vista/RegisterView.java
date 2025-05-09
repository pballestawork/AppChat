package vista;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;

import com.toedter.calendar.JDateChooser;

import dominio.controlador.ChatController;
import dominio.controlador.ChatControllerException;
import dominio.repositorio.RepositorioException;
import utils.EstiloApp;
import utils.Utils;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Dimension;
import javax.swing.JPasswordField;
import javax.swing.JEditorPane;
import javax.swing.Box;
import javax.swing.BoxLayout;

public class RegisterView extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
    private JFrame parent;
    private JTextField nameField;
    private JTextField correoField;
    private JTextField telefonoField;
    private JPasswordField passwordField;
    private JPasswordField passwordField_1;
    private JDateChooser fechaNacimientoField;
    private AgregarFotoPerfilView panelArrastre;
    private JEditorPane textSaludoInicial;
    private File archivoImagen;
    private String rutaFotoPerfil; 
    private JLabel lblImagen;

    public RegisterView(JFrame parent) {
        setResizable(false);
        this.parent = parent;
        setTitle("AppChat - Registro");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 700, 770);
        getContentPane().setBackground(EstiloApp.COLOR_FONDO);
        getContentPane().setLayout(new BorderLayout(0, 0));
        
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(EstiloApp.COLOR_PRIMARIO);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        getContentPane().add(panelSuperior, BorderLayout.NORTH);
        panelSuperior.setLayout(new BorderLayout(0, 0));
        
        JLabel lblTitulo = new JLabel("AppChat");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(EstiloApp.FUENTE_TITULO);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        panelSuperior.add(lblTitulo, BorderLayout.CENTER);
        
        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(EstiloApp.COLOR_FONDO);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
        getContentPane().add(panelCentral, BorderLayout.CENTER);
        
        GridBagLayout gbl_panelCentral = new GridBagLayout();
        gbl_panelCentral.columnWidths = new int[]{0, 0, 0, 0, 0};
        gbl_panelCentral.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_panelCentral.columnWeights = new double[]{0.0, 0.5, 0.0, 0.5, Double.MIN_VALUE};
        gbl_panelCentral.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.5, Double.MIN_VALUE};
        panelCentral.setLayout(gbl_panelCentral);
        
        JLabel lblRegistro = new JLabel("Crear nueva cuenta");
        lblRegistro.setFont(EstiloApp.FUENTE_SUBTITULO);
        lblRegistro.setForeground(EstiloApp.COLOR_TEXTO);
        lblRegistro.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_lblRegistro = new GridBagConstraints();
        gbc_lblRegistro.gridwidth = 4;
        gbc_lblRegistro.insets = new Insets(0, 0, 15, 0);
        gbc_lblRegistro.gridx = 0;
        gbc_lblRegistro.gridy = 0;
        panelCentral.add(lblRegistro, gbc_lblRegistro);
        
        JLabel lblName = new JLabel("Nombre:");
        lblName.setFont(EstiloApp.FUENTE_LABEL);
        lblName.setForeground(EstiloApp.COLOR_TEXTO);
        GridBagConstraints gbc_lblName = new GridBagConstraints();
        gbc_lblName.anchor = GridBagConstraints.WEST;
        gbc_lblName.insets = new Insets(0, 0, 10, 10);
        gbc_lblName.gridx = 0;
        gbc_lblName.gridy = 1;
        panelCentral.add(lblName, gbc_lblName);
        
        nameField = new JTextField();
        nameField.setFont(EstiloApp.FUENTE_NORMAL);
        nameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        GridBagConstraints gbc_nameField = new GridBagConstraints();
        gbc_nameField.insets = new Insets(0, 0, 10, 0);
        gbc_nameField.fill = GridBagConstraints.HORIZONTAL;
        gbc_nameField.gridwidth = 3;
        gbc_nameField.gridx = 1;
        gbc_nameField.gridy = 1;
        panelCentral.add(nameField, gbc_nameField);
        
        JLabel lblCorreo = new JLabel("Correo Electrónico:");
        lblCorreo.setFont(EstiloApp.FUENTE_LABEL);
        lblCorreo.setForeground(EstiloApp.COLOR_TEXTO);
        GridBagConstraints gbc_lblCorreo = new GridBagConstraints();
        gbc_lblCorreo.anchor = GridBagConstraints.WEST;
        gbc_lblCorreo.insets = new Insets(0, 0, 10, 10);
        gbc_lblCorreo.gridx = 0;
        gbc_lblCorreo.gridy = 2;
        panelCentral.add(lblCorreo, gbc_lblCorreo);
        
        correoField = new JTextField();
        correoField.setFont(EstiloApp.FUENTE_NORMAL);
        correoField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        GridBagConstraints gbc_correoField = new GridBagConstraints();
        gbc_correoField.insets = new Insets(0, 0, 10, 0);
        gbc_correoField.fill = GridBagConstraints.HORIZONTAL;
        gbc_correoField.gridwidth = 3;
        gbc_correoField.gridx = 1;
        gbc_correoField.gridy = 2;
        panelCentral.add(correoField, gbc_correoField);
        
        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setFont(EstiloApp.FUENTE_LABEL);
        lblTelefono.setForeground(EstiloApp.COLOR_TEXTO);
        GridBagConstraints gbc_lblTelefono = new GridBagConstraints();
        gbc_lblTelefono.anchor = GridBagConstraints.WEST;
        gbc_lblTelefono.insets = new Insets(0, 0, 10, 10);
        gbc_lblTelefono.gridx = 0;
        gbc_lblTelefono.gridy = 3;
        panelCentral.add(lblTelefono, gbc_lblTelefono);
        
        telefonoField = new JTextField();
        telefonoField.setFont(EstiloApp.FUENTE_NORMAL);
        telefonoField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        GridBagConstraints gbc_telefonoField = new GridBagConstraints();
        gbc_telefonoField.insets = new Insets(0, 0, 10, 0);
        gbc_telefonoField.fill = GridBagConstraints.HORIZONTAL;
        gbc_telefonoField.gridwidth = 3;
        gbc_telefonoField.gridx = 1;
        gbc_telefonoField.gridy = 3;
        panelCentral.add(telefonoField, gbc_telefonoField);
        
        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setFont(EstiloApp.FUENTE_LABEL);
        lblContrasena.setForeground(EstiloApp.COLOR_TEXTO);
        GridBagConstraints gbc_lblContrasena = new GridBagConstraints();
        gbc_lblContrasena.anchor = GridBagConstraints.WEST;
        gbc_lblContrasena.insets = new Insets(0, 0, 10, 10);
        gbc_lblContrasena.gridx = 0;
        gbc_lblContrasena.gridy = 4;
        panelCentral.add(lblContrasena, gbc_lblContrasena);
        
        passwordField = new JPasswordField();
        passwordField.setFont(EstiloApp.FUENTE_NORMAL);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        GridBagConstraints gbc_passwordField = new GridBagConstraints();
        gbc_passwordField.insets = new Insets(0, 0, 10, 20);
        gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
        gbc_passwordField.gridx = 1;
        gbc_passwordField.gridy = 4;
        panelCentral.add(passwordField, gbc_passwordField);
        
        JLabel lblContrasena_1 = new JLabel("Repetir:");
        lblContrasena_1.setFont(EstiloApp.FUENTE_LABEL);
        lblContrasena_1.setForeground(EstiloApp.COLOR_TEXTO);
        GridBagConstraints gbc_lblContrasena_1 = new GridBagConstraints();
        gbc_lblContrasena_1.anchor = GridBagConstraints.WEST;
        gbc_lblContrasena_1.insets = new Insets(0, 0, 10, 10);
        gbc_lblContrasena_1.gridx = 2;
        gbc_lblContrasena_1.gridy = 4;
        panelCentral.add(lblContrasena_1, gbc_lblContrasena_1);
        
        passwordField_1 = new JPasswordField();
        passwordField_1.setFont(EstiloApp.FUENTE_NORMAL);
        passwordField_1.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        GridBagConstraints gbc_passwordField_1 = new GridBagConstraints();
        gbc_passwordField_1.insets = new Insets(0, 0, 10, 0);
        gbc_passwordField_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_passwordField_1.gridx = 3;
        gbc_passwordField_1.gridy = 4;
        panelCentral.add(passwordField_1, gbc_passwordField_1);
        
        JLabel lblFechaDeNacimiento = new JLabel("Fecha de nacimiento:");
        lblFechaDeNacimiento.setFont(EstiloApp.FUENTE_LABEL);
        lblFechaDeNacimiento.setForeground(EstiloApp.COLOR_TEXTO);
        GridBagConstraints gbc_lblFechaDeNacimiento = new GridBagConstraints();
        gbc_lblFechaDeNacimiento.anchor = GridBagConstraints.WEST;
        gbc_lblFechaDeNacimiento.insets = new Insets(0, 0, 10, 10);
        gbc_lblFechaDeNacimiento.gridx = 0;
        gbc_lblFechaDeNacimiento.gridy = 5;
        panelCentral.add(lblFechaDeNacimiento, gbc_lblFechaDeNacimiento);
        
        fechaNacimientoField = new JDateChooser();
        fechaNacimientoField.setDateFormatString("dd/MM/yyyy");
        fechaNacimientoField.setFont(EstiloApp.FUENTE_NORMAL);
        fechaNacimientoField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        GridBagConstraints gbc_fechaNacimientoField = new GridBagConstraints();
        gbc_fechaNacimientoField.insets = new Insets(0, 0, 10, 0);
        gbc_fechaNacimientoField.fill = GridBagConstraints.HORIZONTAL;
        gbc_fechaNacimientoField.gridwidth = 3;
        gbc_fechaNacimientoField.gridx = 1;
        gbc_fechaNacimientoField.gridy = 5;
        panelCentral.add(fechaNacimientoField, gbc_fechaNacimientoField);
        
        JLabel lblSaludoInicial = new JLabel("Saludo inicial:");
        lblSaludoInicial.setFont(EstiloApp.FUENTE_LABEL);
        lblSaludoInicial.setForeground(EstiloApp.COLOR_TEXTO);
        GridBagConstraints gbc_lblSaludoInicial = new GridBagConstraints();
        gbc_lblSaludoInicial.anchor = GridBagConstraints.WEST;
        gbc_lblSaludoInicial.insets = new Insets(0, 0, 10, 10);
        gbc_lblSaludoInicial.gridx = 0;
        gbc_lblSaludoInicial.gridy = 6;
        panelCentral.add(lblSaludoInicial, gbc_lblSaludoInicial);
        
        textSaludoInicial = new JEditorPane();
        textSaludoInicial.setFont(EstiloApp.FUENTE_NORMAL);
        textSaludoInicial.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        textSaludoInicial.setPreferredSize(new Dimension(0, 40));
        
        GridBagConstraints gbc_textSaludoInicial = new GridBagConstraints();
        gbc_textSaludoInicial.insets = new Insets(0, 0, 10, 0);
        gbc_textSaludoInicial.fill = GridBagConstraints.HORIZONTAL;
        gbc_textSaludoInicial.weighty = 0.0;
        gbc_textSaludoInicial.gridwidth = 3;
        gbc_textSaludoInicial.gridx = 1;
        gbc_textSaludoInicial.gridy = 6;
        panelCentral.add(textSaludoInicial, gbc_textSaludoInicial);
        
        JLabel lblFotoPerfil = new JLabel("Foto de perfil:");
        lblFotoPerfil.setFont(EstiloApp.FUENTE_LABEL);
        lblFotoPerfil.setForeground(EstiloApp.COLOR_TEXTO);
        GridBagConstraints gbc_lblFotoPerfil = new GridBagConstraints();
        gbc_lblFotoPerfil.anchor = GridBagConstraints.NORTHWEST;
        gbc_lblFotoPerfil.insets = new Insets(0, 0, 0, 10);
        gbc_lblFotoPerfil.gridx = 0;
        gbc_lblFotoPerfil.gridy = 8;
        panelCentral.add(lblFotoPerfil, gbc_lblFotoPerfil);
        
        JPanel panelImagen = new JPanel(new BorderLayout(0, 15));
        panelImagen.setBackground(EstiloApp.COLOR_FONDO);
        
        GridBagConstraints gbc_panelImagen = new GridBagConstraints();
        gbc_panelImagen.fill = GridBagConstraints.BOTH;
        gbc_panelImagen.gridwidth = 3;
        gbc_panelImagen.gridx = 1;
        gbc_panelImagen.gridy = 8;
        panelCentral.add(panelImagen, gbc_panelImagen);
        
        JPanel panelImagenSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelImagenSuperior.setBackground(EstiloApp.COLOR_FONDO);
        panelImagen.add(panelImagenSuperior, BorderLayout.CENTER);
        
        lblImagen = new JLabel("No se ha seleccionado ninguna imagen");
        lblImagen.setFont(EstiloApp.FUENTE_NORMAL);
        lblImagen.setForeground(Color.GRAY);
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagen.setPreferredSize(new Dimension(200, 170));
        panelImagenSuperior.add(lblImagen);
        
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelBoton.setBackground(EstiloApp.COLOR_FONDO);
        panelImagen.add(panelBoton, BorderLayout.SOUTH);
        
        JButton btnSeleccionarImagen = new JButton("Seleccionar Imagen");
        btnSeleccionarImagen.setFont(EstiloApp.FUENTE_NORMAL);
        btnSeleccionarImagen.setBackground(EstiloApp.COLOR_SECUNDARIO);
        btnSeleccionarImagen.setForeground(EstiloApp.COLOR_PRIMARIO);
        btnSeleccionarImagen.setFocusPainted(false);
        btnSeleccionarImagen.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloApp.COLOR_PRIMARIO),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        btnSeleccionarImagen.setPreferredSize(new Dimension(200, 40));
        btnSeleccionarImagen.addActionListener(e -> {
            panelArrastre = new AgregarFotoPerfilView(parent);
            java.util.List<File> imagenes = panelArrastre.showDialog();
            if (imagenes != null && !imagenes.isEmpty()) {
                archivoImagen = imagenes.get(0);
                try {
                    rutaFotoPerfil = Utils.getRutaResourceFromFile(archivoImagen);
                    ImageIcon iconoImagen = new ImageIcon(getClass().getResource(rutaFotoPerfil));
                    Image imagenEscalada = iconoImagen.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    lblImagen.setIcon(new ImageIcon(imagenEscalada));
                    lblImagen.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al cargar la imagen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
                panelArrastre.setVisible(false);
                panelArrastre.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se ha seleccionado ninguna imagen", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });
        panelBoton.add(btnSeleccionarImagen);
        
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(EstiloApp.COLOR_FONDO);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        getContentPane().add(panelInferior, BorderLayout.SOUTH);
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(EstiloApp.FUENTE_NORMAL);
        btnCancelar.setBackground(Color.WHITE);
        btnCancelar.setForeground(EstiloApp.COLOR_TEXTO);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        btnCancelar.addActionListener(e -> {
            parent.setVisible(true);
            dispose();
        });
        panelInferior.add(btnCancelar);
        
        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.setFont(EstiloApp.FUENTE_NORMAL);
        btnRegistrar.setBackground(EstiloApp.COLOR_PRIMARIO);
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnRegistrar.addActionListener(e -> {
            try {
                String nombre = nameField.getText().trim();
                String telefono = telefonoField.getText().trim();
                String correo = correoField.getText().trim();
                String contrasena = new String(passwordField.getPassword());
                String contrasenaRepetida = new String(passwordField_1.getPassword());
                String saludo = textSaludoInicial.getText().trim();
                
                LocalDate fechaNacimiento = null;
                if (fechaNacimientoField.getDate() != null) {
                    fechaNacimiento = fechaNacimientoField.getDate().toInstant()
                        .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                }
                
                if (!nombre.matches("[A-Za-zÀ-ÿ\\s]+")) {
                    JOptionPane.showMessageDialog(this, "El nombre solo puede contener letras y espacios.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!correo.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    JOptionPane.showMessageDialog(this, "El correo electrónico no tiene un formato válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!telefono.matches("\\d+")) {
                    JOptionPane.showMessageDialog(this, "El teléfono solo debe contener números.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (fechaNacimiento == null || fechaNacimiento.isAfter(LocalDate.now())) {
                    JOptionPane.showMessageDialog(this, "Debe seleccionar una fecha de nacimiento válida.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!contrasena.equals(contrasenaRepetida)) {
                    JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (rutaFotoPerfil == null || rutaFotoPerfil.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Por favor, seleccione una foto de perfil.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                ChatController controlador = ChatController.getUnicaInstancia();
                controlador.registrarUsuario(nombre, fechaNacimiento, correo, rutaFotoPerfil, telefono, contrasena, saludo);
                
                JOptionPane.showMessageDialog(this, "Registro exitoso. Ahora puedes iniciar sesión.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                parent.setVisible(true);
                dispose();
                
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ChatControllerException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (RepositorioException ex) {
            	JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
        });
        panelInferior.add(btnRegistrar);
        
        setLocationRelativeTo(null);
    }
}
