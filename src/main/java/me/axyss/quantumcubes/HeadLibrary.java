package me.axyss.quantumcubes;

import org.bukkit.entity.Player;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HeadLibrary {
    public static void main(Player p) {
        String html = "<html><body><div><h1>Title</h1><p>Paragraph 1</p><p>Paragraph 2</p></div></body></html>";
        Jsoup.connect("https://minecraft-heads.com/custom-heads/decoration/63222");
        Document doc = Jsoup.parse(html);

        Element titleElement = doc.select("textarea#UUID-Skin").first();
        String title = titleElement.text();
        p.sendMessage(title);
    }
}
