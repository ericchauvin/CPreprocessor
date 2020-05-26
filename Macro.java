// Copyright Eric Chauvin 2020.


// The ## makes two _identifiers_ merge in to one.
// #define COMMAND(NAME)  { #NAME, NAME ## _command }
// The single # is for stringizing.
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

    if( last == 2 )
      {
      // Like a #define statement with a key but 
      // no parameters.
      // A #define statement can define an empty
      // macro, like to remove a key word.
      return true;
      }

    StringBuilder sBuilder = new StringBuilder();
    for( int count = 2; count < last; count++ )
      {
      sBuilder.append( Character.toString( 
                                  Markers.Begin ) +
                                  splitS[count] );
      }

    // Put the paramters back, but leave the key out.
    markedUpString = sBuilder.toString();



/*
=====
while( true ) for a different function.

      sBuilder.setLength( 0 );

      for( int count = 2; count < last; count++ )
        {
        String token = splitS[count];
        char firstChar = token.charAt( 0 ); 
        if( firstChar == Markers.TypeLineNumber )
          continue;

        if( firstChar != Markers.TypeIdentifier )
          {
          if( count == 1 )
            {
            mApp.showStatus( "The key is not an identifier." );
            return false;
            }

          continue;
          }

      token = Markers.removeAllMarkers( token );
      // There might be a space character after
      // the end marker.
      token = token.trim();
      if( count == 1 )
        {
        if( !key.equals( token ))
          {
          mApp.showStatus( "The key is not equal to the first token." );
          mApp.showStatus( "Key: >" + key + "<" );
          mApp.showStatus( "Token: >" + token + "<" );
          mApp.showStatus( markedUpString );
          return false;
          }

        continue;
        }
      
      // If one of these tokens is the same as
      // the key then it's self referential.


      mApp.showStatus( "Token: " + token );
==== Does the key exist for this token?

====== if( macroDictionary.keyExists( key ))
      // {

      }

    mApp.showStatus( " " );



/////////
    StringBuilder paramBuilder = new StringBuilder();
    for( int count = 1; count < last; count++ )
      paramBuilder.append( splitS[count] + " " );

    paramStr = paramBuilder.toString();
    paramStr = paramStr.trim();

    // The undef, ifdef and ifndef statements all
    // use a key without the parentheses.  The
    // parentheses is not part of the key.
    if( key.contains( "(" ))
      {
      // This is a function-like macro because there
      // was no space before the first parentheses.
      // isFunctionType = true;

      // This key could end with the parentheses,
      // but sometimes they have no space after the
      // parentheses too like this:
      // DEF_SANITIZER_BUILTIN_1(ENUM,

      StringArray lineSplitter = new StringArray();
      int lastPart = lineSplitter.
                      makeFieldsFromString( key, '(' );

      StringBuilder sBuilder = new StringBuilder();

      key = lineSplitter.getStringAt( 0 );
      for( int count = 1; count < lastPart; count++ )
        sBuilder.append( "(" + lineSplitter.
                                getStringAt( count ));

      paramStr = sBuilder.toString() +
                              " " + paramStr.trim();

      }

    if( macroDictionary.keyExists( key ))
      {
      mApp.showStatus( "Macro key already exists: " + key );
      mApp.showStatus( "paramStr: " + paramStr );
      Macro showMac = macroDictionary.getMacro( key );
      mApp.showStatus( "Original paramStr: " +
                                showMac.getString());
      return false;
      }
    else
      {
      macroDictionary.setMacro( key, this );
      }

    mApp.showStatus( " " );
    mApp.showStatus( "key: " + key );
    mApp.showStatus( "paramStr: " + paramStr );
    mApp.showStatus( " " );
*/

    return true;
    }
    catch( Exception e )
      {
      mApp.showStatus( "Exception in markUpFromString()." );
      mApp.showStatus( e.getMessage() );
      return false;
      }
    }




/*
  public String getString()
    {
    return key + " " + paramStr;
    }
*/


  public String getKey()
    {
    return key;
    }


  }
