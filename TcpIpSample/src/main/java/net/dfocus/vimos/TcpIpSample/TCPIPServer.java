package net.dfocus.vimos.TcpIpSample;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONObject;

public class TCPIPServer implements Runnable {
	ServerSocket serverSocket;
	//Thread[] threadArr;

	public static void main(String[] args) {
		// 5개의 쓰레드를 생성하는 서버를 생성한다.
		TCPIPServer server = new TCPIPServer(5);
		server.start();
	}

	public TCPIPServer(int num) {
		try {
			// 서버 소켓을 생성하여 8282번 포트와 바인딩.
			serverSocket = new ServerSocket(8282);
			System.out.println(getTime() + " 서버가 준비되었습니다.");

			//hreadArr = new Thread[num];
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		Thread thread = new Thread(this);
		thread.start();
	}

	public void run() {
		while (true) {
			try {
				System.out.println(getTime() + " 서버가 연결 요청을 기다립니다.");

				Socket socket = serverSocket.accept();
				System.out.println(getTime() + " " + socket.getInetAddress() + " 로부터 연결요청이 들어왔습니다.");

				InputStream is = socket.getInputStream();				

				
				// Header 데이타 읽어 들어는 처리
				int headerSize = 17;
				byte[] headerBuffer = new byte[headerSize];
						
				int headerLength = 0;
				while ((headerLength = is.read(headerBuffer) ) < headerSize) {
				}
				
				ByteBuffer buffer = ByteBuffer.allocate(headerBuffer.length);
				buffer.put(headerBuffer);
				buffer.flip();
				
				byte[] protocalVersion = new byte[9];
	            byte[] command = new byte[2];
	            byte[] bodyDataType = new byte[2];
	            byte[] bodyDataSize = new byte[4];
	            
				buffer.get(protocalVersion);
				buffer.get(command);
				buffer.get(bodyDataType);
				buffer.get(bodyDataSize);
	            
				System.out.println("headerBuffer Length : " + headerBuffer.length);
				System.out.println("protocalVersion : " + new String(protocalVersion));
				System.out.println("command : " + Integer.toHexString(DataConverter.ByteArrayToShort(command)));
				System.out.println("bodyDataType : " + Integer.toHexString(DataConverter.ByteArrayToShort(bodyDataType)));
				System.out.println("bodyDataSize : " + DataConverter.ByteArrayToInt(bodyDataSize) + "");

				
				
				// Body 데이타 읽어 들어는 처리
				
				int bodyDataLength = 0;
				int bodySize = DataConverter.ByteArrayToInt(bodyDataSize);
				byte[] bodyTempBuffer = new byte[bodySize];
				
				while ((bodyDataLength = is.read(bodyTempBuffer) ) < bodySize) {
					System.out.println("bodyDataLength : " + bodyDataLength);					
				}				
				
				System.out.println("Body Data : " + new String(bodyTempBuffer));
				
				
				OutputStream out = socket.getOutputStream();
				DataOutputStream dos = new DataOutputStream(out);
				
				switch(DataConverter.ByteArrayToShort(command)) {
                case 0x01:
                	System.out.println("VM_CONF_INFO");
                	
                	//Body 정보 생성                	
                	JSONObject vmInfo = new JSONObject();
                    vmInfo.put("vmname", "vimosVM001");
                    vmInfo.put("vmdesc", "테스트");
                    vmInfo.put("vmip", "192.168.0.1");
                    vmInfo.put("vmcpu", "2");
                    vmInfo.put("vmmem", "2147483648");
                    vmInfo.put("vmdisk", "32212254720");
                    vmInfo.put("vmvolumename", "vimosVM001.img");
                    vmInfo.put("vmvolumepath", "/home/vimos/img/");
                    vmInfo.put("result", "1");
                    vmInfo.put("message", "");
                    
                    String sendBodyData = vmInfo.toJSONString();
                	
                	//Header 정보 생성
                    byte[] resProtocolVersion = "vimos 001".getBytes();
                    byte[] resCommand = DataConverter.ShortToByteArray((short)0x00ff);
                    byte[] resBodyDataType = DataConverter.ShortToByteArray((short)0x0001);
                    byte[] resBodyDataSize = DataConverter.IntToByteArray(sendBodyData.length());
                	
                    ByteBuffer resbuffer = ByteBuffer.allocate(17);
                    resbuffer.put(resProtocolVersion);
                    resbuffer.put(resCommand);
                    resbuffer.put(resBodyDataType);
                    resbuffer.put(resBodyDataSize);            
                    
                    dos.write(resbuffer.array());
                    dos.write(sendBodyData.getBytes());
                    dos.flush();
                    
                    
                	break;
                case 0x02:
                	System.out.println("VM_STATE_INFO");
                	break;
                case 0x03:
                	System.out.println("VM_CREATE_INFO");
                	break;
                case 0x04:
                	System.out.println("VM_CREATE_PROGRESS");
                	break;
                default:
                	break;
				}
				
				
				
				
				System.out.println(getTime() + " 데이터를 전송하였습니다.");

				is.close();
				dos.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	static String getTime() {
		SimpleDateFormat f = new SimpleDateFormat("[hh:mm:ss] ");
		return f.format(new Date());
	}
}