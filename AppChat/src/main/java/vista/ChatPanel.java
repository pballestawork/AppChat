package vista;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.h2.engine.User;

import dominio.modelo.ContactoIndividual;
import dominio.modelo.Mensaje;
import dominio.modelo.Usuario;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import tds.BubbleText;
import utils.ChatControllerStub;

public class ChatPanel extends JPanel {
    private JPanel chatContainer;
    private JScrollPane scrollChat;
    private JTextArea areaTexto;
    private JButton btnEnviar;
    private JButton btnEmoji;
	private ContactoIndividual contactoActual;
	private Usuario usuarioActual;
	private ChatControllerStub controlador;

	public ChatPanel() {
    	controlador = ChatControllerStub.getUnicaInstancia();
        setLayout(new BorderLayout());

        // Panel que contiene los mensajes del chat
        chatContainer = new JPanel();
        chatContainer.setLayout(new BoxLayout(chatContainer, BoxLayout.Y_AXIS));

        // Scroll para el chat
        scrollChat = new JScrollPane(chatContainer);
        scrollChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollChat.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollChat, BorderLayout.CENTER);

        // Panel inferior con área de texto y botón de enviar
        JPanel panelInferior = new JPanel(new BorderLayout());
        areaTexto = new JTextArea(1, 30);
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        areaTexto.setFont(new Font("Arial", Font.PLAIN, 14));

