
public class Bib {
	
	private int stnr;
	private long auto;
	private long man;
	
	public Bib(int pStnr, long pAuto, long pMan) {
		stnr = pStnr;
		auto = pAuto;
		man = pMan;
	}
	
	public int getStnr() {
		return stnr;
	}
	
	public long getAuto() {
		return auto;
	}
	
	public long getMan() {
		return man;
	}

}
