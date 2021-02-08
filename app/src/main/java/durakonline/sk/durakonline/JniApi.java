package durakonline.sk.durakonline;

/**
 * Created by root on 18.07.15.
 */
public class JniApi
{
    static
    {
        System.loadLibrary("cdata");
    }

    public static native int port1();
    public static native int port2();

    public static native int xcrc32(byte[] data);

    public static native byte[] dataEncrypt1(byte[] data);
    public static native byte[] dataDecrypt1(byte[] data);

    public static native byte[] dataEncrypt2(byte[] data, byte[] xor_key);
    public static native byte[] dataDecrypt2(byte[] data, byte[] xor_key);

    public static native String f1();
    public static native String k1();

    public static native double sin(double v);
    public static native double cos(double v);
    public static native double sqrt(double v);


}
