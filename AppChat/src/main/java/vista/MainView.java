package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
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
import javax.swing.JDialog;

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
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import java.awt.FlowLayout;

public class MainView extends JFrame {

	private static final long serialVersionUID = 1L;
	
	// Constantes para la interfaz (coinciden con las de LoginView)
    private static final Color COLOR_PRIMARIO = new Color(25, 118, 210);
    private static final Color COLOR_SECUNDARIO = new Color(239, 246, 255);
    private static final Color COLOR_TEXTO = new Color(33, 33, 33);
    private static final Color COLOR_FONDO = new Color(250, 250, 250);
    private static final Font FUENTE_TITULO = new Font("Arial", Font.BOLD, 24);
    private static final Font FUENTE_SUBTITULO = new Font("Arial", Font.BOLD, 18);
    private static final Font FUENTE_LABEL = new Font("Arial", Font.BOLD, 14);
    private static final Font FUENTE_NORMAL = new Font("Arial", Font.PLAIN, 14);
    private static final Font FUENTE_BUTTON = new Font("Arial", Font.BOLD, 13);
	
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
		setBounds(100, 100, 850, 750);
		setTitle("AppChat - Mensajería");
        controlador = ChatControllerStub.getUnicaInstancia();
        listaContactos = new JList<ContactoIndividual>();
		contentPane = new JPanel();
		contentPane.setBackground(COLOR_FONDO);
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		// Panel superior con cabecera y botones
		JPanel panelSuperior = new JPanel();
		panelSuperior.setBackground(COLOR_PRIMARIO);
		panelSuperior.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		contentPane.add(panelSuperior, BorderLayout.NORTH);
		panelSuperior.setLayout(new BorderLayout(10, 0));
		
		// Panel para foto perfil y nombre usuario
		JPanel panelUsuario = new JPanel(new BorderLayout(10, 0));
		panelUsuario.setOpaque(false);
		
		// Botón de foto de perfil con bordes redondeados
		JButton btnFotoPerfil = new JButton("");
		btnFotoPerfil.setPreferredSize(new Dimension(50, 50));
		btnFotoPerfil.setBorder(BorderFactory.createEmptyBorder());
		btnFotoPerfil.setFocusPainted(false);
		btnFotoPerfil.setContentAreaFilled(false);
		
		try {
		    String rutaFoto = usuarioActual.getFotoPerfil();
		    URL urlFoto = getClass().getResource(rutaFoto);
		    if (urlFoto != null) {
		        Image imagenOriginal = ImageIO.read(urlFoto);
		        int dimension = 50;
		        Image imagenEscalada = imagenOriginal.getScaledInstance(dimension, dimension, Image.SCALE_SMOOTH);
		        btnFotoPerfil.setIcon(new ImageIcon(imagenEscalada));
		    } else {
		        System.err.println("No se pudo cargar la imagen: " + rutaFoto);
		    }
		} catch (IOException ex) {
		    ex.printStackTrace();
		}
		
		// Etiqueta con nombre de usuario
		JLabel lblNombreUsuario = new JLabel(usuarioActual.getNombre());
		lblNombreUsuario.setFont(FUENTE_SUBTITULO);
		lblNombreUsuario.setForeground(Color.WHITE);
		
		// Centrar verticalmente los elementos del panel usuario
		JPanel centroUsuario = new JPanel(new BorderLayout());
        centroUsuario.setOpaque(false);
        centroUsuario.add(lblNombreUsuario, BorderLayout.CENTER);
		
		panelUsuario.add(btnFotoPerfil, BorderLayout.WEST);
		panelUsuario.add(centroUsuario, BorderLayout.CENTER);
		
		panelSuperior.add(panelUsuario, BorderLayout.WEST);
		
		// Panel de botones de acción centrado verticalmente
		JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.setOpaque(false);
        
        // Envolver panelBotones en un panel de BorderLayout para centrarlo verticalmente
        JPanel contenedorBotones = new JPanel(new BorderLayout());
        contenedorBotones.setOpaque(false);
        contenedorBotones.add(panelBotones, BorderLayout.CENTER);
		
