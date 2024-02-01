package com.android.internal.app;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.android.internal.R;
import com.android.internal.widget.ResolverDrawerLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: classes3.dex */
public class AccessibilityButtonChooserActivity extends Activity {
    private static final String MAGNIFICATION_COMPONENT_ID = "com.android.server.accessibility.MagnificationController";
    private AccessibilityButtonTarget mMagnificationTarget = null;
    private List<AccessibilityButtonTarget> mTargets = null;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accessibility_button_chooser);
        ResolverDrawerLayout rdl = (ResolverDrawerLayout) findViewById(16908906);
        if (rdl != null) {
            rdl.setOnDismissedListener(new ResolverDrawerLayout.OnDismissedListener() { // from class: com.android.internal.app.-$$Lambda$EK3sgUmlvAVQupMeTV9feOrWuPE
                @Override // com.android.internal.widget.ResolverDrawerLayout.OnDismissedListener
                public final void onDismissed() {
                    AccessibilityButtonChooserActivity.this.finish();
                }
            });
        }
        String component = Settings.Secure.getString(getContentResolver(), Settings.Secure.ACCESSIBILITY_BUTTON_TARGET_COMPONENT);
        if (TextUtils.isEmpty(component)) {
            TextView prompt = (TextView) findViewById(R.id.accessibility_button_prompt);
            prompt.setVisibility(0);
        }
        this.mMagnificationTarget = new AccessibilityButtonTarget(this, MAGNIFICATION_COMPONENT_ID, R.string.accessibility_magnification_chooser_text, R.drawable.ic_accessibility_magnification);
        this.mTargets = getServiceAccessibilityButtonTargets(this);
        if (Settings.Secure.getInt(getContentResolver(), Settings.Secure.ACCESSIBILITY_DISPLAY_MAGNIFICATION_NAVBAR_ENABLED, 0) == 1) {
            this.mTargets.add(this.mMagnificationTarget);
        }
        if (this.mTargets.size() < 2) {
            finish();
        }
        GridView gridview = (GridView) findViewById(R.id.accessibility_button_chooser_grid);
        gridview.setAdapter((ListAdapter) new TargetAdapter());
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.android.internal.app.-$$Lambda$AccessibilityButtonChooserActivity$VBT2N_0vKxB2VkOg6zxi5sAX6xc
            @Override // android.widget.AdapterView.OnItemClickListener
            public final void onItemClick(AdapterView adapterView, View view, int i, long j) {
                r0.onTargetSelected(AccessibilityButtonChooserActivity.this.mTargets.get(i));
            }
        });
    }

    private static List<AccessibilityButtonTarget> getServiceAccessibilityButtonTargets(Context context) {
        AccessibilityManager ams = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> services = ams.getEnabledAccessibilityServiceList(-1);
        if (services == null) {
            return Collections.emptyList();
        }
        ArrayList<AccessibilityButtonTarget> targets = new ArrayList<>(services.size());
        for (AccessibilityServiceInfo info : services) {
            if ((info.flags & 256) != 0) {
                targets.add(new AccessibilityButtonTarget(context, info));
            }
        }
        return targets;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTargetSelected(AccessibilityButtonTarget target) {
        Settings.Secure.putString(getContentResolver(), Settings.Secure.ACCESSIBILITY_BUTTON_TARGET_COMPONENT, target.getId());
        finish();
    }

    /* loaded from: classes3.dex */
    private class TargetAdapter extends BaseAdapter {
        private TargetAdapter() {
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return AccessibilityButtonChooserActivity.this.mTargets.size();
        }

        @Override // android.widget.Adapter
        public Object getItem(int position) {
            return null;
        }

        @Override // android.widget.Adapter
        public long getItemId(int position) {
            return position;
        }

        @Override // android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = AccessibilityButtonChooserActivity.this.getLayoutInflater();
            View root = inflater.inflate(R.layout.accessibility_button_chooser_item, parent, false);
            AccessibilityButtonTarget target = (AccessibilityButtonTarget) AccessibilityButtonChooserActivity.this.mTargets.get(position);
            ImageView iconView = (ImageView) root.findViewById(R.id.accessibility_button_target_icon);
            TextView labelView = (TextView) root.findViewById(R.id.accessibility_button_target_label);
            iconView.setImageDrawable(target.getDrawable());
            labelView.setText(target.getLabel());
            return root;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class AccessibilityButtonTarget {
        public Drawable mDrawable;
        public String mId;
        public CharSequence mLabel;

        public AccessibilityButtonTarget(Context context, AccessibilityServiceInfo serviceInfo) {
            this.mId = serviceInfo.getComponentName().flattenToString();
            this.mLabel = serviceInfo.getResolveInfo().loadLabel(context.getPackageManager());
            this.mDrawable = serviceInfo.getResolveInfo().loadIcon(context.getPackageManager());
        }

        public AccessibilityButtonTarget(Context context, String id, int labelResId, int iconRes) {
            this.mId = id;
            this.mLabel = context.getText(labelResId);
            this.mDrawable = context.getDrawable(iconRes);
        }

        public String getId() {
            return this.mId;
        }

        public CharSequence getLabel() {
            return this.mLabel;
        }

        public Drawable getDrawable() {
            return this.mDrawable;
        }
    }
}
