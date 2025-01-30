package vista;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import dominio.modelo.ContactoIndividual;

public class ListaContactos extends JPanel {

	private JList<ContactoIndividual> listaContactos;
    private JScrollPane scrollLista;

	/**
	 * Create the panel.
	 */
	public ListaContactos() {
		setLayout(new BorderLayout());
        setPreferredSize(new Dimension(200, 600));
        setBorder(BorderFactory.createBevelBorder(1));

        listaContactos = new JList<>();
        listaContactos.setCellRenderer(new ContactoListCellRenderer());

        scrollLista = new JScrollPane(listaContactos);
        scrollLista.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollLista, BorderLayout.CENTER);

	}
	// Método para agregar contactos dinámicamente
    public void setContactos(DefaultListModel<ContactoIndividual> modeloContactos) {
        listaContactos.setModel(modeloContactos);
    }

}
