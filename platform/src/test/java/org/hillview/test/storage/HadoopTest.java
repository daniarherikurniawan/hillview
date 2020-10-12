package org.hillview.test.storage;

// import java.io.BufferedReader;
// import java.io.InputStreamReader;
// import java.net.InetSocketAddress;
// import java.security.PrivilegedExceptionAction;
// import java.util.Scanner;
// import java.util.regex.Pattern;

// import org.apache.hadoop.conf.*;
// import org.apache.hadoop.security.UserGroupInformation;
// import org.apache.hadoop.fs.Path;
// import org.apache.hadoop.hdfs.DFSClient;
// import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
// import org.apache.hadoop.hdfs.protocol.LocatedBlocks;
// import org.apache.hadoop.fs.FileSystem;
// import org.apache.hadoop.fs.FileStatus;

// import org.junit.Test;

// public class HadoopTest {
    
//     private final String filePath1 = "/user/hive/warehouse/invites/ds=2008-08-08/kv3.txt";
//     private final String filePath2 = "/user/hive/warehouse/invites/ds=2008-08-15/kv2.txt";

//     // @Test
//     public void testHadoopClient() {
//         try {
//             String remoteUser = "daniar";
//             UserGroupInformation ugi = UserGroupInformation.createRemoteUser(remoteUser);
//             ugi.doAs(new PrivilegedExceptionAction<Void>() {
// 				private Scanner scanner;

//                 @Override
// 				public Void run() throws Exception {
//                     Configuration conf = new Configuration(false);
//                     // String host = "cass-1.cassandra-mitmem.cs331-uc.emulab.net";
//                     String host = "localhost";
//                     conf.set("fs.defaultFS", "hdfs://" + host + ":9000/user/hive");
//                     conf.set("fs.default.name", conf.get("fs.defaultFS"));
//                     conf.set("hadoop.job.ugi", remoteUser);

//                     FileSystem fs = FileSystem.get(conf);
//                     // fs.createNewFile(new Path("/user/hbase/test"));

//                     FileStatus[] status = fs.listStatus(new Path("/user/hive/warehouse/"));
//                     for (int i = 0; i < status.length; i++) {
//                         System.out.println(status[i].getPath());
//                     }

//                     // Will list the file in a partition
//                     status = fs.listStatus(new Path("/user/hive/warehouse/invites/ds=2008-08-08"));
//                     for (int i = 0; i < status.length; i++) {
//                         System.out.println("File in the partition : " + status[i].getPath());
//                     }

//                     // Will get the replica that stores the file
//                     int nameNodeRpcPort = 9000;
//                     DFSClient dfsClient = new DFSClient(new InetSocketAddress(host, nameNodeRpcPort),conf);
//                     LocatedBlocks blocks = null;
//                     blocks = dfsClient.getNamenode().getBlockLocations(filePath1, 0, Long.MAX_VALUE);
//                     System.out.println("Locatedblock : " + blocks.get(0).toString());
//                     blocks = dfsClient.getNamenode().getBlockLocations(filePath2, 0, Long.MAX_VALUE);
//                     DatanodeInfo[] locations = blocks.get(0).getLocations();
//                     for (DatanodeInfo datanodeInfo : locations) {
//                         System.out.println(datanodeInfo.getIpAddr());
//                     }
                    
//                     // Read the local hdfs file from local node
//                     int i = 0;
//                     Pattern p = Pattern.compile("\u0001", Pattern.LITERAL);
//                     System.out.println("Reading : " + filePath1);
//                     Path pt = new Path("hdfs://localhost:9000" + filePath1);
//                     scanner = new Scanner(fs.open(pt));
//                     scanner.useDelimiter("\001");
//                     while (scanner.hasNextLine() && i < 6) {
//                         String items[] = p.split(scanner.nextLine());
//                         System.out.println(i + ": " + items.length);
//                         int n = 0;
//                         while (n < items.length) {
//                             System.out.println("  -" + items[n] + "-");
//                             n++;
//                         }
//                         i++;
//                     }

//                     i = 0;
//                     BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(pt)));
//                     try {
//                         String line;
//                         line = br.readLine();
//                         while (line != null && i < 6) {
//                             // String[] words = line.split("\001");
//                             System.out.println(i + ": " + line);
//                             String items[] = p.split(line);
//                             System.out.println(i + ": " + items.length);

//                             // System.out.println(i + ": " + words[0]);
//                             // be sure to read the next line otherwise you'll get an infinite loop
//                             line = br.readLine();
//                             i++;
//                         }
//                     } finally {
//                         // you should close out the BufferedReader
//                         br.close();
//                     }
//                     fs.close();

//                     return null;
// 				} 
//             });
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }

// }