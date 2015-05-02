/*
 * Copyright 2014 University Of Wisconsin Parkside
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uwp.cs.edu.parkingtracker.parking;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import uwp.cs.edu.parkingtracker.R;

/**
 * Created by Russ on 4/21/2015.
 */
public class ParkingZoneOptionAdapter extends ArrayAdapter<ParkingZoneOption> {
    Context context;
    int layoutResourceId;
    List<ParkingZoneOption> data = null;
//    ParkingZoneOption data[] = null;

    public ParkingZoneOptionAdapter(Context context, int layoutResourceId, List<ParkingZoneOption> data) {
        super(context,layoutResourceId,data);
        this.layoutResourceId=layoutResourceId;
        this.context=context;
        this.data=data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        ParkingZoneOptionHolder holder = null;

        if (row==null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row=inflater.inflate(layoutResourceId,parent,false);
            holder=new ParkingZoneOptionHolder();
            holder.txtTitle=(TextView)row.findViewById(R.id.txtTitle);
            holder.txtDistance=(TextView)row.findViewById(R.id.txtDistance);
            holder.txtFullness=(TextView)row.findViewById(R.id.txtFullness);
            row.setTag(holder);
        }
        else{
            holder = (ParkingZoneOptionHolder)row.getTag();
        }

        ParkingZoneOption parkingZoneOption = data.get(position);

        String distance = String.format("%.0f",parkingZoneOption.distance);
        String strFullness = "";
        if (parkingZoneOption.color == Color.RED) {
            strFullness="Full";
        }
        if (parkingZoneOption.color == Color.YELLOW) {
            strFullness="Half Full";
        }
        if (parkingZoneOption.color == Color.GREEN) {
            strFullness="Empty";
        }
        int fullnessColor = parkingZoneOption.color;
        String strDistance = String.format("%.0fm",parkingZoneOption.distance);
        String title = parkingZoneOption.id.replace("_", " ");
        holder.txtTitle.setText(title);

        holder.txtDistance.setText(strDistance);
        holder.txtFullness.setText(strFullness);
        holder.txtFullness.setTextColor(fullnessColor);

        return row;
    }

    static class ParkingZoneOptionHolder{
        TextView txtTitle;
        TextView txtDistance;
        TextView txtFullness;
    }

}
