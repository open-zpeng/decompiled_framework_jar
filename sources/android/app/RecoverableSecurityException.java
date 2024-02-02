package android.app;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.service.notification.ZenModeConfig;
import com.android.internal.R;
import com.android.internal.util.Preconditions;
/* loaded from: classes.dex */
public final class RecoverableSecurityException extends SecurityException implements Parcelable {
    public static final Parcelable.Creator<RecoverableSecurityException> CREATOR = new Parcelable.Creator<RecoverableSecurityException>() { // from class: android.app.RecoverableSecurityException.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RecoverableSecurityException createFromParcel(Parcel source) {
            return new RecoverableSecurityException(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RecoverableSecurityException[] newArray(int size) {
            return new RecoverableSecurityException[size];
        }
    };
    private static final String TAG = "RecoverableSecurityException";
    private final RemoteAction mUserAction;
    private final CharSequence mUserMessage;

    public synchronized RecoverableSecurityException(Parcel in) {
        this(new SecurityException(in.readString()), in.readCharSequence(), RemoteAction.CREATOR.createFromParcel(in));
    }

    public synchronized RecoverableSecurityException(Throwable cause, CharSequence userMessage, RemoteAction userAction) {
        super(cause.getMessage());
        this.mUserMessage = (CharSequence) Preconditions.checkNotNull(userMessage);
        this.mUserAction = (RemoteAction) Preconditions.checkNotNull(userAction);
    }

    @Deprecated
    public synchronized RecoverableSecurityException(Throwable cause, CharSequence userMessage, CharSequence userActionTitle, PendingIntent userAction) {
        this(cause, userMessage, new RemoteAction(Icon.createWithResource(ZenModeConfig.SYSTEM_AUTHORITY, (int) R.drawable.ic_restart), userActionTitle, userActionTitle, userAction));
    }

    public synchronized CharSequence getUserMessage() {
        return this.mUserMessage;
    }

    public synchronized RemoteAction getUserAction() {
        return this.mUserAction;
    }

    @Deprecated
    public synchronized void showAsNotification(Context context) {
        NotificationManager nm = (NotificationManager) context.getSystemService(NotificationManager.class);
        String channelId = "RecoverableSecurityException_" + this.mUserAction.getActionIntent().getCreatorUid();
        nm.createNotificationChannel(new NotificationChannel(channelId, TAG, 3));
        showAsNotification(context, channelId);
    }

    public synchronized void showAsNotification(Context context, String channelId) {
        NotificationManager nm = (NotificationManager) context.getSystemService(NotificationManager.class);
        Notification.Builder builder = new Notification.Builder(context, channelId).setSmallIcon(R.drawable.ic_print_error).setContentTitle(this.mUserAction.getTitle()).setContentText(this.mUserMessage).setContentIntent(this.mUserAction.getActionIntent()).setCategory(Notification.CATEGORY_ERROR);
        nm.notify(TAG, this.mUserAction.getActionIntent().getCreatorUid(), builder.build());
    }

    public synchronized void showAsDialog(Activity activity) {
        LocalDialog dialog = new LocalDialog();
        Bundle args = new Bundle();
        args.putParcelable(TAG, this);
        dialog.setArguments(args);
        String tag = "RecoverableSecurityException_" + this.mUserAction.getActionIntent().getCreatorUid();
        FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment old = fm.findFragmentByTag(tag);
        if (old != null) {
            ft.remove(old);
        }
        ft.add(dialog, tag);
        ft.commitAllowingStateLoss();
    }

    /* loaded from: classes.dex */
    public static class LocalDialog extends DialogFragment {
        @Override // android.app.DialogFragment
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final RecoverableSecurityException e = (RecoverableSecurityException) getArguments().getParcelable(RecoverableSecurityException.TAG);
            return new AlertDialog.Builder(getActivity()).setMessage(e.mUserMessage).setPositiveButton(e.mUserAction.getTitle(), new DialogInterface.OnClickListener() { // from class: android.app.-$$Lambda$RecoverableSecurityException$LocalDialog$r8YNkpjWIZllJsQ_8eA0q51FU5Q
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    RecoverableSecurityException.this.mUserAction.getActionIntent().send();
                }
            }).setNegativeButton(android.R.string.cancel, (DialogInterface.OnClickListener) null).create();
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getMessage());
        dest.writeCharSequence(this.mUserMessage);
        this.mUserAction.writeToParcel(dest, flags);
    }
}
