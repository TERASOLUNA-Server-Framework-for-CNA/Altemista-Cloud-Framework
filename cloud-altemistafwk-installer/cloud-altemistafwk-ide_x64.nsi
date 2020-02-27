;--------------------------------
; Include Modern UI

	!include "MUI2.nsh"
	; !define MUI_COMPONENTSPAGE_NODESC ; No description area.
	!define MUI_COMPONENTSPAGE_SMALLDESC 

;--------------------------------
; Include TextReplace NSIS plugin v1.5

	!include "TextReplace.nsh" ; Fast search/replacement in text file
	
	!include "StrFunc.nsh" ; StrFunc provides some additional handy string functions
	${StrRep} ; Supportable for Install Sections and Functions
	
;--------------------------------
; General

	; The name of the installer
	Name "ALTEMISTA Cloud Framework"
	
	; The file to write
	OutFile "altemista_cloudfwk_3.0.0.RELEASE_x64.exe"

	; The default installation directory
	InstallDir C:\cloud-altemistafwk

	; Registry key to check for directory
	; (so if you install again, it will overwrite the old one automatically)
	InstallDirRegKey HKLM "Software\NSIS_altemista_cloudfwk" "Install_Dir"

	; Request application privileges for Windows Vista
	RequestExecutionLevel user
	
;--------------------------------
; Interface Settings

	!define MUI_ABORTWARNING

;--------------------------------
; Pages

	!insertmacro MUI_PAGE_COMPONENTS
	!insertmacro MUI_PAGE_DIRECTORY
	!insertmacro MUI_PAGE_INSTFILES

	!insertmacro MUI_UNPAGE_CONFIRM
	!insertmacro MUI_UNPAGE_INSTFILES
  
;--------------------------------
; Languages
 
	!insertmacro MUI_LANGUAGE "English"

;--------------------------------
; Macros and Functions

	!macro Const name value
		Var /GLOBAL _${name}
		StrCpy $_${name} ${value}
	!macroend
	
	!macro Unzip source
		!insertmacro UnzipTo ${source} $INSTDIR ""
	!macroend
	
	!macro UnzipTo source target args
		File /oname=$PLUGINSDIR\temp.zip ${source}
		IfFileExists ${target}\*.* +2
			CreateDirectory ${target}
		nsisunz::UnzipToLog ${args} "$PLUGINSDIR\temp.zip" "${target}"
		Pop $0
		StrCmp $0 "success" +2
			Abort "$0"
	!macroend

	; !macro DownloadAndUnzip source
		; !insertmacro DownloadAndUnzipTo ${source} $INSTDIR ""
	; !macroend
	
	; !macro DownloadAndUnzipTo source target args
		; inetc::get ${source} $PLUGINSDIR\temp.zip
		; IfFileExists ${target}\*.* +2
			; CreateDirectory ${target}
		; nsisunz::UnzipToLog ${args} "$PLUGINSDIR\temp.zip" "${target}"
		; Pop $0
		; StrCmp $0 "success" +2
			; Abort "$0"
	; !macroend
	
