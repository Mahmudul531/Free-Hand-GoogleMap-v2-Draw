package s.mahmud.com.mapdraw;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by user on 7/12/2017.
 */

public class YouApplication extends Application {
        @Override
        protected void attachBaseContext(Context base) {
            super.attachBaseContext(base);
            MultiDex.install(this);
        }
}
