package io.riskscanner.commons.utils;


import io.riskscanner.commons.constants.TaskConstants;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

/**
 * @author maguohao
 */
public class CommandUtils {

    /**
     * @param command
     * @param workDir 工作路径
     * @throws Exception
     */
    public static String commonExecCmdWithResult(String command, String workDir) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
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
                    stringBuilder.append(line + "\n");
                }
            } else {
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
            }
        } catch (InterruptedException e) {
            throw e;
        }
        return stringBuilder.toString();
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
                assert fwriter != null;
                fwriter.flush();
                fwriter.close();
            } catch (IOException e) {
                throw e;
            } finally {
                // empty!
            }
        }
        return dirPath;
    }

}
