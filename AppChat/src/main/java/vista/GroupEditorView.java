package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import dominio.controlador.ChatController;
import dominio.controlador.ChatControllerException;
import dominio.modelo.ContactoIndividual;
import dominio.modelo.Grupo;
import utils.EstiloApp;
import utils.Utils;

/**
 * Diálogo para editar la información de un grupo (nombre e imagen)
 */
public class GroupEditorView extends JDialog {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JLabel lblImagen;
    private JTextField txtNombre;
    private File archivoImagen;
    private String rutaImagen;
    private Grupo grupo;
    private ChatController controlador;
    private boolean cambiosRealizados = false;
    
    /**
     * Constructor con el frame padre y el grupo a editar
     */
    public GroupEditorView(JFrame parent, Grupo grupo) {
        super(parent, "Editar Grupo", true);
        this.grupo = grupo;
        this.rutaImagen = grupo.getImagen();
        try {
			this.controlador = ChatController.getUnicaInstancia();
		} catch (ChatControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // En caso de error, continuamos con el Look and Feel por defecto
        }
        initComponents();
    }
    
    /**
     * Inicializa los componentes del diálogo
     */
    private void initComponents() {
        // Configurar la ventana basada en la resolución de la pantalla
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = Math.min(600, (int)(screenSize.width * 0.5));
        int height = Math.min(650, (int)(screenSize.height * 0.7));
        
        setResizable(true);
        setBounds(100, 100, width, height);
        setMinimumSize(new Dimension(450, 550));
        getContentPane().setBackground(EstiloApp.COLOR_FONDO);
        getContentPane().setLayout(new BorderLayout());
        
        // Panel principal sin scroll
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(EstiloApp.COLOR_FONDO);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        
        // Panel de contenido
        contentPane = new JPanel();
        contentPane.setBackground(EstiloApp.COLOR_FONDO);
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        mainPanel.add(contentPane, BorderLayout.CENTER);
        
        // Título
        JLabel lblTitulo = new JLabel("Editar Grupo");
        lblTitulo.setFont(EstiloApp.FUENTE_TITULO_MEDIO);
        lblTitulo.setForeground(EstiloApp.COLOR_PRIMARIO);
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        contentPane.add(lblTitulo);
        
        // Panel para la imagen con tamaño ajustable
        JPanel panelImagen = new JPanel(new BorderLayout());
        panelImagen.setBackground(EstiloApp.COLOR_FONDO);
        panelImagen.setMaximumSize(new Dimension(width, 350));
        panelImagen.setPreferredSize(new Dimension(width - 100, 300));
        
        // Contenedor para centrar la imagen
        JPanel contenedorImagen = new JPanel(new FlowLayout(FlowLayout.CENTER));
        contenedorImagen.setBackground(EstiloApp.COLOR_FONDO);
        
        // Label para mostrar la imagen
        lblImagen = new JLabel();
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagen.setPreferredSize(new Dimension(250, 230));
        lblImagen.setMinimumSize(new Dimension(200, 180));
        
        // Borde para la imagen que sea visible pero sutil
        lblImagen.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(EstiloApp.COLOR_PRIMARIO.getRed(), EstiloApp.COLOR_PRIMARIO.getGreen(), EstiloApp.COLOR_PRIMARIO.getBlue(), 150), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Cargar la imagen actual
        actualizarImagenGrupo();
        
        contenedorImagen.add(lblImagen);
        panelImagen.add(contenedorImagen, BorderLayout.CENTER);
        
        // Añadir panel de imagen al contenedor principal
        contentPane.add(panelImagen);
        contentPane.add(Box.createRigidArea(new Dimension(0, 5)));
        
        // Botón para cambiar la imagen
        JPanel panelBotonImagen = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotonImagen.setBackground(EstiloApp.COLOR_FONDO);
        
        JButton btnCambiarImagen = new JButton("Cambiar Imagen");
        btnCambiarImagen.setFont(EstiloApp.FUENTE_BUTTON);
        btnCambiarImagen.setBackground(EstiloApp.COLOR_SECUNDARIO);
        btnCambiarImagen.setForeground(EstiloApp.COLOR_PRIMARIO);
        btnCambiarImagen.setFocusPainted(false);
        btnCambiarImagen.setContentAreaFilled(true);
        btnCambiarImagen.setBorderPainted(true);
        btnCambiarImagen.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloApp.COLOR_PRIMARIO),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        btnCambiarImagen.addActionListener(e -> {
            AgregarFotoPerfilView selectorImagen = new AgregarFotoPerfilView(this);
            List<File> imagenes = selectorImagen.showDialog();
            if (imagenes != null && !imagenes.isEmpty()) {
                archivoImagen = imagenes.get(0);
                try {
                    rutaImagen = Utils.getRutaResourceFromFile(archivoImagen);
                    actualizarImagenGrupo();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, 
                            "Error al cargar la imagen: " + ex.getMessage(), 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
        
        panelBotonImagen.add(btnCambiarImagen);
        contentPane.add(panelBotonImagen);
        contentPane.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Panel para el nombre
        JPanel panelNombre = new JPanel(new BorderLayout(0, 10));
        panelNombre.setBackground(EstiloApp.COLOR_FONDO);
        panelNombre.setMaximumSize(new Dimension(width - 80, 80));
        
        JLabel lblNombreTitulo = new JLabel("Nombre del grupo:");
        lblNombreTitulo.setFont(EstiloApp.FUENTE_LABEL);
        panelNombre.add(lblNombreTitulo, BorderLayout.NORTH);
        
        txtNombre = new JTextField();
        txtNombre.setFont(EstiloApp.FUENTE_NORMAL);
        txtNombre.setText(grupo.getNombre());
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        panelNombre.add(txtNombre, BorderLayout.CENTER);
        
        contentPane.add(panelNombre);
        contentPane.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Panel para ver/editar miembros del grupo
        JPanel panelVerMiembros = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelVerMiembros.setBackground(EstiloApp.COLOR_FONDO);
        
        JButton btnGestionarMiembros = new JButton("Gestionar Miembros del Grupo");
        btnGestionarMiembros.setFont(EstiloApp.FUENTE_BUTTON);
        btnGestionarMiembros.setBackground(EstiloApp.COLOR_SECUNDARIO);
        btnGestionarMiembros.setForeground(EstiloApp.COLOR_PRIMARIO);
        btnGestionarMiembros.setFocusPainted(false);
        btnGestionarMiembros.setContentAreaFilled(true);
        btnGestionarMiembros.setBorderPainted(true);
        btnGestionarMiembros.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloApp.COLOR_PRIMARIO),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        btnGestionarMiembros.addActionListener(e -> {
            abrirGestorMiembrosGrupo();
        });
        
        panelVerMiembros.add(btnGestionarMiembros);
        contentPane.add(panelVerMiembros);
        contentPane.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Panel de botones (Guardar y Cancelar)
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBotones.setBackground(EstiloApp.COLOR_FONDO);
        
        JButton btnEliminarGrupo = new JButton("Eliminar Grupo");
        btnEliminarGrupo.setFont(EstiloApp.FUENTE_BUTTON);
        btnEliminarGrupo.setBackground(EstiloApp.COLOR_ERROR);
        btnEliminarGrupo.setForeground(EstiloApp.COLOR_ERROR);
        btnEliminarGrupo.setFocusPainted(false);
        btnEliminarGrupo.setContentAreaFilled(true);
        btnEliminarGrupo.setBorderPainted(true);
        btnEliminarGrupo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloApp.COLOR_ERROR.darker()),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        btnEliminarGrupo.addActionListener(e -> eliminarGrupo());
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(EstiloApp.FUENTE_BUTTON);
        btnCancelar.setBackground(EstiloApp.COLOR_SECUNDARIO);
        btnCancelar.setForeground(EstiloApp.COLOR_TEXTO);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setContentAreaFilled(true);
        btnCancelar.setBorderPainted(true);
        btnCancelar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloApp.COLOR_TEXTO),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        btnCancelar.addActionListener(e -> dispose());
        
        JButton btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setFont(EstiloApp.FUENTE_BUTTON);
        btnGuardar.setBackground(EstiloApp.COLOR_SECUNDARIO);
        btnGuardar.setForeground(EstiloApp.COLOR_PRIMARIO);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setContentAreaFilled(true);
        btnGuardar.setBorderPainted(true);
        btnGuardar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloApp.COLOR_PRIMARIO.darker()),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        btnGuardar.addActionListener(e -> guardarCambios());
        
