package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import dominio.controlador.ChatController;
import dominio.controlador.ChatControllerException;
import dominio.modelo.ContactoIndividual;
import dominio.modelo.Usuario;

import utils.Utils;

/**
 * Panel que permite crear un nuevo grupo seleccionando contactos
 * de la lista de contactos del usuario.
 */
public class GroupView extends JPanel {
    
    private static final long serialVersionUID = 1L;
    
    // Constantes para la interfaz (coinciden con las de MainView)
    private static final Color COLOR_PRIMARIO = new Color(25, 118, 210);
    private static final Color COLOR_SECUNDARIO = new Color(239, 246, 255);
    private static final Color COLOR_TEXTO = new Color(33, 33, 33);
    private static final Color COLOR_FONDO = new Color(250, 250, 250);
    private static final Font FUENTE_TITULO = new Font("Arial", Font.BOLD, 24);
    private static final Font FUENTE_SUBTITULO = new Font("Arial", Font.BOLD, 18);
    private static final Font FUENTE_LABEL = new Font("Arial", Font.BOLD, 14);
    private static final Font FUENTE_NORMAL = new Font("Arial", Font.PLAIN, 14);
    private static final Font FUENTE_BUTTON = new Font("Arial", Font.BOLD, 13);
    
    private JList<ContactoIndividual> listaTodosContactos;
    private JList<ContactoIndividual> listaContactosSeleccionados;
    private DefaultListModel<ContactoIndividual> modeloTodosContactos;
    private DefaultListModel<ContactoIndividual> modeloContactosSeleccionados;
    private JTextField txtNombreGrupo;
    private JButton btnCrearGrupo;
    private JButton btnCancelar;
    private Usuario usuarioActual;
    private ChatController controlador;
    
    // Componentes para la selección de imagen
    private JLabel lblImagen;
    private File archivoImagen;
    private String rutaImagenGrupo;
    
