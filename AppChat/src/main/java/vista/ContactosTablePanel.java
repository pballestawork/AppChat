package vista;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import dominio.modelo.ContactoIndividual;

public class ContactosTablePanel extends JPanel {

    private JTable table;
    private ContactosTable tableModel;

    public ContactosTablePanel(List<ContactoIndividual> contactos) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(400, 300));
        
        // Crear el modelo de tabla a partir de la lista de contactos
        tableModel = new ContactosTable(contactos);
        table = new JTable(tableModel);
        
        // Opcional: configurar la tabla (por ejemplo, ancho de columnas, selección)
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    public JTable getTable() {
        return table;
    }
    
    // (Opcional) Método para actualizar los contactos
    public void actualizarContactos(List<ContactoIndividual> nuevosContactos) {
        tableModel.setContactos(nuevosContactos);
    }
}