		// Estilo común para botones
		Color colorBotonPrimario = COLOR_SECUNDARIO;
		Color colorTextoBoton = COLOR_PRIMARIO;
		
		// Botón Agregar Contacto
		JButton btnAgregarContacto = crearBoton("+C", "Agregar Contacto", colorBotonPrimario, colorTextoBoton);
		btnAgregarContacto.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        JDialog dialog = new JDialog(MainView.this, "Agregar Contacto", true);
		        dialog.getContentPane().add(new AgregarContactoPanel());
		        dialog.pack();
		        dialog.setLocationRelativeTo(MainView.this);
		        dialog.setVisible(true);
		        cargarContactos();
		    }
		});
		
		// Botón Agregar Grupo
		JButton btnAgregarGrupo = crearBoton("+G", "Agregar Grupo", colorBotonPrimario, colorTextoBoton);
		btnAgregarGrupo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    JDialog dialog = new JDialog(MainView.this, "Crear Grupo", true);
		        dialog.getContentPane().add(new GroupView());
		        dialog.pack();
		        dialog.setLocationRelativeTo(MainView.this);
		        dialog.setVisible(true);
		        cargarContactos(); // Recargar contactos para mostrar el nuevo grupo
			}
		});
		
		// Botón Contactos
		JButton btnContactos = crearBoton("Contactos", null, colorBotonPrimario, colorTextoBoton);
		btnContactos.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        if (usuarioActual != null) {
		            List<ContactoIndividual> contactos = usuarioActual.getContactos()
		                .stream()
		                .filter(c -> c instanceof ContactoIndividual)
		                .map(c -> (ContactoIndividual) c)
		                .toList();
		            
		            ContactosTablePanel panelContactos = new ContactosTablePanel(contactos);
		            
		            JDialog dialog = new JDialog(MainView.this, "Lista de Contactos", true);
		            dialog.getContentPane().add(panelContactos);
		            dialog.pack();
		            dialog.setLocationRelativeTo(MainView.this);
		            dialog.setVisible(true);
		        } else {
		            JOptionPane.showMessageDialog(MainView.this, "No hay usuario autenticado.", "Error", JOptionPane.ERROR_MESSAGE);
		        }
		    }
		});
		
		// Botón Premium
		JButton btnPremium = crearBoton("Premium", null, new Color(255, 215, 0), COLOR_TEXTO);
		
		// Botón Buscar Mensajes
		JButton btnBuscarMensajes = crearBoton("Buscar Mensajes", null, colorBotonPrimario, colorTextoBoton);
		btnBuscarMensajes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog(MainView.this, "Buscar Mensajes", true);
                dialog.getContentPane().add(new SearchView());
                dialog.pack();
                dialog.setLocationRelativeTo(MainView.this);
                dialog.setVisible(true);
            }
        });
		
		// Botón Logout
		JButton btnLogout = crearBoton("Logout", null, new Color(255, 235, 235), new Color(220, 50, 50));
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controlador.cerrarSesion();
		        LoginView login = new LoginView();
		        login.main(null);
		        dispose();
            }
		});
		
		// Añadir todos los botones al panel
		panelBotones.add(btnAgregarContacto);
		panelBotones.add(btnAgregarGrupo);
		panelBotones.add(btnContactos);
		panelBotones.add(btnPremium);
		panelBotones.add(btnBuscarMensajes);
		panelBotones.add(btnLogout);
		
		panelSuperior.add(contenedorBotones, BorderLayout.EAST);
		
		// Panel de contactos (lateral izquierdo)
		panelContatos = new JPanel(new BorderLayout());
		panelContatos.setBackground(COLOR_FONDO);
		panelContatos.setBorder(BorderFactory.createCompoundBorder(
		        BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(200, 200, 200)),
		        BorderFactory.createEmptyBorder(5, 5, 5, 5)
		));
		contentPane.add(panelContatos, BorderLayout.WEST);
		
		// Panel título para la lista de contactos
		JPanel panelTituloContactos = new JPanel(new BorderLayout());
		panelTituloContactos.setOpaque(false);
		panelTituloContactos.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
		
		JLabel lblTituloContactos = new JLabel("Mis Contactos");
		lblTituloContactos.setFont(FUENTE_LABEL);
		lblTituloContactos.setForeground(COLOR_PRIMARIO);
		
		panelTituloContactos.add(lblTituloContactos, BorderLayout.CENTER);
		panelContatos.add(panelTituloContactos, BorderLayout.NORTH);
		
		// JList para mostrar los contactos con estilo mejorado
        listaContactos.setCellRenderer(new ContactoListCellRenderer());
        listaContactos.setBackground(COLOR_FONDO);
        listaContactos.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        listaContactos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                ContactoIndividual contactoSeleccionado = listaContactos.getSelectedValue();
                if (contactoSeleccionado != null) {
                    if (!contactoSeleccionado.tieneNombre()) {
                        String nuevoNombre = utils.DialogoUtils.mostrarDialogoEntrada(
                                MainView.this, 
                                "Asigne un nombre al contacto seleccionado:", 
                                "Asignar nombre");
                                
                        if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                            contactoSeleccionado.setNombre(nuevoNombre.trim());
                            cargarContactos();
                            DefaultListModel<ContactoIndividual> model = (DefaultListModel<ContactoIndividual>) listaContactos.getModel();
                            for (int i = 0; i < model.getSize(); i++) {
                                if (model.getElementAt(i).equals(contactoSeleccionado)) {
                                    listaContactos.setSelectedIndex(i);
                                    break;
                                }
                            }
                        } else {
                            return;
                        }
                    }
                    contactoSeleccionado = listaContactos.getSelectedValue();
                    MainView.this.contactoSeleccionado = contactoSeleccionado;
                    if (contactoSeleccionado != null) {
                        ((ChatPanel) panelChat).cargarMensajesDe(contactoSeleccionado);
                    }
                }
            }
        });

        // Agregar la lista con scroll al panel
        JScrollPane scrollLista = new JScrollPane(listaContactos);
        scrollLista.setPreferredSize(new Dimension(220, 600));
        scrollLista.setBorder(BorderFactory.createEmptyBorder());
        scrollLista.getVerticalScrollBar().setUnitIncrement(16);
        panelContatos.add(scrollLista, BorderLayout.CENTER);
		
        // Panel central con CardLayout para cambiar entre vistas
        cardLayout = new CardLayout();
        panelCentral = new JPanel(cardLayout);
        panelCentral.setBackground(COLOR_FONDO);
        contentPane.add(panelCentral, BorderLayout.CENTER);
		
        // PANEL CHAT
        panelChat = new ChatPanel();
        ((ChatPanel) panelChat).setUsuarioActual(usuarioActual);
        panelCentral.add(panelChat, "panelChat");
        
        // PANEL BUSCADOR
        panelBuscador = new JPanel();
        panelBuscador.setBackground(COLOR_FONDO);
        JLabel lblBuscador = new JLabel("Aquí puedes buscar mensajes");
        lblBuscador.setFont(FUENTE_NORMAL);
        panelBuscador.add(lblBuscador);
        panelCentral.add(panelBuscador, "panelBuscador");

        // Inicia mostrando el panel de chat
        cardLayout.show(panelCentral, "panelChat");
        
        // Centrar la ventana en la pantalla
        setLocationRelativeTo(null);
	}
	
	// Método auxiliar para crear botones con estilo consistente
	private JButton crearBoton(String texto, String tooltip, Color colorFondo, Color colorTexto) {
	    JButton boton = new JButton(texto);
	    boton.setFont(FUENTE_BUTTON);
	    boton.setBackground(colorFondo);
	    boton.setForeground(colorTexto);
	    boton.setFocusPainted(false);
	    boton.setBorder(BorderFactory.createCompoundBorder(
	            BorderFactory.createLineBorder(colorTexto, 1),
	            BorderFactory.createEmptyBorder(7, 15, 7, 15)
	    ));
	    if (tooltip != null) {
	        boton.setToolTipText(tooltip);
	    }
	    return boton;
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
