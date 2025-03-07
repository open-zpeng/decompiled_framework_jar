package android.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes3.dex */
public class RemoteViewsListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<RemoteViews> mRemoteViewsList;
    private int mViewTypeCount;
    private ArrayList<Integer> mViewTypes = new ArrayList<>();

    public RemoteViewsListAdapter(Context context, ArrayList<RemoteViews> remoteViews, int viewTypeCount) {
        this.mContext = context;
        this.mRemoteViewsList = remoteViews;
        this.mViewTypeCount = viewTypeCount;
        init();
    }

    public void setViewsList(ArrayList<RemoteViews> remoteViews) {
        this.mRemoteViewsList = remoteViews;
        init();
        notifyDataSetChanged();
    }

    private void init() {
        if (this.mRemoteViewsList == null) {
            return;
        }
        this.mViewTypes.clear();
        Iterator<RemoteViews> it = this.mRemoteViewsList.iterator();
        while (it.hasNext()) {
            RemoteViews rv = it.next();
            if (!this.mViewTypes.contains(Integer.valueOf(rv.getLayoutId()))) {
                this.mViewTypes.add(Integer.valueOf(rv.getLayoutId()));
            }
        }
        int size = this.mViewTypes.size();
        int i = this.mViewTypeCount;
        if (size > i || i < 1) {
            throw new RuntimeException("Invalid view type count -- view type count must be >= 1and must be as large as the total number of distinct view types");
        }
    }

    @Override // android.widget.Adapter
    public int getCount() {
        ArrayList<RemoteViews> arrayList = this.mRemoteViewsList;
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
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
        if (position < getCount()) {
            RemoteViews rv = this.mRemoteViewsList.get(position);
            rv.addFlags(2);
            if (convertView != null && convertView.getId() == rv.getLayoutId()) {
                rv.reapply(this.mContext, convertView);
                return convertView;
            }
            View v = rv.apply(this.mContext, parent);
            return v;
        }
        return null;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getItemViewType(int position) {
        if (position < getCount()) {
            int layoutId = this.mRemoteViewsList.get(position).getLayoutId();
            return this.mViewTypes.indexOf(Integer.valueOf(layoutId));
        }
        return 0;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getViewTypeCount() {
        return this.mViewTypeCount;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public boolean hasStableIds() {
        return false;
    }
}
