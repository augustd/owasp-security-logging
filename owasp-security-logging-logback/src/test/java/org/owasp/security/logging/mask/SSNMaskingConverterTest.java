package org.owasp.security.logging.mask;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SSNMaskingConverterTest extends BaseConverterTest {

    @Override
    String getConverterClass() {
        return SSNMaskingConverter.class.getName();
    }
    
    @Test
    public void testMask() {
        String layoutMessage = log("This message contains SSN 123-45-6789");
        System.out.println("testMask() layoutMessage: " + layoutMessage);
        assertFalse(layoutMessage.contains("123-45-6789"));
        assertTrue(layoutMessage.contains("***-**-6789"));
    }
    
    @Test
    public void testMaskWithParameter() {
        String layoutMessage = log("This message contains SSN {}", "123-45-6789");
        System.out.println("testMaskWithParameter() layoutMessage: " + layoutMessage);
        assertFalse(layoutMessage.contains("123-45-6789"));
        assertTrue(layoutMessage.contains("***-**-6789"));
    }
    
    @Test
    public void testMaskWithMultiParameter() {
        String layoutMessage = log("For user {} SSN is {}", "Joe Roberts", "123-45-6789");
        System.out.println("testMaskWithMultiParameter() layoutMessage: " + layoutMessage);
        assertFalse(layoutMessage.contains("123-45-6789"));
        assertTrue(layoutMessage.contains("Joe Roberts"));
        assertTrue(layoutMessage.contains("***-**-6789"));
    }
    
    @Test
    public void testNonMask() {
        String layoutMessage = log("This message contains non-SSN 078-05-1120");
        System.out.println("testNonMask() layoutMessage: " + layoutMessage);
        assertFalse(layoutMessage.contains("***-**-1120"));
        assertTrue(layoutMessage.contains("078-05-1120"));
    }
    
    @Test
    public void testNonMaskWithParameter() {
        String layoutMessage = log("This message contains non-SSN {}", "078-05-1120");
        System.out.println("testNonMaskWithParameter() layoutMessage: " + layoutMessage);
        assertFalse(layoutMessage.contains("***-**-1120"));
        assertTrue(layoutMessage.contains("078-05-1120"));
    }

}
