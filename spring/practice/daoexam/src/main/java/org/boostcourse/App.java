package org.boostcourse;

/**
 * Hello world!
 *
 */
class Test{
    public void test(){
        System.out.println("call!");
        throw new RuntimeException();
    }
}
public class App 
{
    public static void main( String[] args ) {
        System.out.println( "before func call!" );

            Test t = new Test();
            t.test();
    }
}