        // Hacer que el JTextArea crezca dinámicamente
        areaTexto.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { ajustarTamañoAreaTexto(); }
            public void removeUpdate(DocumentEvent e) { ajustarTamañoAreaTexto(); }
            public void changedUpdate(DocumentEvent e) { ajustarTamañoAreaTexto(); }
        });

        btnEnviar = new JButton("Enviar");
        btnEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensaje();
            }
        });

        panelInferior.add(new JScrollPane(areaTexto), BorderLayout.CENTER);
        panelInferior.add(btnEnviar, BorderLayout.EAST);
        add(panelInferior, BorderLayout.SOUTH);
        
        btnEmoji = new JButton("");

        Image imagenOriginal;
		try {
			imagenOriginal = ImageIO.read(getClass().getResource("/icons/emoji.png"));
			int anchoDeseado = 20;
		    int altoDeseado = 20;
		    Image imagenEscalada = imagenOriginal.getScaledInstance(anchoDeseado, altoDeseado, Image.SCALE_SMOOTH);
		    btnEmoji.setIcon(new ImageIcon(imagenEscalada));

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		btnEmoji.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        // Crear un JPopupMenu para contener los emojis
		        JPopupMenu popup = new JPopupMenu();
		        JPanel panelEmojis = new JPanel(new GridLayout(5, 5, 5, 5));
		        
		        // Agregar 25 botones (IDs 0 a 24)
		        for (int i = 0; i < 25; i++) {
		            final int emojiId = i;
		            ImageIcon icon = BubbleText.getEmoji(emojiId); // Obtiene la miniatura del emoji
		            JButton btn = new JButton(icon);
		            btn.setPreferredSize(new Dimension(40, 40));
		            btn.setBorder(BorderFactory.createEmptyBorder());
		            btn.setContentAreaFilled(false);
		            btn.addActionListener(new ActionListener() {
		                @Override
		                public void actionPerformed(ActionEvent e) {
		                    // Representamos el mensaje de emoji como "EMOJI:" + emojiId
		                    String emojiMensaje = "EMOJI:" + emojiId;
		                    try {
		                        // Persistir el mensaje en el historial del contacto
		                        controlador.enviarMensaje(contactoActual, emojiMensaje);
		                    } catch (Exception ex) {
		                        ex.printStackTrace();
		                        JOptionPane.showMessageDialog(ChatPanel.this, "Error al enviar emoji: " + ex.getMessage());
		                    }
		                    // Añadir la burbuja a la UI usando el constructor de emoji
		                    BubbleText burbuja = new BubbleText(chatContainer, emojiId, Color.GREEN, usuarioActual.getNombre(), BubbleText.SENT, 16);
		                    chatContainer.add(burbuja);
		                    chatContainer.revalidate();
		                    chatContainer.repaint();
		                    SwingUtilities.invokeLater(() ->
		                        scrollChat.getVerticalScrollBar().setValue(scrollChat.getVerticalScrollBar().getMaximum())
		                    );
		                    popup.setVisible(false);
		                }
		            });
		            panelEmojis.add(btn);
		        }
		        popup.add(panelEmojis);
		        // Mostrar el popup justo encima del botón de emoji
		        popup.show(btnEmoji, 0, -popup.getPreferredSize().height);
		    }
		});

        panelInferior.add(btnEmoji, BorderLayout.WEST);
    }

    // Método para ajustar dinámicamente el tamaño del JTextArea
    private void ajustarTamañoAreaTexto() {
        int lineas = areaTexto.getLineCount();
        int altura = 20 * lineas;
        areaTexto.setPreferredSize(new Dimension(300, altura));
        areaTexto.revalidate();
    }

    // Método para agregar mensajes al chat
    public void agregarMensaje(String texto, String usuario, int tipoMensaje) {
        BubbleText burbuja = new BubbleText(chatContainer, texto, 
                tipoMensaje == BubbleText.SENT ? Color.GREEN : Color.LIGHT_GRAY, 
                usuario, tipoMensaje);
        chatContainer.add(burbuja);
        chatContainer.revalidate();
        chatContainer.repaint();

        // Desplazar el scroll automáticamente hacia abajo
        SwingUtilities.invokeLater(() -> scrollChat.getVerticalScrollBar().setValue(scrollChat.getVerticalScrollBar().getMaximum()));
    }

    
    public void cargarMensajesDe(ContactoIndividual contacto) {
    	this.contactoActual = contacto;
        chatContainer.removeAll();  // Limpia los mensajes actuales
        // Se supone que contacto.getMensajes() devuelve List<Mensaje>
        for (Mensaje m : contacto.getMensajes()) {
            int tipo = m.getTipo() ? BubbleText.SENT : BubbleText.RECEIVED;
            if (m.getContenido().startsWith("EMOJI:")) {
                // Es un mensaje de emoji, extraer el id
                int emojiId = Integer.parseInt(m.getContenido().substring("EMOJI:".length()));
                BubbleText burbuja = new BubbleText(chatContainer, emojiId, 
                        tipo == BubbleText.SENT ? Color.GREEN : Color.LIGHT_GRAY, 
                        m.getEmisor().getNombre(), tipo, 16);
                chatContainer.add(burbuja);
            } else {
                // Mensaje normal de texto
                BubbleText burbuja = new BubbleText(chatContainer, m.getContenido(), 
                        tipo == BubbleText.SENT ? Color.GREEN : Color.LIGHT_GRAY, 
                        m.getEmisor().getNombre(), tipo);
                chatContainer.add(burbuja);
            }
        }
        chatContainer.revalidate();
        chatContainer.repaint();
        SwingUtilities.invokeLater(() -> 
            scrollChat.getVerticalScrollBar().setValue(scrollChat.getVerticalScrollBar().getMaximum())
        );
    }
    
 // Método para enviar mensaje: se persiste el mensaje y se añade a la UI
    private void enviarMensaje() {
        String mensaje = areaTexto.getText().trim();
        if (!mensaje.isEmpty() && usuarioActual != null && contactoActual != null) {
            try {
                // Llamar al controlador para persistir el mensaje en el historial del contacto
                controlador.enviarMensaje(contactoActual, mensaje);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al enviar mensaje: " + ex.getMessage());
            }
            // Agregar la burbuja a la UI usando el nombre del usuario actual
            agregarMensaje(mensaje, usuarioActual.getNombre(), BubbleText.SENT);
            areaTexto.setText("");
        }else {
            System.out.println("No se puede enviar mensaje");
            }
    }
    
  
    
    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
    }
}
