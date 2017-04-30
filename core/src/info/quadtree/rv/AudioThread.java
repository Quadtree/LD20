package rv;

import java.applet.AudioClip;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

public class AudioThread implements Runnable{

	public ArrayBlockingQueue<String> audioNames;
	
	HashMap<String, AudioClip> audioFiles;
	
	public boolean keepRun = true;
	
	@Override
	public void run() {
		audioFiles = new HashMap<String, AudioClip>();
		audioNames = new ArrayBlockingQueue<String>(1024);
		while(keepRun)
		{
			while(!audioNames.isEmpty())
			{
				String name = audioNames.poll();
				
				if(!audioFiles.containsKey(name))
				{
					audioFiles.put(name, RobotVillage.s.getAudioClip(RobotVillage.s.getDocumentBase(), "./media/" + name + ".wav"));
				}
				audioFiles.get(name).play();
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
			}
		}
	}
	
}
