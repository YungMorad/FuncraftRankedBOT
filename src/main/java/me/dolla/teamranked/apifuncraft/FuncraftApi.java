package me.dolla.teamranked.apifuncraft;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Objects;

public class FuncraftApi {

    public static String getKills(String player) throws IOException {
        Document doc = get(player);
        Elements elements = doc.getElementsByClass("stats-value stats-value-daily");
        if (elements != null && elements.size() > 0) {
            return elements.get(6).text();
        } else {
            return null;
        }
    }

    public static String getDeaths(String player) throws IOException {
        Document doc = get(player);
        Elements elements = doc.getElementsByClass("stats-value stats-value-daily");
        if (elements != null && elements.size() > 0) {
            return elements.get(7).text();
        } else {
            return null;
        }
    }

    public static String getWins(String player) throws IOException {
        Document doc = get(player);
        Elements elements = doc.getElementsByClass("stats-value stats-value-daily");
        if (elements != null && elements.size() > 0) {
            return elements.get(3).text();
        } else {
            return null;
        }
    }

    public static String getLooses(String player) throws IOException {
        Document doc = get(player);
        Elements elements = doc.getElementsByClass("stats-value stats-value-daily");
        if (elements != null && elements.size() > 0) {
            return elements.get(4).text();
        } else {
            return null;
        }
    }

    public static String getJoueursTitle(String name) throws IOException {
        Document doc = get(name);
        Elements elements = doc.select("h1");
        if (elements != null && elements.size() > 0) {
            return elements.get(0).text();
        } else {
            return null;
        }
    }

    public static String getBanniTitle(String name) throws IOException {
        Document doc = get(name);
        Elements elements = doc.getElementsByClass("player-alert");
        if (elements != null && elements.size() > 0) {
            return elements.get(0).text();
        } else {
            return null;
        }
    }

    public static boolean hasFuncraftProfile(String name) throws IOException {
        if(getJoueursTitle(name).equals("Joueurs")) {
            return false;
        } else {
            return true;
        }

    }

    public static boolean hasBanFuncraft(String name) throws IOException {
        return Objects.equals(getBanniTitle(name), "Ce joueur est banni (à vie / très longtemps) ! Il n'a pas respecté les règles du serveur.");
    }


    public static Element getElementByClass(Document doc, String className) {
        Elements elements = doc.getElementsByClass(className);
        if (elements != null && elements.size() > 0) {
            return elements.get(6);
        } else {
            return null;
        }
    }

    public static Document get(String player) throws IOException {
        String baseUrl = "https://www.funcraft.net/fr/joueurs?q=";
        String url = baseUrl + player;

        return Jsoup.connect(url).get();
    }
}
