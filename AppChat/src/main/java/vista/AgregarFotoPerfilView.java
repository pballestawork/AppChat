package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.ImageIcon;

import java.awt.SystemColor;

public class AgregarFotoPerfilView extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane = new JPanel();
	private List<File> archivosSubidos = new ArrayList<File>();
	private JLabel lblArchivoSubido;
	private JButton btnAceptar;
	private JButton btnCancelar;

	/**
	 * Create the dialog.
	 */
	public AgregarFotoPerfilView(JFrame owner) {
		super(owner, "Agregar fotos", true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setBackground(SystemColor.desktop);
		setBounds(100, 100, 450, 350);
		getContentPane().setLayout(new BorderLayout());

		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.desktop);
		getContentPane().add(contentPane, BorderLayout.CENTER);

		JEditorPane editorPane = new JEditorPane();
		editorPane.setForeground(SystemColor.text);
		editorPane.setBackground(SystemColor.desktop);
		editorPane.setEditable(false);
		contentPane.add(editorPane);

		JLabel imagenLabel = new JLabel();
		imagenLabel.setHorizontalAlignment(JLabel.CENTER);
		contentPane.add(imagenLabel);

		editorPane.setContentType("text/html");
		editorPane.setText("<h1>Agregar Foto</h1><br> Puedes arrastrar el fichero aquí.  </p>");
		editorPane.setDropTarget(new DropTarget() {
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);

					if (!droppedFiles.isEmpty()) {
						File file = droppedFiles.get(0);
						System.out.println(file.getPath());
						archivosSubidos.add(file);
						lblArchivoSubido.setText(droppedFiles.get(0).getAbsolutePath());
						lblArchivoSubido.setVisible(true);

						// Cargar la imagen en el JLabel
						ImageIcon icon = new ImageIcon(file.getAbsolutePath());
						Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
						imagenLabel.setIcon(new ImageIcon(img));
						lblArchivoSubido.setText(file.getAbsolutePath());
						lblArchivoSubido.setVisible(true);
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
				evt.dropComplete(true);
			}
		});

		lblArchivoSubido = new JLabel();
		lblArchivoSubido.setVisible(false);
		contentPane.add(lblArchivoSubido);

		JButton botonElegir = new JButton("Seleccionar de tu ordenador");
		botonElegir.setForeground(Color.WHITE);
		botonElegir.setBackground(SystemColor.textHighlight);
		contentPane.add(botonElegir);
		botonElegir.addActionListener(e -> {
		    // Define la carpeta inicial (src/main/resources)
		    File directorioInicial = new File("src/main/resources");
		    
		    // Configura el JFileChooser para abrir en esa carpeta
		    JFileChooser fileChooser = new JFileChooser(directorioInicial);
		    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); // Solo permite seleccionar archivos

		    // Filtro opcional para permitir solo imágenes
		    fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
		        "Archivos de imagen", "png", "jpg", "jpeg", "gif"));

		    int resultado = fileChooser.showOpenDialog(this); // Abre el diálogo de selección

		    if (resultado == JFileChooser.APPROVE_OPTION) {
		        // Si se selecciona un archivo
		        File archivoSeleccionado = fileChooser.getSelectedFile();
		        archivosSubidos.clear(); // Limpia la lista actual
		        archivosSubidos.add(archivoSeleccionado); // Agrega el archivo seleccionado a la lista

		        // Muestra la ruta del archivo en el label
		        lblArchivoSubido.setText(archivoSeleccionado.getAbsolutePath());
		        lblArchivoSubido.setVisible(true);

		        // Carga la imagen en el JLabel para previsualizarla
		        ImageIcon icon = new ImageIcon(archivoSeleccionado.getAbsolutePath());
		        Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
		        imagenLabel.setIcon(new ImageIcon(img));
		    } else if (resultado == JFileChooser.CANCEL_OPTION) {
		        JOptionPane.showMessageDialog(this, "Selección cancelada.", "Información", JOptionPane.INFORMATION_MESSAGE);
		    }
		});


		// Panel de botones Aceptar y Cancelar
		JPanel panelBotones = new JPanel();
		panelBotones.setBackground(SystemColor.desktop);
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBackground(SystemColor.inactiveCaption);
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBackground(SystemColor.inactiveCaption);

		// Acción del botón Aceptar
		btnAceptar.addActionListener(ev -> dispose());

		// Acción del botón Cancelar
		btnCancelar.addActionListener(ev -> {
			archivosSubidos.clear(); // Limpia la lista si se cancela
			dispose();
		});

		panelBotones.add(btnAceptar);
		panelBotones.add(btnCancelar);
		getContentPane().add(panelBotones, BorderLayout.SOUTH);

		setLocationRelativeTo(owner); // Centra el diálogo en la ventana principal
	}

	public List<File> showDialog() {
		this.setVisible(true);
		return archivosSubidos;
	}

}
