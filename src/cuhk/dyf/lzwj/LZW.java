package cuhk.dyf.lzwj;

import cuhk.dyf.lzwj.io.LZWInputStream;
import cuhk.dyf.lzwj.io.LZWOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dongyufei on 3/17/15.
 */
public class LZW {
    public static final int DEFAULT_CODEWORD_BITS = 12;

    private int codewordLength = DEFAULT_CODEWORD_BITS;

    //cannot use String as index. 0 = null
    private Map<ByteArray, Integer> compressDic;
    private int comDicSize;

    private void initCompressDic() {
        compressDic = new HashMap<ByteArray, Integer>();
        for (int i = 0; i < 256; i++) {
            compressDic.put(new ByteArray((byte)i), i);
        }
        comDicSize = 256;
    }

    public void compress(OutputStream outputStream, InputStream inputStream) {
        initCompressDic();
        LZWOutputStream output = new LZWOutputStream(outputStream, codewordLength);
        try {
            int preInt = inputStream.read();
            ByteArray pre = new ByteArray((byte) preInt);
            int sufInt = 0;
            ByteArray suf;
            while ((sufInt = inputStream.read()) != -1) {
                if (comDicSize == 4095) {
                    initCompressDic();
                }
                suf = new ByteArray((byte)sufInt);
                ByteArray ps = new ByteArray(pre).append(suf);

                if (compressDic.containsKey(ps)) {
                    pre = ps;
                } else {
                    output.write(compressDic.get(pre));
                    if (comDicSize < 4095) {
                        compressDic.put(ps, comDicSize++);
                    }
                    pre = suf;
                }
            }
            output.write(compressDic.get(pre));
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private List<ByteArray> decomList;
    private int decomListSize;

    private void initDecompressList() {
        decomList = new ArrayList<ByteArray>();
        for (int i = 0; i < 256; i++) {
            decomList.add(new ByteArray((byte) i ));
        }
        decomListSize=256;
    }

    public void decompress(OutputStream outputStream, InputStream inputStream) {
        initDecompressList();
        LZWInputStream input = new LZWInputStream(inputStream, codewordLength);
        try {
            int preInt = input.read();
            outputStream.write(preInt);
            int sufInt = 0;
            int character = preInt;
            while ((sufInt = input.read()) != -1) {
                if (decomListSize == 4095) {
                    initDecompressList();
                }
                ByteArray ps = null;
                if (sufInt >= decomList.size()) {
                    ps = new ByteArray(decomList.get(preInt));
                    ps.append((byte) character);
                } else {
                    ps = decomList.get(sufInt);
                }
                for (int i = 0; i < ps.size(); i++) {
                    outputStream.write(ps.get(i));
                }
                character = ps.get(0);
                decomList.add(new ByteArray(decomList.get(preInt)).append((byte) character));
                decomListSize++;
                preInt = sufInt;
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
