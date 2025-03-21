package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import dominio.modelo.ContactoIndividual;
import dominio.modelo.Usuario;
import tds.BubbleText;
import utils.ChatControllerStub;

import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import javax.swing.ImageIcon;
import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.border.BevelBorder;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;

public class MainView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelContatos;
	private JPanel panelCentral;
	private JPanel panelChat;
	private JPanel panelBuscador;
	private CardLayout cardLayout;
    private JList<ContactoIndividual> listaContactos;
	private static ChatControllerStub controlador;
	private Usuario usuarioActual;
	private ContactoIndividual contactoSeleccionado;
	
	public MainView(Usuario usuario) {
        this.usuarioActual = usuario;
        initComponents();
        cargarContactos(); 
    }
	
	public void initComponents(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 750);
        controlador = ChatControllerStub.getUnicaInstancia();
        listaContactos = new JList<ContactoIndividual>();
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{10, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0};
		gbl_panel.rowHeights = new int[]{5, 23, 5, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JButton btnFotoPerfil = new JButton("");
		GridBagConstraints gbc_btnFotoPerfil = new GridBagConstraints();
		gbc_btnFotoPerfil.insets = new Insets(0, 0, 5, 5);
		gbc_btnFotoPerfil.fill = GridBagConstraints.VERTICAL;
		gbc_btnFotoPerfil.gridx = 1;
		gbc_btnFotoPerfil.gridy = 1;
		panel.add(btnFotoPerfil, gbc_btnFotoPerfil);
		try {
		    // Obtener la ruta de la foto de perfil del usuario actual
		    String rutaFoto = usuarioActual.getFotoPerfil(); // Ej: "/FotosPerfil/Perfil_1.png"
		    // Buscar el recurso en el classpath
		    URL urlFoto = getClass().getResource(rutaFoto);
		    if (urlFoto != null) {
		        // Leer la imagen
		        Image imagenOriginal = ImageIO.read(urlFoto);
		        // Escalar la imagen (ajusta el ancho y alto según necesites)
		        int anchoDeseado = 50;
		        int altoDeseado = 50;
		        Image imagenEscalada = imagenOriginal.getScaledInstance(anchoDeseado, altoDeseado, Image.SCALE_SMOOTH);
		        // Asignar la imagen escalada como ícono del botón
		        btnFotoPerfil.setIcon(new ImageIcon(imagenEscalada));
		    } else {
		        System.err.println("No se pudo cargar la imagen: " + rutaFoto);
		    }
		} catch (IOException ex) {
		    ex.printStackTrace();
		}
		
		JButton btnNewButton_2 = new JButton("Añadir Contacto");
		btnNewButton_2.setBackground(SystemColor.inactiveCaption);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_2.gridx = 3;
		gbc_btnNewButton_2.gridy = 1;
		panel.add(btnNewButton_2, gbc_btnNewButton_2);
		
		JButton btnNewButton_1 = new JButton("Añádir Grupo");
		btnNewButton_1.setBackground(SystemColor.inactiveCaption);
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_1.gridx = 4;
		gbc_btnNewButton_1.gridy = 1;
		panel.add(btnNewButton_1, gbc_btnNewButton_1);
		
		JButton btnNewButton_3 = new JButton("Ver Contactos");
		btnNewButton_3.setBackground(SystemColor.inactiveCaption);
		GridBagConstraints gbc_btnNewButton_3 = new GridBagConstraints();
		gbc_btnNewButton_3.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_3.gridx = 5;
		gbc_btnNewButton_3.gridy = 1;
		panel.add(btnNewButton_3, gbc_btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("Premium");
		btnNewButton_4.setBackground(SystemColor.activeCaption);
		GridBagConstraints gbc_btnNewButton_4 = new GridBagConstraints();
		gbc_btnNewButton_4.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_4.gridx = 6;
		gbc_btnNewButton_4.gridy = 1;
		panel.add(btnNewButton_4, gbc_btnNewButton_4);
		
		JButton btnNewButton_5 = new JButton("Buscar Mensajes");
		btnNewButton_5.setBackground(SystemColor.inactiveCaption);
		GridBagConstraints gbc_btnNewButton_5 = new GridBagConstraints();
		gbc_btnNewButton_5.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_5.gridx = 7;
		gbc_btnNewButton_5.gridy = 1;
		panel.add(btnNewButton_5, gbc_btnNewButton_5);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.setBackground(SystemColor.info);
		GridBagConstraints gbc_btnLogout = new GridBagConstraints();
		gbc_btnLogout.insets = new Insets(0, 0, 5, 5);
		gbc_btnLogout.gridx = 8;
		gbc_btnLogout.gridy = 1;
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controlador.cerrarSesion();
		        LoginView login = new LoginView();
		        login.main(null);
		        dispose();
            }
		});
		panel.add(btnLogout, gbc_btnLogout);
		
		
		BorderLayout bl_panelContatos = new BorderLayout();
		panelContatos = new JPanel(bl_panelContatos);
		BorderLayout borderLayout = (BorderLayout) panelContatos.getLayout();
		borderLayout.setHgap(150);
		panelContatos.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		contentPane.add(panelContatos, BorderLayout.WEST);
		
		 // JList para mostrar los contactos
        listaContactos.setCellRenderer(new ContactoListCellRenderer()); // Aplica el renderizador
		listaContactos.addListSelectionListener(e -> {
			 if (!e.getValueIsAdjusting()) {
			        ContactoIndividual contactoSeleccionado = listaContactos.getSelectedValue();
			        if (contactoSeleccionado != null) {
			            this.contactoSeleccionado = contactoSeleccionado;
			            // Llamamos al método del ChatPanel para cargar los mensajes de este contacto
			            ((ChatPanel) panelChat).cargarMensajesDe(contactoSeleccionado);
			        }
			    }
		});

        // Agregar la lista con scroll al panel
        JScrollPane scrollLista = new JScrollPane(listaContactos);
        scrollLista.setPreferredSize(new Dimension(200, 600)); // Ajusta el tamaño
        panelContatos.add(scrollLista, BorderLayout.CENTER);
		
        cardLayout = new CardLayout();
        panelCentral = new JPanel(cardLayout);
        contentPane.add(panelCentral, BorderLayout.CENTER);
		
        // PANEL CHAT
        panelChat = new ChatPanel();
        panelCentral.add(panelChat, "panelChat");
        
        

        // PANEL BUSCADOR
        panelBuscador = new JPanel();
        panelBuscador.add(new JLabel("Aquí puedes buscar mensajes"));
        panelCentral.add(panelBuscador, "panelBuscador");

        // Inicia mostrando el panel de chat
        cardLayout.show(panelCentral, "panelChat");
        
	}

	private void cargarContactos() {
		 usuarioActual = controlador.getUsuarioActual(); // Obtiene el usuario autenticado
		    if (usuarioActual == null) {
		        System.err.println("No hay usuario autenticado.");
		        return;
		    }else
			{
				System.out.println("Usuario autenticado: " + usuarioActual.getNombre());
			}

		    List<ContactoIndividual> contactos = usuarioActual.getContactos()
		            .stream()
		            .filter(c -> c instanceof ContactoIndividual)
		            .map(c -> (ContactoIndividual) c)
		            .toList();

		    DefaultListModel<ContactoIndividual> model = new DefaultListModel<>();
		    contactos.forEach(model::addElement);
		    listaContactos.setModel(model);
    }
	


}
