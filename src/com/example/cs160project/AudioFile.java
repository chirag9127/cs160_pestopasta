package com.example.cs160project;

import java.io.File;

public class AudioFile {

	public String title;
	public boolean ispublictag = true;
	public File myfile;
	
	public AudioFile(String t, boolean flag, File fl) {
		title = t;
		ispublictag = flag;
		myfile = fl;
	}
	

}
