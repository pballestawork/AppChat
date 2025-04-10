package vista;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dominio.modelo.ContactoIndividual;
import dominio.modelo.Mensaje;
import dominio.modelo.Usuario;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Objects;

import tds.BubbleText;
import utils.ChatControllerStub;

public class ChatPanel extends JPanel {
    // Constantes
    private static final int INITIAL_TEXT_AREA_ROWS = 1;
    private static final int TEXT_AREA_COLUMNS = 30;
    private static final int EMOJI_BUTTON_SIZE = 20;
    private static final int EMOJI_GRID_SIZE = 5;
    private static final int EMOJI_BUTTON_DIMENSION = 40;
    private static final int EMOJI_COUNT = 25;
    private static final int LINE_HEIGHT = 20;
    private static final int EMOJI_SIZE = 16;
    private static final String EMOJI_PREFIX = "EMOJI:";
    private static final String EMOJI_ICON_PATH = "/icons/emoji.png";
    
    // Colores
    private static final Color COLOR_SENT = new Color(220, 248, 198); // Verde claro más agradable
    private static final Color COLOR_RECEIVED = new Color(240, 240, 240); // Gris claro

    // Componentes UI
    private final JPanel chatContainer;
    private final JScrollPane scrollChat;
    private final JTextArea areaTexto;
    private final JButton btnEnviar;
    private final JButton btnEmoji;
    
    // Datos
    private ContactoIndividual contactoActual;
    private Usuario usuarioActual;
    private final ChatControllerStub controlador;

