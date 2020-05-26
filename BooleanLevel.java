// Copyright Eric Chauvin 2020.



public class BooleanLevel
  {
  private MainApp mApp;
  private int currentLevel = 0;
  private boolean value = true;
   


  private BooleanLevel()
    {
    }


  public BooleanLevel( MainApp useApp )
    {
    mApp = useApp;
    }



  public int getCurrentLevel()
    {
    return currentLevel;
    }



  public boolean getValue()
    {
    return value;
    }



  public void setLevel( String command )
    {
    if( command.equals( "if" ))
      {
      currentLevel++;
      return;
      }

    if( command.equals( "ifdef" ))
      {
      currentLevel++;
      return;
      }

    if( command.equals( "ifndef" ))
      {
      currentLevel++;
      return;
      }

    if( command.equals( "endif" ))
      {
      currentLevel--;
      return;
      }

    // else and elseif...
    }



  }

