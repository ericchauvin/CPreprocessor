// Copyright Eric Chauvin 2019 - 2020.



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
    // String fileName = "\\jdk7hotspotmaster\\src\\share\\tools\\launcher\\java.c";
    // String fileName = "\\Eric\\CodeAnalysisCpp\\main.cpp";

    testFiles();
    // listHeaderFiles();
    }



  private void testFiles()
    {
    try
    {
    String mainDir = "C:\\jdk7hotspotmaster\\src\\share\\vm\\code\\";
    // String mainDir = "C:\\gccmaster\\gcc\\";
    String outDir = "C:\\PreprocessOut\\";

    // This list of files would be different for 
    // different projects.  Like if it's a Linux
    // or Windows project.

    String projectFileListName = 
           "\\Eric\\CodeAnalysisJava\\FileList.txt";

    String headerFileName = 
           "\\Eric\\CodeAnalysisJava\\HeaderFiles.txt";

    HeaderFileDictionary headerDictionary = new
                        HeaderFileDictionary( mApp );

    headerDictionary.readFile( headerFileName );
    headerDictionary.readFileList( projectFileListName );

    String[] fileArray = { "codeBlob.cpp",
                           "codeBlob.hpp",
                           "codeCache.cpp",
                           "codeCache.hpp",
/*
                           "compiledIC.cpp",
                           "compiledIC.hpp",
                           "compressedStream.cpp",
                           "compressedStream.hpp",
                           "debugInfo.cpp",
                           "debugInfo.hpp",
                           "debugInfoRec.cpp",
                           "debugInfoRec.hpp",
                           "dependencies.cpp",
                           "dependencies.hpp",
                           "exceptionHandlerTable.cpp",
                           "exceptionHandlerTable.hpp",
                           "icBuffer.cpp",
                           "icBuffer.hpp",
                           "jvmticmlr.h",
                           "location.cpp",
                           "location.hpp",
                           "nmethod.cpp",
                           "nmethod.hpp",
                           "oopRecorder.cpp",
                           "oopRecorder.hpp",
                           "pcDesc.cpp",
                           "pcDesc.hpp",
                           "relocInfo.cpp",
                           "relocInfo.hpp",
                           "scopeDesc.cpp",
                           "scopeDesc.hpp",
                           "stubs.cpp",
                           "stubs.hpp",
                           "vmreg.cpp",
                           "vmreg.hpp",
                           "vtableStubs.cpp",
*/
                           "vtableStubs.hpp" };


    int max = fileArray.length;
    for( int count = 0; count < max; count++ )
      {
      String fileName = mainDir + fileArray[count];
      String outFileName = outDir + fileArray[count];
      MacroDictionary macroDictionary = new
                           MacroDictionary( mApp );

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

      String test = Preprocessor.PreprocessFile(
                                    mApp,
                                    fileName,
                                    macroDictionary,
                                    headerDictionary );

      if( test.length() == 0 )
        {
        mApp.showStatusAsync( " " );
        mApp.showStatusAsync( "The file had an error." );
        mApp.showStatusAsync( fileName );
        headerDictionary.writeFile( headerFileName );
        return;
        }

      FileUtility.writeStringToFile( mApp,
                                     outFileName,
                                     test,
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



  private void listHeaderFiles()
    {
    // String dir = "\\jdk7hotspotmaster\\src";
    // String dir = "\\cygwin64\\usr\\include";
    // String dir = "\\cygwin64\\usr\\include\\sys";
    // String dir = "\\cygwin64\\usr\\include\\machine";
    String dir = "\\cygwin64\\usr\\include\\bits";



    // Add it to the semicolon delimited dictionary file.
    // endsWith( fileToFind )
    String fileToFind = ""; // this/that.h";
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
                       dir );

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
