package util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.annotation.Annotation;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.secure.JACCConfiguration;
import org.hibernate.util.ReflectHelper;
import org.hibernate.util.StringHelper;

/**
 * Hibernate Configuration class
 * 
 * Loads all Annotated classes in a package
 * 
 * @author Adam Retter <adam.retter@googlemail.com>
 */
public class AnnotationConfigurationWithWildcard extends Configuration {

    //mostly copied from Configuration
    @Override
    protected Configuration doConfigure(Document doc) throws HibernateException {
        
        Element sfNode = doc.getRootElement().element( "session-factory" );
        String name = sfNode.attributeValue( "name" );
        if(name != null) {
            Properties props = new Properties();
            props.setProperty(Environment.SESSION_FACTORY_NAME, name);
            addProperties(props);
        }
        addProperties(sfNode);
        parseSessionFactory(sfNode, name);

        Element secNode = doc.getRootElement().element("security");
        if(secNode != null) {
            parseSecurity(secNode);
        }

        //System.out.println("Configured SessionFactory: " + name);
        //System.out.println("properties: " + getProperties());

        return this;
    }
    
    //copied from Configuration
    private void addProperties(Element parent) {
        Properties props = new Properties();
        Iterator itr = parent.elementIterator( "property" );
        while(itr.hasNext()) {
            Element node = (Element) itr.next();
            String name = node.attributeValue( "name" );
            String value = node.getText().trim();
            props.setProperty( name, value );
            if(!name.startsWith( "hibernate" ) ) {
                props.setProperty( "hibernate." + name, value );
            }
        }
        addProperties(props);
        Environment.verifyProperties(getProperties());
    }
    
    //copied from Configuration
    private void parseSessionFactory(Element sfNode, String name) {
        Iterator elements = sfNode.elementIterator();
        while ( elements.hasNext() ) {
            Element subelement = (Element) elements.next();
            String subelementName = subelement.getName();
            if ("mapping".equals( subelementName ) ) {
                parseMappingElement( subelement, name );
            }
            else if ("class-cache".equals( subelementName ) ) {
                String className = subelement.attributeValue( "class" );
                Attribute regionNode = subelement.attribute( "region" );
                final String region = ( regionNode == null ) ? className : regionNode.getValue();
                boolean includeLazy = !"non-lazy".equals( subelement.attributeValue( "include" ) );
                setCacheConcurrencyStrategy( className, subelement.attributeValue( "usage" ), region, includeLazy );
            }
            else if ( "collection-cache".equals( subelementName ) ) {
                String role = subelement.attributeValue( "collection" );
                Attribute regionNode = subelement.attribute( "region" );
                final String region = ( regionNode == null ) ? role : regionNode.getValue();
                setCollectionCacheConcurrencyStrategy( role, subelement.attributeValue( "usage" ), region );
            }
            else if ( "listener".equals( subelementName ) ) {
                parseListener( subelement );
            }
            else if ( "event".equals( subelementName ) ) {
                parseEvent(subelement);
            }
        }
    }
    
    //copied from Configuration - modified handler for fileAttribute
    private void parseMappingElement(Element mappingElement, String name) {
        final Attribute resourceAttribute = mappingElement.attribute( "resource" );
        final Attribute fileAttribute = mappingElement.attribute( "file" );
        final Attribute jarAttribute = mappingElement.attribute( "jar" );
        final Attribute packageAttribute = mappingElement.attribute( "package" );
        final Attribute classAttribute = mappingElement.attribute( "class" );

        if(resourceAttribute != null) {
            final String resourceName = resourceAttribute.getValue();
            addResource( resourceName );
        }
        else if (fileAttribute != null) {
            final String fileName = fileAttribute.getValue();
            addFile( fileName );
        }
        else if (jarAttribute != null) {
            final String jarFileName = jarAttribute.getValue();
            addJar(new File(jarFileName ));
        }
        else if (packageAttribute != null) {
            final String packageName = packageAttribute.getValue();
            addPackage( packageName );
        }
        else if ( classAttribute != null ) {
            final String classAttributeName = classAttribute.getValue();

            final List<String> classNames = new ArrayList<String>();
            
            if(classAttributeName.endsWith(".*")) {
                try {
                	System.out.println("TRY " + classAttributeName);
                    classNames.addAll(getAllAnnotatedClassNames(classAttributeName));
                } catch(IOException ioe) {
                } catch(URISyntaxException use) {
                }
            } else {            
                classNames.add(classAttributeName);
            }
            
            for(String className : classNames) {
                try {
                    addAnnotatedClass(ReflectHelper.classForName(className));
                } catch (Exception e ) {
                    throw new MappingException("Unable to load class [ " + className + "] declared in Hibernate configuration <mapping/> entry", e );
                }
            }
        }
        else {
            throw new MappingException( "<mapping> element in configuration specifies no known attributes" );
        }
    }
    
