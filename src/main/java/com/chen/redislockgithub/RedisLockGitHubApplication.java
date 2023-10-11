package com.chen.redislockgithub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RedisLockGitHubApplication {


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
    public static void main(String[] args) {
        SpringApplication.run(RedisLockGitHubApplication.class, args);
    }

    RedisLockGitHubApplication redisLockGitHubApplication=new RedisLockGitHubApplication();


}
