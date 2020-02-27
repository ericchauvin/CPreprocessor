// Copyright Eric Chauvin 2018 - 2020.



  public class TestMarkers
  {


  public static boolean testBeginEnd( MainApp mApp,
                                      String inString )
    {
    StringBuilder sBuilder = new StringBuilder();

    String[] splitS = inString.split(
                  Character.toString( Markers.Begin ));


    // This contains the string before the first
    // marker.
    sBuilder.append( splitS[0] + "\n" );

    int last = splitS.length;
    // This starts at 1, after the first
    // Markers.Begin.
    for( int count = 1; count < last; count++ )
      {
      String line = splitS[count];
      sBuilder.append( line + "\n" );

      if( !line.contains( Character.toString(
                                       Markers.End )))
        {
        mApp.showStatus( sBuilder.toString() );
        mApp.showStatus( " " );
        mApp.showStatus( "The line has no end marker." );
        mApp.showStatus( "Line: " + line );
        return false;
        }
      }

    return true;
    }




  public static String removeOutsideWhiteSpace(
                                            String in )
    {
    StringBuilder sBuilder = new StringBuilder();

    boolean isInsideObject = false;
    int last = in.length();
    for( int count = 0; count < last; count++ )
      {
      char testChar = in.charAt( count );

      if( testChar == Markers.Begin )
        {
        isInsideObject = true;
        sBuilder.append( testChar );
        continue;
        }

      if( testChar == Markers.End )
        {
        isInsideObject = false;
        sBuilder.append( testChar );
        continue;
        }

      if( isInsideObject )
        {
        sBuilder.append( testChar );
        continue;
        }

      if( testChar == ' ' )
        continue;

      if( testChar == '\n' )
        continue;

      sBuilder.append( "Right here >" + testChar + "<" );

      sBuilder.append( testChar );
      sBuilder.append( Markers.ErrorPoint );
      return sBuilder.toString();
      }

    return sBuilder.toString();
    }



/*
  internal static bool TestBrackets( MainForm MForm, string InString )
    {
    StringBuilder SBuilder = new StringBuilder();
    int BracketCount = 0;
    bool IsInsideObject = false;
    int Last = InString.Length;
    for( int Count = 0; Count < Last; Count++ )
      {
      char TestChar = InString[Count];
      SBuilder.Append( Char.ToString( TestChar ));

      if( IsInsideObject )
        {
        if( TestChar == Markers.End )
          IsInsideObject = false;

        continue;
        }

      if( TestChar == Markers.Begin )
        {
        IsInsideObject = true;
        continue;
        }

      if( !((TestChar == '{') || (TestChar == '}')))
        {
        string ShowS = SBuilder.ToString();
        ShowS = ShowS.Replace(
             Char.ToString( Markers.Begin ), "\r\n" );

        MForm.ShowStatus( ShowS );
        MForm.ShowStatus( "This is not a bracket: " + Char.ToString( TestChar ));
        return false;
        }

      if( TestChar == '{' )
        BracketCount++;

      if( TestChar == '}' )
        BracketCount--;

      if( BracketCount < 0 )
        {
        string ShowS = SBuilder.ToString();
        ShowS = ShowS.Replace(
             Char.ToString( Markers.Begin ), "\r\n" );

        MForm.ShowStatus( ShowS );
        MForm.ShowStatus( "Bracket count went negative." );
        return false;
        }

      }

    if( BracketCount != 0 )
      {
      MForm.ShowStatus( "Bracket count is not zero at the end." );
      return false;
      }

    return true;
    }
*/


  }
