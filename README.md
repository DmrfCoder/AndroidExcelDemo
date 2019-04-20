##  需求描述

将应用内的数据导出为excel表格。

##  实现

###  添加依赖包

在app的build.gradle里面添加依赖包:    

```java
implementation group: 'net.sourceforge.jexcelapi', name: 'jxl', version: '2.6.12'
```

有很多读者提到该依赖有问题，笔者推测是网络代理的问题，遇到这种问题大家可以到[这个地址](https://mvnrepository.com/artifact/net.sourceforge.jexcelapi/jxl/2.6.12)下载对应的jar包手动导入即可：

![屏幕快照 2019-02-14 18.44.42](https://ws1.sinaimg.cn/large/006tKfTcly1g065imu5buj31ld0nhwl4.jpg)

如果还是不能成功笔者已经写好了jar包放在了[这个地址](https://github.com/DmrfCoder/AndroidExcelDemo/blob/master/app/libs/jxl-2.6.12.jar)，大家可以自由下载，下载到jar包之后将其放在app/libs目录下，然后右键jar包文件，然后`Add as Library`即可。

###  编写excel工具类

```java
package cn.xiaojii.cashgift.util.io;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * @author dmrfcoder
 * @date 2018/8/9
 */
public class ExcelUtil {
    private static WritableFont arial14font = null;

    private static WritableCellFormat arial14format = null;
    private static WritableFont arial10font = null;
    private static WritableCellFormat arial10format = null;
    private static WritableFont arial12font = null;
    private static WritableCellFormat arial12format = null;
    private final static String UTF8_ENCODING = "UTF-8";
   
   /**
     * 单元格的格式设置 字体大小 颜色 对齐方式、背景颜色等...
     */
    private static void format() {
        try {
            arial14font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
            arial14font.setColour(jxl.format.Colour.LIGHT_BLUE);
            arial14format = new WritableCellFormat(arial14font);
            arial14format.setAlignment(jxl.format.Alignment.CENTRE);
            arial14format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            arial14format.setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW);

            arial10font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            arial10format = new WritableCellFormat(arial10font);
            arial10format.setAlignment(jxl.format.Alignment.CENTRE);
            arial10format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            arial10format.setBackground(Colour.GRAY_25);

            arial12font = new WritableFont(WritableFont.ARIAL, 10);
            arial12format = new WritableCellFormat(arial12font);
            //对齐格式
            arial10format.setAlignment(jxl.format.Alignment.CENTRE);
            //设置边框
            arial12format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);

        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化Excel
     *
     * @param fileName 导出excel存放的地址（目录）
     * @param colName excel中包含的列名（可以有多个）
     */
    public static void initExcel(String fileName, String[] colName) {
        format();
        WritableWorkbook workbook = null;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            workbook = Workbook.createWorkbook(file);
           //设置表格的名字
            WritableSheet sheet = workbook.createSheet("账单", 0);
            //创建标题栏
            sheet.addCell((WritableCell) new Label(0, 0, fileName, arial14format));
            for (int col = 0; col < colName.length; col++) {
                sheet.addCell(new Label(col, 0, colName[col], arial10format));
            }
            //设置行高
            sheet.setRowView(0, 340);
            workbook.write();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> void writeObjListToExcel(List<T> objList, String fileName, Context c) {
     if (objList != null && objList.size() > 0) {
            WritableWorkbook writebook = null;
            InputStream in = null;
            try {
                WorkbookSettings setEncode = new WorkbookSettings();
                setEncode.setEncoding(UTF8_ENCODING);
                in = new FileInputStream(new File(fileName));
                Workbook workbook = Workbook.getWorkbook(in);
                writebook = Workbook.createWorkbook(new File(fileName), workbook);
                WritableSheet sheet = writebook.getSheet(0);

                for (int j = 0; j < objList.size(); j++) {
                    ProjectBean projectBean = (ProjectBean) objList.get(j);
                    List<String> list = new ArrayList<>();
                    list.add(projectBean.getName());
                    list.add(projectBean.getProject());
                    list.add(projectBean.getMoney());
                    list.add(projectBean.getYear() + " " + projectBean.getMonth()+" "+projectBean.getDay());
                    list.add(projectBean.getBeizhu());

                    for (int i = 0; i < list.size(); i++) {
                        sheet.addCell(new Label(i, j + 1, list.get(i), arial12format));
                        if (list.get(i).length() <= 4) {
                            //设置列宽
                            sheet.setColumnView(i, list.get(i).length() + 8);
                        } else {
                            //设置列宽
                            sheet.setColumnView(i, list.get(i).length() + 5);
                        }
                    }
                    //设置行高
                    sheet.setRowView(j + 1, 350);
                }

                writebook.write();
                Toast.makeText(c, "导出Excel成功", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (writebook != null) {
                    try {
                        writebook.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
}
```


###  使用


​    
```java
String filePath = Environment.getExternalStorageDirectory() + "/AndroidExcelDemo";
File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }


        String excelFileName = "/demo.xls";


        String[] title = {"姓名", "年龄", "男孩"};
        String sheetName = "demoSheetName";


        List<DemoBean> demoBeanList = new ArrayList<>();
        DemoBean demoBean1 = new DemoBean("张三", 10, true);
        DemoBean demoBean2 = new DemoBean("小红", 12, false);
        DemoBean demoBean3 = new DemoBean("李四", 18, true);
        DemoBean demoBean4 = new DemoBean("王香", 13, false);
        demoBeanList.add(demoBean1);
        demoBeanList.add(demoBean2);
        demoBeanList.add(demoBean3);
        demoBeanList.add(demoBean4);
        filePath = filePath + excelFileName;


        ExcelUtil.initExcel(filePath, sheetName, title);


        ExcelUtil.writeObjListToExcel(demoBeanList, filePath, context);

        textView.setText("excel已导出至：" + filePath);
```

最终生成的excel效果如下：

![image-20190216174313758](https://ws3.sinaimg.cn/large/006tKfTcly1g08empfrhrj30u01o0tby.jpg)

注意一定要处理权限申请的相关工作。