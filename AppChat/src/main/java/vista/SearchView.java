package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import dominio.modelo.Mensaje;
import dominio.modelo.Usuario;
import dominio.controlador.ChatController;
import dominio.controlador.ChatControllerException;
import dominio.modelo.Contacto;
import dominio.modelo.ContactoIndividual;
import dominio.modelo.Grupo;
import tds.BubbleText;


public class SearchView extends JPanel {
    
    // Constantes para la interfaz
    private static final Color COLOR_PRIMARIO = new Color(25, 118, 210);
    private static final Color COLOR_SECUNDARIO = new Color(239, 246, 255);
    private static final Color COLOR_TEXTO = new Color(33, 33, 33);
    private static final Color COLOR_RESULTADOS = new Color(250, 250, 250);
    private static final Font FUENTE_TITULO = new Font("Arial", Font.BOLD, 16);
    private static final Font FUENTE_LABEL = new Font("Arial", Font.BOLD, 12);
    private static final Font FUENTE_NORMAL = new Font("Arial", Font.PLAIN, 14);
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final int ANCHO_PREFERIDO = 600; // Aumentado para dar más espacio
    
    // Componentes de la interfaz
    private JTextField txtFragmento;
    private JTextField txtContacto;
    private JTextField txtTelefono;
    private JButton btnBuscar;
    private JButton btnLimpiar;
    private JPanel panelResultados;
    private JScrollPane scrollResultados;
    private ChatController controlador;
    
