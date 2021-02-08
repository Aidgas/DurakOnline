package durakonline.sk.durakonline;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import durakonline.sk.durakonline.other.Log;
import durakonline.sk.durakonline.other.SessionInfo;


public class ProtokolUtils 
{
	static public int htonl(int value) 
	{
		  if (ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN) )
		  {
		     return value;
		  }
		  return Integer.reverseBytes(value);
	}

	static public long htonl2(long value)
	{
		if (ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN) )
		{
			return value;
		}

		return Long.reverseBytes(value);
	}
	
	static public short htons(short value) 
	{
		  if (ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN) )
		  {
		     return value;
		  }
		  return Short.reverseBytes(value);
	}
	
	static public int fromByteArray(byte[] bytes) 
	{
	     return ByteBuffer.wrap(bytes).getInt();
	}
	
	static public short fromByteArray_short(byte[] bytes) 
	{
	     return ByteBuffer.wrap(bytes).getShort();
	}
	
	static public ReadCommand read_command_encrypt(InputStream is, SessionInfo session_info)
	{
		ReadCommand res = new ReadCommand();
		
		int num_read        = 0;
		byte[] token        = new byte[1];
		byte[] byte_array_2 = new byte[2];
		byte[] len_message  = new byte[4];
		byte[] crc32        = new byte[4];
		int offset          = 0;

        //-----------------------------------------------------------------------

        try
        {
            num_read = is.read(token, 0, 1);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return res;
        }

        // If no byte is available because the stream is at the end of the file, the value -1 is returned
        if( num_read == -1 )
		{
			//Log.i("TAG", "num_read == -1");
			res.close_sock = true;
			return res;
		}

        if(num_read < 1) { Log.i("TAG", "num_read < 1"); return res; }

        if( (new String( token )).equalsIgnoreCase("*") == false )
        {
            Log.e("TAG", "ERROR token " );
            return null;
        }

        //-----------------------------------------------------------------------
		
		try
		{
			num_read = is.read(len_message, 0, 4); offset += 4;
			
			//Log.i("TAG", "num_read " + String.valueOf(num_read));
		} 
		catch (IOException e) 
		{
			//e.printStackTrace();
			return res;
		}
		
		if(num_read == -1)
		{
			res.close_sock = true;
			return res;
		}
		
		if(num_read < 4)
		{
			return res;
		}
		
		int len = htonl( fromByteArray( len_message ) );

		//len -= 4; // crc32
		
		/*try
		{
			num_read = is.read(token, 0, 1);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return res;
		}
		
		if(num_read < 1) { return res; }
		
		if( (new String( token )).equalsIgnoreCase("*") == false )
		{
			return res;
		}*/

		//Log.i("TAG", "len " + String.valueOf(len));

		if( len < 0 )
		{
			return null;
		}
		
		///------------------------------------------------------------------------------------------
		ByteBuffer b = ByteBuffer.allocate( len );
		
		int local_buffer_size = 1024*512;
		byte[] local_buffer = new byte[local_buffer_size];
		
		int total_read    = 0;
		int count_read    = 0;
		int need_download = len;
		boolean error     = false;

		/*if(len == 73704)
		{
			//Log.i("TAG", "OK");
		}*/
		
		while(true)
		{
			if(need_download - total_read > local_buffer_size)
            {
				try
				{
					count_read = is.read(local_buffer, 0, local_buffer_size);
				}
				catch (IOException e)
				{
					e.printStackTrace();
					break;
				}
            }
            else
            {
            	try
				{
                	count_read = is.read(local_buffer, 0, (need_download - total_read) );
            	}
                catch (IOException e)
				{
					e.printStackTrace();
					break;
				}
            }

            if(count_read < 0)
            {
                if((need_download - total_read != 0) )
                { error = true; }
                break;
            }
            
            total_read += count_read;

            //fwrite(buffer_local, 1, count_read, f);
            b.put(local_buffer, 0, count_read);

            if(need_download - total_read == 0)
            {
                break;
            }
            //-------------
		}

		if( error )
		{
			Log.e("TAG", "Error read 560000");
			return res;
		}

		///------------------------------------------------------------------------------------------
 		//Log.i("TAG", "total_read " + String.valueOf(total_read) + " " + String.valueOf(b.position()));

		try
		{
			num_read = is.read(token, 0, 1);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return res;
		}

		if(num_read < 1) { return res; }

		if( (new String( token )).equalsIgnoreCase("*") == false )
		{
			Log.e("TAG", "ERROR token " );
			return null;
		}

		try
		{
			num_read = is.read(crc32, 0, 4); offset += 4;

			if(num_read != 4)
			{
				Log.i("TAG", "error num_read " + String.valueOf(num_read));
				return null;
			}

			//Log.i("TAG", "num_read " + String.valueOf(num_read));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return res;
		}

		int read_crc32 = fromByteArray( crc32 );
		int calc_crc32 = JniApi.xcrc32(b.array());

		if( read_crc32 != calc_crc32 )
		{
			Log.e("TAG", "ERROR READ TCP " + String.valueOf(need_download) + " crc32: " + Integer.toBinaryString(read_crc32) );
			return null;
		}

		
		byte[] read_byff = null;

		if( session_info.type_key == 0 )
		{
			read_byff = JniApi.dataDecrypt1(b.array());
		}
		else
        {
            read_byff = JniApi.dataDecrypt2(b.array(), session_info.xor_key);
        }

		//byte[] read_byff = b.array();
		offset = 0;

		System.arraycopy(read_byff, offset, token, 0, 1); offset += 1;

		if( (new String( token )).equalsIgnoreCase("*") == false )
		{
			return null;
		}


		System.arraycopy(read_byff, offset, token, 0, 1); offset += 1;

		if( (new String( token )).equalsIgnoreCase("-") == false )
		{
			return null;
		}

		byte[] buffer_timestamp = new byte[8];
		System.arraycopy(read_byff, offset, buffer_timestamp, 0, 8); offset += 8;
		res.timestamp = htonl2( ByteBuffer.wrap(buffer_timestamp).getLong() );
		
		System.arraycopy(read_byff, offset, byte_array_2, 0, 2); offset += 2;
		
		len = htons( fromByteArray_short( byte_array_2 ) );
		
		String command = "";
		byte[] _bcommand = new byte[ len ];
		
		System.arraycopy(read_byff, offset, _bcommand, 0, len); offset += len;
		
		command = new String( _bcommand );
		
		res.cmd = command;

		byte[] _count_params = new byte[1];
		
		System.arraycopy(read_byff, offset, _count_params, 0, 1); offset += 1;
		
		//len = htonl( fromByteArray( len_message ) );
		int count_params = _count_params[0];
		
		if(count_params == 0)
		{
			res.params = null;
		}
		else
		{
			res.params = new ArrayList<byte[]>();
			
			for(int i = 0; i < count_params; i++)
			{
				System.arraycopy(read_byff, offset, byte_array_2, 0, 2); offset += 2;
				
				int len_param_item = htons( fromByteArray_short( byte_array_2 ) );
				
				String p_command  = "";
				byte[] p_bcommand = new byte[ len_param_item ];
				
				System.arraycopy(read_byff, offset, p_bcommand, 0, len_param_item); offset += len_param_item;
				
				//p_command = new String( p_bcommand );
				
				res.params.add( p_bcommand );
			}
		}
		
		// len data
		System.arraycopy(read_byff, offset, len_message, 0, 4); offset += 4;
		
		len = htonl( fromByteArray( len_message ) );
		
		if(len > 0)
		{
			res.data = new byte[len];
			System.arraycopy(read_byff, offset, res.data, 0, len); offset += len;
		}
		
		res.init_ok = true;
		
		return res;
	}
	
	static public byte[] get_buffer_command_encrypt(String cmd, ArrayList<String> params, byte[] add_buffer, SessionInfo session_info, long timestamp)
	{
		byte[] res     = null;
		byte[] encrypt = null;
		
		if(cmd.trim().length() == 0)
		{
			return null;
		}
		
		int total_len = 0;

		total_len += 2;                           // *-
        total_len += 8;                           // timestamp
        total_len += 4 + cmd.getBytes().length;   // название команды
        total_len += 1;                           // всего параметров
		byte count_params = 0;
		
		if( params != null )
		{
			count_params = (byte) params.size();
			
			for(int i = 0; i < params.size(); i++)
			{
				total_len += 2 + params.get(i).getBytes().length;
			}
		}
		
		total_len += 4; // длинна буффера binare
		
		if(add_buffer != null)
		{
			total_len += add_buffer.length;
		}

		res     = new byte[ total_len + 4 + 4 + 4];
		encrypt = new byte[ total_len ];
		

		int offset   = 0;
		int offset_2 = 0;

		byte[] _len_message = ByteBuffer.allocate(4).putInt( htonl(total_len) ).array();
		byte[] _socket_id   = ByteBuffer.allocate(4).putInt( htonl(session_info.session_socket_id) ).array();

		System.arraycopy(_len_message,    0, res, offset, 4); offset += 4; /// сохранить обший размер пакета
		System.arraycopy(_socket_id,      0, res, offset, 4); offset += 4; /// сохранить socket_id

		_len_message = ByteBuffer.allocate(2).put( (new String("*-")).getBytes() ).array();

		System.arraycopy(_len_message,    0, encrypt, offset_2, _len_message.length); offset_2 += _len_message.length;

		// encrypt mass
		_len_message = ByteBuffer.allocate(8).putLong(htonl2(timestamp)).array();

		System.arraycopy(_len_message,    0, encrypt, offset_2, _len_message.length); offset_2 += _len_message.length;

		_len_message = ByteBuffer.allocate(4).putInt( htonl( cmd.length() ) ).array();
		System.arraycopy(_len_message,    0, encrypt, offset_2, _len_message.length); offset_2 += _len_message.length;
		
		byte[] _bcmd = cmd.getBytes();
		System.arraycopy(_bcmd,    0, encrypt, offset_2, _bcmd.length); offset_2 += _bcmd.length;
		
		_len_message = ByteBuffer.allocate(1).put(count_params ).array();
		System.arraycopy(_len_message,    0, encrypt, offset_2, _len_message.length); offset_2 += _len_message.length;
		
		if( params != null )
		{
			for(int i = 0; i < params.size(); i++)
			{
				String param_item = params.get(i);
				
				_len_message = ByteBuffer.allocate(2).putShort( htons((short) param_item.getBytes().length) ).array();
				System.arraycopy(_len_message,    0, encrypt, offset_2, _len_message.length); offset_2 += _len_message.length;
				
				_bcmd = param_item.getBytes();
				System.arraycopy(_bcmd,    0, encrypt, offset_2, _bcmd.length); offset_2 += _bcmd.length;
			}
		}
		
		int len_add_byffer = 0;
		
		if( add_buffer != null )
		{
			len_add_byffer = add_buffer.length;
		}
		
		_len_message = ByteBuffer.allocate(4).putInt( htonl( len_add_byffer ) ).array();
		System.arraycopy(_len_message,    0, encrypt, offset_2, _len_message.length); offset_2 += _len_message.length;
		
		if(add_buffer != null)
		{
			System.arraycopy(add_buffer,    0, encrypt, offset_2, add_buffer.length); offset_2 += add_buffer.length;
		}
		
		byte[] _t = null;

		if( session_info.type_key == 0 )
		{
			_t = JniApi.dataEncrypt1(encrypt);
		}
		else
		{
			_t = JniApi.dataEncrypt2(encrypt, session_info.xor_key);
		}
		
		System.arraycopy(_t,    0, res, offset, _t.length); offset += _t.length;

		int calc_crc32 = JniApi.xcrc32(_t);

		_len_message = ByteBuffer.allocate(4).putInt( htonl( calc_crc32 ) ).array();

		System.arraycopy(_len_message,    0, res, offset, _len_message.length); offset += _len_message.length;
		
		return res;
	}
}
