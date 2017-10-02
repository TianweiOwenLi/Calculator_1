package calculator;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import acm.graphics.*;

public class Field extends JFrame{
	
	static Field f = new Field();
	
	static GCanvas c = new GCanvas();
	
	static JLabel lbl = new JLabel(), xt = new JLabel("< x <"), yt = new JLabel("< y <");
	
	static JTextField txt = new JTextField(), val = new JTextField(), xt1 = new JTextField(), xt2 = new JTextField(), yt1 = new JTextField(), yt2 = new JTextField();
	
	static String t, v;
	
	static GRect paper = new GRect(0, 0, 399, 400), bar = new GRect(0, 400, 400, 50);
	
	static JButton clear = new JButton("REL"), refresh = new JButton("GPH");
	
	static GLine x_axis = new GLine(0, 200, 399, 200), y_axis = new GLine(200, 0, 200, 399);
	
	static double low_x = -6, high_x = 6, low_y = -6, high_y = 6;
	
	static ArrayList<GLine> func = new ArrayList<GLine>();
	
	//For initializing the calculator.
	static void init(){
		
		//Initializing an object of Field. Note that it is extended to JFrame so following operations are valid.
		f.setVisible(true);
		f.setBounds(600, 200, 400, 620);
		f.setDefaultCloseOperation(EXIT_ON_CLOSE);
		f.setTitle("CALCULATOR");
		f.setResizable(false);
		
		//Add the "<x<", "<y<" to canvas.
		xt.setBounds(160, 412, 50, 25);
		c.add(xt);
		yt.setBounds(300, 412, 50, 25);
		c.add(yt);
		
		//Add canvas to Field object f.
		c.setBackground(Color.GRAY);
		f.add(c);
		
		//Add an output label to canvas.
		lbl.setOpaque(true);
		lbl.setBackground(Color.getHSBColor(0.5f, 0.3f, 0.95f));		//I like this color very much. Don't change it.
		lbl.setBounds(20, 470, 360, 25);
		c.add(lbl);
		
		//Add all text-fields to canvas. Initial text is automatically generated.
		add(txt, 16, 510, 368, 32, "//Insert a function.");
		add(val, 16, 550, 368, 32, "//Insert a function.");
		add(xt1, 110, 410, 50, 32, low_x);
		add(xt2, 200, 410, 50, 32, high_x);
		add(yt1, 250, 410, 50, 32, low_y);
		add(yt2, 340, 410, 50, 32, high_y);
		
		//Initializing the GRect object "paper". This works as a small "canvas" within the calculator.
		paper.setFilled(true);
		paper.setFillColor(Color.WHITE);
		paper.setColor(Color.WHITE);
		c.add(paper);
		
		//In order to make the calculator look awesome, I added a colored bar on the upper edge of input sections.
		bar.setFilled(true);
		bar.setFillColor(Color.LIGHT_GRAY);
		bar.setColor(Color.LIGHT_GRAY);
		c.add(bar);
		
		//Add x and y axis to canvas.
		x_axis.setColor(Color.RED);
		y_axis.setColor(Color.RED);
		c.add(x_axis);
		c.add(y_axis);
		
		//Set locations of two buttons.
		clear.setBounds(5, 409, 45, 35);
		refresh.setBounds(55, 409, 45, 35);
		
		//Add action listener for one button.
		clear.addActionListener(new ActionListener(){
			
			//This will remove all components on the canvas and reload them back, but function graphs will disappear.s
			public void actionPerformed(ActionEvent e){
				c.removeAll();
				reload();
			}
			
		});
		
		//Add action listener for another button.
		refresh.addActionListener(new ActionListener(){
			
			//This will update the bounds of graph, re-generate x and y axis according to this, and repaint the graph according to new bounds.
			public void actionPerformed(ActionEvent e) {
				
				//If no bounds are empty, they'll be updated.
				if(!(xt1.getText().trim().equals("") || xt2.getText().trim().equals("") || yt1.getText().trim().equals("") || yt2.getText().trim().equals(""))){
					
					low_x = new Double(xt1.getText()).doubleValue();
					high_x = new Double(xt2.getText()).doubleValue();
					low_y = new Double(yt1.getText()).doubleValue();
					high_y = new Double(yt2.getText()).doubleValue();
					
				}
				
				//Reload all components and re-graph the function.
				reload();
				graph(txt.getText());
				
			}
		});
		
		//Add two buttons to canvas.
		c.add(clear);
		c.add(refresh);
	}
	
