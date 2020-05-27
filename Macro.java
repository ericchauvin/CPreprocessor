// Copyright Eric Chauvin 2020.



// https://gcc.gnu.org/onlinedocs/gcc-4.8.5/cpp/Stringification.html


public class Macro
  {
  private MainApp mApp;
  private String key = "";
  private String markedUpString = "";
  private boolean isFunctionType = false;  
  private MacroDictionary macroDictionary;
  private boolean enabled = true; // For undef.



  private Macro()
    {
    }



  public Macro( MainApp useApp, 
                   MacroDictionary dictionaryToUse )
    {
    mApp = useApp;
    macroDictionary = dictionaryToUse;
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
      mApp.showStatus( "There is no key in the directive." );
      return false;
      }

    key = splitS[0];

    // The undef, ifdef and ifndef statements all
    // use a key without the parentheses.  The
    // parentheses is not part of the key.
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
      mApp.showStatus( "The key length is zero." );
      return false;
      }

    return true;
    }
    catch( Exception e )
      {
      mApp.showStatus( "Exception in setKeyFromString()." );
      mApp.showStatus( e.getMessage() );
      return false;
      }
    }



  public boolean markUpFromString( String in )
    {
    try
    {
    markedUpString = MarkupString.MarkItUp( mApp,
                                            in );

    String[] splitS = markedUpString.split(
                                 Character.toString(
                                 Markers.Begin ));

    int last = splitS.length;
    if( last < 2 )
      {
      mApp.showStatus( "This macro has no key marked up." );
      return false;
      }
      
    // The string at zero is what's before the first
    // Begin marker, which is nothing.

    String testKey = splitS[1];
    char firstChar = testKey.charAt( 0 ); 
    if( firstChar != Markers.TypeIdentifier )
      {
      mApp.showStatus( "The key is not an identifier." );
      mApp.showStatus( markedUpString );
      return false;
      }

    testKey = Markers.removeAllMarkers( testKey );
    if( !key.equals( testKey ))
      {
      mApp.showStatus( "The key is not equal to the first token." );
      mApp.showStatus( "Key: >" + key + "<" );
      mApp.showStatus( "TestKey: >" + testKey + "<" );
      mApp.showStatus( markedUpString );
      return false;
      }

/*
Do a warning if there are too many loops to replace
macros.  Like infinite indirect references.
What if a macro has the same name as a variable name?

// ==== see if an identifier is self referential.
// Put in a warning when this happens.
// And comment the code.
*/

    StringBuilder sBuilder = new StringBuilder();
    for( int count = 2; count < last; count++ )
      {
      sBuilder.append( Character.toString( 
                                  Markers.Begin ) +
                                  splitS[count] );
      }

    // Put the paramters back, but leave the key out.
    markedUpString = sBuilder.toString();

    // How many loops would be abnormal?
    for( int count = 0; count < 100; count++ )
      {
      if( !replaceMacros())
        break;

      }

    return setNewMacroInDictionary();
    }
    catch( Exception e )
      {
      mApp.showStatus( "Exception in markUpFromString()." );
      mApp.showStatus( e.getMessage() );
      return false;
      }
    }



  private boolean setNewMacroInDictionary()
    {
    if( macroDictionary.keyExists( key ))
      {
      mApp.showStatus( "Macro key already exists: " + key );
      mApp.showStatus( "markedUpString: " + markedUpString );
      Macro showMac = macroDictionary.getMacro( key );
      mApp.showStatus( "Original markedUpString: " +
                            showMac.getMarkedUpString());
      return false;
      }

    macroDictionary.setMacro( key, this );
    // mApp.showStatus( " " );
    // mApp.showStatus( "Setting new key: " + key );
    // mApp.showStatus( "markedUpString: " + markedUpString );
    // mApp.showStatus( " " );
    return true;
    }



  private boolean replaceMacros()
    {
    boolean replacedIdentifier = false;

    StringBuilder sBuilder = new StringBuilder();

    String[] splitS = markedUpString.split(
                                 Character.toString(
                                 Markers.Begin ));

    int last = splitS.length;
      
    // The string at zero is what's before the first
    // Begin marker, which is nothing.

    for( int count = 1; count < last; count++ )
      {
      String token = splitS[count];
      char firstChar = token.charAt( 0 ); 
      if( firstChar == Markers.TypeLineNumber )
        {
        sBuilder.append( Character.toString( 
                                   Markers.Begin ) +
                                   splitS[count] );
        continue;
        }


// ====== Fix stringification.
// Do a search and replace for begin marker
// the operator then # character and see if it's
// two in a row.  Put them together as one operator.
// Do things like >= and operators like that too.
// Make a SetOperators object?


      if( firstChar != Markers.TypeIdentifier )
        {
        sBuilder.append( Character.toString( 
                                   Markers.Begin ) +
                                   splitS[count] );
        continue;
        }

      token = Markers.removeAllMarkers( token );
      if( token.equals( key ))
        {
        mApp.showStatus( "This macro has a self referential key." );
        mApp.showStatus( "Key: " + key );
        mApp.showStatus( markedUpString );
        return false;
        }

      if( macroDictionary.keyExists( token ))
        {
        Macro replaceMacro = macroDictionary.
                                   getMacro( token );

        replacedIdentifier = true;
        sBuilder.append( replaceMacro.
                                getMarkedUpString());
        continue;
        }

      sBuilder.append( Character.toString( 
                       Markers.Begin ) + splitS[count] );

      }

    markedUpString = sBuilder.toString();

    if( !MarkupString.testMarkers( markedUpString, 
                                   "replaceMacros().",
                                   mApp ))
      {
      return false;
      }

    if( replacedIdentifier )
      mApp.showStatus( "markedUpString: " + markedUpString );

    return replacedIdentifier; 
    }



  public String getMarkedUpString()
    {
    return markedUpString;
    }



  public String getKey()
    {
    return key;
    }


  }
