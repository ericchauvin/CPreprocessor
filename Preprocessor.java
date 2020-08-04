// Copyright Eric Chauvin 2018 - 2020.




// This gets called recursively from PreProcessLines
// when it comes to an include statement.

public class Preprocessor
  {


  public static StrA PreprocessFile( MainApp mApp,
                             StrA fileName,
                             MacroDictionary
                                  macroDictionary,
                             IncludeDictionary
                                  includeDnry,
                             CodeBlockDictionary
                                  codeBlockDictionary )
    {
    try
    {
    String showError = "";
    mApp.showStatusAsync( "Preprocessing file:\n" +
                                fileName.toString() );

    StrA result = FileUtility.readFileToStrA(
                                        mApp,
                                        fileName,
                                        false,
                                        false );

    if( result.length() == 0 )
      {
      mApp.showStatusAsync( "Nothing in Source File." );
      // Return an empty string to stop further 
      // processing.
      return StrA.Empty;
      }

    // This adds line number markers and also fixes
    // line splices.
    result = RemoveComments.removeAllComments( mApp,
                                           result,
                                           fileName );

    if( result.length() == 0 )
      return StrA.Empty;

    if( !MarkupString.testMarkers( result, 
                   new StrA( "RemoveAllComments()" ),
                                  mApp ))
      {
      return StrA.Empty;
      }
    
    PreProcessLines procLines = new
                              PreProcessLines( mApp,
                              macroDictionary,
                              includeDnry,
                              codeBlockDictionary );

    result = procLines.mainFileLoop( result, fileName );

    if( result.length() == 0 )
      return StrA.Empty;



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
      return StrA.Empty;
      }
    }




  }
