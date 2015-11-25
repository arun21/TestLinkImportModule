package com.example.general;

import java.io.File;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.example.util.ResourceHelper;

public class ExcelToXmlTest extends TestCase {

	public ExcelToXmlTest(String testName) {
		super(testName);
	}

	public void test() {
		ExcelConversion excel = new ExcelConversion();
		File input = new File(ResourceHelper.getConfig("file_path_test_source"));
		excel.generateXML(input, ResourceHelper.getConfig("file_path_test_destination"));
		assertTrue(input.exists());
		Assert.assertNotNull(excel);
	}
}
