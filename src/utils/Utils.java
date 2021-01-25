package utils;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.*;
import java.util.Enumeration;

public class Utils {

    public static Reader getReader(String path) throws IOException {
        return new InputStreamReader(new FileInputStream(path));
    }

    public static String readText(String path) throws IOException {
        Reader reader = getReader(path);
        int tempChar;
        StringBuilder builder = new StringBuilder();
        while ((tempChar = reader.read()) != -1) {
            builder.append((char) tempChar);
        }
        return builder.toString();
    }

    public static void saveText(String path, String text) {
        File file = new File(path);
        if (file.isDirectory()) throw new Error("此路径下存在同名文件夹，缓存文件生成失败！");
        if (!file.exists()) {
            boolean bool = new File(file.getParent()).mkdirs();
            System.out.print("Directory created? " + bool + "\n");
        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            bw.write(text);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getFileName(String path) {
        File file = new File(path);
        if (file.isFile()) return file.getName();
        return null;
    }

    public static String getSpace(int num) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < num; i++) {
            builder.append(" ");
        }
        return builder.toString();
    }

    public static void initGlobalFont(Font fnt){    // 设置全局字体
        FontUIResource fontRes = new FontUIResource(fnt);
        for(Enumeration<?> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();){
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if(value instanceof FontUIResource) UIManager.put(key, fontRes);
        }
    }
}
