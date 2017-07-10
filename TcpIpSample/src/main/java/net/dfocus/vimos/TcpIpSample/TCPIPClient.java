package net.dfocus.vimos.TcpIpSample;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import org.json.simple.JSONObject;

public class TCPIPClient {

    public static void main(String[] args) {
        try{
            String serverIp = "192.168.1.8";
 
            // 소켓을 생성하여 연결을 요청한다.
            System.out.println("서버에 연결중입니다. 서버IP : " + serverIp);
            Socket socket = new Socket(serverIp, 8282);

            // 소켓의 출력스트림을 얻는다.
            OutputStream os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
                        
            // Body 정보 생성
            JSONObject vmInfo = new JSONObject();
            vmInfo.put("serviceid", "VM_CONF_INFO");
            vmInfo.put("vmname", "vimosVM001");
            String sendBodyData =  vmInfo.toJSONString();
            //Header 정보 생성
            byte[] protocolVersion = "vimos 001".getBytes();
            byte[] command = DataConverter.ShortToByteArray((short)0x0001);
            byte[] bodyDataType = DataConverter.ShortToByteArray((short)0x0001);
            byte[] bodyDataSize = DataConverter.IntToByteArray(sendBodyData.length());
            
            
            ByteBuffer buffer = ByteBuffer.allocate(17);
            buffer.put(protocolVersion);
            buffer.put(command);
            buffer.put(bodyDataType);
            buffer.put(bodyDataSize);            
            
            dos.write(buffer.array());
            dos.write(sendBodyData.getBytes());
            dos.flush();
            
            // 소켓의 입력스트림을 얻는다.
            InputStream is = socket.getInputStream();
             
            // 소켓으로부터 받은 데이터를 출력한다.
//            System.out.println("서버로부터 받은 메세지 : " + dis.readUTF());
            
            
            // Header 데이타 읽어 들어는 처리
			int resHeaderSize = 17;
			byte[] resHeaderBuffer = new byte[resHeaderSize];
					
			int resHeaderLength = 0;
			while ((resHeaderLength = is.read(resHeaderBuffer) ) < resHeaderSize) {
			}
			
			ByteBuffer resBuffer = ByteBuffer.allocate(resHeaderBuffer.length);
			resBuffer.put(resHeaderBuffer);
			resBuffer.flip();
			
			byte[] resProtocalVersion = new byte[9];
            byte[] resCommand = new byte[2];
            byte[] resBodyDataType = new byte[2];
            byte[] resBodyDataSize = new byte[4];
            
            resBuffer.get(resProtocalVersion);
            resBuffer.get(resCommand);
            resBuffer.get(resBodyDataType);
            resBuffer.get(resBodyDataSize);
            
			System.out.println("headerBuffer Length : " + resHeaderBuffer.length);
			System.out.println("protocalVersion : " + new String(resProtocalVersion));
			System.out.println("command : " + Integer.toHexString(DataConverter.ByteArrayToShort(resCommand)));
			System.out.println("bodyDataType : " + Integer.toHexString(DataConverter.ByteArrayToShort(resBodyDataType)));
			System.out.println("bodyDataSize : " + DataConverter.ByteArrayToInt(resBodyDataSize) + "");
			
			// Body 데이타 읽어 들어는 처리
			
			int resBodyDataLength = 0;
			int resBodySize = DataConverter.ByteArrayToInt(resBodyDataSize);
			byte[] resBodyTempBuffer = new byte[resBodySize];
			
			while ((resBodyDataLength = is.read(resBodyTempBuffer) ) < resBodySize) {
				System.out.println("bodyDataLength : " + resBodyDataLength);					
			}				
			
			System.out.println("Body Data : " + new String(resBodyTempBuffer));
            
            
            
            System.out.println("연결을 종료합니다.");
             
//            dis.close();
            socket.close();
            System.out.println("연결이 종료되었습니다.");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}