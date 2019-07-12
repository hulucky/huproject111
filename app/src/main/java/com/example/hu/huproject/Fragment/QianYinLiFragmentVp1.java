package com.example.hu.huproject.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hu.huproject.Adapter.NvQianYinLiAdapter;
import com.example.hu.huproject.Application.MyApp;
import com.example.hu.huproject.Entity.QianYinLiEntity;
import com.example.hu.huproject.Entity.TaskEntity;
import com.example.hu.huproject.R;
import com.example.hu.huproject.Utils.MyFunc;
import com.example.hu.huproject.Utils.TaskEntityUtils;
import com.xzkydz.bean.ComBean;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

//牵引力测试--vp1
public class QianYinLiFragmentVp1 extends Fragment {
    TextView tvQyl;
    Button clearBtn;
    LinearLayout llQyl;
    TextView tvZuidaQyl;
    LinearLayout llZuidaQyl;
    TextView tvChazhi0;
    TextView tvChazhi;
    LinearLayout llChazhi;
    RelativeLayout llChart;
    TextView item0;
    TextView item1;
    TextView item2;
    TextView item3;
    TextView item4;
    LinearLayout llTittleName;
    View line;
    ListView listview;
    RelativeLayout flData;
    EditText etEdingQianyinli;
    LinearLayout llEding;
    Button paramSureBtn;
    RelativeLayout paramRl;
    RelativeLayout flChart;

    public static final String TAG = "ZhiDongLiFragmentVp1";
    public static int MSG_RECEIVED_DATA = 1;

    private TaskEntity mtaskEntity;
    private MyApp myApp;
    private String parent;//用于判断该fragment是从哪里进入的

    private View view;
    //任务参数id
    private Long id_param;
    private DecimalFormat df2 = new DecimalFormat("#0.00");
    private DecimalFormat df3 = new DecimalFormat("#0.000");
    private DecimalFormat df = new DecimalFormat("0");

    public List<Float> testResLists;//记录的拉力集合
    public List<QianYinLiEntity> qianYinLiEntities;

    public int testingNum = 0; //本次测试的序号
    public float maxFaceShow = 0f; //最大制动力
    private Handler handler;
    private byte[] buffer;
    private NvQianYinLiAdapter qylAdapter;
    private float record_zdzdl = 0;//记录的力的偏移量，校准用
    private float value;//传感器记录的值，在没有外力时，就是偏差
    private Unbinder unbinder;
    private float edingFloat = 0;//用户输入的额定牵引力

    public static QianYinLiFragmentVp1 newInstance(int position) {
        QianYinLiFragmentVp1 fragment = new QianYinLiFragmentVp1();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_qianyinlilivp1, container, false);
        myApp = MyApp.getInstance();
        parent = getActivity().getClass().getName();

        mtaskEntity = MyApp.getTaskEntity();
        id_param = mtaskEntity.getTaskId();//拿到本次测试的id
        initView();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void initView() {
        if (view != null) {
            tvQyl = view.findViewById(R.id.tv_qyl);
            clearBtn = view.findViewById(R.id.clear_btn);
            llQyl = view.findViewById(R.id.ll_qyl);
            tvZuidaQyl = view.findViewById(R.id.tv_zuida_qyl);
            tvChazhi = view.findViewById(R.id.tv_chazhi);
            tvChazhi0 = view.findViewById(R.id.tv_chazhi0);
            llChart = view.findViewById(R.id.ll_chart);
            item0 = view.findViewById(R.id.item0);
            item1 = view.findViewById(R.id.item1);
            item2 = view.findViewById(R.id.item2);
            item3 = view.findViewById(R.id.item3);
            item4 = view.findViewById(R.id.item4);
            llTittleName = view.findViewById(R.id.ll_tittle_name);
            line = view.findViewById(R.id.line);
            listview = view.findViewById(R.id.listview);
            flData = view.findViewById(R.id.fl_data);
            etEdingQianyinli = view.findViewById(R.id.et_eding_qianyinli);
            llEding = view.findViewById(R.id.ll_eding);
            paramSureBtn = view.findViewById(R.id.param_sure_btn);
            paramRl = view.findViewById(R.id.param_rl);
            flChart = view.findViewById(R.id.fl_chart);

            initData();
        }
    }

