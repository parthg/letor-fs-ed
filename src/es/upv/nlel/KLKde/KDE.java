package es.upv.nlel.KLKde;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import gnu.trove.iterator.TDoubleIterator;
import gnu.trove.list.TDoubleList;

public class KDE {
	/** Normal Distribution with zero mean and one variance
	 * 
	 * @param x
	 * @return PDF of x using normal distribution
	 */
	double h = 0.0;
	public double normPDF(double x) {
		return  (1/Math.sqrt(2*Math.PI))*Math.exp(-0.5*x*x);
	}
	
	/** Calculates the bandwidth of the 
	 * Kernel Density Estimation based on the 
	 * Rule of thumb or Silverman's formula 
	 * 
	 * @param x
	 */
	public void setH(TDoubleList x) {
		StandardDeviation std = new StandardDeviation();
		double[] X = x.toArray();
		double y = std.evaluate(X);
		this.h = 1.06 * y * Math.pow(x.size(),(-1/5));
	}
	
	/** Estimates the PDF of the test point X according to the 
	 * Training samples X. Eq. 4 of the paper.
	 * 
	 * @param X
	 * @param x
	 * @return
	 */
	public double kde(TDoubleList X, double x) {
		this.setH(X);
		double y = 0.0;
		TDoubleIterator iter = X.iterator();
		while(iter.hasNext()) {
			y+=this.normPDF((iter.next()-x)/this.h);
		}
		return (y / (X.size()*this.h));
	}
	
}