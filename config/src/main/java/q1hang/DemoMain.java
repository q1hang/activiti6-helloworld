package q1hang;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoMain.class);

    public static void main(String[] args) {
        LOGGER.info("启动我们的流程");
        //创建流程引擎
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
        ProcessEngine processEngine = cfg.buildProcessEngine();
        String name = processEngine.getName();
        String version = ProcessEngine.VERSION;
        LOGGER.info("流程引擎版本{},版本{}", name, version);
        //部署流程定义文件
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("second-approve.bpmn20.xml");
        Deployment deployment = deploymentBuilder.deploy();
        String deploymentId = deployment.getId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
        LOGGER.info("流程定义文件{},流程ID{}",processDefinition.getName(),processDefinition.getId());

        //启动运行流程
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        System.out.println(processInstance);
        LOGGER.info("启动流程{}",processInstance.getProcessDefinitionKey());

        //处理流程任务




        LOGGER.info("结束我们的流程");
    }


}
