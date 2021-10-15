package ui;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<String>();
        list.add(null);
        list.add(null);
        list.add(null);
        System.out.println(list);
        list.add("5");
        list.set(1,"3");
        System.out.println(list);
        System.out.println(list.size());
        ArrayList<String> list1 = new ArrayList<String>();
        list1.add("3");
        System.out.println(list1);
        list.addAll(list1);
        System.out.println(list);



    }
}
