package vista;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dominio.controlador.ChatController;
import dominio.controlador.ChatControllerException;
import dominio.modelo.Usuario;
import dominio.repositorio.EntidadNoEncontrada;
import dominio.repositorio.RepositorioException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import java.awt.Image;
import utils.EstiloApp;

public class LoginView extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField txtCorreo;
	private JPasswordField txtPassword;
	private ChatController controlador;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginView frame = new LoginView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginView() {
		setResizable(false);
		setTitle("AppChat - Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 600);
		contentPane = new JPanel();
		contentPane.setBackground(EstiloApp.COLOR_FONDO);
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		// Panel superior con título y logo
		JPanel panelSuperior = new JPanel();
		panelSuperior.setBackground(EstiloApp.COLOR_PRIMARIO);
		panelSuperior.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
		contentPane.add(panelSuperior, BorderLayout.NORTH);
		panelSuperior.setLayout(new BorderLayout(0, 0));
		
		JLabel lblTitulo = new JLabel("AppChat");
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(EstiloApp.FUENTE_TITULO);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		panelSuperior.add(lblTitulo, BorderLayout.CENTER);
		
		// Panel central con formulario
		JPanel panelCentral = new JPanel();
		panelCentral.setBackground(EstiloApp.COLOR_FONDO);
		panelCentral.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
		contentPane.add(panelCentral, BorderLayout.CENTER);
		GridBagLayout gbl_panelCentral = new GridBagLayout();
		gbl_panelCentral.columnWidths = new int[]{0, 0};
		gbl_panelCentral.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panelCentral.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelCentral.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelCentral.setLayout(gbl_panelCentral);
		
		// Mensaje de bienvenida
		JLabel lblBienvenida = new JLabel("Bienvenido/a a AppChat");
		lblBienvenida.setFont(EstiloApp.FUENTE_TITULO_PEQUENO);
		lblBienvenida.setForeground(EstiloApp.COLOR_TEXTO);
		lblBienvenida.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblBienvenida = new GridBagConstraints();
		gbc_lblBienvenida.insets = new Insets(0, 0, 20, 0);
		gbc_lblBienvenida.gridx = 0;
		gbc_lblBienvenida.gridy = 0;
		panelCentral.add(lblBienvenida, gbc_lblBienvenida);
		
		// Etiqueta Correo
		JLabel lblCorreo = new JLabel("Número de teléfono");
		lblCorreo.setFont(EstiloApp.FUENTE_LABEL);
		lblCorreo.setForeground(EstiloApp.COLOR_TEXTO);
		GridBagConstraints gbc_lblCorreo = new GridBagConstraints();
		gbc_lblCorreo.anchor = GridBagConstraints.WEST;
		gbc_lblCorreo.insets = new Insets(0, 0, 5, 0);
		gbc_lblCorreo.gridx = 0;
		gbc_lblCorreo.gridy = 2;
		panelCentral.add(lblCorreo, gbc_lblCorreo);
		
		// Campo de texto para correo
		txtCorreo = new JTextField();
		txtCorreo.setFont(EstiloApp.FUENTE_NORMAL);
		txtCorreo.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.LIGHT_GRAY),
				BorderFactory.createEmptyBorder(10, 15, 10, 15)
		));
		GridBagConstraints gbc_txtCorreo = new GridBagConstraints();
		gbc_txtCorreo.insets = new Insets(0, 0, 15, 0);
		gbc_txtCorreo.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtCorreo.gridx = 0;
		gbc_txtCorreo.gridy = 3;
		panelCentral.add(txtCorreo, gbc_txtCorreo);
		txtCorreo.setColumns(10);
		
		// Etiqueta Contraseña
		JLabel lblContrasea = new JLabel("Contraseña");
		lblContrasea.setFont(EstiloApp.FUENTE_LABEL);
		lblContrasea.setForeground(EstiloApp.COLOR_TEXTO);
		GridBagConstraints gbc_lblContrasea = new GridBagConstraints();
		gbc_lblContrasea.anchor = GridBagConstraints.WEST;
		gbc_lblContrasea.insets = new Insets(0, 0, 5, 0);
		gbc_lblContrasea.gridx = 0;
		gbc_lblContrasea.gridy = 4;
		panelCentral.add(lblContrasea, gbc_lblContrasea);
		
		// Campo de contraseña
		txtPassword = new JPasswordField();
		txtPassword.setFont(EstiloApp.FUENTE_NORMAL);
		txtPassword.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.LIGHT_GRAY),
				BorderFactory.createEmptyBorder(10, 15, 10, 15)
		));
		GridBagConstraints gbc_txtPassword = new GridBagConstraints();
		gbc_txtPassword.insets = new Insets(0, 0, 25, 0);
		gbc_txtPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPassword.gridx = 0;
		gbc_txtPassword.gridy = 5;
		panelCentral.add(txtPassword, gbc_txtPassword);
		
		// Botón de inicio de sesión
		JButton btnLogin = new JButton("Iniciar Sesión");
		btnLogin.setFont(EstiloApp.FUENTE_NORMAL);
		btnLogin.setBackground(EstiloApp.COLOR_PRIMARIO);
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setFocusPainted(false);
		btnLogin.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
		GridBagConstraints gbc_btnLogin = new GridBagConstraints();
		gbc_btnLogin.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnLogin.insets = new Insets(0, 0, 15, 0);
		gbc_btnLogin.gridx = 0;
		gbc_btnLogin.gridy = 6;
		panelCentral.add(btnLogin, gbc_btnLogin);
		
		// Etiqueta para registrarse
		JLabel lblRegistro = new JLabel("¿No tienes cuenta? Regístrate");
		lblRegistro.setFont(EstiloApp.FUENTE_LINK);
		lblRegistro.setForeground(EstiloApp.COLOR_PRIMARIO);
		lblRegistro.setHorizontalAlignment(SwingConstants.CENTER);
		lblRegistro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		GridBagConstraints gbc_lblRegistro = new GridBagConstraints();
		gbc_lblRegistro.gridx = 0;
		gbc_lblRegistro.gridy = 7;
		panelCentral.add(lblRegistro, gbc_lblRegistro);
		
		// Panel inferior para información adicional
		JPanel panelInferior = new JPanel();
		panelInferior.setBackground(EstiloApp.COLOR_FONDO);
		panelInferior.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
		contentPane.add(panelInferior, BorderLayout.SOUTH);
		panelInferior.setLayout(new BoxLayout(panelInferior, BoxLayout.Y_AXIS));
		
		// Texto de copyright
		JLabel lblCopyright = new JLabel("© 2025 AppChat - Todos los derechos reservados");
		lblCopyright.setAlignmentX(CENTER_ALIGNMENT);
		lblCopyright.setFont(new Font("Arial", Font.PLAIN, 12));
		lblCopyright.setForeground(new Color(120, 120, 120));
		panelInferior.add(lblCopyright);
		
		// Centrar ventana en pantalla
		setLocationRelativeTo(null);
		
		// Instancia del controlador
		try {
			controlador = ChatController.getUnicaInstancia();
		} catch (ChatControllerException e) {
			e.printStackTrace();
		}
		
		// Action Listener para botón de login
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String correo = txtCorreo.getText();
					String password = new String(txtPassword.getPassword());
					
					// Validaciones básicas
					if(correo.isEmpty() || password.isEmpty()) {
						JOptionPane.showMessageDialog(LoginView.this, 
							"Por favor, complete todos los campos", 
							"Campos vacíos", 
							JOptionPane.WARNING_MESSAGE);
						return;
					}
					
					// Validar login
					Usuario usuario = controlador.iniciarSesion(correo, password);
					if (usuario != null) {
						// Login exitoso, abrir la ventana principal
						MainView mainView = new MainView(usuario);
						mainView.setVisible(true);
						dispose(); // Cerrar ventana de login
					} else {
						// Login fallido
						JOptionPane.showMessageDialog(LoginView.this, 
							"Número de teléfono o contraseña incorrectos", 
							"Error de autenticación", 
							JOptionPane.ERROR_MESSAGE);
					}
				} catch(ChatControllerException | RepositorioException | EntidadNoEncontrada ex) {
					JOptionPane.showMessageDialog(LoginView.this, 
						"Error: " + ex.getMessage(), 
						"Error", 
						JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		// Mouse Listener para etiqueta de registro
		lblRegistro.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				RegisterView registerView = new RegisterView(LoginView.this);
				registerView.setVisible(true);
				//setVisible(false);
			}
			
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				lblRegistro.setText("<html><u>¿No tienes cuenta? Regístrate</u></html>");
			}
			
			public void mouseExited(java.awt.event.MouseEvent evt) {
				lblRegistro.setText("¿No tienes cuenta? Regístrate");
			}
		});
	}
}
