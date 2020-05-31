package it.polito.tdp.food.model;
import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import it.polito.tdp.food.db.FoodDao;
public class Model {
	private FoodDao dao=new FoodDao();
	private Map<Integer,Food> idMap=new TreeMap<Integer,Food>();
	private Graph<Food,DefaultWeightedEdge> grafo;

	public String creaGrafo(int quantita)
	{
		this.grafo=new SimpleWeightedGraph(DefaultWeightedEdge.class);
		this.idMap=new TreeMap<Integer,Food>();
		List<Food> list=this.dao.listFood(quantita, idMap);
		Graphs.addAllVertices(this.grafo, list);
		List<FoodCouple> coppieCibi=dao.listCouple(quantita, idMap);
		for(FoodCouple c: coppieCibi)
		{
			Graphs.addEdgeWithVertices(this.grafo, c.getF1(), c.getF2(), c.getMediaCalories());
		}
		return String.format("Grafo creato! #vertici %d, # Archi %d\n", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
	}
	public List<Food> getFood(int quantita)
	{
		return this.dao.listFood(quantita, idMap);
	}
	public List<FoodCouple> getCouple()
	{
		List<FoodCouple> list=new ArrayList<>();
		for(DefaultWeightedEdge e: grafo.edgeSet())
		{		
			list.add(new FoodCouple(this.grafo.getEdgeSource(e),this.grafo.getEdgeTarget(e), this.grafo.getEdgeWeight(e)));
		}
		return list;
	}
	public List<FoodCouple> topFive(Food f)
	{
		List<Food> vicini=Graphs.neighborListOf(this.grafo, f);
		List<FoodCouple> coppie=new ArrayList<>();
		for(Food v:vicini)
		{
			FoodCouple c=new FoodCouple(f,v,this.grafo.getEdgeWeight(this.grafo.getEdge(f, v)));
			coppie.add(c);
		}
		Collections.sort(coppie);
		if(coppie.size()<=5)
			return coppie;
		else
		{
			List<FoodCouple> top=new ArrayList<>();
			for(int i=1;i<=5;i++)
			{
				top.add(coppie.get(i));
			}
			return top;
		}
		
	}
}
