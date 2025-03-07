package android.webkit;

import android.annotation.SystemApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.wifi.WifiEnterpriseConfig;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import com.android.internal.R;

@SystemApi
/* loaded from: classes3.dex */
public class FindActionModeCallback implements ActionMode.Callback, TextWatcher, View.OnClickListener, WebView.FindListener {
    private ActionMode mActionMode;
    private int mActiveMatchIndex;
    private View mCustomView;
    private EditText mEditText;
    private InputMethodManager mInput;
    private TextView mMatches;
    private boolean mMatchesFound;
    private int mNumberOfMatches;
    private Resources mResources;
    private WebView mWebView;
    private Rect mGlobalVisibleRect = new Rect();
    private Point mGlobalVisibleOffset = new Point();

    public FindActionModeCallback(Context context) {
        this.mCustomView = LayoutInflater.from(context).inflate(R.layout.webview_find, (ViewGroup) null);
        this.mEditText = (EditText) this.mCustomView.findViewById(16908291);
        this.mEditText.setCustomSelectionActionModeCallback(new NoAction());
        this.mEditText.setOnClickListener(this);
        setText("");
        this.mMatches = (TextView) this.mCustomView.findViewById(R.id.matches);
        this.mInput = (InputMethodManager) context.getSystemService(InputMethodManager.class);
        this.mResources = context.getResources();
    }

    public void finish() {
        this.mActionMode.finish();
    }

    public void setText(String text) {
        this.mEditText.setText(text);
        Spannable span = this.mEditText.getText();
        int length = span.length();
        Selection.setSelection(span, length, length);
        span.setSpan(this, 0, length, 18);
        this.mMatchesFound = false;
    }

    public void setWebView(WebView webView) {
        if (webView == null) {
            throw new AssertionError("WebView supplied to FindActionModeCallback cannot be null");
        }
        this.mWebView = webView;
        this.mWebView.setFindDialogFindListener(this);
    }

    @Override // android.webkit.WebView.FindListener
    public void onFindResultReceived(int activeMatchOrdinal, int numberOfMatches, boolean isDoneCounting) {
        if (isDoneCounting) {
            updateMatchCount(activeMatchOrdinal, numberOfMatches, numberOfMatches == 0);
        }
    }

    private void findNext(boolean next) {
        WebView webView = this.mWebView;
        if (webView == null) {
            throw new AssertionError("No WebView for FindActionModeCallback::findNext");
        }
        if (!this.mMatchesFound) {
            findAll();
        } else if (this.mNumberOfMatches == 0) {
        } else {
            webView.findNext(next);
            updateMatchesString();
        }
    }

    public void findAll() {
        if (this.mWebView == null) {
            throw new AssertionError("No WebView for FindActionModeCallback::findAll");
        }
        CharSequence find = this.mEditText.getText();
        if (find.length() == 0) {
            this.mWebView.clearMatches();
            this.mMatches.setVisibility(8);
            this.mMatchesFound = false;
            this.mWebView.findAll(null);
            return;
        }
        this.mMatchesFound = true;
        this.mMatches.setVisibility(4);
        this.mNumberOfMatches = 0;
        this.mWebView.findAllAsync(find.toString());
    }

    public void showSoftInput() {
        if (this.mEditText.requestFocus()) {
            this.mInput.showSoftInput(this.mEditText, 0);
        }
    }

    public void updateMatchCount(int matchIndex, int matchCount, boolean isEmptyFind) {
        if (!isEmptyFind) {
            this.mNumberOfMatches = matchCount;
            this.mActiveMatchIndex = matchIndex;
            updateMatchesString();
            return;
        }
        this.mMatches.setVisibility(8);
        this.mNumberOfMatches = 0;
    }

    private void updateMatchesString() {
        int i = this.mNumberOfMatches;
        if (i == 0) {
            this.mMatches.setText(R.string.no_matches);
        } else {
            this.mMatches.setText(this.mResources.getQuantityString(R.plurals.matches_found, i, Integer.valueOf(this.mActiveMatchIndex + 1), Integer.valueOf(this.mNumberOfMatches)));
        }
        this.mMatches.setVisibility(0);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        findNext(true);
    }

    @Override // android.view.ActionMode.Callback
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        if (mode.isUiFocusable()) {
            mode.setCustomView(this.mCustomView);
            mode.getMenuInflater().inflate(R.menu.webview_find, menu);
            this.mActionMode = mode;
            Editable edit = this.mEditText.getText();
            Selection.setSelection(edit, edit.length());
            this.mMatches.setVisibility(8);
            this.mMatchesFound = false;
            this.mMatches.setText(WifiEnterpriseConfig.ENGINE_DISABLE);
            this.mEditText.requestFocus();
            return true;
        }
        return false;
    }

    @Override // android.view.ActionMode.Callback
    public void onDestroyActionMode(ActionMode mode) {
        this.mActionMode = null;
        this.mWebView.notifyFindDialogDismissed();
        this.mWebView.setFindDialogFindListener(null);
        this.mInput.hideSoftInputFromWindow(this.mWebView.getWindowToken(), 0);
    }

    @Override // android.view.ActionMode.Callback
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override // android.view.ActionMode.Callback
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        WebView webView = this.mWebView;
        if (webView == null) {
            throw new AssertionError("No WebView for FindActionModeCallback::onActionItemClicked");
        }
        this.mInput.hideSoftInputFromWindow(webView.getWindowToken(), 0);
        switch (item.getItemId()) {
            case R.id.find_next /* 16909018 */:
                findNext(true);
                break;
            case R.id.find_prev /* 16909019 */:
                findNext(false);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        findAll();
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable s) {
    }

    public int getActionModeGlobalBottom() {
        if (this.mActionMode == null) {
            return 0;
        }
        View view = (View) this.mCustomView.getParent();
        if (view == null) {
            view = this.mCustomView;
        }
        view.getGlobalVisibleRect(this.mGlobalVisibleRect, this.mGlobalVisibleOffset);
        return this.mGlobalVisibleRect.bottom;
    }

    /* loaded from: classes3.dex */
    public static class NoAction implements ActionMode.Callback {
        @Override // android.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override // android.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override // android.view.ActionMode.Callback
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }

        @Override // android.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode mode) {
        }
    }
}
