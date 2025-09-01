package runner;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber; 

import io.cucumber.junit.CucumberOptions; 

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",  
    glue = {"steps"},                          
    plugin = {
        "pretty",
        "html:target/cucumber-report-junit.html",
        "json:target/cucumber-report-junit.json"
    },
    monochrome = true
)
public class JUnitTestRunner {
}
