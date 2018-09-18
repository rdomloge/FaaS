package catholicon;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.example.faas.common.Function;

import catholicon.domain.Match;
import catholicon.controller.MatchCardController;

public class CatholiconRecentMatchesSpiderFunction extends RecentMatchResultsSpider implements Function<List<Match>> {

	private Properties params;
	
	private Properties config;
	
	@Override
	public List<Match> call() {
		matchCardController = new MatchCardController();
		spiderLatestResults();
		return getRecentMatches();
	}

	@Override
	public void setJobParams(Properties params) {
		this.params = params;
	}

	@Override
	public void setStaticConfig(Properties config) {
		this.config = config;
		super.BASE = config.getProperty("BASE_URL");
		System.out.println("Set base URL to "+super.BASE);
	}

}
