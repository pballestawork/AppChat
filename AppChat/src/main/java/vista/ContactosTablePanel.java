package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import dominio.modelo.ContactoIndividual;
import dominio.controlador.ChatController;
import dominio.controlador.ChatControllerException;

public class ContactosTablePanel extends JPanel {

    // Constantes para la interfaz (coinciden con las de LoginView/RegisterView)
    private static final Color COLOR_PRIMARIO = new Color(25, 118, 210);
    private static final Color COLOR_SECUNDARIO = new Color(239, 246, 255);
    private static final Color COLOR_TEXTO = new Color(33, 33, 33);
    private static final Color COLOR_FONDO = new Color(250, 250, 250);
    private static final Font FUENTE_TITULO = new Font("Arial", Font.BOLD, 18);
    private static final Font FUENTE_LABEL = new Font("Arial", Font.BOLD, 14);
    private static final Font FUENTE_NORMAL = new Font("Arial", Font.PLAIN, 14);
    private static final Color COLOR_SELECCION = new Color(184, 207, 229);
    private static final Color COLOR_ERROR = new Color(220, 53, 69);

    private JTable table;
    private ContactosTable tableModel;
    private ChatController controlador;
    private List<ContactoIndividual> listaContactos;
    private MainView parentView;

    public ContactosTablePanel(List<ContactoIndividual> contactos) {
        try {
            controlador = ChatController.getUnicaInstancia();
        } catch (ChatControllerException e) {
            e.printStackTrace();
        }
        
        // Crear una copia mutable de la lista
        this.listaContactos = new ArrayList<>(contactos);
        
        // Intentar obtener la vista principal parent
        SwingUtilities.invokeLater(() -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JDialog) {
                JDialog dialog = (JDialog) window;
                if (dialog.getOwner() instanceof MainView) {
                    parentView = (MainView) dialog.getOwner();
                }
            }
        });
        
        setLayout(new BorderLayout(0, 10));
        setBackground(COLOR_FONDO);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setPreferredSize(new Dimension(550, 400));
        
        // Panel de título
        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setBackground(COLOR_FONDO);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JLabel lblTitulo = new JLabel("Lista de Contactos");
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(COLOR_PRIMARIO);
        panelTitulo.add(lblTitulo, BorderLayout.CENTER);
        
        add(panelTitulo, BorderLayout.NORTH);
        
        // Crear el modelo de tabla a partir de la lista de contactos
        tableModel = new ContactosTable(listaContactos);
        table = new JTable(tableModel);
        
        // Configurar la tabla para mejorar la estética
        table.setFont(FUENTE_NORMAL);
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(COLOR_SELECCION);
        table.getTableHeader().setFont(FUENTE_LABEL);
        table.getTableHeader().setBackground(COLOR_SECUNDARIO);
        table.getTableHeader().setForeground(COLOR_PRIMARIO);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_PRIMARIO));
        
        // Configurar el renderizador para centrar texto y manejar texto largo
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Teléfono centrado
        
        // Renderizador para wrapping text en nombre y saludo
        DefaultTableCellRenderer wrapRenderer = new DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                if (value != null) {
                    // Si el texto es demasiado largo, añadir elipsis
                    String text = value.toString();
                    if (text.length() > 40) {
                        text = text.substring(0, 37) + "...";
                    }
                    setText(text);
                } else {
                    setText("");
                }
            }
        };
        
        // Aplicar el renderizador de texto con elipsis a las columnas de nombre y saludo
        table.getColumnModel().getColumn(0).setCellRenderer(wrapRenderer); // Nombre
        table.getColumnModel().getColumn(2).setCellRenderer(wrapRenderer); // Saludo
        
        // Configurar ancho de columnas
        table.getColumnModel().getColumn(0).setPreferredWidth(150); // Nombre
        table.getColumnModel().getColumn(1).setPreferredWidth(100); // Teléfono
        table.getColumnModel().getColumn(2).setPreferredWidth(250); // Saludo
        
        // Crear scroll con bordes mejorados y desactivar scroll horizontal
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARIO, 1),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Desactivar scroll horizontal
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(COLOR_FONDO);
        
        // Botón de eliminar contacto
        JButton btnEliminar = new JButton("Eliminar Contacto");
        btnEliminar.setFont(FUENTE_NORMAL);
        btnEliminar.setBackground(new Color(255, 235, 235));
        btnEliminar.setForeground(COLOR_ERROR);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_ERROR, 1),
                BorderFactory.createEmptyBorder(7, 15, 7, 15)
        ));
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = table.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    ContactoIndividual contacto = tableModel.getContactoAt(filaSeleccionada);
                    if (contacto != null) {
                        int respuesta = JOptionPane.showConfirmDialog(
                                SwingUtilities.getWindowAncestor(ContactosTablePanel.this),
                                "¿Está seguro que desea eliminar al contacto " + contacto.getNombre() + "?",
                                "Confirmar eliminación",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE);
                        
                        if (respuesta == JOptionPane.YES_OPTION) {
                            try {
                                controlador.eliminarContacto(contacto);
                                JOptionPane.showMessageDialog(
                                        SwingUtilities.getWindowAncestor(ContactosTablePanel.this),
                                        "Contacto eliminado exitosamente",
                                        "Contacto eliminado",
                                        JOptionPane.INFORMATION_MESSAGE);
                                
                                // Eliminar de la lista local y actualizar la tabla
                                listaContactos.remove(contacto);
                                tableModel.setContactos(new ArrayList<>(listaContactos));
                                
                                // Actualizar vista padre si es posible
                                actualizarVistaPrincipal();
                            } catch (ChatControllerException ex) {
                                JOptionPane.showMessageDialog(
                                        SwingUtilities.getWindowAncestor(ContactosTablePanel.this),
                                        "Error al eliminar el contacto: " + ex.getMessage(),
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            SwingUtilities.getWindowAncestor(ContactosTablePanel.this),
                            "Por favor, seleccione un contacto para eliminar",
                            "Selección requerida",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        // Botón cerrar
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(FUENTE_NORMAL);
        btnCerrar.setBackground(COLOR_SECUNDARIO);
        btnCerrar.setForeground(COLOR_PRIMARIO);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARIO, 1),
                BorderFactory.createEmptyBorder(7, 15, 7, 15)
        ));
        btnCerrar.addActionListener(e -> {
            // Cerrar el diálogo padre y actualizar la vista principal
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JDialog) {
                actualizarVistaPrincipal();
                ((JDialog) window).dispose();
            }
        });
        
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCerrar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    public JTable getTable() {
        return table;
    }
    
    // Método para actualizar los contactos
    public void actualizarContactos(List<ContactoIndividual> nuevosContactos) {
        this.listaContactos = new ArrayList<>(nuevosContactos);
        tableModel.setContactos(this.listaContactos);
    }
    
    // Método para actualizar la vista principal si es accesible
    private void actualizarVistaPrincipal() {
        // Buscar la MainView si aún no la tenemos
        if (parentView == null) {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JDialog) {
                JDialog dialog = (JDialog) window;
                if (dialog.getOwner() instanceof MainView) {
                    parentView = (MainView) dialog.getOwner();
                }
            }
        }
        
        // Si encontramos la MainView, actualizamos sus contactos
        if (parentView != null) {
            SwingUtilities.invokeLater(() -> {
                parentView.cargarContactos();
            });
        }
    }
}
