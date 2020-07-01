// Copyright Eric Chauvin 2020.



public class HeaderFileDictionaryLine
  {
  private StrA[] keyArray;
  private StrA[] valueArray;
  private int[] sortIndexArray;
  private int arrayLast = 0;



  public HeaderFileDictionaryLine()
    {
    keyArray = new StrA[8];
    valueArray = new StrA[8];
    sortIndexArray = new int[8];
    resetSortIndexArray();
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
    int max = sortIndexArray.length;
    sortIndexArray = new int[max + toAdd];
    resetSortIndexArray();

    StrA[] tempKeyArray = new StrA[max + toAdd];
    StrA[] tempValueArray = new StrA[max + toAdd];

    for( int count = 0; count < arrayLast; count++ )
      {
      tempKeyArray[count] = keyArray[count];
      tempValueArray[count] = valueArray[count];
      }

    keyArray = tempKeyArray;
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
      if( keyArray[count].compareToIgnoreCase(
                              keyArray[count + 1] ) > 0 )
        {
        int temp = sortIndexArray[count];
        sortIndexArray[count] = sortIndexArray[count + 1];
        sortIndexArray[count + 1] = temp;
        switched = true;
        }
      }

    return switched;
    }



  private int getIndexOfKey( StrA key )
    {
    if( arrayLast < 1 )
      return -1;

    for( int count = 0; count < arrayLast; count++ )
      {
      if( keyArray[count].equals( key ))
        return count;

      }

    return -1;
    }



  public void setValue( StrA key, StrA value )
    {
    int index = getIndexOfKey( key );
    if( index >= 0 )
      {
      // valueArray[index] += ";" + value;
      valueArray[arrayLast] = value;
      }
    else
      {
      if( arrayLast >= sortIndexArray.length )
        resizeArrays( 1024 * 4 );

      keyArray[arrayLast] = key;
      valueArray[arrayLast] = value;
      arrayLast++;
      }
    }



  public StrA getValue( StrA key )
    {
    int index = getIndexOfKey( key );
    if( index < 0 )
      return new StrA( "" );

    return valueArray[index];
    }




  public boolean keyExists( StrA key )
    {
    int index = getIndexOfKey( key );
    if( index < 0 )
      return false;

    return true;
    }




  public StrA makeKeysValuesStrA()
    {
    if( arrayLast < 1 )
      return new StrA( "" );

    StrABld sBuilder = new StrABld( 1024 * 32 );

    for( int count = 0; count < arrayLast; count++ )
      {
      // Using the sortIndexArray for the sorted order.
      StrA oneLine = new StrA( 
              keyArray[sortIndexArray[count]] );

      oneLine = oneLine.concat( new StrA( ";" ));
      oneLine = oneLine.concat( 
                valueArray[sortIndexArray[count]] );
      oneLine = oneLine.concat( new StrA( "\n" ));

      sBuilder.appendStrA( oneLine );
      }

    return sBuilder.toStrA();
    }



  }
