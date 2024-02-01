package android.service.autofill;

import android.os.Bundle;
import android.util.ArrayMap;

/* loaded from: classes2.dex */
public interface FieldClassificationUserData {
    String[] getCategoryIds();

    Bundle getDefaultFieldClassificationArgs();

    String getFieldClassificationAlgorithm();

    String getFieldClassificationAlgorithmForCategory(String str);

    ArrayMap<String, String> getFieldClassificationAlgorithms();

    ArrayMap<String, Bundle> getFieldClassificationArgs();

    String[] getValues();
}
