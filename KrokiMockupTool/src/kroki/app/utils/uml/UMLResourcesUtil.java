/*
 * Copyright (c) 2012, 2013 CEA and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   CEA - initial API and implementation
 *   Kenn Hussey (CEA) - 389542, 399544
 *
 */
package kroki.app.utils.uml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.ContentHandler;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.xmi.impl.RootXMLContentHandlerImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLContentHandlerImpl;
import org.eclipse.uml2.types.TypesPackage;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.UMLPlugin;
import org.eclipse.uml2.uml.profile.l2.L2Package;
import org.eclipse.uml2.uml.profile.l3.L3Package;
import org.eclipse.uml2.uml.resource.CMOF2UMLResource;
import org.eclipse.uml2.uml.resource.UML212UMLResource;
import org.eclipse.uml2.uml.resource.UML22UMLResource;
import org.eclipse.uml2.uml.resource.UML302UMLResource;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resource.XMI2UMLResource;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * This class is used for initializing resources that will be used for implementation of the import and export
 * from Eclipse UML files to Kroki project and vice versa. This class is originally located in the package 
 * org.eclipse.uml2.uml.resources.util of a jar file with the first part of the name starting with 
 * org.eclipse.uml2.uml.resources. Difference between the original class is that the new location of
 * the extracted model, library and profile files, had to be added to the global registry that is used
 * in the Eclipse UML2 classes that implement import and export functionality. 
 * @since 4.0
 */
