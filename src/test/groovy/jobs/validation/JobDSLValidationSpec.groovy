package jobs.validation

import categories.Validation
import javaposse.jobdsl.dsl.DslScriptLoader
import javaposse.jobdsl.plugin.JenkinsJobManagement
import org.junit.ClassRule
import org.junit.experimental.categories.Category
import org.jvnet.hudson.test.JenkinsRule
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@Category(Validation)
class JobDSLValidationSpec extends Specification {
    @Shared
    @ClassRule
    JenkinsRule jenkinsRule = new JenkinsRule()

    @Unroll
    def 'test script #file.name'(File file) {
        given:
        def jobManagement = new JenkinsJobManagement(System.out, [:], new File('.'))

        when:
        new DslScriptLoader(jobManagement).runScript(file.text)

        then:
        noExceptionThrown()

        where:
        file << jobFiles
    }

    static List<File> getJobFiles() {
        List<File> files = []
        new File('jobs').eachFileRecurse {
            if (it.name.endsWith('.groovy')) {
                files << it
            }
        }
        files
    }
}