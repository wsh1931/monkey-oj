import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author: wusihao
 * @date: 2025/1/4 11:49
 * @version: 1.0
 * @description: 读取服务器文件（文件泄露）
 */
public class Main {
    public static void main(String[] args) throws Exception{
        String userDir = System.getProperty("user.dir");
        String filePath=userDir+ File.separator+"/src/main/resources/application.yml";
        List<String> allLines = Files.readAllLines(Paths.get(filePath));
        System.out.println(String.join("\n",allLines));
    }
}