    private void initData() {
        testResLists = new ArrayList<>();
        qianYinLiEntities = new ArrayList<>();
        qylAdapter = new NvQianYinLiAdapter(getActivity(), qianYinLiEntities, QianYinLiFragmentVp1.this);
        listview.setAdapter(qylAdapter);
        //刚开始时就接受数据
        handler = new Handler();
        handler.post(runnable);
    }

    public void start() {//开始制动力测试
        testResLists.clear();
        tvZuidaQyl.setText("0");
        maxFaceShow = 0f;
        clearBtn.setClickable(false);//开始后就不能再清零
        clearBtn.setBackgroundResource(R.color.gray);//把按钮置为灰色
        if (handler == null) {
            handler = new Handler();
            handler.post(runnable);
        }
    }

//    //定义一个数组用来临时存放牵引力实体，每次保存后，都把此数组清零。
//    public List<QianYinLiEntity> qylEntities = new ArrayList<>();

    public void save() {
        //第一次testingNum全部保存为0，第二次全部为1，以此类推，用以区分是第几次测试
        //当没有增加新数据时，testingNum也会自动增长，此时那一组测试的数据就为空
        for (int i = 0; i < qianYinLiEntities.size(); i++) {
            QianYinLiEntity qianYinLiEntity = qianYinLiEntities.get(i);
            //过滤掉已经保存过的，从而给qianYinLiEntity设置
            // 不同的testingNunm，用来区分序号
            if (qianYinLiEntity.getIsSave()) {
                continue;
            }
            qianYinLiEntity.setTestingNum(testingNum);
            qianYinLiEntity.setIsSave(true);
            myApp.getmDaoSession().getQianYinLiEntityDao().insert(qianYinLiEntity);
        }
        testingNum = testingNum + 1;
        mtaskEntity.setIs_QianYinLiSave(true);
        TaskEntityUtils.update(mtaskEntity);
        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                "保存成功！", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);// 居中显示
        toast.show();
    }

    public void record() {//记录
        clearBtn.setClickable(true);//变为可点击
        clearBtn.setBackgroundResource(R.color.orange);
        //点击记录时，让handle不再接收消息
//        if (handler != null) {
//            handler.removeMessages(MSG_RECEIVED_DATA);
//            handler.removeCallbacks(runnable);
//            handler = null;
//        }
        QianYinLiEntity qylEntity = new QianYinLiEntity();
        qylEntity.setKey(id_param);

        //没输入额定牵引力时，差值保存0
        if (edingFloat != 0) {
            float chazhiFloat = Math.abs(maxFaceShow - edingFloat) / edingFloat;
            if (chazhiFloat > 0.05) {
                tvChazhi.setTextColor(getResources().getColor(R.color.red));
            } else {
                tvChazhi.setTextColor(getResources().getColor(R.color.green));
            }
            //差值=（最大牵引力-额定牵引力）/额定牵引力 *100%
            String chazhiStr = df.format(chazhiFloat * 100) + "%";
            qylEntity.setChazhi(chazhiStr);//差值
            tvChazhi.setText(chazhiStr);
        } else {
            qylEntity.setChazhi("0");//差值
        }
        qylEntity.setEdingQianyinli(df3.format(edingFloat));//额定牵引力
        qylEntity.setMaxQianyinli(df3.format(maxFaceShow));//最大牵引力
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = sdf.format(new Date());
        qylEntity.setTime(str);//创建时间
        qianYinLiEntities.add(qylEntity);
//        qylEntities.add(qylEntity);
        qylAdapter.updateListView(qianYinLiEntities);
        Toasty.success(getContext(), "成功记录一条测试数据", 2).show();

    }

    //点击删除按钮时，执行的事件
    public void showInitRes(QianYinLiEntity qianYinLiEntity, List<QianYinLiEntity> list) {
        this.qianYinLiEntities = list;
//        this.qylEntities = list;
        if (qianYinLiEntity.getIsSave()) {
            myApp.getmDaoSession().getQianYinLiEntityDao().delete(qianYinLiEntity);
        }
    }

    @OnClick({R.id.clear_btn, R.id.param_sure_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.clear_btn://清零
                record_zdzdl = value;//记录下此时要减去的值(偏差)
                maxFaceShow = 0;
                tvQyl.setText("0");
                tvZuidaQyl.setText("0");
                Toasty.success(getActivity(), "清除成功！", 2).show();
//                if (handler != null) {
//                    handler.removeMessages(MSG_RECEIVED_DATA);
//                    handler.removeCallbacks(runnable);
//                    handler = null;
//                }
                break;
            case R.id.param_sure_btn:
                paramRl.setVisibility(View.GONE);
                String edingStr = etEdingQianyinli.getText().toString().trim();
                if (edingStr.equals("")) {//未输入额定牵引力时，差值变灰
                    edingFloat = 0;
                    tvChazhi.setTextColor(getResources().getColor(R.color.grey));
                    tvChazhi0.setTextColor(getResources().getColor(R.color.grey));
                } else {
                    edingFloat = Float.parseFloat(edingStr);
                }
                Log.i("ssss", "edingFloat: " + edingFloat);
                break;
        }
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                updateChart1(MyApp.comBeanLali);
            } catch (Exception e) {
                Log.i(TAG, "更新拉力异常exceprion");
            }
            //延迟一秒后又将该线程加入到线程队列中
            Message message = new Message();
            message.what = MSG_RECEIVED_DATA;
            if (handler != null) {
                handler.sendMessage(message);
            } else {
                return;
            }
            handler.postDelayed(runnable, 1000);
        }
    };

    private void updateChart1(ComBean comBeanLali) {
        if (comBeanLali.recData.length > 0) {
            buffer = comBeanLali.recData;//拿到串口数据
            int type = buffer[9] & 0xff;
            if (type == 64) {
                if (buffer.length == 22) {
//                    Log.i("ddd", "record_zdzdl(偏差)====== " + record_zdzdl);
                    float res0 = (float) MyFunc.twoBytesToInt(buffer, 16) / 100f;
                    float res1 = (float) MyFunc.twoBytesToInt(buffer, 14) / 100f;
                    Log.i("ddd", "数据为：" + Arrays.toString(buffer));
                    value = (res0 + res1) / 2f;//传感器记录的值，在没有外力时，就是偏差
//                    Log.i("ddd", "value（传感器的值）====== " + value);
                    //在校零时，record_zdzdl就是静止时的偏差,用读到的值减去偏差才是实际的值
                    final float res = Math.abs(value - record_zdzdl);
//                    Log.i("ddd", "res（实际值）====== " + res);
                    tvQyl.setText(df3.format(res));
                    testResLists.add(Math.abs(res));
                    maxFaceShow = Math.abs(res) > Math.abs(maxFaceShow) ? Math.abs(res) : maxFaceShow;
//                    Log.i("ddd", "maxFaceShow====== " + maxFaceShow);
                    if (testResLists.size() != 0) {
                        tvZuidaQyl.setText(df3.format(maxFaceShow));
                    }
                }
            } else {
                Toast.makeText(getContext(), "拿到的不是拉力传感器的数据", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "串口数据为空", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeMessages(MSG_RECEIVED_DATA);
            handler.removeCallbacks(runnable);
            handler = null;
        }
        testingNum = 0;//退出时，把测试序号清零
    }
}
