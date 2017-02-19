package rve;

import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import sgf.ImageSource;

public class ImageLoader implements ImageSource {

	@Override
	public Image getImageByName(String arg0) {
		try {
			return ImageIO.read(new FileInputStream("C:\\Users\\quadtree\\ludumdare\\RobotVillage\\media\\" + arg0 + ".png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
