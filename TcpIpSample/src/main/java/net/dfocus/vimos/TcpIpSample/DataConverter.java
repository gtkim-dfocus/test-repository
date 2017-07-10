package net.dfocus.vimos.TcpIpSample;

public class DataConverter {

    public static  byte[] IntToByteArray(int value) {
		byte[] byteArray = new byte[4];
		byteArray[0] = (byte)(value >> 24);
		byteArray[1] = (byte)(value >> 16);
		byteArray[2] = (byte)(value >> 8);
		byteArray[3] = (byte)(value);
		return byteArray;
	}
    
    public static  byte[] ShortToByteArray(short value) {
		byte[] byteArray = new byte[2];
		byteArray[0] = (byte)(value >> 8);
		byteArray[1] = (byte)(value);
		return byteArray;
	}
    
    public static  int ByteArrayToInt(byte bytes[]) {
    	return ((((int)bytes[0] & 0xff) << 24) |
    			(((int)bytes[1] & 0xff) << 16) |
    			(((int)bytes[2] & 0xff) << 8) |
    			(((int)bytes[3] & 0xff)));
    }
    
    
    public static  int ByteArrayToShort(byte bytes[]) {
    	return ((((int)bytes[0] & 0xff) << 8) |
    			(((int)bytes[1] & 0xff)));
    } 

}
