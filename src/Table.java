

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 * JPanel of a table containing a row that is buttons
 * Adapted from example from http://www.java2s.com/Code/Java/Swing-Components/ButtonTableExample.htm
 * @author Oliver
 *
 */
@SuppressWarnings("serial")
public class Table extends JPanel implements Observer
{
	private DefaultTableModel dm;
	private JTable table;
	private JScrollPane scroll;
	private MyClient info;

	public Table(NotificationQueue queue, MyClient info)
	{
		super();
		this.info = info;

		dm = new DefaultTableModel();
		// should start empty but here is how you set things in it
		//		dm.setDataVector(new Object[][] { { "Oliver", 0, "Challenge" }, { "Billy", 0, "Challenge" } },
		//				new Object[] { "Player", "Score", "-" });

		dm.setDataVector(new Object[][] {}, new Object[] { "Player", "Score", "-" });

		table = new JTable(dm);
		table.getColumn("-").setCellRenderer(new ButtonRenderer());
		table.getColumn("-").setCellEditor(new ButtonEditor(new JCheckBox()));
		table.getTableHeader().setReorderingAllowed(false);
		scroll = new JScrollPane(table);
		add(scroll);

		addARow(info.getName()); // you can always be certain that at least you are logged in
	}

	public void addARow(String name)
	{
		dm.addRow(new Object[] { name, 0, "Challenge" });
	}

	/**
	 * what to do when notified of a change
	 */
	@Override
	public void update(Observable o, Object arg)
	{
		// this method is very dodgy
		// TODO improve table update
		
		ArrayList<String> online = info.getAllPlayers();
		ArrayList<String> copy = new ArrayList<String>(online.size());
		for (int i=0;i<online.size();i++)
		{
			copy.add(online.get(i));
		}

		// for all rows in the table add the items in "online" that aren't in the table already and remove those that left
		for (int i = 0; i < table.getRowCount(); i++)
		{
			// if the name is in the table but not the list, remove from table (they have logged out)
			if (!online.contains(table.getValueAt(i, 0)))
			{
				System.out.println("Removing: " + table.getValueAt(i, 0));
				dm.removeRow(i);
			}
			// remove it from the list we've been given
			// it should now contain only name we have to add to our table
			else
			{
				copy.remove(table.getValueAt(i, 0));
			}
		}
		// add the new names to the list
		for (int i = 0; i < copy.size(); i++)
		{
			System.out.println("Adding: " + copy.get(i));
			addARow(copy.get(i));
		}

		//System.out.println("UPDATED TABLE");

		repaint();
	}
}

@SuppressWarnings("serial")
class ButtonRenderer extends JButton implements TableCellRenderer
{

	public ButtonRenderer()
	{
		setOpaque(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column)
	{
		if (isSelected)
		{
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		}
		else
		{
			setForeground(table.getForeground());
			setBackground(UIManager.getColor("Button.background"));
		}
		setText((value == null) ? "" : value.toString());
		return this;
	}
}

@SuppressWarnings("serial")
class ButtonEditor extends DefaultCellEditor
{
	protected JButton button;
	private String label;
	private boolean isPushed;
	private JTable table;
	private int row;

	public ButtonEditor(JCheckBox checkBox)
	{
		super(checkBox);
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				fireEditingStopped();
			}
		});
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		this.table = table;
		this.row = row;

		if (isSelected)
		{
			button.setForeground(table.getSelectionForeground());
			button.setBackground(table.getSelectionBackground());
		}
		else
		{
			button.setForeground(Color.blue); // turn blue when pressed
			button.setBackground(UIManager.getColor("Button.background"));
		}
		label = (value == null) ? "" : value.toString();
		button.setText(label);
		isPushed = true;
		return button;
	}

	public Object getCellEditorValue()
	{
		if (isPushed)
		{
			if (label.equals("Challenge"))
			{
				JOptionPane.showMessageDialog(button,
						table.getValueAt(row, 0) + " has been sent a play request!\n(This is a lie)");
				// TODO send string to server (via ClientSender) in format "/play <opponent>"
				label = "Pending...";
			}
			else if (label.equals("Respond"))
			{
				// TODO bring up a yes/no box which act as "/accept" and "/decline"
			}

		}
		isPushed = false;
		return new String(label);
	}

	public boolean stopCellEditing()
	{
		isPushed = false;
		return super.stopCellEditing();
	}

	protected void fireEditingStopped()
	{
		super.fireEditingStopped();
	}
}
