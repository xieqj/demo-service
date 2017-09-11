package com.sinotn.demo.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Fields;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.sinotn.poi.CellValueExtractor;

public class Excel {
	@Test
	public void testAllRead() throws Exception{
		ClassPathResource cpr=new ClassPathResource("/com/sinotn/demo/poi/outline.xls");
		InputStream is=cpr.getInputStream();
		CellValueExtractor extractor=CellValueExtractor.getCellValueExtractor();
		Workbook wb=WorkbookFactory.create(is);
		Sheet sheet=wb.getSheetAt(0);
		Row row;
		Cell cell;
		int lcn;
		int lrn=sheet.getLastRowNum();
		for(int i=0;i<=lrn;i++){
			row=sheet.getRow(i);
			lcn=row.getLastCellNum();
			System.out.print(i+"、");
			for(int j=0;j<lcn;j++){
				cell=row.getCell(j);
				System.out.print(extractor.getStringValue(cell)+"|");
			}
			System.out.println();
		}

		is.close();
	}

	@Test
	public void testCompare() throws Exception{
		File file1=new File("D:\\demo\\考生统计.xls");
		HashMap<String, String> map=new HashMap<String, String>();
		CellValueExtractor extractor=CellValueExtractor.getCellValueExtractor();
		Workbook wb=WorkbookFactory.create(file1);
		Sheet sheet=wb.getSheetAt(0);
		Row row;
		String key;
		String val;
		int lrn=sheet.getLastRowNum();
		for(int i=1;i<=lrn;i++){
			row=sheet.getRow(i);
			key=extractor.getStringValue(row.getCell(0))+extractor.getStringValue(row.getCell(1));
			val=extractor.getStringValue(row.getCell(2));
			map.put(key, val);
		}

		File file2=new File("D:\\demo\\工作簿1.xls");
		wb=WorkbookFactory.create(file2);
		sheet=wb.getSheetAt(0);
		lrn=sheet.getLastRowNum();
		for(int i=0;i<=lrn;i++){
			row=sheet.getRow(i);
			key=extractor.getStringValue(row.getCell(0))+extractor.getStringValue(row.getCell(1));
			val=extractor.getStringValue(row.getCell(2));
			if(val.equals(map.get(key))==false){
				System.out.println(key+"->"+val+"->"+map.get(key));
			}
		}
	}

	@Test
	public void grade() throws Exception {
		String src="d:/成绩文件[21729](包含各题型得分).xls";
		String out="d:/成绩文件[21729](包含各题型得分)_out.xls";
		FileInputStream fis = new FileInputStream(new File(src));
		Workbook wb = new HSSFWorkbook(fis);

		CellValueExtractor extractor=CellValueExtractor.getCellValueExtractor();
		double total,a1,a2;
		Row row;
		Cell cell;
		Sheet sheet = wb.getSheetAt(0);
		int lrn = sheet.getLastRowNum();
		for (int i = lrn; i > 0; i--) {
			row=sheet.getRow(i);
			cell=row.getCell(6);
			total=Double.valueOf(extractor.getStringValue(cell));

			cell=row.getCell(4);
			a2=Double.valueOf(extractor.getStringValue(cell));
			cell=row.getCell(3);
			a1=Double.valueOf(extractor.getStringValue(cell));

			cell=row.createCell(5);
			cell.setCellValue(String.valueOf(total-a1-a2));
		}
		fis.close();

		FileOutputStream fos=new FileOutputStream(new File(out));
		wb.write(fos);
		fos.close();
	}

	@Test
	public void randomTestAccount() throws Exception {
		String xls = "D:\\sinotn\\实施\\黄金12月考试\\考点测试账号.xls";
		String xls2 = "D:\\sinotn\\实施\\黄金12月考试\\考点正式账号.xls";
		FileInputStream fis = new FileInputStream(new File(xls));
		Workbook wb = new HSSFWorkbook(fis);
		Sheet sheet = wb.getSheetAt(0);

		String prefix = "s";

		HashSet<String> set = new HashSet<String>();
		Row row;
		Cell cell;
		String uuid;
		String account;
		String passwd;
		int lrn = sheet.getLastRowNum();
		for (int i = lrn; i > 0; i--) {
			row = sheet.getRow(i);
			// cell=row.getCell(1);
			uuid = UUID.randomUUID().toString();
			account = prefix + uuid.substring(0, 7);
			while (set.contains(account)) {
				uuid = UUID.randomUUID().toString();
				account = prefix + uuid.substring(0, 7);
			}

			cell = row.getCell(1);
			if (cell == null) {
				cell = row.createCell(1);
			}
			cell.setCellValue(account);

			cell = row.getCell(2);
			if (cell == null) {
				cell = row.createCell(2);
			}
			passwd = uuid.substring(uuid.length() - 6);
			cell.setCellValue(passwd);
		}

		FileOutputStream fos = new FileOutputStream(new File(xls2));
		wb.write(fos);
		fos.close();

		fis.close();
	}

	@Test
	public void accountWord() throws Exception {
		String xls = "D:\\sinotn\\实施\\黄金12月考试\\考点账号\\考点正式账号.xls";
		File folder = new File("D:\\sinotn\\实施\\黄金12月考试\\考点账号\\word");
		FileInputStream fis = new FileInputStream(new File(xls));
		Workbook wb = new HSSFWorkbook(fis);
		Sheet sheet = wb.getSheetAt(0);

		HashMap<String, String> params;
		Row row;
		Cell cell;
		String examNodeName;
		String account;
		String passwd;
		CellValueExtractor extractor = CellValueExtractor.getCellValueExtractor();
		int lrn = sheet.getLastRowNum();
		for (int i = lrn; i > lrn - 1; i--) {
			row = sheet.getRow(i);
			cell = row.getCell(0);
			params = new HashMap<String, String>();
			examNodeName = extractor.getStringValue(cell);
			params.put("${nodeName}", examNodeName);
			cell = row.getCell(1);
			account = extractor.getStringValue(cell);
			params.put("${account}", account);
			cell = row.getCell(2);
			passwd = extractor.getStringValue(cell);
			params.put("${passwd}", passwd);

			this.mergeWord(params, new File(folder, examNodeName + ".doc"));
		}

		fis.close();
	}

	private void mergeWord(Map<String, String> params, File output) throws Exception {
		ClassPathResource cpr = new ClassPathResource("/com/sinotn/demo/poi/sge.doc");
		InputStream is = cpr.getInputStream();
		HWPFDocument hwpfDocument = new HWPFDocument(is);
		Range range = hwpfDocument.getRange();

		Fields fields=hwpfDocument.getFields();


		for (Map.Entry<String, String> entry : params.entrySet()) {
			range.replaceText(entry.getKey(), entry.getValue());
		}
		// FileOutputStream fos=new FileOutputStream(output);
		// hwpfDocument.write(fos);
		// fos.close();
		is.close();
	}

	@Test
	public void wordTemplate() throws Exception {
		ClassPathResource cpr = new ClassPathResource("/com/sinotn/demo/poi/sge3.doc");
		InputStream is = cpr.getInputStream();
		HWPFDocument hwpfDocument = new HWPFDocument(is);
		Range range = hwpfDocument.getRange();
		range.replaceText("${examNodeName}", "北京信诺软通");

		FileOutputStream fos=new FileOutputStream("d:/word_output.doc");
		hwpfDocument.write(fos);
		fos.close();
		is.close();
	}
}
