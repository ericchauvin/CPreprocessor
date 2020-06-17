// Copyright Eric Chauvin 2020.



public class PreprocessProject
  {
  private MainApp mApp;
  private Thread fileThread;



  private PreprocessProject()
    {
    }



  public PreprocessProject( MainApp useApp )
    {
    mApp = useApp;
    }



  public void cancel()
    {
    if( fileThread != null )
      {
      mApp.showStatusAsync( "Canceling..." );
      fileThread.interrupt();
      fileThread = null;
      }
    }



  public void doProject()
    {
    testFiles();
    // listHeaderFiles();
    }



  private void testFiles()
    {
    try
    {
    // String fileName = "\\jdk7hotspotmaster\\src\\share\\tools\\launcher\\java.c";
    // String fileName = "\\Eric\\CodeAnalysisCpp\\main.cpp";

    // String mainDir = "C:\\jdk7hotspotmaster\\src\\share\\vm\\code\\";
    // String mainDir = "C:\\gccmaster\\gcc\\";
    String outDir = "C:\\PreprocessOut\\";

    // This list of files would be different for 
    // different projects.  Like if it's a Linux
    // or Windows project.

    String projectFileName = 
            "\\Eric\\CPreprocessor\\ProjectFiles.txt";

    String headerFileName = 
           "\\Eric\\CPreprocessor\\HeaderFiles.txt";

    HeaderFileDictionary headerDictionary = new
                        HeaderFileDictionary( mApp );

    headerDictionary.readFile( headerFileName );


    String fileStr = FileUtility.readFileToString(
                                      mApp,
                                      projectFileName,
                                      false );

    if( fileStr.length() == 0 )
      {
      mApp.showStatusAsync( "Nothing in the project file." );
      return;
      }


    // .rc extension
    // Resource files for Windows.  Like bitmaps,
    // menus, icons, etc.

    String[] fileArray = fileStr.split( "\n" );
    int max = fileArray.length;
    for( int count = 0; count < max; count++ )
      {
      // For testing:
      if( count > 2 )
        break;

      String fileName = fileArray[count];
      fileName = fileName.trim();
      if( fileName.length() < 1 )
        continue;

      if( fileName.startsWith( "//" ))
        continue;

      String outFileName = outDir + 
                          StringsUtility.getFileName(
                          fileArray[count], '\\' );

      mApp.showStatusAsync( "Out file: " + outFileName );
    
      MacroDictionary macroDictionary = new
                           MacroDictionary( mApp );

      addMacros( macroDictionary );

      String result = Preprocessor.PreprocessFile(
                                    mApp,
                                    fileName,
                                    macroDictionary,
                                    headerDictionary );

      if( result.length() == 0 )
        {
        mApp.showStatusAsync( " " );
        mApp.showStatusAsync( "The file had an error." );
        mApp.showStatusAsync( fileName );
        headerDictionary.writeFile( headerFileName );
        return;
        }

      FileUtility.writeStringToFile( mApp,
                                     outFileName,
                                     result,
                                     false );

      }

    headerDictionary.writeFile( headerFileName );

    mApp.showStatusAsync( " " );
    mApp.showStatusAsync( "Finished processing files." );
    mApp.showStatusAsync( " " );
    }
    catch( Exception e )
      {
      mApp.showStatusAsync( "Exception in testFiles()." );
      mApp.showStatusAsync( e.getMessage() );
      }
    }




  private void addMacros( MacroDictionary
                          macroDictionary )
    {
    // Make a configuration file for these.

    // Target Architechture.
    // "Zero-Assembler Project: Zero is a port of
    // OpenJDK that uses no assembler and therefore
    // can trivially be built on any system."

    // TARGET_ARCH_zero, TARGET_ARCH_arm,
    Macro macro = new Macro( mApp );
    macro.setMacroWithEmptyParams( "TARGET_ARCH_x86" );
    macroDictionary.setMacro( "TARGET_ARCH_x86",
                                macro );


    // Intel x86 is Little Endian.
    // __IEEE_BIG_ENDIAN
    macro = new Macro( mApp );
    macro.setMacroWithEmptyParams( "__IEEE_LITTLE_ENDIAN" );
    macroDictionary.setMacro( "__IEEE_LITTLE_ENDIAN",
                                macro );

    // I want to use the Cygwin compiler to compile
    // this after it is preprocessed here.
    macro = new Macro( mApp );
    macro.setMacroWithEmptyParams( "__CYGWIN__" );
    macroDictionary.setMacro( "__CYGWIN__",
                                macro );


    // Gamma is Hotspot internal test code.
    // Leave GAMMA undefined.

 
    // __cplusplus
     
    macro = new Macro( mApp );
    macro.setMacroWithEmptyParams( "__x86_64__" );
    macroDictionary.setMacro( "__x86_64__",
                                macro );

    macro = new Macro( mApp );
    macro.setMacroWithEmptyParams( "__STRICT_ANSI__" );
    macroDictionary.setMacro( "__STRICT_ANSI__",
                                macro );

    // JIT compilers.    
    // C1 compiler.  Bytecode compiler.
    // C2 compiler.  Called opto?  Higher
    // optimization.

    macro = new Macro( mApp );
    macro.setMacroWithEmptyParams( "COMPILER1" );
    macroDictionary.setMacro( "COMPILER1", macro );

    // .pch is Precompiled header.
    // or  .gch for Gnu.  Gnu compiled header.
    }



  private void listHeaderFiles()
    {
    // String dir = "\\jdk7hotspotmaster\\src";
    // String dir = "\\cygwin64\\usr\\include";
    // String dir = "\\cygwin64\\usr\\include\\sys";
    // String dir = "\\cygwin64\\usr\\include\\machine";
    // String dir = "\\cygwin64\\usr\\include\\bits";
    // String dir = "\\cygwin64\\usr\\include\\c++";
    // String dir = "\\cygwin64\\usr\\include\\cygwin";
    // String dir = "\\cygwin-master\\winsup\\cygwin";

    // String dir = "\\EricFiles\\WindowsSdk10\\ucrt";
    // String dir = "\\EricFiles\\WindowsSdk10\\um";
    String dir = "\\EricFiles\\WindowsSdk10\\shared";


    // Add it to the semicolon delimited dictionary file.
    // endsWith( fileToFind )
    String fileToFind = ""; // stddef.h";
    listFiles( dir, fileToFind );
    }



  private void listFiles( String dir,
                          String fileToFind )
    {
    try
    {
    if( fileThread != null )
      {
      // If it's not still doing something.
      if( !fileThread.isAlive())
        {
        fileThread = null;
        }
      else
        {
        mApp.showStatusAsync( "The thread is already running." );
        return;
        }
      }

// fileToFind
    FileSearchRunnable fileSearch = new 
                       FileSearchRunnable( mApp,
                       dir,
                       true,
                       "" );

    fileThread = new Thread( fileSearch );
    fileThread.start();
    }
    catch( Exception e )
      {
      mApp.showStatusAsync( "Exception in listFiles()." );
      mApp.showStatusAsync( e.getMessage() );
      }
    }



  }
