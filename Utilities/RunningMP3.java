package Utilities;

import java.io.FileInputStream;
import java.io.InputStream;

import javazoom.jl.decoder.*;
import javazoom.jl.player.*;

public class RunningMP3 {
	
	private Bitstream bitstream;
	private Decoder decoder;
	private AudioDevice audio;
	
	private boolean complete,closed,paused;
	private int frame,lastPosition;
	
	public RunningMP3(InputStream stream) throws JavaLayerException {
		this(stream, null);	
	}
	
	public RunningMP3(InputStream stream, AudioDevice device) throws JavaLayerException {
		bitstream = new Bitstream(stream);		
		decoder = new Decoder();
				
		if (device!=null) {		
			audio = device;
		} else {			
			FactoryRegistry r = FactoryRegistry.systemRegistry();
			audio = r.createAudioDevice();
		}
		audio.open(decoder);
	}
	
	public void start() {
		start(Integer.MAX_VALUE);
	}
	
	protected synchronized void start(final int frameCount) {
		Runnable r= new Runnable(){
			public void run(){
				int frames=frameCount;
				boolean ret = true;
				paused=false;
				
				try {
					while (frames-- > 0 && ret)	{
						while(paused){
							try {
								Thread.sleep(10);
							} catch (Exception e){ }
						}
						ret = decodeFrame();			
					}
				} catch (JavaLayerException e){
					stop();
					e.printStackTrace();					
				}
		
				if (!ret){
					// last frame, ensure all data flushed to the audio device. 
					AudioDevice out = audio;
					if (out!=null){		
						out.flush();	
						synchronized (this)	{
							complete = (!closed);
							stop();
						}
					}
				}
			}
		};
		
		new Thread(r,"RunningMP3").start();
	}
	
	public synchronized void pause(){
		paused=true;
	}
	
	public synchronized void resume(){
		paused=false;
	}
	
	public synchronized void stop(){		
		AudioDevice out = audio;
		if (out!=null)	{ 
			closed = true;
			audio = null;	
			// this may fail, so ensure object state is set up before
			// calling this method. 
			out.close();
			lastPosition = out.getPosition();
			try	{
				bitstream.close();
			} catch (BitstreamException ex){ }
		}
	}
	
	public synchronized boolean isComplete(){
		return complete;	
	}
	
	public int getPosition() {
		int position = lastPosition;
		
		AudioDevice out = audio;		
		if (out!=null) {
			position = out.getPosition();	
		}
		return position;
	}
	
	protected boolean decodeFrame() throws JavaLayerException {		
		try	{
			AudioDevice out = audio;
			if (out==null)
				return false;

			Header h = bitstream.readFrame();	
			
			if (h==null)
				return false;
				
			// sample buffer set when decoder constructed
			SampleBuffer output = (SampleBuffer)decoder.decodeFrame(h, bitstream);
																																					
			synchronized (this)	{
				out = audio;
				if (out!=null) {					
					out.write(output.getBuffer(), 0, output.getBufferLength());
				}				
			}
																			
			bitstream.closeFrame();
		}		
		catch (RuntimeException ex)	{
			throw new JavaLayerException("Exception decoding audio frame", ex);
		}
		return true;
	}
	
	public static void main (String[] args) throws  Exception{
		FileInputStream fis=new FileInputStream("E:\\abc.mp3");
		RunningMP3 mp3=new RunningMP3(fis);
		System.out.println ("hi");
		mp3.start();
		System.out.println ("hi");
		Thread.sleep(7000);
		System.out.println ("pausing");
		mp3.pause();
		Thread.sleep(3000);
		System.out.println ("resuming");
		mp3.resume();
		Thread.sleep(10000);
		System.out.println ("stopping");
		mp3.stop();
	}
}