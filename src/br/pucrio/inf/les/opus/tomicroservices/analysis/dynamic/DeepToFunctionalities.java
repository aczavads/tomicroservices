package br.pucrio.inf.les.opus.tomicroservices.analysis.dynamic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class DeepToFunctionalities {
	
	private Map<Long, List<String>> deepToFunctionalities = new HashMap<Long, List<String>>();
	
	private TreeSet<Long> deepStored = new TreeSet<Long>();
	
	private long maxDeep;
	
	public DeepToFunctionalities() {
		this.maxDeep = 0l;
	}
	
	/**
	public List<String> getFunctionalities(long deep) {
		Long deepFloor = this.deepStored.floor(deep);
		if (deepFloor != null) {
			return this.deepToFunctionalities.get(deepFloor);
		}
		return null;
	}
	**/
	
	public List<String> getFunctionalities(long deep) {
		Long deepFloor = this.deepStored.floor(deep);
		List<String> result = null;
		if (deepFloor != null) {
			this.maxDeep = deepFloor;
			result = this.deepToFunctionalities.get(deepFloor);
		} else {
			result = null;
			deep = 0;
			this.maxDeep = 0;
		}
		Long deepHigher = this.deepStored.higher(deep);
		while (deepHigher != null) {
			this.deepToFunctionalities.remove(deepHigher);
			this.deepStored.remove(deepHigher);
			deepHigher = this.deepStored.higher(deep);
		}
		return result;
	}
	
	/**
	 * Add functionalities associated to a deep. Deep = 0 is used to represent base functionalities.
	 * @param deep deep in the execution of the system under analysis.
	 * @param functionalities functionalities to be assoacited
	 */
	public void addFunctionalitites(long deep, List<String> functionalities) {
		if (this.maxDeep >= deep) {
			Long deepHigher = this.deepStored.higher(deep);
			while (deepHigher != null) {
				this.deepToFunctionalities.remove(deepHigher);
				this.deepStored.remove(deepHigher);
				deepHigher = this.deepStored.higher(deep);
			}
			/**
			Long deepFloor = this.deepStored.floor(deep);
			while (deepFloor != null && deepFloor >= deep) {
				this.deepStored.remove(deepFloor);
				this.deepToFunctionalities.remove(deepFloor);
				deepFloor = this.deepStored.floor(deep);
			}
			**/
		}
		this.maxDeep = deep;
		this.deepToFunctionalities.put(deep, functionalities);
		removeEqualsElements(deep);
		this.deepStored.add(deep);
	}
	
	private void removeEqualsElements(long deep) {
		List<String> funcs = this.deepToFunctionalities.get(deep);
		Set<String> onlyOne = new HashSet<String>(funcs);
		onlyOne.addAll(funcs);
		funcs.clear();
		funcs.addAll(onlyOne);
	}
}
