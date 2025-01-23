package vista;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import utils.Utils;

import java.awt.SystemColor;
import java.io.File;
import java.util.List;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JPasswordField;
import javax.swing.JEditorPane;

public class RegisterView extends JFrame {
	private JFrame parent;
	private JTextField nameField;
	private JTextField apellidoField;
	private JTextField telefonoField;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JTextField fechaNacimientoField;
	private AgregarFotoPerfilView panelArrastre;
	private File archivoImagen;
	private String rutaArchivo;

	public RegisterView(JFrame parent) {
		setResizable(false);
		this.parent = parent;
		getContentPane().setBackground(SystemColor.desktop);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel_central = new JPanel();
		panel_central.setBackground(SystemColor.desktop);
		getContentPane().add(panel_central, BorderLayout.CENTER);
		GridBagLayout gbl_panel_central = new GridBagLayout();
		gbl_panel_central.columnWidths = new int[]{50, 120, 100, 20, 0, 100, 50, 0};
		gbl_panel_central.rowHeights = new int[]{40, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8, 60, 8, 0, 40, 0};
		gbl_panel_central.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_central.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_central.setLayout(gbl_panel_central);
		
		JLabel lblName = new JLabel("Nombre:");
		lblName.setForeground(SystemColor.text);
		lblName.setFont(new Font("Tahoma", Font.BOLD, 16));
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.WEST;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 1;
		gbc_lblName.gridy = 1;
		panel_central.add(lblName, gbc_lblName);
		
		nameField = new JTextField();
		nameField.setBackground(SystemColor.inactiveCaption);
		GridBagConstraints gbc_nameField = new GridBagConstraints();
		gbc_nameField.gridwidth = 4;
		gbc_nameField.insets = new Insets(0, 0, 5, 5);
		gbc_nameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_nameField.gridx = 2;
		gbc_nameField.gridy = 1;
		panel_central.add(nameField, gbc_nameField);
		
		JLabel lblApellidos = new JLabel("Apellidos:");
		lblApellidos.setForeground(SystemColor.text);
		lblApellidos.setFont(new Font("Tahoma", Font.BOLD, 16));
		GridBagConstraints gbc_lblApellidos = new GridBagConstraints();
		gbc_lblApellidos.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblApellidos.insets = new Insets(0, 0, 5, 5);
		gbc_lblApellidos.gridx = 1;
		gbc_lblApellidos.gridy = 3;
		panel_central.add(lblApellidos, gbc_lblApellidos);
		
		apellidoField = new JTextField();
		apellidoField.setBackground(SystemColor.inactiveCaption);
		GridBagConstraints gbc_apellidoField = new GridBagConstraints();
		gbc_apellidoField.gridwidth = 4;
		gbc_apellidoField.insets = new Insets(0, 0, 5, 5);
		gbc_apellidoField.fill = GridBagConstraints.BOTH;
		gbc_apellidoField.gridx = 2;
		gbc_apellidoField.gridy = 3;
		panel_central.add(apellidoField, gbc_apellidoField);
		
		JLabel lblContrasea = new JLabel("Contraseña:");
		lblContrasea.setForeground(SystemColor.text);
		lblContrasea.setFont(new Font("Tahoma", Font.BOLD, 16));
		GridBagConstraints gbc_lblContrasea = new GridBagConstraints();
		gbc_lblContrasea.anchor = GridBagConstraints.WEST;
		gbc_lblContrasea.insets = new Insets(0, 0, 5, 5);
		gbc_lblContrasea.gridx = 1;
		gbc_lblContrasea.gridy = 5;
		panel_central.add(lblContrasea, gbc_lblContrasea);
		
		passwordField = new JPasswordField();
		passwordField.setBackground(SystemColor.inactiveCaption);
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField.fill = GridBagConstraints.BOTH;
		gbc_passwordField.gridx = 2;
		gbc_passwordField.gridy = 5;
		panel_central.add(passwordField, gbc_passwordField);
		
		JLabel lblContrasena_1 = new JLabel("Contraseña:");
		lblContrasena_1.setForeground(SystemColor.text);
		lblContrasena_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		GridBagConstraints gbc_lblContrasena_1 = new GridBagConstraints();
		gbc_lblContrasena_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblContrasena_1.anchor = GridBagConstraints.EAST;
		gbc_lblContrasena_1.gridx = 4;
		gbc_lblContrasena_1.gridy = 5;
		panel_central.add(lblContrasena_1, gbc_lblContrasena_1);
		
		passwordField_1 = new JPasswordField();
		passwordField_1.setBackground(SystemColor.inactiveCaption);
		GridBagConstraints gbc_passwordField_1 = new GridBagConstraints();
		gbc_passwordField_1.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField_1.fill = GridBagConstraints.BOTH;
		gbc_passwordField_1.gridx = 5;
		gbc_passwordField_1.gridy = 5;
		panel_central.add(passwordField_1, gbc_passwordField_1);
		
		JLabel lblTelfono = new JLabel("Teléfono:");
		lblTelfono.setForeground(SystemColor.text);
		lblTelfono.setFont(new Font("Tahoma", Font.BOLD, 16));
		GridBagConstraints gbc_lblTelfono = new GridBagConstraints();
		gbc_lblTelfono.anchor = GridBagConstraints.WEST;
		gbc_lblTelfono.insets = new Insets(0, 0, 5, 5);
		gbc_lblTelfono.gridx = 1;
		gbc_lblTelfono.gridy = 7;
		panel_central.add(lblTelfono, gbc_lblTelfono);
		
		telefonoField = new JTextField();
		telefonoField.setBackground(SystemColor.inactiveCaption);
		GridBagConstraints gbc_telefonoField = new GridBagConstraints();
		gbc_telefonoField.insets = new Insets(0, 0, 5, 5);
		gbc_telefonoField.fill = GridBagConstraints.BOTH;
		gbc_telefonoField.gridx = 2;
		gbc_telefonoField.gridy = 7;
		panel_central.add(telefonoField, gbc_telefonoField);
		
		JLabel lblFotoPerfil = new JLabel("Foto de perfil:");
		lblFotoPerfil.setForeground(SystemColor.text);
		lblFotoPerfil.setFont(new Font("Tahoma", Font.BOLD, 16));
		GridBagConstraints gbc_lblFotoPerfil = new GridBagConstraints();
		gbc_lblFotoPerfil.gridwidth = 2;
		gbc_lblFotoPerfil.insets = new Insets(0, 0, 5, 5);
		gbc_lblFotoPerfil.gridx = 4;
		gbc_lblFotoPerfil.gridy = 7;
		panel_central.add(lblFotoPerfil, gbc_lblFotoPerfil);
		
		JLabel lblFechaDeNacimiento = new JLabel("Fecha de nacimiento:");
		lblFechaDeNacimiento.setForeground(SystemColor.text);
		lblFechaDeNacimiento.setFont(new Font("Tahoma", Font.BOLD, 16));
		GridBagConstraints gbc_lblFechaDeNacimiento = new GridBagConstraints();
		gbc_lblFechaDeNacimiento.anchor = GridBagConstraints.EAST;
		gbc_lblFechaDeNacimiento.insets = new Insets(0, 0, 5, 5);
		gbc_lblFechaDeNacimiento.gridx = 1;
		gbc_lblFechaDeNacimiento.gridy = 9;
		panel_central.add(lblFechaDeNacimiento, gbc_lblFechaDeNacimiento);
		
		fechaNacimientoField = new JTextField();
		fechaNacimientoField.setBackground(SystemColor.inactiveCaption);
		GridBagConstraints gbc_fechaNacimientoField = new GridBagConstraints();
		gbc_fechaNacimientoField.insets = new Insets(0, 0, 5, 5);
		gbc_fechaNacimientoField.fill = GridBagConstraints.BOTH;
		gbc_fechaNacimientoField.gridx = 2;
		gbc_fechaNacimientoField.gridy = 9;
		panel_central.add(fechaNacimientoField, gbc_fechaNacimientoField);
		
		JLabel lblImagen = new JLabel("No se ha seleccionado ninguna imagen");
		lblImagen.setForeground(SystemColor.text);
		GridBagConstraints gbc_lblImagen = new GridBagConstraints();
		gbc_lblImagen.gridheight = 3;
		gbc_lblImagen.gridwidth = 2;
		gbc_lblImagen.insets = new Insets(0, 0, 5, 5);
		gbc_lblImagen.gridx = 4;
		gbc_lblImagen.gridy = 9;
		panel_central.add(lblImagen, gbc_lblImagen);
		
		JLabel lblSaludoInicial = new JLabel("Saludo inicial:");
		lblSaludoInicial.setForeground(SystemColor.text);
		lblSaludoInicial.setFont(new Font("Tahoma", Font.BOLD, 16));
		GridBagConstraints gbc_lblSaludoInicial = new GridBagConstraints();
		gbc_lblSaludoInicial.anchor = GridBagConstraints.WEST;
		gbc_lblSaludoInicial.insets = new Insets(0, 0, 5, 5);
		gbc_lblSaludoInicial.gridx = 1;
		gbc_lblSaludoInicial.gridy = 11;
		panel_central.add(lblSaludoInicial, gbc_lblSaludoInicial);
		
		JEditorPane editorPane = new JEditorPane();
		editorPane.setBackground(SystemColor.inactiveCaption);
		GridBagConstraints gbc_editorPane = new GridBagConstraints();
		gbc_editorPane.insets = new Insets(0, 0, 5, 5);
		gbc_editorPane.fill = GridBagConstraints.BOTH;
		gbc_editorPane.gridx = 2;
		gbc_editorPane.gridy = 11;
		panel_central.add(editorPane, gbc_editorPane);
		
		JButton btnNewButton = new JButton("Seleccionar Imagen");
		btnNewButton.setBackground(SystemColor.inactiveCaption);
		btnNewButton.addActionListener(e -> {
			panelArrastre = new AgregarFotoPerfilView(parent);
			List<File> imagenes = panelArrastre.showDialog();
			//panel de arrastre retorna la imagen en una lista de File y se obtiene la ruta
			if (imagenes != null && !imagenes.isEmpty()) {
			archivoImagen = imagenes.get(0);
			rutaArchivo = Utils.getRutaResourceFromFile(archivoImagen);
			}
			//se dibuja la imagen en el panel de registro sobre la etiqueta “No se ha ..”
			ImageIcon iconoImagen = new ImageIcon(getClass().getResource(rutaArchivo));
			Image imagenEscalada = iconoImagen.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
			lblImagen.setIcon(new ImageIcon(imagenEscalada));
			lblImagen.setText("");
			//se oculta el panel y se elimina
			panelArrastre.setVisible(false);
			panelArrastre.dispose();
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.gridwidth = 5;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 13;
		panel_central.add(btnNewButton, gbc_btnNewButton);
		
		JPanel panel_superior = new JPanel();
		panel_superior.setBackground(SystemColor.desktop);
		getContentPane().add(panel_superior, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("AppChat");
		lblNewLabel.setForeground(SystemColor.text);
		lblNewLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 60));
		panel_superior.add(lblNewLabel);
		
		JPanel panel_inferior = new JPanel();
		panel_inferior.setBackground(SystemColor.desktop);
		getContentPane().add(panel_inferior, BorderLayout.SOUTH);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBackground(new Color(153, 180, 209));
		btnCancelar.addActionListener(e -> {
			parent.setVisible(true);
			dispose();
		});
		panel_inferior.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel_inferior.add(btnCancelar);
		
		JButton btnSubmit = new JButton("Registrar");
		btnSubmit.setBackground(SystemColor.activeCaption);
		btnSubmit.addActionListener(e -> {
			parent.setVisible(true);
			dispose();
		});
		panel_inferior.add(btnSubmit);
		setBackground(SystemColor.desktop);
		setAlwaysOnTop(true);
		// Configuración de la ventana
		setTitle("Registro");
		setSize(700, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);
        
        parent.setVisible(false);

		setVisible(true);
	}

}
