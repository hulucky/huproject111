package com.example.hu.huproject.Activity;

import com.xzkydz.function.pdf.PdfInfBean;
import com.xzkydz.function.pdf.PdfListActivity;

import java.util.ArrayList;
import java.util.List;

//测试标准Activity
public class StandardActivity extends PdfListActivity {
    @Override
    public List<PdfInfBean> getPdfInf() {
        List<PdfInfBean> pdfInfBeanList = new ArrayList<>();
        //这里只是示例，"煤矿安全规程"是内置的，不需要设置路径，如果没有别的PDF文件可以直接返回null值。
        PdfInfBean pf0 = new PdfInfBean("煤矿用单轨吊安全规范","pdf/煤矿在用单轨吊车安全性能检测检验规范.pdf");
        pdfInfBeanList.add(pf0);
        return pdfInfBeanList;
    }


}
