package vista;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import tds.BubbleText;

public class ChatPanel extends JPanel {
    private JPanel chatContainer;
    private JScrollPane scrollChat;
    private JTextArea areaTexto;
    private JButton btnEnviar;

    public ChatPanel() {
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

    // Método para manejar el envío de mensajes
    private void enviarMensaje() {
        String mensaje = areaTexto.getText().trim();
        if (!mensaje.isEmpty()) {
            agregarMensaje(mensaje, "Tú", BubbleText.SENT);
            areaTexto.setText(""); // Limpiar el área de texto después de enviar
        }
    }
}
