package uaa.web.rest.util.HBase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HbaseTest {
//    public HBaseAdmin admin = null;
//    // 定义配置对象HBaseConfiguration
//    private static Configuration configuration;
//    public HbaseTest() throws Exception {
//        configuration = HBaseConfiguration.create();
//        configuration.set("hbase.zookeeper.quorum","10.1.8.241");  //hbase 服务地址
//        configuration.set("hbase.zookeeper.property.clientPort","2181"); //端口号
////        admin = new HBaseAdmin(configuration);
//    }
//    // Hbase获取所有的表信息
//    public List getAllTables() {
//        List<String> tables = null;
//        if (admin != null) {
//            try {
//                HTableDescriptor[] allTable = admin.listTables();
//                if (allTable.length > 0)
//                    tables = new ArrayList<String>();
//                for (HTableDescriptor hTableDescriptor : allTable) {
//                    tables.add(hTableDescriptor.getNameAsString());
//                    System.out.println(hTableDescriptor.getNameAsString());
//                }
//            }catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return tables;
//    }
//    public static void main(String[] args) throws Exception {
//        HbaseTest hbaseTest = new HbaseTest();
//        hbaseTest.getAllTables();
//    }
}
