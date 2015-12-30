package client.gui.image;


import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.event.*;

import javax.imageio.*;
import javax.swing.*;

import shared.communication.DownloadBatchResult;

import java.util.*;
import java.io.*;
import java.net.URL;

import client.controller.*;


/**
 * Main Image display for indexer batch	
 */

public class ImagePanel extends JComponent implements BatchListener {


	private static Image NULL_IMAGE = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
	
	private BatchState batchState;
	private DrawingRect Newrect ;
	private Image imageURL;
	private int recordHeight;
	private int iWidth;
	private int iHeight;
	
	private int w_translateX;
	private int w_translateY;
	private double scale;
	
	private boolean dragging;
	private int w_dragStartX;
	private int w_dragStartY;
	private int w_dragStartTranslateX;
	private int w_dragStartTranslateY;
	private AffineTransform dragTransform;

	private ArrayList<DrawingListener> listeners;
	private ArrayList<DrawingShape> shapes;
	

	/**
	 * Constructor
	 */
	public ImagePanel() {
		
		w_translateX = 0;
		w_translateY = 0;
		scale = 1.0;
		
		initDrag();

		shapes = new ArrayList<DrawingShape>();
		listeners = new ArrayList<DrawingListener>();
		setBackground(Color.LIGHT_GRAY);
	
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);
		this.addMouseWheelListener(mouseAdapter);
	}
	
	/**
	 * Set pointer
	 */
	public void setPointer(BatchState batchState){
		
		this.batchState = batchState;
		this.batchState.addListener(this);	
	}
	
	
	
	private void initDrag() {
		dragging = false;
		w_dragStartX = 0;
		w_dragStartY = 0;
		w_dragStartTranslateX = 0;
		w_dragStartTranslateY = 0;
		dragTransform = null;
	}
	
	
	public void setScale(double newScale) {
		scale = newScale;
		this.repaint();
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;
		drawBackground(g2);

		g2.translate(getWidth()/2, getHeight()/2);
		g2.scale(scale, scale);
		g2.translate(w_translateX, w_translateY);

		drawShapes(g2);
	}
	
	private void drawBackground(Graphics2D g2) {
		g2.setColor(getBackground());
		g2.fillRect(0,  0, getWidth(), getHeight());
	}

	private void drawShapes(Graphics2D g2) {
		for (DrawingShape shape : shapes) {
			shape.draw(g2);
		}
	}
	
	
	/**
	 * Translation between image and image nav
	 * 
	 */
	public void setTranslation(int w_newTranslateX, int w_newTranslateY) {
		w_translateX = w_newTranslateX;
		w_translateY = w_newTranslateY;
		this.repaint();
	}
	
	public void addDrawingListener(DrawingListener listener) {
		listeners.add(listener);
	}
	
	public void notifyTranslationChanged(int w_newTranslateX, int w_newTranslateY) {
		for (DrawingListener listener : listeners) {
			listener.translationChanged(w_newTranslateX, w_newTranslateY);
		}
	}
	
	

	/**
	 * Mouse Listener for image nav
	 * 
	 */
	private MouseAdapter mouseAdapter = new MouseAdapter() {

		@Override
		public void mouseClicked(MouseEvent e) {
			
			int d_X = e.getX();
			int d_Y = e.getY(); 
			 
			AffineTransform transform = new AffineTransform();
			transform.translate(getWidth()/2, getHeight()/2);
			transform.scale(scale, scale);
			transform.translate(w_translateX, w_translateY);
			
			Point2D d_Pt = new Point2D.Double(d_X, d_Y);
			Point2D w_Pt = new Point2D.Double();
			try
			{
				transform.inverseTransform(d_Pt, w_Pt);
			}
			catch (NoninvertibleTransformException ex) {
				return;
			}
			int w_X = (int)w_Pt.getX();
			int w_Y = (int)w_Pt.getY();
			
			batchState.imageSelectedChanged(w_X, w_Y);
			
		}
		
		
		@Override
		public void mousePressed(MouseEvent e) {
			int d_X = e.getX();
			int d_Y = e.getY();
			
			AffineTransform transform = new AffineTransform();
			transform.translate(getWidth()/2, getHeight()/2);
			transform.scale(scale, scale);
			transform.translate(w_translateX, w_translateY);
			
			Point2D d_Pt = new Point2D.Double(d_X, d_Y);
			Point2D w_Pt = new Point2D.Double();
			try
			{
				transform.inverseTransform(d_Pt, w_Pt);
			}
			catch (NoninvertibleTransformException ex) {
				return;
			}
			int w_X = (int)w_Pt.getX();
			int w_Y = (int)w_Pt.getY();
			
			boolean hitShape = false;
			
			Graphics2D g2 = (Graphics2D)getGraphics();
			for (DrawingShape shape : shapes) {
				if (shape.contains(g2, w_X, w_Y)) {
					hitShape = true;
					break;
				}
			}
			
			if (hitShape) {
				dragging = true;		
				w_dragStartX = w_X;
				w_dragStartY = w_Y;		
				w_dragStartTranslateX = w_translateX;
				w_dragStartTranslateY = w_translateY;
				dragTransform = transform;
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {		
			if (dragging) {
				int d_X = e.getX();
				int d_Y = e.getY();
				
				Point2D d_Pt = new Point2D.Double(d_X, d_Y);
				Point2D w_Pt = new Point2D.Double();
				try
				{
					dragTransform.inverseTransform(d_Pt, w_Pt);
				}
				catch (NoninvertibleTransformException ex) {
					return;
				}
				int w_X = (int)w_Pt.getX();
				int w_Y = (int)w_Pt.getY();
				
				int w_deltaX = w_X - w_dragStartX;
				int w_deltaY = w_Y - w_dragStartY;
				
				w_translateX = w_dragStartTranslateX + w_deltaX;
				w_translateY = w_dragStartTranslateY + w_deltaY;
				
				notifyTranslationChanged(-1*w_translateX, -1*w_translateY);
				
				repaint();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			initDrag();
		}


		/**
		 *  Send the mouse wheel rotation direction to batch state
		 */
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			
			if(e.getPreciseWheelRotation() > 0)
				batchState.calZoom(true);
			
			else
				batchState.calZoom(false);
			
			repaint();
		}	
	};
	

	////////////////
	// Drawing Shape
	////////////////

	interface DrawingShape {
		boolean contains(Graphics2D g2, double x, double y);
		void draw(Graphics2D g2);
		Rectangle2D getBounds(Graphics2D g2);
	}


	class DrawingRect implements DrawingShape {

		private Rectangle2D rect;
		private Color color;
		
		public DrawingRect(Rectangle2D rect, Color color) {
			this.rect = rect;
			this.color = color;
		}
		
		public void setColor (Color color) {
			this.color = color;
		}

		@Override
		public boolean contains(Graphics2D g2, double x, double y) {
			return rect.contains(x, y);
		}

		@Override
		public void draw(Graphics2D g2) {
			g2.setColor(color);
			g2.fill(rect);
		}
		
		@Override
		public Rectangle2D getBounds(Graphics2D g2) {
			return rect.getBounds2D();
		}
	}

	class DrawingImage implements DrawingShape {

		private Image image;
		private Rectangle2D rect;
		
		public DrawingImage(Image image, Rectangle2D rect) {
			this.image = image;
			this.rect = rect;
		}

		@Override
		public boolean contains(Graphics2D g2, double x, double y) {
			return rect.contains(x, y);
		}

		@Override
		public void draw(Graphics2D g2) {
			g2.drawImage(image, (int)rect.getMinX(), (int)rect.getMinY(), (int)rect.getMaxX(), (int)rect.getMaxY(),
							0, 0, image.getWidth(null), image.getHeight(null), null);
		}	
		
		@Override
		public Rectangle2D getBounds(Graphics2D g2) {
			return rect.getBounds2D();
		}
	}

	
	/**
	 *  BatchState Listener methods
	 */

	
	/**
	 * Highlight and load the selected cell with values
	 */	
	@Override
    public void selectedCell(String[][] cellValue) {
	     
		int calXcoord = batchState.getCalXcoord();
		int nYcoord = batchState.getnYcoord();
		int calWidth  = batchState.getCalWidth();

		if(batchState.getToggle())
		{
			Newrect=new DrawingRect(new Rectangle2D.Double( calXcoord, nYcoord ,calWidth, recordHeight), new Color(51,153,255,100));
	 		shapes.set(1,Newrect);
		}
		else
		{
			Newrect=new DrawingRect(new Rectangle2D.Double(calXcoord , nYcoord ,calWidth, recordHeight), new Color(0,0,0,0));
	 		shapes.set(1,Newrect);
		}
		
		repaint();    
    }

	@Override
    public void cellChanged(String newCellValue) {
	    // TODO Auto-generated method stub
	    
    }

	
	/**
	 *  Download image  & draw rect when a batch is down loaded
	 */

	@Override
    public void Download(DownloadBatchResult getBatchRes) {
	   
		if(getBatchRes!=null)
		{
		try {
			
			iWidth = -250;
			iHeight = -150;
			batchState.setImageDetail(iWidth, iHeight);
			recordHeight = getBatchRes.getRecordHeight()/2;
			
			ArrayList<Integer> getXcoord = getBatchRes.getGetXcoord();;
			ArrayList<Integer> getWidth = getBatchRes.getGetWidth(); 
			
			int rWidth = iWidth + getXcoord.get(0)/2; 
			int rHeight = iHeight +getBatchRes.getFirstYcoord()/2;
			
			batchState.setCalXcoord(rWidth); //set Coordinate in BatchState
			batchState.setnYcoord(rHeight);
			batchState.setCalWidth(getWidth.get(0)/2);
			
            imageURL = ImageIO.read(new URL(getBatchRes.getImageUrl()));
            shapes.add(new DrawingImage(imageURL, new Rectangle2D.Double( iWidth, iHeight ,imageURL.getWidth(null)/2 , imageURL.getHeight(null)/2)));
            
            Newrect = new DrawingRect(new Rectangle2D.Double(rWidth, rHeight,getWidth.get(0)/2 , recordHeight ), new Color(51,153,255,100));
            shapes.add(Newrect);
            repaint();
            
            }
		   catch (IOException e1) {
               e1.printStackTrace();}
		}
		
    }

	@Override
    public void Zoom(double zoomNo, boolean setZoom) {
		
		setScale(zoomNo);
	    
    }
	
	
	@Override
    public void Zoom(double zoomNo) {
	   
		setScale(zoomNo); 
    }

	
	/**
	 *  Invert the image
	 */

	@Override
    public void Invert() {
	    
		RescaleOp op = new RescaleOp(-1.0f, 255f, null);
		imageURL = op.filter((BufferedImage) imageURL, null);
		shapes.set(0,new DrawingImage(imageURL, new Rectangle2D.Double(iWidth, iHeight,imageURL.getWidth(null)/2 , imageURL.getHeight(null)/2)));	
		repaint();
    }

	

	/**
	 * Remove all components when user submit
	 */	
	@Override
    public void Submit() {
	    
		 shapes.clear();
         repaint();
    }

	

	/**
	 * Set cell highlight on or off
	 */	
	@Override
    public void Toggle() {
	 
		boolean getToggle = batchState.getToggle();
			
		if(getToggle==false)
		{
			Newrect.setColor(new Color(0,0,0,0));
			repaint();
		}
		
		else if (getToggle ==true)
		{
			Newrect.setColor(new Color(51,153,255,100));
			repaint();
		}
	    
    }

	

	/**
	 * Save translation data to batch state
	 */	
	@Override
    public void Save() {
		
	   batchState.setW_translateX(w_translateX);
	   batchState.setW_translateY(w_translateY);
    }

	
	/**
	 * Remove all components when user logout
	 */	
	@Override
    public void Logout() {
		
		shapes.clear();
        repaint();
    }
	
}// end of ImagePanel
