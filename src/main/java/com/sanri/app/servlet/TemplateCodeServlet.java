package com.sanri.app.servlet;

import com.sanri.app.BaseServlet;
import com.sanri.frame.RequestMapping;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 模板代码服务
 */
@RequestMapping("/tmpcode")
public class TemplateCodeServlet extends BaseServlet {
    private static File tempCode;
    static {
        tempCode = mkConfigPath("tempcode");
    }

    /**
     * 列出所有的模板
     * @return
     */
    public List<String> listTemplates(){
       return Arrays.asList(tempCode.list());
    }

    /**
     * 模板数据读取
     * @param baseName
     * @return
     */
    public String readTmplate(String baseName) throws IOException {
        File file = new File(tempCode,baseName);
        return FileUtils.readFileToString(file);
    }

    /***
     *  新模板
     * @param baseName
     * @param content
     * @return
     */
    public int writeTemplate(String baseName,String content) throws IOException {
        File file = new File(tempCode,baseName);
        FileUtils.writeStringToFile(file,content);
        return 0;
    }
}
