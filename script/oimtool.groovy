import groovy.transform.SourceURI
import java.nio.file.Paths
import java.util.Hashtable
import java.util.HashSet
import Thor.API.Exceptions.*
import Thor.API.Operations.tcExportOperationsIntf
import Thor.API.Operations.tcITResourceInstanceOperationsIntf
import Thor.API.Operations.tcImportOperationsIntf
import Thor.API.Operations.tcOrganizationOperationsIntf
import Thor.API.tcResultSet
import com.thortech.xl.vo.ddm.RootObject
import oracle.iam.identity.utils.Constants
import oracle.iam.platform.OIMClient
import oracle.iam.platformservice.api.PlatformService
import oracle.iam.platformservice.api.PlatformUtilsService
import oracle.iam.platformservice.vo.JarElement

@SourceURI
URI scriptUri

class OimTool {
    private client

    OimTool(url, user, password) {
        def env = new Hashtable()
        env.put(OIMClient.JAVA_NAMING_FACTORY_INITIAL, OIMClient.WLS_CONTEXT_FACTORY)
        env.put(OIMClient.JAVA_NAMING_PROVIDER_URL, url)
        client = new OIMClient(env)
        client.login(user, password.toCharArray())
    }

    def close() {
        client.logout()
    }

    def itresparams(args) {
        System.err.println("Parameters of ITResource '${args[0]}':")
        def svc = client.getService(tcITResourceInstanceOperationsIntf.class)
        def key = getItResourceKey(svc, args[0])
        def params = getItResourceParameters(svc, key)
        params.each { k, v -> println("$k = $v") }
    }

    private long getItResourceKey(svc, itResourceName) {
        def res = svc.findITResourceInstances(Collections.singletonMap(Constants.IT_RESOURCE_NAME, itResourceName))
        if (res.isEmpty()) {
            throw new tcITResourceNotFoundException("ITResource $itResourceName not wound")
        }
        res.goToRow(0)
        return Long.parseLong(res.getStringValue(Constants.IT_RESOURCE_KEY))
    }

    private Map<String, String> getItResourceParameters(svc, itResKey) {
        def res = svc.getITResourceInstanceParameters(itResKey)
        def params = [:] as Map
        int count = res.getRowCount()
        for (int i = 0; i < count; i++) {
            res.goToRow(i)
            def name = res.getStringValue(Constants.IT_RESOURCE_TYPE_PARAMETER_NAME)
            //def value = res.getStringValue("IT Resource.Parameter.Value")
            def value = res.getStringValue(Constants.IT_RESOURCE_TYPE_PARAMETER_VALUE)
            params[name] = value
        }
        return params
    }

    def categories() {
        System.err.println('Categories')
        def svc = client.getService(tcExportOperationsIntf.class)
        svc.retrieveCategories().each {
            println(it)
        }
    }

    def export1(args) {
        System.err.println('Export1')
        def svc = client.getService(tcExportOperationsIntf.class)
        def roots = []
        args.each { objspec ->
            def (type, mask) = objspec.split(':')
            System.err.println("  ? $type:$mask")
            def objs = svc.findObjects(type, mask)
            objs.each { obj -> System.err.println("  > $type:$obj.name") }
            objs += svc.retrieveChildren(objs)
            //objs += svc.retrieveDependencyTree(objs)
            objs = svc.arrangeSelectionAsATree(objs)
            roots += objs
        }
        def xml = svc.getExportXML(roots, '')
        new File("EXPORT.xml").withWriter('UTF-8') { out -> out.write(xml) }
    }

    def export2(args) {
        System.err.println('Export2')
        def svc = client.getService(tcExportOperationsIntf.class)
        args.each { objspec ->
            def (type, mask) = objspec.split(':')
            System.err.println("  ? $type:$mask")
            svc.findObjects(type, mask).each { obj ->
                def name = obj.name
                System.err.println("  > $type:$name")
                def objs = [obj]
                objs += svc.retrieveChildren(objs)
                //objs += svc.retrieveDependencyTree(objs)
                objs = svc.arrangeSelectionAsATree(objs)
                def xml = svc.getExportXML(objs, '')
                new File("$type-${name}.xml").withWriter('UTF-8') { out -> out.write(xml) }
            }
        }
    }

    def register(args) {
        System.err.println('Register')
        def svc = client.getService(PlatformService.class)
        args.each { filename ->
            System.err.println("  > ${filename}")
            svc.registerPlugin(new File(filename).bytes)
        }
    }

    def unregister(args) {
        System.err.println('Unregister')
        def svc = client.getService(PlatformService.class)
        args.each { pluginspec ->
            System.err.println("  > ${pluginspec}")
            def (classname, version) = pluginspec.split(':')
            svc.unRegisterPlugin(classname, version)
        }
    }

    def uploadjars(args) {
    	processjars('upload', args)
    }

    def updatejars(args) {
    	processjars('update', args)
    }

    def deletejars(args) {
    	processjars('delete', args)
    }

    private void processjars(cmd, args) {
        System.err.println("${cmd.capitalize()} ThirdParty Jars")
        def svc = client.getService(PlatformUtilsService.class)
        Set<JarElement> set = new HashSet<>()
        args.each { filename ->
            System.err.println("  > ${filename}")
            JarElement jar = new JarElement()
            jar.setType('ThirdParty')
            jar.setName(filename)
            jar.setPath(new File(filename).getAbsolutePath())
            set.add(jar)
        }
        svc."${cmd}Jars"(set)
    }

    def purge(args) {
        System.err.println('Purge')
        def svc = client.getService(PlatformUtilsService.class)
        args.each { obj -> 
            System.err.println("  > $obj")
            svc.purgeCache(obj)
        }
    }

    def purgeall() {
        purge(['All'])
    }
}

def getArgs(argspec) {
    if (!argspec || (argspec[0] == '-f' && argspec.size() < 2)) return []
    if (argspec[0] != '-f') return argspec
    def list = []
    new File(argspec[1]).eachLine { line ->
        line = line.trim()
        if (!line || line.startsWith('#')) return
        list << line
    }
    return list
}

if (!args) {
    println('Usage: oimtool <command> [arguments]')
    System.exit(0)
}

def fconf = new File(Paths.get(scriptUri).getParent().toString() + '/conf/oimtool.conf')
def props = new Properties()
fconf.withInputStream { props.load(it) }
def tool = new OimTool(props.url, props.user, props.password)
def (cmd, arglist) = [args.head(), getArgs(args.tail())]
arglist ? tool."$cmd"(arglist) : tool."$cmd"()
