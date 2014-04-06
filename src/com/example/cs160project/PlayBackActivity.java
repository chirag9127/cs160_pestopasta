package com.example.cs160project;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class PlayBackActivity extends Activity{
	public TextView songName,startTimeField,endTimeField;
	private MediaPlayer mediaPlayer;
	private SeekBar seekbar;
	private ImageButton playButton,pauseButton;
	private double startTime = 0;
	private double finalTime = 0;
	public static int oneTimeOnly = 0;
	private Handler myHandler = new Handler();
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_playback);
          //playRecord();
          songName = (TextView)findViewById(R.id.textView4);
          startTimeField =(TextView)findViewById(R.id.textView1);
          endTimeField =(TextView)findViewById(R.id.textView2);
          seekbar = (SeekBar)findViewById(R.id.seekBar1);
          playButton = (ImageButton)findViewById(R.id.imageButton1);
          pauseButton = (ImageButton)findViewById(R.id.imageButton2);
          songName.setText("Test");
          mediaPlayer = new MediaPlayer();
          try {
			mediaPlayer.setDataSource("/sdcard/test.pcm");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
          seekbar.setClickable(false);
          pauseButton.setEnabled(false);
    }
	public void play(View view){
		   Toast.makeText(getApplicationContext(), "Playing sound", 
		   Toast.LENGTH_SHORT).show();
		      mediaPlayer.start();
		      finalTime = mediaPlayer.getDuration();
		      startTime = mediaPlayer.getCurrentPosition();
		      if(oneTimeOnly == 0){
		         seekbar.setMax((int) finalTime);
		         oneTimeOnly = 1;
		      } 

		      endTimeField.setText(String.format("%d min, %d sec", 
		         TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
		         TimeUnit.MILLISECONDS.toSeconds((long) finalTime) - 
		         TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
		         toMinutes((long) finalTime)))
		      );
		      startTimeField.setText(String.format("%d min, %d sec", 
		         TimeUnit.MILLISECONDS.toMinutes((long) startTime),
		         TimeUnit.MILLISECONDS.toSeconds((long) startTime) - 
		         TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
		         toMinutes((long) startTime)))
		      );
		      seekbar.setProgress((int)startTime);
		      myHandler.postDelayed(UpdateSongTime,100);
		      pauseButton.setEnabled(true);
		      playButton.setEnabled(false);
		   }

		   private Runnable UpdateSongTime = new Runnable() {
		      public void run() {
		         startTime = mediaPlayer.getCurrentPosition();
		         startTimeField.setText(String.format("%d min, %d sec", 
		            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
		            TimeUnit.MILLISECONDS.toSeconds((long) startTime) - 
		            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
		            toMinutes((long) startTime)))
		         );
		         seekbar.setProgress((int)startTime);
		         myHandler.postDelayed(this, 100);
		      }
		   };
		   public void pause(View view){
		      Toast.makeText(getApplicationContext(), "Pausing sound", 
		      Toast.LENGTH_SHORT).show();

		      mediaPlayer.pause();
		      pauseButton.setEnabled(false);
		      playButton.setEnabled(true);
		   }
	void playRecord(){
		  
		  File file = new File(Environment.getExternalStorageDirectory(), "test.pcm");
		  
		        int shortSizeInBytes = Short.SIZE/Byte.SIZE;
		  
		  int bufferSizeInBytes = (int)(file.length()/shortSizeInBytes);
		  short[] audioData = new short[bufferSizeInBytes];
		  
		  try {
		   InputStream inputStream = new FileInputStream(file);
		   BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
		   DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);
		   
		   int i = 0;
		   while(dataInputStream.available() > 0){
		    audioData[i] = dataInputStream.readShort();
		    i++;
		   }
		   
		   dataInputStream.close();
		   
		   AudioTrack audioTrack = new AudioTrack(
		     AudioManager.STREAM_MUSIC,
		     11025,
		     AudioFormat.CHANNEL_CONFIGURATION_MONO,
		     AudioFormat.ENCODING_PCM_16BIT,
		     bufferSizeInBytes,
		     AudioTrack.MODE_STREAM);
		   
		   audioTrack.play();
		   audioTrack.write(audioData, 0, bufferSizeInBytes);

		   
		  } catch (FileNotFoundException e) {
		   e.printStackTrace();
		  } catch (IOException e) {
		   e.printStackTrace();
		  }
		 }
}