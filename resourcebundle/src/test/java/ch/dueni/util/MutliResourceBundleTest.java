package ch.dueni.util;

import org.junit.Test;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.Assert.assertEquals;

public class MutliResourceBundleTest {
	
	@Test
	public void resourcesCombined() {
		Locale.setDefault(Locale.ROOT);
		ResourceBundle bundle = ResourceBundle.getBundle("ch.dueni.util.CombinedResource");
		assertEquals("res1", bundle.getObject("res1"));
		assertEquals("res2", bundle.getObject("res2"));
		assertEquals("res3more", bundle.getObject("res3"));
		assertEquals("more1", bundle.getObject("more1"));
		assertEquals("more2", bundle.getObject("more2"));
		assertEquals("more3", bundle.getObject("more3"));
		assertEquals(6, bundle.keySet().size());
	}

	@Test
	public void resourcesCombined_de() {
		Locale.setDefault(Locale.GERMAN);
		ResourceBundle bundle = ResourceBundle.getBundle("ch.dueni.util.CombinedResource");
		assertEquals("res1(de)", bundle.getObject("res1"));
		assertEquals("res2(de)", bundle.getObject("res2"));
		assertEquals("res3more(de)", bundle.getObject("res3"));
		assertEquals("more1(de)", bundle.getObject("more1"));
		assertEquals("more2(de)", bundle.getObject("more2"));
		assertEquals("more3(de)", bundle.getObject("more3"));
		assertEquals(6, bundle.keySet().size());
	}

	@Test
	public void resourcesCombined_en() {
		Locale.setDefault(Locale.ENGLISH);
		ResourceBundle bundle = ResourceBundle.getBundle("ch.dueni.util.CombinedResource");
		assertEquals("res1(en)", bundle.getObject("res1"));
		assertEquals("res2(en)", bundle.getObject("res2"));
		assertEquals("res3more(en)", bundle.getObject("res3"));
		assertEquals("more1(en)", bundle.getObject("more1"));
		assertEquals("more2(en)", bundle.getObject("more2"));
		assertEquals("more3(en)", bundle.getObject("more3"));
		assertEquals(6, bundle.keySet().size());
	}

    @Test
    public void resourcesCombinedWithoutArguments() {
        Locale.setDefault(Locale.ROOT);
        ResourceBundle bundle = ResourceBundle.getBundle("ch.dueni.util.CombinedResourceWithoutArguments");
        assertEquals("res1", bundle.getObject("res1"));
        assertEquals("res2", bundle.getObject("res2"));
        assertEquals("res3more", bundle.getObject("res3"));
        assertEquals("more1", bundle.getObject("more1"));
        assertEquals("more2", bundle.getObject("more2"));
        assertEquals("more3", bundle.getObject("more3"));
        assertEquals(6, bundle.keySet().size());
    }

    @Test
    public void resourcesCombinedWithoutArguments_de() {
        Locale.setDefault(Locale.GERMAN);
        ResourceBundle bundle = ResourceBundle.getBundle("ch.dueni.util.CombinedResourceWithoutArguments");
        assertEquals("res1(de)", bundle.getObject("res1"));
        assertEquals("res2(de)", bundle.getObject("res2"));
        assertEquals("res3more(de)", bundle.getObject("res3"));
        assertEquals("more1(de)", bundle.getObject("more1"));
        assertEquals("more2(de)", bundle.getObject("more2"));
        assertEquals("more3(de)", bundle.getObject("more3"));
        assertEquals(6, bundle.keySet().size());
    }

    @Test
    public void resourcesCombinedWithoutArguments_en() {
        Locale.setDefault(Locale.ENGLISH);
        ResourceBundle bundle = ResourceBundle.getBundle("ch.dueni.util.CombinedResourceWithoutArguments");
        assertEquals("res1(en)", bundle.getObject("res1"));
        assertEquals("res2(en)", bundle.getObject("res2"));
        assertEquals("res3more(en)", bundle.getObject("res3"));
        assertEquals("more1(en)", bundle.getObject("more1"));
        assertEquals("more2(en)", bundle.getObject("more2"));
        assertEquals("more3(en)", bundle.getObject("more3"));
        assertEquals(6, bundle.keySet().size());
    }

}
