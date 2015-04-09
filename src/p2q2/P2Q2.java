package p2q2;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;

public class P2Q2 {
	static int[] firstPix = new int[3];
	static int[] scanLines = new int[3];
	static int[] width = new int[3];
	static int[] height = new int[3];
	static byte[][] arrOfBytes=new byte[3][];
	static boolean[] little_endian = new boolean[3];
	static int[] offset = new int[3];
	static int[] byteOrder= new int[3];
	public static void main(String[] args) throws IOException{
		File[] files = fileChooser();
		String[] paths=new String[files.length];
		
		// get file paths
		for(int i=0;i<files.length;i++){
			paths[i]=files[i].getAbsolutePath();
			
		}
		
		// place bytes in arrOfBytes
		for(int i = 0;i < 3; i++){
			arrOfBytes[i]=getFileInfo(paths[i]);
		}
		
	
		
		
		// get endianness of each image
		getEndianness();
		
		// get offsets
		getOffset();
		
		for(int i=0;i<3;i++){
			
			getIFD(i);
		}
		
		// set reds
		int[][] red = new int[width[0]][height[0]];
		int[][] red0 = new int[width[0]][height[0]];
		int[][] red1 = new int[width[0]][height[0]];
		int[][] red2 = new int[width[0]][height[0]];
		
		int[] k = new int[3];
		for(int i=0;i<3;i++){
			k[i]=firstPix[i];
		}
		for (int i=0;i<height[0];i++){
			for(int j=0;j<width[0];j++){
				red[j][i]=(int)(((arrOfBytes[0][k[0]]&0xff)+(arrOfBytes[1][k[1]]&0xff)+(arrOfBytes[2][k[2]]&0xff))/3);
				red0[j][i]=(int)arrOfBytes[0][k[0]]&0xff;
				red1[j][i]=(int)arrOfBytes[1][k[1]]&0xff;
				red2[j][i]=(int)arrOfBytes[2][k[2]]&0xff;
				k[0]+=3;
				k[1]+=3;
				k[2]+=3;
			}
		}

		
		
		
		// set greens
		for(int i=0;i<3;i++){
			k[i]=firstPix[i]+1;
		}
		int[][]green = new int[width[0]][height[0]];
		
		int[][] green0 = new int[width[0]][height[0]];
		int[][] green1 = new int[width[0]][height[0]];
		int[][] green2 = new int[width[0]][height[0]];
		for (int i=0;i<height[0];i++){
			for(int j=0;j<width[0];j++){
				green[j][i]=(int)(((arrOfBytes[0][k[0]]&0xff)+(arrOfBytes[1][k[1]]&0xff)+(arrOfBytes[2][k[2]]&0xff))/3);
				green0[j][i]=(int)arrOfBytes[0][k[0]]&0xff;
				green1[j][i]=(int)arrOfBytes[1][k[1]]&0xff;
				green2[j][i]=(int)arrOfBytes[2][k[2]]&0xff;
				k[0]+=3;
				k[1]+=3;
				k[2]+=3;
			}
		}
		
		
		// set blues
		for(int i=0;i<3;i++){
			k[i]=firstPix[i]+2;
		}
		int[][] blue = new int[width[0]][height[0]];
		int[][] blue0 = new int[width[0]][height[0]];
		int[][] blue1 = new int[width[0]][height[0]];
		int[][] blue2 = new int[width[0]][height[0]];
		
		
		for (int i=0;i<height[0];i++){
			for(int j=0;j<width[0];j++){
				blue[j][i]=(int)(((arrOfBytes[0][k[0]]&0xff)+(arrOfBytes[1][k[1]]&0xff)+(arrOfBytes[2][k[2]]&0xff))/3);
				blue0[j][i]=(int)arrOfBytes[0][k[0]]&0xff;
				blue1[j][i]=(int)arrOfBytes[1][k[1]]&0xff;
				blue2[j][i]=(int)arrOfBytes[2][k[2]]&0xff;
				k[0]+=3;
				k[1]+=3;
				k[2]+=3;
			}
		}
		Draw draw0 = new Draw(red0,green0,blue0,height[0],width[0]);
		Draw draw1 = new Draw(red1,green1,blue1,height[0],width[0]);
		Draw draw2 = new Draw(red2,green2,blue2,height[0],width[0]);
		Draw draw = new Draw(red,green,blue,height[0],width[0]);
		
		int[] data= new int[height[0]*width[0]];
		// TODO: need to draw other three images
		// use red,green, blue to create buffered image using setRGB, then convert to tiff/compressed image?

	}
	
