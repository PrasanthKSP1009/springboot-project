package com.example.springbootfirst.controllers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloTest {
  @Test
  public void tesHelloTest(){
    Hello hell = new Hello();
    String str = hell.helloTest();
    assertEquals("Hello Test",str);
  }
}
