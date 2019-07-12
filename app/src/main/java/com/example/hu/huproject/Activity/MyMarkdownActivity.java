package com.example.hu.huproject.Activity;

import com.xzkydz.function.menu.markdown.MarkDownActivity;

public class MyMarkdownActivity extends MarkDownActivity {
    // 复写父类方法，返回产品信息的文件路径
    @Override
    public String setProductInfFileUrl() {
        return "markdown/ProductInf.md";
    }


}
