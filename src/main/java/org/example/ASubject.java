package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ASubject {
    private int id;
    private int index_number;
    private String subject;
    private int rate;


    public ASubject(String[] arr) {
        this(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), arr[2], Integer.parseInt(arr[3]));
    }
}
