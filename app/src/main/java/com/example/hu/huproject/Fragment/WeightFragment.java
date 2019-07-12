package com.example.hu.huproject.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hu.huproject.Adapter.WeightAdapter;
import com.example.hu.huproject.Application.MyApp;
import com.example.hu.huproject.Bean.WeightBean;
import com.example.hu.huproject.Entity.TaskEntity;
import com.example.hu.huproject.R;
import com.example.hu.huproject.Utils.TaskEntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static com.example.hu.huproject.Utils.FormatData.df2;

public class WeightFragment extends Fragment {
    private View view;
    private ListView f_lv, s_lv, t_lv;
    private WeightAdapter w1Adapter, w2Adapter, w3Adapter;
    private Context mcontext;
    private List<WeightBean> mlists1;
    private List<WeightBean> mlists2;
    private List<WeightBean> mlists3;

    private Spinner sp_weight;
    private List<String> numStrList;
    private LinearLayout first_ll, second_ll, third_ll;
    private TextView tv_sum1, tv_sum2, tv_sum3, tv_ave;
    public boolean is_WeightVisible;
    private int selectedId = -1;
    private float sum1, sum2, sum3;
    private float ave;
    private TaskEntity taskEntity;
    private boolean isSum1Empty = false;
    private boolean isSum2Empty = false;
    private boolean isSum3Empty = false;
    private float str1 = 0f, str2, str3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.weight_layout, null);
        mcontext = getActivity();
        taskEntity = MyApp.getTaskEntity();
        if (taskEntity != null) {
            Log.i("sa", "taskentity" + taskEntity.getTaskId());
        } else {
            Log.i("sa", "taskentity ==null");
        }
        initView();

        return view;
    }

    public void initView() {
        if (view != null) {
            mlists1 = new ArrayList<>();
            mlists2 = new ArrayList<>();
            mlists3 = new ArrayList<>();
            f_lv = view.findViewById(R.id.first_lv);
            s_lv = view.findViewById(R.id.second_lv);
            t_lv = view.findViewById(R.id.third_lv);
            first_ll = view.findViewById(R.id.first_ll);
            second_ll = view.findViewById(R.id.second_ll);
            third_ll = view.findViewById(R.id.third_ll);
            sp_weight = view.findViewById(R.id.sp_weight);
            tv_ave = view.findViewById(R.id.ave);
            tv_sum1 = view.findViewById(R.id.sum1);
            tv_sum2 = view.findViewById(R.id.sum2);
            tv_sum3 = view.findViewById(R.id.sum3);
        }

        w1Adapter = new WeightAdapter(mcontext, mlists1, WeightFragment.this);
        w2Adapter = new WeightAdapter(mcontext, mlists2, WeightFragment.this);
        w3Adapter = new WeightAdapter(mcontext, mlists3, WeightFragment.this);
        numStrList = new ArrayList<>();
        numStrList.add("选择第一段");
        numStrList.add("选择第二段");
        numStrList.add("选择第三段");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mcontext, R.layout.item_spinner, R.id.tv_item_spinner, numStrList);
        sp_weight.setAdapter(adapter);
        sp_weight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("zg", "pos==" + position);
                selectedId = position;
                switch (position) {
                    case 0:
                        first_ll.setBackgroundResource(R.drawable.bg_shape_chart_selected);
                        second_ll.setBackgroundResource(R.drawable.bg_shape_chart);
                        third_ll.setBackgroundResource(R.drawable.bg_shape_chart);
                        f_lv.setAdapter(w1Adapter);
                        break;
                    case 1:
                        first_ll.setBackgroundResource(R.drawable.bg_shape_chart);
                        second_ll.setBackgroundResource(R.drawable.bg_shape_chart_selected);
                        third_ll.setBackgroundResource(R.drawable.bg_shape_chart);
                        s_lv.setAdapter(w2Adapter);
                        break;
                    case 2:
                        first_ll.setBackgroundResource(R.drawable.bg_shape_chart);
                        second_ll.setBackgroundResource(R.drawable.bg_shape_chart);
                        third_ll.setBackgroundResource(R.drawable.bg_shape_chart_selected);
                        t_lv.setAdapter(w3Adapter);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        f_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showTip(position, 1);

            }
        });
        s_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showTip(position, 2);
            }
        });
        t_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showTip(position, 3);
            }
        });
    }

    /**
     * 显示popupWindow
     */
    private void showTip(final int position, final int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View v = inflater.inflate(R.layout.dialog_update, null);

        Button btn_change = v.findViewById(R.id.btn_change);
        Button btn_delete = v.findViewById(R.id.btn_delete);
        Button btn_gb = v.findViewById(R.id.btn_gb);
        builder.setCancelable(false);
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog(position, type);
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 1) {
                    mlists1.remove(position);
                    w1Adapter.updateListView(mlists1);
                    sum1 = 0f;
                    for (int i = 0; i < mlists1.size(); i++) {
                        sum1 += Float.parseFloat(mlists1.get(i).getWeight());
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_sum1.setText(df2.format(sum1));
                        }
                    });
                } else if (type == 2) {
                    mlists2.remove(position);
                    w1Adapter.updateListView(mlists2);
                    sum2 = 0f;
                    for (int i = 0; i < mlists2.size(); i++) {
                        sum2 += Float.parseFloat(mlists2.get(i).getWeight());
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_sum2.setText(df2.format(sum2));
                        }
                    });
                } else if (type == 3) {
                    mlists3.remove(position);
                    w3Adapter.updateListView(mlists3);
                    sum3 = 0f;
                    for (int i = 0; i < mlists3.size(); i++) {
                        sum3 += Float.parseFloat(mlists3.get(i).getWeight());
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_sum3.setText(df2.format(sum3));
                        }
                    });
                }
                updateAve();
                dialog.dismiss();
            }
        });
        btn_gb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void dialog(final int pos, final int type) {
        final AlertDialog.Builder customizeDialog = new AlertDialog.Builder(mcontext);
        final View dialogView = LayoutInflater.from(mcontext).inflate(R.layout.dialog_edit, null);
        String weight = "";
        if (type == 1) {
            weight = mlists1.get(pos).getWeight();
        } else if (type == 2) {
            weight = mlists2.get(pos).getWeight();
        } else if (type == 3) {
            weight = mlists3.get(pos).getWeight();
        }
        final EditText inputEdittext = dialogView.findViewById(R.id.dialog_input);
        inputEdittext.setSelection(inputEdittext.getText().length());
        inputEdittext.setText(weight);
        customizeDialog.setTitle("修改");
        customizeDialog.setView(dialogView);
        customizeDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String inputStr = inputEdittext.getText().toString().trim();
                        WeightBean b = new WeightBean();
                        b.setWeight(inputStr);
                        if (type == 1) {
                            mlists1.set(pos, b);
                            w1Adapter.updateListView(mlists1);
                            sum1 = 0f;
                            for (int i = 0; i < mlists1.size(); i++) {
                                sum1 += Float.parseFloat(mlists1.get(i).getWeight());
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_sum1.setText(df2.format(sum1));
                                }
                            });
                        } else if (type == 2) {
                            mlists2.set(pos, b);
                            w2Adapter.updateListView(mlists2);
                            sum2 = 0f;
                            for (int i = 0; i < mlists2.size(); i++) {
                                sum2 += Float.parseFloat(mlists2.get(i).getWeight());
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_sum2.setText(df2.format(sum2));
                                }
                            });
                        } else if (type == 3) {
                            mlists3.set(pos, b);
                            w3Adapter.updateListView(mlists3);
                            sum3 = 0f;
                            for (int i = 0; i < mlists3.size(); i++) {
                                sum3 += Float.parseFloat(mlists3.get(i).getWeight());
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_sum3.setText(df2.format(sum3));
                                }
                            });
                        }
                        updateAve();
                    }
                });
        customizeDialog.show();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            is_WeightVisible = true;
        } else {
            is_WeightVisible = false;
        }
    }

    public void start() {

    }

    public void stop() {

        Random ra = new Random();
        float ax = ra.nextFloat() + 100;
        ax = Float.parseFloat(df2.format(ax));
        WeightBean weightBean = new WeightBean();
        weightBean.setWeight(String.valueOf(ax));
        sum1 = 0f;
        sum2 = 0f;
        sum3 = 0f;
        ave = 0f;
        if (selectedId == 0) {
            mlists1.add(weightBean);
            w1Adapter.updateListView(mlists1);

            for (int i = 0; i < mlists1.size(); i++) {
                sum1 += Float.parseFloat(mlists1.get(i).getWeight());
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_sum1.setText(df2.format(sum1));
                }
            });

        } else if (selectedId == 1) {
            mlists2.add(weightBean);
            w2Adapter.updateListView(mlists2);
            for (int i = 0; i < mlists2.size(); i++) {
                sum2 += Float.parseFloat(mlists2.get(i).getWeight());
            }
            tv_sum2.setText(df2.format(sum2));
        } else if (selectedId == 2) {
            mlists3.add(weightBean);
            w3Adapter.updateListView(mlists3);
            for (int i = 0; i < mlists3.size(); i++) {
                sum3 += Float.parseFloat(mlists3.get(i).getWeight());
            }
            tv_sum3.setText(df2.format(sum3));
        }
        updateAve();

    }

    private void updateSum() {

    }

    private void updateAve() {
        isSum1Empty = TextUtils.isEmpty(tv_sum1.getText().toString());
        isSum2Empty = TextUtils.isEmpty(tv_sum2.getText().toString());
        isSum3Empty = TextUtils.isEmpty(tv_sum3.getText().toString());

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isSum1Empty && isSum2Empty && isSum3Empty) {
                    str1 = Float.parseFloat(tv_sum1.getText().toString());
                    ave = str1;
                    tv_ave.setText(df2.format(ave));
                }
                if (isSum1Empty && !isSum2Empty && isSum3Empty) {
                    str2 = Float.parseFloat(tv_sum2.getText().toString());
                    ave = str2;
                    tv_ave.setText(df2.format(ave));
                }
                if (isSum1Empty && isSum2Empty && !isSum3Empty) {
                    str3 = Float.parseFloat(tv_sum3.getText().toString());
                    ave = str3;
                    tv_ave.setText(df2.format(ave));
                }
                if (!isSum1Empty && !isSum2Empty && isSum3Empty) {
                    str1 = Float.parseFloat(tv_sum1.getText().toString());
                    str2 = Float.parseFloat(tv_sum2.getText().toString());
                    ave = (str1 + str2) / 2;
                    tv_ave.setText(df2.format(ave));
                }
                if (!isSum1Empty && isSum2Empty && !isSum3Empty) {
                    str1 = Float.parseFloat(tv_sum1.getText().toString());
                    str3 = Float.parseFloat(tv_sum3.getText().toString());
                    ave = (str1 + str3) / 2;
                    tv_ave.setText(df2.format(ave));
                }
                if (isSum1Empty && !isSum2Empty && !isSum3Empty) {
                    str2 = Float.parseFloat(tv_sum2.getText().toString());
                    str3 = Float.parseFloat(tv_sum3.getText().toString());
                    ave = (str2 + str3) / 2;
                    tv_ave.setText(df2.format(ave));
                }
                if (!isSum1Empty && !isSum2Empty && !isSum3Empty) {
                    str1 = Float.parseFloat(tv_sum1.getText().toString());
                    str2 = Float.parseFloat(tv_sum2.getText().toString());
                    str3 = Float.parseFloat(tv_sum3.getText().toString());
                    ave = (str1 + str2 + str3) / 3;
                    tv_ave.setText(df2.format(ave));
                }
            }
        });

    }

    public void save() {
        taskEntity.setIs_WeightSave(true);
        TaskEntityUtils.update(taskEntity);
        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                "保存成功！", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);// 居中显示
        toast.show();
    }
}
