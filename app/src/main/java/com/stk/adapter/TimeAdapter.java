package com.stk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stk.eschool.R;
import com.stk.fragment.StudentCenterFragment;
import com.stk.model.AttentRecordVo;

import java.util.List;

/**
 * Created by wangl on 2016/11/10.
 */

public class TimeAdapter extends BaseAdapter{
    private Context context;
    private List<AttentRecordVo> attentRecordVoList;
    private StudentCenterFragment.OnTimeChick  onTimeChick;

    public StudentCenterFragment.OnTimeChick getOnTimeChick() {
        return onTimeChick;
    }

    public void setOnTimeChick(StudentCenterFragment.OnTimeChick onTimeChick) {
        this.onTimeChick = onTimeChick;
    }

    public TimeAdapter(Context context, List<AttentRecordVo> attentRecordVoList) {
        this.context = context;
        this.attentRecordVoList = attentRecordVoList;
    }

    @Override
    public int getCount() {
        return attentRecordVoList.size();
    }

    @Override
    public Object getItem(int position) {
        return attentRecordVoList;
    }

    @Override
    public long getItemId(int position) {
        return attentRecordVoList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
         ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.record_adapter,null);
            viewHolder.sc_time = (TextView) convertView.findViewById(R.id.sc_time);
            viewHolder.sc_type = (TextView) convertView.findViewById(R.id.sc_type);
            viewHolder.video_button = (TextView) convertView.findViewById(R.id.video_button);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        if(attentRecordVoList.get(position).getSTU_ELEC_STATUS().equals("20")){
            viewHolder.sc_type.setText("进校时间：");
            viewHolder.sc_time.setText(attentRecordVoList.get(position).getBEGIN_TIME());
        }else if(attentRecordVoList.get(position).getSTU_ELEC_STATUS().equals("24")){
            viewHolder.sc_type.setText("出校时间：");
            viewHolder.sc_time.setText(attentRecordVoList.get(position).getBEGIN_TIME());
        }
        viewHolder.video_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onTimeChick.timeChick(v,position);
            }
        });
        return convertView;
    }

    private class ViewHolder{
        TextView sc_type;
        TextView sc_time;
        TextView video_button;
    }

}
