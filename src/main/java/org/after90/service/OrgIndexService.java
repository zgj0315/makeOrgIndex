package org.after90.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * Created by zhaogj on 28/12/2016.
 */
@Service
@Slf4j
public class OrgIndexService {
    public void makeOrgIndex() {
        FileWriter writer = null;
        BufferedWriter out = null;
        try {
            writer = new FileWriter("./index.org");
            out = new BufferedWriter(writer);
            out.write("#+TITLE: After90");
            out.newLine();
            out.write("#+AUTHOR: 赵光建");
            out.newLine();
            out.write("#+EMAIL: zgj0315@gmail.com");
            out.newLine();
            for (File filePath : (new File("./")).listFiles()) {
                if (filePath.isDirectory() && !".git".equals(filePath.getName())) {
                    String strPathName = filePath.getName();
                    out.write("* " + strPathName);
                    out.newLine();
                    for (File fileOrg : filePath.listFiles()) {
                        if (fileOrg.isFile()) {
                            if (fileOrg.getName().startsWith("summaryAndPlan")) {
                                // 跳过工作计划
                                continue;
                            }
                            if (fileOrg.getName().endsWith("org")) {
                                FileReader reader = null;
                                BufferedReader in = null;
                                try {
                                    reader = new FileReader(fileOrg);
                                    in = new BufferedReader(reader);
                                    String strLine = null;
                                    while ((strLine = in.readLine()) != null) {
                                        if (strLine.startsWith("#+TITLE:")) {
                                            String strTitle = strLine.replaceAll("\\#\\+TITLE:", "").trim();
                                            String strFileHtmlName = fileOrg.getName().substring(0,
                                                    fileOrg.getName().length() - 3) + "html";
                                            out.write("- [[./" + strPathName + "/" + strFileHtmlName + "][" + strTitle
                                                    + "]]");
                                            out.newLine();
                                        }
                                    }
                                } catch (Exception e) {
                                    log.error("BufferedReader or FileReader err", e);
                                } finally {
                                    if (in != null) {
                                        try {
                                            in.close();
                                        } catch (Exception e) {
                                            log.error("", e);
                                        }
                                    }
                                    if (reader != null) {
                                        try {
                                            reader.close();
                                        } catch (Exception e) {
                                            log.error("", e);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            out.flush();
            writer.flush();
        } catch (Exception e) {
            log.error("BufferedWriter or FileWriter err", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    log.error("", e);
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    log.error("", e);
                }
            }
        }
    }
}
