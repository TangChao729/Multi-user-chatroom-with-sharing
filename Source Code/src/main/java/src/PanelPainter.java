package src;

import src.Commands;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class PanelPainter extends JPanel{

	private ArrayList<String> drawRecords = new ArrayList<>();

	public void setList(ArrayList<String> recordList) {
		this.drawRecords = recordList;
	}

	public void paint(Graphics gr) {
		super.paint(gr);
		draw((Graphics2D) gr, this.drawRecords);
	}

	public void draw(Graphics2D graphics2D, ArrayList<String> recordList) {
		try {
			String[] recordArray = recordList.toArray(new String[recordList.size()]);
			for (String aRecord : recordArray) {
				String[] splitedRecord = aRecord.split(",",8);
				int startX, startY, endX, endY, t, red, green, blue;
				Color color;

				switch (splitedRecord[0]) {
					case Commands.CIRCLE:
						graphics2D.setStroke(new BasicStroke(1));
						startX = Integer.parseInt(splitedRecord[1]);
						startY = Integer.parseInt(splitedRecord[2]);
						endX = Integer.parseInt(splitedRecord[3]);
						endY = Integer.parseInt(splitedRecord[4]);
						red = Integer.parseInt(splitedRecord[5]);
						green = Integer.parseInt(splitedRecord[6]);
						blue = Integer.parseInt(splitedRecord[7]);
						color = new Color(red, green, blue);
						graphics2D.setColor(color);

						int diameter = Math.min(Math.abs(startX - endX),  Math.abs(startY - endY));
						graphics2D.drawOval(Math.min(startX,  endX),  Math.min(startY,  endY),  diameter, diameter);
						break;

					case Commands.RECTANGLE:
						graphics2D.setStroke(new BasicStroke(1));
						startX = Integer.parseInt(splitedRecord[1]);
						startY = Integer.parseInt(splitedRecord[2]);
						endX = Integer.parseInt(splitedRecord[3]);
						endY = Integer.parseInt(splitedRecord[4]);
						red = Integer.parseInt(splitedRecord[5]);
						green = Integer.parseInt(splitedRecord[6]);
						blue = Integer.parseInt(splitedRecord[7]);
						color = new Color(red, green, blue);
						graphics2D.setColor(color);

						graphics2D.drawRect(Math.min(startX,  endX),  Math.min(startY,  endY),  Math.abs(startX - endX), Math.abs(startY - endY));
						break;

					case Commands.TEXT:
						Font font = new Font(null, Font.PLAIN,  10);
						graphics2D.setFont(font);

						startX = Integer.parseInt(splitedRecord[1]);
						startY = Integer.parseInt(splitedRecord[2]);

						red = Integer.parseInt(splitedRecord[3]);
						green = Integer.parseInt(splitedRecord[4]);
						blue = Integer.parseInt(splitedRecord[5]);
						color = new Color(red, green, blue);
						graphics2D.setColor(color);


						String text = splitedRecord[7];
						graphics2D.drawString(text,  startX,  startY);
						break;

					case Commands.LINE:
						graphics2D.setStroke(new BasicStroke(1));
						startX = Integer.parseInt(splitedRecord[1]);
						startY = Integer.parseInt(splitedRecord[2]);
						endX = Integer.parseInt(splitedRecord[3]);
						endY = Integer.parseInt(splitedRecord[4]);
						red = Integer.parseInt(splitedRecord[5]);
						green = Integer.parseInt(splitedRecord[6]);
						blue = Integer.parseInt(splitedRecord[7]);
						color = new Color(red, green, blue);
						graphics2D.setColor(color);

						graphics2D.drawLine(startX,  startY,  endX,  endY);
						break;

					case Commands.TRIANGLE:
						graphics2D.setStroke(new BasicStroke(1));
						startX = Integer.parseInt(splitedRecord[1]);
						startY = Integer.parseInt(splitedRecord[2]);
						endX = Integer.parseInt(splitedRecord[3]);
						endY = Integer.parseInt(splitedRecord[4]);
						red = Integer.parseInt(splitedRecord[5]);
						green = Integer.parseInt(splitedRecord[6]);
						blue = Integer.parseInt(splitedRecord[7]);
						color = new Color(red, green, blue);
						graphics2D.setColor(color);

						graphics2D.drawLine(startX,  startY,  endX,  endY);
						graphics2D.drawLine(startX,  endY,  endX,  endY);
						graphics2D.drawLine(startX,  startY,  startX,  endY);
						break;
					default:
						break;
				}

			}

		} catch (Exception e) {
			System.out.println("panel painter error.");
		}

	}


}