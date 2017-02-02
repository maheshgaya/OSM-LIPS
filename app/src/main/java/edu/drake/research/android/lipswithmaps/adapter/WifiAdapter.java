/*
 * Copyright 2017 Drake University
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package edu.drake.research.android.lipswithmaps.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.drake.research.android.lipswithmaps.R;
import edu.drake.research.android.lipswithmaps.data.WifiItem;

/**
 * Created by Mahesh Gaya on 1/15/17.
 */

public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.ViewHolder> {
    private List<WifiItem> mWifiItemList;

    public WifiAdapter(List<WifiItem> wifiItemList){
        this.mWifiItemList = wifiItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_wifi_item, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WifiItem wifiItem = mWifiItemList.get(position);
        holder. levelTextView.setText(String.valueOf(wifiItem.getLevel()));
        holder.ssidTextView.setText(wifiItem.getSsid());
        holder.bssidTextView.setText(wifiItem.getBssid());

    }

    @Override
    public int getItemCount() {
        return mWifiItemList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.textview_wifi_level)TextView levelTextView;
        @BindView(R.id.textview_wifi_ssid)TextView ssidTextView;
        @BindView(R.id.textview_wifi_bssid)TextView bssidTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
