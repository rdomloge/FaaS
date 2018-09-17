package catholicon;

import java.util.List;
import java.util.Map;

import com.example.faas.common.Function;

import catholicon.domain.Match;
import catholicon.controller.MatchCardController;

public class CatholiconRecentMatchesSpiderFunction extends RecentMatchResultsSpider implements Function<List<Match>> {

	private Map<String, String> params;
	
	private Map<String, String> config;
	
	@Override
	public List<Match> call() {
		matchCardController = new MatchCardController();
		spiderLatestResults();
		return getRecentMatches();
	}

	@Override
	public void setJobParams(Map<String, String> params) {
		this.params = params;
	}

	@Override
	public void setStaticConfig(Map<String, String> config) {
		this.config = config;
		super.BASE = config.get("BASE_URL");
		System.out.println("Set base URL to "+super.BASE);
	}

}
