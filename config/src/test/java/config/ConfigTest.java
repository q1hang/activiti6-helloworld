package config;


import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import q1hang.DemoMain;

@Slf4j
public class ConfigTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoMain.class);
    @Test
    public void testConfig1(){
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResourceDefault();
        System.out.println(configuration);
    }


    @Test
    public void testConfig2(){
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration
                .createStandaloneProcessEngineConfiguration();
        System.out.println(configuration);
    }






























}
