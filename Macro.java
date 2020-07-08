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
  private StrA markedUpS = StrA.Empty;
  private boolean isFunctionType = false;  
  private boolean enabled = true; // For undef.
  private StrArray paramArray = null;



  private Macro()
    {
    key = StrA.Empty;
    }



  public Macro( MainApp useApp, StrA keyToUse )
    {
    mApp = useApp;
    enabled = true;
    key = keyToUse;
    markedUpS = StrA.Empty;
    }



  public Macro( MainApp useApp, StrA in,
                                    boolean notUsed )
    {
    mApp = useApp;
    enabled = true;
    markedUpS = StrA.Empty;

    // This will set isFunctionType.
    key = getKeyForConstructor( in );
    }



  private StrA getKeyForConstructor( StrA in )
    {
    isFunctionType = false;

    // Remove line numbers.
    StrA line = in.removeSections( Markers.Begin,
                                        Markers.End );

    line = line.trim();
    StrArray splitS = line.splitChar( ' ' );
    final int last = splitS.length();
    if( last == 0 )
      {
      mApp.showStatusAsync( "There is no key." );
      return StrA.Empty;
      }

    StrA tempKey = splitS.getStrAt( 0 );

    // The parentheses is not part of the key.
    if( tempKey.containsChar( '(' ))
      {
      // This is a function-like macro because there
      // was no space before the first parentheses.
      isFunctionType = true;

      StrArray lineSplitter = tempKey.splitChar( '(' );
      tempKey = lineSplitter.getStrAt( 0 );
      }

    if( tempKey.length() == 0 )
      {
      mApp.showStatusAsync( "The key length is zero." );
      return StrA.Empty;
      }

    return tempKey;
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



  public boolean markUpForDefine( StrA in,
                     MacroDictionary macroDictionary )
    {
    try
    {
    // StrA originalStr = in;
    markedUpS = in;
    // Remove the line number markers.
    markedUpS = markedUpS.removeSections( 
                                        Markers.Begin,
                                        Markers.End );

    markedUpS = MarkupString.MarkItUp( mApp,
                                        markedUpS );

    StrArray splitS = markedUpS.splitChar( 
                                     Markers.Begin );

    final int last = splitS.length();
    if( last < 2 )
      {
      mApp.showStatusAsync( "This macro has no key marked up." );
      return false;
      }
      
    StrA testNothing = splitS.getStrAt( 0 );
    if( testNothing.length() != 0 )
      {
      mApp.showStatusAsync( "testNothing: " + testNothing );
      return false;
      }

    StrA testKey = splitS.getStrAt( 1 );
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
    if( !key.equalTo( testKey ))
      {
      mApp.showStatusAsync( "The key is not equal to the first identifier." );
      mApp.showStatusAsync( "Key: >" + key + "<" );
      mApp.showStatusAsync( "TestKey: >" + testKey + "<" );
      mApp.showStatusAsync( markedUpS.toString() );
      return false;
      }

    StrABld sBuilder = new StrABld( 1024 );
    for( int count = 2; count < last; count++ )
      {
      sBuilder.appendStrA( new StrA( "" +
                            Markers.Begin  +
                            splitS.getStrAt( count )));
      }

    // Put the paramters back, but leave the key out.
    markedUpS = sBuilder.toStrA();
    // If there are no parameters then markedUpS
    // would have zero length.
    // mApp.showStatusAsync( "markedUpS after toString: " + markedUpS );

    if( isFunctionType )
      {
      // This is a function type macro with no
      // function body.
      // #define WINAPI_PARTITION_SERVER(
      //       WINAPI_FAMILY==WINAPI_FAMILY_SERVER)

      // mApp.showStatusAsync( "isFunctionType: " + markedUpS );
      markedUpS = putFParametersInArray( markedUpS );
      // The function might have no body.
      // if( markedUpS.length() == 0 )

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

    // mApp.showStatusAsync( "markedUpS at end: " + markedUpS );

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
      return StrA.Empty;

    StrA result = in;
    for( int count = 0; count < 100; count++ )
      {
      if( count > 10 )
        mApp.showStatusAsync( "count > 10 to replace macros.\nKey: " + key + "\n" + in );

      if( result.length() == 0 )
        {
        // It can replace something with nothing.
        return result;
        }

      StrA testS = result;

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
      if( testS.equalTo( result ))
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
      return StrA.Empty;

    if( !in.containsChar( Markers.Begin ))
      {
      mApp.showStatusAsync( "\n\nNo begin marker in replaceMacrosOnce(): " + in );
      return in;
      }

    // mApp.showStatusAsync( "\n\nIn: " + in );

    StrABld sBuilder = new StrABld( 1024 );

    StrArray splitS = in.splitChar( Markers.Begin );
    final int last = splitS.length();
    if( last == 0 )
      {
      mApp.showStatusAsync( "Last is zero in replaceMacrosOnce()." );
      return StrA.Empty;
      } 
      
    StrA testNothing = splitS.getStrAt( 0 );
    if( testNothing.length() != 0 )
      {
      mApp.showStatusAsync( "testNothing: " + testNothing );
      return StrA.Empty;
      }

    for( int count = 1; count < last; count++ )
      {
      StrA partS = splitS.getStrAt( count );
      // It would have at least two markers.
      if( partS.length() < 2 )
        continue;

      StrA originalPartS = partS;
      char firstChar = partS.charAt( 0 ); 
      if( firstChar != Markers.TypeIdentifier )
        {
        sBuilder.appendStrA( new StrA( "" +
                                     Markers.Begin +
                                     originalPartS ));
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
          if( key.equalTo( replaceMacro.getKey()))
            {
            mApp.showStatusAsync( "This is a self-referential macro: " + key );
            return StrA.Empty;
            }
          }

        if( replaceMacro.getIsFunctionType())
          {
          // Ignore it here, since it's done
          // somewhere else.
          sBuilder.appendStrA( new StrA( "" +
                                      Markers.Begin +
                                      originalPartS ));
          continue;
          }

        // The whole string can include many markers.
        sBuilder.appendStrA( replaceMacro.
                                getMarkedUpS());

        continue;
        }

      sBuilder.appendStrA( new StrA( "" +
                                    Markers.Begin +
                                    originalPartS ));
      }

    StrA result = sBuilder.toStrA();

    // It can replace a macro with an empty string,
    // so it could be length zero.

    if( result.length() > 0 )
      {
      if( !MarkupString.testMarkers( result, 
                                 new StrA(
                                 "replaceMacros()." ),
                                 mApp ))
        {
        mApp.showStatusAsync( "testMarkers() returned false." );
        return StrA.Empty;
        }
      }

    // if( in.contains( "Partitions" ))
      // mApp.showStatusAsync( "Out: " + result );

    return result; 
    }



  private StrA putFParametersInArray( StrA in )
    {
    // This is a function type macro with no
    // function body.  And the parameters have to
    // be evaluated.
    // #define WINAPI_PARTITION_SERVER(
    //       WINAPI_FAMILY==WINAPI_FAMILY_SERVER)

    // mApp.showStatusAsync( "\nIn Macro.putFParam: " + in );

    paramArray = new StrArray();
    StrABld sBuilder = new StrABld( 1024 );
    StrArray splitS = in.splitChar( Markers.Begin );
    final int last = splitS.length();
    if( last == 0 )
      {
      mApp.showStatusAsync( "Last is zero in putFParametersInArray()." );
      return StrA.Empty;
      } 
      
    StrA testNothing = splitS.getStrAt( 0 );
    if( testNothing.length() != 0 )
      {
      mApp.showStatusAsync( "testNothing: " + testNothing );
      return StrA.Empty;
      }

    int inside = 0;
    for( int count = 1; count < last; count++ )
      {
      StrA partS = splitS.getStrAt( count );
      // mApp.showStatusAsync( "PartS: " + partS );

      if( partS.length() < 2 )
        continue;

      if( inside >= 2 )
        {
        sBuilder.appendChar( Markers.Begin );
        sBuilder.appendStrA( partS );
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
        mApp.showStatusAsync( "Does this param have to be replaced wiht its macro value? " + partS );
        // And evaluated to?

        paramArray.append( new StrA( "" + 
                             Markers.Begin + partS ));
        continue;
        }
      }

    StrA result = sBuilder.toStrA();
    // mApp.showStatusAsync( "Result: " + result );
    return result;   
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
      return StrA.Empty;

    return paramArray.getStrAt( where );
    }



  }