        panelBotones.add(btnEliminarGrupo);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);
        contentPane.add(panelBotones);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Centrar diálogo en la pantalla
        setLocationRelativeTo(getOwner());
    }
    
    /**
     * Abre un diálogo para gestionar los miembros del grupo
     */
    private void abrirGestorMiembrosGrupo() {
        JDialog dialog = new JDialog(this, "Gestionar Miembros - " + grupo.getNombre(), true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(650, 500);
        dialog.setLocationRelativeTo(this);
        
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelPrincipal.setBackground(EstiloApp.COLOR_FONDO);
        
        // Título
        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setOpaque(false);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JLabel lblTitulo = new JLabel("Gestionar miembros del grupo");
        lblTitulo.setFont(EstiloApp.FUENTE_SUBTITULO);
        lblTitulo.setForeground(EstiloApp.COLOR_PRIMARIO);
        panelTitulo.add(lblTitulo, BorderLayout.CENTER);
        
        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        
        // Panel central con listas de contactos
        JPanel panelCentral = new JPanel(new BorderLayout(10, 0));
        panelCentral.setOpaque(false);
        
        // Panel izquierdo - Contactos disponibles
        JPanel panelIzquierdo = new JPanel(new BorderLayout(0, 10));
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(EstiloApp.COLOR_PRIMARIO, 1),
                        "Contactos disponibles"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        DefaultListModel<ContactoIndividual> modeloContactosDisponibles = new DefaultListModel<>();
        JList<ContactoIndividual> listaContactosDisponibles = new JList<>(modeloContactosDisponibles);
        listaContactosDisponibles.setCellRenderer(new ContactoListCellRenderer());
        listaContactosDisponibles.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaContactosDisponibles.setBackground(EstiloApp.COLOR_FONDO);
        
        // Cargar contactos disponibles (aquellos que no están ya en el grupo)
        List<ContactoIndividual> contactosUsuario = controlador.getUsuarioActual().getContactos()
                .stream()
                .filter(c -> c instanceof ContactoIndividual)
                .map(c -> (ContactoIndividual) c)
                .toList();
        
        List<ContactoIndividual> miembrosGrupo = grupo.getMiembros();
        
        // Panel derecho - Miembros actuales del grupo
        JPanel panelDerecho = new JPanel(new BorderLayout(0, 10));
        panelDerecho.setOpaque(false);
        panelDerecho.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(EstiloApp.COLOR_PRIMARIO, 1),
                        "Miembros del grupo"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        DefaultListModel<ContactoIndividual> modeloContactosMiembros = new DefaultListModel<>();
        JList<ContactoIndividual> listaContactosMiembros = new JList<>(modeloContactosMiembros);
        listaContactosMiembros.setCellRenderer(new ContactoListCellRenderer());
        listaContactosMiembros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaContactosMiembros.setBackground(EstiloApp.COLOR_FONDO);
        
        // Cargar miembros actuales del grupo
        for (ContactoIndividual miembro : miembrosGrupo) {
            modeloContactosMiembros.addElement(miembro);
        }
        
        // Ahora cargar los contactos disponibles después de haber declarado listaContactosMiembros
        for (ContactoIndividual contacto : contactosUsuario) {
            // Solo añadir si no está ya en el grupo
            boolean yaEsMiembro = false;
            for (ContactoIndividual miembro : miembrosGrupo) {
                if (contacto.getUsuario().getTelefono().equals(miembro.getUsuario().getTelefono())) {
                    yaEsMiembro = true;
                    break;
                }
            }
            if (!yaEsMiembro) {
                modeloContactosDisponibles.addElement(contacto);
            }
        }
        
        JScrollPane scrollContactosDisponibles = new JScrollPane(listaContactosDisponibles);
        scrollContactosDisponibles.setPreferredSize(new Dimension(200, 300));
        panelIzquierdo.add(scrollContactosDisponibles, BorderLayout.CENTER);
        
        // Botón para añadir contactos al grupo
        JButton btnAgregarAlGrupo = new JButton("Agregar al grupo ►");
        btnAgregarAlGrupo.setFont(EstiloApp.FUENTE_BUTTON);
        btnAgregarAlGrupo.setBackground(EstiloApp.COLOR_PRIMARIO);
        btnAgregarAlGrupo.setForeground(EstiloApp.COLOR_PRIMARIO);
        btnAgregarAlGrupo.setFocusPainted(false);
        btnAgregarAlGrupo.addActionListener(e -> {
            List<ContactoIndividual> seleccionados = listaContactosDisponibles.getSelectedValuesList();
            if (!seleccionados.isEmpty()) {
                for (ContactoIndividual contacto : seleccionados) {
                    try {
                        controlador.agregarContactoAGrupo(grupo, contacto);
                        modeloContactosDisponibles.removeElement(contacto);
                        ((DefaultListModel<ContactoIndividual>)listaContactosMiembros.getModel()).addElement(contacto);
                        cambiosRealizados = true;
                    } catch (ChatControllerException ex) {
                        JOptionPane.showMessageDialog(dialog,
                                "Error al añadir contacto: " + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "Seleccione al menos un contacto para añadir",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        panelIzquierdo.add(btnAgregarAlGrupo, BorderLayout.SOUTH);
        
        JScrollPane scrollContactosMiembros = new JScrollPane(listaContactosMiembros);
        scrollContactosMiembros.setPreferredSize(new Dimension(200, 300));
        panelDerecho.add(scrollContactosMiembros, BorderLayout.CENTER);
        
        // Botón para quitar contactos del grupo
        JButton btnQuitarDelGrupo = new JButton("◄ Quitar del grupo");
        btnQuitarDelGrupo.setFont(EstiloApp.FUENTE_BUTTON);
        btnQuitarDelGrupo.setBackground(EstiloApp.COLOR_ERROR);
        btnQuitarDelGrupo.setForeground(EstiloApp.COLOR_ERROR);
        btnQuitarDelGrupo.setFocusPainted(false);
        btnQuitarDelGrupo.addActionListener(e -> {
            ContactoIndividual seleccionado = listaContactosMiembros.getSelectedValue();
            if (seleccionado != null) {
                try {
                    controlador.eliminarContactoDeGrupo(grupo, seleccionado);
                    ((DefaultListModel<ContactoIndividual>)listaContactosMiembros.getModel()).removeElement(seleccionado);
                    modeloContactosDisponibles.addElement(seleccionado);
                    cambiosRealizados = true;
                } catch (ChatControllerException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Error al eliminar contacto: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "Seleccione un contacto para quitar del grupo",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        panelDerecho.add(btnQuitarDelGrupo, BorderLayout.SOUTH);
        
        // Añadir paneles al panel central
        panelCentral.add(panelIzquierdo, BorderLayout.WEST);
        panelCentral.add(panelDerecho, BorderLayout.EAST);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        
        // Panel inferior con botón de cerrar
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelInferior.setOpaque(false);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(EstiloApp.FUENTE_BUTTON);
        btnCerrar.setBackground(EstiloApp.COLOR_SECUNDARIO);
        btnCerrar.setForeground(EstiloApp.COLOR_PRIMARIO);
        btnCerrar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloApp.COLOR_PRIMARIO),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        btnCerrar.addActionListener(e -> dialog.dispose());
        
        panelInferior.add(btnCerrar);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
        
        dialog.add(panelPrincipal);
        dialog.setVisible(true);
    }
    
    /**
     * Guarda los cambios realizados en el grupo
     */
    private void guardarCambios() {
        try {
            boolean nombreActualizado = false;
            boolean imagenActualizada = false;
            
            // Comprobar si ha cambiado el nombre
            String nuevoNombre = txtNombre.getText().trim();
            if (nuevoNombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                        "El nombre del grupo no puede estar vacío", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!nuevoNombre.equals(grupo.getNombre())) {
                nombreActualizado = true;
            }
            
            // Comprobar si ha cambiado la imagen
            if (!rutaImagen.equals(grupo.getImagen())) {
                imagenActualizada = true;
            }
            
            // Si ha habido cambios, actualizar el grupo
            if (nombreActualizado || imagenActualizada) {
                controlador.actualizarGrupo(grupo, nuevoNombre, rutaImagen);
                cambiosRealizados = true;
                JOptionPane.showMessageDialog(this, 
                        "Grupo actualizado correctamente", 
                        "Éxito", 
                        JOptionPane.INFORMATION_MESSAGE);
            } else if (!cambiosRealizados) {
                JOptionPane.showMessageDialog(this, 
                        "No se detectaron cambios en el grupo", 
                        "Información", 
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Grupo actualizado correctamente", 
                        "Éxito", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
            
            dispose();
        } catch (ChatControllerException ex) {
            JOptionPane.showMessageDialog(this, 
                    "Error al guardar los cambios: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    /**
     * Actualiza la imagen del grupo en la interfaz
     */
    private void actualizarImagenGrupo() {
        try {
            // Cargar y mostrar la imagen actual del grupo
            ImageIcon iconoImagen = new ImageIcon(getClass().getResource(rutaImagen));
            
            // Calcular dimensiones para que se mantenga la proporción pero quepa en el espacio
            int anchoOriginal = iconoImagen.getIconWidth();
            int altoOriginal = iconoImagen.getIconHeight();
            
            // Tamaño máximo para la imagen
            int anchoMax = 220;
            int altoMax = 200;
            
            // Si la imagen es muy pequeña, no escalarla hacia arriba
            if (anchoOriginal <= anchoMax && altoOriginal <= altoMax) {
                lblImagen.setIcon(iconoImagen);
                lblImagen.setText("");
                return;
            }
            
            // Calcular escala manteniendo proporción
            double escalaAncho = (double) anchoMax / anchoOriginal;
            double escalaAlto = (double) altoMax / altoOriginal;
            double escala = Math.min(escalaAncho, escalaAlto);
            
            // Nuevas dimensiones
            int anchoNuevo = (int) (anchoOriginal * escala);
            int altoNuevo = (int) (altoOriginal * escala);
            
            // Escalar la imagen con alta calidad
            Image imagenEscalada = iconoImagen.getImage().getScaledInstance(
                    anchoNuevo, altoNuevo, Image.SCALE_SMOOTH);
            
            // Mostrar la imagen
            lblImagen.setIcon(new ImageIcon(imagenEscalada));
            lblImagen.setText("");
        } catch (Exception ex) {
            lblImagen.setIcon(null);
            lblImagen.setText("No se pudo cargar la imagen");
            ex.printStackTrace();
        }
    }
    
    /**
     * Indica si se realizaron cambios que requieren actualizar la interfaz
     * @return true si se realizaron cambios
     */
    public boolean isCambiosRealizados() {
        return cambiosRealizados;
    }
    
    /**
     * Elimina el grupo actual de la lista de contactos del usuario
     */
    private void eliminarGrupo() {
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar el grupo '" + grupo.getNombre() + "'?\nEsta acción no se puede deshacer.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        
        if (respuesta == JOptionPane.YES_OPTION) {
            try {
                // Usar el método eliminarContacto que funciona tanto para contactos individuales como para grupos
                controlador.eliminarContacto(grupo);
                
                JOptionPane.showMessageDialog(this,
                        "El grupo ha sido eliminado correctamente.",
                        "Grupo eliminado",
                        JOptionPane.INFORMATION_MESSAGE);
                
                cambiosRealizados = true;
                dispose();
            } catch (ChatControllerException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar el grupo: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}