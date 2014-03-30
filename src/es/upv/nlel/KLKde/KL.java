package es.upv.nlel.KLKde;

public class KL {
	/** Computes Jensen-Shannon KL divergence. Eq. 3 of the FS-ED paper.
	 * 
	 * @param p
	 * @param q
	 * @return
	 */
	public double JSdiv(double p, double q) {
		double m = 0.5*(p+q);
		double d = 0.5*this.div(p,m) + 0.5*this.div(q,m);
		
		return d;
	}
	
	public double div(double p, double q) {
		return p*Math.log((p/q));
	}
}