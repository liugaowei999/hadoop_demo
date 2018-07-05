package com.liugw.hd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class HdfsTest {
	static String path = "hdfs://192.168.213.160:9000";

	//2���ϴ��ļ�
	public static void uploadFileToHdfs() throws Exception {
		//�������Ȩ�����⣬�м��н������������һ�֣�����������hdfs��xml�ļ������
		//System.setProperty("HADOOP_USER_NAME","hadoop") ;

		//FileSystem��һ�������࣬���ǿ���ͨ���鿴Դ�����˽�
		URI uri = new URI(path);//����URI����  
		FileSystem fs = FileSystem.get(uri, new Configuration());//��ȡ�ļ�ϵͳ
		//����Դ��ַ
		Path src = new Path("d://aa.txt");
		//����Ŀ���ַ
		Path dst = new Path("/");
		//�����ļ�ϵͳ�ĸ��ƺ�����ǰ��Ĳ�����ָ�Ƿ�ɾ��Դ�ļ���trueΪɾ��������ɾ�� 
		fs.copyFromLocalFile(false, src, dst);
		//���ر��ļ�ϵͳ
		System.out.println("=========�ļ��ϴ��ɹ�==========");
		fs.close();//��Ȼ������������ʽ��д�����ʱ����Ҫ�����޸ģ���finally���йر�
	}

	//3�������ļ���
	public static void mkdirToHdfs() {

		URI uri = null;
		FileSystem fs = null;
		try {
			//����URI����  
			uri = new URI(path);
			fs = FileSystem.get(uri, new Configuration());//��ȡ�ļ�ϵͳ
			Path dirPath = new Path("/mktest");
			fs.mkdirs(dirPath);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("==========����Ŀ¼�ɹ�=========");
	}

	//4�������ļ�
	public static void createFile() {
		//����URI����  
		URI uri = null;
		FileSystem fs = null;
		FSDataOutputStream out = null;
		try {
			uri = new URI(path);
			fs = FileSystem.get(uri, new Configuration());//��ȡ�ļ�ϵͳ
			Path dst = new Path("/mktest/aa.txt");//Ҫ�������ļ���·��
			byte[] content = "�Ұ�����".getBytes();
			//�����ļ�
			out = fs.create(dst);
			//д����
			out.write(content);
			System.out.println("=======�ļ������ɹ�========");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				//�ر���
				out.close();
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	//5���ļ�������
	public static void renameFileOrDir() {

		//����URI����  
		URI uri = null;
		FileSystem fs = null;

		//���ļ����Ƶ�path
		//          Path oldName = new Path("/mktest/aa.txt") ;
		//          Path newName = new Path("/mktest/bb") ;     
		Path oldName = new Path("/mktest");
		Path newName = new Path("/mktest2");
		try {
			uri = new URI(path);
			fs = FileSystem.get(uri, new Configuration());//��ȡ�ļ�ϵͳ
			fs.rename(oldName, newName);
			System.out.println("=========�������ɹ�========");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//�����ļ�ϵͳ��ĳ��Ŀ¼
	public static void listDir() {

		//����URI����  
		URI uri = null;
		FileSystem fs = null;
		try {
			uri = new URI(path);
			fs = FileSystem.get(uri, new Configuration());
			//����Ҫ������Ŀ¼·��
			Path dst = new Path("/");
			//����listStatus()������ȡһ���ļ�����  
			//FileStatus�����װ���ļ��ĺ�Ŀ¼��Ԫ���ݣ������ļ����ȡ����С��Ȩ�޵���Ϣ
			FileStatus[] liststatus = fs.listStatus(dst);
			for (FileStatus ft : liststatus) {
				//�ж��Ƿ���Ŀ¼
				String isDir = ft.isDirectory() ? "�ļ���" : "�ļ�";
				//��ȡ�ļ���Ȩ��
				String permission = ft.getPermission().toString();
				//��ȡ���ݿ�
				short replication = ft.getReplication();
				//��ȡ����ĳ���
				long len = ft.getLen();
				//��ȡ�ļ���·��
				String filePath = ft.getPath().toString();
				System.out.println("�ļ���Ϣ��");
				System.out.println("�Ƿ���Ŀ¼�� " + isDir);
				System.out.println("�ļ�Ȩ�� " + permission);
				System.out.println("���ݿ� " + replication);
				System.out.println("�ļ�����  " + len);
				System.out.println("�ļ�·��  " + filePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	//ɾ���ļ�
	public static void delFile() {

		//����URI����  
		URI uri = null;
		FileSystem fs = null;
		try {
			uri = new URI(path);
			fs = FileSystem.get(uri, new Configuration());
			//              Path dst = new Path("/job.txt") ;
			Path dst = new Path("/mktest2");

			//������ɾ��ָ�����ļ���Ŀ¼�����Ŀ����һ����Ŀ¼�����ļ�����ôrecursive��ֵ�ͻᱻ���ԡ�
			//ֻ��recursive��trueʱ��һ���ǿ�Ŀ¼�������ݲŻᱻɾ��
			boolean flag = fs.delete(dst, true);
			if (flag) {
				System.out.println("==========ɾ���ɹ�=========");
			} else {
				System.out.println("==========ɾ��ʧ��=========");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	//�����ļ�
	public static void downFromHdfs() throws Exception {
		URI uri = new URI(path);
		FileSystem fs = FileSystem.get(uri, new Configuration());

		//Hadoop�ļ�ϵͳ��ͨ��Hadoop Path����������һ���ļ�
		Path src = new Path("/tfiles/a.txt");
		FSDataInputStream in = fs.open(src);

		File targetFile = new File("d://aa.txt");
		FileOutputStream out = new FileOutputStream(targetFile);
		//IOUtils��Hadoop�Լ��ṩ�Ĺ����࣬�ڱ�̵Ĺ������õķǳ�����
		//����Ǹ����������Ƿ�ʹ����رյ���˼
		IOUtils.copyBytes(in, out, 4096, true);
		System.out.println("=========�ļ����سɹ�=========");
	}

	public static void main(String[] args) throws Exception {
		System.setProperty("HADOOP_USER_NAME", "hadoop");
		//downFromHdfs();
		//createFile();
		listDir();
	}

}
