package io.xjar.key;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

/**
 * A secure random that returns pre-seeded data to calls of nextBytes() or generateSeed().
 */
public class XSecureRandom extends SecureRandom {
    private byte[] _data;

    private int _index;
    private int _intPad;

    public XSecureRandom(byte[] value) {
        this(false, new byte[][]{value});
    }

    public XSecureRandom(byte[][] values) {
        this(false, values);
    }

    public XSecureRandom(boolean intPad, byte[] value) {
        this(intPad, new byte[][]{value});
    }

    public XSecureRandom(boolean intPad, byte[][] values) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        for (int i = 0; i != values.length; i++) {
            try {
                bos.write(values[i]);
            } catch (IOException e) {
                throw new IllegalArgumentException("can't save value array.");
            }
        }

        _data = bos.toByteArray();

        if (intPad) {
            _intPad = _data.length % 4;
        }
    }

    public void nextBytes(byte[] bytes) {
        System.arraycopy(_data, _index, bytes, 0, bytes.length);

        _index += bytes.length;
    }

    public byte[] generateSeed(int numBytes) {
        byte[] bytes = new byte[numBytes];

        this.nextBytes(bytes);

        return bytes;
    }

    public int nextInt() {
        int val = 0;

        val |= nextValue() << 24;
        val |= nextValue() << 16;

        if (_intPad == 2) {
            _intPad--;
        } else {
            val |= nextValue() << 8;
        }

        if (_intPad == 1) {
            _intPad--;
        } else {
            val |= nextValue();
        }

        return val;
    }


    public long nextLong() {
        long val = 0;

        val |= (long) nextValue() << 56;
        val |= (long) nextValue() << 48;
        val |= (long) nextValue() << 40;
        val |= (long) nextValue() << 32;
        val |= (long) nextValue() << 24;
        val |= (long) nextValue() << 16;
        val |= (long) nextValue() << 8;
        val |= nextValue();

        return val;
    }

    public boolean isExhausted() {
        return _index == _data.length;
    }

    private int nextValue() {
        return _data[_index++] & 0xff;
    }
}
