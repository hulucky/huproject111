package com.example.hu.huproject.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hu.huproject.Fragment.ZhiDongFragment;
import com.example.hu.huproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ZhiDongDialog extends DialogFragment {
    @BindView(R.id.radioBtn1)
    RadioButton radioBtn1;
    @BindView(R.id.radioBtn2)
    RadioButton radioBtn2;
    @BindView(R.id.radioBtn3)
    RadioButton radioBtn3;
    @BindView(R.id.radioBtn4)
    RadioButton radioBtn4;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.tv_sure)
    TextView tvSure;

    private  Context instance;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
       instance = getActivity();
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        Dialog dialog = new Dialog(instance, R.style.BottomDialog);
        //隐藏Android默认的Title栏,设置Content前设定
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_zhidong);
        dialog.setCanceledOnTouchOutside(true);//外部点击取消
//        // 设置宽度为屏宽, 靠近屏幕中部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;//宽度持平
//        lp.height = WindowManager.LayoutParams.MATCH_PARENT; // 高度持平
         window.setAttributes(lp);
        ButterKnife.bind(instance, dialog); // Dialog即View
        initData();
        return dialog;
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {

        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioBtn1:
                        Toast.makeText(instance, "listener1", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioBtn2:
                        Toast.makeText(instance, "listener2", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioBtn3:
                        Toast.makeText(instance, "listener3", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioBtn4:
                        Toast.makeText(instance, "listener4", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }


    @OnClick({R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sure:
                Toast.makeText(instance, "点击了确定", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
