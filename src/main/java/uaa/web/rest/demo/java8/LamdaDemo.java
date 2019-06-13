package uaa.web.rest.demo.java8;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class LamdaDemo {
    int[] arr = {4,12,1,3,7,9};

    @Test
    public void collect () {
        ArrayList<Integer> list = Arrays.stream(arr).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        System.out.println(list);
        list.stream().forEach(System.out::println);
        Set<Integer> set = list.stream().collect(Collectors.toSet());

        Map<String, Integer> collect = list.stream().collect(Collectors.toMap(a -> "k" + a.toString(), a -> a));
//        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
//        objectObjectHashMap
        System.out.println(collect);
    }

    @Test
    public void filter() {
        Arrays.stream(arr).filter((x)-> x%2 !=0).forEach(System.out::println);
    }
    @Test
    public void map (){
        Arrays.stream(arr).map(x -> x * x).forEach(System.out::println);
    }
    @Test
    public void reduce(){
        Arrays.stream(arr).reduce((x,y)->x+y).ifPresent(System.out::println);
    }
//    @Test
//    public void groupBy(){
//        Map<String,List<Artist>> rs = SampleData.getThreeArtists().stream().collect(Collectors.groupingBy(Artist::getNationality));
//        System.out.println(rs);
//    }
//    @Test
//    public void join(){
//        String joinedNames = SampleData.getThreeArtists().stream().map(Artist::getName).collect(Collectors.joining(","));
//        System.out.println(joinedNames);
//        joinedNames.chars().mapToObj(c -> (char) Character.toUpperCase(c)).forEach(System.out::println);
//    }
    @Test
    public void average(){
        Arrays.stream(arr).average().ifPresent(System.out::println);
    }

    @Test
    public void sum(){
        System.out.println(Arrays.stream(arr).sum());
    }

    @Test
    public void max(){
        Arrays.stream(arr).max().ifPresent(System.out::println);
    }

    @Test
    public void min(){
        Arrays.stream(arr).min().ifPresent(System.out::println);
    }

    public class UtilDemo {

        int[] data = {4,12,1,3,5,7,9};

        @Test
        public void parallelSort(){
            Arrays.parallelSort(data);
            System.out.println(Arrays.toString(data));
        }

        @Test
        public void testCollectPrallel() {
            //[4, 16, 17, 20, 25, 32, 41]
            Arrays.parallelPrefix(data, Integer::sum);
            System.out.println(Arrays.toString(data));
        }
        @Test
        public void list() throws IOException {
            Files.list(Paths.get(".")).filter(Files::isDirectory).forEach(System.out::println);
        }

        @Test
        public void walk() throws IOException {
            Files.walk(Paths.get("."), FileVisitOption.FOLLOW_LINKS).forEach(System.out::println);
        }
    }

}
