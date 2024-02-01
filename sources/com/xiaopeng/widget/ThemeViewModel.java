package com.xiaopeng.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.util.ArrayUtils;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes3.dex */
public class ThemeViewModel {
    private static final String BACKGROUND = "background";
    private static final String SRC = "src";
    private static final String STYLE = "style";
    private static final String TEXT_COLOR = "textColor";
    private static final String THEME = "theme";
    public static final HashMap<String, Integer> sAttributeMap = new HashMap<>();
    private HashMap<String, Integer> mAttributeSet;
    private OnCallback mCallback;
    private int mUiMode;

    /* loaded from: classes3.dex */
    public interface OnCallback {
        void onThemeChanged();
    }

    static {
        sAttributeMap.clear();
        sAttributeMap.put("style", 0);
        sAttributeMap.put(THEME, 16842752);
        sAttributeMap.put(BACKGROUND, 16842964);
        sAttributeMap.put(SRC, 16843033);
        sAttributeMap.put(TEXT_COLOR, 16842904);
    }

    private ThemeViewModel(Context context, HashMap<String, Integer> attributes) {
        this.mUiMode = context.getResources().getConfiguration().uiMode;
        this.mAttributeSet = attributes != null ? attributes : new HashMap<>();
    }

    public static ThemeViewModel create(Context context) {
        HashMap<String, Integer> map = new HashMap<>();
        return new ThemeViewModel(context, map);
    }

    public static ThemeViewModel create(Context context, AttributeSet attrs) {
        HashMap<String, Integer> map = resolveAttribute(context, attrs, 0, 0);
        return new ThemeViewModel(context, map);
    }

    public static ThemeViewModel create(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        HashMap<String, Integer> map = resolveAttribute(context, attrs, defStyleAttr, defStyleRes);
        return new ThemeViewModel(context, map);
    }

    public void setCallback(OnCallback callback) {
        this.mCallback = callback;
    }

    public void onAttachedToWindow(View view) {
        Context context = view.getContext();
        int uiMode = context.getResources().getConfiguration().uiMode;
        if (isUiModeChanged(uiMode)) {
            updateThemeResource(view);
        }
        this.mUiMode = uiMode;
    }

    public void onDetachedFromWindow(View view) {
    }

    public void onWindowFocusChanged(View view, boolean hasWindowFocus) {
        if (hasWindowFocus) {
            Context context = view.getContext();
            int uiMode = context.getResources().getConfiguration().uiMode;
            if (isUiModeChanged(uiMode)) {
                updateThemeResource(view);
            }
            this.mUiMode = uiMode;
        }
    }

    public void onWindowVisibilityChanged(View view, int visibility) {
        if (visibility == 0) {
            Context context = view.getContext();
            int uiMode = context.getResources().getConfiguration().uiMode;
            if (isUiModeChanged(uiMode)) {
                updateThemeResource(view);
            }
            this.mUiMode = uiMode;
        }
    }

    public void onConfigurationChanged(View view, Configuration newConfig) {
        if (isThemeChanged(newConfig)) {
            updateThemeResource(view);
            this.mUiMode = newConfig != null ? newConfig.uiMode : this.mUiMode;
        }
    }

    public void setTextColor(int resid) {
        this.mAttributeSet.put(TEXT_COLOR, Integer.valueOf(resid));
    }

    private void updateThemeResource(View view) {
        updateAttribute(view, this.mAttributeSet);
        OnCallback onCallback = this.mCallback;
        if (onCallback != null) {
            onCallback.onThemeChanged();
        }
    }

