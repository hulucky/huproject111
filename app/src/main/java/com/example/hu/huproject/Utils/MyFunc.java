package com.example.hu.huproject.Utils;

import java.nio.ByteBuffer;

/**
 * 数据转换工具
 */
public class MyFunc {


    //获取其中的一段
    public static byte[] getByteArry(byte[] bytes, int len) {
        byte[] res = new byte[len];
        for (int i = 0; i < 14; i++) {
            res[i] = bytes[i];
        }
        return res;
    }

    //-------------------------------------------------------
    static public String ByteArrToHex2(byte[] inBytArr)//字节数组转转hex字符串
    {
        StringBuilder strBuilder = new StringBuilder();
        int j = inBytArr.length;
        for (int i = 0; i < j; i++) {
            strBuilder.append(Byte2Hex(inBytArr[i]));
        }
        return strBuilder.toString();
    }

    /**
     *  
     *  * 字节转换为浮点 
     *  * @param b 字节（至少4个字节） 
     *  * @param index 开始位置 
     *  * @return 
     *  
     */
    public static float fourbyte2float(byte[] b, int index) {
        byte[] a = {b[index], b[index + 1], b[index + 2], b[index + 3]};
        ByteBuffer buf = ByteBuffer.allocateDirect(4); //无额外内存的直接缓存
        //buf=buf.order(ByteOrder.LITTLE_ENDIAN);//默认大端，小端用这行
        buf.put(a);
        buf.rewind();
        float f2 = buf.getFloat();
        return f2;
    }

    //温湿度大气压
    public static int byte4Toint(byte[] b, int index) {
        byte[] by = new byte[4];
        by[0] = b[index + 3];
        by[1] = b[index + 2];
        by[2] = b[index + 1];
        by[3] = b[index];
        int l;
        l = by[0];
        l &= 0xff;
        l |= ((long) by[1] << 8);
        l &= 0xffff;
        l |= ((long) by[2] << 16);
        l &= 0xffffff;
        l |= ((long) by[3] << 24);
        l &= 0xffffffff;
        return l;
    }


    /**
     * 两字节转int  有符号位
     * <p>
     * 功率箱 P Q  解析
     *
     * @param a ： 第一字节
     * @param b ： 第二字节
     * @return
     */
    public static int twoBytesToIntHave(byte a, byte b) {
        byte[] by = new byte[4];
        by[0] = b;
        by[1] = a;

        int l;
        l = by[0];
        l &= 0xff;
        l |= ((long) by[1] << 8);
        l &= 0xffff;
        l = l << 16;
        l = l / (int) Math.pow(2, 16);
        return l;
    }


    /**
     * @Description ：不带符号
     */
    public static int twoBytesToInt(byte[] bytes, int index) {
        byte[] by = new byte[4];
        by[0] = bytes[index + 1];
        by[1] = bytes[index];

        int l;
        l = by[0];
        l &= 0xff;
        l |= ((long) by[1] << 8);
        l &= 0xffff;
        return l;
    }


    // 2字节byte转int 无符号
    public static int twobyteToint_(byte a, byte b) {
        int result = 0;
        int res1 = HexToInt(Byte2Hex(a)) * 256;
        int res2 = HexToInt(Byte2Hex(b));
        result = res1 + res2;
        return result;
    }

    //耿春平代码中的
    //2字节byte转int  带符号位
    public static int  twobyteToint (byte a_,byte b_ ){
        int a = Integer.valueOf(Byte2Hex(a_),16)*256;
        int b = Integer.valueOf(Byte2Hex(b_),16);
        int result = 0;
        result =(int) (b| a);
        if ((result & 32768)==32768){
            result =(result+1-65535);
        }
        return  result ;
    }

    public static int byteToInt(byte a){
        return HexToInt(Byte2Hex(a));
    }

    // 2字节首位代表符号位，其余不取反直接计算 功率箱的P、Q、COS、
    public static int twobyteToint_Sp(byte a, byte b) {

        int result = 0;
        int res1 = HexToInt(Byte2Hex(a)) * 256;
        int res2 = HexToInt(Byte2Hex(b));
        result = res1 + res2;
        if ((a & 0x80) == 0x80) {
            result = -(result - 32768);
        }
        return result;
    }


