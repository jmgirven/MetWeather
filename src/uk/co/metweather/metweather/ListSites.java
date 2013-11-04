package uk.co.metweather.metweather;

public class ListSites {

    static String[] Sites = {
        "Bramblemet",
        "Cambermet",
        "Chimet",
        "Sotonmet"
    };
    
    static String[] BasicURLs = {
    	"http://www.bramblemet.co.uk/",
    	"http://www.cambermet.co.uk/",
    	"http://www.chimet.co.uk/",
    	"http://www.sotonmet.co.uk/"
    };

    static String[] CodeURLs = {
    	ListSites.BasicURLs[0] + "(S(odixqznxuchbba45ylprv0yy))/",
    	ListSites.BasicURLs[1] + "(S(vc0udn3vjxrurg250elykq45))/",
    	ListSites.BasicURLs[2] + "(S(q55jcmuygc55ay45pocnryi5))/",
    	ListSites.BasicURLs[3] + "(S(nr523daobmlcmtuozjwswz3q))/"
    };

    static String[] HomeURLs = {
    	ListSites.CodeURLs[0] + "default.aspx",
    	ListSites.CodeURLs[1] + "default.aspx",
    	ListSites.CodeURLs[2] + "default.aspx",
    	ListSites.CodeURLs[3] + "default.aspx"
    };

    static String[] ImageURLs = {
    	ListSites.CodeURLs[0] + "GetImage.ashx?src=bra",
    	ListSites.CodeURLs[1] + "GetImage.ashx?src=cam",
    	ListSites.CodeURLs[2] + "GetImage.ashx?src=Chi",
    	ListSites.CodeURLs[3] + "GetImage.ashx?src=sot"
    };

    static String[] CompassURLs = {
    	ListSites.BasicURLs[0] + "compass/comp",
    	ListSites.BasicURLs[1] + "compass/comp",
    	ListSites.BasicURLs[2] + "compass/comp",
    	ListSites.BasicURLs[3] + "compass/comp"
    };

    static String[] Conditions = {
        "Summary",
        "Wind",
        "Sea",
        "Atmospheric"
    };

    static String[] ConditionsTwo = {
        "Summary & Wind",
        "Sea & Atmospheric"
    };
}
