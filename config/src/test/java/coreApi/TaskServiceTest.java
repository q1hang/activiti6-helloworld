package coreApi;

import com.google.common.collect.Maps;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class TaskServiceTest {

    private static final Logger logger= LoggerFactory.getLogger(TaskServiceTest.class);

    @Rule
    public ActivitiRule activitiRule=new ActivitiRule("activiti.cfg.xml");

    @Test
    @Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskService(){
        Map<String,Object> variables = Maps.newHashMap();
        variables.put("message","my test message !!!");
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process-task",variables);
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        logger.info("task = {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        logger.info("task.description = {}", task.getDescription());

        taskService.setVariable(task.getId(),"key1","value1");
        taskService.setVariableLocal(task.getId(),"localKey1","localValue1");

        Map<String, Object> taskServiceVariables = taskService.getVariables(task.getId());
        Map<String, Object> taskServiceVariablesLocal = taskService.getVariablesLocal(task.getId());

        Map<String, Object> processVariables = activitiRule.getRuntimeService().getVariables(task.getExecutionId());

        logger.info("taskServiceVariables = {}",taskServiceVariables);
        logger.info("taskServiceVariablesLocal = {}",taskServiceVariablesLocal);
        logger.info("processVariables = {}",processVariables);

        Map<String,Object> completeVar = Maps.newHashMap();
        completeVar.put("cKey1","cValue1");
        taskService.complete(task.getId(),completeVar);

        Task task1 = taskService.createTaskQuery().taskId(task.getId()).singleResult();
        logger.info("task1 = {}",task1);
    }











}
