import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main
{
	public static void main(String[] args) throws IOException
	{
        File f = new File("/storage/emulated/0/ADM Pro+.txt");//图片地址(输出，不用管)
        Writer w = new FileWriter(f,true);
		File out = new File("/sdcard/tgweb.txt");//网站地址
        FileInputStream in = new FileInputStream(out);
        InputStreamReader inReader = new InputStreamReader(in);
        BufferedReader bufReader = new BufferedReader(inReader);
        String line = null;
        int i = 1;
        while((line = bufReader.readLine()) != null){
            System.out.println("链接："+line);
           // System.out.println();
            String zz = "(src=\"/file/)[\\w]{21}+(.jpg)|(src=\"/file/)[\\w]{21}+(.png)|(src=\"/file/)[\\w]{21}+(.jpeg)";
            String zz1 = "(https://www.mymypic.net/data/attachment/forum/)[\\d]{6}+(/)[\\d]{2}+(/)[\\w]{10,30}+(.jpg.thumb.jpg)|(https://www.mymypic.net/data/attachment/forum/)[\\d]{6}+(/)[\\d]{2}+(/)[\\w]{10,30}+(.jpeg.thumb.jpeg)|(https://www.mymypic.net/data/attachment/forum/)[\\d]{6}+(/)[\\d]{2}+(/)[\\w]{10,30}+(.jpeg)|(https://www.mymypic.net/data/attachment/forum/)[\\d]{6}+(/)[\\d]{2}+(/)[\\w]{10,30}+(.jpg)";
            String con = doGet(line);
            Pattern patt = Pattern.compile(zz);
            Matcher matt = patt.matcher(con);
            System.out.println("开始爬...");
            while(matt.find()){
                System.out.println("https://telegra.ph/"+matt.group().substring(matt.group().indexOf("\"")+1,matt.group().lastIndexOf("g")+1));
                w.write("https://telegra.ph/"+matt.group().substring(matt.group().indexOf("\"")+1,matt.group().lastIndexOf("g")+1)+"\r\n");
            }
            Pattern patt1 = Pattern.compile(zz1);
            Matcher matt1 = patt1.matcher(con);
            //System.out.println("开始爬...");
            while(matt1.find()){
                System.out.println(matt1.group());
                w.write(matt1.group()+"\r\n");
            }
            i++;
        }
        bufReader.close();
        inReader.close();
        in.close();
		System.out.println("完成！");
        w.close();
       /* System.out.print("请输入tg图片预览的地址：");
        Scanner sc = new Scanner(System.in);*/
		
        
	}
    
    public static String doGet(String httpurl) {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(httpurl);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：25000毫秒
            connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36");
            connection.setConnectTimeout(60000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 封装输入流is，并指定字符集
                br = new BufferedReader(new InputStreamReader(is, "GB2312"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            connection.disconnect();// 关闭远程连接
        }

        return result;
    }
}
