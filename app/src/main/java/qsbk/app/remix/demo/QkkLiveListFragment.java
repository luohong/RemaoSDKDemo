package qsbk.app.remix.demo;

import qsbk.app.core.net.UrlConstants;
import qsbk.app.live.ui.list.LiveListFragment;

public class QkkLiveListFragment extends LiveListFragment {
    @Override
    public String getTabIndex() {
        return "qkk";
    }

    @Override
    public String getLiveRequestUrl() {
        return UrlConstants.LIVE_LIST;
    }
}
