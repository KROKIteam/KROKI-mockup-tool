package adapt.util.xml_readers;

import java.io.File;
import java.util.ArrayList;

import javax.persistence.EntityManager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import adapt.core.AppCache;
import adapt.util.ejb.PersisenceHelper;
import adapt.util.repository_utils.RepositoryPathsUtil;
import adapt.util.staticnames.Tags;
import adapt.util.xml_utils.XMLParserUtils;
import ejb.AdaptOperation;
import ejb.AdaptPermission;
import ejb.AdaptResource;
import ejb.AdaptRole;
import ejb.AdaptRolePermission;
import ejb.AdaptUser;
import ejb.AdaptUserRoles;

public class AdministrationSubsystemReader {
	
	protected static String generatedRepoPath 	= RepositoryPathsUtil.getGeneratedModelPath();
	protected static String generatedModelPath 	= RepositoryPathsUtil.getGeneratedModelPath();
	protected static String xmlFileName		 	= "administration-generated.xml";
	
	private static String logPrefix = "ADMINISTRATION-SUBSYTEM READER: ";

	/**
	 * Loads menu.xml file form application repository and creates object model representation of menus
	 */
	public static void load(EntityManager em) {
		try {
			AppCache.displayTextOnMainFrame(logPrefix + "Reading Administration-Subsytem structure from XML specification...", 0);
			storeAdministrationSubsytem(xmlFileName, em);
			AppCache.displayTextOnMainFrame(logPrefix + "Administration-Subsytem structure obtained successfully!", 0);
		} catch (Exception e) {
			AppCache.displayTextOnMainFrame("Error reading administration-generated.xml", 1);
			AppCache.displayStackTraceOnMainFrame(e);
		}
	}
	
	
	public static void storeAdministrationSubsytem(String file, EntityManager em2) {
		File f = new File(".");
		String appPath = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1);
		Document resDoc = XMLParserUtils.parseXml(generatedModelPath + File.separator + xmlFileName); 
//		mainFrame.displayText("Fetching resources from file: " + resDoc.getBaseURI().substring(8), 0);
		
		EntityManager em = PersisenceHelper.createEntityManager();
		em.getTransaction().begin();
		
		ArrayList<AdaptUser> users = new ArrayList<AdaptUser>();
		ArrayList<AdaptRole> roles = new ArrayList<AdaptRole>();
		ArrayList<AdaptOperation> userOperations = new ArrayList<AdaptOperation>();
		ArrayList<AdaptResource> resources = new ArrayList<AdaptResource>();
		ArrayList<AdaptPermission> permissions = new ArrayList<AdaptPermission>();
		ArrayList<AdaptRolePermission> rolePermissions = new ArrayList<AdaptRolePermission>();
		
		//Ekstrakcija resursa
//		mainFrame.displayText("Resursi", 0);
		AdaptResource tResource = null;
		NodeList resNodes = resDoc.getElementsByTagName("resource");
		for(int i=0; i<resNodes.getLength(); i++) {
			Element element = (Element)resNodes.item(i);
			NodeList nname = element.getElementsByTagName("name");
			NodeList nlink = element.getElementsByTagName("link");
			
			String name = XMLParserUtils.getCharacterDataFromElement((Element)nname.item(0));
			String link = XMLParserUtils.getCharacterDataFromElement((Element)nlink.item(0));
			tResource = new AdaptResource();
			tResource.setName(name);
			tResource.setLink(link);
			em.persist(tResource);
			resources.add(tResource);
			
//			mainFrame.displayText("Name je: " + name + ", link je " + link, 0);
		}
		
//		mainFrame.displayText("Korisnici", 0);
		//Ekstrakcija korisnika
		AdaptUser u = null;
		NodeList userNodes = resDoc.getElementsByTagName("user");
		for (int i = 0; i < userNodes.getLength(); i++) {
			Element element = (Element)userNodes.item(i);
			NodeList nusername = element.getElementsByTagName("username");
			NodeList npassword = element.getElementsByTagName("password");
			
			String username = XMLParserUtils.getCharacterDataFromElement((Element)nusername.item(0));
			String password = XMLParserUtils.getCharacterDataFromElement((Element)npassword.item(0));
			u = new AdaptUser();
			u.setUsername(username);
			u.setPassword(password);
			em.persist(u);
			users.add(u);


//			mainFrame.displayText("Username: " + username + ", password : " + password, 0);
		}
		
//		mainFrame.displayText("Grupe", 0);
		//Ekstrakcija korisnickih grupa
		AdaptRole userGroup = null;
		NodeList userGroupNodes = resDoc.getElementsByTagName("group");
		for (int i = 0; i < userGroupNodes.getLength(); i++) {
			Element element = (Element)userGroupNodes.item(i);
			NodeList nname = element.getElementsByTagName("name");
			
			String name = XMLParserUtils.getCharacterDataFromElement((Element)nname.item(0));
			userGroup = new AdaptRole();
			userGroup.setName(name);
			em.persist(userGroup);
			roles.add(userGroup);
//			mainFrame.displayText("Grupa: " + name, 0);
		}
		
		
//		mainFrame.displayText("Korisnici u grupama", 0);
		//Ekstrakcija korisnika u grupama
		AdaptUserRoles userInGroup = null;
		NodeList userInGroupNodes = resDoc.getElementsByTagName("user_in_group");
		for (int i = 0; i < userInGroupNodes.getLength(); i++) {
			Element element = (Element)userInGroupNodes.item(i);
			NodeList nid = element.getElementsByTagName("id");
			NodeList nuserInGroupGroup = element.getElementsByTagName("user_in_group_group");
			NodeList nuserInGroupId = element.getElementsByTagName("user_in_group_group_id");
			NodeList nuserInUserId = element.getElementsByTagName("user_in_group_user_id");
			NodeList nuserInUserName = element.getElementsByTagName("user_in_group_user_name");
			
			
			String id = XMLParserUtils.getCharacterDataFromElement((Element)nid.item(0));
			String groupName = XMLParserUtils.getCharacterDataFromElement((Element)nuserInGroupGroup.item(0));
			String groupId = XMLParserUtils.getCharacterDataFromElement((Element)nuserInGroupId.item(0));
			String userId = XMLParserUtils.getCharacterDataFromElement((Element)nuserInUserId.item(0));
			String userName = XMLParserUtils.getCharacterDataFromElement((Element)nuserInUserName.item(0));
			
			userInGroup = new AdaptUserRoles();
			for (AdaptUser tempUser : users) {
				if(tempUser.getUsername().equals(userName)) {
					userInGroup.setUser(tempUser);
				}
			}
			
			for (AdaptRole tempUserGroup : roles) {
				if (tempUserGroup.getName().equals(groupName)) {
					userInGroup.setRole(tempUserGroup);
				}
			}
			
			
			em.persist(userInGroup);
//			mainFrame.displayText("Korisnik u grupi: " , 0);
		}
		
