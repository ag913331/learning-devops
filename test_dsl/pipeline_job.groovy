@Grab('org.yaml:snakeyaml:1.17')
import org.yaml.snakeyaml.Yaml

def config = readFileFromWorkspace('config.yaml')

Yaml parser = new Yaml()
def example_dict = parser.load(config)

example_dict["pipelines"].eachWithIndex { p, index -> 
    pipelineJob("${p.name}") {
        description("Testing ${p.name}")

        definition {
            cps {
                script(readFileFromWorkspace(example_dict[p.workflow]))
            }
        }
    }
}