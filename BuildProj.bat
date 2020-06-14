rem @echo off

rem Compile Java files.

SET JAVA_HOME="C:\Javajdk"
SET JDK_HOME=%JAVA_HOME%
rem SET JRE_HOME="C:\Javajdk\jre"

SET CLASSPATH=C:\Javajdk\lib;C:\Javajdk\jre\lib;

SET PATH=%PATH%;%JAVA_HOME%\bin;

rem Compile all of them.
cd \Eric\CPreprocessor
del *.class


rem Make something to build these separately.
rem Like system( "javac whatever" ).

rem Javac -Xlint -Xstdout JavaBuild.log MainApp.java EditorTabPage.java LayoutSimpleVertical.java FileUtility.java StringsUtility.java MenuActions.java ConfigureFile.java StringDictionary.java StringDictionaryLine.java FileTree.java

rem cls
Javac -Xlint -Xstdout Build.log *.java

rem type Build.log

rem pause
