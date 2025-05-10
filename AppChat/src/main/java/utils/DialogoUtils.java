package utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * Clase utilitaria para mostrar diálogos emergentes (PopUps) con el estilo visual
 * consistente con el resto de la aplicación.
 */
public class DialogoUtils {
    
    // Constantes para la interfaz (coinciden con las de LoginView/RegisterView)
    private static final Color COLOR_PRIMARIO = new Color(25, 118, 210);
    private static final Color COLOR_SECUNDARIO = new Color(239, 246, 255);
    private static final Color COLOR_TEXTO = new Color(33, 33, 33);
    private static final Color COLOR_FONDO = new Color(250, 250, 250);
    private static final Font FUENTE_TITULO = new Font("Arial", Font.BOLD, 16);
    private static final Font FUENTE_NORMAL = new Font("Arial", Font.PLAIN, 14);
    
    /**
     * Muestra un diálogo de mensaje con el estilo visual de la aplicación.
     * 
     * @param parentComponent El componente padre (para posicionamiento)
     * @param mensaje El mensaje a mostrar
     * @param titulo El título del diálogo
     * @param tipoMensaje El tipo de mensaje: JOptionPane.ERROR_MESSAGE, WARNING_MESSAGE, etc.
     */
    public static void mostrarDialogo(Component parentComponent, String mensaje, String titulo, int tipoMensaje) {
        aplicarEstiloDialogos();
        JOptionPane.showMessageDialog(parentComponent, mensaje, titulo, tipoMensaje);
    }
    
    /**
     * Muestra un diálogo de confirmación con el estilo visual de la aplicación.
     * 
     * @param parentComponent El componente padre (para posicionamiento)
     * @param mensaje El mensaje a mostrar
     * @param titulo El título del diálogo
     * @param tipoMensaje El tipo de mensaje: JOptionPane.YES_NO_OPTION, YES_NO_CANCEL_OPTION, etc.
     * @return La opción seleccionada
     */
    public static int mostrarConfirmacion(Component parentComponent, String mensaje, String titulo, int tipoOpcion) {
        aplicarEstiloDialogos();
        return JOptionPane.showConfirmDialog(parentComponent, mensaje, titulo, tipoOpcion);
    }
    
    /**
     * Muestra un diálogo de entrada con el estilo visual de la aplicación.
     * 
     * @param parentComponent El componente padre (para posicionamiento)
     * @param mensaje El mensaje a mostrar
     * @param titulo El título del diálogo
     * @return El texto ingresado por el usuario o null si canceló
     */
    public static String mostrarDialogoEntrada(Component parentComponent, String mensaje, String titulo) {
        aplicarEstiloDialogos();
        return JOptionPane.showInputDialog(parentComponent, mensaje, titulo, JOptionPane.PLAIN_MESSAGE);
    }
    
    /**
     * Aplica el estilo de la aplicación a los diálogos emergentes.
     */
    private static void aplicarEstiloDialogos() {
        // Personalizar los diálogos
        UIManager.put("OptionPane.background", COLOR_FONDO);
        UIManager.put("Panel.background", COLOR_FONDO);
        UIManager.put("OptionPane.messageForeground", COLOR_TEXTO);
        UIManager.put("OptionPane.messageFont", FUENTE_NORMAL);
        UIManager.put("OptionPane.buttonFont", FUENTE_NORMAL);
        UIManager.put("Button.background", COLOR_SECUNDARIO);
        UIManager.put("Button.foreground", COLOR_PRIMARIO);
        UIManager.put("Button.focus", COLOR_SECUNDARIO);
        UIManager.put("OptionPane.titleFont", FUENTE_TITULO);
    }
    
    /**
     * Configura y muestra un JDialog con el estilo visual de la aplicación.
     * 
     * @param dialog El diálogo a personalizar
     * @param titulo El título del diálogo
     * @param parentComponent El componente padre (para posicionamiento)
     */
    public static void configurarDialogo(JDialog dialog, String titulo, Component parentComponent) {
        dialog.setTitle(titulo);
        dialog.setModal(true);
        dialog.setResizable(false);
        dialog.pack();
        dialog.setLocationRelativeTo(parentComponent);
        dialog.getContentPane().setBackground(COLOR_FONDO);
    }
}