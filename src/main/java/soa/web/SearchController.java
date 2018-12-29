package soa.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class SearchController {

  private final ProducerTemplate producerTemplate;

  @Autowired
  public SearchController(ProducerTemplate producerTemplate) {
    this.producerTemplate = producerTemplate;
  }

  @RequestMapping("/")
  public String index() {
    return "index";
  }


  @RequestMapping(value = "/search")
  @ResponseBody
  public Object search(@RequestParam("q") String q) {

    String keywords;
    Map<String,Object> headers = new HashMap<String,Object>();

    int positionOfMax = q.indexOf("max");
    
    if (positionOfMax>=0) {
      // max parameter is being used
      String[] keywordsAndMax = q.substring(positionOfMax).split(":");
      int maxNumOfTwitts =Integer.parseInt(keywordsAndMax[1]);
      
      keywords = q.substring(0,positionOfMax);

      headers.put("CamelTwitterCount",maxNumOfTwitts);

    } else {
      // max parameter is NOT being used
      keywords = q;
    }
    
    headers.put("CamelTwitterKeywords",keywords);

    return producerTemplate.requestBodyAndHeaders("direct:search", "", headers);

  }
}