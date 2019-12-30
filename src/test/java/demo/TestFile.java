package demo;

import org.junit.Test;

import java.io.File;

public class TestFile {

    @Test
    public void testF(){
        File file = new File("./");
        if(file.isDirectory()){
                File files[] = file.listFiles();
                for(File f:files){
                    System.out.println(f.getName());
                }
        }
    }
}
