

import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * The 3x3 grid needed to play noughts and crosses on
 * @author Oliver
 *
 */
@SuppressWarnings("serial")
public class Grid extends JPanel //should implement Observer
 implements Observer
{
	public static final String BLANK = ".", CROSS = "X", NOUGHT = "O";
	
	private JButton[][] cell;
	private MyClient info;
	
	public Grid(NotificationQueue queue, MyClient info)
	{
		super();
		this.info = info;
		
		//create array of buttons
		cell = new JButton[3][3];
		
		//set layout of panel
		setLayout(new GridLayout(3, 3));
		
		//for each square in grid:create a button; place on panel
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				cell[i][j] = new JButton(" ");
				final int x = j, y = i;
				// TODO make this print to the server somehow rather than make a dialog box
				//cell[i][j].addActionListener(e->JOptionPane.showMessageDialog(cell[x][y],"/move "+x+" "+y));
				cell[i][j].addActionListener(e->queue.offer(new PlainNote("/move "+x+" "+y)));
				cell[i][j].setEnabled(false); // false to start with, until they're in a game
				add(cell[i][j]);
			}
		}
	}

	/**
	 * when notified of a change
	 */
	@Override
	public void update(Observable o, Object arg)
	{
		
		// for each square do the following:
		// if it's a NOUGHT, put O on button
		// if it's a CROSS, put X on button
		// else put     on button
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				if(info.getBoardSquare(i, j) == CROSS)
				{
					cell[i][j].setText("X");
					cell[i][j].setEnabled(false);
				}
				else if(info.getBoardSquare(i, j) == NOUGHT)
				{
					cell[i][j].setText("O");
					cell[i][j].setEnabled(false);
				}
				else
				{
					cell[i][j].setText(" ");
					cell[i][j].setEnabled(info.inGame());
				}
			}
		}
		
		//System.out.println("UPDATED GRID");
		
		repaint();
	}

//	@Override
//	public void update(Observable obs, Object obj)
//	{
//		// for each square do the following:
//		// if it's a NOUGHT, put O on button
//		// if it's a CROSS, put X on button
//		// else put     on button
//		for(int i = 0; i < 3; i++)
//		{
//			for(int j = 0; j < 3; j++)
//			{
//				if(info.getBoardSquare(i, j) == CROSS)
//				{
//					cell[i][j].setText("X");
//					cell[i][j].setEnabled(false);
//				}
//				else if(info.getBoardSquare(i, j) == NOUGHT)
//				{
//					cell[i][j].setText("O");
//					cell[i][j].setEnabled(false);
//				}
//				else
//				{
//					cell[i][j].setText(" ");
//					/*boolean notOver = (model.whoWon() ==
//						NoughtsCrosses.BLANK);
//					cell[i][j].setEnabled(notOver);*/
//					cell[i][j].setEnabled(true);
//				}
//			}
//		}
//		repaint();
//	}

}
