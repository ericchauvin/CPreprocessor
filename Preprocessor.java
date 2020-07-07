// Copyright Eric Chauvin 2018 - 2020.




// This can't be runnable because it gets called
// recursively from PreProcessLines when it comes
// to an include statement.
public class Preprocessor
  {


  public static StrA PreprocessFile( MainApp mApp,
                             StrA fileName,
                             MacroDictionary
                                  macroDictionary,
                             HeaderFileDictionary
                                  headerDictionary,
                             CodeBlockDictionary
                                  codeBlockDictionary )
    {
    try
    {
    String showError = "";
    mApp.showStatusAsync( "Preprocessing file:\n" +
                                fileName.toString() );

    // The first level of lexical analysis and
    // processing is inside FileUtility.java when
    // it reads the file to a string.
    StrA result = FileUtility.readFileToStrA(
                                        mApp,
                                        fileName,
                                        false,
                                        false );

    if( result.trim().length() == 0 )
      {
      mApp.showStatusAsync( "Nothing in Source File." );
      // Return an empty string to stop further 
      // processing.
      return new StrA( "" );
      }

    // This adds line number markers and also fixes
    // line splices.
    result = RemoveComments.removeAllComments( mApp,
                                           result,
                                           fileName );

    if( result.length() == 0 )
      return new StrA( "" );

    if( !MarkupString.testMarkers( result, 
                   new StrA( "RemoveAllComments()" ),
                                  mApp ))
      {
      return new StrA( "" );
      }
    
    PreProcessLines procLines = new
                              PreProcessLines( mApp,
                              macroDictionary,
                              headerDictionary,
                              codeBlockDictionary );

    result = procLines.mainFileLoop( result, fileName );

    if( result.length() == 0 )
      return new StrA( "" );



    // result = MarkupString.MarkItUp( mApp,
       //                                 result );


    // mApp.showStatusAsync( result.toString() );
    // mApp.showStatusAsync( " " );
    // mApp.showStatusAsync( "Done preprocessing:" );
    // mApp.showStatus( fileName );
    // mApp.showStatus( " " );

    return result;

    }
    catch( Exception e )
      {
      mApp.showStatusAsync( "Exception in PreprocessFile()." );
      mApp.showStatusAsync( e.getMessage() );
      return new StrA( "" );
      }
    }




  }
