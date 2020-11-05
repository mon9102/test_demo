package com.testDemo.maven;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Auther: zouren
 * @Date: 2020/2/27 15:33
 * @Description:
 */
public class FileUtil {
    String fileEnd = ".jar.lastUpdated";

    @Test
    public void delFile() {
        String mavenRepository = "D:/java/mavenrepository";

        Set<String> re = new HashSet<String>(100000);

        getNextFile(new File(mavenRepository), re);
        System.out.println(re);
        re.stream().forEach(path -> deleteDir(new File(path)));

    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    private void getNextFile(File file, Set<String> fileList) {
//        System.out.println(file.getPath());
        if (file.isFile()) {
            if (file.getName().endsWith(fileEnd)) {
                fileList.add(file.getParent());
            }
        } else {
            File[] nextFile = file.listFiles();
            for (int i = 0; i < nextFile.length; i++) {
                getNextFile(nextFile[i], fileList);
            }
        }
    }
}
