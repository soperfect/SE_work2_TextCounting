import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Red Date.
 * @date 2020/3/15 15:08
 */
public class WcUtils {

    public static void operation(String path,String parameter) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        switch (parameter){
            case "-c":
                int countChar = WcUtils.characterCounting(br);
                System.out.println("【字符数】："+countChar);
                break;
            case "-w": int countWord = WcUtils.wordCounting(br);
                System.out.println("【单词数】："+countWord);
                break;
            case "-l": int countLine = WcUtils.lineCounting(br);
                System.out.println("【行数】："+countLine);
                break;
            case "-a": int[] data = WcUtils.complexDataCounting(br);
                System.out.println("【代码行】："+data[0]);
                System.out.println("【空行】："+data[1]);
                System.out.println("【注释行】："+data[2]);
                break;
            default:
                System.out.println("【参数错误，请输入正确的参数】");
        }
    }

    /**
     * -c:统计文件的字符数
     *
     * @param br
     * @return
     */
    public static int characterCounting(BufferedReader br) throws IOException {
        int countChar = 0;
        //用于接收每一行的内容
        String line = null;
        while ((line = br.readLine()) != null) {
            countChar += line.length();
        }
        return countChar;
    }

    /**
     * -w:统计文件中词的数目
     *
     * @param br
     * @return
     */
    public static int wordCounting(BufferedReader br) throws IOException {
        //用于接收每一行的内容
        String line = null;
        StringBuffer stringBuffer=new StringBuffer();
        while ((line = br.readLine()) != null) {
            stringBuffer.append(line);
        }
        line = stringBuffer.toString();
        line = line.replaceAll("[^a-zA-Z\\s+]", " ");
        String[] strings=line.split("[\\s+,\\.\n]");
        Multiset<String> col= HashMultiset.create();
        for(String string:strings) {
            col.add(string);
        }
        col.elementSet().remove("");
        return col.size();
    }

    /**
     * -l:统计文件的行数
     *
     * @param br
     * @return
     */
    public static int lineCounting(BufferedReader br) throws IOException {
        int countLine = 0;
        //用于接收每一行的内容
        String line = null;
        while ((line = br.readLine()) != null) {
            countLine++;
        }
        return countLine;
    }

    /**
     * -a   返回更复杂的数据（代码行 / 空行 / 注释行）
     * 0号元素为代码行，1号元素为空行，2号元素为注释行
     *
     * @param br
     * @return
     * @throws IOException
     */
    public static int[] complexDataCounting(BufferedReader br) throws IOException {
        //用于存储代码行、空行、注释行的数目
        int[] data = {0, 0, 0};
        //用于接收每一行的内容
        String line = null;
        //空白行正则表达式
        String regexContainNull = "\\s*";
        Pattern english = Pattern.compile("[a-zA-z]");
        //用于标明下一行是不是注释行
        boolean comment = false;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.matches(regexContainNull) || line.equals("{") || line.equals("}")) {
                data[1]++;
            } else if (line.startsWith("/*") && !line.endsWith("*/")) {
                data[2]++;
                comment = true;
            } else if (line.startsWith("/*") && line.endsWith("*/")) {
                data[2]++;
            } else if (true == comment) {
                data[2]++;
                if (line.endsWith("*/"))
                    comment = false;
            } else if (line.startsWith("//") || line.contains("//") ) {
                data[2]++;
                if(english.matcher(line).find() && line.contains("}")&& line.contains("//"))
                    data[0]++;
            } else {
                data[0]++;
            }
        }
        return data;
    }

    /**
     * 通配符匹配
     *
     * @param list,pattern
     * @return
     */
    public static List<String> wildcardMatching(List<String> list ,String pattern) {
        List<String> result = new ArrayList<String>();
        if (list.isEmpty()){
            return null;
        }else{
            if(pattern!=null){
                String[] character = pattern.split("\\.");
                //如果pattern只有单独一个*,遍历所有
                if(pattern.length()==1 && pattern.equals("*")){
                    return list;
                }
                //遍历文件名字匹配通配符
                for (String string :list){
                    //获取文件名
                    String[] fileNames = string.split("\\\\");
                    //如果文件名没有后缀，跳出继续遍历下一个
                    if(!fileNames[fileNames.length-1].contains("."))
                        break;
                    String fileName = fileNames[fileNames.length-1];
                    //文件名再次分割
                    String[] names = fileName.split("\\.");
                    //开始匹配通配符
                    //文件名开头含有*
                    if (character[0].startsWith("*")){
                        //如果文件名前缀只有一个*
                        if(character[0].length() == 1){
                            if(names[1].equals(character[1]))
                                result.add(string);
                        }else{
                            String[] name1 = character[0].split("\\*");
                            if(names[0].endsWith(name1[1]) && names[1].equals(character[1])){
                                result.add(string);
                            }
                        }
                    }
                    //文件名结尾含有*
                    else if(character[0].endsWith("*")){
                        String[] name1 = character[0].split("\\*");
                        if(names[0].startsWith(name1[0]) && names[1].equals(character[1])){
                            result.add(string);
                        }
                    }
                    //文件后缀名含有*的
                    else if(character[1].equals("*")){
                        if (character[0].equals(names[0]))
                            result.add(string);
                        if(character[0].contains("?") && names[0].length() == character[0].length())
                            result.add(string);
                    }
                    //文件名含有？
                    else if(character[0].contains("?")){
                        if(names[0].length() == character[0].length() && names[1].equals(character[1])){
                            result.add(string);
                        }
                    }
                }
            }else
                result = list;
        }
        return result;
    }

    /**
     * 遍历文件夹下的所有目录，包括子目录
     * @param path
     * @param list
     */
    public static void recursive(String path, List<String> list) {
        File file = new File(path);
        //如果文件存在
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File file2 : files) {
                    if(file2.isDirectory()){
                        recursive(file2.getAbsolutePath(),list);
                    }else if (file2.isFile()){
                        list.add(file2.getAbsolutePath());
                    }
                }
            }
        }
    }

    /**
     * 遍历当前目录，不包括子目录
     * @param path
     * @param list
     */
    public static void  recursiveNotContainSub(String path, List<String> list){
        File file = new File(path);
        //如果文件存在
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File file2 : files) {
                    if (file2.isFile()){
                        list.add(file2.getAbsolutePath());
                    }
                }
            }
        }
    }

    /**
     * 删除数组中的某个元素
     * @param strings
     * @return
     */
    public static String[] arrayDelete(String[] strings,String deleteString){
        List<String> list= Arrays.asList(strings);
        List<String> arrayList =new ArrayList<String>(list);//转换为ArrayLsit调用相关的remove方法
        arrayList.remove(deleteString);
        String[] result = arrayList.toArray(new String[0]);
        return result;
    }
}
