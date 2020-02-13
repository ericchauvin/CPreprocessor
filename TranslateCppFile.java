// Copyright Eric Chauvin 2018 2020.



  public class TranslateCppFile
  {

/*
  private MainApp mApp;


  private TranslateCSharpFile()
    {
    }



  internal TranslateCSharpFile( MainForm UseForm )
    {
    MForm = UseForm;
    }

*/

  // public static Token GetTokensFromFile( String fileName )
  public static void GetTokensFromFile( MainApp mApp,
                                    String fileName )

    {
    // The first level of lexical analysis and
    // processing is inside FileUtility.java when
    // it reads the file to a string.
    String result = FileUtility.readFileToString(
                                        mApp,
                                        fileName,
                                        false );

    if( result.length() == 0 )
      {
      mApp.showStatus( "Nothing in Source File." );
      return; // null;
      }

    mApp.showStatus( result );
/*
    result = RemoveStarComments.removeAllComments(
                                  mApp, result );



    if( result.contains( Character.toString(
                      Markers.ErrorPoint )))
      {
      mApp.showStatus( " " );
      mApp.showStatus( "There was an error marker after RemoveStarComments." );
      return; //  null;
      }

    // if( !MForm.CheckEvents())
      // return null;


    if( !TestMarkers.TestBeginEnd( MForm, Result ))
      {
      ShowStatus( " " );
      ShowStatus( "TestBeginEnd returned false after RStComments." );
      return null;
      }


    Result = RemoveSlashComments.RemoveAllDoubleSlashComments( MForm, Result );
    if( !MForm.CheckEvents())
      return; //  null;

    if( !TestMarkers.TestBeginEnd( MForm, Result ))
      {
      ShowStatus( " " );
      ShowStatus( "TestBeginEnd returned false after RemoveSlashComments." );
      return null;
      }

    Result = CSharpToStrings.MakeStringObjects( MForm, Result );
    if( !MForm.CheckEvents())
      return null;

    if( Result.Contains( Char.ToString(
                      Markers.ErrorPoint )))
      {
      ShowStatus( " " );
      ShowStatus( "There was an error after CSharpToStrings." );
      ShowStatus( " " );
      ShowStatus( Result );
      return null;
      }

    if( !TestMarkers.TestBeginEnd( MForm, Result ))
      {
      ShowStatus( " " );
      ShowStatus( "TestBeginEnd returned false after CSToStrings." );
      return null;
      }

    Result = CSharpToCharacters.MakeCharacterObjects( Result );
    if( !MForm.CheckEvents())
      return null;

    if( Result.Contains( Char.ToString(
                      Markers.ErrorPoint )))
      {
      ShowStatus( " " );
      ShowStatus( "There was an error marker after CSToCharacters." );
      return null;
      }

    if( !TestMarkers.TestBeginEnd( MForm, Result ))
      {
      ShowStatus( " " );
      ShowStatus( "TestBeginEnd returned false after CSToCharacters." );
      return null;
      }

    Result = CSharpToIdentifiers.MakeIdentifierObjects( MForm, Result );
    if( !MForm.CheckEvents())
      return null;

    if( !TestMarkers.TestBeginEnd( MForm, Result ))
      {
      ShowStatus( " " );
      ShowStatus( "TestBeginEnd returned false after CSToIdentifiers." );
      return null;
      }

    Result = CSharpToNumbers.MakeNumberObjects( Result );
    if( !MForm.CheckEvents())
      return null;

    if( !TestMarkers.TestBeginEnd( MForm, Result ))
      {
      ShowStatus( " " );
      ShowStatus( "TestBeginEnd returned false after CSToNumbers." );
      return null;
      }

    Result = CSharpToOperators.MakeOperatorObjects( Result );
    if( !MForm.CheckEvents())
      return null;

    if( !TestMarkers.TestBeginEnd( MForm, Result ))
      {
      ShowStatus( " " );
      ShowStatus( "TestBeginEnd returned false after CSToOperators." );
      return null;
      }

    Result = TestMarkers.RemoveOutsideWhiteSpace( Result );

    if( !TestMarkers.TestBrackets( MForm, Result ))
      {
      // ShowStatus( Result );
      return null;
      }

    IDDictionary IdentDictionary = new
                               IDDictionary( MForm );

    // CSharpFixIdentifiers CSFixIDs = new
    //                    CSharpFixIdentifiers( MForm,
    //                                IdentDictionary );

    // if( !CSFixIDs.GetIdentifiers( Result ))
      // {
      // ShowStatus( "GetIdentifiers returned false." );
      // return "";
      // }

    // Result = CSFixIDs.MakeIdentifiersLowerCase( Result );

    // IdentDictionary.ShowIDs();

    // CSFixIDs = null;
    if( !MForm.CheckEvents())
      return null;


    Result = BracketLevel.SetLevelChars( MForm, Result );
    if( Result == "" )
      {
      // ShowStatus( Result );
      return null;
      }


    Token Tk = new Token( MForm );
    Tk.AddTokensFromString( Result );
    Tk.SetLowestTokenBlocks();

    ShowStatus( " " );
    Tk.ShowTokensAtLevel( 1 );

    return Tk;
    */
    }



  }