    public SearchView() {
        try {
			controlador = ChatController.getUnicaInstancia();
		} catch (ChatControllerException e) {
			e.printStackTrace();
		}
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Establecer un tamaño preferido más ancho para evitar scroll horizontal
        setPreferredSize(new Dimension(ANCHO_PREFERIDO, 500));
        
        // Panel de título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setLayout(new BoxLayout(panelTitulo, BoxLayout.Y_AXIS));
        
        JLabel lblTitulo = new JLabel("Búsqueda de Mensajes");
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(COLOR_PRIMARIO);
        lblTitulo.setAlignmentX(LEFT_ALIGNMENT);
        
        JLabel lblSubtitulo = new JLabel("Encuentra mensajes entre tú y tus contactos");
        lblSubtitulo.setFont(FUENTE_NORMAL);
        lblSubtitulo.setForeground(COLOR_TEXTO);
        lblSubtitulo.setAlignmentX(LEFT_ALIGNMENT);
        
        panelTitulo.add(lblTitulo);
        panelTitulo.add(Box.createRigidArea(new Dimension(0, 5)));
        panelTitulo.add(lblSubtitulo);
        panelTitulo.add(Box.createRigidArea(new Dimension(0, 15)));
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        add(panelTitulo, BorderLayout.NORTH);
        
        // Panel central con formulario y resultados
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(180);
        splitPane.setEnabled(true);
        splitPane.setDividerSize(5);
        
        // Panel de formulario
        JPanel panelFormulario = new JPanel(new BorderLayout(10, 10));
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_PRIMARIO, 1, true),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                )
        ));
        panelFormulario.setBackground(COLOR_SECUNDARIO);
        
        // Panel de campos
        JPanel panelCampos = new JPanel(new GridLayout(3, 2, 10, 10));
        panelCampos.setOpaque(false);
        
        // Estilo para los labels y campos de texto
        JLabel lblFragmento = new JLabel("Fragmento de texto:");
        lblFragmento.setFont(FUENTE_LABEL);
        lblFragmento.setForeground(COLOR_TEXTO);
        
        JLabel lblContacto = new JLabel("Nombre de contacto:");
        lblContacto.setFont(FUENTE_LABEL);
        lblContacto.setForeground(COLOR_TEXTO);
        
        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setFont(FUENTE_LABEL);
        lblTelefono.setForeground(COLOR_TEXTO);
        
        txtFragmento = new JTextField();
        txtFragmento.setFont(FUENTE_NORMAL);
        txtFragmento.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 7, 5, 7)
        ));
        
        txtContacto = new JTextField();
        txtContacto.setFont(FUENTE_NORMAL);
        txtContacto.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 7, 5, 7)
        ));
        
        txtTelefono = new JTextField();
        txtTelefono.setFont(FUENTE_NORMAL);
        txtTelefono.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 7, 5, 7)
        ));
        
        // Añadir evento de Enter para buscar
        KeyAdapter enterKeyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buscarMensajes();
                }
            }
        };
        
        txtFragmento.addKeyListener(enterKeyAdapter);
        txtContacto.addKeyListener(enterKeyAdapter);
        txtTelefono.addKeyListener(enterKeyAdapter);
        
        panelCampos.add(lblFragmento);
        panelCampos.add(txtFragmento);
        panelCampos.add(lblContacto);
        panelCampos.add(txtContacto);
        panelCampos.add(lblTelefono);
        panelCampos.add(txtTelefono);
        
        panelFormulario.add(panelCampos, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setOpaque(false);
        
        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFont(FUENTE_NORMAL);
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        
        btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(FUENTE_NORMAL);
        btnBuscar.setBackground(COLOR_PRIMARIO);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.addActionListener(e -> buscarMensajes());
        
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnBuscar);
        
        panelFormulario.add(panelBotones, BorderLayout.SOUTH);
        
        // Panel de resultados con mejor manejo de espacio
        panelResultados = new JPanel();
        panelResultados.setLayout(new BoxLayout(panelResultados, BoxLayout.Y_AXIS));
        panelResultados.setBackground(COLOR_RESULTADOS);
        
        // Panel con mensaje inicial
        mostrarMensajeInicial();
        
        scrollResultados = new JScrollPane(panelResultados);
        scrollResultados.setBorder(BorderFactory.createEmptyBorder());
        scrollResultados.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollResultados.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Forzar que no haya scroll horizontal
        
        // Añadir paneles al split pane
        splitPane.setTopComponent(panelFormulario);
        splitPane.setBottomComponent(scrollResultados);
        
        add(splitPane, BorderLayout.CENTER);
        
        // Dar más espacio a la parte inferior
        splitPane.setResizeWeight(0.3);
    }
    
    private void mostrarMensajeInicial() {
        panelResultados.removeAll();
        
        JPanel panelMensaje = new JPanel(new BorderLayout());
        panelMensaje.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
        panelMensaje.setOpaque(false);
        
        JLabel lblIcono = new JLabel("🔍", SwingConstants.CENTER);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        
        JLabel lblMensaje = new JLabel("Introduce criterios de búsqueda y pulsa 'Buscar'", SwingConstants.CENTER);
        lblMensaje.setFont(FUENTE_NORMAL);
        lblMensaje.setForeground(Color.GRAY);
        
        panelMensaje.add(lblIcono, BorderLayout.CENTER);
        panelMensaje.add(lblMensaje, BorderLayout.SOUTH);
        
        panelResultados.add(Box.createVerticalGlue());
        panelResultados.add(panelMensaje);
        panelResultados.add(Box.createVerticalGlue());
        
        panelResultados.revalidate();
        panelResultados.repaint();
    }
    
    private void limpiarFormulario() {
        txtFragmento.setText("");
        txtContacto.setText("");
        txtTelefono.setText("");
        mostrarMensajeInicial();
        txtFragmento.requestFocus();
    }
    
    private void buscarMensajes() {
        String fragmento = txtFragmento.getText().trim();
        String contacto = txtContacto.getText().trim();
        String telefono = txtTelefono.getText().trim();
        
        // Validar que al menos un campo esté lleno
        if (fragmento.isEmpty() && contacto.isEmpty() && telefono.isEmpty()) {
            mostrarMensajeSinCriterios();
            return;
        }
        
        List<Mensaje> resultados = controlador.buscarMensajes(
                fragmento.isEmpty() ? null : fragmento,
                contacto.isEmpty() ? null : contacto,
                telefono.isEmpty() ? null : telefono
        );
        
        mostrarResultados(resultados);
    }
    
    private void mostrarMensajeSinCriterios() {
        panelResultados.removeAll();
        
        JPanel panelError = new JPanel(new BorderLayout());
        panelError.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
        panelError.setOpaque(false);
        
        JLabel lblIcono = new JLabel("⚠️", SwingConstants.CENTER);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        
        JLabel lblError = new JLabel("Por favor, introduce al menos un criterio de búsqueda", SwingConstants.CENTER);
        lblError.setFont(FUENTE_NORMAL);
        lblError.setForeground(new Color(217, 83, 79)); // Rojo suave
        
        panelError.add(lblIcono, BorderLayout.CENTER);
        panelError.add(lblError, BorderLayout.SOUTH);
        
        panelResultados.add(Box.createVerticalGlue());
        panelResultados.add(panelError);
        panelResultados.add(Box.createVerticalGlue());
        
        panelResultados.revalidate();
        panelResultados.repaint();
    }
    
    private void mostrarResultados(List<Mensaje> mensajes) {
        panelResultados.removeAll();
        
        if (mensajes.isEmpty()) {
            JPanel panelNoResultados = new JPanel(new BorderLayout());
            panelNoResultados.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
            panelNoResultados.setOpaque(false);
            
            JLabel lblIcono = new JLabel("😕", SwingConstants.CENTER);
            lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
            
            JLabel lblNoResultados = new JLabel("No se encontraron mensajes con los criterios especificados", SwingConstants.CENTER);
            lblNoResultados.setFont(FUENTE_NORMAL);
            lblNoResultados.setForeground(Color.GRAY);
            
            panelNoResultados.add(lblIcono, BorderLayout.CENTER);
            panelNoResultados.add(lblNoResultados, BorderLayout.SOUTH);
            
            panelResultados.add(Box.createVerticalGlue());
            panelResultados.add(panelNoResultados);
            panelResultados.add(Box.createVerticalGlue());
        } else {
            // Panel informativo con el número de resultados
            JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panelInfo.setBackground(new Color(241, 248, 233)); // Verde muy claro
            panelInfo.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            
            JLabel lblResultados = new JLabel("Se encontraron " + mensajes.size() + " mensaje(s)");
            lblResultados.setFont(FUENTE_LABEL);
            lblResultados.setForeground(new Color(46, 125, 50)); // Verde oscuro
            
            panelInfo.add(lblResultados);
            panelInfo.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelInfo.getPreferredSize().height));
            panelInfo.setAlignmentX(LEFT_ALIGNMENT);
            
            panelResultados.add(panelInfo);
            panelResultados.add(Box.createRigidArea(new Dimension(0, 10)));
            
            // Contenedor principal de los mensajes con margen
            JPanel contenedorMensajes = new JPanel();
            contenedorMensajes.setLayout(new BoxLayout(contenedorMensajes, BoxLayout.Y_AXIS));
            contenedorMensajes.setBackground(COLOR_RESULTADOS);
            contenedorMensajes.setBorder(new EmptyBorder(0, 10, 10, 10));
            
            // Asegurar que el contenedor de mensajes no cause scroll horizontal
            contenedorMensajes.setMaximumSize(new Dimension(ANCHO_PREFERIDO - 70, Integer.MAX_VALUE));
            contenedorMensajes.setAlignmentX(LEFT_ALIGNMENT);
            
            // Obtener el usuario actual para determinar la dirección de los mensajes
            Usuario usuarioActual = controlador.getUsuarioActual();
            
            // Añadir cada mensaje como una burbuja
            for (Mensaje m : mensajes) {
                // Determinar si es un mensaje enviado o recibido
                int tipo = m.getTipo() ? BubbleText.SENT : BubbleText.RECEIVED;
                Color color = tipo == BubbleText.SENT ? 
                        new Color(220, 248, 198) : // Verde claro para enviados
                        new Color(240, 240, 240);  // Gris claro para recibidos
                
                // Crear panel contenedor del mensaje con margen adecuado
                JPanel panelMensaje = new JPanel(new BorderLayout());
                panelMensaje.setBackground(COLOR_RESULTADOS);
                panelMensaje.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
                
                // Panel de información de contacto y fecha
                JPanel panelInfo2 = new JPanel(new BorderLayout());
                panelInfo2.setOpaque(false);
                panelInfo2.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
                
                // Panel para información de emisor y receptor
                JPanel panelContactos = new JPanel(new BorderLayout());
                panelContactos.setOpaque(false);
                
                // Determinar el emisor y receptor basado en la información del mensaje
                String nombreEmisor = m.getEmisor().getNombre();
                
                // Crear etiqueta para el emisor
                String etiquetaEmisor;
                if (m.getEmisor().equals(usuarioActual)) {
                    etiquetaEmisor = "De: " + nombreEmisor + " (tú)";
                } else {
                    etiquetaEmisor = "De: " + nombreEmisor;
                }
                
                // Crear etiqueta para el receptor
                String etiquetaReceptor;
                if (m.getReceptor() != null) {
                    // Si el mensaje tiene receptor definido, usarlo
                    String nombreReceptor = m.getReceptor().getNombre();
                    
                    if (m.getReceptor() instanceof ContactoIndividual &&
                        ((ContactoIndividual)m.getReceptor()).getUsuario().equals(usuarioActual)) {
                        etiquetaReceptor = "Para: " + nombreReceptor + " (tú)";
                    } else {
                        etiquetaReceptor = "Para: " + nombreReceptor;
                    }
                } else {
                    // Si no tiene receptor definido (compatibilidad), usar la lógica antigua
                    if (m.getTipo()) {
                        etiquetaReceptor = "Para: " + determinarReceptor(m, usuarioActual);
                    } else {
                        etiquetaReceptor = "Para: " + usuarioActual.getNombre() + " (tú)";
                    }
                }
                
                JLabel lblEmisor = new JLabel(etiquetaEmisor);
                lblEmisor.setFont(new Font("Arial", Font.BOLD, 11));
                lblEmisor.setForeground(new Color(97, 97, 97));
                
                JLabel lblReceptor = new JLabel(etiquetaReceptor);
                lblReceptor.setFont(new Font("Arial", Font.PLAIN, 11));
                lblReceptor.setForeground(new Color(97, 97, 97));
                
                panelContactos.add(lblEmisor, BorderLayout.NORTH);
                panelContactos.add(lblReceptor, BorderLayout.SOUTH);
                
                // Formatear fecha correctamente (LocalDateTime a String)
                String fechaFormateada = "Sin fecha";
                if (m.getFechaEnvio() != null) {
                    fechaFormateada = m.getFechaEnvio().format(FORMATO_FECHA);
                }
                
                JLabel lblFecha = new JLabel(fechaFormateada);
                lblFecha.setFont(new Font("Arial", Font.ITALIC, 11));
                lblFecha.setForeground(new Color(158, 158, 158));
                
                panelInfo2.add(panelContactos, BorderLayout.WEST);
                panelInfo2.add(lblFecha, BorderLayout.EAST);
                
                // Contenedor para el contenido del mensaje
                JPanel contenidoPanel = new JPanel(new BorderLayout());
                contenidoPanel.setOpaque(false);
                
                // Determinar si es un emoji o texto
                if (m.getContenido() != null && m.getContenido().startsWith("EMOJI:")) {
                    try {
                        int emojiId = Integer.parseInt(m.getContenido().substring("EMOJI:".length()));
                        BubbleText burbuja = new BubbleText(panelResultados, emojiId, color, 
                                m.getEmisor().getNombre(), tipo, 16);
                        contenidoPanel.add(burbuja, BorderLayout.CENTER);
                    } catch (NumberFormatException e) {
                        // Si hay error al parsear el emoji, mostrar como texto normal
                        JTextArea txtMensaje = crearAreaTextoMensaje(m.getContenido());
                        contenidoPanel.add(txtMensaje, BorderLayout.CENTER);
                    }
                } else {
                    // Mensaje normal de texto
                    JTextArea txtMensaje = crearAreaTextoMensaje(m.getContenido());
                    contenidoPanel.add(txtMensaje, BorderLayout.CENTER);
                }
                
                // Añadir los componentes al panel del mensaje
                panelMensaje.add(panelInfo2, BorderLayout.NORTH);
                panelMensaje.add(contenidoPanel, BorderLayout.CENTER);
                
                // Configurar tamaño máximo para evitar scroll horizontal
                panelMensaje.setMaximumSize(new Dimension(ANCHO_PREFERIDO - 80, panelMensaje.getPreferredSize().height));
                panelMensaje.setAlignmentX(LEFT_ALIGNMENT);
                contenedorMensajes.add(panelMensaje);
            }
            
            // Añadir el contenedor de mensajes al panel de resultados
            panelResultados.add(contenedorMensajes);
        }
        
        panelResultados.revalidate();
        panelResultados.repaint();
        
        // Desplazar al inicio
        SwingUtilities.invokeLater(() -> {
            scrollResultados.getVerticalScrollBar().setValue(0);
        });
    }
    
    /**
     * Método auxiliar para determinar el receptor de un mensaje
     */
    private String determinarReceptor(Mensaje mensaje, Usuario usuarioActual) {
        // Si el tipo de mensaje es true, el mensaje ha sido enviado por el usuario actual
        // Si el tipo de mensaje es false, el mensaje ha sido recibido por el usuario actual
        
        if (mensaje.getTipo()) {
            // Si el mensaje es enviado por el usuario actual
            // Tenemos que buscar el destinatario del mensaje en los contactos
            
            // Recorrer todos los contactos del usuario actual (tanto individuales como grupos)
            for (Contacto contacto : usuarioActual.getContactos()) {
                // Comprobar si este contacto tiene el mensaje en su lista de mensajes
                if (contacto.getMensajes().contains(mensaje)) {
                    // Si encontramos el mensaje en este contacto, este es el destinatario
                    if (contacto instanceof Grupo) {
                        // Es un grupo - mostrar el nombre del grupo
                        return contacto.getNombre();
                    } else if (contacto instanceof ContactoIndividual) {
                        // Es un contacto individual - mostrar su nombre, o el nombre de usuario si no tiene nombre
                        ContactoIndividual contactoInd = (ContactoIndividual) contacto;
                        return contactoInd.getNombre().isEmpty() ? 
                               contactoInd.getUsuario().getNombre() : 
                               contactoInd.getNombre();
                    }
                }
            }
            
            // Si no se encuentra el contacto, puede ser que el mensaje no esté 
            // correctamente asociado a un contacto en este usuario
            return "desconocido";
        } else {
            // Si el mensaje es recibido por el usuario actual
            // El receptor es el usuario actual
            return usuarioActual.getNombre();
        }
    }
    
    private JTextArea crearAreaTextoMensaje(String contenido) {
        JTextArea txtMensaje = new JTextArea(contenido != null ? contenido : "");
        txtMensaje.setEditable(false);
        txtMensaje.setLineWrap(true);      // Habilitar salto de línea
        txtMensaje.setWrapStyleWord(true); // Salto de línea por palabras
        txtMensaje.setOpaque(true);
        txtMensaje.setFont(FUENTE_NORMAL);
        txtMensaje.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        // Fondo ligeramente coloreado para distinguir el mensaje
        txtMensaje.setBackground(new Color(245, 245, 245));
        
        // Asegurar que el tamaño se ajuste al contenido
        txtMensaje.setRows(calculateRows(contenido));
        
        // Establecer ancho máximo para forzar saltos de línea adecuados
        txtMensaje.setSize(ANCHO_PREFERIDO - 100, Short.MAX_VALUE); // Esto fuerza el cálculo del wrap
        
        return txtMensaje;
    }
    
    // Método para calcular número de filas necesarias según el contenido
    private int calculateRows(String text) {
        if (text == null || text.isEmpty()) {
            return 1;
        }
        
        // Ajustamos el número de caracteres por línea para que sea más preciso
        // considerando el ancho del componente (menos caracteres por línea)
        int charsPerLine = 40; // Reducido de 50 a 40 para asegurar suficiente espacio
        int lines = text.length() / charsPerLine + 1;
        
        // Contar saltos de línea explícitos
        int newlines = text.split("\n").length - 1;
        return Math.max(1, Math.min(10, lines + newlines)); // Limitar a 10 filas max
    }
}

