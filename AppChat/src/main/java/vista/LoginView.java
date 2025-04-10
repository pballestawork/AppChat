package vista;

import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;

import dominio.controlador.ChatControllerException;
import dominio.modelo.Usuario;
import utils.ChatControllerStub;

import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

public class LoginView {

    private JFrame frmAppchat;
    private JTextField inputPhone;
    private JPasswordField passwordField;
    private static ChatControllerStub controlador;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    controlador = ChatControllerStub.getUnicaInstancia();
                    LoginView window = new LoginView();
                    window.frmAppchat.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public LoginView() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frmAppchat = new JFrame();
        frmAppchat.setResizable(false);
        frmAppchat.setAlwaysOnTop(true);
        frmAppchat.setTitle("AppChat");
        frmAppchat.setBackground(SystemColor.desktop);
        frmAppchat.setBounds(100, 100, 450, 350);
        frmAppchat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmAppchat.getContentPane().setLayout(new BorderLayout(0, 0));
        frmAppchat.setLocationRelativeTo(null);

        JPanel panel_superior = new JPanel();
        panel_superior.setBackground(SystemColor.desktop);
        frmAppchat.getContentPane().add(panel_superior, BorderLayout.NORTH);

        JLabel lblNewLabel = new JLabel("AppChat");
        lblNewLabel.setForeground(SystemColor.text);
        lblNewLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 60));
        panel_superior.add(lblNewLabel);

        JPanel panel_inferior = new JPanel();
        panel_inferior.setBackground(SystemColor.desktop);
        frmAppchat.getContentPane().add(panel_inferior, BorderLayout.SOUTH);

        JButton btnRegister = new JButton("Registrar");
        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new RegisterView(frmAppchat);
            }
        });
        btnRegister.setBackground(SystemColor.activeCaption);
        panel_inferior.add(btnRegister);

        JButton btnLogin = new JButton("Iniciar sesión");
        btnLogin.addActionListener(new ActionListener() {
            @SuppressWarnings("deprecation")
            public void actionPerformed(ActionEvent e) {
                try {
                    // Validación preliminar (opcional)
                    if(inputPhone.getText().trim().isEmpty() || new String(passwordField.getPassword()).trim().isEmpty()){
                        throw new IllegalArgumentException("Por favor, ingrese teléfono y contraseña.");
                    }
                    Usuario usuario = controlador.iniciarSesion(inputPhone.getText(), passwordField.getText());
                    if (usuario != null) {
                        System.out.println("Inicio de sesión exitoso: " + usuario.getNombre());
                        frmAppchat.dispose();
                        MainView mainView = new MainView(usuario);
                        mainView.setVisible(true);
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frmAppchat, ex.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
                } catch (ChatControllerException ex) {
                    JOptionPane.showMessageDialog(frmAppchat, ex.getMessage(), "Error de autenticación", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnLogin.setBackground(SystemColor.activeCaption);
        panel_inferior.add(btnLogin);

        JPanel panel_central = new JPanel();
        panel_central.setBackground(SystemColor.desktop);
        frmAppchat.getContentPane().add(panel_central, BorderLayout.CENTER);
        GridBagLayout gbl_panel_central = new GridBagLayout();
        gbl_panel_central.columnWidths = new int[]{100, 0, 100, 0};
        gbl_panel_central.rowHeights = new int[]{40, 0, 0, 10, 0, 0, 40, 0};
        gbl_panel_central.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_panel_central.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        panel_central.setLayout(gbl_panel_central);

        JLabel lblNewLabel_1 = new JLabel("Teléfono");
        lblNewLabel_1.setForeground(SystemColor.text);
        lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 16));
        GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
        gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_1.gridx = 1;
        gbc_lblNewLabel_1.gridy = 1;
        panel_central.add(lblNewLabel_1, gbc_lblNewLabel_1);

        inputPhone = new JTextField();
        inputPhone.setFont(new Font("Tahoma", Font.PLAIN, 12));
        inputPhone.setBackground(SystemColor.inactiveCaption);
        GridBagConstraints gbc_inputPhone = new GridBagConstraints();
        gbc_inputPhone.fill = GridBagConstraints.HORIZONTAL;
        gbc_inputPhone.insets = new Insets(0, 0, 5, 5);
        gbc_inputPhone.gridx = 1;
        gbc_inputPhone.gridy = 2;
        panel_central.add(inputPhone, gbc_inputPhone);
        inputPhone.setColumns(10);

        JLabel lblContrasea = new JLabel("Contraseña");
        lblContrasea.setForeground(SystemColor.text);
        lblContrasea.setFont(new Font("Tahoma", Font.BOLD, 16));
        GridBagConstraints gbc_lblContrasea = new GridBagConstraints();
        gbc_lblContrasea.insets = new Insets(0, 0, 5, 5);
        gbc_lblContrasea.gridx = 1;
        gbc_lblContrasea.gridy = 4;
        panel_central.add(lblContrasea, gbc_lblContrasea);

        passwordField = new JPasswordField();
        passwordField.setBackground(SystemColor.inactiveCaption);
        GridBagConstraints gbc_passwordField = new GridBagConstraints();
        gbc_passwordField.insets = new Insets(0, 0, 5, 5);
        gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
        gbc_passwordField.gridx = 1;
        gbc_passwordField.gridy = 5;
        panel_central.add(passwordField, gbc_passwordField);
    }
}
