// Copyright Eric Chauvin 2018 - 2020.


// This means that I need to get the defined value
// from a previous macro in this macro.
// if SWITCHABLE_TARGET



  public class PreprocIfSection
  {
  private MainApp mApp;
  // private PreprocIfSection[] SectionArray = null;
  // private int SectionArrayLast = 0;
  
/*
  private string Text = "";
  private int Level = 0;
  private char TokenType = ' '; // Markers.TypeString
  // This Token can contain other Tokens.
*/


  private PreprocIfSection()
    {
    }



  public PreprocIfSection( MainApp useApp )
    {
    mApp = useApp;

    }


  public String markLevels( String in )
    {
    StringBuilder sBuilder = new StringBuilder();
    StringBuilder lineBuilder = new StringBuilder();

/*
    String startMarkers = Character.toString(
                           Markers.Begin ) +
                           Character.toString(
                           Markers.TypePreprocessor );

    String lineMarkers = Character.toString(
                           Markers.Begin ) +
                           Character.toString(
                           Markers.TypeLineNumber );
*/

    String[] splitS = in.split( Character.toString(
                                    Markers.Begin ) );

    int last = splitS.length;
    if( last == 0 )
      {
      mApp.showStatus( "There is nothing in the string for markLevels()." );
      return "";
      }

    // Before the first Begin marker.
    sBuilder.append( splitS[0] );

    int level = 0;
    // Count starts at one with the first Begin marker.
    for( int count = 1; count < last; count++ )
      {
      String line = splitS[count];
      if( !line.contains( Character.toString(
                          Markers.TypePreprocessor )))
        {
        sBuilder.append( Markers.Begin );
        sBuilder.append( splitS[count] );
        continue;
        }

      // Can I trim this macro line?
      line = line.trim();

      String[] splitLine = line.split( " " );
      int lastPart = splitLine.length;
      if( lastPart == 0 )
        {
        mApp.showStatus( "Preprocessor line doesn't have parts." );
        return "";
        }

      String command = splitLine[0]; 
      command = command.replace( Character.toString(
                    Markers.TypePreprocessor ), "" ); 

      boolean commandHasNoBody = command.contains(
                                  Character.toString(Markers.End ));

      if( commandHasNoBody )
        {
        command = command.replace( Character.toString(
                                  Markers.End ), "" ); 
        }

      command = command.toLowerCase();
      if( !isValidCommand( command ))
        {
        mApp.showStatus( command + " is not a valid command." );
        return "";
        }

      if( command.equals( "if" ))
        {
        level++;
        mApp.showStatus( command + ") " + level );
        }

      if( command.equals( "ifdef" ))
        {
        level++;
        mApp.showStatus( command + ") " + level );
        }

      if( command.equals( "ifndef" ))
        {
        level++;
        mApp.showStatus( command + ") " + level );
        }

      if( command.equals( "else" ))
        {
        // else ends a level but then it begins a new one.
        level--;
        level++;
        mApp.showStatus( command + ") " + level );
        }

      if( command.equals( "elif" ))
        {
        // elif is actually two statements.  An if
        // followed by and else.
  
        level--;
        // level++;
        level++;
        mApp.showStatus( command + ") " + level );
        }

      if( command.equals( "endif" ))
        {
        level--;
        mApp.showStatus( command + ") " + level );
        }

      if( level < 0 )
        {
        mApp.showStatus( "Level is less than zero." );
        return "";
        }


      String testLine = "";
      lineBuilder.setLength( 0 );

      if( commandHasNoBody )
        {
        testLine = Character.toString( 
                        Markers.TypePreprocessor ) +
                        command + Markers.End;
 
        }
      else
        {
        for( int count2 = 1; count2 < lastPart; count2++ )
          lineBuilder.append( splitLine[count2] + " " );
 
        String macroBody = lineBuilder.toString();
        macroBody = macroBody.trim();

        // mApp.showStatus( "Command: " + command ); 
        // mApp.showStatus( "Body: " + macroBody ); 
        // mApp.showStatus( " " ); 

        testLine = Character.toString( 
                        Markers.TypePreprocessor ) +
                        command + " " + macroBody;
        }

      mApp.showStatus( testLine ); 
      mApp.showStatus( " " ); 
      
      if( !line.equals( testLine ))
        {
        mApp.showStatus( "Command + line parts don't match up." ); 
        mApp.showStatus( testLine ); 
        mApp.showStatus( line ); 
        return "";
        }


/*
        if( command.contains( Character.toString(
                          Markers.TypeLineNumber )))
          {
          String[] splitCommand = splitS[count].
                                split( lineMarkers );

          command = splitCommand[0];
          }

        mApp.showStatus( "Command: " + command ); 
*/

      }

    if( level != 0 )
      {
      mApp.showStatus( "Level is not zero at end." );
      return "";
      }

    return sBuilder.toString();
    }



  private boolean isValidCommand( String in )
    {
    if( in.equals( "define" ))
      return true;

    if( in.equals( "undef" ))
      return true;

    // Add comments back in to the code to show
    // where a file was included and all that.
    if( in.equals( "include" ))
      return true;

    if( in.equals( "pragma" ))
      return true;

    if( in.equals( "if" ))
      return true;

    if( in.equals( "ifdef" ))
      return true;

    if( in.equals( "ifndef" ))
      return true;

    if( in.equals( "else" ))
      return true;

    if( in.equals( "elif" ))
      return true;

    if( in.equals( "endif" ))
      return true;

    return false;
    }



/*
  private void ShowStatus( string ToShow )
    {
    if( MForm != null )
      MForm.ShowStatus( ToShow );

    }


///////////
  internal void ClearArray()
    {
    TokenArray = null;
    TokenArrayLast = 0;
    }
//////////



  internal bool AppendTokenToArray( Token Tk )
    {
    try
    {
    if( TokenArray == null )
      TokenArray = new Token[256];

    if( TokenArrayLast >= TokenArray.Length )
      {
      Array.Resize( ref TokenArray, TokenArray.Length + 1024 );
      }

    TokenArray[TokenArrayLast] = Tk;
    TokenArrayLast++;
    return true;
    }
    catch( Exception Except )
      {
      MForm.ShowStatus( "Exception in Token.AppendTokenToArray(). " + Except.Message );
      return false;
      }
    }



  internal bool AddTokensFromString( string InString )
    {
    string[] SplitS = InString.Split( new Char[] { Markers.Begin } );

    // ShowStatus( "Split at zero: >" + SplitS[0] + "<" );
    // ShowStatus( " " );

    int Last = SplitS.Length;
    // Count starts at 1:
    for( int Count = 1; Count < Last; Count++ )
      {
      string Line = SplitS[Count];
      if( '{'
        level ++;

      // ShowStatus( " " );
      // ShowStatus( Line );
      Token Tk = MakeTokenFromString( line, level );
      if( Tk == null )
        return false;

      if( !AppendTokenToArray( Tk ))
        return false;

      }

    // ShowTokens();
    return true;
    }



  private void ShowTokens()
    {
    ShowStatus( " " );
    ShowStatus( " " );
    ShowStatus( "Showing Tokens." );

    for( int Count = 0; Count < TokenArrayLast; Count++ )
      {
      Token Tk = TokenArray[Count];
      ShowStatus( " " );
      if( Tk.TokenType == Markers.TypeLineNumber )
        ShowStatus( "Line Number: " );

      ShowStatus( "Text: " + Tk.Text );
      ShowStatus( "Level: " + Tk.Level.ToString() );
      }
    }




  private Token MakeTokenFromString( String in,
                                     int level )
    {
    try
    {
    Token Tk = new Token( MForm );

    string LevelStr = "";
    int Position = 0;
    int Last = InString.Length;
    for( int Count = 0; Count < Last; Count++ )
      {
      char TestChar = InString[Count];
      if( Markers.IsMarker( TestChar ))
        {
        Tk.TokenType = TestChar;
        Position = Count + 1;
        break;
        }

      LevelStr += Char.ToString( TestChar );
      }

    // ShowStatus( "LevelStr: " + LevelStr );
    Tk.Level = Int32.Parse( LevelStr );
    // ShowStatus( "Level: " + Tk.Level.ToString());

    for( int Count = Position; Count < Last; Count++ )
      {
      char TestChar = InString[Count];
      if( Markers.IsMarker( TestChar ))
        break;

      Tk.Text += Char.ToString( TestChar );
      }

    // ShowStatus( "Text: " + Tk.Text );
    return Tk;
    }
    catch( Exception Except )
      {
      ShowStatus( "Exception in Token.MakeTokenFromString():" );
      ShowStatus( Except.Message );
      return null;
      }
    }




  private int GetLowestLevel()
    {
    int Lowest = 2000000000;
    for( int Count = 0; Count < TokenArrayLast; Count++ )
      {
      Token Tk = TokenArray[Count];
      if( Lowest > Tk.Level )
        Lowest = Tk.Level;

      // if( Tk.TokenType == Markers.TypeLineNumber )
      }

    return Lowest;
    }



  internal bool SetLowestTokenBlocks()
    {
    if( TokenArray == null )
      return true; // Nothing to do.

    Token[] TempTokenArray = new Token[TokenArrayLast];
    int TempTokenArrayLast = 0;

    int Lowest = GetLowestLevel();
    // ShowStatus( " " );
    // ShowStatus( "Lowest: " + Lowest.ToString());

    Token BlockTk = null;
    for( int Count = 0; Count < TokenArrayLast; Count++ )
      {
      Token Tk = TokenArray[Count];
      if( Lowest == Tk.Level )
        {
        if( BlockTk != null )
          {
          TempTokenArray[TempTokenArrayLast] = BlockTk;
          TempTokenArrayLast++;
          // Do this recursively.
          if( !BlockTk.SetLowestTokenBlocks())
            return false;

          BlockTk = null;
          }

        TempTokenArray[TempTokenArrayLast] = Tk;
        TempTokenArrayLast++;
        }
      else
        {
        if( BlockTk == null )
          {
          BlockTk = new Token( MForm );
          BlockTk.Text = "Block";
          }

        if( !BlockTk.AppendTokenToArray( Tk ))
          return false;

        }
      }

    if( BlockTk != null )
      {
      ShowStatus( "BlockTK should be null here." );
      return false;
      }

    // ShowStatus( "End of this one." );

    TokenArray = TempTokenArray;
    TokenArrayLast = TempTokenArrayLast;
    return true;
    }




  internal void ShowTokensAtLevel( int ShowLevel )
    {
    if( TokenArray == null )
      {
      if( TokenType == Markers.TypeLineNumber )
        return;

      if( Level == ShowLevel )
        ShowStatus( Text );

      return;
      }


    ShowStatus( "Block" );

    for( int Count = 0; Count < TokenArrayLast; Count++ )
      {
      Token Tk = TokenArray[Count];
      Tk.ShowTokensAtLevel( ShowLevel );
      }
    }

*/

  }
