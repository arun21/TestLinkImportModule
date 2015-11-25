package com.example.general;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.example.util.Constants;
import com.example.util.ResourceHelper;

public class ExcelConversion {

	public void generateXML(File excelFile, String destination) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();
			InputStream fis = new FileInputStream(excelFile);

			Element rootElement = document.createElement(Constants.TEST_SUITE);
			rootElement.setAttribute("name", "DECLIC");

			Element subElement = document.createElement(Constants.NODE_ORDER);
			CDATASection cdata2 = document.createCDATASection("");
			subElement.appendChild(cdata2);
			rootElement.appendChild(subElement);

			Element subElement1 = document.createElement(Constants.DETAILS);
			CDATASection cdata1 = document.createCDATASection("");
			subElement1.appendChild(cdata1);

			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet spreadsheet = workbook.getSheetAt(0);
			XSSFRow row = spreadsheet.getRow(0);

			String[] key = new String[Constants.COLUMN_INDEX];
			for (int i = 0; i < row.getLastCellNum(); i++) {
				key[i] = row.getCell(i).getStringCellValue();
			}
			List<String> strTestCaseNames = new ArrayList<String>();
			int stepNumbers = 0;
			Element testCaseColumn = document.createElement(key[0]);
			Element keywords = document.createElement(Constants.KEYWORDS);
			Element steps = document.createElement(Constants.STEPS);
			for (int j = 1; j <= spreadsheet.getLastRowNum(); j++) {
				XSSFRow rowNum = spreadsheet.getRow(j);
				if (!isRowEmpty(rowNum)) {
					String rowValue = (rowNum.getCell(0) != null) ? rowNum.getCell(0).getStringCellValue() : strTestCaseNames.get(0);
					Element notes = document.createElement(Constants.NOTES);
					Element step = document.createElement(Constants.STEP);
					Element step_number = document.createElement(Constants.STEP_NUMBER);
					int endIndex;
					int startIndex;
					if (j == 1) {
						testCaseColumn.setAttribute("name", rowValue);
						stepNumbers = 1;
						strTestCaseNames.add(rowValue);
					} else {
						if (!strTestCaseNames.contains(rowValue)) {
							// strTestCaseNames = new ArrayList<String>();
							stepNumbers = 1;
							strTestCaseNames.add(rowValue);
							testCaseColumn = document.createElement(key[0]);
							testCaseColumn.setAttribute("name", rowValue);
							keywords = document.createElement(Constants.KEYWORDS);
							steps = document.createElement(Constants.STEPS);
						} else {
							stepNumbers++;
						}
					}
					CDATASection cdata17 = document.createCDATASection(String.valueOf(stepNumbers));
					step_number.appendChild(cdata17);
					step.appendChild(step_number);
					for (int k = 1; k < row.getLastCellNum(); k++) {
						if (!isCellEmpty(rowNum.getCell(k))) {
							Element excelColumn = document.createElement(key[k]);
							if (excelColumn.getTagName().equals(Constants.ACTIONS) || excelColumn.getTagName().equals(Constants.EXPECTED_RESULTS)) {
								String valueToSet = rowNum.getCell(k).getStringCellValue().replaceAll("\n", "<br/>");
								CDATASection cdata = document.createCDATASection("<p>" + valueToSet.trim() + "</p>");
								excelColumn.appendChild(cdata);
								step.appendChild(excelColumn);
								steps.appendChild(step);
								testCaseColumn.appendChild(steps);
							} else if (excelColumn.getTagName().equals(Constants.KEYWORD)) {
								if (rowNum.getCell(k).getStringCellValue().contains(",")) {
									startIndex = rowNum.getCell(k).getStringCellValue().indexOf(',');
									String keyword1 = rowNum.getCell(k).getStringCellValue().substring(0, startIndex);
									endIndex = rowNum.getCell(k).getStringCellValue().length();
									String keyword2 = rowNum.getCell(k).getStringCellValue().substring(startIndex + 1, endIndex);
									excelColumn.setAttribute("name", keyword1 + " " + keyword2);
								} else {
									String keywordNameAttributeValue = rowNum.getCell(k).getStringCellValue().substring(0, 1).toUpperCase()
									        + rowNum.getCell(k).getStringCellValue().substring(1).toLowerCase();
									excelColumn.setAttribute("name", keywordNameAttributeValue.trim());
								}
								excelColumn.appendChild(notes);
								keywords.appendChild(excelColumn);
								testCaseColumn.appendChild(keywords);
							} else {
								CDATASection cdata = document.createCDATASection("<p>" + rowNum.getCell(k).getStringCellValue().trim() + "</p>");
								excelColumn.appendChild(cdata);
								testCaseColumn.appendChild(excelColumn);
							}
						}
					}
					rootElement.appendChild(testCaseColumn);
				}
			}
			document.appendChild(rootElement);
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(destination));
			transformer.transform(source, result);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	private boolean isCellEmpty(XSSFCell cell) {
		if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
			return false;
		}
		return true;
	}

	private boolean isRowEmpty(XSSFRow row) {
		for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
			Cell cell = row.getCell(c);
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK)
				return false;
		}
		return true;
	}

	public static void main(String[] args) {
		ExcelConversion excel = new ExcelConversion();
		File input = new File(ResourceHelper.getConfig("file_path_source"));
		excel.generateXML(input, ResourceHelper.getConfig("file_path_destination"));
	}
}
