package com.android.internal.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import com.android.internal.R;

/* loaded from: classes3.dex */
public class ResolverTargetActionsDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
    private static final int APP_INFO_INDEX = 1;
    private static final String NAME_KEY = "componentName";
    private static final String PINNED_KEY = "pinned";
    private static final String TITLE_KEY = "title";
    private static final int TOGGLE_PIN_INDEX = 0;

    public ResolverTargetActionsDialogFragment() {
    }

    public ResolverTargetActionsDialogFragment(CharSequence title, ComponentName name, boolean pinned) {
        Bundle args = new Bundle();
        args.putCharSequence("title", title);
        args.putParcelable(NAME_KEY, name);
        args.putBoolean("pinned", pinned);
        setArguments(args);
    }

    @Override // android.app.DialogFragment
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int itemRes;
        Bundle args = getArguments();
        if (args.getBoolean("pinned", false)) {
            itemRes = R.array.resolver_target_actions_unpin;
        } else {
            itemRes = R.array.resolver_target_actions_pin;
        }
        return new AlertDialog.Builder(getContext()).setCancelable(true).setItems(itemRes, this).setTitle(args.getCharSequence("title")).create();
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialog, int which) {
        Bundle args = getArguments();
        ComponentName name = (ComponentName) args.getParcelable(NAME_KEY);
        if (which != 0) {
            if (which == 1) {
                Intent in = new Intent().setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.fromParts("package", name.getPackageName(), null)).addFlags(524288);
                startActivity(in);
            }
        } else {
            SharedPreferences sp = ChooserActivity.getPinnedSharedPrefs(getContext());
            String key = name.flattenToString();
            boolean currentVal = sp.getBoolean(name.flattenToString(), false);
            if (!currentVal) {
                sp.edit().putBoolean(key, true).apply();
            } else {
                sp.edit().remove(key).apply();
            }
            ((ChooserActivity) getActivity()).handlePackagesChanged();
        }
        dismiss();
    }
}
