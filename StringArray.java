// Copyright Eric Chauvin 2020.



public class StringArray
  {
  private String[] valueArray;
  private int[] sortIndexArray;
  private int arrayLast = 0;
  private StringBuilder sBuilder;


  public StringArray()
    {
    sBuilder = new StringBuilder();
    valueArray = new String[8];
    sortIndexArray = new int[8];
    resetSortIndexArray();
    }


  public void clear()
    {
    arrayLast = 0;
    }



  public int length()
    {
    return arrayLast;
    }


  private void resetSortIndexArray()
    {
    // It's not to arrayLast.  It's to the whole length.
    int max = sortIndexArray.length;
    for( int count = 0; count < max; count++ )
      sortIndexArray[count] = count;

    }



  private void resizeArrays( int toAdd )
    {
    int oldLength = sortIndexArray.length;
    sortIndexArray = new int[oldLength + toAdd];
    resetSortIndexArray();

    String[] tempValueArray = new String[oldLength + toAdd];
    for( int count = 0; count < arrayLast; count++ )
      {
      tempValueArray[count] = valueArray[count];
      }

    valueArray = tempValueArray;
    }



  public void sort()
    {
    if( arrayLast < 2 )
      return;

    for( int count = 0; count < arrayLast; count++ )
      {
      if( !bubbleSortOnePass() )
        break;

      }
    }



  private boolean bubbleSortOnePass()
    {
    // This returns true if it swaps anything.

    boolean switched = false;
    for( int count = 0; count < (arrayLast - 1); count++ )
      {
      // compareTo() uses case.
      if( valueArray[count].compareToIgnoreCase(
                        valueArray[count + 1] ) > 0 )
        {
        int temp = sortIndexArray[count];
        sortIndexArray[count] = sortIndexArray[count + 1];
        sortIndexArray[count + 1] = temp;
        switched = true;
        }
      }

    return switched;
    }



  public void appendString( String value )
    {
    if( arrayLast >= sortIndexArray.length )
      resizeArrays( 256 );

    valueArray[arrayLast] = value;
    arrayLast++;
    }



  public int makeFieldsFromString( String in,
                                   char delimit )
    {
    clear();
    sBuilder.setLength( 0 );

    if( in == null )
      return 0;

    int max = in.length();
    if( max == 0 )
      return 0;

    for( int count = 0; count < max; count++ )
      {
      char testChar = in.charAt( count );
      if( testChar == delimit )
        {
        appendString( sBuilder.toString());
        sBuilder.setLength( 0 );
        }
      else
        {
        sBuilder.append( testChar );
        }
      }

    if( sBuilder.length() > 0 )
      {
      appendString( sBuilder.toString());
      }

    return arrayLast;
    }


  public String getStringAt( int where )
    {
    if( where < 0 )
      return "";

    if( where >= arrayLast )
      return "";

    return valueArray[where];
    }


  }
