package org.wiztools.cssimportresolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.Collections;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.wiztools.commons.Charsets;
import org.wiztools.commons.FileUtil;
import static org.junit.Assert.*;

/**
 *
 * @author subwiz
 */
public class CSSImportResolverTest {
    
    public CSSImportResolverTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of resolve method, of class CSSImportResolver.
     */
    @Test
    public void testResolve() throws Exception {
        System.out.println("resolve");
        File file = new File("src/test/resources/main.css");
        Charset charset = Charsets.UTF_8;
        CSSImportResolver resolver = new CSSImportResolver(charset, true, Collections.EMPTY_LIST);
        resolver.resolve(file);

        String expectedResult = FileUtil.getContentAsString(
                new File("src/test/resources/expOutput.css"), Charsets.UTF_8);
        System.out.println(resolver);
        assertEquals(expectedResult, resolver.toString());
    }
    
    @Test
    public void testForgiving() throws Exception {
        System.out.println("forgiving");
        File file = new File("src/test/resources/forgiving.css");
        Charset charset = Charsets.UTF_8;
        CSSImportResolver resolver = new CSSImportResolver(charset, true, Collections.EMPTY_LIST);
        resolver.resolve(file);

        String expectedResult = FileUtil.getContentAsString(
                new File("src/test/resources/expOutputForgiving.css"), Charsets.UTF_8);
        System.out.println(resolver);
        assertEquals(expectedResult, resolver.toString());
    }
    
    @Test
    public void testNotForgiving() throws Exception {
        System.out.println("notForgiving");
        File file = new File("src/test/resources/forgiving.css");
        Charset charset = Charsets.UTF_8;
        try {
            CSSImportResolver resolver = new CSSImportResolver(charset, false, Collections.EMPTY_LIST);
            resolver.resolve(file);
            fail("Forgiving is false. Should not come here!");
        }
        catch(FileNotFoundException ex) {
            // should not come here!
        }
    }
    
    @Test
    public void testRecursive() throws Exception {
        System.out.println("recursive");
        File file = new File("src/test/resources/recursive1.css");
        Charset charset = Charsets.UTF_8;
        CSSImportResolver resolver = new CSSImportResolver(charset, true, Collections.EMPTY_LIST);
        resolver.resolve(file);

        String expectedResult = FileUtil.getContentAsString(
                new File("src/test/resources/expOutputRecursive.css"), Charsets.UTF_8);
        System.out.println(resolver);
        assertEquals(expectedResult, resolver.toString());
    }
}
