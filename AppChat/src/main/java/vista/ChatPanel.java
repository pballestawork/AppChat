package vista;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dominio.controlador.ChatController;
import dominio.controlador.ChatControllerException;
import dominio.modelo.Contacto;
import dominio.modelo.Mensaje;
import dominio.modelo.Usuario;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Objects;

import tds.BubbleText;


public class ChatPanel extends JPanel {
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
    
    private static final Color COLOR_SENT = new Color(220, 248, 198);
    private static final Color COLOR_RECEIVED = new Color(240, 240, 240);

    private final JPanel chatContainer;
    private final JScrollPane scrollChat;
    private final JTextArea areaTexto;
    private final JButton btnEnviar;
    private final JButton btnEmoji;
    
    private Contacto contactoActual;
    private Usuario usuarioActual;
    private ChatController controlador;

    public ChatPanel() {
        try {
			controlador = ChatController.getUnicaInstancia();
		} catch (ChatControllerException e) {
			e.printStackTrace();
		}
        setLayout(new BorderLayout());

        chatContainer = new JPanel();
        chatContainer.setLayout(new BoxLayout(chatContainer, BoxLayout.Y_AXIS));
        chatContainer.setBackground(Color.WHITE);

      
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.add(chatContainer, BorderLayout.CENTER);
        wrapperPanel.add(Box.createHorizontalStrut(18), BorderLayout.EAST);

        scrollChat = new JScrollPane(wrapperPanel);
        scrollChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollChat.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollChat.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(scrollChat, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout(5, 5));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        areaTexto = new JTextArea(INITIAL_TEXT_AREA_ROWS, TEXT_AREA_COLUMNS);
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        areaTexto.setFont(new Font("Arial", Font.PLAIN, 14));
        areaTexto.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        areaTexto.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { ajustarTamañoAreaTexto(); }
            @Override
            public void removeUpdate(DocumentEvent e) { ajustarTamañoAreaTexto(); }
            @Override
            public void changedUpdate(DocumentEvent e) { ajustarTamañoAreaTexto(); }
        });
        
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

        btnEnviar = new JButton("Enviar");
        btnEnviar.setBackground(new Color(0, 132, 255));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setFocusPainted(false);
        btnEnviar.addActionListener(e -> enviarMensaje());

        btnEmoji = new JButton();
        btnEmoji.setFocusPainted(false);
        cargarIconoEmoji();
        configurarBotonEmoji();

        JScrollPane scrollTexto = new JScrollPane(areaTexto);
        scrollTexto.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollTexto.setBorder(BorderFactory.createEmptyBorder());
        
        panelInferior.add(btnEmoji, BorderLayout.WEST);
        panelInferior.add(scrollTexto, BorderLayout.CENTER);
        panelInferior.add(btnEnviar, BorderLayout.EAST);
        
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void cargarIconoEmoji() {
        try {
            Image imagenOriginal = ImageIO.read(Objects.requireNonNull(getClass().getResource(EMOJI_ICON_PATH)));
            Image imagenEscalada = imagenOriginal.getScaledInstance(EMOJI_BUTTON_SIZE, EMOJI_BUTTON_SIZE, Image.SCALE_SMOOTH);
            btnEmoji.setIcon(new ImageIcon(imagenEscalada));
        } catch (IOException | NullPointerException e) {
            System.err.println("Error al cargar el icono de emoji: " + e.getMessage());
            btnEmoji.setText("😊");
        }
    }

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
    
    private JButton crearBotonEmoji(ImageIcon icon, int emojiId, JPopupMenu popup) {
        JButton btn = new JButton(icon);
        btn.setPreferredSize(new Dimension(EMOJI_BUTTON_DIMENSION, EMOJI_BUTTON_DIMENSION));
        btn.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        
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
    
    private void agregarBurbujaEmoji(int emojiId, String nombre, int tipo) {
        Color color = (tipo == BubbleText.SENT) ? COLOR_SENT : COLOR_RECEIVED;
        BubbleText burbuja = new BubbleText(chatContainer, emojiId, color, nombre, tipo, EMOJI_SIZE);
        chatContainer.add(burbuja);
        actualizarVistaMensajes();
    }

    private void ajustarTamañoAreaTexto() {
        int lineas = Math.min(5, areaTexto.getLineCount());
        int altura = LINE_HEIGHT * lineas;
        areaTexto.setPreferredSize(new Dimension(300, altura));
        areaTexto.revalidate();
    }

    public void agregarMensaje(String texto, String usuario, int tipoMensaje) {
        Color color = (tipoMensaje == BubbleText.SENT) ? COLOR_SENT : COLOR_RECEIVED;
        BubbleText burbuja = new BubbleText(chatContainer, texto, color, usuario, tipoMensaje);
        chatContainer.add(burbuja);
        actualizarVistaMensajes();
    }

    private void actualizarVistaMensajes() {
        chatContainer.revalidate();
        chatContainer.repaint();
        desplazarAlFinal();
    }
    
    private void desplazarAlFinal() {
        SwingUtilities.invokeLater(() -> 
            scrollChat.getVerticalScrollBar().setValue(
                scrollChat.getVerticalScrollBar().getMaximum())
        );
    }

    public void cargarMensajesDe(Contacto contacto) {
        this.contactoActual = contacto;
        chatContainer.removeAll();
        
        if (contacto == null) return;
        
        for (Mensaje m : contacto.getMensajes()) {
            int tipo = m.isEnviado(usuarioActual) ? BubbleText.SENT : BubbleText.RECEIVED;
            String contenido = m.getContenido();
            
            if(m.isEnviado(usuarioActual)) {
            	if (contenido.startsWith(EMOJI_PREFIX)) {
            		try {
            			int emojiId = Integer.parseInt(contenido.substring(EMOJI_PREFIX.length()));
            			agregarBurbujaEmoji(emojiId, m.getEmisor().getNombre(), tipo);
            		} catch (NumberFormatException e) {
            			agregarBurbujaMensaje(contenido, m.getEmisor().getNombre(), tipo);
            		}
            	} else {
            		agregarBurbujaMensaje(contenido, m.getEmisor().getNombre(), tipo);
            	}
			} else {
				if (contenido.startsWith(EMOJI_PREFIX)) {
					try {
						int emojiId = Integer.parseInt(contenido.substring(EMOJI_PREFIX.length()));
						agregarBurbujaEmoji(emojiId, m.getReceptor().getNombre(), tipo);
					} catch (NumberFormatException e) {
						agregarBurbujaMensaje(contenido, m.getReceptor().getNombre(), tipo);
					}
				} else {
					agregarBurbujaMensaje(contenido, m.getReceptor().getNombre(), tipo);
				}
			}
            
        }
        actualizarVistaMensajes();
    }
    
    private void agregarBurbujaMensaje(String contenido, String emisor, int tipo) {
        Color color = (tipo == BubbleText.SENT) ? COLOR_SENT : COLOR_RECEIVED;
        BubbleText burbuja = new BubbleText(chatContainer, contenido, color, emisor, tipo);
        chatContainer.add(burbuja);
    }
    
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
    
    private void mostrarErrorEnvio(Exception ex, String tipo) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(
            this, 
            "Error al enviar " + tipo + ": " + ex.getMessage(),
            "Error de envío",
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
    }
}
