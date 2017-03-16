package ut.mr.jrkr;

import org.junit.Test;
import mr.jrkr.api.MyPluginComponent;
import mr.jrkr.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}