    public static int byteToint(byte[] b, int index) {
        byte[] by = new byte[4];
        by[0] = b[index + 3];
        by[1] = b[index + 2];
        by[2] = b[index + 1];
        by[3] = b[index];

        int l;
        l = by[0];
        l &= 0xff;
        l |= ((long) by[1] << 8);
        l &= 0xffff;
        l |= ((long) by[2] << 16);
        l &= 0xffffff;
        l |= ((long) by[3] << 24);
        l &= 0xffffffff;
        return l;
    }


    /**
     * 字节转换为浮点
     *
     * @param b     字节（至少4个字节）
     * @param index 开始位置
     * @return
     */
    public static float byte2float(byte[] b, int index) {
        byte[] by = new byte[4];
        by[0] = b[index + 3];
        by[1] = b[index + 2];
        by[2] = b[index + 1];
        by[3] = b[index];
        int l;
        l = by[0];
        l &= 0xff;
        l |= ((long) by[1] << 8);
        l &= 0xffff;
        l |= ((long) by[2] << 16);
        l &= 0xffffff;
        l |= ((long) by[3] << 24);
        return Float.intBitsToFloat(l);
    }

    //-------------------------------------------------------
    // 判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数
    static public int isOdd(int num) {
        return num & 0x1;
    }

    //-------------------------------------------------------
    static public int HexToInt(String inHex)//Hex字符串转int
    {
        return Integer.parseInt(inHex, 16);
    }

    //-------------------------------------------------------
    static public byte HexToByte(String inHex)//Hex字符串转byte
    {
        return (byte) Integer.parseInt(inHex, 16);
    }

    //-------------------------------------------------------
    static public String Byte2Hex(Byte inByte)//1字节转2个Hex字符
    {
        return String.format("%02x", inByte).toUpperCase();
    }

    //-------------------------------------------------------
    static public String ByteArrToHex(byte[] inBytArr)//字节数组转转hex字符串
    {
        StringBuilder strBuilder = new StringBuilder();
        int j = inBytArr.length;
        for (int i = 0; i < j; i++) {
            strBuilder.append(Byte2Hex(inBytArr[i]));
            strBuilder.append(" ");
        }
        return strBuilder.toString();
    }

    //-------------------------------------------------------
    static public String ByteArrToHex(byte[] inBytArr, int offset, int byteCount)//字节数组转转hex字符串，可选长度
    {
        StringBuilder strBuilder = new StringBuilder();
        int j = byteCount;
        for (int i = offset; i < j; i++) {
            strBuilder.append(Byte2Hex(inBytArr[i]));
        }
        return strBuilder.toString();
    }

    //-------------------------------------------------------
    //hex字符串转字节数组
    static public byte[] HexToByteArr(String inHex)//hex字符串转字节数组
    {
        int hexlen = inHex.length();
        byte[] result;
        if (isOdd(hexlen) == 1) {//奇数
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {//偶数
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = HexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    static public int fourByte2int(byte[] b, int index) {
        byte[] by = new byte[4];
        by[0] = b[index + 3];
        by[1] = b[index + 2];
        by[2] = b[index + 1];
        by[3] = b[index];
        int l;
        l = by[0];
        l &= 0xff;
        l |= ((long) by[1] << 8);
        l &= 0xffff;
        l |= ((long) by[2] << 16);
        l &= 0xffffff;
        l |= ((long) by[3] << 24);
        l &= 0xffffffff;
        return l;
    }
    /**
     * 速度
     * */
    public static int twoBytesToInt_speed (byte[] bytes , int index){
        byte[] by = new byte[4];
        by[0] = bytes[index];
        by[1] = bytes[index+1];

        int l;
        l = by[ 0];
        l &= 0xff;
        l |= ((long) by[ 1] << 8);
        l &= 0xffff;
        return l;
    }

}