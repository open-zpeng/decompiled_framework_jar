package android.inputmethodservice;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import com.android.internal.R;
/* loaded from: classes.dex */
public class ExtractEditLayout extends LinearLayout {
    Button mExtractActionButton;

    public synchronized ExtractEditLayout(Context context) {
        super(context);
    }

    public synchronized ExtractEditLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mExtractActionButton = (Button) findViewById(R.id.inputExtractAction);
    }
}