    public ChatPanel() {
        controlador = ChatControllerStub.getUnicaInstancia();
        setLayout(new BorderLayout());

        // Panel de mensajes
        chatContainer = new JPanel();
        chatContainer.setLayout(new BoxLayout(chatContainer, BoxLayout.Y_AXIS));
        chatContainer.setBackground(Color.WHITE);

        // Scroll para chat con bordes más suaves
        scrollChat = new JScrollPane(chatContainer);
        scrollChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollChat.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollChat.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(scrollChat, BorderLayout.CENTER);

        // Panel inferior mejorado
        JPanel panelInferior = new JPanel(new BorderLayout(5, 5));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Área de texto con mejor apariencia
        areaTexto = new JTextArea(INITIAL_TEXT_AREA_ROWS, TEXT_AREA_COLUMNS);
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        areaTexto.setFont(new Font("Arial", Font.PLAIN, 14));
        areaTexto.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // Hacer que el área de texto crezca dinámicamente
        areaTexto.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { ajustarTamañoAreaTexto(); }
            @Override
            public void removeUpdate(DocumentEvent e) { ajustarTamañoAreaTexto(); }
            @Override
            public void changedUpdate(DocumentEvent e) { ajustarTamañoAreaTexto(); }
        });
        
        // Agregar soporte para Enter para enviar mensaje (Shift+Enter para nueva línea)
        areaTexto.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enviar");
        areaTexto.getActionMap().put("enviar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensaje();
            }
        });
        areaTexto.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.SHIFT_DOWN_MASK), "newline");
        areaTexto.getActionMap().put("newline", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                areaTexto.append("\n");
            }
        });

        // Botón de enviar mejorado
        btnEnviar = new JButton("Enviar");
        btnEnviar.setBackground(new Color(0, 132, 255));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setFocusPainted(false);
        btnEnviar.addActionListener(e -> enviarMensaje());

        // Botón de emoji mejorado
        btnEmoji = new JButton();
        btnEmoji.setFocusPainted(false);
        cargarIconoEmoji();
        configurarBotonEmoji();

        // Agregar componentes al panel inferior
        JScrollPane scrollTexto = new JScrollPane(areaTexto);
        scrollTexto.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollTexto.setBorder(BorderFactory.createEmptyBorder());
        
        panelInferior.add(btnEmoji, BorderLayout.WEST);
        panelInferior.add(scrollTexto, BorderLayout.CENTER);
        panelInferior.add(btnEnviar, BorderLayout.EAST);
        
        add(panelInferior, BorderLayout.SOUTH);
    }

    // Método para cargar el icono de emoji con manejo de errores mejorado
    private void cargarIconoEmoji() {
        try {
            Image imagenOriginal = ImageIO.read(Objects.requireNonNull(getClass().getResource(EMOJI_ICON_PATH)));
            Image imagenEscalada = imagenOriginal.getScaledInstance(EMOJI_BUTTON_SIZE, EMOJI_BUTTON_SIZE, Image.SCALE_SMOOTH);
            btnEmoji.setIcon(new ImageIcon(imagenEscalada));
        } catch (IOException | NullPointerException e) {
            System.err.println("Error al cargar el icono de emoji: " + e.getMessage());
            btnEmoji.setText("😊"); // Emoji de respaldo en caso de error
        }
    }

    // Configuración del botón de emoji y su popup
    private void configurarBotonEmoji() {
        btnEmoji.addActionListener(e -> {
            JPopupMenu popup = new JPopupMenu();
            JPanel panelEmojis = new JPanel(new GridLayout(EMOJI_GRID_SIZE, EMOJI_GRID_SIZE, 5, 5));
            panelEmojis.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            
            for (int i = 0; i < EMOJI_COUNT; i++) {
                final int emojiId = i;
                ImageIcon icon = BubbleText.getEmoji(emojiId);
                JButton btn = crearBotonEmoji(icon, emojiId, popup);
                panelEmojis.add(btn);
            }
            
            popup.add(panelEmojis);
            popup.show(btnEmoji, 0, -popup.getPreferredSize().height);
        });
    }
    
    // Método para crear botones de emoji con efecto hover
    private JButton crearBotonEmoji(ImageIcon icon, int emojiId, JPopupMenu popup) {
        JButton btn = new JButton(icon);
        btn.setPreferredSize(new Dimension(EMOJI_BUTTON_DIMENSION, EMOJI_BUTTON_DIMENSION));
        btn.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        
        // Efectos de hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBorder(BorderFactory.createLineBorder(new Color(0, 132, 255), 2));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
            }
        });
        
        btn.addActionListener(e -> {
            enviarEmoji(emojiId);
            popup.setVisible(false);
        });
        
        return btn;
    }
    
    // Método separado para enviar emoji
    private void enviarEmoji(int emojiId) {
        if (contactoActual == null || usuarioActual == null) return;
        
        String emojiMensaje = EMOJI_PREFIX + emojiId;
        try {
            controlador.enviarMensaje(contactoActual, emojiMensaje);
            agregarBurbujaEmoji(emojiId, usuarioActual.getNombre(), BubbleText.SENT);
        } catch (Exception ex) {
            mostrarErrorEnvio(ex, "emoji");
        }
    }
    
    // Método para agregar burbuja de emoji
    private void agregarBurbujaEmoji(int emojiId, String nombre, int tipo) {
        Color color = (tipo == BubbleText.SENT) ? COLOR_SENT : COLOR_RECEIVED;
        BubbleText burbuja = new BubbleText(chatContainer, emojiId, color, nombre, tipo, EMOJI_SIZE);
        chatContainer.add(burbuja);
        actualizarVistaMensajes();
    }

    // Método para ajustar dinámicamente el tamaño del área de texto
    private void ajustarTamañoAreaTexto() {
        int lineas = Math.min(5, areaTexto.getLineCount()); // Máximo 5 líneas visibles
        int altura = LINE_HEIGHT * lineas;
        areaTexto.setPreferredSize(new Dimension(300, altura));
        areaTexto.revalidate();
    }

    // Método para agregar mensajes de texto al chat
    public void agregarMensaje(String texto, String usuario, int tipoMensaje) {
        Color color = (tipoMensaje == BubbleText.SENT) ? COLOR_SENT : COLOR_RECEIVED;
        BubbleText burbuja = new BubbleText(chatContainer, texto, color, usuario, tipoMensaje);
        chatContainer.add(burbuja);
        actualizarVistaMensajes();
    }

    // Método común para actualizar la vista después de agregar mensajes
    private void actualizarVistaMensajes() {
        chatContainer.revalidate();
        chatContainer.repaint();
        desplazarAlFinal();
    }
    
    // Método para desplazar el scroll al final
    private void desplazarAlFinal() {
        SwingUtilities.invokeLater(() -> 
            scrollChat.getVerticalScrollBar().setValue(
                scrollChat.getVerticalScrollBar().getMaximum())
        );
    }

    // Método para cargar historial de mensajes
    public void cargarMensajesDe(ContactoIndividual contacto) {
        this.contactoActual = contacto;
        chatContainer.removeAll();
        
        if (contacto == null) return;
        
        for (Mensaje m : contacto.getMensajes()) {
            int tipo = m.getTipo() ? BubbleText.SENT : BubbleText.RECEIVED;
            String contenido = m.getContenido();
            
            if (contenido.startsWith(EMOJI_PREFIX)) {
                try {
                    int emojiId = Integer.parseInt(contenido.substring(EMOJI_PREFIX.length()));
                    agregarBurbujaEmoji(emojiId, m.getEmisor().getNombre(), tipo);
                } catch (NumberFormatException e) {
                    // Si hay un error al parsear el ID, mostrarlo como texto normal
                    agregarBurbujaMensaje(contenido, m.getEmisor().getNombre(), tipo);
                }
            } else {
                agregarBurbujaMensaje(contenido, m.getEmisor().getNombre(), tipo);
            }
        }
        actualizarVistaMensajes();
    }
    
    // Método para agregar una burbuja de mensaje de texto
    private void agregarBurbujaMensaje(String contenido, String emisor, int tipo) {
        Color color = (tipo == BubbleText.SENT) ? COLOR_SENT : COLOR_RECEIVED;
        BubbleText burbuja = new BubbleText(chatContainer, contenido, color, emisor, tipo);
        chatContainer.add(burbuja);
    }
    
    // Método para enviar mensaje de texto
    private void enviarMensaje() {
        String mensaje = areaTexto.getText().trim();
        if (mensaje.isEmpty() || usuarioActual == null || contactoActual == null) {
            return;
        }
        
        try {
            controlador.enviarMensaje(contactoActual, mensaje);
            agregarMensaje(mensaje, usuarioActual.getNombre(), BubbleText.SENT);
            areaTexto.setText("");
        } catch (Exception ex) {
            mostrarErrorEnvio(ex, "mensaje");
        }
    }
    
    // Método para mostrar errores
    private void mostrarErrorEnvio(Exception ex, String tipo) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(
            this, 
            "Error al enviar " + tipo + ": " + ex.getMessage(),
            "Error de envío",
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    // Establecer usuario actual
    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
    }
}
