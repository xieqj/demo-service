package com.sinotn.demo.sigar;

import java.util.Properties;

import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.Swap;
import org.hyperic.sigar.Who;
import org.junit.Test;

public class SigarTest {

	@Test
	public void testSys(){
		System.out.println(System.getenv("java.library.path"));
		System.out.println(System.getProperty("java.library.path"));
		Sigar sigar=new Sigar();
		try{
			// 取当前操作系统的信息
			OperatingSystem os = OperatingSystem.getInstance();
			// 操作系统内核类型如： 386、486、586等x86
			print("OS.getArch() = " + os.getArch());
			print("OS.getCpuEndian() = " + os.getCpuEndian());//
			print("OS.getDataModel() = " + os.getDataModel());//
			// 系统描述
			print("OS.getDescription() = " + os.getDescription());
			print("OS.getMachine() = " + os.getMachine());//
			// 操作系统类型
			print("OS.getName() = " + os.getName());
			print("OS.getPatchLevel() = " + os.getPatchLevel());//
			// 操作系统的卖主
			print("OS.getVendor() = " + os.getVendor());
			// 卖主名称
			print("OS.getVendorCodeName() = " + os.getVendorCodeName());
			// 操作系统名称
			print("OS.getVendorName() = " + os.getVendorName());
			// 操作系统卖主类型
			print("OS.getVendorVersion() = " + os.getVendorVersion());
			// 操作系统的版本号
			print("OS.getVersion() = " + os.getVersion());

			// 取当前系统进程表中的用户信息
			Who who[] = sigar.getWhoList();
			if (who != null && who.length > 0) {
				for (int i = 0; i < who.length; i++) {
					print("\n~~~~~~~~~" + String.valueOf(i) + "~~~~~~~~~");
					Who _who = who[i];
					print("getDevice() = " + _who.getDevice());
					print("getHost() = " + _who.getHost());
					print("getTime() = " + _who.getTime());
					// 当前系统进程表中的用户名
					print("getUser() = " + _who.getUser());
				}
			}
		}catch(Throwable e){
			e.printStackTrace();
		}finally{
			sigar.close();
		}
	}

	@Test
	public void property(){
		Runtime r = Runtime.getRuntime();
		Properties props = System.getProperties();


		System.out.println("Java的运行环境供应商：    " + props.getProperty("java.vendor"));
		System.out.println("Java供应商的URL：    " + props.getProperty("java.vendor.url"));


		System.out.println("Java的虚拟机规范供应商：    " + props.getProperty("java.vm.specification.vendor"));
		System.out.println("Java的虚拟机规范名称：    " + props.getProperty("java.vm.specification.name"));
		System.out.println("Java的虚拟机实现版本：    " + props.getProperty("java.vm.version"));
		System.out.println("Java的虚拟机实现供应商：    " + props.getProperty("java.vm.vendor"));
		System.out.println("Java运行时环境规范名称：    " + props.getProperty("java.specification.name"));
		System.out.println("Java的虚拟机规范版本：    " + props.getProperty("java.vm.specification.version"));

		System.out.println("Java运行时环境规范版本：    " + props.getProperty("java.specification.version"));
		System.out.println("Java运行时环境规范供应商：    " + props.getProperty("java.specification.vender"));

		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("JVM可以使用的总内存:    " + r.totalMemory());
		System.out.println("JVM可以使用的剩余内存:    " + r.freeMemory());
		System.out.println("JVM可以使用的处理器个数:    " + r.availableProcessors());

		System.out.println("Java的运行环境版本：    " + props.getProperty("java.version"));
		System.out.println("Java的类格式版本号：    " + props.getProperty("java.class.version"));
		System.out.println("Java的虚拟机实现名称：    " + props.getProperty("java.vm.name"));

		System.out.println("Java的安装路径：    " + props.getProperty("java.home"));
		System.out.println("Java的类路径：    " + props.getProperty("java.class.path"));
		System.out.println("加载库时搜索的路径列表：    " + props.getProperty("java.library.path"));
		System.out.println("默认的临时文件路径：    " + props.getProperty("java.io.tmpdir"));
		System.out.println("一个或多个扩展目录的路径：    " + props.getProperty("java.ext.dirs"));
	}

