package utils;

import java.awt.Color;
import java.awt.Font;

/**
 * Clase que centraliza todas las constantes de estilo para
 * mantener una apariencia coherente en toda la aplicación.
 */
public class EstiloApp {
    
    // Colores principales
    public static final Color COLOR_PRIMARIO = new Color(25, 118, 210);
    public static final Color COLOR_SECUNDARIO = new Color(239, 246, 255);
    public static final Color COLOR_TEXTO = new Color(33, 33, 33);
    public static final Color COLOR_FONDO = new Color(250, 250, 250);
    
    // Colores adicionales
    public static final Color COLOR_ERROR = new Color(211, 47, 47);
    public static final Color COLOR_EXITO = new Color(76, 175, 80);
    public static final Color COLOR_ADVERTENCIA = new Color(255, 152, 0);
    public static final Color COLOR_SELECCION = new Color(184, 207, 229);
    public static final Color COLOR_SENT = new Color(220, 248, 198);
    public static final Color COLOR_RECEIVED = new Color(240, 240, 240);
    public static final Color COLOR_ORO = new Color(255, 215, 0);
    
    // Fuentes
    public static final Font FUENTE_TITULO = new Font("Arial", Font.BOLD, 36);
    public static final Font FUENTE_TITULO_MEDIO = new Font("Arial", Font.BOLD, 24);
    public static final Font FUENTE_TITULO_PEQUENO = new Font("Arial", Font.BOLD, 20);
    public static final Font FUENTE_SUBTITULO = new Font("Arial", Font.BOLD, 18);
    public static final Font FUENTE_SUBTITULO_PEQUENO = new Font("Arial", Font.BOLD, 16);
    public static final Font FUENTE_LABEL = new Font("Arial", Font.BOLD, 14);
    public static final Font FUENTE_NORMAL = new Font("Arial", Font.PLAIN, 14);
    public static final Font FUENTE_BUTTON = new Font("Arial", Font.BOLD, 13);
    public static final Font FUENTE_LINK = new Font("Arial", Font.PLAIN, 12);
    
    // Dimensiones comunes
    public static final int PADDING_STANDARD = 15;
    public static final int PADDING_PEQUENO = 5;
    public static final int PADDING_GRANDE = 30;
    public static final int ESPACIO_COMPONENTES = 10;
    
    // Rutas para recursos
    public static final String RUTA_RECURSOS = "src/main/resources";
    public static final String RUTA_ICONOS = "/icons";
    public static final String RUTA_FOTOS_PERFIL = "/FotosPerfil";
    
    // Constantes para mensajes
    public static final String EMOJI_PREFIX = "EMOJI:";
} 