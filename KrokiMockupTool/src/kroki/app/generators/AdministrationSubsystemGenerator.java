package kroki.app.generators;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import kroki.app.generators.utils.XMLWriter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import devHub.AppType;

/**
 * Generates administration subsystem
 * @author Kroki Team
 *
 */
public class AdministrationSubsystemGenerator {
	
	public void generate() {
		XMLWriter writer = new XMLWriter();
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		
		try {
			docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();

			// korenski tag <classes>
			Element resourcesRoot = doc.createElement("classes");
			doc.appendChild(resourcesRoot);
			
			generateUsers(resourcesRoot, doc);
			generateResources(resourcesRoot, doc);
			generateRoles(resourcesRoot, doc);
			generateUserRoles(resourcesRoot, doc);
			generateOperations(resourcesRoot, doc);
			generatePermissions(resourcesRoot, doc);
			generateRolePermissions(resourcesRoot, doc);
			
			
			writer.write(doc, "administration-generated", AppType.WEB);

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void generateRolePermissions(Element resourcesRoot, Document doc) {
		//<role_permissions> tag
		Element rolePermissionsTag = doc.createElement("role_permissions");
		resourcesRoot.appendChild(rolePermissionsTag);
			
		dao.administration.RolePermissionHibernateDao pDao = new dao.administration.RolePermissionHibernateDao();
		List<ejb.administration.RolePermission> permissions = pDao.findAll();
		
		for (ejb.administration.RolePermission rp : permissions) {
			Element rolePermissionTag = doc.createElement("role_permission");
			rolePermissionsTag.appendChild(rolePermissionTag);
			
			Element idTag = doc.createElement("id");
			idTag.setTextContent(rp.getId().toString());
			rolePermissionTag.appendChild(idTag);
			
			Element roleTag = doc.createElement("role_name");
			roleTag.setTextContent(rp.getRole().getName().toString());
			rolePermissionTag.appendChild(roleTag);
			
			Element permissionTag = doc.createElement("permission_name");
			permissionTag.setTextContent(rp.getPermission().getName());
			rolePermissionTag.appendChild(permissionTag);
		}
	}

	private void generatePermissions(Element resourcesRoot, Document doc) {
		//<permissions> tag
		Element permissionsTag = doc.createElement("permissions");
		resourcesRoot.appendChild(permissionsTag);
			
		dao.administration.PermissionHibernateDao pDao = new dao.administration.PermissionHibernateDao();
		List<ejb.administration.Permission> permissions = pDao.findAll();
		
		for (ejb.administration.Permission p : permissions) {
			Element permissionTag = doc.createElement("permission");
			permissionsTag.appendChild(permissionTag);
			
			Element idTag = doc.createElement("permission_name");
			idTag.setTextContent(p.getName());
			permissionTag.appendChild(idTag);
			
			Element operationIdTag = doc.createElement("operation_name");
			operationIdTag.setTextContent(p.getOperation().getName());
			permissionTag.appendChild(operationIdTag);
			
			Element resourceTag = doc.createElement("resource_name");
			resourceTag.setTextContent(p.getResource().getName());
			permissionTag.appendChild(resourceTag);
			
		}
	}

	private void generateOperations(Element resourcesRoot, Document doc) {
		//<user_operations> tag
		Element userOperationsTag = doc.createElement("users_operations");
		resourcesRoot.appendChild(userOperationsTag);
		
		dao.administration.OperationHibernateDao uoDao = new dao.administration.OperationHibernateDao();
		List<ejb.administration.Operation> userOperations = uoDao.findAll();
		
		for (ejb.administration.Operation uo : userOperations) {
			Element userOperationTag = doc.createElement("user_operation");
			userOperationsTag.appendChild(userOperationTag);
			
			Element idTag = doc.createElement("id");
			idTag.setTextContent(uo.getId().toString());
			userOperationTag.appendChild(idTag);
			
			Element nameTag = doc.createElement("operation_name");
			nameTag.setTextContent(uo.getName());
			userOperationTag.appendChild(nameTag);
			
			Element viewTag = doc.createElement("name");
			viewTag.setTextContent(uo.getName());
			userOperationTag.appendChild(viewTag);
		}
	}

	private void generateUserRoles(Element resourcesRoot, Document doc) {
		//<user_in_group> tag
		Element resourceTag = doc.createElement("users_in_group");
		resourcesRoot.appendChild(resourceTag);
		
		dao.administration.UserRolesHibernateDao ugDao = new dao.administration.UserRolesHibernateDao();
		List<ejb.administration.UserRoles> groups = ugDao.findAll();
		
		for (ejb.administration.UserRoles ug : groups) {
			Element userInGroupTag = doc.createElement("user_in_group");
			resourceTag.appendChild(userInGroupTag);
			
			Element idTag = doc.createElement("id");
			idTag.setTextContent(ug.getId().toString());
			userInGroupTag.appendChild(idTag);
			
			Element userinGroupGroupTag = doc.createElement("user_in_group_group");
			userinGroupGroupTag.setTextContent(ug.getRole().getName());
			userInGroupTag.appendChild(userinGroupGroupTag);
			
			Element userinGroupGroupIdTag = doc.createElement("user_in_group_group_id");
			userinGroupGroupIdTag.setTextContent(ug.getRole().getId().toString());
			userInGroupTag.appendChild(userinGroupGroupIdTag);
			
			Element userinGroupUserIdTag = doc.createElement("user_in_group_user_id");
			userinGroupUserIdTag.setTextContent(ug.getUser().getId().toString());
			userInGroupTag.appendChild(userinGroupUserIdTag);
			
			Element userinGroupUserNameTag = doc.createElement("user_in_group_user_name");
			userinGroupUserNameTag.setTextContent(ug.getUser().getUsername());
			userInGroupTag.appendChild(userinGroupUserNameTag);
		}
		
	}

	private void generateRoles(Element resourcesRoot, Document doc) {
		//<groups> tag
		Element resourceTag = doc.createElement("groups");
		resourcesRoot.appendChild(resourceTag);
		
		dao.administration.RoleHibernateDao ugDao = new dao.administration.RoleHibernateDao();
		List<ejb.administration.Role> groups = ugDao.findAll();
		
		for (ejb.administration.Role ug : groups) {
			Element groupTag = doc.createElement("group");
			resourceTag.appendChild(groupTag);
			
			Element nameTag = doc.createElement("name");
			nameTag.setTextContent(ug.getName());
			groupTag.appendChild(nameTag);
			
			
		}
	}

	private void generateResources(Element resourcesRoot, Document doc) {
		//<resources> tag
		Element resourcesTag = doc.createElement("resources");
		resourcesRoot.appendChild(resourcesTag);
		
		dao.administration.ResourceHibernateDao rDao = new dao.administration.ResourceHibernateDao();
		List<ejb.administration.Resource> resources = rDao.findAll();
		
		for (ejb.administration.Resource res : resources) {
			Element resourceTag = doc.createElement("resource");
			resourcesTag.appendChild(resourceTag);
			
			Element nameTag = doc.createElement("name");
			nameTag.setTextContent(res.getName());
			resourceTag.appendChild(nameTag);
			
			Element linkTag = doc.createElement("link");
			linkTag.setTextContent(res.getLink());
			resourceTag.appendChild(linkTag);
			
		}
	}

	/**
	 * Generate users inside <classes> root tag
	 * @param resourcesRoot
	 * @param doc
	 */
	private void generateUsers(Element resourcesRoot, Document doc) {
		//<users> tag
		Element resourceTag = doc.createElement("users");
		resourcesRoot.appendChild(resourceTag);
		
		dao.administration.UserHibernateDao udao = new dao.administration.UserHibernateDao();
		List<ejb.administration.User> users = udao.findAll();
		
		for (ejb.administration.User u : users) {
			Element userTag = doc.createElement("user");
			resourceTag.appendChild(userTag);
			
			//System.out.println(u.getUsername());
			//<username>
			Element usernameTag = doc.createElement("username");
			usernameTag.setTextContent(u.getUsername());
			userTag.appendChild(usernameTag);
			
			//<password>
			Element passwordTag = doc.createElement("password");
			passwordTag.setTextContent(u.getPassword());
			userTag.appendChild(passwordTag);	
		}
	}

}
