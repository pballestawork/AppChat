package vista;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import dominio.modelo.ContactoIndividual;

public class ContactosTable extends AbstractTableModel {

    private List<ContactoIndividual> contactos;
    private String[] columnNames = {"Nombre", "Teléfono", "Saludo"};

    public ContactosTable(List<ContactoIndividual> contactos) {
        this.contactos = contactos;
    }

    @Override
    public int getRowCount() {
        return contactos.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ContactoIndividual c = contactos.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return c.getNombre();
            case 1:
                return c.getUsuario().getTelefono();
            case 2:
                return c.getUsuario().getSaludo();
            default:
                return "";
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    // (Opcional) Si deseas que las celdas sean de solo lectura
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    // (Opcional) Puedes agregar métodos para actualizar la lista de contactos
    public void setContactos(List<ContactoIndividual> contactos) {
        this.contactos = contactos;
        fireTableDataChanged();
    }
}
