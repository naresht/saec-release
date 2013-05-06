package com.bfds.saec.web.model;

import com.bfds.saec.dao.AssociateDao;
import com.bfds.saec.domain.Role;
import com.bfds.saec.domain.User;
import com.bfds.saec.web.util.JsfUtils;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel to administer UserRoles Management
 */
public class AdminUserRolesViewModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
    final Logger log = LoggerFactory.getLogger(AdminUserRolesViewModel.class);
    
    @Autowired
    private transient AssociateDao associateDao;
    
    private List<String> associateNames;
    
    private List<User> users;
    
    private String selectedUser;
    private String selectedRoleName;
    
    // flag to toggle roles panel in UI
    boolean userFound = false;
    
    private PickListBean pickList = new PickListBean();
    
    public List<String> getAssociateNames() {
    	if(associateNames == null) {
    		associateNames = associateDao.getAllAssociateNames();
    	}
    	return associateNames;
    }
    
    public List<User> loadUsers() {
        return User.findAllUsers();
    }       
    
	public boolean saveUser(MessageContext messageContext) {
        User user = User.findUserByName(selectedUser);
        if(user != null) {
            messageContext.addMessage(new MessageBuilder().error()
                    .source(JsfUtils.getClientId("associateNtId"))
                    .defaultText("User "+selectedUser+" exists in user list.").build());
          return false;
        }
        user = new User();
        user.setUserName(selectedUser);
        user.addRole(Role.findRolesByRoleName(selectedRoleName).getSingleResult());
        user.persist();
        if(log.isInfoEnabled())
			log.info(String.format("User with UserName: %s is added with role: %s.", user.getUserName(),user.getRoleNames()));
        return true;
	}	
    

    public void addRoles(final MessageContext messageContext) {
        User selectedUser = User.findUserByName(this.selectedUser);
        List<Role> targetRoles = this.pickList.getChangedRoles().getTarget();       
       
        
       List<Role> currentRoles = selectedUser.getRoles();
       List<Role> deletedRoles = new ArrayList<Role>();
        
       if(log.isInfoEnabled())
			log.info(String.format("User with UserName: %s is currently having roles : %s", selectedUser.getUserName(),currentRoles.toString()));
       
        for (Role role: currentRoles) {
        	
            if (!targetRoles.contains(role)) {
                deletedRoles.add(role);
            }             
        }
        
        // add new roles                
        for (Role role: targetRoles) {
        	if (!currentRoles.contains(role)) {
	            selectedUser.addRole(role); 
	            selectedUser.merge();
        	}        	
        } 
        if(log.isInfoEnabled()) 
			log.info(String.format("User with UserName: %s are provided the target roles : %s ", selectedUser.getUserName(),targetRoles.toString()));	
        
        // remove old roles
        for (Role role: deletedRoles) {
            selectedUser.removeRole(role); 
            selectedUser.merge();
            log.info(role.getRoleName()+",");
        }
        if(log.isInfoEnabled())
			log.info(String.format("Roles : %s are removed for User with UserName: %s.", selectedUser.getUserName(),deletedRoles.toString()));
		
        
        userFound=false ;
        this.selectedUser="" ;
    }

    public boolean search(final MessageContext messages) {
        User foundUser = null;
        try {
            foundUser = User.findUserByName(selectedUser);
            userFound = true;
            
            // update corresponding roles
            pickList.updateRolesTarget(foundUser);
        }
        catch(EmptyResultDataAccessException e) {
            messages.addMessage(new MessageBuilder().error().defaultText("User not found.").build());
            return false;      
        }
        
        selectedUser = foundUser.getUserName();
        return true;    
    }
    
    public void clear() {
    	this.userFound = false;
    	this.selectedUser="" ;
    }
    
    public List<User> getUsers() {
        return users;
    }
    
    public List<Role> getRoles() {
        return Role.findAllRoles();
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(String selectedUser) {
        this.selectedUser = selectedUser;
    }

    public String getSelectedRoleName() {
        return selectedRoleName;
    }

    public void setSelectedRoleName(String selectedRoleName) {
        this.selectedRoleName = selectedRoleName;
    }

    public PickListBean getPickList() {
        return this.pickList;
    }

    public void setPickList(PickListBean pickList) {
        this.pickList = pickList;
    }
    
    public boolean isUserFound() {
        return userFound;
    }

    public void setUserFound(boolean userFound) {
        this.userFound = userFound;
    }

    public class PickListBean implements Serializable {  
      
        private DualListModel<Role> changedRoles;  
      
        public PickListBean() {  
            List<Role> source = getRoles();
            List<Role> target = new ArrayList<Role>();
            
            if (StringUtils.hasLength(selectedUser)) {
                target = User.findUserByName(selectedUser).getRoles();           
            }            
            
            changedRoles = new DualListModel<Role>(source, target);  
        }

        public DualListModel<Role> getChangedRoles() {
            return this.changedRoles;
        }

        public void setChangedRoles(DualListModel<Role> changedRoles) {
            this.changedRoles = changedRoles;
        }
        
        public void updateRolesTarget(User user) {
            changedRoles.setTarget(user.getRoles()); 
            updateRolesSource(user) ;
        }
        
        public void updateRolesSource(User user) {
        	List<Role> rrls = getRoles();
        	boolean rem = rrls.removeAll(changedRoles.getTarget()) ;
            changedRoles.setSource(rrls); 
        }
        
    }  
    
}
