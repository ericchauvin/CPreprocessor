// Copyright Eric Chauvin 2020.



public class BoolLevelArray
  {
  private MainApp mApp;
  private boolean[] boolArray;
  private int boolArrayLast;


  private BoolLevelArray()
    {
    }



  public BoolLevelArray( MainApp useApp )
    {
    mApp = useApp;
    boolArray = new boolean[2];
    boolArrayLast = 1;
    boolArray[0] = true; // Notice this one at zero 
                         // can't be changed because
                         // the bottom level is
                         // always true.
    }



  private void resizeArray( int toAdd )
    {
    int oldLength = boolArray.length;

    boolean[] tempBoolArray = new boolean[oldLength + toAdd];
    for( int count = 0; count < boolArrayLast; count++ )
      {
      tempBoolArray[count] = boolArray[count];
      }

    boolArray = tempBoolArray;
    }



  public int getLevel()
    {
    return boolArrayLast - 1;
    }



  public boolean getValueAt( int where )
    {
    if( where < 0 )
      return false;

    if( where >= boolArrayLast )
      return false;

    return boolArray[where];
    }



  public boolean getCurrentValue()
    {
    /////////////////////////
    // Test code:
    // Verify that there are no false values above
    // a true value.
    boolean testVal = boolArray[boolArrayLast - 1];
    if( testVal )
      {
      for( int count = 0; count < boolArrayLast - 1; count++ )
        {
        if( !boolArray[count] )
          {
          mApp.showStatusAsync( "The test for getCurrentValue() failed." );
          return false;
          }
        }
      }
    ////////////////////////

    return boolArray[boolArrayLast - 1];
    }



  public boolean setCurrentValue( boolean setTo )
    {
    // It is always true at the zero level.
    if( boolArrayLast == 1 )
      {
      if( !setTo )
        return false; // error 

      return true;
      }
     
    boolArray[boolArrayLast - 1] = setTo;
    return true;
    }



  public boolean subtractLevel()
    {
    if( boolArrayLast <= 1 )
      {
      mApp.showStatusAsync( "SubtractLevel() can't go here." );
      return false;
      }

    boolArrayLast--;
    return true;
    }



  public void addNewLevel( boolean setTo )
    {
    if( boolArrayLast >= boolArray.length )
      resizeArray( 64 );

    boolArray[boolArrayLast] = setTo;
    boolArrayLast++;
    }



  }
