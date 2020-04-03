// Copyright Eric Chauvin 2020.



public class Macro
  {
  private MainApp mApp;
  private String key = "";
  private String paramStr = "";
  private boolean isFunctionType = false;  


  private Macro()
    {
    }



  public Macro( MainApp useApp )
    {
    mApp = useApp;
    }



  public boolean setFromString( String in )
    {
    try
    {
    // A macro can replace something with nothing.
    // If the parameter list is empty, it replaces
    // the identifier with nothing.
    // This would replace the word 'volatile' with
    // nothing.
    // #define volatile

    isFunctionType = false;

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

    if( key.contains( "(" ))
      {
      // This is a function-like macro because there
      // was no space before the first parentheses.
      isFunctionType = true;

      // This key could end with the parentheses,
      // but sometimes they have no space after the
      // parentheses too like this:
      // DEF_SANITIZER_BUILTIN_1(ENUM,

      StringArray lineSplitter = new StringArray();
      int lastPart = lineSplitter.makeFieldsFromString( key, '(' );

      StringBuilder sBuilder = new StringBuilder();

      key = lineSplitter.getStringAt( 0 );
      for( int count = 1; count < lastPart; count++ )
        sBuilder.append( "(" + lineSplitter.getStringAt( count ));

      paramStr = sBuilder.toString() +
                              " " + paramStr.trim();

      // definesDictionary.setString( key, paramStr );
      
      }

/*
    if( definesDictionary.keyExists( key ))
      {
      mApp.showStatus( "define key already exists: " + key );
      mApp.showStatus( "paramStr: " + paramStr );
      String showP = definesDictionary.getString( key );
      mApp.showStatus( "Original paramStr: " + showP );
      }
    else
      {
      definesDictionary.setString( key, paramStr );
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
      mApp.showStatus( "Exception in setFromString()." );
      mApp.showStatus( e.getMessage() );
      return false;
      }
    }



  }
