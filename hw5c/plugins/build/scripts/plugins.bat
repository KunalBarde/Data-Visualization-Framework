@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  plugins startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and PLUGINS_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\plugins.jar;%APP_HOME%\lib\framework.jar;%APP_HOME%\lib\gson-2.8.5.jar;%APP_HOME%\lib\xchart-3.5.4.jar;%APP_HOME%\lib\jsoup-1.11.3.jar;%APP_HOME%\lib\poi-ooxml-4.0.1.jar;%APP_HOME%\lib\poi-4.0.1.jar;%APP_HOME%\lib\jfreechart-1.5.0.jar;%APP_HOME%\lib\junit-4.12.jar;%APP_HOME%\lib\core-1.47.1.jar;%APP_HOME%\lib\VectorGraphics2D-0.13.jar;%APP_HOME%\lib\google-oauth-client-jetty-1.11.0-beta.jar;%APP_HOME%\lib\google-oauth-client-java6-1.11.0-beta.jar;%APP_HOME%\lib\google-oauth-client-1.11.0-beta.jar;%APP_HOME%\lib\google-http-client-1.11.0-beta.jar;%APP_HOME%\lib\httpclient-4.0.3.jar;%APP_HOME%\lib\commons-codec-1.11.jar;%APP_HOME%\lib\commons-collections4-4.2.jar;%APP_HOME%\lib\commons-math3-3.6.1.jar;%APP_HOME%\lib\poi-ooxml-schemas-4.0.1.jar;%APP_HOME%\lib\commons-compress-1.18.jar;%APP_HOME%\lib\curvesapi-1.05.jar;%APP_HOME%\lib\hamcrest-core-1.3.jar;%APP_HOME%\lib\guava-13.0.1.jar;%APP_HOME%\lib\jsr305-1.3.9.jar;%APP_HOME%\lib\mail-1.4.jar;%APP_HOME%\lib\xmlbeans-3.0.2.jar;%APP_HOME%\lib\jetty-6.1.26.jar;%APP_HOME%\lib\activation-1.1.jar;%APP_HOME%\lib\jetty-util-6.1.26.jar;%APP_HOME%\lib\servlet-api-2.5-20081211.jar;%APP_HOME%\lib\xpp3-1.1.4c.jar;%APP_HOME%\lib\httpcore-4.0.1.jar;%APP_HOME%\lib\commons-logging-1.1.1.jar

@rem Execute plugins
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %PLUGINS_OPTS%  -classpath "%CLASSPATH%" edu.cmu.cs.cs214.hw5.Main %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable PLUGINS_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%PLUGINS_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
