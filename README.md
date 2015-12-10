# JARVIS-BUTLER
A speech recognition program designed to respond, give news updates, and enact simple commands (such as: "open Netflix, turn on music, etc...")

  1. Simple program runs with **Voce free Speech recognition** API (http://voce.sourceforge.net/)
  2. Listens for audio
  3. Analyzes audio /end thread


**Editing this code:**
  1. All speech analysis should be done inside JARVIS speech initializer
  2. Any actions performed due to a command need their own class file (that way people can easily implement/edit actions)
  3. Read all the Voce read me files before inputting your own code/comments
  
**Important features of Voce code:**
  1. static void|||| 	init (String vocePath, boolean initSynthesis, boolean initRecognition, String grammarPath, String grammarName)
  2. static void|||| 	destroy ()
  3. static void|||| 	synthesize (String message)
  4. static void|||| 	stopSynthesizing ()
  5. static int |||| 	getRecognizerQueueSize ()
  6. static String|| 	popRecognizedString ()
  7. static void|||| 	setRecognizerEnabled (boolean e)
  8. static boolean| 	isRecognizerEnabled ()

****NOTES****
I set grammarPath and grammarName to "" because Voce automatically chooses the default grammar packs. I do not have a vocePath set as it is not necessary quite yet. It will likely be necessary once I have an active event_listener setup.

If you want to setup an executable file read this: http://www.excelsior-usa.com/articles/java-to-exe.html

I think the .bat will suffice for my needs. I want to run this on a Raspberry Pi 2 so, I just need it to execute once and it'll run in the background constantly
