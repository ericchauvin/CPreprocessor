// Copyright Eric Chauvin 2020.



public class CodeBlockDictionary
  {
  private MainApp mApp;
  private CodeBlockDictionaryLine lineArray[];
  private int nextCodeBlock = 0;
  private final int keySize = 1000;



  private CodeBlockDictionary()
    {
    }



  public CodeBlockDictionary( MainApp useApp )
    {
    mApp = useApp;
    lineArray = new CodeBlockDictionaryLine[keySize];
    }


  private int getNextCodeBlock()
    {
    nextCodeBlock++;
    return nextCodeBlock;
    }



  public void clear()
    {
    nextCodeBlock = 0;
    for( int count = 0; count < keySize; count++ )
      lineArray[count] = null;

    }



  private int getIndex( int key )
    {
    return key % keySize;
    }



  public int setValue( StrA value )
    {
    try
    {
    int key = getNextCodeBlock();
    int index = getIndex( key );

    if( lineArray[index] == null )
      lineArray[index] = new CodeBlockDictionaryLine();

    lineArray[index].setValue( key, value );
    return key;
    }
    catch( Exception e )
      {
      mApp.showStatusAsync( "Exception in setValue()." );
      mApp.showStatusAsync( e.getMessage() );
      return -1;
      }
    }



  public StrA getValue( int key )
    {
    int index = getIndex( key );
    if( lineArray[index] == null )
      return StrA.Empty;

    StrA result = lineArray[index].getValue( key );
    return result;
    }


/*
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
*/


/*
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
*/

/*
  public void writeFile( StrA fileName )
    {
    StrA toWrite = makeKeysValuesStrA();

    FileUtility.writeStrAToFile( mApp,
                                   fileName,
                                   toWrite,
                                   false,
                                   false );
    }
*/


/*
  public void readFile( StrA fileName )
    {
    StrA fileList = mApp.getProgramDirectory();
    fileList = fileList.concat( new StrA(
                                 "\\FileList.txt" ));

    readFileList( fileList );

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
*/


  public boolean keyExists( int key )
    {
    int index = getIndex( key );
    if( lineArray[index] == null )
      return false;

    return lineArray[index].keyExists( key );
    }




  }
