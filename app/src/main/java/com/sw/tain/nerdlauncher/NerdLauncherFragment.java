package com.sw.tain.nerdlauncher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by home on 2016/11/22.
 */
public class NerdLauncherFragment extends Fragment{
    private RecyclerView mRecyclerView;

    public static NerdLauncherFragment newInstance(){
        NerdLauncherFragment f = new NerdLauncherFragment();
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nerd_launcher, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_nerd_launcher);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setupAdapter();
        return v;
    }

    private void setupAdapter(){
        final PackageManager pm = getActivity().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> activityList =  pm.queryIntentActivities(intent, 0);
        Collections.sort(activityList, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo o1, ResolveInfo o2) {
                return String.CASE_INSENSITIVE_ORDER.compare(o1.loadLabel(pm).toString(), o2.loadLabel(pm).toString());
            }
        });
        ActivityAdapter adapter = new ActivityAdapter(activityList);
        mRecyclerView.setAdapter(adapter);
    }

    private class ActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView mActivityNameTextView;
        private final ImageView mActivityIcon;
        private ResolveInfo mResolveInfo;

        public ActivityHolder(View itemView) {
            super(itemView);
            mActivityNameTextView = (TextView)itemView.findViewById(R.id.textView_activity_title);
            mActivityNameTextView.setOnClickListener(this);
            mActivityIcon = (ImageView)itemView.findViewById(R.id.imageView_activity_icon);
        }

        public void bindView(ResolveInfo resolveInfo) {
            mResolveInfo = resolveInfo;
            String activityTitle = mResolveInfo.activityInfo.loadLabel(getActivity().getPackageManager()).toString();
            mActivityNameTextView.setText(activityTitle);
            mActivityIcon.setImageDrawable(mResolveInfo.activityInfo.applicationInfo.loadIcon(getActivity().getPackageManager()));
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setClassName(mResolveInfo.activityInfo.applicationInfo.packageName, mResolveInfo.activityInfo.name);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            startActivity(intent);
        }
    }

    private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder>{

        private final List<ResolveInfo> mActivityList;

        public ActivityAdapter(List<ResolveInfo> list){
            mActivityList = list;
        }

        @Override
        public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_activity_recyclerview, parent, false);
            ActivityHolder holder = new ActivityHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(ActivityHolder holder, int position) {
            ResolveInfo resolveInfo = mActivityList.get(position);
            holder.bindView(resolveInfo);

        }

        @Override
        public int getItemCount() {
            return mActivityList.size();
        }
    }
}
