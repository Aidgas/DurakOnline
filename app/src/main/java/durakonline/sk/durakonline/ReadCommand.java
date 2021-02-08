package durakonline.sk.durakonline;

import java.util.ArrayList;

public class ReadCommand
{
	public long timestamp;

	public boolean init_ok = false;
	public boolean close_sock = false;
	public String cmd = "";
	public ArrayList<byte[]> params = null;
	public byte[] data = null;
}