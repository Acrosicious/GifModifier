package gif;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author macros @ Imgur
 *
 */
public class GifExtractor {

	public static BufferedImage[] extractFrames(File gif) throws WrongFileException {
		
		if(!gif.getName().endsWith(".gif")) {
			throw new WrongFileException("File is no GIF!");
		}
		
		BufferedImage[] master = null;
		
		try {

		    String[] imageatt = new String[]{
		            "imageLeftPosition",
		            "imageTopPosition",
		            "imageWidth",
		            "imageHeight"
		    };


		    ImageReader reader = (ImageReader)ImageIO.getImageReadersByFormatName("gif").next();
		    ImageInputStream ciis = ImageIO.createImageInputStream(gif);
		    reader.setInput(ciis, false);

		    int noi = reader.getNumImages(true);

		    master = new BufferedImage[noi];

		    for (int i = 0; i < noi; i++) {


		        BufferedImage image = reader.read(i);
		        IIOMetadata metadata = reader.getImageMetadata(i);

		        Node tree = metadata.getAsTree("javax_imageio_gif_image_1.0");

		        NodeList children = tree.getChildNodes();

		        for (int j = 0; j < children.getLength(); j++) {

		            Node nodeItem = children.item(j);

		            if(nodeItem.getNodeName().equals("ImageDescriptor")){

		                Map<String, Integer> imageAttr = new HashMap<String, Integer>();

		                for (int k = 0; k < imageatt.length; k++) {

		                    NamedNodeMap attr = nodeItem.getAttributes();

		                    Node attnode = attr.getNamedItem(imageatt[k]);

		                    imageAttr.put(imageatt[k], Integer.valueOf(attnode.getNodeValue()));

		                }

		                
		                master[i] = new BufferedImage(imageAttr.get("imageWidth"), imageAttr.get("imageHeight"), BufferedImage.TYPE_INT_ARGB);
		                
		                master[i].getGraphics().drawImage(image, imageAttr.get("imageLeftPosition"), imageAttr.get("imageTopPosition"), null);


		            }
		        }

		    }


		} catch (IOException e) {
		    e.printStackTrace();
		}

		return master;
	}
	

	
	
}
