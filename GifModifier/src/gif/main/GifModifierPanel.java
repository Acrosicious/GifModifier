package gif.main;

import gif.AnimatedGifEncoder;
import gif.GifExtractor;
import gif.WrongFileException;
import image.modifiers.ImageModifer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JPanel;

/**
 * @author macros @ Imgur
 *
 */
public class GifModifierPanel extends JPanel implements Runnable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BufferedImage[] unmodGif;
	private BufferedImage[] currentGif;
	private int index;
	
	private int tiling = 20;
	
	private int fps = 20;
	private boolean threadRunning = true;
	private Thread gifThread;
	
	private BufferedImage drawingImage;
	
	public GifModifierPanel() {
		setPreferredSize(new Dimension(0, 0));
		
		drawingImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		
		gifThread = new Thread(this);
		gifThread.start();
	}
	
	public void loadGif(File f) {
		try {
			unmodGif = GifExtractor.extractFrames(f);
			drawingImage = new BufferedImage(unmodGif[0].getWidth(), unmodGif[0].getHeight(), BufferedImage.TYPE_INT_ARGB);
			setPreferredSize(new Dimension(unmodGif[0].getWidth(), unmodGif[0].getHeight()));
			setTiling(tiling);
		} catch (WrongFileException e) {
			e.printStackTrace();
		}
	}
	
	public void saveGif(File f) {
		AnimatedGifEncoder e = new AnimatedGifEncoder();
		e.start(f.getAbsolutePath());
		e.setFrameRate(fps);
		e.setRepeat(0);
		
		BufferedImage img = new BufferedImage(unmodGif[0].getWidth(), unmodGif[0].getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		for(int i = 0; i < currentGif.length; i++) {
			img.getGraphics().drawImage(currentGif[i], 0, 0, null);
			e.addFrame(img);
		}

		e.finish();
	}
	
	public void setTiling(int tiling) {
		this.tiling = tiling;
		if(unmodGif != null) {
			currentGif = ImageModifer.brickTileImage(unmodGif, tiling);
		}		
	}
	
	public void setFPS(int fps) {
		this.fps = fps;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		Graphics dg = drawingImage.getGraphics();
		if(currentGif != null) {
			dg.drawImage(currentGif[index], 0, 0, null);
			g.drawImage(drawingImage, (getWidth() - currentGif[0].getWidth()) / 2, 0, null);
		}
		
		
	}

	@Override
	public void run() {
		
		while(threadRunning) {			
			if(currentGif != null) {
				index++;
				index %= currentGif.length;
				repaint();
			}			
			
			try {
				Thread.sleep(1000/fps);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}
