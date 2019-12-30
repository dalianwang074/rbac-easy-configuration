package demo;

import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TestMd5 {

    @org.junit.Test
    public void testMd5(){
        // 需要加密的字符串
        String src = "123456";
        try {
            // 加密对象，指定加密方式
            MessageDigest md5 = MessageDigest.getInstance("md5");
            // 准备要加密的数据
            byte[] b = src.getBytes();
            // 加密
            byte[] digest = md5.digest(b);
            // 十六进制的字符
            char[] chars = new char[] { '0', '1', '2', '3', '4', '5',
                    '6', '7' , '8', '9', 'A', 'B', 'C', 'D', 'E','F' };
            StringBuffer sb = new StringBuffer();
            // 处理成十六进制的字符串(通常)
            for (byte bb : digest) {
                System.out.println(bb);
                sb.append(chars[(bb >> 4) & 15]);
                System.out.println(sb.toString());
                sb.append(chars[bb & 15]);
                System.out.println(sb.toString());
                System.out.println();
            }
            // 打印加密后的字符串
            System.out.println(sb);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    @org.junit.Test
    public void testbb(){
        System.out.println((-31 >> 4) & 15);
        System.out.println((10 >> 4) & 15);
        System.out.println((57 >> 4) & 15);

        System.out.println((-31 ) & 15);
        System.out.println((10 ) & 15);
        System.out.println((57 ) & 15);
    }

    @Test
    public void testCc(){
        System.out.println(this.toString().substring(0,this.toString().lastIndexOf(".")));
    }

}
