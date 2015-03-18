package cuhk.dyf.lzwj.io;

import cuhk.dyf.lzwj.Utils;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by dongyufei on 3/17/15.
 */
public class LZWOutputStream {

    private int codewordLength;

    private OutputStream output;

    private int buffer;
    private int maxBytesInBuffer;
    private int maxBitsInBuffer;

    private int maxNumsInBuffer;

    private int bufferedCodes;

    public LZWOutputStream(OutputStream os, int codewordLength) {
        this.output = os;
        this.codewordLength = codewordLength;
        this.buffer = 0;
        this.bufferedCodes = 0;
        this.maxBitsInBuffer = Utils.leastCommonMultiple(8, this.codewordLength);
        this.maxBytesInBuffer = this.maxBitsInBuffer / 8;
        this.maxNumsInBuffer = this.maxBitsInBuffer / this.codewordLength;
    }

    public void write(int code) throws IOException {
        code = (code&4095) << ( bufferedCodes * codewordLength );
        buffer |= code;
        bufferedCodes++;

        if (bufferedCodes >= maxNumsInBuffer) {
            for (int i=0; i<maxBytesInBuffer; i++) {
                output.write((int)(buffer&255));
                buffer >>= 8;
            }
            bufferedCodes = 0;
            buffer = 0;
        }
    }

    public void flush() throws IOException {
        if (bufferedCodes > 0 && bufferedCodes < maxNumsInBuffer ) {
            write(-1);
        } else {
            write(-1);
            write(0);
        }
        output.flush();
    }

    public void close() throws IOException {
        output.close();
    }

}