public class UMLResourcesUtil
		extends UMLUtil {

	private static final ContentHandler XMI_CONTENT_HANDLER = new XMLContentHandlerImpl.XMI();

	private static final ContentHandler UML2_1_0_0_CONTENT_HANDLER = new RootXMLContentHandlerImpl(
		UML22UMLResource.UML2_CONTENT_TYPE_IDENTIFIER,
		new String[]{UML22UMLResource.FILE_EXTENSION},
		RootXMLContentHandlerImpl.XMI_KIND,
		UML22UMLResource.UML2_METAMODEL_NS_URI, null);

	private static final ContentHandler UML2_2_0_0_CONTENT_HANDLER = new RootXMLContentHandlerImpl(
		UMLResource.UML_2_0_0_CONTENT_TYPE_IDENTIFIER,
		new String[]{UMLResource.FILE_EXTENSION},
		RootXMLContentHandlerImpl.XMI_KIND, UML2_UML_PACKAGE_2_0_NS_URI, null);

	private static final ContentHandler UML2_2_1_0_CONTENT_HANDLER = new RootXMLContentHandlerImpl(
		UMLResource.UML_2_1_0_CONTENT_TYPE_IDENTIFIER,
		new String[]{UMLResource.FILE_EXTENSION},
		RootXMLContentHandlerImpl.XMI_KIND,
		UML212UMLResource.UML_METAMODEL_NS_URI, null);

	private static final ContentHandler UML2_3_0_0_CONTENT_HANDLER = new RootXMLContentHandlerImpl(
		UMLResource.UML_3_0_0_CONTENT_TYPE_IDENTIFIER,
		new String[]{UMLResource.FILE_EXTENSION},
		RootXMLContentHandlerImpl.XMI_KIND,
		UML302UMLResource.UML_METAMODEL_NS_URI, null);

	private static final ContentHandler UML2_4_0_0_CONTENT_HANDLER = new RootXMLContentHandlerImpl(
		UMLResource.UML_4_0_0_CONTENT_TYPE_IDENTIFIER,
		new String[]{UMLResource.FILE_EXTENSION},
		RootXMLContentHandlerImpl.XMI_KIND, UMLPackage.eNS_URI, null);

	private static final ContentHandler OMG_2_1_CONTENT_HANDLER = new RootXMLContentHandlerImpl(
		XMI2UMLResource.UML_2_1_CONTENT_TYPE_IDENTIFIER,
		new String[]{XMI2UMLResource.FILE_EXTENSION},
		RootXMLContentHandlerImpl.XMI_KIND,
		XMI2UMLResource.UML_METAMODEL_2_1_NS_URI, null);

	private static final ContentHandler OMG_2_1_1_CONTENT_HANDLER = new RootXMLContentHandlerImpl(
		XMI2UMLResource.UML_2_1_1_CONTENT_TYPE_IDENTIFIER,
		new String[]{XMI2UMLResource.FILE_EXTENSION},
		RootXMLContentHandlerImpl.XMI_KIND,
		XMI2UMLResource.UML_METAMODEL_2_1_1_NS_URI, null);

	private static final ContentHandler OMG_2_2_CONTENT_HANDLER = new RootXMLContentHandlerImpl(
		XMI2UMLResource.UML_2_2_CONTENT_TYPE_IDENTIFIER,
		new String[]{XMI2UMLResource.FILE_EXTENSION},
		RootXMLContentHandlerImpl.XMI_KIND,
		XMI2UMLResource.UML_METAMODEL_2_2_NS_URI, null);

	private static final ContentHandler OMG_2_4_CONTENT_HANDLER = new RootXMLContentHandlerImpl(
		XMI2UMLResource.UML_2_4_CONTENT_TYPE_IDENTIFIER,
		new String[]{XMI2UMLResource.FILE_EXTENSION},
		RootXMLContentHandlerImpl.XMI_KIND,
		XMI2UMLResource.UML_METAMODEL_2_4_NS_URI, null);

	private static final ContentHandler OMG_2_4_1_CONTENT_HANDLER = new RootXMLContentHandlerImpl(
		XMI2UMLResource.UML_2_4_1_CONTENT_TYPE_IDENTIFIER,
		new String[]{XMI2UMLResource.FILE_EXTENSION},
		RootXMLContentHandlerImpl.XMI_KIND,
		XMI2UMLResource.UML_METAMODEL_2_4_1_NS_URI, null);

	private static final ContentHandler CMOF_2_0_CONTENT_HANDLER = new RootXMLContentHandlerImpl(
		CMOF2UMLResource.CMOF_2_0_CONTENT_TYPE_IDENTIFIER, new String[]{
			CMOF2UMLResource.FILE_EXTENSION, XMI2UMLResource.FILE_EXTENSION},
		RootXMLContentHandlerImpl.XMI_KIND,
		CMOF2UMLResource.CMOF_2_0_METAMODEL_NS_URI, null);

	private static final ContentHandler CMOF_2_4_CONTENT_HANDLER = new RootXMLContentHandlerImpl(
		CMOF2UMLResource.CMOF_2_4_CONTENT_TYPE_IDENTIFIER, new String[]{
			CMOF2UMLResource.FILE_EXTENSION, XMI2UMLResource.FILE_EXTENSION},
		RootXMLContentHandlerImpl.XMI_KIND,
		CMOF2UMLResource.CMOF_2_4_METAMODEL_NS_URI, null);

	private static final ContentHandler CMOF_2_4_1_CONTENT_HANDLER = new RootXMLContentHandlerImpl(
		CMOF2UMLResource.CMOF_2_4_1_CONTENT_TYPE_IDENTIFIER, new String[]{
			CMOF2UMLResource.FILE_EXTENSION, XMI2UMLResource.FILE_EXTENSION},
		RootXMLContentHandlerImpl.XMI_KIND,
		CMOF2UMLResource.CMOF_2_4_1_METAMODEL_NS_URI, null);

	/**
	 * Initializes the registries for the specified resource set (and/or the
	 * global registries) with the registrations needed to work with UML2
	 * resources in stand-alone mode (i.e., without Eclipse).
	 * 
	 * @param resourceSet
	 *            The resource set whose registries to initialize, or
	 *            <code>null</code>.
	 * @return The resource set (or <code>null</code>).
	 * 
	 * @since 4.0
	 */
	public static ResourceSet init(ResourceSet resourceSet) {
		EPackage.Registry packageRegistry = EPackage.Registry.INSTANCE;

		packageRegistry.put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);

		packageRegistry.put(TypesPackage.eNS_URI, TypesPackage.eINSTANCE);

		packageRegistry.put(UML22UMLResource.UML2_METAMODEL_NS_URI, UMLPackage.eINSTANCE);
		
		packageRegistry.put(UML2_UML_PACKAGE_2_0_NS_URI, UMLPackage.eINSTANCE);

		packageRegistry.put(UML212UMLResource.UML_METAMODEL_NS_URI,
			UMLPackage.eINSTANCE);
		
		packageRegistry.put(UML302UMLResource.UML_METAMODEL_NS_URI,
			UMLPackage.eINSTANCE);
		
		packageRegistry.put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		
		packageRegistry.put(UML302UMLResource.STANDARD_PROFILE_NS_URI,
			L2Package.eINSTANCE);

		packageRegistry.put(L2Package.eNS_URI, L2Package.eINSTANCE);
		packageRegistry.put(L3Package.eNS_URI, L3Package.eINSTANCE);

		/*
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
				"xml", XMI2UMLResource.Factory.INSTANCE);
		/**/
		/*
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
			UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
        /**/
		
		Map<String, URI> ePackageNsURIToProfileLocationMap = UMLPlugin
			.getEPackageNsURIToProfileLocationMap();

		ePackageNsURIToProfileLocationMap.put(L2Package.eNS_URI,
			URI.createURI("pathmap://UML_PROFILES/StandardL2.profile.uml#_0")); //$NON-NLS-1$
		ePackageNsURIToProfileLocationMap.put(L3Package.eNS_URI,
			URI.createURI("pathmap://UML_PROFILES/StandardL3.profile.uml#_0")); //$NON-NLS-1$

		ePackageNsURIToProfileLocationMap.put(UMLResource.ECORE_PROFILE_NS_URI,
			URI.createURI("pathmap://UML_PROFILES/Ecore.profile.uml#_0")); //$NON-NLS-1$

		
		Map<URI, URI> uriMap = URIConverter.URI_MAP;
		
		String path=(new File(".")).getAbsolutePath();
		
		System.out.println("PATH: " + path);
		
		URI uri = URI.createURI("file:/"+path+"/libECore/org.eclipse.uml2.uml/");
		uriMap.put(URI.createURI("platform:/plugin/org.eclipse.uml2.uml/"), uri.appendSegment(""));
		
		uri = URI.createURI("file:/"+path+"/libECore/org.eclipse.uml2.uml.resources/");
		uriMap.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP), uri.appendSegment("libraries").appendSegment(""));
		uriMap.put(URI.createURI(UMLResource.METAMODELS_PATHMAP), uri.appendSegment("metamodels").appendSegment(""));
		uriMap.put(URI.createURI(UMLResource.PROFILES_PATHMAP), uri.appendSegment("profiles").appendSegment(""));

		List<ContentHandler> contentHandlers = ContentHandler.Registry.INSTANCE
			.get(ContentHandler.Registry.LOW_PRIORITY);

		if (contentHandlers == null
			|| !contentHandlers.contains(XMI_CONTENT_HANDLER)) {

			ContentHandler.Registry.INSTANCE.put(
				ContentHandler.Registry.LOW_PRIORITY, XMI_CONTENT_HANDLER);
		}

		contentHandlers = ContentHandler.Registry.INSTANCE
			.get(ContentHandler.Registry.NORMAL_PRIORITY);

		if (contentHandlers == null) {
			ContentHandler.Registry.INSTANCE.put(
				ContentHandler.Registry.NORMAL_PRIORITY,
				contentHandlers = new ArrayList<ContentHandler>());
		}

		if (!contentHandlers.contains(UML2_4_0_0_CONTENT_HANDLER)) {
			contentHandlers.add(UML2_4_0_0_CONTENT_HANDLER);
		}

		if (!contentHandlers.contains(UML2_3_0_0_CONTENT_HANDLER)) {
			contentHandlers.add(UML2_3_0_0_CONTENT_HANDLER);
		}

		if (!contentHandlers.contains(UML2_2_1_0_CONTENT_HANDLER)) {
			contentHandlers.add(UML2_2_1_0_CONTENT_HANDLER);
		}

		if (!contentHandlers.contains(UML2_2_0_0_CONTENT_HANDLER)) {
			contentHandlers.add(UML2_2_0_0_CONTENT_HANDLER);
		}

		if (!contentHandlers.contains(UML2_1_0_0_CONTENT_HANDLER)) {
			contentHandlers.add(UML2_1_0_0_CONTENT_HANDLER);
		}

		if (!contentHandlers.contains(OMG_2_4_1_CONTENT_HANDLER)) {
			contentHandlers.add(OMG_2_4_1_CONTENT_HANDLER);
		}

		if (!contentHandlers.contains(OMG_2_4_CONTENT_HANDLER)) {
			contentHandlers.add(OMG_2_4_CONTENT_HANDLER);
		}

		if (!contentHandlers.contains(OMG_2_2_CONTENT_HANDLER)) {
			contentHandlers.add(OMG_2_2_CONTENT_HANDLER);
		}

		if (!contentHandlers.contains(OMG_2_1_1_CONTENT_HANDLER)) {
			contentHandlers.add(OMG_2_1_1_CONTENT_HANDLER);
		}

		if (!contentHandlers.contains(OMG_2_1_CONTENT_HANDLER)) {
			contentHandlers.add(OMG_2_1_CONTENT_HANDLER);
		}

		if (!contentHandlers.contains(CMOF_2_4_1_CONTENT_HANDLER)) {
			contentHandlers.add(CMOF_2_4_1_CONTENT_HANDLER);
		}

		if (!contentHandlers.contains(CMOF_2_4_CONTENT_HANDLER)) {
			contentHandlers.add(CMOF_2_4_CONTENT_HANDLER);
		}

		if (!contentHandlers.contains(CMOF_2_0_CONTENT_HANDLER)) {
			contentHandlers.add(CMOF_2_0_CONTENT_HANDLER);
		}
		/**/
		
		Resource.Factory.Registry.INSTANCE.getContentTypeToFactoryMap().put(
				UML22UMLResource.UML2_CONTENT_TYPE_IDENTIFIER,
				UML22UMLResource.Factory.INSTANCE);
		
		Resource.Factory.Registry.INSTANCE.getContentTypeToFactoryMap().put(
				UMLResource.UML_2_0_0_CONTENT_TYPE_IDENTIFIER,
				UML212UMLResource.Factory.INSTANCE);
		
		/**/
		Resource.Factory.Registry.INSTANCE.getContentTypeToFactoryMap().put(
				UMLResource.UML_2_1_0_CONTENT_TYPE_IDENTIFIER,
				UML212UMLResource.Factory.INSTANCE);
		
		Resource.Factory.Registry.INSTANCE.getContentTypeToFactoryMap().put(
				UMLResource.UML_3_0_0_CONTENT_TYPE_IDENTIFIER,
				UML302UMLResource.Factory.INSTANCE);
		
		Resource.Factory.Registry.INSTANCE.getContentTypeToFactoryMap().put(
			UMLResource.UML_CONTENT_TYPE_IDENTIFIER,
			UMLResource.Factory.INSTANCE);
		/**/
		
		return UMLUtil.init(resourceSet);
	}

}