	private void print(String str){
		System.out.println(str);
	}

	@Test
	public void memory() throws Exception{
		Sigar sigar = new Sigar();
		Mem mem = sigar.getMem();
		// 内存总量
		System.out.println("内存总量:    " + mem.getTotal() / 1024L + "K av");
		// 当前内存使用量
		System.out.println("当前内存使用量:    " + mem.getUsed() / 1024L + "K used");
		// 当前内存剩余量
		System.out.println("当前内存剩余量:    " + mem.getFree() / 1024L + "K free");
		Swap swap = sigar.getSwap();
		// 交换区总量
		System.out.println("交换区总量:    " + swap.getTotal() / 1024L + "K av");
		// 当前交换区使用量
		System.out.println("当前交换区使用量:    " + swap.getUsed() / 1024L + "K used");
		// 当前交换区剩余量
		System.out.println("当前交换区剩余量:    " + swap.getFree() / 1024L + "K free");

	}

	@Test
	public void file() throws Exception {
		Sigar sigar = new Sigar();
		FileSystem fslist[] = sigar.getFileSystemList();
		System.out.println(fslist.length);
		for (int i = 0; i < fslist.length; i++) {
			System.out.println("分区的盘符名称" + i);
			FileSystem fs = fslist[i];
			// 分区的盘符名称
			System.out.println("盘符名称:    " + fs.getDevName());
			// 分区的盘符名称
			System.out.println("盘符路径:    " + fs.getDirName());
			System.out.println("盘符标志:    " + fs.getFlags());//
			// 文件系统类型，比如 FAT32、NTFS
			System.out.println("盘符类型:    " + fs.getSysTypeName());
			// 文件系统类型名，比如本地硬盘、光驱、网络文件系统等
			System.out.println("盘符类型名:    " + fs.getTypeName());
			// 文件系统类型
			System.out.println("盘符文件系统类型:    " + fs.getType());
			FileSystemUsage usage = null;
			usage = sigar.getFileSystemUsage(fs.getDirName());
			switch (fs.getType()) {
			case 0: // TYPE_UNKNOWN ：未知
				break;
			case 1: // TYPE_NONE
				break;
			case 2: // TYPE_LOCAL_DISK : 本地硬盘
				// 文件系统总大小
				System.out.println(fs.getDevName() + "总大小:    " + usage.getTotal() + "KB");
				// 文件系统剩余大小
				System.out.println(fs.getDevName() + "剩余大小:    " + usage.getFree() + "KB");
				// 文件系统可用大小
				System.out.println(fs.getDevName() + "可用大小:    " + usage.getAvail() + "KB");
				// 文件系统已经使用量
				System.out.println(fs.getDevName() + "已经使用量:    " + usage.getUsed() + "KB");
				double usePercent = usage.getUsePercent() * 100D;
				// 文件系统资源的利用率
				System.out.println(fs.getDevName() + "资源的利用率:    " + usePercent + "%");
				break;
			case 3:// TYPE_NETWORK ：网络
				break;
			case 4:// TYPE_RAM_DISK ：闪存
				break;
			case 5:// TYPE_CDROM ：光驱
				break;
			case 6:// TYPE_SWAP ：页面交换
				break;
			}
			System.out.println(fs.getDevName() + "读出：    " + usage.getDiskReads());
			System.out.println(fs.getDevName() + "写入：    " + usage.getDiskWrites());
		}
		return;
	}

	@Test
	public void file2() throws Exception {
		Sigar sigar = new Sigar();
		FileSystem fslist[] = sigar.getFileSystemList();
		StringBuilder sb;
		FileSystemUsage usage = null;
		for (int i = 0; i < fslist.length; i++) {
			FileSystem fs = fslist[i];
			if(fs.getType()==2){
				sb=new StringBuilder();
				sb.append(fs.getDevName());
				sb.append(" ").append(fs.getSysTypeName());

				usage = sigar.getFileSystemUsage(fs.getDirName());
				sb.append("\r\n  total:").append(usage.getTotal()).append("KB");
				sb.append("  free:").append(usage.getFree()).append("KB");
				sb.append("  avail:").append(usage.getAvail()).append("KB");
				System.out.println(sb.toString());
			}
		}
		sigar.close();
	}
}
