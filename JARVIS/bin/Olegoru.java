/*
 * Copyright 1999-2004 Carnegie Mellon University.
 * Portions Copyright 2004 Sun Microsystems, Inc.
 * Portions Copyright 2004 Mitsubishi Electric Research Laboratories.
 * All Rights Reserved.  Use is subject to license terms.
 *
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 *
 */

package edu.cmu.sphinx.app.olegoru;


import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.Context;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.frontend.util.AudioFileDataSource;
//import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.ConfidenceResult;
import edu.cmu.sphinx.result.ConfidenceScorer;
import edu.cmu.sphinx.result.MAPConfidenceScorer;
import edu.cmu.sphinx.result.Path;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.result.WordResult;
import edu.cmu.sphinx.util.props.ConfigurationManager;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.sound.sampled.AudioFileFormat;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.lang.*;
import java.net.URL;
import java.text.DecimalFormat;
import javaFlacEncoder.FLACFileWriter;
import javaFlacEncoder.FLAC_FileEncoder;

import com.darkprograms.speech.microphone.Microphone;
//import com.darkprograms.speech.recognizer.Recognizer; //This will collide with s4's Recognizer 
import com.darkprograms.speech.recognizer.GoogleResponse;


/**
 * A simple HelloWorld demo showing a simple speech application built using Sphinx-4. This application uses the Sphinx-4
 * endpointer, which automatically segments incoming audio into utterances and silences.
 */
public class Olegoru{

    static JLabel label;

    private static final String ACOUSTIC_MODEL =
            "file:/Users/jeff/Develop/olegoru/SpeechRecognizer/edu/cmu/sphinx/app/olegoru/etc/en-us";
    private static final String DICTIONARY_PATH =
            "resource:/edu/cmu/sphinx/app/olegoru/etc/cmudict.0.7a";
    private static final String GRAMMAR_PATH =
            "resource:/edu/cmu/sphinx/app/olegoru/";
//    private static final String LANGUAGE_MODEL =
//            "resource:/edu/cmu/sphinx/demo/dialog/weather.lm";
    
    private static final String FILE_WAV_PATH_44100 = "/Users/jeff/Develop/olegoru/SpeechRecognizer/olegoru.wav";
    private static final String FILE_WAV_PATH_44100_TRIM = "/Users/jeff/Develop/olegoru/SpeechRecognizer/olegoru_trim.wav";
    private static final String FILE_WAV_PATH_16000 = "/Users/jeff/Develop/olegoru/SpeechRecognizer/olegoru_16000.wav";
    private static final String FILE_WAV_PATH_16000_TRIM = "/Users/jeff/Develop/olegoru/SpeechRecognizer/olegoru_16000_trim.wav";
    private static final String FILE_FLAC_PATH = "/Users/jeff/Develop/olegoru/SpeechRecognizer/olegoru.flac";    
    
