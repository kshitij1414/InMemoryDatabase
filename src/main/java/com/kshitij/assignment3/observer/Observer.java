package com.kshitij.assignment3.observer;

public class Observer implements IObserver{
    public Observer() {

    }

    public void update(String message) {
        System.out.println(message);
    }
}
