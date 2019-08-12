package coreApi;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RespostoryServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RespostoryServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg.xml");

    @Test
 //   @Deployment(resources = {"my-process.bpmn20.xml"})
    public void test() {
        RepositoryService repositoryService=activitiRule.getRepositoryService();

        DeploymentBuilder deploymentBuilder=repositoryService.createDeployment();
        deploymentBuilder.name("测试部署资源").addClasspathResource("my-process.bpmn20.xml")
                .addClasspathResource("second-approve.bpmn20.xml");
        Deployment deploy = deploymentBuilder.deploy();
        LOGGER.info("deploy = {}",deploy);

        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        Deployment deployment = deploymentQuery.deploymentId(deploy.getId()).singleResult();
        LOGGER.info("deploy = {}",deployment);

        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploy.getId()).listPage(0, 100);
        processDefinitions.forEach(x->LOGGER.info("processDefinitions = {} , version = {} , key = {} , id = {}",
                x, x.getVersion(),x.getKey(),x.getId()));
    }

    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testsuspent(){
        RepositoryService repositoryService=activitiRule.getRepositoryService();
        ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery().singleResult();
        LOGGER.info("processDefinition.id = {}",processDefinition.getId());

        repositoryService.suspendProcessDefinitionById(processDefinition.getId());
        try{
            LOGGER.info("开始启动");
            activitiRule.getRuntimeService().startProcessInstanceById(processDefinition.getId());
            LOGGER.info("启动成功");
        }catch (Exception e){
            LOGGER.info("启动失败");
            LOGGER.info(e.getMessage(),e);
        }

        repositoryService.activateProcessDefinitionById(processDefinition.getId());
        LOGGER.info("重新开始启动");
        activitiRule.getRuntimeService().startProcessInstanceById(processDefinition.getId());
        LOGGER.info("启动成功");
    }

    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testCandidateStarter(){
        RepositoryService repositoryService=activitiRule.getRepositoryService();
        ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery().singleResult();
        LOGGER.info("processDefinition.id = {}",processDefinition.getId());

        repositoryService.addCandidateStarterUser(processDefinition.getId(),"user");
        repositoryService.addCandidateStarterGroup(processDefinition.getId(),"groupM");

        List<IdentityLink> identityLink = repositoryService.getIdentityLinksForProcessDefinition(processDefinition.getId());
        identityLink.forEach(x->LOGGER.info("identityLink = {}",x));

        repositoryService.deleteCandidateStarterGroup(processDefinition.getId(),"groupM");
        repositoryService.deleteCandidateStarterUser(processDefinition.getId(),"user");

    }
}
