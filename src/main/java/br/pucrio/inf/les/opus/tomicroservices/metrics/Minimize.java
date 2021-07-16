package br.pucrio.inf.les.opus.tomicroservices.metrics;

public class Minimize implements ConvertValue {

	@Override
	public double convert(double value) {
		if (value != 0.0) {
			return 1.0/value;
		} else {
			return Double.MAX_VALUE;
			//return 1000.0; //Think about MAX_VALUE;
		}
	}

}
