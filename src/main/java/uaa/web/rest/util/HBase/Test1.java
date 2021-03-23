package uaa.web.rest.util.HBase;

//
//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.hbase.*;
//import org.apache.hadoop.hbase.client.*;
//import org.apache.hadoop.hbase.util.Bytes;
//
//import java.io.IOException;
//import java.util.*;


public class Test1 {
//    public Connection connection;
//    public static  HBaseAdmin admin = null;
//    // 用HBaseconfiguration初始化配置信息是会自动加载当前应用的classpath下的hbase-site.xml
//    public static Configuration configuration;
//    {
//        configuration = HBaseConfiguration.create();
//        configuration.set("hbase.zookeeper.quorum","193.112.161.157");  //hbase 服务地址
//        configuration.set("hbase.zookeeper.property.clientPort","2181"); //端口号
//
//
//    }
//
//    public static void main(String[] args) throws Exception{
//        LamdaDemo test1 = new LamdaDemo();
////        test1.admin = new HBaseAdmin(configuration);
//
//
////        test1.createTable("app_log_log","id","num");
//    }
//    public List getAllTables() {
////        原文：https://blog.csdn.net/yz930618/article/details/74173332
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
//    public LamdaDemo() throws Exception {
//        // 对connection进行初始化、
//        // 当然也可以手动加载配置文件，手动加载配置文件时要调用configuration的addResource方法
//         configuration.addResource("hbase-site.xml");
//        connection = ConnectionFactory.createConnection(configuration);
//    }
//    public void createTable(String tableName , String... cf1)throws Exception{
//        Admin admin = connection.getAdmin();
//        //HTD需要TableName类型的tableName，创建TableName类型的tableName
//        TableName tbName = TableName.valueOf(tableName);
//        //判断表述否已存在，不存在则创建表
//        if(admin.tableExists(tbName)){
//            System.err.println("表" + tableName + "已存在！");
//            return;
//        }
//        //通过HTableDescriptor创建一个HTableDescriptor将表的描述传到createTable参数中
//        HTableDescriptor HTD = new HTableDescriptor(tbName);
//        //为描述器添加表的详细参数
//        for(String cf : cf1) {
//            // 创建HColumnDescriptor对象添加表的详细的描述
//            HColumnDescriptor HCD = new HColumnDescriptor(cf);
//            HTD.addFamily(HCD);
//        }
//        //调用createtable方法创建表
//        admin.createTable(HTD);
//    }
//    public void deleteTable(String tableName) throws Exception {
//        Admin admin = connection.getAdmin();
//        //通过tableName创建表名
//        TableName tbName = TableName.valueOf(tableName);
//        //判断表是否存在，若存在就删除，不存在就退出
//        if (admin.tableExists(tbName)) {
//            //首先将表解除占用，否则无法删除
//            admin.disableTable(tbName);
//            //调用delete方法
//            admin.deleteTable(tbName);
//            System.err.println("表" + tableName + "已删除");
//        }else{
//            System.err.println("表" + tableName + "不存在！");
//        }
//    }
//    public void getData_1() throws Exception{
//        //获取想要查询的表的TableName
//        TableName tbname = TableName.valueOf("bd14:fromJava");
//        //通过tbName获得Table对象
//        Table table =connection.getTable(tbname);
//        //创建Get的集合以承接查询的条件
//        List<Get> gets = new ArrayList<>();
//        //循环五次，取前五个测试数据
//        for(int i=0;i<5;i++){
//            //就将查询条件放入get对象中
//            Get get = new Get(Bytes.toBytes("rowkey_"+i));
//            //将get对象放入聚合
//            gets.add(get);
//        }
//        //调用table.get方法传入查询条件，获得查询的结果的数组
//        Result[] results = table.get(gets);
//        //遍历结果数组，利用CellScanner配合cellUtil获得对应的数据
//        for (Result result : results) {
//            //调用result.cellscanner创建scanner对象
//            CellScanner cellScanner = result.cellScanner();
//            //遍历结果集，取出查询结果，
//            //如果存在下一个cell则advandce方法返回true，且current方法会返回一个有效的cell，可以当作循环条件
//            while (cellScanner.advance()) {
//                //current方法返回一个有效的cell
//                Cell cell = cellScanner.current();
//                //使用CellUtil调用相应的方法获取想用的数据，并利用Bytes.toString方法将结果转换为string输出
//                String family = Bytes.toString(CellUtil.cloneFamily(cell));
//                String qualify = Bytes.toString(CellUtil.cloneQualifier(cell));
//                String rowkey = Bytes.toString(CellUtil.cloneRow(cell));
//                String value = Bytes.toString(CellUtil.cloneValue(cell));
//                System.err.println(family+"_"+qualify+"_"+rowkey+"_"+value);
//            }
//        }
//    }
//
//    public void putData() throws Exception{
//        //通过表名获取tbName
//        TableName tbname = TableName.valueOf("bd14:fromJava");
//        //通过connection获取相应的表
//        Table table =connection.getTable(tbname);
//        //创建Random对象以作为随机参数
//        Random random = new Random();
//        //hbase支持批量写入数据，创建Put集合来存放批量的数据
//        List<Put> batput = new ArrayList<>();
//        //循环10次，创建10组测试数据放入list中
//        for(int i=0;i<10;i++){
//            //实例化put对象，传入行键
//            Put put =new Put(Bytes.toBytes("rowkey_"+i));
//            //调用addcolum方法，向i簇中添加字段
//            put.addColumn(Bytes.toBytes("i"), Bytes.toBytes("username"),Bytes.toBytes("un_"+i));
//            put.addColumn(Bytes.toBytes("i"), Bytes.toBytes("age"),Bytes.toBytes(random.nextInt(50)+1));
//            put.addColumn(Bytes.toBytes("i"), Bytes.toBytes("birthday"),Bytes.toBytes("2017"+i));
//            put.addColumn(Bytes.toBytes("i"), Bytes.toBytes("phone"),Bytes.toBytes("phone:"+i));
//            put.addColumn(Bytes.toBytes("i"), Bytes.toBytes("邮箱"),Bytes.toBytes("邮箱:"+i));
//            //将测试数据添加到list中
//            batput.add(put);
//        }
//        //调用put方法将list中的测试数据写入hbase
//        table.put(batput);
//        System.err.println("数据插入完成！");
//    }
//    public void getData_2() throws Exception {
//        TableName tbname = TableName.valueOf("bd14:fromJava");
//        Table table = connection.getTable(tbname);
//        List<Get> gets = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            Get get = new Get(Bytes.toBytes("rowkey_" + i));
//            gets.add(get);
//        }
//        Result[] results = table.get(gets);
//        for (Result result : results) {
//            NavigableMap<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> map = result.getMap();
//            for (byte[] cf : map.keySet()) {
//                NavigableMap<byte[], NavigableMap<Long, byte[]>> valueWithColumnQualify = map.get(cf);
//                for (byte[] columnQualify : valueWithColumnQualify.keySet()) {
//                    NavigableMap<Long, byte[]> valueWithTimestamp = valueWithColumnQualify.get(columnQualify);
//                    for (Long ts : valueWithTimestamp.keySet()) {
//                        byte[] value = valueWithTimestamp.get(ts);
//                        String rowKey = Bytes.toString(result.getRow());
//                        String columnFamily = Bytes.toString(cf);
//                        String columnqualify = Bytes.toString(columnQualify);
//                        String timestamp = new Date(ts) + "";
//                        String values = Bytes.toString(columnQualify);
//                        System.out.println(rowKey + "-" + columnFamily + "-" + columnqualify + "-" + timestamp + "-" + values);
//                    }
//                }
//            }
//        }
//    }
//    public void getData_3() throws Exception{
//        TableName tbname = TableName.valueOf("bd14:fromJava");
//        Table table =connection.getTable(tbname);
//        List<Get> gets = new ArrayList<>();
//        for(int i=0;i<5;i++){
//            Get get = new Get(Bytes.toBytes("rowkey_"+i));
//            gets.add(get);
//        }
//        Result[] results = table.get(gets);
//        //遍历结果对象results
//        for (Result result : results) {
//            //嵌套遍历result获取cell
//            for(Cell cell : result.listCells()){
//                //使用CellUtil工具类直接获取cell中的数据
//                String family = Bytes.toString(CellUtil.cloneFamily(cell));
//                String qualify = Bytes.toString(CellUtil.cloneQualifier(cell));
//                String rowkey = Bytes.toString(CellUtil.cloneRow(cell));
//                String value = Bytes.toString(CellUtil.cloneValue(cell));
//                System.err.println(family+"_"+qualify+"_"+rowkey+"_"+value);
//            }
//        }
//
//    }
//    public void getData() throws Exception{
//        TableName tbname = TableName.valueOf("bd14:fromJava");
//        Table table =connection.getTable(tbname);
//        List<Get> gets = new ArrayList<>();
//        for(int i=0;i<5;i++){
//            Get get = new Get(Bytes.toBytes("rowkey_"+i));
//            gets.add(get);
//        }
//        Result[] results = table.get(gets);
//        for (Result result : results) {
//            //嵌套遍历result获取cell
//            for(Cell cell : result.listCells()){
//                //使用CellUtil工具类直接获取cell中的数据
//                String family = Bytes.toString(CellUtil.cloneFamily(cell));
//                String qualify = Bytes.toString(CellUtil.cloneQualifier(cell));
//                String rowkey = Bytes.toString(CellUtil.cloneRow(cell));
//                String value = Bytes.toString(CellUtil.cloneValue(cell));
//                System.err.println(family+"_"+qualify+"_"+rowkey+"_"+value);
//            }
//        }
//
//    }
//    public void updateData(String tableName,String rowKey,String family, String columkey,String updatedata) throws Exception{
//        //hbase中更新数据同样采用put方法，在相同的位置put数据，则在查询时只会返回时间戳较新的数据
//        //且在文件合并时会将时间戳较旧的数据舍弃
//        Put put = new Put(Bytes.toBytes(rowKey));
//        //将新数据添加到put中
//        put.addColumn(Bytes.toBytes(family), Bytes.toBytes(columkey),Bytes.toBytes(updatedata));
//        Table table = connection.getTable(TableName.valueOf(tableName));
//        //将put写入HBase
//        table.put(put);
//    }
//    //删除某条记录
//    public void deleteData(String tableName,String rowKey,String family, String columkey) throws Exception{
//        Table table = connection.getTable(TableName.valueOf(tableName));
//        //创建delete对象
//        Delete deletData= new Delete(Bytes.toBytes(rowKey));
//        //将要删除的数据的准确坐标添加到对象中
//        deletData.addColumn(Bytes.toBytes(family), Bytes.toBytes(columkey));
//        //删除表中数据
//        table.delete(deletData);
//    }
//
//    //删除一行数据
//    public void deleteRow(String tableName,String rowKey) throws Exception{
//        Table table = connection.getTable(TableName.valueOf(tableName));
//        //通过行键删除一整行的数据
//        Delete deletRow= new Delete(Bytes.toBytes(rowKey));
//        table.delete(deletRow);
//    }

}

//        ---------------------
//            作者：Flying_Driver
//        来源：CSDN
//        原文：https://blog.csdn.net/sinat_39409672/article/details/78403015
//        版权声明：本文为博主原创文章，转载请附上博文链接！
//}
