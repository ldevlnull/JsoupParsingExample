import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    private final static String URL = "https://www.hltv.org/matches";

    public static void main(String[] args) {
        try {
            Document doc = Jsoup.connect(URL)
                    .userAgent("Chrome/4.0.249.0 Safari/532.5")
                    .referrer("http://google.com").get();

            Elements elements = doc.getElementsByClass("upcoming-match");
            List<MatchModel> matchModelList = new ArrayList<>();

            for (Element element: elements) {
                if(!element.getElementsByClass("map-text").text().equalsIgnoreCase("")) {
                    String time = element
                            .getElementsByAttribute("data-time-format")
                            .get(0).text();
                    String teams[] = element.getElementsByClass("team")
                            .stream().map(Element::text)
                            .collect(Collectors.toList()).toArray(new String[]{});

                    MatchModel model = new MatchModel();
                    model.setTime(formatTime(time));
                    model.setType(element.getElementsByClass("map-text").text());
                    model.setTournament(element.getElementsByClass("event-name").text());

                    try {
                        model.setTeam1(teams[0]);
                        model.setTeam2(teams[1]);
                    } catch (IndexOutOfBoundsException e) {
                        model.setTeam1("No data");
                        model.setTeam2("No data");
                    }

                    matchModelList.add(model);
                }
            }

            matchModelList.forEach(System.out::println);

        } catch (IOException e){
            System.out.println("Document not found");
        }
    }

    private static Time formatTime(String time){
        int hours = Integer.parseInt(time.substring(0, 2));
        int minutes = Integer.parseInt(time.substring(3, 5));
        int millieSec = hours*3600*1000 + minutes*60*1000;

        return new Time(millieSec);
    }
}