    private static HashMap<String, Integer> resolveAttribute(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        int resId;
        HashMap<String, Integer> map = new HashMap<>();
        boolean hasTheme = false;
        if (attrs != null) {
            int N = attrs.getAttributeCount();
            for (int i = 0; i < N; i++) {
                try {
                    String name = attrs.getAttributeName(i);
                    String value = attrs.getAttributeValue(i);
                    if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(value) && hasAttribute(name) && value.startsWith("@")) {
                        if (isThemeAttribute(name)) {
                            hasTheme = true;
                        } else {
                            map.put(name, Integer.valueOf(Integer.parseInt(value.substring(1))));
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        if (hasTheme) {
            context.getTheme();
            for (String key : sAttributeMap.keySet()) {
                int value2 = sAttributeMap.get(key).intValue();
                if (value2 != 0 && (resId = getThemeResourceId(value2, context, attrs, defStyleAttr, defStyleRes)) != 0) {
                    map.put(key, Integer.valueOf(resId));
                }
            }
        }
        if (defStyleAttr > 0 || defStyleRes > 0) {
            try {
                HashMap<String, Integer> maps = sAttributeMap;
                HashMap<Integer, String> temp = new HashMap<>();
                for (String key2 : maps.keySet()) {
                    temp.put(maps.get(key2), key2);
                }
                int[] attrArrays = ArrayUtils.convertToIntArray(new ArrayList(temp.keySet()));
                if (attrArrays != null && !temp.isEmpty()) {
                    for (int attr : attrArrays) {
                        String key3 = temp.get(Integer.valueOf(attr));
                        int resId2 = getThemeResourceId(attr, context, null, defStyleAttr, defStyleRes);
                        if (!map.containsKey(key3) && resId2 != 0) {
                            map.put(key3, Integer.valueOf(resId2));
                        }
                    }
                }
            } catch (Exception e2) {
            }
        }
        return map;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean hasAttribute(String attr) {
        if (!TextUtils.isEmpty(attr)) {
            return sAttributeMap.containsKey(attr);
        }
        return false;
    }

    private static boolean isThemeAttribute(String attr) {
        if (TextUtils.isEmpty(attr)) {
            return false;
        }
        return THEME.equals(attr) || "style".equals(attr);
    }

    private static int getThemeResourceId(int attr, Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray ta = context.obtainStyledAttributes(attrs, new int[]{attr}, defStyleAttr, defStyleRes);
        if (ta != null) {
            try {
                int resId = ta.getResourceId(0, 0);
                boolean hasThemeOverride = resId != 0;
                if (hasThemeOverride) {
                    ta.recycle();
                    return resId;
                }
            } catch (Exception e) {
            } catch (Throwable th) {
                ta.recycle();
                throw th;
            }
            ta.recycle();
        }
        return 0;
    }

    private static void updateAttribute(final View view, final HashMap<String, Integer> map) {
        if (view != null && map != null) {
            view.post(new Runnable() { // from class: com.xiaopeng.widget.ThemeViewModel.1
                @Override // java.lang.Runnable
                public void run() {
                    for (String attr : map.keySet()) {
                        if (ThemeViewModel.hasAttribute(attr)) {
                            ThemeViewModel.setViewResource(view.getContext(), view, attr, ((Integer) map.get(attr)).intValue());
                        }
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void setViewResource(Context context, View view, String attr, int resId) {
        if (context != null && view != null && !TextUtils.isEmpty(attr)) {
            Resources res = context.getResources();
            Resources.Theme theme = context.getTheme();
            try {
                if (BACKGROUND.equals(attr)) {
                    view.setBackground(context.getDrawable(resId));
                } else if (SRC.equals(attr)) {
                    ImageView imageView = (ImageView) view;
                    imageView.setImageDrawable(context.getDrawable(resId));
                } else if (TEXT_COLOR.equals(attr)) {
                    ((TextView) view).setTextColor(res.getColorStateList(resId, theme));
                }
                view.refreshDrawableState();
            } catch (Exception e) {
            }
        }
    }

    private boolean isUiModeChanged(int uiMode) {
        return uiMode != this.mUiMode;
    }

    protected static boolean isThemeChanged(Configuration newConfig) {
        return (newConfig == null || (newConfig.uiMode & 192) == 0) ? false : true;
    }
}
