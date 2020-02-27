# TSF+ Eclipse plug-in quick reference


## Refreshing configuration or core changes in the plug-in

Steps:
* `mvn --file cloud-altemistafwk-plugin-config\pom.xml clean install`
* `mvn --file cloud-altemistafwk-plugin-core\pom.xml clean install`
* `mvn --file cloud-altemistafwk-plugin-eclipse\pom.xml clean dependency:copy-dependencies`

(or use `mvnall.bat` that will do these three steps for you)


## Running the plug-in (inside Eclipse)

Steps (over the `terasoluna-plus-plugin-eclipse` project):
* _Refresh_ (F5)
* _Build All_ (Ctrl+B)
* _Run As_ > _Eclipse Application_ (Alt+Shift+X, E)


## Exporting the plug-in

Steps  (over `terasoluna-plus-plugin-eclipse`):
* Right click, _Export..._
* _Plug-in Development_ > _Deployable features_
* In the _Available Features_ list:
	* [X] `terasolunaPlusPlugin (<version>)`
* Under the _Destination_ tab:
	* [X] _Archive file_
* Under the _Options_ tab:
	* [X] _Package as individual JAR archives (...)_ 
* Click _Finish_
