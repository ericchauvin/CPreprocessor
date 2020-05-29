// Copyright Eric Chauvin 2018 - 2020.



public class Preprocessor
  {


  public static String PreprocessFile( MainApp mApp,
                     String fileName,
                     MacroDictionary macroDictionary )
    {
    try
    {
    String showError = "";
    mApp.showStatusAsync( "Preprocessing file:\n" + fileName );

    // The first level of lexical analysis and
    // processing is inside FileUtility.java when
    // it reads the file to a string.
    String result = FileUtility.readFileToString(
                                        mApp,
                                        fileName,
                                        false );

    if( result.trim().length() == 0 )
      {
      mApp.showStatusAsync( "Nothing in Source File." );
      // Return an empty string to stop further 
      // processing.
      return "";
      }

    // This adds line number markers and also fixes
    // line splices.
    result = RemoveComments.removeAllComments( mApp,
                                           result,
                                           fileName );

    if( result.length() == 0 )
      return "";

    if( !MarkupString.testMarkers( result, 
                                  "RemoveAllComments()",
                                  mApp ))
      {
      return "";
      }
    
    PreProcessLines procLines = new
                              PreProcessLines( mApp,
                                  macroDictionary );

    result = procLines.mainFileLoop( result );

    if( result.length() == 0 )
      return "";



    // result = MarkupString.MarkItUp( mApp,
       //                                 result );


    // mApp.showStatusAsync( result );
    mApp.showStatusAsync( " " );
    // mApp.showStatusAsync( "Done preprocessing:" );
    // mApp.showStatus( fileName );
    // mApp.showStatus( " " );

    return result;

    }
    catch( Exception e )
      {
      mApp.showStatusAsync( "Exception in PreprocessFile()." );
      mApp.showStatusAsync( e.getMessage() );
      return "";
      }
    }




  }
