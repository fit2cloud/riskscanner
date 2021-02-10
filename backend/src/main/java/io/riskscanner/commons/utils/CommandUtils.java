package io.riskscanner.commons.utils;


import io.riskscanner.commons.constants.TaskConstants;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

/**
 * @author maguohao
 * @date 2020-04-30 14:40
 */
public class CommandUtils {

    /**
     * @param command
     * @param workDir 工作路径
     * @return
     * @throws Exception
     */
    public static String commonExecCmdWithResult(String command, String workDir) throws Exception {
        StringBuffer stringBuffer = new StringBuffer();
        Process exec;
        if (StringUtils.isNotBlank(workDir)) {
            exec = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command}, null, new File(workDir));
        } else {
            exec = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command});
        }
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(exec.getErrorStream()))
        ) {
            exec.waitFor();
            String line;
            if (exec.exitValue() != 0) {
                //错误执行返回信息
                while ((line = errorReader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }
            } else {
                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }
            }
        } catch (InterruptedException e) {
            throw e;

        }
        return stringBuffer.toString();
    }

    public static String saveAsFile(String content, String id) throws Exception {
        String dirPath = TaskConstants.RESULT_FILE_PATH_PREFIX + id;
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        FileWriter fwriter = null;
        try {
            // true表示不覆盖原来的内容，而是加到文件的后面。若要覆盖原来的内容，直接省略这个参数就好
            fwriter = new FileWriter(dirPath + "/policy.yml", false);
            fwriter.write(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                return ex.getMessage();
            }
        }
        return dirPath;
    }

}
