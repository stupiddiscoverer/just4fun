package spider;

import java.util.ArrayList;

public class AllSiteDownload {
    final int poolSize = 24;

    public static void main(String[] args) {
        for (int i=1; i<224; i++) {
            if (i == 10 || i == 127)
                continue;
            DownSite downSite = new DownSite(i);
            System.out.println("start Thread " + i);
            downSite.start();
        }
    }
}
