package com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.sticky_header;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PeopleRepo {
    public static List<Person> getPeople() {
        ArrayList arrayList = new ArrayList(45);
        arrayList.add(new Person("George", "Washington"));
        arrayList.add(new Person("John", "Adams"));
        arrayList.add(new Person("Thomas", "Jefferson"));
        arrayList.add(new Person("James", "Madison"));
        arrayList.add(new Person("James", "Monroe"));
        arrayList.add(new Person("John Quincy", "Adams"));
        arrayList.add(new Person("Andrew", "Jackson"));
        arrayList.add(new Person("Martin", "Van Buren"));
        arrayList.add(new Person("William", "Harrison"));
        arrayList.add(new Person("John", "Tyler"));
        arrayList.add(new Person("Zachary", "Taylor"));
        arrayList.add(new Person("Millard", "Fillmore"));
        arrayList.add(new Person("Franklin", "Pierce"));
        arrayList.add(new Person("James", "Buchanan"));
        arrayList.add(new Person("Abraham", "Lincoln"));
        arrayList.add(new Person("Andrew", "Johnson"));
        arrayList.add(new Person("Ulysses", "Grant"));
        arrayList.add(new Person("Rutherford", "Hayes"));
        arrayList.add(new Person("James", "Garfield"));
        arrayList.add(new Person("Chester", "Arthur"));
        arrayList.add(new Person("Grover", "Cleveland"));
        arrayList.add(new Person("Benjamin", "Harrison"));
        arrayList.add(new Person("William", "McKinley"));
        arrayList.add(new Person("Theodore", "Roosevelt"));
        arrayList.add(new Person("William", "Taft"));
        arrayList.add(new Person("Woodrow", "Wilson"));
        arrayList.add(new Person("Warren", "Harding"));
        arrayList.add(new Person("Calvin", "Coolidge"));
        arrayList.add(new Person("Herbert", "Hoover"));
        arrayList.add(new Person("Harry", "Truman"));
        arrayList.add(new Person("Dwight", "Eisenhower"));
        arrayList.add(new Person("John", "Kennedy"));
        arrayList.add(new Person("Lyndon", "Johnson"));
        arrayList.add(new Person("Richard", "Nixon"));
        arrayList.add(new Person("Gerald", "Ford"));
        arrayList.add(new Person("Jimmy", "Carter"));
        arrayList.add(new Person("Ronald", "Reagan"));
        arrayList.add(new Person("George H.W.", "Bush"));
        arrayList.add(new Person("Bill", "Clinton"));
        arrayList.add(new Person("George W.", "Bush"));
        arrayList.add(new Person("Barack", "Obama"));
        arrayList.add(new Person("Donald", "Trump"));
        return arrayList;
    }

    public static List<Person> getPeopleSorted() {
        List<Person> people = getPeople();
        Collections.sort(people);
        return people;
    }
}
