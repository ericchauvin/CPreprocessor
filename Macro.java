// Copyright Eric Chauvin 2020.




// Don't put preprocessor directives inside a macro.
// The Gnu Compiler Collection is like the poster
// child of how to do really bad things with macros.
// In the C and C++ standards a directive in a macro
// is undefined.  It is an error in this preprocessor.

 


public class Macro
  {
  private MainApp mApp;
  private String key = "";
  private String paramStr = "";
  // private boolean isFunctionType = false;  
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



  public boolean setFromString( String in )
    {
    try
    {
    // isFunctionType = false;

    String[] splitS = in.split( " " );
    int last = splitS.length;
    if( last == 0 )
      {
      mApp.showStatus( "There is nothing in the macro." );
      return false;
      }

    key = splitS[0];

    StringBuilder paramBuilder = new StringBuilder();
    for( int count = 1; count < last; count++ )
      paramBuilder.append( splitS[count] + " " );

    paramStr = paramBuilder.toString();
    paramStr = " " + paramStr.trim();

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

    return true;
    }
    catch( Exception e )
      {
      mApp.showStatus( "Exception in setFromString()." );
      mApp.showStatus( e.getMessage() );
      return false;
      }
    }



  public String getString()
    {
    return key + " " + paramStr;
    }

  }
