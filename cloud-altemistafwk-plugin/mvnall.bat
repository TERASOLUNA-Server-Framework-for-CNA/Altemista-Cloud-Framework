@echo off

if not "%M2_HOME%"=="" goto m2HomeSet
echo M2_HOME is not set
goto end

:m2HomeSet
if exist "%M2_HOME%\bin\mvn.bat" goto runMvn
if exist "%M2_HOME%\bin\mvn.cmd" goto runMvn
echo M2_HOME does not point to a valid maven location
set M2_HOME
goto end

:runMvn
setlocal
REM set OPTIONS=--offline -P!update-license,!update-copyright,!verify-copyright,!with-sources,!with-javadoc,!with-asciidoc
set OPTIONS=-P!update-license,!update-copyright,!verify-copyright,!with-sources,!with-javadoc,!with-asciidoc
set TARGETS=clean install
if "%*"=="" goto targetsOk
set TARGETS=%*

:targetsOk
cls
call %M2_HOME%\bin\mvn %OPTIONS% --file cloud-altemistafwk-plugin-config\pom.xml %TARGETS% -s C:\terasoluna-plus\apache-maven-3.3.9\conf\settings.xml
if not %ERRORLEVEL% == 0 (
	exit /b %ERRORLEVEL%
)

call %M2_HOME%\bin\mvn %OPTIONS% --file cloud-altemistafwk-plugin-core\pom.xml %TARGETS% -s C:\terasoluna-plus\apache-maven-3.3.9\conf\settings.xml
if not %ERRORLEVEL% == 0 (
	exit /b %ERRORLEVEL%
)

if not "%*"=="" goto end
call %M2_HOME%\bin\mvn %OPTIONS% --file cloud-altemistafwk-plugin-eclipse\pom.xml clean dependency:copy-dependencies -s C:\terasoluna-plus\apache-maven-3.3.9\conf\settings.xml
if not %ERRORLEVEL% == 0 (
	exit /b %ERRORLEVEL%
)

:end
