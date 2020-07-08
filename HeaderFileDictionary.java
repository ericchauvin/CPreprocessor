// Copyright Eric Chauvin 2020.



public class HeaderFileDictionary
  {
  private MainApp mApp;
  private StrArray fileListArray;
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



  public void readFileList( StrA fileName )
    {
    StrA fileS = FileUtility.readFileToStrA(
                                        mApp,
                                        fileName,
                                        false,
                                        false );

    if( fileS.length() == 0 )
      {
      fileListArray = new StrArray();
      return;
      }

    fileListArray = fileS.splitChar( '\n' );
    }



  private StrA getFileFromList( StrA key )
    {
    if( fileListArray == null )
      return StrA.Empty;

    StrA result = StrA.Empty;

    key = key.trim().toLowerCase();
    if( !key.startsWithChar( '\\' ))
      key = new StrA( new StrA( "\\" ), key );

    final int last = fileListArray.length();
    int matches = 0;
    for( int count = 0; count < last; count++ )
      {
      StrA line = fileListArray.getStrAt( count ).
                                              trim();
      line = line.toLowerCase();
      // mApp.showStatusAsync( "Line: " + line );
      if( line.endsWith( key ))
        {
        result = line;
        // mApp.showStatusAsync( "Found: " + result );        
        setValue( key, result );
        matches++;
        }
      }

    if( matches > 1 )
      {
      // stdio.h includes sys/stdio.h.
      // So there are two keys:
      // stdio.h and sys/stdio.h

      String showS = "Duplicates for key: " +
                          key + "\n";
                          
      mApp.showStatusAsync( showS );
      return StrA.Empty;
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




  private int getIndex( StrA key )
    {
    // This index needs to be in sorted order.

    int keyLength = key.length();
    if( keyLength < 1 )
      return 0;

    // The letter z by itself would be sorted before
    // the two letters az unless the space character
    // is added.
    if( keyLength == 1 )
      key = key.concat( new StrA( " " ));

    int one = letterToIndexNumber( key.charAt( 0 ));
    int tempTwo = letterToIndexNumber( key.charAt( 1 ));
    int two = (one << 6) | tempTwo;

    if( two >= keySize )
      two = keySize - 1;

    return two;
    }



  public void setValue( StrA key, StrA value )
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



  public StrA getValue( StrA key )
    {
    if( key == null )
      return StrA.Empty;

    key = key.trim();
    if( key.length() < 1 )
      return StrA.Empty;

    int index = getIndex( key );
    if( lineArray[index] == null )
      return getFileFromList( key );

    StrA result = lineArray[index].getValue( key );
    if( result.length() == 0 )
      return getFileFromList( key );

    return result;
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



  public StrA makeKeysValuesStrA()
    {
    try
    {
    sort();

    StrABld sBuilder = new StrABld( 1024 * 64 );

    for( int count = 0; count < keySize; count++ )
      {
      if( lineArray[count] == null )
        continue;

      sBuilder.appendStrA( lineArray[count].
                            makeKeysValuesStrA());

      }

    return sBuilder.toStrA();
    }
    catch( Exception e )
      {
      mApp.showStatusAsync( "Exception in HeaderFilesDictionary.makeKeysValuesString():\n" );
      mApp.showStatusAsync( e.getMessage() );
      return StrA.Empty;
      }
    }


  public void writeFile( StrA fileName )
    {
    StrA toWrite = makeKeysValuesStrA();

    FileUtility.writeStrAToFile( mApp,
                                   fileName,
                                   toWrite,
                                   false,
                                   false );
    }



  public void readFile( StrA fileName )
    {
/*
Testing
    StrA fileList = mApp.getProgramDirectory();
    fileList = fileList.concat( new StrA(
                                 "\\FileList.txt" ));

    readFileList( fileList );
*/

    StrA fileS = FileUtility.readFileToStrA(
                                        mApp,
                                        fileName,
                                        false,
                                        false );

    if( fileS.length() == 0 )
      return;
   
    StrArray fileLines = fileS.splitChar( '\n' );
    final int last = fileLines.length();
    for( int count = 0; count < last; count++ )
      {
      StrA line = fileLines.getStrAt( count );
      // mApp.showStatusAsync( line );
      StrArray parts = line.splitChar( ';' );
      if( parts.length() < 2 )
        {
        mApp.showStatusAsync( "This line has less than two parts.\n" + line );
        }

      setValue( parts.getStrAt( 0 ), parts.getStrAt( 1 ));
      }
    }



  public boolean keyExists( StrA key )
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
