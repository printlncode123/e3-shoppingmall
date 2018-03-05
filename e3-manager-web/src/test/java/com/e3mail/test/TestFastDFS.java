package com.e3mail.test;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;
import cn.e3mail.utils.FastDFSClient;

public class TestFastDFS {
	@Test
	public void test() throws FileNotFoundException, IOException, MyException{
		//创建一个配置文件，内容是tracker服务器的地址（conf/tracker.properties）
		//全局加载tracker配置文件
		ClientGlobal.init("E:\\e3_eclipse-mars_workspace\\JAVAEE32\\e3-manager-web\\src\\main\\resources\\conf\\tracker.conf");
		//创建trackerclient对象
		TrackerClient trackerClient=new TrackerClient();
		//通过trackerclient对象获取trackerserver
		TrackerServer trackerServer = trackerClient.getConnection();
		StorageServer storageServer=null;//创建storageserver
		//创建 storageclient对象，参数是trackerserver和storageserver(需手动创建，可设置为空)
		StorageClient storageClient=new StorageClient(trackerServer, storageServer);
		//通过storageclient对象上传图片到图片服务器
		String[] upload_appender_file = storageClient.upload_appender_file("E:\\head\\7.jpeg", "jpeg", null);
		for (String str : upload_appender_file) {
			System.out.println(str);
		}
	}
	@Test
	public void testFastDFS() throws Exception{
		FastDFSClient fastDFSClient=new FastDFSClient("E:\\e3_eclipse-mars_workspace\\JAVAEE32\\e3-manager-web\\src\\main\\resources\\conf\\tracker.conf");
		String s = fastDFSClient.uploadFile("E:\\head\\7.jpeg");
		System.out.println(s);
	}
}
