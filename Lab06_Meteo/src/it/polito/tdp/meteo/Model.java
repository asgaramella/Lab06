package it.polito.tdp.meteo;

import java.util.*;
import java.util.List;

import it.polito.tdp.meteo.bean.*;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 50;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	private List<Citta> citta;

	public Model() {

	}

	public String getUmiditaMedia(int mese) {
		MeteoDAO dao = new MeteoDAO();
		Map<String, Float> mtemp = new HashMap<String, Float>(dao.getMedie(mese));
		String result = "";
		for (String s : mtemp.keySet()) {
			{
				result += s + " " + Float.toString(mtemp.get(s)) + "\n";
			}

		}
		return result;
	}

	public void popolaBean(int mese) {
		MeteoDAO dao = new MeteoDAO();
		citta = new LinkedList<Citta>(dao.getAllCitta());

		for (Citta c : citta) {
			c.setRilevamenti(dao.getAllRilevamentiLocalitaMese(mese, c.getNome()));
			}
	}
	
	

	public void scegli(List<SimpleCity> parziale, int livello, List<SimpleCity> best) {
		if (parziale.size() == 15) {
			if (this.controllaParziale(parziale) && this.punteggioSoluzione(parziale) > this.punteggioSoluzione(best)) {
				best.clear();
				best.addAll(parziale);
			}
			return;
		} else {
			for(Citta ctemp:citta){
				if( ctemp.getCounter()<7){
					
					SimpleCity stemp= new SimpleCity(ctemp.getNome());
					stemp.setCosto(COST*(ctemp.getRilevamenti().get(livello).getUmidita()));
							
					parziale.add(stemp);
					ctemp.increaseCounter();
					
					if(!)
					scegli(parziale,livello+1,best);
					
					
					parziale.remove(stemp);
					ctemp.decreaseCounter();
				}
				
			}

		}

	}

	public String trovaSequenza(int mese) {
		this.popolaBean(mese);
		List<SimpleCity> parziale = new LinkedList<SimpleCity>();
		List<SimpleCity> best = new LinkedList<SimpleCity>();
		String result = "";

		scegli(parziale, 0, best);

		for (SimpleCity ctemp : best) {
			result += ctemp.getNome() + "\n";
		}
		return result;
	}

	private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata) {
		double score = 0.0;

		return score;
	}

	private boolean controllaParziale(List<SimpleCity> parziale) {

		for (Citta ctemp : citta) {
			SimpleCity stemp=new SimpleCity(ctemp.getNome());
			if (!parziale.contains(stemp))
				return false;
		}
		return true;
	}

}
