// Copyright Eric Chauvin 2020.



// Stringification:
// https://gcc.gnu.org/onlinedocs/gcc-4.8.5/cpp/Stringification.html



// Notice the two starting brackets here, with the
// end brackets defined later in a different macro.
// #define _BEGIN_STD_C namespace std { extern "C" {
// #define _END_STD_C  } }



public class Macro
  {
  private MainApp mApp;
  // Once the key is assigned, it can't be changed.
  private final StrA key;
  private StrA markedUpS = new StrA( "" );
  private boolean isFunctionType = false;  
  private boolean enabled = true; // For undef.
  private StrArray paramArray = null;



  private Macro()
    {
    }



  public Macro( MainApp useApp )
    {
    mApp = useApp;
    enabled = true;
    }



  public Macro( MainApp useApp, StrA keyToUse )
    {
    mApp = useApp;
    enabled = true;
    key = keyToUse;
    markedUpS = new StrA( "" );
    }



  public boolean getIsFunctionType()
    {
    return isFunctionType;
    }
 



  public boolean getEnabled()
    {
    return enabled;
    }



  public void setEnabled( boolean setTo )
    {
    enabled = setTo;
    }



  public boolean setKeyFromStrA( StrA in )
    {
    try
    {
    isFunctionType = false;

    StrA line = in.removeSections( Markers.Begin,
                                        Markers.End );

    line = line.trim();
    StrArray splitS = line.splitChar( ' ' );
    int last = splitS.length();
    if( last == 0 )
      {
      mApp.showStatusAsync( "There is no key in the directive." );
      return false;
      }

    StrA tempKey = splitS.getStrAt( 0 );

    // The parentheses is not part of the key.
    if( tempKey.contains( new StrA( "(" )))
      {
      // This is a function-like macro because there
      // was no space before the first parentheses.
      isFunctionType = true;

      StrArray lineSplitter = tempKey.splitChar( '(' );
      int lastPart = lineSplitter.length();

      tempKey = lineSplitter.getStrAt( 0 );
      }

    if( tempKey.length() == 0 )
      {
      mApp.showStatusAsync( "The key length is zero." );
      return false;
      }

    key = tempKey;  // Assigned once.
    return true;
    }
    catch( Exception e )
      {
      mApp.showStatusAsync( "Exception in setKeyFromString()." );
      mApp.showStatusAsync( e.getMessage() );
      return false;
      }
    }



  public boolean markUpFromStrA( StrA in,
                     MacroDictionary macroDictionary,
                     boolean doStrict )
    {
    try
    {
    StrA originalStr = in;
    markedUpS = new StrA( in );
    // Remove the line number markers.
    markedUpS = markedUpS.removeSections( 
                                        Markers.Begin,
                                        Markers.End );

    markedUpS = new StrA( MarkupString.MarkItUp( mApp,
                               markedUpS.toString()));

    StrArray splitS = markedUpS.splitChar( 
                                     Markers.Begin );

    int last = splitS.length();
    if( last < 2 )
      {
      mApp.showStatusAsync( "This macro has no key marked up." );
      return false;
      }
      
    String testNothing = splitS.getStrAt( 0 ).
                                         toString();
    if( testNothing.length() != 0 )
      {
      mApp.showStatusAsync( "testNothing: " + testNothing );
      return false;
      }

    String testKey = splitS.getStrAt( 1 ).toString();
    // It would have at least 2 marker characters.
    if( testKey.length() < 2 )
      {
      mApp.showStatusAsync( "The key is missing." );
      mApp.showStatusAsync( markedUpS.toString() );
      return false;
      }

    char firstChar = testKey.charAt( 0 ); 
    if( firstChar != Markers.TypeIdentifier )
      {
      mApp.showStatusAsync( "The key is not an identifier." );
      mApp.showStatusAsync( markedUpS.toString() );
      return false;
      }

    testKey = Markers.removeAllMarkers( testKey );
    if( !key.equals( testKey ))
      {
      mApp.showStatusAsync( "The key is not equal to the first token." );
      mApp.showStatusAsync( "Key: >" + key + "<" );
      mApp.showStatusAsync( "TestKey: >" + testKey + "<" );
      mApp.showStatusAsync( markedUpS.toString() );
      return false;
      }

    StringBuilder sBuilder = new StringBuilder();
    for( int count = 2; count < last; count++ )
      {
      sBuilder.append( "" + Markers.Begin  +
                            splitS.getStrAt( count ));
      }

    // Put the paramters back, but leave the key out.
    markedUpS = new StrA( sBuilder.toString());
    // If there are no parameters then markedUpS
    // would have zero length.
    // mApp.showStatusAsync( "markedUpS after toString: " + markedUpS );

    if( isFunctionType )
      {
      markedUpS = putFParametersInArray(
                             markedUpS );
      if( markedUpS.length() == 0 )
        {
        mApp.showStatusAsync( "putFPameters had an error." );
        return false;
        }

      // mApp.showStatusAsync( "\n\nMaking function type body: " + key );
      // mApp.showStatusAsync( markedUpS + "\n\n" );
      }

    // If there are any parameters.
    if( markedUpS.length() > 0 )
      {
      markedUpS = new StrA( replaceMacros( mApp,
                                 key.toString(),
                                 markedUpS.toString(),
                                 macroDictionary ));

      }

    return true;
    }
    catch( Exception e )
      {
      mApp.showStatusAsync( "Exception in markUpFromString()." );
      mApp.showStatusAsync( e.getMessage() );
      return false;
      }
    }



  public static StrA replaceMacros( MainApp mApp,
                                    StrA key,
                                    StrA in,
                                    MacroDictionary
                                      macroDictionary )
    {
    if( in == null )
      return new StrA( "" );

    String result = in;
    for( int count = 0; count < 100; count++ )
      {
      if( count > 10 )
        mApp.showStatusAsync( "count > 10 to replace macros.\nKey: " + key + "\n" + in );

      if( result.length() == 0 )
        {
        // It can replace something with nothing.
        return result;
        }

      String testS = result;

      // If this is being called from outside,
      // when a macro is not being created.
      if( key.length() == 0 )
        {
        result = FunctionMacro.replaceMacrosOnce(
                                    mApp,
                                    result,
                                    macroDictionary );

        }
 
      result = replaceMacrosOnce( mApp,
                                  key,
                                  result,
                                  macroDictionary );


      // If it hasn't replaced anything.
      if( testS.equals( result ))
        break;

      }
    
    return result;
    }




  private static StrA replaceMacrosOnce( MainApp mApp,
                                    StrA key,
                                    StrA in,
                                    MacroDictionary
                                      macroDictionary )
    {
    if( in.length() == 0 )
      return "";

    if( !in.contains( "" + Markers.Begin ))
      {
      mApp.showStatusAsync( "\n\nNo begin marker in replaceMacrosOnce(): " + in );
      return in;
      }

    // mApp.showStatusAsync( "\n\nIn: " + in );

    StringBuilder sBuilder = new StringBuilder();

    String[] splitS = in.split( "" + Markers.Begin );
    int last = splitS.length;
    if( last == 0 )
      {
      mApp.showStatusAsync( "Last is zero in replaceMacrosOnce()." );
      return "";
      } 
      
    String testNothing = splitS[0];
    if( testNothing.length() != 0 )
      {
      mApp.showStatusAsync( "testNothing: " + testNothing );
      return "";
      }

    for( int count = 1; count < last; count++ )
      {
      String partS = splitS[count];
      // It would have at least two markers.
      if( partS.length() < 2 )
        continue;

      String originalPartS = partS;
      char firstChar = partS.charAt( 0 ); 
      if( firstChar != Markers.TypeIdentifier )
        {
        sBuilder.append( "" + Markers.Begin +
                                     originalPartS );
        continue;
        }

      partS = Markers.removeAllMarkers( partS );
      if( macroDictionary.keyExists( partS ))
        {
        Macro replaceMacro = macroDictionary.
                                   getMacro( partS );

        // This static function can be called with an
        // empty key.  Which means it is probably 
        // being called from somewhere else like
        // IfExpression.
        if( key.length() > 0 )
          {
          if( key.equals( replaceMacro.getKey()))
            {
            mApp.showStatusAsync( "This is a self-referential macro: " + key );
            return "";
            }
          }

        if( replaceMacro.getIsFunctionType())
          {
          // Ignore it here, since it's done
          // somewhere else.
          sBuilder.append( "" + Markers.Begin +
                                       originalPartS );
          continue;
          }

        // The whole string can include many markers.
        sBuilder.append( replaceMacro.
                                getMarkedUpString());

        continue;
        }

      sBuilder.append( "" + Markers.Begin +
                                    originalPartS );
      }

    String result = sBuilder.toString();

    // It can replace a macro with an empty string,
    // so it could be length zero.

    if( result.length() > 0 )
      {
      if( !MarkupString.testMarkers( result, 
                                     "replaceMacros().",
                                     mApp ))
        {
        mApp.showStatusAsync( "testMarkers() returned false." );
        return "";
        }
      }

    // if( in.contains( "Partitions" ))
      // mApp.showStatusAsync( "Out: " + result );

    return result; 
    }



  private StrA putFParametersInArray( StrA in )
    {
    // mApp.showStatusAsync( "\nputFParam: " + in );

    paramArray = new StrArray();
    StringBuilder sBuilder = new StringBuilder();
    StrArray splitS = in.splitChar( Markers.Begin );
    int last = splitS.length();
    if( last == 0 )
      {
      mApp.showStatusAsync( "Last is zero in putFParametersInArray()." );
      return new StrA( "" );
      } 
      
    String testNothing = splitS.getStrAt( 0 ).toString();
    if( testNothing.length() != 0 )
      {
      mApp.showStatusAsync( "testNothing: " + testNothing );
      return new StrA( "" );
      }

    int inside = 0;
    for( int count = 1; count < last; count++ )
      {
      String partS = splitS.getStrAt( count ).toString();
      if( partS.length() < 2 )
        continue;

      if( inside >= 2 )
        {
        sBuilder.append( "" + Markers.Begin + partS );
        continue;
        }

      char firstChar = partS.charAt( 0 );
      if( firstChar == Markers.TypeOperator )
        {
        char secondChar = partS.charAt( 1 );
        if( secondChar == '(' )
          {
          inside++;
          continue;
          }

        if( secondChar == ')' )
          {
          inside++;
          continue;
          }
        }

      if( firstChar == Markers.TypeIdentifier )
        {
        // mApp.showStatusAsync( "Adding param: " + partS );
 
        paramArray.append( new StrA( "" + Markers.Begin +
                                 partS ));
 
        continue;
        }
      }

    String result = sBuilder.toString();
    // mApp.showStatusAsync( "Result: " + result );
    return new StrA( result );   
    }




  public StrA getMarkedUpS()
    {
    return markedUpS;
    }



  public StrA getKey()
    {
    return key;
    }


  public int getParamArrayLength()
    {
    if( paramArray == null )
      return 0;

    return paramArray.length();
    }



  public StrA getParamArrayValue( int where )
    {
    if( paramArray == null )
      return new StrA( "" );

    return paramArray.getStrAt( where );
    }



  }
