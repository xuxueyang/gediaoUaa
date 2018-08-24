package uaa.web.rest.util.neo4j;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Test1 {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File("Data/Test"));
        System.out.println("Database Load!");
        //开启事务
        try (Transaction tx = graphDb.beginTx()) {
            // Perform DB operations
            Node steve = graphDb.createNode(Labels.USER);
            steve.setProperty("name", "Steve");
            Node linda = graphDb.createNode(Labels.USER);
            linda.setProperty("name", "Linda");
            steve.createRelationshipTo( linda, RelationshipTypes.IS_FRIEND_OF );
            System.out.println("created node name is" + steve.getProperty("name"));
            tx.success();
        }
        //查询数据库
        String query ="match (n:USER) return n.name as name";
        Map<String, Object > parameters = new HashMap<String, Object>();
        try ( Result result = graphDb.execute( query, parameters ) )
        {
            while ( result.hasNext() )
            {
                Map<String, Object> row = result.next();
                for ( String key : result.columns() )
                {
                    System.out.printf( "%s = %s%n", key, row.get( key ) );
                }
            }
        }

        registerShutdownHook(graphDb);
        System.out.println("Database Shutdown!");

    }
    //设置标签，但是必须继承于接口label
    public enum Labels implements Label {
        USER,
        MOVIE;
    }

    public enum RelationshipTypes implements RelationshipType {
        IS_FRIEND_OF,
        HAS_SEEN;
    }


    private static void registerShutdownHook(final GraphDatabaseService graphDb){
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run(){
                graphDb.shutdown();
            }
        });
    }
}
