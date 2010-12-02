package edu.asu;

import java.io.FileInputStream;
import java.io.IOException;

import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

public class Music implements Runnable
{
	
//		static AudioPlayer MGP = AudioPlayer.player;
		static AudioStream BGM;
		public static void music()
		{
			while(true){
//		AudioData MD;
			try{
				BGM = new AudioStream(new FileInputStream("BGMusic.wav"));
//			System.out.println(BGM.getLength());
//			AudioData MD = BGM.getData(); 
//			ContinuousAudioDataStream loop = new ContinuousAudioDataStream(MD);
//			AudioPlayer.player.start(loop);
				AudioPlayer.player.start(BGM);
			}catch(IOException e){
				e.printStackTrace();
			}
			try 	{
				Thread.sleep(BGM.getLength());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
//			e.printStackTrace();
			}
		}
//	5000
//	50000
//	500000
//	MGP.start(loop);
}
//	public static boolean AudioPlayer()
//	{
//		return AudioPlayer.player.isInterrupted();
//
//	}
	@SuppressWarnings("deprecation")
	public static void stop()
	{
		AudioPlayer.player.stop(BGM);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			music();
		}
	}

}