;--------------------------------
; The stuff to install

	InstType "Typical"
	InstType "Full"
	InstType "Minimal"
	
	Section "-Init"

		SectionIn RO

		; Set output path to the installation directory.
		SetOutPath "$INSTDIR"

		; Escaped version of $INSTDIR
		Var /GLOBAL escapedInstDir
		${StrRep} $escapedInstDir "$INSTDIR" "\" "\\"
		${StrRep} $escapedInstDir "$escapedInstDir" ":" "\:"

		; Initializes the plug-ins dir if not already initialized. 
		InitPluginsDir
		
	SectionEnd

	Section "-Start menu folder"

		SectionIn RO
		
		; Start menu folder
		CreateDirectory "$SMPROGRAMS\ALTEMISTA Cloud Framework"

	SectionEnd

	Section "!ADF IDE" idests

		SectionIn RO 1 2 3
		
		; Spring Tool Suite 3.8.3 based on Eclipse 4.6.2
		!insertmacro Unzip content_x86_64\spring-tool-suite-3.8.3.RELEASE-e4.6.2-win32-x86_64.zip
		!insertmacro Const eclipseFolder $INSTDIR\sts-bundle\sts-3.8.3.RELEASE
		
		; Altemista Cloudfwk plug-in
		!insertmacro Const adfpdropindir $_eclipseFolder\dropins\cloud-altemistafwk-plugin-3.0.0.RELEASE
		!insertmacro UnzipTo content\cloud-altemistafwk-plugin-3.0.0.RELEASE.zip $_adfpdropindir ""
		Delete "$_adfpdropindir\artifacts.jar"
		Delete "$_adfpdropindir\content.jar"
		
		; Embedded Maven configuration (1/2)
		!insertmacro Const settingsTarget $_eclipseFolder\settings.xml
		File /oname=$_settingsTarget content\settings.xml
		${textreplace::ReplaceInFile} "$_settingsTarget" "$_settingsTarget" "INSTDIR" "$INSTDIR" "" $0
		
		; Embedded Maven configuration (2/2)
		!insertmacro Const prefsTargetFolder $INSTDIR\workspace\.metadata\.plugins\org.eclipse.core.runtime\.settings
		!insertmacro Const prefsTarget $_prefsTargetFolder\org.eclipse.m2e.core.prefs
		File /r content\workspace-m2e\*.*
		Var /GLOBAL escapedSettingsTarget
		${StrRep} $escapedSettingsTarget "$_settingsTarget" "\" "\\"
		${StrRep} $escapedSettingsTarget "$escapedSettingsTarget" ":" "\:"
		${textreplace::ReplaceInFile} "$_prefsTarget" "$_prefsTarget" "SETTINGSFILEPATH" "$escapedSettingsTarget" "" $0

		; Start menu shortcut
		CreateShortcut "$SMPROGRAMS\ALTEMISTA Cloud Framework\ACF IDE.lnk" "$_eclipseFolder\STS.exe" "-data $INSTDIR\workspace"

		; Dekstop shortcut
		CreateShortcut "$DESKTOP\ADF+ IDE.lnk" "$_eclipseFolder\STS.exe" "-data $INSTDIR\workspace"
		
	SectionEnd

	Section "-Documentation folder"

		SectionIn RO
		
		; Documentation folder
		CreateDirectory $INSTDIR\documentation

	SectionEnd

	SectionGroup "Offline ADF Documentation" doc

		Section "!Current version (v3.0.0.RELEASE)" doccur
		
			SectionIn 1 2
			
			; HTML, unzipped
			!insertmacro UnzipTo target\dependency\cloud-altemistafwk-documentation-3.0.0.RELEASE-html.zip "$INSTDIR\documentation" ""

			; Start menu shortcuts
			CreateShortcut "$SMPROGRAMS\ALTEMISTA Cloud Framework \Getting Started with ACF.lnk" "$INSTDIR\documentation\cloud-altemistafwk-documentation-3.0.0.RELEASE\Getting Started with ADF.html"
			CreateShortcut "$SMPROGRAMS\ALTEMISTA Cloud Framework \ACF Reference Documentation.lnk" "$INSTDIR\documentation\cloud-altemistafwk-documentation-3.0.0.RELEASE\ADFplus Reference Documentation.html"

		SectionEnd
	
	SectionGroupEnd

	SectionGroup "Apache Maven" maven

		Section "Apache Maven 3.3.x" maven33

			SectionIn 1 2

			; Apache Maven 3.3
			!insertmacro Unzip content_x86\apache-maven-3.3.9-bin.zip
			
			; Apache Maven configuration
			File /oname=$INSTDIR\apache-maven-3.3.9\conf\settings.xml content\settings.xml
			${textreplace::ReplaceInFile} "$INSTDIR\apache-maven-3.3.9\conf\settings.xml" "$INSTDIR\apache-maven-3.3.9\conf\settings.xml" "INSTDIR" "$INSTDIR" "" $0

		SectionEnd

		; Section "Apache Maven 3.2.x (online)" maven32

			; SectionIn 2

			; ; Apache Maven 3.2
			; !insertmacro DownloadAndUnzip https://archive.apache.org/dist/maven/maven-3/3.2.5/binaries/apache-maven-3.2.5-bin.zip
			
			; ; Apache Maven configuration
			; File /oname=$INSTDIR\apache-maven-3.2.5\conf\settings.xml content\settings.xml
			; ${textreplace::ReplaceInFile} "$INSTDIR\apache-maven-3.2.5\conf\settings.xml" "$INSTDIR\apache-maven-3.2.5\conf\settings.xml" "INSTDIR" "$INSTDIR" "" $0

		; SectionEnd

	SectionGroupEnd

	SectionGroup "Apache Tomcat" tomcat

		; Section "Apache Tomcat 8.5.x (online)" tomcat85

			; SectionIn 2

			; ; Apache Tomcat 8.5.x
			; !insertmacro DownloadAndUnzip http://ftp.cixug.es/apache/tomcat/tomcat-8/v8.0.39/bin/apache-tomcat-8.0.39-windows-x64.zip
		  
		; SectionEnd

		Section "Apache Tomcat 8.0.x" tomcat8

			SectionIn 1 2

			; Apache Tomcat 8.0.x
			!insertmacro Unzip content_x86_64\apache-tomcat-8.5.50-windows-x64.zip
			
			; Workspace configuration (1/3)
			File /r content\workspace-tomcat8\*.*
			
			; Workspace configuration (2/3)
			Var /GLOBAL tomcatPrefsFolder
			StrCpy $tomcatPrefsFolder $INSTDIR\workspace\.metadata\.plugins\org.eclipse.core.runtime\.settings
			Var /GLOBAL tomcatPrefsTarget1
			StrCpy $tomcatPrefsTarget1 $tomcatPrefsFolder\org.eclipse.jst.server.tomcat.core.prefs
			${textreplace::ReplaceInFile} "$tomcatPrefsTarget1" "$tomcatPrefsTarget1" "INSTDIR" "$escapedInstDir\\apache-tomcat-8.0.39" "" $0
			
			; Workspace configuration (3/3)
			Var /GLOBAL tomcatPrefsTarget2
			StrCpy $tomcatPrefsTarget2 $tomcatPrefsFolder\org.eclipse.wst.server.core.prefs
			${textreplace::ReplaceInFile} "$tomcatPrefsTarget2" "$tomcatPrefsTarget2" "INSTDIR" "$escapedInstDir\\apache-tomcat-8.0.39" "" $0
		  
		SectionEnd

		; Section "Apache Tomcat 7.0.x (online)" tomcat7

			; SectionIn 2

			; ; Apache Tomcat 7.0.x
			; !insertmacro DownloadAndUnzip http://apache.rediris.es/tomcat/tomcat-7/v7.0.70/bin/apache-tomcat-7.0.70-windows-x64.zip
		  
		; SectionEnd

	SectionGroupEnd

	; SectionGroup "Java SE Development Kit" jdk

		; Section "JDK 8" jdk8

			; SectionIn 1 2

			; ; JDK 8
			; !insertmacro Unzip content_x86_64\jdk-8u121-x64.zip
			
			; ; Java Cryptography Extension (JCE) Unlimited Strength
			; !insertmacro UnzipTo content_x86\jce_policy-8.zip $INSTDIR\jdk-8u121-x64\lib\security "/noextractpath /file UnlimitedJCEPolicyJDK8\local_policy.jar"
			
			; ; IDE configuration (-vm option in STS.ini) NOT WORKING
			; ; !insertmacro Const stsIniTarget $_eclipseFolder\STS.ini
			; ; File /oname=$_stsIniTarget content\STS.ini
			; ; ${textreplace::ReplaceInFile} "$_stsIniTarget" "$_stsIniTarget" "INSTDIR" "$INSTDIR\jdk-8u121-x64" "" $0

		; SectionEnd

		; Section "JDK 7" jdk7

			; SectionIn 2

			; ; JDK 7
			; !insertmacro Unzip content_x86_64\jdk-7u80-x64.zip
			
			; ; Java Cryptography Extension (JCE) Unlimited Strength
			; !insertmacro UnzipTo content_x86\UnlimitedJCEPolicyJDK7.zip $INSTDIR\jdk-7u80-x64\lib\security "/noextractpath /file UnlimitedJCEPolicy\local_policy.jar"

		; SectionEnd

	; SectionGroupEnd

	Section "-Clean workspace backup"

		; Clean workspace backup
		File /oname=$PLUGINSDIR\7z1900-x64.exe content_x86_64\7z1900-x64.exe
		ExecWait '"$PLUGINSDIR\7z1900-x64.exe" a $INSTDIR\cleanWorkspace.7z $INSTDIR\workspace'
	
	SectionEnd
	
	Section "-Registry and uninstall keys"

		SectionIn RO
		
		; Write the installation path into the registry
		WriteRegStr HKLM SOFTWARE\NSIS_altemista_cloudfwk "Install_Dir" "$INSTDIR"

		; Write the uninstall keys for Windows
		WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\NSIS_altemista_cloudfwk" "DisplayName" "ALTEMISTA Cloud Framework (remove only)"
		WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\NSIS_altemista_cloudfwk" "UninstallString" '"$INSTDIR\uninstall.exe"'
		WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\NSIS_altemista_cloudfwk" "NoModify" 1
		WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\NSIS_altemista_cloudfwk" "NoRepair" 1
		WriteUninstaller "uninstall.exe"

		; Start menu shortcut
		CreateShortcut "$SMPROGRAMS\ALTEMISTA Cloud Framework\Uninstall.lnk" "$INSTDIR\uninstall.exe"
		
	SectionEnd

