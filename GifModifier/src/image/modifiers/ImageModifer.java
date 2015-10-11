package image.modifiers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * @author macros @ Imgur
 *
 */
public class ImageModifer {
	
	private static Image brick;
	
	private static final int[] colors = {0xffffff, 0xD9BB7B,0xD67240,0xDE000D,0x0057A8,0xFEC400,0x010101,0x007B28,
		0x009624,0xA83D15,0x478CC6,0xE76318,0x95B90B,0x9C006B,0x5E748C,0x8D7452,
		0x002541,0x003416,0x5F8265,0x80081B,0xF49B00,0x5B1C0C,0x9C9291,0x4C5156,
		0xE4E4DA,0x87C0EA,0xDE378B,0xEE9DC3,0xFFFF99,0x2C1577,0xF5C189,0x300F06,
		0xAA7D55,0x469bc3,0x68c3e2,0xd3f2ea,0xa06eb9,0xcda4de,0xf5f3d7,0xe2f99a,
		0x77774E,0x96B93B};
	
	private static final List<Color> brickColors;
	
	static{
		try {
			brick = ImageIO.read(ImageModifer.class.getResource("/images/brick.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		brickColors = new LinkedList<Color>();
		
		for(int i = 0; i < colors.length; i++) {
			brickColors.add(new Color(colors[i]));
		}
	}

	public static BufferedImage tileImage(BufferedImage img, int size) {
		
		BufferedImage br = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = br.getGraphics();
		int w = 0, h = 0;
		for(w = 0; w < img.getWidth(); w += size) {
			for(h = 0; h < img.getHeight(); h += size) {
				int a = img.getRGB(w, h) >> 24;
				if(a == 0) {
					continue;
				}
			
				g.setColor(new Color(img.getRGB(w, h)));
				g.fillRect(w, h, size, size);
			}
		}
		
		return br;
	}
	
	public static BufferedImage[] tileImage(BufferedImage[] img, int size) {
		BufferedImage[] result = new BufferedImage[img.length];
		for(int i = 0; i < img.length; i++) {
			result[i] = tileImage(img[i], size);
		}
		return result;
	}
	
	public static BufferedImage brickTileImage(BufferedImage img, int size) {
		
		BufferedImage br = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = br.getGraphics();
		int w = 0, h = 0;
		for(w = 0; w < img.getWidth(); w += size) {
			for(h = 0; h < img.getHeight(); h += size) {
				int a = img.getRGB(w, h) >> 24;
				if(a == 0) {
					continue;
				}
				
				BufferedImage tile = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
				Graphics tg = tile.getGraphics();
				tg.drawImage(brick.getScaledInstance(size, size, 0), 0, 0, null);
				int c = findClosestColor(new Color(img.getRGB(w, h))).getRGB();
				tg.setColor(new Color(0xFF & (c >> 16), 0xFF & (c >> 8), 0xFF & (c >> 0), 135));
				tg.fillRect(0, 0, size, size);
				
				g.drawImage(tile, w, h, null);
			}
		}
		
		return br;
	}
	
	public static BufferedImage[] brickTileImage(BufferedImage[] img, int size) {
		BufferedImage[] result = new BufferedImage[img.length];
		for(int i = 0; i < img.length; i++) {
			result[i] = brickTileImage(img[i], size);
		}
		return result;
	}
	
	private static Color findClosestColor(Color c) {
		Color closest = Color.black;
		double minDist = Double.MAX_VALUE;
		
		for(Color bc : brickColors) {
			double d = Math.pow(c.getRed() - bc.getRed(), 2) + Math.pow(c.getBlue() - bc.getBlue(), 2) + Math.pow(c.getGreen() - bc.getGreen(), 2);
			if(d < minDist) {
				minDist = d;
				closest = bc;
			}
		}
		
		return closest;
	}
	
}
