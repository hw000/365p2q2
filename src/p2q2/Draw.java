package p2q2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
//import javax.swing.JPanel;

public class Draw extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int[][]red,green,blue,grey;
	protected int width,height;
	public Draw(int[][] red, int[][] green, int[][] blue, int height, int width){
		super();
		this.red=red;
		this.green=green;
		this.blue=blue;
		this.height=height;
		this.width=width;
		
	
		this.setPreferredSize(new Dimension(width*1,height*1));
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	public void paint(Graphics g){
		super.paint(g);
		int locX=0;
		int locY=0;
		Color c;
		for(int i=0;i<height;i++) {
			for(int j=0;j<width;j++){
					c=new Color(red[j][i],green[j][i],blue[j][i]);
			
				g.setColor(c);
				locX=j;
				locY=i;
				g.drawLine(locX,locY,locX,locY);
			}
		}
	}

}
