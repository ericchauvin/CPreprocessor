// Copyright Eric Chauvin 2018 - 2020.



public class Preprocessor
  {

  public static String PreprocessFile( MainApp mApp,
                                     String fileName )

    {
    String showError = "";
    mApp.showStatus( "Preprocessing file:\n" + fileName );

    // The first level of lexical analysis and
    // processing is inside FileUtility.java when
    // it reads the file to a string.
    String result = FileUtility.readFileToString(
                                        mApp,
                                        fileName,
                                        false );

    if( result.trim().length() == 0 )
      {
      mApp.showStatus( "Nothing in Source File." );
      // Return an empty string to stop further 
      // processing.
      return "";
      }

    // This adds line number markers and also fixes
    // line splices.
    result = RemoveComments.removeAllComments(
                                  mApp, result );

    if( result.contains( Character.toString(
                      Markers.ErrorPoint )))
      {
      mApp.showStatus( " " );
      showError = "There was an error marker after" +
                               " RemoveAllComments.";

      mApp.showStatus( showError );
      return "";
      }

    if( !TestMarkers.testBeginEnd( mApp, result ))
      {
      mApp.showStatus( " " );
      showError = "TestBeginEnd returned false" +
                        " after RemoveAllComments.";
      mApp.showStatus( showError );
      return "";
      }

    result = MarkupForPreproc.markPreprocessorLines(
                                        mApp, result );

    if( result.contains( Character.toString(
                      Markers.ErrorPoint )))
      {
      mApp.showStatus( " " );
      showError = "There was an error marker after" +
                               " markPreprocessorLines.";

      mApp.showStatus( showError );
      return "";
      }

    if( !TestMarkers.testBeginEnd( mApp, result ))
      {
      mApp.showStatus( " " );
      showError = "TestBeginEnd returned false" +
                       " after markPreprocessorLine.";

      mApp.showStatus( showError );
      return "";
      }

    result = MarkupForPreproc.MarkItUp( mApp,
                                        result );

    mApp.showStatus( result );
    mApp.showStatus( " " );
    mApp.showStatus( "Done preprocessing:" );
    mApp.showStatus( fileName );
    mApp.showStatus( " " );

    return result;
    }




  }
