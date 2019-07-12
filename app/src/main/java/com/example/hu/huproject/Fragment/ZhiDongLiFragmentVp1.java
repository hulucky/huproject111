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

import com.example.hu.huproject.Adapter.NvQylAdapter;
import com.example.hu.huproject.Application.MyApp;
import com.example.hu.huproject.Entity.TaskEntity;
import com.example.hu.huproject.Entity.ZhiDongLiEntity;
import com.example.hu.huproject.R;
import com.example.hu.huproject.Utils.MyFunc;
import com.example.hu.huproject.Utils.TaskEntityUtils;
import com.xzkydz.bean.ComBean;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

//制动力测试--vp1
public class ZhiDongLiFragmentVp1 extends Fragment {
    TextView tvZdl;
    Button clearBtn;
    LinearLayout llZdl;
    TextView tvZuidaZdl;
    LinearLayout llZuidaZdl;
    TextView tvZongZdl;
    LinearLayout llZongZdl;
    TextView tvBizhi;
    TextView tvBizhi0;
    LinearLayout llBizhi;
    RelativeLayout llChart;
    TextView item0;
    TextView item1;
    TextView item2;
    TextView item3;
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
    private boolean isTest = true;
    private View view;
    //任务参数id
    private Long id_param;
    private DecimalFormat df2 = new DecimalFormat("#0.00");
    private DecimalFormat df3 = new DecimalFormat("#0.000");
    private float radiousVel = 0f;
    public List<Float> testResLists;//记录的拉力集合
    public List<ZhiDongLiEntity> zhdlEntities;
    public float zhdljVel;  //制动力矩
    public int testingNum = 0; //本次测试的序号
    public float maxFaceShow = 0f; //最大制动力
    private Handler handler;
    private byte[] buffer;
    private NvQylAdapter qylAdapter;
    private float record_zdzdl = 0;//记录的力的偏移量，校准用
    private float value;//传感器记录的值，在没有外力时，就是偏差
    private float res;//实时记录的力的值
    private float edingFloat;//用户输入的额定牵引力
    Unbinder unbinder;
    private float f = 0;//总制动力

    public static ZhiDongLiFragmentVp1 newInstance(int position) {
        ZhiDongLiFragmentVp1 fragment = new ZhiDongLiFragmentVp1();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_zhidonglivp1, container, false);
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
            tvZdl = view.findViewById(R.id.tv_zdl);
            clearBtn = view.findViewById(R.id.clear_btn);
            llZdl = view.findViewById(R.id.ll_zdl);
            tvZuidaZdl = view.findViewById(R.id.tv_zuida_zdl);
            tvZongZdl = view.findViewById(R.id.tv_zong_zdl);
            llZongZdl = view.findViewById(R.id.ll_zong_zdl);
            tvBizhi = view.findViewById(R.id.tv_bizhi);
            tvBizhi0 = view.findViewById(R.id.tv_bizhi0);
            llBizhi = view.findViewById(R.id.ll_bizhi);
            llChart = view.findViewById(R.id.ll_chart);
            item0 = view.findViewById(R.id.item0);
            item1 = view.findViewById(R.id.item1);
            item2 = view.findViewById(R.id.item2);
            item3 = view.findViewById(R.id.item3);
            llTittleName = view.findViewById(R.id.ll_tittle_name);
            line = view.findViewById(R.id.line);
            listview = view.findViewById(R.id.listview);
            flData = view.findViewById(R.id.fl_data);
            etEdingQianyinli = view.findViewById(R.id.et_eding_qianyinli);
            llEding = view.findViewById(R.id.ll_eding);
            paramSureBtn = view.findViewById(R.id.param_sure_btn);
            paramRl = view.findViewById(R.id.param_rl);
            flChart = view.findViewById(R.id.fl_chart);

