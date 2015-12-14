import javax.rmi.CORBA.Util;

import voce.SpeechRecognizer;
import voce.SpeechSynthesizer;
import voce.Utils;

public class SpeechRecognition
{
	private static SpeechSynthesizer mSynthesizer = null;
	private static SpeechRecognizer mRecognizer = null;
	
	public static void main(String[] args)
	{		
		String vocePath = "C:/Users/Sean/Desktop/Java Bin/voce-0.9.1/lib";
		boolean initSynthesis = true;
		boolean initRecognition = true;
		String grammarPath = "";
		String grammarName = "";
		
		Utils.setPrintDebug(false);
		Utils.log("debug", "Beginning initialization");
		 
		 if (!initSynthesis && !initRecognition)
		 {
			 Utils.log("warning", "Synthesizer and recognizer are both" 
					 + "uninitialized");
		 }
		 
		 if(initSynthesis)
		 {
			 //Create a speech synthesizer
			 Utils.log("", "Initializing synthesizer");
			 mSynthesizer = new SpeechSynthesizer("JARVIS");
		 }
		 
		 if(initRecognition)
		 {
			 if(grammarPath.equals(""))
			 {
				 grammarPath = "./";
			 }
			 
			 //Always use the same config file/
			 String configFilename = "voce.config.xml";
			 
			 //Create the speech recognizer
			 Utils.log("",  "Initializing recognizer."
					 + " This may take some time...");
			 mRecognizer = new SpeechRecognizer(vocePath + "/" + configFilename, grammarPath, grammarName);
			 
			 //Enable the recognizer
			 setRecognizerEnabled(true);
		 }
		 
		 Utils.log("", "Initialization complete");
		 
		 
	}
	
	public static void destroy()
	{
         Utils.log("debug", "Shutting down...");
                 
          if (null != mSynthesizer)
         {
             mSynthesizer.destroy();
         }
 
         if (null != mRecognizer)
         {
             mRecognizer.destroy();
         }
 
         Utils.log("", "Shutdown complete");
	}
 
     public static void synthesize(String message)
     {
             if (null == mSynthesizer)
             {
                     Utils.log("warning", "synthesize called before " 
                         + "synthesizer was initialized.  Request will be ignored.");
                         return;
                 }
 
                 //Utils.log("debug", "SpeechInterface.speak: Adding message to speech queue: " + message);
             
             mSynthesizer.synthesize(message);
     }
 
     public static void stopSynthesizing()
     {
             if (null == mSynthesizer)
             {
                     Utils.log("warning", "stopSynthesizing called before " 
                     + "synthesizer was initialized.  Request will be ignored.");
                         return;
                 }
 
                 mSynthesizer.stopSynthesizing();
     }
 
     public static int getRecognizerQueueSize()
     {
             if (null == mRecognizer)
             {
                     Utils.log("warning", "getRecognizerQueueSize "
                 + "called before recognizer was initialized.  Returning " 
                 + "0.");
                         return 0;
                 }
 
                 return mRecognizer.getQueueSize();
     }
 
     public static String popRecognizedString()
     {
             if (null == mRecognizer)
             {
                     Utils.log("warning", "popRecognizedString "
                 + "called before recognizer was initialized.  Returning " 
                 + "an empty string.");
         return "";
                 }
 
                 return mRecognizer.popString();
     }
	 
	
	public static void setRecognizerEnabled(boolean e)
	{
	        if (null == mRecognizer)
	        {
	                Utils.log("warning", "setRecognizerEnabled "
	                        + "called before recognizer was initialized.  Request " 
	                        + "will be ignored.");
	                return;
	        }
	        
         mRecognizer.setEnabled(e);
         
 	}
	
	public static boolean isRecognizerEnabled()
	{
	         if (null == mRecognizer)
	         {
	                 Utils.log("warning", "isRecognizerEnabled "
	                         + "called before recognizer was initialized.  Returning " 
	                         + "false.");
	                         return false;
	                 }       
	 
	                 return mRecognizer.isEnabled();
    }
	
}