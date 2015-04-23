package ir.assignments.three;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.Set;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;

import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class Crawler extends WebCrawler {
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp3|zip|gz))$");
	private static int ID = 0;
	private static ArrayList<String> ret = new ArrayList<String>();
	
	

	
	/**
	* This method receives two parameters. The first parameter is the page
	* in which we have discovered this new url and the second parameter is
	* the new url. You should implement this function to specify whether
	* the given url should be crawled or not (based on your crawling logic).
	* In this example, we are instructing the crawler to ignore urls that
	* have css, js, git, ... extensions and to only accept urls that start
	* with "http://www.ics.uci.edu/". In this case, we didn't need the
	* referringPage parameter to make the decision.
	*/
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches()
		&& href.startsWith("http://www.ics.uci.edu/");
	}
	
	/**
	* This function is called when a page is fetched and ready
	* to be processed by your program.
	*/	
	public void visit(Page page) {
		int docid = page.getWebURL().getDocid();
	    String url = page.getWebURL().getURL();
	    String domain = page.getWebURL().getDomain();
	    String path = page.getWebURL().getPath();
	    String subDomain = page.getWebURL().getSubDomain();
	    String parentUrl = page.getWebURL().getParentUrl();
	    String anchor = page.getWebURL().getAnchor();

	    logger.debug("Docid: {}", docid);
	    logger.info("URL: {}", url);
	    logger.debug("Domain: '{}'", domain);
	    logger.debug("Sub-domain: '{}'", subDomain);
	    logger.debug("Path: '{}'", path);
	    logger.debug("Parent page: {}", parentUrl);
	    logger.debug("Anchor text: {}", anchor);

	    if (page.getParseData() instanceof HtmlParseData) {
	      HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
	      String text = htmlParseData.getText();
	      String html = htmlParseData.getHtml();
	      Set<WebURL> links = htmlParseData.getOutgoingUrls();

	      logger.debug("Text length: {}", text.length());
	      logger.debug("Html length: {}", html.length());
	      logger.debug("Number of outgoing links: {}", links.size());
	    }

	    Header[] responseHeaders = page.getFetchResponseHeaders();
	    if (responseHeaders != null) {
	      logger.debug("Response headers:");
	      for (Header header : responseHeaders) {
	        logger.debug("\t{}: {}", header.getName(), header.getValue());
	      }
	    }

	    logger.debug("=============");
		
		
		/*
		String url = page.getWebURL().getURL();
		System.out.println("URL: " + url);
		
		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			Set<WebURL> links = htmlParseData.getOutgoingUrls();
		ret.add(url + " " + ID);
		ID++;
//		System.out.println(ret);
		
		System.out.println("Text length: " + text.length());
		System.out.println("Html length: " + html.length());
		System.out.println("Number of outgoing links: " + links.size());
		}
		 */
	}
	
	/**
	 * This method is for testing purposes only. It does not need to be used
	 * to answer any of the questions in the assignment. However, it must
	 * function as specified so that your crawler can be verified programatically.
	 * 
	 * This methods performs a crawl starting at the specified seed URL. Returns a
	 * collection containing all URLs visited during the crawl.
	 * @throws Exception 
	 */
	public static Collection<String> crawl(String seedURL) throws Exception {

		
		String crawlStorageFolder = "C:\\Users\\Ahsan Memon\\workspaceJava\\Project2a\\config";
        int numberOfCrawlers = 7;
        int politenessDelay = 300;
   
        
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        
        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        
        controller.addSeed(seedURL);
        
        CrawlConfig crawlConfig = new CrawlConfig();
        crawlConfig.setUserAgentString("UCI Inf141-CS121 crawler 59212236 43513937");
        crawlConfig.setPolitenessDelay(politenessDelay);
		
        controller.start(Crawler.class, numberOfCrawlers);
        
		return ret;
	}
	
	public static void main(String[] args) throws Exception{
		String seedURL = "http://www.ics.uci.edu/";
		crawl(seedURL);

	}
}


