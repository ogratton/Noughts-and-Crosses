

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Dual purpose button (used for both the exit and concede buttons)
 * @author Oliver
 *
 */
@SuppressWarnings("serial")
public class Button extends JPanel
{

	public Button(String text, NotificationQueue queue)
	{
		JButton button = new JButton(text);
		
		if (text.equalsIgnoreCase("exit"))
		{
			button.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					queue.offer(new PlainNote("/exit"));
				}
			});
		}
		
		else if (text.equalsIgnoreCase("concede"))
		{
			button.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					queue.offer(new PlainNote("/leave"));
				}
			});
		}
		add(button);
	}
}
