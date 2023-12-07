package com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.sticky_header;

import java.util.Locale;


public class Person implements Comparable<Person> {
    private static final String NAME_DISPLAY = "%s, %s";
    private final CharSequence firstName;
    private final CharSequence lastName;

    public Person(CharSequence charSequence, CharSequence charSequence2) {
        this.firstName = charSequence;
        this.lastName = charSequence2;
    }

    public CharSequence getLastName() {
        return this.lastName;
    }

    public CharSequence getFirstName() {
        return this.firstName;
    }

    public String getFullName() {
        return String.format(Locale.getDefault(), NAME_DISPLAY, getLastName(), getFirstName());
    }

    @Override
    public int compareTo(Person person) {
        return getLastName().toString().compareTo(person.getLastName().toString());
    }
}
