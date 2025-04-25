package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;
import dominio.modelo.ContactoIndividual;

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

    private JTable table;
    private ContactosTable tableModel;

    public ContactosTablePanel(List<ContactoIndividual> contactos) {
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
        tableModel = new ContactosTable(contactos);
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
        
        // Panel de botones (opcional)
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(COLOR_FONDO);
        
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
            // Cerrar el diálogo padre
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JDialog) {
                ((JDialog) window).dispose();
            }
        });
        
        panelBotones.add(btnCerrar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    public JTable getTable() {
        return table;
    }
    
    // Método para actualizar los contactos
    public void actualizarContactos(List<ContactoIndividual> nuevosContactos) {
        tableModel.setContactos(nuevosContactos);
    }
}
