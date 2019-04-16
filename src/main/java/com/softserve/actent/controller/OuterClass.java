package com.softserve.actent.controller;

public class OuterClass {
    public String outerStr = "Hello";
    public static class InnerClass{
        public static void staticInnerMethod(){}
    }

    public static void main(String[] args) {
        OuterClass outerClass = new OuterClass();
        outerClass.getClass();
//        OuterClass.InnerClass innerClass = outerClass.new InnerClass();
    }
    public void someMethod(){
       class InnerClass{

        }
        class InnerClass1{

        }
        class InnerClass2{

        }
        class InnerClass3{

        }
    }
}
