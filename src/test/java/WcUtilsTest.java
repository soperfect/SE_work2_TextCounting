import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * WcUtils Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>���� 17, 2020</pre>
 */
public class WcUtilsTest extends TestCase {


    /**
     * Method: characterCounting(BufferedReader br)
     */
    @Test
    public void testCharacterCounting() throws Exception {
        String path = "D:/myapp/test/14.txt";
        String path1 = "D:/myapp/test/20.txt";
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(path1)));
        int countChar = WcUtils.characterCounting(br);
        int countChar1 = WcUtils.characterCounting(br1);
        Assert.assertEquals(7,countChar);
        Assert.assertEquals(40,countChar1);
    }

    /**
     * Method: wordCounting(BufferedReader br)
     */
    @Test
    public void testWordCounting() throws Exception {
        String path = "D:/myapp/test/20.txt";
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        int countWord = WcUtils.wordCounting(br);
        Assert.assertEquals(6,countWord);
    }

    /**
     * Method: lineCounting(BufferedReader br)
     */
    @Test
    public void testLineCounting() throws Exception {
        String path = "D:/myapp/test/abcdef.txt";
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        int countLine = WcUtils.lineCounting(br);
        Assert.assertEquals(6,countLine);
    }

    /**
     * Method: complexDataCounting(BufferedReader br)
     */
    @Test
    public void testComplexDataCounting() throws Exception {
        String path = "D:/myapp/test/1.java";
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        int[] data = WcUtils.complexDataCounting(br);
        Assert.assertEquals(21,data[0]);
        Assert.assertEquals(14,data[1]);
        Assert.assertEquals(7,data[2]);
    }

    /**
     * Method: recursiveProcessing(String path, String pattern)
     */
    @Test
    public void testRecursiveProcessing() throws Exception {
        String path = "D:/myapp/test";
        String pattern1 = "*.txt";
        String pattern2 = "*ab.txt";
        String pattern3 = "ab*.txt";
        String pattern4 = "*";
        String pattern5 = "??.txt";
        String pattern6 = "?.txt";
        String pattern7 = "??.*";
        List<String> pList1 = Arrays.asList("D:\\myapp\\test\\11ab.txt","D:\\myapp\\test\\14.txt",
                "D:\\myapp\\test\\20.txt","D:\\myapp\\test\\22ab.txt","D:\\myapp\\test\\3.txt",
                "D:\\myapp\\test\\abcdef.txt","D:\\myapp\\test\\w\\1.txt","D:\\myapp\\test\\w\\2.txt",
                "D:\\myapp\\test\\w\\3.txt","D:\\myapp\\test\\w\\ab88.txt");
        List<String> pList2 = Arrays.asList("D:\\myapp\\test\\11ab.txt","D:\\myapp\\test\\22ab.txt");
        List<String> pList3 = Arrays.asList("D:\\myapp\\test\\abcdef.txt","D:\\myapp\\test\\w\\ab88.txt");
        List<String> pList4 = new ArrayList<>();
        pList4.add("D:\\myapp\\test\\1.java");
        pList4.addAll(pList1);
        pList4.add("D:\\myapp\\test\\wc.exe");
        List<String> pList5 = Arrays.asList("D:\\myapp\\test\\14.txt","D:\\myapp\\test\\20.txt");
        List<String> pList6 = Arrays.asList("D:\\myapp\\test\\3.txt","D:\\myapp\\test\\w\\1.txt",
                "D:\\myapp\\test\\w\\2.txt","D:\\myapp\\test\\w\\3.txt");
        List<String> pList7 = Arrays.asList("D:\\myapp\\test\\14.txt","D:\\myapp\\test\\20.txt","D:\\myapp\\test\\wc.exe");
        List<String> list = new ArrayList<>();
        WcUtils.recursive(path,list);
        List<String> list1 = WcUtils.wildcardMatching(list,pattern1);
        List<String> list2 = WcUtils.wildcardMatching(list,pattern2);
        List<String> list3 = WcUtils.wildcardMatching(list,pattern3);
        List<String> list4 = WcUtils.wildcardMatching(list,pattern4);
        List<String> list5 = WcUtils.wildcardMatching(list,pattern5);
        List<String> list6 = WcUtils.wildcardMatching(list,pattern6);
        List<String> list7 = WcUtils.wildcardMatching(list,pattern7);
        Assert.assertEquals(pList1,list1);
        Assert.assertEquals(pList2,list2);
        Assert.assertEquals(pList3,list3);
        Assert.assertEquals(pList4,list4);
        Assert.assertEquals(pList5,list5);
        Assert.assertEquals(pList6,list6);
        Assert.assertEquals(pList7,list7);
    }

    @Test
    public void testArrayDelete(){
        String[] a = {"-c","-w","-l","-a","-s","-x"};
        String[] b = WcUtils.arrayDelete(a,"-s");
        String[] expect = {"-c","-w","-l","-a","-x"};
        Assert.assertEquals(expect,b);
    }
} 
