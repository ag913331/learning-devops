@Grab('org.yaml:snakeyaml:1.17')
import org.yaml.snakeyaml.Yaml

def config = readFileFromWorkspace('config.yaml')

Yaml parser = new Yaml()
def example_dict = parser.load(config)

example_dict["pipelines"].each { p -> 
    pipelineJob("${p.name}") {
        description("${p.description}")
        p.parameters.each { param -> 
            println "$param.p_name"
            // switch("${$param.p_type}") {
            //     case "boolean":
            //         booleanParam($param.p_name, $param.p_default, $param.p_description)
            //     default:
            //         break
            // }
        }
        // parameters {
        //     $p.parameters.each { param -> 
        //         println "$param"
        //         // switch("${$param.p_type}") {
        //         //     case "boolean":
        //         //         booleanParam($param.p_name, $param.p_default, $param.p_description)
        //         //     default:
        //         //         break
        //         // }
        //     }
        // }

        disabled()

        definition {
            cps {
                script(readFileFromWorkspace("${p.workflow}"))
            }
        }
    }
}