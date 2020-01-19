package br.pucrio.inf.les.opus.tomicroservices.graph;

import java.util.ArrayList;
import java.util.List;

public class Edge {

	private boolean staticCall;
	
	private long dynamicCall;
	
	private List<Long> dataTraffic;
	
	private Vertex source;
	
	private Vertex target;
	
	public Edge(Vertex source, Vertex target, boolean staticCall, long dynamicCall, long dataTraffic) {
		this.source = source;
		this.target = target;
		this.setStaticCall(staticCall);
		this.setDynamicCall(dynamicCall);
		this.dataTraffic = new ArrayList<Long>();
		this.dataTraffic.add(new Long(dataTraffic));
		source.addOutbound(this);
		target.addInBound(this);
	}

	public boolean getStaticCall() {
		return staticCall;
	}

	public void setStaticCall(boolean staticCall) {
		this.staticCall = staticCall;
	}
	
	public void addStaticCall(boolean staticCall) {
		if (staticCall == true) {
			this.staticCall = true;
		}
	}

	public long getDynamicCall() {
		return dynamicCall;
	}

	public void setDynamicCall(long dynamicCall) {
		this.dynamicCall = dynamicCall;
	}

	public List<Long> getDataTraffic() {
		return this.dataTraffic;
	}
	
	public void addDataTraffic(List<Long> dataTraffic) {
		this.dataTraffic.addAll(dataTraffic);
	}

	public void addDataTraffic(long dataTraffic) {
		this.dataTraffic.add(dataTraffic);
	}
	
	public void addDynamicCall() {
		++this.dynamicCall;
	}
	
	public void addDynamicCall(long dynamicCall) {
		this.dynamicCall += dynamicCall;
	}

	public Vertex getSource() {
		return source;
	}

	public Vertex getTarget() {
		return target;
	}
	
	public void incrementNewData(Edge edge) {
		addStaticCall(edge.getStaticCall());
		addDataTraffic(edge.getDataTraffic());
		addDynamicCall(edge.getDynamicCall());
	}
}
