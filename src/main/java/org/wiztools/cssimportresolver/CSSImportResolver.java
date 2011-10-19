package org.wiztools.cssimportresolver;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.wiztools.commons.FileUtil;

/**
 *
 * @author subwiz
 */
class CSSImportResolver {
    
    private static final Pattern RE = Pattern.compile("@import\\s+url\\(\\s*('|\")?([^)^'^\"]*)('|\")?\\s*\\)\\s*;");
    
    static void resolve(File file, Charset charset, StringBuffer sb) throws IOException {
        final String fileContent = FileUtil.getContentAsString(file, charset);
        
        Matcher m = RE.matcher(fileContent);
        while(m.find()) {
            final String importedFileName = m.group(2);
            final File importedFile = new File(file.getParentFile(), importedFileName);
            final String importedContent = FileUtil.getContentAsString(importedFile, charset);
            m.appendReplacement(sb, importedContent);
        }
        m.appendTail(sb);
    }
}
