// Copyright Eric Chauvin 2019 - 2020.



// An identifier name like Table is different from the
// name table, so this dictionary needs to be
// case-sensitive.  But when it's sorted, it is a
// case-insensitive sort.  So the words Table and
// table would be considered equal in the sort order.



public class DefinesDictionary
  {
  private MainApp mApp;
  private DefinesDictionaryLine lineArray[];
  private final int maxIndexLetter = 'z' - 'a';
  private final int keySize = ((maxIndexLetter << 6) |
                                  maxIndexLetter) + 1;




  private DefinesDictionary()
    {
    }



  public DefinesDictionary( MainApp useApp )
    {
    mApp = useApp;

    lineArray = new DefinesDictionaryLine[keySize];
    }



  public void clear()
    {
    for( int count = 0; count < keySize; count++ )
      lineArray[count] = null;

    }



  private int letterToIndexNumber( char letter )
    {
    letter = Character.toLowerCase( letter );
    int index = letter - 'a';
    if( index < 0 )
      index = 0;

    // A letter that's way up there in Unicode, like
    // a Chinese character, would be given the value
    // maxindexletter.  In other words anything higher
    // than the letter z is lumped together with z.
    if( index > maxIndexLetter )
      index = maxIndexLetter;

    return index;
    }



  private int getIndex( String key )
    {
    // This index needs to be in sorted order.

    int keyLength = key.length();
    if( keyLength < 1 )
      return 0;

    // The letter z by itself would be sorted before
    // the two letters az unless the space character
    // is added.
    if( keyLength == 1 )
      key = key + " ";

    int one = letterToIndexNumber( key.charAt( 0 ));
    int tempTwo = letterToIndexNumber( key.charAt( 1 ));
    int two = (one << 6) | tempTwo;

    if( two >= keySize )
      two = keySize - 1;

    return two;
    }




  public void setString( String key, String value )
    {
    try
    {
    if( key == null )
      return;

    key = key.trim();
    if( key.length() < 1 )
      return;

    int index = getIndex( key );

    if( lineArray[index] == null )
      lineArray[index] = new DefinesDictionaryLine();

    lineArray[index].setString( key, value );

    }
    catch( Exception e )
      {
      mApp.showStatus( "Exception in setString()." );
      mApp.showStatus( e.getMessage() );
      }
    }



  public String getString( String key )
    {
    if( key == null )
      return "";

    key = key.trim();
    if( key.length() < 1 )
      return "";

    int index = getIndex( key );
    if( lineArray[index] == null )
      return "";

    return lineArray[index].getString( key );
    }



  public void sort()
    {
    // This is a Library Sort mixed with a Bubble
    // Sort for each line in the array.
    for( int count = 0; count < keySize; count++ )
      {
      if( lineArray[count] == null )
        continue;

      lineArray[count].sort();
      }
    }



  public String makeKeysValuesString()
    {
    try
    {
    // mApp.showStatus( "Sorting..." );
    sort();
    // mApp.showStatus( "Finished sorting." );

    StringBuilder sBuilder = new StringBuilder();

    for( int count = 0; count < keySize; count++ )
      {
      if( lineArray[count] == null )
        continue;

      sBuilder.append( lineArray[count].
                            makeKeysValuesString());

      }

    return sBuilder.toString();

    }
    catch( Exception e )
      {
      mApp.showStatus( "Exception in DefinesDictionary.makeKeysValuesString():\n" );
      mApp.showStatus( e.getMessage() );
      return "";
      }
    }




  public boolean keyExists( String key )
    {
    if( key == null )
      return false;

    key = key.trim();
    if( key.length() < 1 )
      return false;

    int index = getIndex( key );
    if( lineArray[index] == null )
      return false;

    return lineArray[index].keyExists( key );
    }



  }
