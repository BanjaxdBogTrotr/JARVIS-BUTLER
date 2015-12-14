import javax.rmi.CORBA.Util;

import voce.SpeechRecognizer;
import voce.SpeechSynthesizer;
import voce.Utils;

public class SpeechRecognition {
  private static SpeechSynthesizer mSynthesizer = null;
  private static SpeechRecognizer mRecognizer = null;

  public static void main(String[] args) {		
    String vocePath = "./lib/";
	boolean initSynthesis = true;
	boolean initRecognition = true;
	String grammarPath = "./lib/grammar/";
	String grammarName = "fruit";
		
	Utils.setPrintDebug(false);
	Utils.log("debug", "Beginning initialization");
		 
	if (!initSynthesis && !initRecognition) {
	  Utils.log("warning", "Synthesizer and recognizer are both" 
			  + "uninitialized");
	}
		 
    if(initSynthesis) {
	  //Create a speech synthesizer
	  Utils.log("", "Initializing synthesizer");
	  mSynthesizer = new SpeechSynthesizer("JARVIS");
	}
		 
    if(initRecognition) {
      if(grammarPath.equals("")) {
	    grammarPath = "./";
	  }
	  //Always use the same config file/
	  String configFilename = "voce.config.xml";

	  //Create the speech recognizer
	  Utils.log("",  "Initializing recognizer."
				   + " This may take some time...");
	  mRecognizer = new SpeechRecognizer(
        vocePath + "/" + configFilename,
        grammarPath,
        grammarName
      );

	  //Enable the recognizer
	  setRecognizerEnabled(true);
	  
    }
	
	Utils.log("", "Initialization complete");
	mSynthesizer.synthesize("Hullo suh! What may I do for you today?");
	
	try {
	  Thread.sleep(500);
	} catch (InterruptedException e) {}
	
	/* TODO: Filler code until we figure out what we actually want to do here.
	 * Implementation is at least basic
	 */
	boolean quit = false;
	while (!quit) {
      try {
	    Thread.sleep(200);
	  } catch (InterruptedException e) {
	  }

	  while (mRecognizer.getQueueSize() > 0) {
	    String s = mRecognizer.popString();

		// Check if the string contains 'quit'.
		if (s.indexOf("quit") != -1) {
		  quit = true;
		}
        
        String particle = "a";
        if ("aeiou".indexOf(Character.toLowerCase(s.charAt(0))) >= 0) {
          particle += "n";
        }
        String synthSentence = quit
          ? "Of course suh ... I'll be going now."
          : "I do believe you said you want to eat " + particle + " " + s;
        System.out.println("You said: " + particle + " " + s);
	    mSynthesizer.synthesize(synthSentence);
      }
    }
	mSynthesizer.synthesize("Good day, Mastuh Wayne");
  }
	
  public static void destroy() {
    Utils.log("debug", "Shutting down...");
    if (mSynthesizer != null) {
      mSynthesizer.destroy();
    }
 
    if (mRecognizer != null) {
      mRecognizer.destroy();
    }
 
    Utils.log("", "Shutdown complete");
  }
 
  public static void synthesize(String message) {
    if (mSynthesizer == null) {
      Utils.log("warning", "synthesize called before " 
                + "synthesizer was initialized.  Request will be ignored.");
      return;
    }
 
   mSynthesizer.synthesize(message);
  }
 
  public static void stopSynthesizing() {
    if (mSynthesizer == null) {
      Utils.log("warning", "stopSynthesizing called before " 
              + "synthesizer was initialized.  Request will be ignored.");
      return;
    }
 
    mSynthesizer.stopSynthesizing();
 }
 
  public static int getRecognizerQueueSize()  {
    if (mRecognizer == null)  {
      Utils.log("warning", "getRecognizerQueueSize "
              + "called before recognizer was initialized.  Returning " 
              + "0.");
      return 0;
    }
 
    return mRecognizer.getQueueSize();
  }
 
  public static String popRecognizedString() {
    if (mRecognizer == null) {
      Utils.log("warning", "popRecognizedString "
		      + "called before recognizer was initialized.  Returning " 
              + "an empty string.");
      return "";
     }
 
    return mRecognizer.popString();
  }
	 
	
  public static void setRecognizerEnabled(boolean e) {
    if (mRecognizer == null)  {
      Utils.log("warning", "setRecognizerEnabled "
                  + "called before recognizer was initialized.  Request " 
                  + "will be ignored.");
      return;
    }    
    mRecognizer.setEnabled(e);
  }
	
  public static boolean isRecognizerEnabled() {
	if (mRecognizer == null)  {
	  Utils.log("warning", "isRecognizerEnabled "
	             + "called before recognizer was initialized.  Returning " 
	             + "false.");
	             return false;
	}
	return mRecognizer.isEnabled();
  }
}