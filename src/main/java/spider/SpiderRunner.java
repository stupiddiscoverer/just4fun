package spider;

import test.TestMain;

public class SpiderRunner extends Thread{
    int start, end;

    public SpiderRunner(int s, int e) {
        start = s;
        end = e;
    }

    @Override
    public void run(){
        int s = start, e = end;
        VideoSpider videoSpider = new VideoSpider();
        videoSpider.download(s, e);
    }
}
