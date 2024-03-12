package ui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class CellColorRenderer extends JLabel implements TableCellRenderer
{

    public CellColorRenderer() {
        super.setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        super.setBackground((Color)value);
        super.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        return this;
    }
}
