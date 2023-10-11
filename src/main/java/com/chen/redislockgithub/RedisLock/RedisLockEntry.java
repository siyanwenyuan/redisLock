package com.chen.redislockgithub.RedisLock;

public class RedisLockEntry {



    final Object object=new Object();
    public void entry01(){
        new Thread(()->{

            synchronized (object){
                System.out.println(Thread.currentThread().getName()+"\t"+"外层调用");
                synchronized (object){
                    System.out.println(Thread.currentThread().getName()+"\t"+"中层调用");

                    synchronized (object){
                        System.out.println(Thread.currentThread().getName()+"\t"+"内层调用");

                    }
                }
            }


        },"t1").start();
    }

    public void entry02(){
        m1();

    }

   private synchronized void m1(){
       System.out.println(Thread.currentThread().getName()+"外层调用");
       m2();
   }

    private synchronized void m2() {
        System.out.println(Thread.currentThread().getName()+"中层调用");
        m3();

    }

    private synchronized void m3() {
        System.out.println(Thread.currentThread().getName()+"内层调用");

    }

    public static void main(String[] args) {
        RedisLockEntry redisLockEntry=new RedisLockEntry();
        redisLockEntry.entry01();

        redisLockEntry.entry02();


    }
}