            if (parent.contains("ZhiDongli_TestActivity")) {
                isTest = true;
            }
            if (parent.contains("DataDetailActivity")) {
                isTest = false;
            }
            initData();
        }
    }

    private void initData() {
        testResLists = new ArrayList<>();
        zhdlEntities = new ArrayList<>();
        qylAdapter = new NvQylAdapter(getActivity(), zhdlEntities, ZhiDongLiFragmentVp1.this);
        listview.setAdapter(qylAdapter);
        //刚开始时就接受数据
        handler = new Handler();
        handler.post(runnable);
    }

    public void start() {//开始制动力测试
        testResLists.clear();
        tvZuidaZdl.setText("0");
        maxFaceShow = 0f;
        clearBtn.setClickable(false);//开始后就不能再清零
        clearBtn.setBackgroundResource(R.color.gray);//把按钮置为灰色
        if (handler == null) {
            handler = new Handler();
            handler.post(runnable);
        }
    }

    public void save() {
        //第一次testingNum全部保存为0，第二次全部为1，以此类推，用以区分是第几次测试
        //当没有增加新数据时，testingNum也会自动增长，此时那一组测试的数据就为空
        for (int i = 0; i < zhdlEntities.size(); i++) {
            ZhiDongLiEntity zhiDongLiEntity = zhdlEntities.get(i);
            //过滤掉已经保存过的，从而给qianYinLiEntity设置
            // 不同的testingNunm，用来区分序号
            if (zhiDongLiEntity.getIsSave()) {
                continue;
            }
            zhiDongLiEntity.setTestingNum(testingNum);
            zhiDongLiEntity.setIsSave(true);
            myApp.getmDaoSession().getZhiDongLiEntityDao().insert(zhiDongLiEntity);
        }
        testingNum = testingNum + 1;
        mtaskEntity.setIs_ZhiDongLiSave(true);
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
        ZhiDongLiEntity zdlEntity = new ZhiDongLiEntity();
        zdlEntity.setKey(id_param);
        zdlEntity.setMaxZhiDongli(df3.format(maxFaceShow));//保存最大制动力

        //总制动力
//        f = 0;
//        for (int i = 0; i < zhdlEntities.size(); i++) {//遍历制动力集合，算出总制动力
//            String maxZhiDongli = zhdlEntities.get(i).getMaxZhiDongli();
//            float v = Float.parseFloat(maxZhiDongli);
//            f += v;
//        }
        f = f + maxFaceShow;//总制动力
        tvZongZdl.setText(df3.format(f));
        zdlEntity.setZongZhiDongLi(df3.format(f));//保存总制动力

        if (edingFloat != 0) {//没输入额定牵引力时，比值保存0
            float bizhiFloat = f / edingFloat;//算出比值
            if (bizhiFloat > 1.5 && bizhiFloat < 2) {
                tvBizhi.setTextColor(getResources().getColor(R.color.green));
            } else {
                tvBizhi.setTextColor(getResources().getColor(R.color.red));
            }
            tvBizhi.setText(df3.format(bizhiFloat));
            zdlEntity.setBizhi(df3.format(bizhiFloat));//保存比值
        } else {
            zdlEntity.setBizhi("0");//保存比值
        }
        zdlEntity.setEdingZhiDongli(df3.format(edingFloat));//保存额定制动力
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = sdf.format(new Date());
        zdlEntity.setTime(str);//创建时间
        zhdlEntities.add(zdlEntity);
        qylAdapter.updateListView(zhdlEntities);

        Toasty.success(getContext(), "成功记录一条测试数据", 5).show();

    }

    @OnClick({R.id.clear_btn, R.id.param_sure_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.clear_btn://清零
                record_zdzdl = value;//记录下此时要减去的值
                maxFaceShow = 0;
                tvZdl.setText("0");//制动力
                tvZuidaZdl.setText("0");//最大制动力
                Toasty.success(getActivity(), "清除成功！", 2).show();
//                if (handler != null) {
//                    handler.removeMessages(MSG_RECEIVED_DATA);
//                    handler.removeCallbacks(runnable);
//                    handler = null;
//                }
                break;
            case R.id.param_sure_btn://点击确定按钮隐藏参数输入框
                paramRl.setVisibility(View.GONE);
                String edingStr = etEdingQianyinli.getText().toString().trim();
                if (edingStr.equals("")) {//未输入额定牵引力时，比值变灰
                    edingFloat = 0;
                    tvBizhi.setTextColor(getResources().getColor(R.color.grey));
                    tvBizhi0.setTextColor(getResources().getColor(R.color.grey));
                } else {
                    edingFloat = Float.parseFloat(edingStr);
                }
                Log.i("ssss", "edingFloat: " + edingFloat);
                break;
        }
    }

    //点击删除按钮时，执行的事件
    public void showInitRes(ZhiDongLiEntity zhiDongLiEntity, List<ZhiDongLiEntity> list) {
        this.zhdlEntities = list;
//        this.qylEntities = list;
        if (zhiDongLiEntity.getIsSave()) {
            myApp.getmDaoSession().getZhiDongLiEntityDao().delete(zhiDongLiEntity);
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
                    float res0 = (float) MyFunc.twoBytesToInt(buffer, 16) / 100f;
                    float res1 = (float) MyFunc.twoBytesToInt(buffer, 14) / 100f;
                    value = (res0 + res1) / 2f;//传感器记录的值，在没有外力时，就是偏差
                    //在校零时，record_zdzdl就是静止时的偏差,用读到的值减去偏差才是实际的值
                    res = Math.abs(value - record_zdzdl);
                    tvZdl.setText(df3.format(res));//制动力
                    Log.i("ddd", "record_zdzdl====== " + record_zdzdl);
                    Log.i("ddd", "res====== " + res);
                    testResLists.add(Math.abs(res));
                    maxFaceShow = Math.abs(res) > Math.abs(maxFaceShow) ? Math.abs(res) : maxFaceShow;
                    Log.i("ddd", "maxFaceShow====== " + maxFaceShow);
                    if (testResLists.size() != 0) {
                        tvZuidaZdl.setText(df3.format(maxFaceShow));//最大制动力
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
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeMessages(MSG_RECEIVED_DATA);
            handler.removeCallbacks(runnable);
            handler = null;
        }
        testingNum = 0;//退出时，把测试序号清零
        f = 0;//退出时，把总制动力清零
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
