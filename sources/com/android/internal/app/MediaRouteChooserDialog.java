package com.android.internal.app;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaRouter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.android.internal.R;
import java.util.Comparator;

/* loaded from: classes3.dex */
public class MediaRouteChooserDialog extends Dialog {
    private RouteAdapter mAdapter;
    private boolean mAttachedToWindow;
    private final MediaRouterCallback mCallback;
    private Button mExtendedSettingsButton;
    private View.OnClickListener mExtendedSettingsClickListener;
    private ListView mListView;
    private int mRouteTypes;
    private final MediaRouter mRouter;

    public MediaRouteChooserDialog(Context context, int theme) {
        super(context, theme);
        this.mRouter = (MediaRouter) context.getSystemService(Context.MEDIA_ROUTER_SERVICE);
        this.mCallback = new MediaRouterCallback();
    }

    public int getRouteTypes() {
        return this.mRouteTypes;
    }

    public void setRouteTypes(int types) {
        if (this.mRouteTypes != types) {
            this.mRouteTypes = types;
            if (this.mAttachedToWindow) {
                this.mRouter.removeCallback(this.mCallback);
                this.mRouter.addCallback(types, this.mCallback, 1);
            }
            refreshRoutes();
        }
    }

    public void setExtendedSettingsClickListener(View.OnClickListener listener) {
        if (listener != this.mExtendedSettingsClickListener) {
            this.mExtendedSettingsClickListener = listener;
            updateExtendedSettingsButton();
        }
    }

    public boolean onFilterRoute(MediaRouter.RouteInfo route) {
        return !route.isDefault() && route.isEnabled() && route.matchesTypes(this.mRouteTypes);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Dialog
    public void onCreate(Bundle savedInstanceState) {
        int i;
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(3);
        setContentView(R.layout.media_route_chooser_dialog);
        if (this.mRouteTypes == 4) {
            i = R.string.media_route_chooser_title_for_remote_display;
        } else {
            i = R.string.media_route_chooser_title;
        }
        setTitle(i);
        getWindow().setFeatureDrawableResource(3, isLightTheme(getContext()) ? R.drawable.ic_media_route_off_holo_light : R.drawable.ic_media_route_off_holo_dark);
        this.mAdapter = new RouteAdapter(getContext());
        this.mListView = (ListView) findViewById(R.id.media_route_list);
        this.mListView.setAdapter((ListAdapter) this.mAdapter);
        this.mListView.setOnItemClickListener(this.mAdapter);
        this.mListView.setEmptyView(findViewById(16908292));
        this.mExtendedSettingsButton = (Button) findViewById(R.id.media_route_extended_settings_button);
        updateExtendedSettingsButton();
    }

    private void updateExtendedSettingsButton() {
        Button button = this.mExtendedSettingsButton;
        if (button != null) {
            button.setOnClickListener(this.mExtendedSettingsClickListener);
            this.mExtendedSettingsButton.setVisibility(this.mExtendedSettingsClickListener != null ? 0 : 8);
        }
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mAttachedToWindow = true;
        this.mRouter.addCallback(this.mRouteTypes, this.mCallback, 1);
        refreshRoutes();
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onDetachedFromWindow() {
        this.mAttachedToWindow = false;
        this.mRouter.removeCallback(this.mCallback);
        super.onDetachedFromWindow();
    }

    public void refreshRoutes() {
        if (this.mAttachedToWindow) {
            this.mAdapter.update();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isLightTheme(Context context) {
        TypedValue value = new TypedValue();
        return context.getTheme().resolveAttribute(16844176, value, true) && value.data != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public final class RouteAdapter extends ArrayAdapter<MediaRouter.RouteInfo> implements AdapterView.OnItemClickListener {
        private final LayoutInflater mInflater;

        public RouteAdapter(Context context) {
            super(context, 0);
            this.mInflater = LayoutInflater.from(context);
        }

        public void update() {
            clear();
            int count = MediaRouteChooserDialog.this.mRouter.getRouteCount();
            for (int i = 0; i < count; i++) {
                MediaRouter.RouteInfo route = MediaRouteChooserDialog.this.mRouter.getRouteAt(i);
                if (MediaRouteChooserDialog.this.onFilterRoute(route)) {
                    add(route);
                }
            }
            sort(RouteComparator.sInstance);
            notifyDataSetChanged();
        }

        @Override // android.widget.BaseAdapter, android.widget.ListAdapter
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override // android.widget.BaseAdapter, android.widget.ListAdapter
        public boolean isEnabled(int position) {
            return getItem(position).isEnabled();
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = this.mInflater.inflate(R.layout.media_route_list_item, parent, false);
            }
            MediaRouter.RouteInfo route = getItem(position);
            TextView text1 = (TextView) view.findViewById(16908308);
            TextView text2 = (TextView) view.findViewById(16908309);
            text1.setText(route.getName());
            CharSequence description = route.getDescription();
            if (TextUtils.isEmpty(description)) {
                text2.setVisibility(8);
                text2.setText("");
            } else {
                text2.setVisibility(0);
                text2.setText(description);
            }
            view.setEnabled(route.isEnabled());
            return view;
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MediaRouter.RouteInfo route = getItem(position);
            if (route.isEnabled()) {
                route.select();
                MediaRouteChooserDialog.this.dismiss();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public final class MediaRouterCallback extends MediaRouter.SimpleCallback {
        private MediaRouterCallback() {
        }

        @Override // android.media.MediaRouter.SimpleCallback, android.media.MediaRouter.Callback
        public void onRouteAdded(MediaRouter router, MediaRouter.RouteInfo info) {
            MediaRouteChooserDialog.this.refreshRoutes();
        }

        @Override // android.media.MediaRouter.SimpleCallback, android.media.MediaRouter.Callback
        public void onRouteRemoved(MediaRouter router, MediaRouter.RouteInfo info) {
            MediaRouteChooserDialog.this.refreshRoutes();
        }

        @Override // android.media.MediaRouter.SimpleCallback, android.media.MediaRouter.Callback
        public void onRouteChanged(MediaRouter router, MediaRouter.RouteInfo info) {
            MediaRouteChooserDialog.this.refreshRoutes();
        }

        @Override // android.media.MediaRouter.SimpleCallback, android.media.MediaRouter.Callback
        public void onRouteSelected(MediaRouter router, int type, MediaRouter.RouteInfo info) {
            MediaRouteChooserDialog.this.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class RouteComparator implements Comparator<MediaRouter.RouteInfo> {
        public static final RouteComparator sInstance = new RouteComparator();

        private RouteComparator() {
        }

        @Override // java.util.Comparator
        public int compare(MediaRouter.RouteInfo lhs, MediaRouter.RouteInfo rhs) {
            return lhs.getName().toString().compareTo(rhs.getName().toString());
        }
    }
}
