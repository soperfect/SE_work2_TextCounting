import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * @author Red Date.
 * @date 2020/3/15 15:10
 */
public class WcView {

    public static void view(){
        // 创建一个顶层容器（窗口）
        final JFrame jf = new JFrame("wc.exe");          // 创建窗口
        jf.setSize(800, 350);                       // 设置窗口大小
        jf.setLocationRelativeTo(null);             // 把窗口位置设置到屏幕中心
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // 当点击窗口的关闭按钮时退出程序（没有这一句，程序不会退出）
        // 创建面板容器，使用默认的布局管理器
        JPanel panel01 = new JPanel(new BorderLayout());
        JPanel panel02 = new JPanel();
        JButton openBtn = new JButton("选择文件");
        openBtn.setBounds(200,400,100,40);
        final Vector rowData = new Vector();
        // 表头（列名）
        final Vector columnNames = new Vector();
        columnNames.add("文件名");
        columnNames.add("字符数");
        columnNames.add("单词数");
        columnNames.add("行数");
        columnNames.add("代码行");
        columnNames.add("空行");
        columnNames.add("注释行");

        final JTable table = new JTable(rowData,columnNames);
        // 设置滚动面板视口大小（超过该大小的行数据，需要拖动滚动条才能看到）
        table.setPreferredScrollableViewportSize(new Dimension(800, 320));
        table.setFont(new Font("宋体",Font.PLAIN,16));
        // 获取表头
        JTableHeader jTableHeader = table.getTableHeader();
        // 设置表头名称字体样式
        jTableHeader.setFont(new Font("宋体",Font.PLAIN,16));
        // 先获取到某列
        TableColumn tableColumn = table.getColumnModel().getColumn(0);
        tableColumn.setMinWidth(350);


        // 把 表格 放到 滚动面板 中（表头将自动添加到滚动面板顶部）
        JScrollPane scrollPane = new JScrollPane(table);
        // 添加到内容面板
        panel01.add(scrollPane);
        panel02.add(openBtn);
        // 创建一个垂直盒子容器, 把上面 3 个 JPanel 串起来作为内容面板添加到窗口
        Box vBox = Box.createVerticalBox();
        vBox.add(panel01);
        vBox.add(panel02);
        jf.setLocationRelativeTo(null);
        // 把 面板容器 作为窗口的内容面板 设置到 窗口
        jf.setContentPane(vBox);
        // 显示窗口，前面创建的信息都在内存中，通过 jf.setVisible(true) 把内存中的窗口显示在屏幕上。
        jf.setVisible(true);

        openBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 创建一个默认的文件选取器
                JFileChooser fileChooser = new JFileChooser();
                // 设置文件选择的模式（只选文件、只选文件夹、文件和文件均可选）
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                // 设置是否允许多选
                fileChooser.setMultiSelectionEnabled(false);
                // 打开文件选择框（线程将被阻塞, 直到选择框被关闭）
                int result = fileChooser.showOpenDialog(jf);
                if (result == JFileChooser.APPROVE_OPTION) {

                    // 如果点击了"确定", 则获取选择的文件路径
                    File file = fileChooser.getSelectedFile();
                    ArrayList<Object[]> arrayList = new ArrayList<>();
                    Object[] data = new Object[7];
                    if(file.isFile()){
                        try {

                            data = getData(file);
                            arrayList.add(data);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }else{
                        File[] files = file.listFiles();
                        for (File file1 :files){
                            if (file1.isFile()){
                                try {
                                    data = getData(file1);
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                arrayList.add(data);
                            }
                        }
                    }
                    // 表格所有行数据
                    Object[][] rowDatas  = (Object [][])arrayList.toArray(new Object[0][0]);
                    for(int rowIndex=0;rowIndex<rowDatas.length;rowIndex++){
                        Vector line = new Vector();
                        for(int columnIndex = 0;columnIndex<rowDatas[rowIndex].length;columnIndex++){
                            line.add(rowDatas[rowIndex][columnIndex]);
                        }
                        rowData.add(line);
                    }
                    DefaultTableModel model = new DefaultTableModel(rowData, columnNames);
                    table.setModel(model);
                    // 先获取到某列
                    TableColumn tableColumn = table.getColumnModel().getColumn(0);
                    tableColumn.setMinWidth(350);
                }
            }
        });
    }


    private static Object[] getData(File file) throws IOException {
        int countChar = 0;          //字符数
        int countWord = 0;          //词数
        int countLine = 0;          //行数
        int codeLine = 0;           //代码行
        int nullLine = 0;           //空行
        int annotationLine = 0;     //注释行
        BufferedReader br = new BufferedReader(new FileReader(file));
        countChar = WcUtils.characterCounting(br);
        countWord = WcUtils.wordCounting(br);
        countLine = WcUtils.lineCounting(br);
        int[] data = new int[0];
        data = WcUtils.complexDataCounting(br);
        codeLine = data[0];
        nullLine = data[1];
        annotationLine = data[2];
        Object[] result = {file.getAbsolutePath(),countChar, countWord, countLine, codeLine, nullLine, annotationLine};
        return result;
    }
}
