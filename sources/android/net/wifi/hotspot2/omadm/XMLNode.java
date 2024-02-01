package android.net.wifi.hotspot2.omadm;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/* loaded from: classes2.dex */
public class XMLNode {
    private final XMLNode mParent;
    private final String mTag;
    private final List<XMLNode> mChildren = new ArrayList();
    private StringBuilder mTextBuilder = new StringBuilder();
    private String mText = null;

    public XMLNode(XMLNode parent, String tag) {
        this.mTag = tag;
        this.mParent = parent;
    }

    public void addText(String text) {
        this.mTextBuilder.append(text);
    }

    public void addChild(XMLNode child) {
        this.mChildren.add(child);
    }

    public void close() {
        this.mText = this.mTextBuilder.toString().trim();
        this.mTextBuilder = null;
    }

    public String getTag() {
        return this.mTag;
    }

    public XMLNode getParent() {
        return this.mParent;
    }

    public String getText() {
        return this.mText;
    }

    public List<XMLNode> getChildren() {
        return this.mChildren;
    }

    public boolean equals(Object thatObject) {
        if (this == thatObject) {
            return true;
        }
        if (thatObject instanceof XMLNode) {
            XMLNode that = (XMLNode) thatObject;
            return TextUtils.equals(this.mTag, that.mTag) && TextUtils.equals(this.mText, that.mText) && this.mChildren.equals(that.mChildren);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.mTag, this.mText, this.mChildren);
    }
}