		//Ekstrakcija korisnickih operacija
		AdaptOperation userOperation = null;
		NodeList userOperationNodes = resDoc.getElementsByTagName("user_operation");
		for (int i = 0; i < userOperationNodes.getLength(); i++) {
			Element element = (Element)userOperationNodes.item(i);
			NodeList nname = element.getElementsByTagName("operation_name");
			NodeList noperationname = element.getElementsByTagName("name");


			String name = XMLParserUtils.getCharacterDataFromElement((Element)nname.item(0));
			String operationname = XMLParserUtils.getCharacterDataFromElement((Element)noperationname.item(0));

			
			userOperation = new AdaptOperation();
			userOperation.setName(operationname);

			
			em.persist(userOperation);
			userOperations.add(userOperation);
			
		}
		
		
		//Ekstrakcija Permissions
//		mainFrame.displayText("Permissions: " , 0);
		AdaptPermission permission = null;
		NodeList permissionNodes = resDoc.getElementsByTagName("permission");
		for (int i = 0; i < permissionNodes.getLength(); i++) {
			Element element = (Element)permissionNodes.item(i);
			NodeList nname = element.getElementsByTagName("permission_name");
			NodeList noperationname = element.getElementsByTagName("operation_name");
			NodeList nresourcename = element.getElementsByTagName("resource_name");
			
			String name = XMLParserUtils.getCharacterDataFromElement((Element)nname.item(0));
			String operationName = XMLParserUtils.getCharacterDataFromElement((Element)noperationname.item(0));
			String resourceName = XMLParserUtils.getCharacterDataFromElement((Element)nresourcename.item(0));
			
			permission = new AdaptPermission();
			permission.setName(name);
			
			for (AdaptOperation o : userOperations) {
				if (o.getName().equals(operationName)) {
					permission.setOperation(o);
				}
			}
			
			for (AdaptResource r : resources) {
				if (r.getName().equals(resourceName)) {
					permission.setResource(r);
				}
			}
			
			em.persist(permission);
			permissions.add(permission);
			
//			mainFrame.displayText("Permission: " + name  , 0);
		}
		
		
		//Ekstrakcija RolePermissions
//		mainFrame.displayText("RolePermissions: " , 0);
		AdaptRolePermission rolePermission = null;
		NodeList rolePermissionNodes = resDoc.getElementsByTagName("role_permission");
		for (int i = 0; i < rolePermissionNodes.getLength(); i++) {
			Element element = (Element)rolePermissionNodes.item(i);
			NodeList nrolename = element.getElementsByTagName("role_name");
			NodeList npermissionname = element.getElementsByTagName("permission_name");
			
			String roleName = XMLParserUtils.getCharacterDataFromElement((Element)nrolename.item(0));
			String permissionName = XMLParserUtils.getCharacterDataFromElement((Element)npermissionname.item(0));

			
			rolePermission = new AdaptRolePermission();
			
			for (AdaptPermission p : permissions) {
				if (p.getName().equals(permissionName)) {
					rolePermission.setPermission(p);
				}
			}
			
			for (AdaptRole r : roles) {
				if (r.getName().equals(roleName)) {
					rolePermission.setRole(r);
				}
			}
			
			em.persist(rolePermission);
			rolePermissions.add(rolePermission);
			
//			mainFrame.displayText("RolePermission: " + permissionName + ", " + roleName  , 0);
		}
		
		em.getTransaction().commit();
	}
	


}