	static void getOffset(){
		for(int i=0;i<3;i++){
			offset[i]=getInt(i,4);
		}
	}
	
	static void getEndianness(){
		for(int i=0;i<3;i++){
			byteOrder[i]=getShort(i,0);
			if(byteOrder[i]==0x4949){
				little_endian[i]=true;
			}else{
				little_endian[i]=false;
			}
		}
	}
	
	// retrieves bytes of file and places in an array
	static byte[] getFileInfo(String fileName) throws IOException {
		int read;
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		@SuppressWarnings("resource")
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(fileName));
		
		byte[] buffer= new byte[500000];
		
		while((read=in.read(buffer))>0) {
			out.write(buffer,0,read);
						
		}
		out.flush();
		byte[] fileBytes=out.toByteArray();
		return fileBytes;
	}
	
	
	// file chooser
	public static File[] fileChooser(){
		
		JButton open = new JButton();
		JFileChooser fc = new JFileChooser();
		fc.setMultiSelectionEnabled(true);
		fc.setCurrentDirectory(new java.io.File("."));
		fc.setDialogTitle("");
		//fc.showOpenDialog(open);
		
		if(fc.showOpenDialog(open)==JFileChooser.APPROVE_OPTION){
			//
		}
		File[] files=fc.getSelectedFiles();
		return files;
	}
	
	
	static int getInt(int pic, int i) {
		int b0=arrOfBytes[pic][i+0]&0xFF;
		int b1=arrOfBytes[pic][i+1]&0xFF;
		int b2=arrOfBytes[pic][i+2]&0xFF;
		int b3=arrOfBytes[pic][i+3]&0xFF;

		if(little_endian[pic]){
			return ((b3 << 24) + (b2 << 16) + (b1 << 8) + (b0 << 0));
		}else
			return ((b0 << 24) + (b1 << 16) + (b2 << 8) + b3);
	}
	static int getShort(int pic,int i) {
		int b0=arrOfBytes[pic][i+0]&0xff;
		int b1=arrOfBytes[pic][i+1]&0xff;
	

		
		if(little_endian[pic]) {
			return ((b1 << 8) + b0);
		}
		else{
			return ((b0 << 8) + b1);
		}
	}


	//double getRational
	static void getIFD(int pic){
		int dir=offset[pic]+2;
		int numOfDir=getShort(pic,offset[pic]);
		
		int tag,type,count,val;

		for(int i=0;i<numOfDir;i++) {
			tag=getShort(pic,dir);
			type=getShort(pic,(dir+2));
			count= getInt(pic,(dir+4));
			
			val= getVal(pic,dir+8,type);


//			System.out.println("directory entry "+ i + " tag: " +tag);
//			System.out.println("directory entry "+ i + " type: " + type);
//			System.out.println("directory entry "+ i + " count: " + count);	// number of values
//			System.out.println("directory entry "+ i + " value/offset: " + val);
//			
//			System.out.println();
			
			dir=dir+12;
			
			if(tag==256){
				width[pic]=val;
			}
			if(tag==257){
				height[pic]=val;
			}
			if(tag==257){
				scanLines[pic]=val;
			}
			if(tag==273){
				firstPix[pic]=val;
			}
		
		}
		offset[pic]=offset[pic]+2+numOfDir*12;

		
	}
	static int getVal(int pic,int i,int type) {
		if(type==3){
			return getShort(pic,i);
		}
		
		else{
			return getInt(pic,i);
		}
	}

	

}