	//Reloading the components in calculator.
	public static void reload(){
		f.add(c);
		c.add(xt);
		c.add(yt);
		c.add(paper);
		c.add(bar);
		c.add(clear);
		c.add(refresh);
		c.add(lbl);
		c.add(x_axis);
		c.add(y_axis);
		add(txt, 16, 510, 368, 32, txt.getText());
		add(val, 16, 550, 368, 32, val.getText());
		add(xt1, 110, 410, 50, 32);
		add(xt2, 200, 410, 50, 32);
		add(yt1, 250, 410, 50, 32);
		add(yt2, 340, 410, 50, 32);
		set_XY();
	}
	
	//Add a text-field to canvas.
	private static void add(JTextField txt, int a, int b, int w, int h, String str){
		txt.setBounds(a, b, w, h);
		txt.setText(str);
		c.add(txt);
	}
	
	//Add a text-field to canvas.
	private static void add(JTextField txt, int a, int b, int w, int h, double d){
		txt.setBounds(a, b, w, h);
		txt.setText(new Double(d).toString());
		c.add(txt);
	}
	
	//Add a text-field to canvas.
		private static void add(JTextField txt, int a, int b, int w, int h){
			txt.setBounds(a, b, w, h);
			c.add(txt);
		}
	
	//Sets the location of X and Y axis.
	private static void set_XY(){
		
		//If x-axis should appear, draw x-axis.
		if(low_y*high_y <= 0){
			x_axis.setVisible(true);
			x_axis.setStartPoint(0, high_y*(400/(high_y-low_y)));
			x_axis.setEndPoint(399, high_y*(400/(high_y-low_y)));
		}else
			x_axis.setVisible(false);			//Else, x-axis will not appear.
		
		//If y-axis should appear, draw y-axis.
		if(low_x*high_x <= 0){
			y_axis.setVisible(true);
			y_axis.setStartPoint(-low_x*(400/(high_x-low_x)),0);
			y_axis.setEndPoint(-low_x*(400/(high_x-low_x)),399);
		}else
			y_axis.setVisible(false);			//Else, y-axis will not appear.
	}
	
	//Graphs a function according to input string. It is drawn by connecting a large number of straight lines.
	private static void graph(String f){
		
		//Get rid of notation parts in f(x).
		if(f.indexOf("//") != -1)
			f = f.substring(0, f.indexOf("//"));
		
		//Will not graph if f(x) is invalid.
		if(!f.equals("")){
			
			//Draw lines that have locations ranging from 0 to 400.
			for(int i=0;i<399;i++){
				
				//Calculate x values.
				double a = Fx.evaluate(f, low_x+(high_x-low_x)*i/400), b = Fx.evaluate(f, low_x+(high_x-low_x)*(i+1)/400);
				
				//This shows if a section of the function is completely out of bounds.
				boolean no = (a >= high_y|| a <= low_y)&&(b >= high_y|| b <= low_y);
				
				//If one line is partially out of bounds, it is drawn to the bounds.
				if(a >= high_y)
					a = high_y;
				if(a <= low_y)
					a = low_y;
				
				if(b >= high_y)
					b = high_y;
				if(b <= low_y)
					b = low_y;
				
				//Convert y value into actual position.
				a = (high_y-a)*400/(high_y-low_y);
				b = (high_y-b)*400/(high_y-low_y);
				GLine l = new GLine(i, a, i+1, b);
				l.setColor(Color.BLUE);
				
				//If one line is completely out of bounds, it will not appear.
				if(no)
					l.setVisible(false);
				
				//Draw the line.
				c.add(l);
			}
		}
	}
	
	public static void main(String[] args) {
		
		//initialize.
		init();
		
		while(true){
			
			//Get the input text of two text-fields.
			t = txt.getText();
			v = val.getText();
			
			//For getting rid of non-executive notations, such as this sentence.
			if(t.indexOf("//") != -1)
				t = t.substring(0, t.indexOf("//"));
			if(v.indexOf("//") != -1)
				v = v.substring(0, v.indexOf("//"));		
			
			//Evaluate according to x.
			if(!(txt.hasFocus()||val.hasFocus()||t.equals("")||v.equals("")||refresh.hasFocus())){
				lbl.setText("f(x) = "+m.round((Fx.evaluate(t, Fx.evaluate(v, 0))),6));
			}
		}
		
	}

}
