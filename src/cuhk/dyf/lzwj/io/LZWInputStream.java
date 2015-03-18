package cuhk.dyf.lzwj.io;

import cuhk.dyf.lzwj.Utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dongyufei on 3/17/15.
 */
public class LZWInputStream {
    private InputStream input;

    //12-bits
    private int codewordLength;

    //buffer code
    private int buffer;

    /**
     * in <code>buffer</code> , if the number of byte reaches maxBytesInBuffer, bytes in buffer will be written into file.
     **/
    private int maxBytesInBuffer;

    /**
     * in <code>buffer</code> , if the number of bit reaches maxBitsInBuffer，bits in buffer will be written  into file.
     **/
    private int maxBitsInBuffer;

    private int maxNumsInBuffer;

    //the number of codeword
    private int bufferedCodes;

    private boolean eof;

    public LZWInputStream(InputStream input, int codewordLength) {
        this.input = input;
        this.codewordLength = codewordLength;
        bufferedCodes = 0;
        buffer = 0;
        this.eof = false;
        this.maxBitsInBuffer = Utils.leastCommonMultiple(8, this.codewordLength);
        this.maxBytesInBuffer = this.maxBitsInBuffer / 8;
        this.maxNumsInBuffer = this.maxBitsInBuffer / this.codewordLength;
    }

    public int read() throws IOException {
        if (bufferedCodes <= 0 && !eof ) {
            for (int i=0; i<maxBytesInBuffer; i++) {
                int bt = input.read();
                if (bt == -1) {
                    eof = true;
                }
                bt = bt & 255;
                bt <<= (i * 8);
                buffer |= bt;
            }
            bufferedCodes = maxNumsInBuffer;
        }

        if (bufferedCodes > 0) {
            int code = (int)(buffer & 4095);
            buffer >>= codewordLength;
            bufferedCodes--;
            if (code != 4095) {
                return code;
            } else {
                return -1;
            }
        }
        return -1;
    }

    public void close() throws IOException {
        input.close();
    }
}
