package ca.cmpt213.as2;

import java.io.File;
import java.io.FileFilter;


public class JsonFileFilter {

    public static File[] anonFilter(String folderPath){

        FileFilter filter = new FileFilter(){ //do i need to implement filefilter in order to use its methods since file filter is a interface
            @Override
            public boolean accept(File file){
                return file.getName().endsWith(".json");

            }
        };
        File folder = new File(folderPath);

        File[] fileList = folder.listFiles(filter);
        return fileList;


    }


}
