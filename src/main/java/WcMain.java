import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Red Date.
 * @date 2020/3/15 15:07
 */
public class WcMain {
    public static void main(String[] args) throws IOException {
        //优先级最高的参数式"-x",其次是"-s",优先判断这两个
        if (Arrays.asList(args).contains("-x")) {
            WcView.view();
            return;
        }
        if(args.length<2){
            System.out.println("【参数缺少，请输入正确的命令】");
            return;
        }
        //参数和路径分离
        int length = args.length;
        String filePath = args[length - 1];
        if(filePath.endsWith("?")){
            System.out.println("【通配符使用错误】");
            return;
        }
        String[] argArray = WcUtils.arrayDelete(args, args[length - 1]);
        String[] allArray = {"-c", "-w", "-l", "-a", "-s", "-x"};
        //判断参数是否正确
        Set<String> allSet = new HashSet<String>(Arrays.asList(allArray));
        Set<String> argsSet = new HashSet<String>(Arrays.asList(argArray));
        //传入的参数数组对全部参数取差集
        argsSet.removeAll(allSet);
        if (argsSet.size() > 0) {
            System.out.println("【参数错误，请输入正确的参数】");
            return;
        }
        //获取当前的目录
        File directory = new File(".");
        String currentDirectory = directory.getCanonicalPath();
        //System.out.println(currentDirectory);
        //判断路径或文件名是否正确
        File file = new File(filePath);
        //判断是否有参数"-s"
        if (Arrays.asList(argArray).contains("-s")) {
            //"-s"不能单独使用
            if (argArray.length > 1) {
                if (file.exists()) {
                    if (file.isFile()) {
                        System.out.println("【不能对文件使用参数 -s】");
                        return;
                    } else {
                        //去掉参数"-s"
                        String[] result = WcUtils.arrayDelete(argArray, "-s");
                        List<String> lists = new ArrayList<>();
                        WcUtils.recursive(filePath, lists);
                        for (String string : lists) {
                            System.out.println("【文件名】：" + string);
                            for (int i = 0; i < length - 2; i++) {
                                WcUtils.operation(string, result[i]);
                            }
                        }
                    }
                } else {  //文件不存在，先匹配通配符
                    if (filePath.contains("*") || filePath.contains("?")) {
                        //分割获取路径和通配符
                        String[] path1 = filePath.split("\\\\");
                        String pattern = path1[path1.length - 1];
                        String path = "";
                        if(path1.length==1){
                            path = currentDirectory;
                        }else {
                            for (int i = 0; i < path1.length - 1; i++) {
                                if (i == path1.length - 2) {
                                    path = path + path1[i];
                                    break;
                                }
                                path = path + path1[i] + "\\";
                            }
                        }
                        //遍历目录
                        List<String> files = new ArrayList<>();
                        WcUtils.recursive(path, files);
                        if (files.isEmpty()) {
                            System.out.println("【路径错误，该文件或目录不存在】");
                            return;
                        }
                        List<String> wildcardResult = WcUtils.wildcardMatching(files, pattern);
                        if (wildcardResult.isEmpty()) {
                            System.out.println("【没有与查找到符合条件的文件】");
                            return;
                        } else {
                            //去掉参数"-s"
                            String[] result = WcUtils.arrayDelete(argArray, "-s");
                            for (String string : wildcardResult) {
                                System.out.println("【文件名】：" + string);
                                for (int i = 0; i < length - 2; i++) {
                                    WcUtils.operation(string, result[i]);
                                }
                            }
                        }
                    }else {
                        System.out.println("【路径错误，该文件或目录不存在】");
                        return;
                    }
                }
            } else {
                System.out.println("【参数 -s 不能单独使用，请配合其他参数一起使用】");
                return;
            }
        } else {  //不含有参数"-s"
            //判断是否有通配符
            if (filePath.contains("*") || filePath.contains("?")) {
                //分割获取路径和通配符
                String[] path1 = filePath.split("\\\\");
                String pattern = path1[path1.length - 1];
                String path = "";
                if(path1.length==1){
                    path = currentDirectory;
                }else {
                    for (int i = 0; i < path1.length - 1; i++) {
                        if (i == path1.length - 2) {
                            path = path + path1[i];
                            break;
                        }
                        path = path + path1[i] + "\\";
                    }
                }
                //遍历目录
                List<String> files = new ArrayList<>();
                WcUtils.recursiveNotContainSub(path, files);
                if (files.isEmpty()) {
                    System.out.println("【路径错误，该文件或目录不存在】");
                    return;
                }
                List<String> wildcardResult = WcUtils.wildcardMatching(files, pattern);
                if (wildcardResult.isEmpty()) {
                    System.out.println("【没有与查找到符合条件的文件】");
                    return;
                } else {
                    for (String string : wildcardResult) {
                        System.out.println("【文件名】：" + string);
                        for (int i = 0; i < length - 1; i++) {
                            WcUtils.operation(string, argArray[i]);
                        }
                    }
                }
            } else { //不含通配符
                if (file.isFile()) {
                    for (int i = 0; i < length - 1; i++) {
                        WcUtils.operation(filePath, argArray[i]);
                    }
                } else {
                    //遍历目录
                    List<String> files = new ArrayList<>();
                    WcUtils.recursiveNotContainSub(filePath, files);
                    if (files.isEmpty()) {
                        System.out.println("【路径错误，该文件或目录不存在】");
                        return;
                    } else {
                        for (String string : files) {
                            System.out.println("【文件名】：" + string);
                            for (int i = 0; i < length - 1; i++) {
                                WcUtils.operation(string, argArray[i]);
                            }
                        }
                    }
                }
            }
        }
    }
}
