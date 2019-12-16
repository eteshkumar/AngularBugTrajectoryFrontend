package com.iris.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iris.models.BugAllocationDetail;
import com.iris.models.BugSeverity;
import com.iris.models.BugStatus;
import com.iris.models.BugType;
import com.iris.models.Project;
import com.iris.models.User;
import com.iris.service.BugAllocationDetailService;
import com.iris.service.BugSeverityService;
import com.iris.service.BugStatusService;
import com.iris.service.BugTypeService;
import com.iris.service.ProjectService;
import com.iris.service.UserService;

@Controller
@CrossOrigin("http://localhost:4200")
public class HomeController {

	@Autowired
	UserService userService;
	@Autowired
	ProjectService projectService;
	@Autowired
	BugSeverityService severityService;
	@Autowired
	BugTypeService bugTypeService;
	@Autowired
	BugAllocationDetailService bugAllocationDetailService;
	@Autowired
	BugStatusService bugStatusService;

	@RequestMapping(value="/",method=RequestMethod.GET)
	public String LogInForm() {
		return "index";
		
}
	
	@ResponseBody
    @RequestMapping(value="/login",method=RequestMethod.POST)
    public ResponseEntity<?> validateUserForLoginPage(@RequestParam int userid, @RequestParam String password,ModelMap map)
    {
           
		User uObj=userService.validate(userid, password);
		
           if(uObj!=null) {
             return new ResponseEntity<Object>(uObj,HttpStatus.OK);
                 
           }
           else {
                  return new ResponseEntity<String>("User is Invalid",HttpStatus.UNAUTHORIZED);
                  
            }
           

	
    }
	@ResponseBody
	@CrossOrigin("http://localhost:4200")
	
	@GetMapping(value="/getProjectList")
	public List<Project> getAllProject(){
		return projectService.getAllProject();
	}
	@ResponseBody
	@GetMapping(value="/getDeveloperList")
	public List<User> getAllDeveloper(){
		return userService.getAllDeveloper();
	}
	@ResponseBody
	@GetMapping(value="/getTesterList")
	public List<User> getAllTester(){
		return userService.getAllTesterI();
	}
	
	
	@PostMapping("/allocateProject")
    public ResponseEntity<?> allocateProject(@RequestBody Project addProObj)
    {
    System.out.println("Project : "+addProObj.getP_Id()+" ");
    
    Project projectObj=projectService.getProjectById(addProObj.getP_Id());
    
    List<User> userList=projectObj.getUsers();
    for(int userid:addProObj.getUser()) {
       userList.add(userService.getUserById(userid));
    }
    projectObj.setUsers(userList);
    
    System.out.println("project Obj : "+projectObj);
    System.out.println("project Obj : "+projectObj.getUser());
    
    
    boolean b =projectService.updateProject(projectObj);
    if(b==true) {
       return new ResponseEntity<String>("done",HttpStatus.OK );
    }
    else 
       return null;
}
	@ResponseBody
    @GetMapping("/projectList/{userid}")
    public List<Project> getprojectList(@PathVariable int userid){        
           User userObj=userService.getUserById(userid);
           List <Project> testprojectList=userObj.getProject();
           return testprojectList;
    }
	@ResponseBody
	@GetMapping(value="/severityList")
	public List<BugSeverity> getSeverityList(){
		return severityService.getBugSeverity();
	}

	@ResponseBody
	@GetMapping(value="/bugList")
	public List<BugType> getBugType(){
		return bugTypeService.getAllBug();
	}
	
	@ResponseBody
	@GetMapping(value="/getPoject/{projectId}")
	public Project getProjectId(@PathVariable int projectId) {
		return projectService.getProjectById(projectId);
	}
	
	@ResponseBody
	@GetMapping(value="/bugStatus")
	public List<BugStatus> getBugStatus(){
		return bugStatusService.getAllBugStatus();
	}
	
	/*http://localhost:8081/AngularBug_Trajectory_Frontend/BugSuccessfullyAllocated?bugType2&&bugSeverity=bug_2&&Description=Bug
		%20Desc&&projectId=101&&userid=110*/
	
	@ResponseBody
	@RequestMapping(value="/BugSuccessfullyAllocated",method=RequestMethod.GET)
    public String getBugAllocationDetails(@RequestParam(name="bugType") String bugType,@RequestParam (name="bugSeverity") int bugSeverity,
    		@RequestParam (name="Description")
    		String description,@RequestParam(name="userid") int userid,@RequestParam(name="projectId") int p_id)
    {
                  
                    System.out.println("I m here...."+bugType+" "+bugSeverity+" "+description);
                    BugAllocationDetail bugObj = new BugAllocationDetail();
                    bugObj.setUserid(userid);
                    bugObj.setStatusId("b101");
                    bugObj.setBugTypeId(bugType);
                    bugObj.setSeverityId(bugSeverity);
                    bugObj.setDescription(description); 
                    bugObj.setP_Id(p_id);
                    bugAllocationDetailService.saveBugDetails(bugObj);
					return "BugSuccessfullyAllocated";
                  
    }
	@ResponseBody
	@GetMapping(value="/getBugDetailList")
	public List<BugAllocationDetail> getBugDetailList(){
		return bugAllocationDetailService.getAllBugDescription();
	}
	@RequestMapping(value="/bugAllocation",method=RequestMethod.GET)
	@ResponseBody
	public String upadteBugAllocationDetails(@RequestParam int bugAllocateId,@RequestParam int userId,
	       @RequestParam int projectId,@RequestParam("createdDate") @DateTimeFormat(pattern="yyyy-MM-dd")  Date createdDate,
	       @RequestParam("endDate") @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate)
	{
	              
	              
	       BugAllocationDetail bugObject=bugAllocationDetailService.getBugAllocationById(bugAllocateId);
	           bugObject.setP_Id(projectId);
	           bugObject.setStatusId("b102");
	           bugObject.setUserid(userId);
	           bugObject.setPlannedStartDate(createdDate);
	           bugObject.setPlannedEndDate(endDate);
	          
	           bugAllocationDetailService.update(bugObject);
	           
	           return "Bug Allocated Successfully";
	}

	
	}

    

	
	
