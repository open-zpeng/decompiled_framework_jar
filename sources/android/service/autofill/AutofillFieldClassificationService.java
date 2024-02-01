package android.service.autofill;

import android.annotation.SystemApi;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.service.autofill.IAutofillFieldClassificationService;
import android.util.Log;
import android.view.autofill.AutofillValue;
import com.android.internal.util.function.HexConsumer;
import com.android.internal.util.function.pooled.PooledLambda;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
@SystemApi
/* loaded from: classes2.dex */
public abstract class AutofillFieldClassificationService extends Service {
    public static final String EXTRA_SCORES = "scores";
    public static final String SERVICE_INTERFACE = "android.service.autofill.AutofillFieldClassificationService";
    public static final String SERVICE_META_DATA_KEY_AVAILABLE_ALGORITHMS = "android.autofill.field_classification.available_algorithms";
    public static final String SERVICE_META_DATA_KEY_DEFAULT_ALGORITHM = "android.autofill.field_classification.default_algorithm";
    private static final String TAG = "AutofillFieldClassificationService";
    private final Handler mHandler = new Handler(Looper.getMainLooper(), null, true);
    private AutofillFieldClassificationServiceWrapper mWrapper;

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void getScores(RemoteCallback callback, String algorithmName, Bundle algorithmArgs, List<AutofillValue> actualValues, String[] userDataValues) {
        Bundle data = new Bundle();
        float[][] scores = onGetScores(algorithmName, algorithmArgs, actualValues, Arrays.asList(userDataValues));
        if (scores != null) {
            data.putParcelable(EXTRA_SCORES, new Scores(scores));
        }
        callback.sendResult(data);
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.mWrapper = new AutofillFieldClassificationServiceWrapper();
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.mWrapper;
    }

    @SystemApi
    public float[][] onGetScores(String algorithm, Bundle algorithmOptions, List<AutofillValue> actualValues, List<String> userDataValues) {
        Log.e(TAG, "service implementation (" + getClass() + " does not implement onGetScore()");
        return null;
    }

    /* loaded from: classes2.dex */
    private final class AutofillFieldClassificationServiceWrapper extends IAutofillFieldClassificationService.Stub {
        private AutofillFieldClassificationServiceWrapper() {
        }

        @Override // android.service.autofill.IAutofillFieldClassificationService
        public synchronized void getScores(RemoteCallback callback, String algorithmName, Bundle algorithmArgs, List<AutofillValue> actualValues, String[] userDataValues) throws RemoteException {
            AutofillFieldClassificationService.this.mHandler.sendMessage(PooledLambda.obtainMessage(new HexConsumer() { // from class: android.service.autofill.-$$Lambda$AutofillFieldClassificationService$AutofillFieldClassificationServiceWrapper$LVFO8nQdiSarBMY_Qsf1G30GEZQ
                @Override // com.android.internal.util.function.HexConsumer
                public final void accept(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6) {
                    ((AutofillFieldClassificationService) obj).getScores((RemoteCallback) obj2, (String) obj3, (Bundle) obj4, (List) obj5, (String[]) obj6);
                }
            }, AutofillFieldClassificationService.this, callback, algorithmName, algorithmArgs, actualValues, userDataValues));
        }
    }

    /* loaded from: classes2.dex */
    public static final class Scores implements Parcelable {
        public static final Parcelable.Creator<Scores> CREATOR = new Parcelable.Creator<Scores>() { // from class: android.service.autofill.AutofillFieldClassificationService.Scores.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Scores createFromParcel(Parcel parcel) {
                return new Scores(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Scores[] newArray(int size) {
                return new Scores[size];
            }
        };
        public final float[][] scores;

        private synchronized Scores(Parcel parcel) {
            int size1 = parcel.readInt();
            int size2 = parcel.readInt();
            this.scores = (float[][]) Array.newInstance(float.class, size1, size2);
            for (int i = 0; i < size1; i++) {
                for (int j = 0; j < size2; j++) {
                    this.scores[i][j] = parcel.readFloat();
                }
            }
        }

        private synchronized Scores(float[][] scores) {
            this.scores = scores;
        }

        public String toString() {
            int size1 = this.scores.length;
            int size2 = size1 > 0 ? this.scores[0].length : 0;
            StringBuilder sb = new StringBuilder("Scores [");
            sb.append(size1);
            sb.append("x");
            sb.append(size2);
            StringBuilder builder = sb.append("] ");
            for (int i = 0; i < size1; i++) {
                builder.append(i);
                builder.append(": ");
                builder.append(Arrays.toString(this.scores[i]));
                builder.append(' ');
            }
            return builder.toString();
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int flags) {
            int size1 = this.scores.length;
            int size2 = this.scores[0].length;
            parcel.writeInt(size1);
            parcel.writeInt(size2);
            for (int i = 0; i < size1; i++) {
                for (int j = 0; j < size2; j++) {
                    parcel.writeFloat(this.scores[i][j]);
                }
            }
        }
    }
}