    public static void main(String[] args) throws Exception {
        
    	MyKeyListener mKL = new MyKeyListener("Key Listener Tester");
    	
    	JedisPool pool = new JedisPool(new JedisPoolConfig(), "dhcp3-234.si.umich.edu");
    	Jedis jedis = pool.getResource();
    	
//    	jedis.slaveof("dhcp3-235.si.umich.edu", 6379);
    	
    	URL configURL;
    	
    	configURL = new File("/Users/jeff/Develop/olegoru/SpeechRecognizer/edu/cmu/sphinx/app/olegoru/config.xml").toURI().toURL();
    	
        ConfigurationManager cm = new ConfigurationManager(configURL);
        
        
        MyMicrophone microphone;
//        MyMicrophone microphone = new MyMicrophone(AudioFileFormat.Type.WAVE);
        
        
        // Clean up all the files
        File f1 = new File(FILE_WAV_PATH_44100);
        f1.delete();
      
        f1 = new File(FILE_WAV_PATH_16000);
        f1.delete();
      
        f1 = new File(FILE_WAV_PATH_16000_TRIM);
      	f1.delete();
      	
      	f1 = new File(FILE_WAV_PATH_44100_TRIM);
      	f1.delete();
      	
      	f1 = new File(FILE_FLAC_PATH);
      	f1.delete();
        
        File file_wav = new File(FILE_WAV_PATH_44100); 
      	File file_flac = new File(FILE_FLAC_PATH);
      	File file_wav_trim;
      	
        Recognizer recognizer = (Recognizer) cm.lookup("recognizer");
        
        
        
    	recognizer.allocate();
        
        System.out.println("Say: I want to hear the sound of X");

        // loop the recognition until the programm exits.
        while (true) {

        	
        	
        	Result result = null;
            if( mKL.isbKeyPressed() ){
            	microphone = new MyMicrophone(AudioFileFormat.Type.WAVE);
            	
            	try {
        	        microphone.captureAudioToFile(file_wav);
        	    } catch (Exception ex) {//Microphone not available or some other error.
        	        System.out.println("ERROR: Microphone is not availible.");
        	        ex.printStackTrace();
        	        //TODO Add your error Handling Here
        	    }
            	
            	
            	microphone.startRecording();
            	System.out.println("Start speaking. Press Ctrl-C to quit.\n");
            	
            	 
            	// Record 4 secs 
            	Thread.sleep(4000);
            	System.out.println("Stop.\n");
//            	microphone.stopRecording();
            	microphone.close();
            	
            	// Downsampling and trim the audio files
            	Runtime rt = Runtime.getRuntime();
//            	Process pr3 = rt.exec("/usr/local/bin/sox "+ FILE_WAV_PATH_44100 +" "+ FILE_WAV_PATH_44100_TRIM +" trim 4");
//                Thread.sleep(200);
            	Process pr = rt.exec("/usr/local/bin/sox " + FILE_WAV_PATH_44100 + " -r 16000 " + FILE_WAV_PATH_16000);
            	Thread.sleep(500);
//                 Process pr2 = rt.exec("/usr/local/bin/sox "+ FILE_WAV_PATH_16000 +" "+ FILE_WAV_PATH_16000_TRIM+" trim 5");
//                Process pr3 = rt.exec("/usr/local/bin/sox "+ FILE_WAV_PATH_44100 +" "+ FILE_WAV_PATH_44100+" trim 5");
                
            	
                URL audioURL = new File(FILE_WAV_PATH_16000).toURI().toURL();
                 
                // Remember to check config file for audio file data source
                AudioFileDataSource dataSource = (AudioFileDataSource) cm.lookup("dataSource");
                dataSource.setAudioFile(audioURL,  null);
                 
               
                result = recognizer.recognize();	    
            	
            } else {
//            	System.out.print(".");
            	
            	result = null;
            	// olegoru.wav should be generated at this point
            	// remember to delete it later on
            }
            
           
            
            if (result != null) {
//            	SpeechResult spResult = new SpeechResult(result);
//            	String resultText = spResult.getHypothesis();
                // Convert to FLAC
           	    file_wav_trim = new File(FILE_WAV_PATH_44100);
           	    
           	    FLAC_FileEncoder ffe = new FLAC_FileEncoder();
           	    ffe.encode(file_wav_trim, file_flac);
           	    
           	    TranscriptionThread transcriptionThread = new TranscriptionThread("en");
           	    
           	    String googleResultText = transcriptionThread.transcribe(FILE_FLAC_PATH);
           	    
           	    float conf = 0;
           	    if(googleResultText != null){
           	    	conf = transcriptionThread.getConfidence();
           	    }
           	    
           	    System.out.println("Google Transcribe: " + googleResultText + " conf=" + conf);
           	    
            	
            	String resultText = result.getBestFinalResultNoFiller();
            	
                System.out.println("You said: " + resultText + "\n");
                try {
                	
                	JSONObject json = new JSONObject();
                	json.put("local", resultText );
                	json.put("google", googleResultText);
                	json.put("google-conf", conf);
                	
                	String publishedStr = json.toString(); 
                	
                	System.out.println("publishedStr: " + publishedStr);
                	
                	if (resultText != null || googleResultText != null )
                		jedis.publish("SPEECH_CH", publishedStr);
                	
                } catch (JedisConnectionException e) {
                	if (jedis != null){
                		pool.returnBrokenResource(jedis);
                		jedis = null;
                	}
                	
                } finally {
                	if (jedis != null){
                		pool.returnResource(jedis);
                		pool.destroy();
                	}
                }
                
                // Clean up all the files
                f1 = new File(FILE_WAV_PATH_44100);
                f1.delete();
              
                f1 = new File(FILE_WAV_PATH_16000);
                f1.delete();
              
                f1 = new File(FILE_WAV_PATH_16000_TRIM);
              	f1.delete();
              	
              	f1 = new File(FILE_WAV_PATH_44100_TRIM);
              	f1.delete();
              	
              	f1 = new File(FILE_FLAC_PATH);
              	f1.delete();
              	
                
            } else {
//                System.out.println("I can't hear what you said.\n");
            }
            
            Thread.sleep(50);
            
        }
        
        
                
    }
    

    
    
}