    /**
     * Constructor que crea el panel de creación de grupos.
     */
    public GroupView() {
        try {
			controlador = ChatController.getUnicaInstancia();
		} catch (ChatControllerException e) {
			e.printStackTrace();
		}
        usuarioActual = controlador.getUsuarioActual();
        
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(COLOR_FONDO);
        
        // Panel título
        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setOpaque(false);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JLabel lblTitulo = new JLabel("Crear nuevo grupo");
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(COLOR_PRIMARIO);
        panelTitulo.add(lblTitulo, BorderLayout.CENTER);
        
        add(panelTitulo, BorderLayout.NORTH);
        
        // Panel central con los dos paneles de contactos
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        
        // Panel izquierdo - Todos los contactos
        JPanel panelIzquierdo = new JPanel(new BorderLayout(0, 10));
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(COLOR_PRIMARIO, 1),
                        "Contactos disponibles"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        modeloTodosContactos = new DefaultListModel<>();
        listaTodosContactos = new JList<>(modeloTodosContactos);
        listaTodosContactos.setCellRenderer(new ContactoListCellRenderer());
        listaTodosContactos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaTodosContactos.setBackground(COLOR_FONDO);
        
        JScrollPane scrollTodosContactos = new JScrollPane(listaTodosContactos);
        scrollTodosContactos.setPreferredSize(new Dimension(200, 300));
        panelIzquierdo.add(scrollTodosContactos, BorderLayout.CENTER);
        
        JButton btnAgregarAlGrupo = new JButton("Agregar al grupo ►");
        btnAgregarAlGrupo.setFont(FUENTE_BUTTON);
        btnAgregarAlGrupo.setBackground(COLOR_PRIMARIO);
        btnAgregarAlGrupo.setForeground(Color.WHITE);
        btnAgregarAlGrupo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarContactoAlGrupo();
            }
        });
        panelIzquierdo.add(btnAgregarAlGrupo, BorderLayout.SOUTH);
        
        // Panel derecho - Contactos seleccionados
        JPanel panelDerecho = new JPanel(new BorderLayout(0, 10));
        panelDerecho.setOpaque(false);
        panelDerecho.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(COLOR_PRIMARIO, 1),
                        "Miembros del grupo"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        modeloContactosSeleccionados = new DefaultListModel<>();
        listaContactosSeleccionados = new JList<>(modeloContactosSeleccionados);
        listaContactosSeleccionados.setCellRenderer(new ContactoListCellRenderer());
        listaContactosSeleccionados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaContactosSeleccionados.setBackground(COLOR_FONDO);
        
        JScrollPane scrollContactosSeleccionados = new JScrollPane(listaContactosSeleccionados);
        scrollContactosSeleccionados.setPreferredSize(new Dimension(200, 300));
        panelDerecho.add(scrollContactosSeleccionados, BorderLayout.CENTER);
        
        JButton btnQuitarDelGrupo = new JButton("◄ Quitar del grupo");
        btnQuitarDelGrupo.setFont(FUENTE_BUTTON);
        btnQuitarDelGrupo.setBackground(new Color(220, 50, 50));
        btnQuitarDelGrupo.setForeground(Color.WHITE);
        btnQuitarDelGrupo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quitarContactoDelGrupo();
            }
        });
        panelDerecho.add(btnQuitarDelGrupo, BorderLayout.SOUTH);
        
        // Añadir paneles a panel central
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panelCentral.add(panelIzquierdo, gbc);
        
        gbc.gridx = 1;
        panelCentral.add(panelDerecho, gbc);
        
        add(panelCentral, BorderLayout.CENTER);
        
        // Panel inferior - Nombre del grupo, imagen y botones
        JPanel panelInferior = new JPanel(new GridBagLayout());
        panelInferior.setOpaque(false);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Campo nombre del grupo
        JLabel lblNombreGrupo = new JLabel("Nombre del grupo:");
        lblNombreGrupo.setFont(FUENTE_LABEL);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        panelInferior.add(lblNombreGrupo, gbc);
        
        txtNombreGrupo = new JTextField(20);
        txtNombreGrupo.setFont(FUENTE_NORMAL);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        panelInferior.add(txtNombreGrupo, gbc);
        
        // Panel para la imagen
        JLabel lblFotoGrupo = new JLabel("Imagen del grupo:");
        lblFotoGrupo.setFont(FUENTE_LABEL);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        panelInferior.add(lblFotoGrupo, gbc);
        
        // Panel para la imagen y el botón con un layout más adecuado
        JPanel panelImagen = new JPanel(new BorderLayout(0, 10));
        panelImagen.setOpaque(false);
        
        // Panel para la imagen (arriba)
        JPanel panelImagenSuperior = new JPanel(new BorderLayout());
        panelImagenSuperior.setOpaque(false);
        panelImagenSuperior.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        
        // La imagen seleccionada
        lblImagen = new JLabel("No se ha seleccionado ninguna imagen");
        lblImagen.setFont(FUENTE_NORMAL);
        lblImagen.setForeground(Color.GRAY);
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagen.setPreferredSize(new Dimension(150, 150));
        panelImagenSuperior.add(lblImagen, BorderLayout.CENTER);
        
        // Panel para el botón (abajo)
        JPanel panelBotonImagen = new JPanel(new BorderLayout());
        panelBotonImagen.setOpaque(false);
        
        // Botón para seleccionar imagen
        JButton btnSeleccionarImagen = new JButton("Seleccionar Imagen");
        btnSeleccionarImagen.setFont(FUENTE_NORMAL);
        btnSeleccionarImagen.setBackground(COLOR_SECUNDARIO);
        btnSeleccionarImagen.setForeground(COLOR_PRIMARIO);
        btnSeleccionarImagen.setFocusPainted(false);
        btnSeleccionarImagen.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARIO),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        btnSeleccionarImagen.addActionListener(e -> {
            // Usar la versión mejorada de AgregarFotoPerfilView que acepta JDialog como propietario
            AgregarFotoPerfilView panelArrastre = new AgregarFotoPerfilView(
                (javax.swing.JDialog) SwingUtilities.getWindowAncestor(this));
            java.util.List<File> imagenes = panelArrastre.showDialog();
            
            if (imagenes != null && !imagenes.isEmpty()) {
                archivoImagen = imagenes.get(0);
                try {
                    rutaImagenGrupo = Utils.getRutaResourceFromFile(archivoImagen);
                    ImageIcon iconoImagen = new ImageIcon(getClass().getResource(rutaImagenGrupo));
                    Image imagenEscalada = iconoImagen.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    lblImagen.setIcon(new ImageIcon(imagenEscalada));
                    lblImagen.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, 
                            "Error al cargar la imagen: " + ex.getMessage(), 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
        
        panelBotonImagen.add(btnSeleccionarImagen, BorderLayout.CENTER);
        
        // Añadir al panel de imagen principal
        panelImagen.add(panelImagenSuperior, BorderLayout.CENTER);
        panelImagen.add(panelBotonImagen, BorderLayout.SOUTH);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panelInferior.add(panelImagen, gbc);
        
        // Botones
        JPanel panelBotones = new JPanel(new GridBagLayout());
        panelBotones.setOpaque(false);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        btnCrearGrupo = new JButton("Crear Grupo");
        btnCrearGrupo.setFont(FUENTE_BUTTON);
        btnCrearGrupo.setBackground(COLOR_PRIMARIO);
        btnCrearGrupo.setForeground(Color.WHITE);
        btnCrearGrupo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearGrupo();
            }
        });
        
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(FUENTE_BUTTON);
        btnCancelar.setBackground(COLOR_SECUNDARIO);
        btnCancelar.setForeground(COLOR_PRIMARIO);
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarPanel();
            }
        });
        
        GridBagConstraints gbcBtn = new GridBagConstraints();
        gbcBtn.insets = new Insets(5, 5, 5, 5);
        gbcBtn.fill = GridBagConstraints.HORIZONTAL;
        gbcBtn.gridx = 0;
        gbcBtn.gridy = 0;
        gbcBtn.weightx = 1.0;
        panelBotones.add(btnCancelar, gbcBtn);
        
        gbcBtn.gridx = 1;
        panelBotones.add(btnCrearGrupo, gbcBtn);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        panelInferior.add(panelBotones, gbc);
        
        add(panelInferior, BorderLayout.SOUTH);
        
        // Cargar contactos
        cargarContactos();
    }
    
    /**
     * Carga los contactos del usuario actual en la lista de contactos disponibles.
     */
    private void cargarContactos() {
        if (usuarioActual == null) {
            System.err.println("No hay usuario autenticado.");
            return;
        }
        
        // Obtener solo los contactos individuales (no los grupos) para añadirlos a la lista de selección
        List<ContactoIndividual> contactosIndividuales = usuarioActual.getContactos()
                .stream()
                .filter(c -> c instanceof ContactoIndividual)
                .map(c -> (ContactoIndividual) c)
                .toList();
        
        modeloTodosContactos.clear();
        contactosIndividuales.forEach(modeloTodosContactos::addElement);
    }
    
    /**
     * Agrega los contactos seleccionados de la lista de contactos disponibles a la lista de miembros del grupo.
     */
    private void agregarContactoAlGrupo() {
        List<ContactoIndividual> contactosSeleccionados = listaTodosContactos.getSelectedValuesList();
        if (contactosSeleccionados != null && !contactosSeleccionados.isEmpty()) {
            boolean hayDuplicados = false;
            
            // Añadir todos los contactos que no están ya en el grupo
            for (ContactoIndividual contacto : contactosSeleccionados) {
                boolean yaExiste = false;
                
                // Verificar que no esté ya en el grupo usando el teléfono como identificador único
                for (int i = 0; i < modeloContactosSeleccionados.size(); i++) {
                    ContactoIndividual contactoEnLista = modeloContactosSeleccionados.get(i);
                    
                    // Comparamos por el teléfono del usuario asociado al contacto
                    if (contactoEnLista.getUsuario().getTelefono().equals(contacto.getUsuario().getTelefono())) {
                        yaExiste = true;
                        hayDuplicados = true;
                        break;
                    }
                }
                
                // Si no existe, lo añadimos
                if (!yaExiste) {
                    modeloContactosSeleccionados.addElement(contacto);
                }
            }
            
            // Mostrar mensaje si hubo duplicados
            if (hayDuplicados) {
                JOptionPane.showMessageDialog(
                        this,
                        "Algunos contactos ya estaban en el grupo y no se han añadido de nuevo.",
                        "Contactos duplicados",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Por favor, seleccione uno o más contactos para añadir al grupo.",
                    "Ningún contacto seleccionado",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Quita el contacto seleccionado de la lista de miembros del grupo.
     */
    private void quitarContactoDelGrupo() {
        int indice = listaContactosSeleccionados.getSelectedIndex();
        if (indice != -1) {
            modeloContactosSeleccionados.remove(indice);
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Por favor, seleccione un contacto para quitar del grupo.",
                    "Ningún contacto seleccionado",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Crea un nuevo grupo con los contactos seleccionados y el nombre especificado.
     */
    private void crearGrupo() {
        String nombreGrupo = txtNombreGrupo.getText().trim();
        
        // Validar nombre del grupo
        if (nombreGrupo.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Por favor, ingrese un nombre para el grupo.",
                    "Nombre requerido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validar que haya al menos un miembro
        if (modeloContactosSeleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "El grupo debe tener al menos un miembro.",
                    "Sin miembros",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 	Validar que haya introducido la foto
        if (rutaImagenGrupo == null || rutaImagenGrupo.isEmpty()) {
            JOptionPane.showMessageDialog(
            		this, 
            		"Por favor, seleccione una foto de perfil de grupo.",
            		"Error",
            		JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Crear lista de miembros
        List<ContactoIndividual> miembros = new ArrayList<>();
        for (int i = 0; i < modeloContactosSeleccionados.size(); i++) {
            miembros.add(modeloContactosSeleccionados.get(i));
        }
        
        try {
            // Crear el grupo
            controlador.crearGrupo(miembros, nombreGrupo, rutaImagenGrupo);
            JOptionPane.showMessageDialog(
                    this,
                    "Grupo '" + nombreGrupo + "' creado exitosamente.",
                    "Grupo creado",
                    JOptionPane.INFORMATION_MESSAGE);
            cerrarPanel();
        } catch (ChatControllerException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al crear el grupo: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Cierra el panel (debe ser invocado desde un JDialog).
     */
    private void cerrarPanel() {
        if (getTopLevelAncestor() instanceof javax.swing.JDialog) {
            ((javax.swing.JDialog) getTopLevelAncestor()).dispose();
        }
    }
}
