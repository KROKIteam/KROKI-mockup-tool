package graphedit.layout.graph;

import java.util.ArrayList;
import java.util.List;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.diagram.GraphEditModel;

public class GraphFactory {
	
	/*
	 * Elements which are already contained in a graph
	 */
	private List<GraphElement> coveredElements = new ArrayList<GraphElement>();
	private List<Link> coveredLinks = new ArrayList<Link>();
	private GraphEditModel model;
	
	public GraphFactory(GraphEditModel model){
		this.model = model;
	}
	
	public List<Graph<GraphElement, Link>> createGraphs(){
		
		
		List<Graph<GraphElement, Link>> ret = new ArrayList<Graph<GraphElement, Link>>();
		List<GraphElement> allElements = new ArrayList<GraphElement>();
		allElements.addAll(model.getDiagramElements());
		allElements.addAll(model.getContainedPackages());
		
	
		
		
		for (GraphElement element : allElements){
			
			//if element is already in a graph, skip it
			if (coveredElements.contains(element))
				continue;
			
			//else, form a new graph
			
			Graph<GraphElement, Link> graph 
				= new UndirectedSparseGraph<GraphElement, Link>();
			
			formGraph(graph, element);
			ret.add(graph);
			System.out.println(graph);
			
			}
			
			
		
		return ret;
		
	}
	
	private void formGraph(Graph<GraphElement, Link> graph, GraphElement element){
		
		coveredElements.add(element);
		
		graph.addVertex(element);
		
		for (Link link : model.getAssociatedLinks(element)){
			
			//avoid infinite recursion
			if (coveredLinks.contains(link))
				continue;
			
			coveredLinks.add(link);
			
			GraphElement source = model.getElementByConnector().get(link.getSourceConnector());
			GraphElement destination = model.getElementByConnector().get(link.getDestinationConnector());
			
			if (!graph.containsVertex(source))
				graph.addVertex(source);
			if (!graph.containsVertex(destination))
				graph.addVertex(destination);
			
			graph.addEdge(link, source, destination);
			
			if (source != element)
				formGraph(graph, source);
			else if (destination != element)
				formGraph(graph, destination);
		}
	}

}
