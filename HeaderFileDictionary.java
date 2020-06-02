// Copyright Eric Chauvin 2020.



public class HeaderFileDictionary
  {
  private MainApp mApp;
  private String[] fileListArray;
  private HeaderFileDictionaryLine lineArray[];
  private final int maxIndexLetter = 'z' - 'a';
  private final int keySize = ((maxIndexLetter << 6) |
                                  maxIndexLetter) + 1;



  private HeaderFileDictionary()
    {
    }



  public HeaderFileDictionary( MainApp useApp )
    {
    mApp = useApp;

    lineArray = new HeaderFileDictionaryLine[keySize];
    }



  public void readFileList( String fileName )
    {
    // false is Keep Tabs.
    String fileS = FileUtility.readFileToString(
                                        mApp,
                                        fileName,
                                        false );

    if( fileS.length() == 0 )
      return;

    fileListArray = fileS.split( "\n" );
    }



  public String getFileFromList( String key )
    {
    String result = "";

    key = key.trim().toLowerCase();
    int last = fileListArray.length;
    int matches = 0;
    for( int count = 0; count < last; count++ )
      {
      String line = fileListArray[count].trim();
      line = line.toLowerCase();
      // mApp.showStatusAsync( "Line: " + line );
      if( line.endsWith( key ))
        {
        result = line;
        // mApp.showStatusAsync( "Found: " + result );        
        matches++;
        }
      }

    if( matches > 1 )
      {
      String showS = "Duplicates for key: " +
                          key + "\n";
                          
      mApp.showStatusAsync( showS );
      return "";
      }

    return result;
    }



  public void clear()
    {
    for( int count = 0; count < keySize; count++ )
      lineArray[count] = null;

    }



  private int letterToIndexNumber( char letter )
    {
    // This is case-insensitive so that it sorts
    // names in a case-insensitive way.

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



  public void setValue( String key, String value )
    {
    try
    {
    if( key == null )
      return;

    key = key.trim();
    if( key.length() < 1 )
      return;

    // if( isBadKey( key ))

    int index = getIndex( key );

    if( lineArray[index] == null )
      lineArray[index] = new HeaderFileDictionaryLine();

    lineArray[index].setValue( key, value );
    }
    catch( Exception e )
      {
      mApp.showStatusAsync( "Exception in setValue()." );
      mApp.showStatusAsync( e.getMessage() );
      }
    }



  public String getValue( String key )
    {
    if( key == null )
      return "";

    key = key.trim();
    if( key.length() < 1 )
      return "";

    int index = getIndex( key );
    if( lineArray[index] == null )
      return "";

    return lineArray[index].getValue( key );
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
    sort();

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
      mApp.showStatusAsync( "Exception in HeaderFilesDictionary.makeKeysValuesString():\n" );
      mApp.showStatusAsync( e.getMessage() );
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
