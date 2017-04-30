package sgf.demo;

import java.util.concurrent.BrokenBarrierException;

import javax.swing.JFrame;

import sgf.SGF;

public class SGFDemo {

	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws BrokenBarrierException 
	 */
	public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
		JFrame mainFrame = new JFrame();
		mainFrame.setBounds(0, 0, 1000, 1000);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainFrame.setVisible(true);
		
		SGF.getInstance().start(mainFrame, new ImageReader(), new SGFDemoGame());
	}

}
