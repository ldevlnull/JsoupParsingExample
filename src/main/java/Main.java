import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private final static String URL = "https://www.hltv.org/matches";

    public static void main(String[] args) {
        try {
            Document doc = Jsoup.connect(URL)
                    .userAgent("Chrome/4.0.249.0 Safari/532.5")
                    .referrer("http://google.com").get();

            Elements elements = doc.getElementsByClass("upcoming-match");
            List<MatchModel> matchModelList = new ArrayList<MatchModel>();

            for (Element element: elements) {
                MatchModel model = new MatchModel();
                String t = element
                        .getElementsByAttribute("data-time-format")
                        .get(0).text();

                int hours = Integer.parseInt(t.substring(0, 2));
                int minutes = Integer.parseInt(t.substring(3, 5));
                int millieSec = hours*3600*1000 + minutes*60*1000;

                model.setTime(new Time(millieSec));

                matchModelList.add(model);
            }

            matchModelList.forEach(e ->
                    System.out.println(e.getTime().toLocalTime()));

        } catch (IOException e){
            System.out.println("Document not found");
        }
    }
}
