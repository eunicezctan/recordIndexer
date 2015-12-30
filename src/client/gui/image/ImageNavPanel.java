package client.gui.image;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import shared.communication.DownloadBatchResult;
import client.controller.BatchListener;
import client.controller.BatchState;


/**
 * Image Navigation
 */
public class ImageNavPanel extends JComponent implements BatchListener {
	
private static Image NULL_IMAGE = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
	
	private BatchState batchState;
	private Image shapeImage;
	private DrawingRect navRect ;
	private double navZoom;
	
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
	 * 
	 */
	
	public ImageNavPanel() {

		w_translateX = 0;
		w_translateY = 0;
		scale = 1.0;
		navZoom=1;
		
		initDrag();

		shapes = new ArrayList<DrawingShape>();
		listeners = new ArrayList<DrawingListener>();
		setBackground(Color.LIGHT_GRAY);
		setLayout(new BorderLayout());
		
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);
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
		
		int width =  (int) (this.getWidth()/1.8);
		int height = (int) (this.getHeight()/1.2);
		Graphics2D g2 = (Graphics2D)g;
		drawBackground(g2);
		g.drawImage(shapeImage, 150, 25,width,height,null);
		
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
	
	private void notifyTranslationChanged(int w_newTranslateX, int w_newTranslateY) {
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


		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
		
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
	

	/**
	 *  BatchState Listener methods
	 */

	@Override
    public void selectedCell(String[][] cellValue) {
		
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
			
            shapeImage = ImageIO.read(new URL(getBatchRes.getImageUrl()));     
            navRect = new DrawingRect(new Rectangle2D.Double(-300, -120, 650, 200 ), new Color(136,136,136,100));
            shapes.add(navRect);
            repaint();
            
            }
		   catch (IOException e1) {
               e1.printStackTrace();}
		}
		
    }

	
	/**
	 *  compute view port zooming
	 */
	@Override
    public void Zoom(double zoomNo, boolean setZoom) {
	
		
		if(setZoom)
			navZoom=navZoom/0.5;
		
		else
			navZoom= navZoom*0.5;
		
		setScale(navZoom);
	    
    }
	
	
	/**
	 *  For restoring view port zooming for existing user state
	 */
	@Override
    public void Zoom(double zoomNo) {
	   
		navZoom = batchState.getNavZoom();
		setScale(navZoom);
	    
    }

	@Override
    public void Invert() {
	    
    }
	

	/**
	 * Remove all components when user submit
	 */	
	@Override
    public void Submit() {
	    
		 shapeImage = null;
         shapes.clear();
         repaint();
	    
    }

	@Override
    public void Toggle() {
	    
    }

	/**
	 * Save view port zooming scale in batch state  
	 */	
	@Override
    public void Save() {
	   batchState.setNavZoom(navZoom);
	    
    }

	
	/**
	 * Remove all components when user logout
	 */	
	@Override
    public void Logout() {
	    
		 shapeImage = null;
         shapes.clear();
         repaint();
    }

		
}// end of ImageNavPanel