    //copied from Configuration
    private void parseSecurity(Element secNode) {
        String contextId = secNode.attributeValue( "context" );
        setProperty(Environment.JACC_CONTEXTID, contextId);
        JACCConfiguration jcfg = new JACCConfiguration( contextId );
        Iterator grantElements = secNode.elementIterator();
        while ( grantElements.hasNext() ) {
                Element grantElement = (Element) grantElements.next();
                String elementName = grantElement.getName();
                if ( "grant".equals( elementName ) ) {
                        jcfg.addPermission(
                                        grantElement.attributeValue( "role" ),
                                        grantElement.attributeValue( "entity-name" ),
                                        grantElement.attributeValue( "actions" )
                                );
                }
        }
    }

    //copied from Configuration
    private void parseEvent(Element element) {
        String type = element.attributeValue( "type" );
        List listeners = element.elements();
        String[] listenerClasses = new String[ listeners.size() ];
        for ( int i = 0; i < listeners.size() ; i++ ) {
                listenerClasses[i] = ( (Element) listeners.get( i ) ).attributeValue( "class" );
        }
        setListeners( type, listenerClasses );
    }

    //copied from Configuration
    private void parseListener(Element element) {
        String type = element.attributeValue( "type" );
        if ( type == null ) {
                throw new MappingException( "No type specified for listener" );
        }
        String impl = element.attributeValue( "class" );
        setListeners( type, new String[]{impl} );
    }

    /**
     * Load all annotated classes from the package
     */
    private List<String> getAllAnnotatedClassNames(String fileAttributeName) throws URISyntaxException, FileNotFoundException, IOException {
        
        List<String> fileNames = new ArrayList<String>();
        
        String path = fileAttributeName.substring(0, fileAttributeName.lastIndexOf(".")).replace(".", File.separator);
        System.out.println("WILD: " + path);
        //URL packageUrl = new URL(File.separator + path);
		File fPackageDir = new File(File.separator + path);
		System.out.println("WILD FILE: " + fPackageDir.getAbsolutePath());
		if(!fPackageDir.exists()) {
			System.out.println("WILD !EXISTS");
		    fileNames.add(fileAttributeName); //cant find package, so default behaviour
		} else {
			System.out.println("WILD ELSE2");
		    File classFiles[] = fPackageDir.listFiles(new FilenameFilter(){
		        @Override
		        public boolean accept(File file, String name) {
		            return name.endsWith(".class");
		        }
		    });
		    
		    for(File classFile : classFiles) {
		    	System.out.println("CLASS: "  + classFile.getName());
		        DataInputStream dis = null;
		        try {
		            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(classFile)));
		            ClassFile cf =  new ClassFile(dis);
		            AnnotationsAttribute visible = (AnnotationsAttribute) cf.getAttribute(AnnotationsAttribute.visibleTag);
		            for(Annotation ann : visible.getAnnotations()) {
		                 if(ann.getTypeName().equals("javax.persistence.Entity")) {
		                     fileNames.add(cf.getName());
		                 }
		            }
		        } catch(IOException ioe) {
		        } finally {
		            if(dis != null) {
		                dis.close();
		            }
		        }
		    }
		}
        
        return fileNames;
    }
}