import groovy.json.JsonSlurper;

def jsonSlurper = new JsonSlurper()

File fl = new File('${workspace}/config.json')

// parse(File file) method is available since 2.2.0
def obj = jsonSlurper.parse(fl)

print obj
// for versions < 2.2.0 it's possible to use
// def old = jsonSlurper.parse(fl.text)