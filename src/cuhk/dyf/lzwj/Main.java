package cuhk.dyf.lzwj;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongyufei on 3/17/15.
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length < 2) {
            System.err.println("params are wrong!");
            return;
        }
        if ("-c".equals(args[0]) && args.length >=3) {
            String[] dstArr = new String[args.length-2];
            System.arraycopy(args, 2, dstArr, 0, args.length-2);
            compress(args[1], dstArr);
        } else if ("-d".equals(args[0])) {
            decompress(args[1]);
        } else {
            System.err.println("params are wrong!");
        }

    }

    public static void decompress(String fileS) {

        try {
            File file = new File(fileS);
            String decomDir = file.getParent();
            FileInputStream fis = new FileInputStream(file);

            int w = 0;
            List<String> names = new ArrayList<String>();
            String name = "";
            while ((w = fis.read()) != -1) {
                if ("".equals(name) && w == 10) { // 10 is '\n'
                    break;
                }
                if (w != 10) {
                    name += String.valueOf((char)w);
                } else {
                    names.add(name);
                    name="";
                }
            }
            LZW lzw = new LZW();
            for (String fn: names) {
                File f = new File(fn);
                f = new File(decomDir + "/" + f.getName());
                lzw.decompress(new FileOutputStream(f), fis);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void compress(String dst, String ...listFiles) {
        File dstF = new File(dst);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(dstF);
            for (String fn : listFiles) {
                File lf = new File(fn);
                if (lf.length() == 0) {
                    continue;
                }
                fos.write(fn.getBytes());
                fos.write('\n');
            }
            fos.write('\n');

            LZW lzw = new LZW();
            for (String fn : listFiles) {
                File lf = new File(fn);
                if (lf.length() == 0) {
                    continue;
                }
                FileInputStream fis = new FileInputStream(lf);
                lzw.compress(fos, fis);
                fis.close();
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