;--------------------------------
; Modern UI descriptions

	LangString DESC_idests ${LANG_ENGLISH} "Spring Tool Suite (v3.8.3 based on Eclipse v4.6.2) with ADF plugin and proper embedded Maven settings"
	LangString DESC_doc ${LANG_ENGLISH} "Offline ADF Documentation"
	LangString DESC_doccur ${LANG_ENGLISH} "v3.0.0.RELEASE (Current version)"
	LangString DESC_doc110 ${LANG_ENGLISH} "v1.1.0-RELEASE (Archived)"
	LangString DESC_doc100 ${LANG_ENGLISH} "v1.0.0.RELEASE (Archived)"
	; LangString DESC_doc010 ${LANG_ENGLISH} "v0.1.0.RELEASE (Archived)"
	LangString DESC_maven ${LANG_ENGLISH} "Standalone installations of Apache Maven"
	LangString DESC_maven33 ${LANG_ENGLISH} "Apache Maven (v3.3.9) with proper settings"
	; LangString DESC_maven32 ${LANG_ENGLISH} "Apache Maven (v3.2.5) with proper settings"
	LangString DESC_tomcat ${LANG_ENGLISH} "Standalone installations of Apache Tomcat"
	; LangString DESC_tomcat85 ${LANG_ENGLISH} "Apache Tomcat 8.5.x (v8.5.4)"
	LangString DESC_tomcat8 ${LANG_ENGLISH} "Apache Tomcat 8.0.x (v8.5.34)"
	; LangString DESC_tomcat7 ${LANG_ENGLISH} "Apache Tomcat 7.0.x (v7.0.70)"
	; LangString DESC_jdk ${LANG_ENGLISH} "Standalone installations of Java SE Development Kit"
	; LangString DESC_jdk8 ${LANG_ENGLISH} "Java SE Development Kit 8 patched with Java Cryptography Extension (JCE) Unlimited Strength"
	; LangString DESC_jdk7 ${LANG_ENGLISH} "Java SE Development Kit 7 patched with Java Cryptography Extension (JCE) Unlimited Strength"

	!insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
		!insertmacro MUI_DESCRIPTION_TEXT ${ide} $(DESC_ide)
		!insertmacro MUI_DESCRIPTION_TEXT ${idests} $(DESC_idests)
		!insertmacro MUI_DESCRIPTION_TEXT ${doc} $(DESC_doc)
		!insertmacro MUI_DESCRIPTION_TEXT ${doccur} $(DESC_doccur)
		!insertmacro MUI_DESCRIPTION_TEXT ${doc110} $(DESC_doc110)
		!insertmacro MUI_DESCRIPTION_TEXT ${doc100} $(DESC_doc100)
		!insertmacro MUI_DESCRIPTION_TEXT ${maven} $(DESC_maven)
		!insertmacro MUI_DESCRIPTION_TEXT ${maven33} $(DESC_maven33)
		; !insertmacro MUI_DESCRIPTION_TEXT ${maven32} $(DESC_maven32)
		!insertmacro MUI_DESCRIPTION_TEXT ${tomcat} $(DESC_tomcat)
		; !insertmacro MUI_DESCRIPTION_TEXT ${tomcat85} $(DESC_tomcat85)
		!insertmacro MUI_DESCRIPTION_TEXT ${tomcat8} $(DESC_tomcat8)
		; !insertmacro MUI_DESCRIPTION_TEXT ${tomcat7} $(DESC_tomcat7)
		; !insertmacro MUI_DESCRIPTION_TEXT ${jdk} $(DESC_jdk)
		; !insertmacro MUI_DESCRIPTION_TEXT ${jdk8} $(DESC_jdk8)
		; !insertmacro MUI_DESCRIPTION_TEXT ${jdk7} $(DESC_jdk7)
	!insertmacro MUI_FUNCTION_DESCRIPTION_END

;--------------------------------
; Uninstaller

	Section "Uninstall"
	  
	  ; Remove registry keys
	  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\NSIS_altemista_cloudfwk"
	  DeleteRegKey HKLM SOFTWARE\NSIS_altemista_cloudfwk
	  
	  ; Remove shortcuts, if any
	  Delete "$SMPROGRAMS\ALTEMISTA Cloud Framework\*.*"
	  RMDir "$SMPROGRAMS\ALTEMISTA Cloud Framework"
	  Delete "$DESKTOP\ACF IDE.lnk"
	  
	  ; Remove directories used
	  RMDir /r "$INSTDIR"
	  
	SectionEnd
