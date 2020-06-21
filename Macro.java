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
  private String key = "";
  private String markedUpS = "";
  private boolean isFunctionType = false;  
  private boolean enabled = true; // For undef.
  private StringArray paramArray = null;



  private Macro()
    {
    }



  public Macro( MainApp useApp )
    {
    mApp = useApp;
    enabled = true;
    }



  public boolean getIsFunctionType()
    {
    return isFunctionType;
    }
  


  public void setMacroWithEmptyParams(
                                     String keyToUse )
    {
    key = keyToUse;
    markedUpS = "";
    }



  public boolean getEnabled()
    {
    return enabled;
    }


  public void setEnabled( boolean setTo )
    {
    enabled = setTo;
    }



  public boolean setKeyFromString( String in )
    {
    try
    {
    isFunctionType = false;

    String line = StringsUtility.removeSections( in,
                                        Markers.Begin,
                                        Markers.End );

    line = line.trim();
    String[] splitS = line.split( " " );
    int last = splitS.length;
    if( last == 0 )
      {
      mApp.showStatusAsync( "There is no key in the directive." );
      return false;
      }

    key = splitS[0];

    // The parentheses is not part of the key.
    if( key.contains( "(" ))
      {
      // This is a function-like macro because there
      // was no space before the first parentheses.
      isFunctionType = true;

      StringArray lineSplitter = new StringArray();
      int lastPart = lineSplitter.
                      makeFieldsFromString( key, '(' );

      key = lineSplitter.getStringAt( 0 );
      }

    if( key.length() == 0 )
      {
      mApp.showStatusAsync( "The key length is zero." );
      return false;
      }

    return true;
    }
    catch( Exception e )
      {
      mApp.showStatusAsync( "Exception in setKeyFromString()." );
      mApp.showStatusAsync( e.getMessage() );
      return false;
      }
    }



  public boolean markUpFromString( String in,
                     MacroDictionary macroDictionary,
                     boolean doStrict )
    {
    try
    {
    String originalStr = in;
    markedUpS = in;
    // Remove the line number markers.
    markedUpS = StringsUtility.removeSections(
                                        markedUpS,
                                        Markers.Begin,
                                        Markers.End );

    markedUpS = MarkupString.MarkItUp( mApp,
                                          markedUpS );

    String[] splitS = markedUpS.split( "" +
                                   Markers.Begin );

    int last = splitS.length;
    if( last < 2 )
      {
      mApp.showStatusAsync( "This macro has no key marked up." );
      return false;
      }
      
    String testNothing = splitS[0];
    if( testNothing.length() != 0 )
      {
      mApp.showStatusAsync( "testNothing: " + testNothing );
      return false;
      }

    String testKey = splitS[1];
    // It would have at least 2 marker characters.
    if( testKey.length() < 2 )
      {
      mApp.showStatusAsync( "The key is missing." );
      mApp.showStatusAsync( markedUpS );
      return false;
      }

    char firstChar = testKey.charAt( 0 ); 
    if( firstChar != Markers.TypeIdentifier )
      {
      mApp.showStatusAsync( "The key is not an identifier." );
      mApp.showStatusAsync( markedUpS );
      return false;
      }

    testKey = Markers.removeAllMarkers( testKey );
    if( !key.equals( testKey ))
      {
      mApp.showStatusAsync( "The key is not equal to the first token." );
      mApp.showStatusAsync( "Key: >" + key + "<" );
      mApp.showStatusAsync( "TestKey: >" + testKey + "<" );
      mApp.showStatusAsync( markedUpS );
      return false;
      }

    StringBuilder sBuilder = new StringBuilder();
    for( int count = 2; count < last; count++ )
      {
      sBuilder.append( "" + Markers.Begin  +
                                  splitS[count] );
      }

    // Put the paramters back, but leave the key out.
    markedUpS = sBuilder.toString();
    // If there are no parameters then markedUpS
    // would have zero length.
    // mApp.showStatusAsync( "markedUpS after toString: " + markedUpS );

    if( isFunctionType )
      {
      markedUpS = putFParametersInArray( markedUpS );
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
      markedUpS = replaceMacros( mApp,
                                 key,
                                 markedUpS,
                                 macroDictionary );

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



  public static String replaceMacros( MainApp mApp,
                                      String key,
                                      String in,
                                      MacroDictionary
                                      macroDictionary )
    {
    if( in == null )
      return "";

    String result = in;
    FunctionMacro functionMacro = new 
                               FunctionMacro( mApp,
                                   macroDictionary );

    for( int count = 0; count < 100; count++ )
      {
      if( result.length() == 0 )
        {
        // It can replace something with nothing.
        // mApp.showStatusAsync( "\nreplaceMacros returned nothing." );
        // mApp.showStatusAsync( "Original: " + in );
        return result;
        }

      if( count > 10 )
        mApp.showStatusAsync( "count > 10 to replace macros.\nKey: " + key + "\n" + in );

      String testS = result;

      if( key.length() == 0 )
        {
        result = functionMacro.replaceMacrosOnce(
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




  private static String replaceMacrosOnce(
                                    MainApp mApp,
                                    String key,
                                    String in,
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



  private String putFParametersInArray( String in )
    {
    // mApp.showStatusAsync( "\nputFParam: " + in );

    paramArray = new StringArray();
    StringBuilder sBuilder = new StringBuilder();
    String[] splitS = in.split( "" + Markers.Begin );
    int last = splitS.length;
    if( last == 0 )
      {
      mApp.showStatusAsync( "Last is zero in putFParametersInArray()." );
      return "";
      } 
      
    String testNothing = splitS[0];
    if( testNothing.length() != 0 )
      {
      mApp.showStatusAsync( "testNothing: " + testNothing );
      return "";
      }

    int inside = 0;
    for( int count = 1; count < last; count++ )
      {
      String partS = splitS[count];
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
        String param = Markers.removeAllMarkers( 
                                              partS );

        // mApp.showStatusAsync( "Adding param: " + param );
        paramArray.appendString( param );
        continue;
        }
      }

    String result = sBuilder.toString();
    // mApp.showStatusAsync( "Result: " + result );
    return result;   
    }




  public String getMarkedUpString()
    {
    return markedUpS;
    }



  public String getKey()
    {
    return key;
    }


  public int getParamArrayLength()
    {
    if( paramArray == null )
      return 0;

    return paramArray.length();
    }



  public String getParamArrayValue( int where )
    {
    if( paramArray == null )
      return "";

    return paramArray.getStringAt( where );
    }



  }
