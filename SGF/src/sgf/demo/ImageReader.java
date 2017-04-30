package sgf.demo;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import sgf.ImageSource;

public class ImageReader implements ImageSource {

	@Override
	public Image getImageByName(String imageName) {
		try {
			return ImageIO.read(new File("./media/" + imageName + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
