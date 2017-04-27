package cop4656.jrdbnntt.com.groupproject1;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Cristian Palencia on 4/4/2017.
 */

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetAdapter(this);
    }
}
