# OimTool
Oracle Identity Manager (OIM) tool.

## Usage
`oimtool.cmd COMMAND ARG(s)` or `oim<COMMAND>.cmd ARG(S)`

Multiple objects can be specified:
- in the command line: `oimtool.cmd COMMAND ARG1 ARG2 ...`
- or in a file: `oimtool.cmd COMMAND -f FILE` (each line in file represents a separate argument)

Available commands:
*) `itresparams ITRESNAME` - output parameters of IT-resource ITRESNAME.
*) `categories` - output exportable categories names.
*) `export1 OBJSPEC(S)` - export objects to one file. OBJSPEC is "Category:Name", e.g. "Lookup:Lookup.SAPHCM.OM.ReconAttrMap".
*) `export2 OBJSPEC(S)` - export objects to separate files.
*) `import DEPLOYMENTFILE(s)` - import objects from deployment file(s). **FIXME: Dependencies resolution does not work yet**
*) `register ZIPFILE(S)` - register plugin(s) from zip-archives.
*) `unregister PLUGINSPEC` - unregister plugin(s), where PLUGINSPEC is "PluginClassName:Version", e.g. "org.oim.test.PluginClass:1.0".
*) `uploadjars JAR` - upload jar(s).
*) `updatejars JAR` - update jar(s).
*) `deletejars JAR` - delete jar(s).
*) `purge OBJ(s)` - PurgeCache for specified object(s).
*) `purgeall` - PurgeCache All.
