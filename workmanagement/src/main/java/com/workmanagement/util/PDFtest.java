package com.workmanagement.util;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class PDFtest {
	//使用资源字体(ClassPath) 宋体
	private static String basePath;
	
	public static void main(String[] args) throws Exception {
		
		Map<String, String> info = new HashMap<String, String>();
		
		//logo
		info.put("logo", "F://title.jpg");
		//报告编号
		info.put("reportNumber", "000001");
//		查询时间
		info.put("searchDate", "2016-10-10 22:22:22");
//		查询人
		info.put("searchMan", "hansen");
//		****信用信息服务中心
		info.put("infoServiceCenter", "中国银行");
//		服务热线
		info.put("servicePhone", "18380426135");
		info.put("info1", "11");
		info.put("info2", "11");
		info.put("info3", "11");
		info.put("info4", "11");
//		身份信息 企业名称
		info.put("info41", "11");
//		code
		info.put("info42", "12");
//		注册地址
		info.put("info43", "13");
//		行业分类
		info.put("info44", "2016年10月10日");
//		实缴资本
		info.put("info45", "2016年10月11日");
//		数据来源
		info.put("info46", "13");
//		采集时间
		info.put("info47", "13");
//		末尾数据
		info.put("info48", "13");
		/*
    	 * 信息主体于20**年首次与金融机构发生信贷关系，报告期内，
    	 * 共在**家金融机构办理过信贷业务，截止查询日在**家金融机构**万元贷款业务仍未结清
    	 */
		info.put("info49", "1");
		info.put("info50", "2");
		info.put("info51", "3");
		info.put("info52", "4");
		
		String[][] d1 = new String[][]{  //出资情况
            {"出资方名称","证件类型","证件号码","币种","出资金额","出资方式","出资占比","数据来源","采集时间"},  
            {"1","test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司","18905710571","浙江省杭州市西湖区三墩镇三墩街188号"}
		}; 
		String[][] d2 = new String[][]{  //高管信息
            {"高管类型","姓名","证件类型","证件号码","数据来源","采集时间"},  
            {"2","test","31","软件工程师","浙江杭州","adfa"}
		}; 
		String[][] d3 = new String[][]{  //关联信息
			{"序号","关联企业名称","组织机构代码（统一社会信用代码）","关联类型"},  
			{"1","test","31","软件工程师"}
		}; 
		String[][] d4 = new String[][]{  //报数机构说明
			{"报数机构说明"}
		}; 
		String[][] d5 = new String[][]{  //服务中心说明
			{"服务中心说明"}
		}; 
		String[][] d6 = new String[][]{  //信息主体说明
			{"信息主体说明"}
		}; 
		String[][] d7 = new String[][]{  //查询记录
			{"序号","查询日期","查询用户","查询原因"},  
			{"1","test","31","软件工程师"}
		}; 
		String[][] d8 = new String[][]{  //银行贷款
	    	{"未\r\r结\r\r清","test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司","18905710571"},
	    	{"test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司","18905710571"},
	    	{"test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司","18905710571"}
	    }; 
	    String[][] d9 = new String[][]{  //银行贷款
	    	{"已\r\r结\r\r清","test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司","18905710571"},
	    	{"test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司","18905710571"}
	    }; 
		//表格数据
		List<String[][]> data = new ArrayList<String[][]>();
		data.add(d1);//出资情况
		data.add(d2);//高管信息
		data.add(d3);//关联信息
		data.add(d4);//报数机构说明
		data.add(d5);//服务中心说明
		data.add(d6);//信息主体说明
		data.add(d7);//查询记录
		data.add(d8);//银行贷款 未结清
		data.add(d9);//银行贷款，已结清
//		createPdfForGovernment(info, data);
	}





	public static String createPdfForGovernment(Map<String, String> info, List<String[][]> datas,HttpServletRequest request) throws Exception {
		String[] path = DataUtil.createTempPath(".pdf");//"F://test555.pdf";//
    	String outPath = path[0] + path[1];
//    	String outPath = "F://test213125.pdf";
        //设置纸张
        Rectangle rect = new Rectangle(PageSize.A4);
    	
        //创建文档实例
        Document doc=new Document(rect);
        
        //添加中文字体
        BaseFont bfChinese=BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        //使用资源字体(ClassPath) 
        basePath = request.getSession().getServletContext().getRealPath("/")+"assets" + File.separator + "font"+File.separator+"simsun.ttf";
        BaseFont baseFont = BaseFont.createFont(basePath,BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
        //设置字体样式
        Font textFont = new Font(baseFont,11,Font.NORMAL); //正常
        Font redTextFont = new Font(baseFont,11,Font.NORMAL,Color.RED); //正常,红色
        Font boldFont = new Font(baseFont,11,Font.NORMAL); 
        Font boldFont1 = new Font(baseFont,11,Font.BOLD); //加粗
        Font redBoldFont = new Font(baseFont,11,Font.BOLD,Color.RED); //加粗,红色
        Font firsetTitleFont = new Font(baseFont,22,Font.BOLD); //一级标题
        Font secondTitleFont = new Font(baseFont,15,Font.BOLD); //二级标题
        Font underlineFont = new Font(baseFont,11,Font.UNDERLINE); //下划线斜体
        String  imagePath= request.getSession().getServletContext().getRealPath("/")+"assets" + File.separator + "images"+File.separator+"hand.png";
        //手指图片
//        String  imagePath = "F://hand.png";
        Image hand = Image.getInstance(imagePath);
        
        //创建输出流
        PdfWriter.getInstance(doc, new FileOutputStream(new File(outPath)));
        
        doc.open();
        doc.newPage();
        
        //段落  
        Paragraph p1 = new Paragraph();  
        //短语
        Phrase ph1 = new Phrase();  
        Image logoImage = null;
        try{
        	logoImage = Image.getInstance(info.get("logo"));
        }catch(Exception e){
        	LoggerUtil.error(e);
        	String logoPath = request.getSession().getServletContext().getRealPath("/")+"assets" + File.separator + "images"+File.separator+"reportDefaultLogo.jpg";
        	logoImage = Image.getInstance(logoPath);
        }
//        Image logoImage = Image.getInstance(info.get("logo"));
        logoImage.scaleAbsolute(30f, 30f);
        //块
        Chunk c1 = new Chunk(logoImage, 0, 0) ;
        Chunk c11 = new Chunk("（信用报告提供机构logo）", textFont) ;
        //将块添加到短语
        ph1.add(c1);
        ph1.add(c11);
        //将短语添加到段落
        p1.add(ph1);
        //将段落添加到短语
        doc.add(p1);
        
        p1 = new Paragraph();  
        ph1 = new Phrase(); 
        Chunk c2 = new Chunk("报告编号：", boldFont1) ;
        Chunk c22 = new Chunk(info.get("reportNumber"), textFont) ;
        ph1.add(c2);
        ph1.add(c22);
        p1.add(ph1);
        doc.add(p1);
        
        p1 = new Paragraph("企业信用报告", firsetTitleFont);
        p1.setLeading(50);
        p1.setAlignment(Element.ALIGN_CENTER);
        doc.add(p1);

        p1 = new Paragraph("（政府版）", textFont);
        p1.setLeading(20);
        p1.setAlignment(Element.ALIGN_CENTER);
        doc.add(p1);
            
        p1 = new Paragraph();  
        p1.setLeading(20);
        p1.setAlignment(Element.ALIGN_CENTER);
        ph1 = new Phrase(); 
        Chunk c3 = new Chunk("查询时间：", boldFont1) ;
        Chunk c33 = new Chunk(info.get("searchDate"), textFont) ;
        Chunk c4 = new Chunk(leftPad("查询人：", 10), boldFont1) ;
        Chunk c44 = new Chunk(info.get("searchMan")+"（用户登录名）", textFont) ;
        ph1.add(c3);
        ph1.add(c33);
        ph1.add(c4);
        ph1.add(c44);
        p1.add(ph1);
        doc.add(p1);
            
        p1 = new Paragraph("报告说明", secondTitleFont);
        p1.setLeading(50);
        p1.setAlignment(Element.ALIGN_CENTER);
        doc.add(p1);
        
        p1 = new Paragraph(" ");  
        p1.setLeading(30);
        doc.add(p1);
        
        p1 = new Paragraph();  
        ph1 = new Phrase(); 
        Chunk c5 = new Chunk("1.本报告由", textFont) ;
        Chunk c6 = new Chunk(info.get("infoServiceCenter")+"信用信息服务中心", underlineFont) ;
        c6.setSkew(0, 30);
        Chunk c7 = new Chunk(" 出具，依据截止报告时间小微企业信用信息数据库记录的信息生成。除异议标注和查询记录外，报告中的信息均由相关报数机构和信息主体提供，", textFont);
        Chunk c8 = new Chunk("不保证其真实性和准确性，但承诺在信息整合、汇总、展示的全过程中保持客观、中立的地位。", textFont) ;
        ph1.add(c5);
        ph1.add(c6);
        ph1.add(c7);
        ph1.add(c6);
        ph1.add(c8);
        p1.add(ph1);
        doc.add(p1);

        p1 = new Paragraph();  
        ph1 = new Phrase(); 
        Chunk c9 = new Chunk("2.异议标注是", textFont) ;
        Chunk c10 = new Chunk(" 对报告中的信息记录或对信息主体所作的说明。", textFont);
        ph1.add(c9);
        ph1.add(c6);
        ph1.add(c10);
        p1.add(ph1);
        doc.add(p1);
            
        p1 = new Paragraph("3.信息主体说明是信息主体对报数机构提供的信息记录所作的简要说明。", textFont);  
        doc.add(p1);
        
        p1 = new Paragraph();  
        ph1 = new Phrase(); 
        Chunk c12 = new Chunk("4.信息主体有权对本报告中的内容提出异议。如有异议，可联系报数机构，也可到", textFont) ;
        Chunk c13 = new Chunk(" 提出异议申请。", textFont);
        ph1.add(c12);
        ph1.add(c6);
        ph1.add(c13);
        p1.add(ph1);
        doc.add(p1);
        
        p1 = new Paragraph("5.更多咨询，请致电客户服务热线 "+info.get("servicePhone")+"。", textFont);  
        doc.add(p1);
 //******************************身份信息************************************
        
        p1 = new Paragraph();  
        p1.setSpacingAfter(10);
        ph1 = new Phrase(); 
        Chunk c14 = new Chunk(hand, 0, 0);
        Chunk c15 = new Chunk(leftPad("身份信息", 7), boldFont1);
        ph1.add(c14);
        ph1.add(c15);
        p1.add(ph1);
        doc.add(p1);
        
        // 创建一个有4列的表格  
        PdfPTable table = new PdfPTable(4);
        table.setTotalWidth(new float[]{ 105, 170, 105, 170 }); //设置列宽
        table.setLockedWidth(true); //锁定列宽
        
        PdfPCell cell;
        cell = new PdfPCell(new Phrase("企业名称", boldFont));
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthTop(2);
        cell.setMinimumHeight(30); //设置单元格高度
        cell.setUseAscender(true); //设置可以居中
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(info.get("info41"), textFont));
        cell.setBorderWidthRight(2);
        cell.setBorderWidthTop(2);
        cell.setUseAscender(true); //设置可以居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        cell.setColspan(3);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("统一社会信用代码（组织机构代码）", boldFont));
        cell.setBorderWidthLeft(2);
        cell.setMinimumHeight(40); //设置单元格高度
        cell.setUseAscender(true); //设置可以居中
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(info.get("info42"), textFont));
        cell.setUseAscender(true); //设置可以居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("注册地址", boldFont));
        cell.setUseAscender(true); //设置可以居中
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(info.get("info43"), textFont));
        cell.setBorderWidthRight(2);
        cell.setUseAscender(true); //设置可以居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("行业分类", boldFont));
        cell.setBorderWidthLeft(2);
        cell.setMinimumHeight(30); //设置单元格高度
        cell.setUseAscender(true); //设置可以居中
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(info.get("info44"), textFont));
        cell.setUseAscender(true); //设置可以居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("实缴资本", boldFont));
        cell.setUseAscender(true); //设置可以居中
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(info.get("info45"), textFont));
        cell.setBorderWidthRight(2);
        cell.setUseAscender(true); //设置可以居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("数据来源", boldFont));
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthBottom(2);
        cell.setMinimumHeight(30); //设置单元格高度
        cell.setUseAscender(true); //设置可以居中
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(info.get("info46"), textFont));
        cell.setBorderWidthBottom(2);
        cell.setUseAscender(true); //设置可以居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("采集时间", boldFont));
        cell.setBorderWidthBottom(2);
        cell.setUseAscender(true); //设置可以居中
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(info.get("info47"), textFont));
        cell.setBorderWidthRight(2);
        cell.setBorderWidthBottom(2);
        cell.setUseAscender(true); //设置可以居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        table.addCell(cell);
        doc.add(table);
        
        //******************************出资情况************************************
        
        p1 = new Paragraph();  
        p1.setSpacingBefore(20);
        p1.setSpacingAfter(10);
        ph1 = new Phrase(); 
        Chunk c17 = new Chunk(hand, 0, 0);
        Chunk c18 = new Chunk(leftPad("出资情况", 7), boldFont1);
        ph1.add(c17);
        ph1.add(c18);
        p1.add(ph1);
        doc.add(p1);
        
        table = new PdfPTable(9);
        table.setTotalWidth(new float[]{80, 60, 60, 50, 60, 60, 60, 60, 60}); //设置列宽
        table.setLockedWidth(true); //锁定列宽
        
        PDFUtil.setData(doc, table, datas.get(0), baseFont);
//******************************高管信息************************************
        
    	p1 = new Paragraph();  
        p1.setSpacingBefore(20);
        p1.setSpacingAfter(10);
        ph1 = new Phrase(); 
        Chunk c20 = new Chunk(hand, 0, 0);
        Chunk c21 = new Chunk(leftPad("高管信息", 7), boldFont1);
        ph1.add(c20);
        ph1.add(c21);
        p1.add(ph1);
        doc.add(p1);
        
        table = new PdfPTable(6);
        table.setTotalWidth(new float[]{90, 90, 90, 120, 80, 80}); //设置列宽
        table.setLockedWidth(true); //锁定列宽
        
        PDFUtil.setData(doc, table, datas.get(1), baseFont);
		
//******************************关联信息************************************
    	
    	p1 = new Paragraph();  
        p1.setSpacingBefore(20);
        p1.setSpacingAfter(10);
        ph1 = new Phrase(); 
        Chunk c30 = new Chunk(hand, 0, 0);
        Chunk c31 = new Chunk(leftPad("关联信息", 7), boldFont1);
        ph1.add(c30);
        ph1.add(c31);
        p1.add(ph1);
        doc.add(p1);
        
        table = new PdfPTable(4);
        table.setTotalWidth(new float[]{40, 250, 160, 100}); //设置列宽
        table.setLockedWidth(true); //锁定列宽
    	
    	PDFUtil.setData(doc, table, datas.get(2), baseFont);
    	
    	//******************************银行贷款************************************	
    	
    	 //段落  
    	p1 = new Paragraph();  
        p1.setSpacingBefore(20);
        p1.setSpacingAfter(10);
        ph1 = new Phrase(); 
        Chunk c40 = new Chunk(hand, 0, 0);
        Chunk c41 = new Chunk(leftPad("银行贷款", 7), boldFont1);
        ph1.add(c40);
        ph1.add(c41);
        p1.add(ph1);
        doc.add(p1);
        /*
    	 * 信息主体于20**年首次与金融机构发生信贷关系，报告期内，
    	 * 共在**家金融机构办理过信贷业务，截止查询日在**家金融机构**万元贷款业务仍未结清
    	 */
        p1 = new Paragraph("信息主体于"+info.get("info1")+"首次与金融机构发生信贷关系，报告期内，共在"+info.get("info2")+"家金融机构办理过信贷业务，截止查询日在"+info.get("info3")+"家金融机构"+info.get("info4")+"万元贷款业务仍未结清", textFont);  
        p1.setFirstLineIndent(23);
        doc.add(p1);
        p1 = new Paragraph(" ", textFont);  
        p1.setFirstLineIndent(23);
        doc.add(p1);
		// 创建一个有8列的表格  
		table = new PdfPTable(7);
		table.setTotalWidth(new float[]{120, 75, 70, 70, 70, 70, 70 }); //设置列宽
		table.setLockedWidth(true); //锁定列宽
		
		//start 表头信息
//		PdfPCell cell;
        cell = new PdfPCell(new Phrase("", boldFont));
        cell.setBorderWidthLeft(3);
        cell.setBorderWidthTop(3);
        cell.setMinimumHeight(30); //设置单元格高度
        cell.setUseAscender(true); //设置可以居中
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        cell.setRowspan(2);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("正常类", boldFont));
        cell.setBorderWidthTop(3);
        cell.setUseAscender(true); //设置可以居中
        cell.setMinimumHeight(30); //设置单元格高度
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
        cell.setColspan(2);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("关注类", boldFont));
        cell.setBorderWidthTop(3);
        cell.setUseAscender(true); //设置可以居中
        cell.setMinimumHeight(30); //设置单元格高度
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
        cell.setColspan(2);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("不良类", boldFont));
        cell.setBorderWidthTop(3);
        cell.setBorderWidthRight(3);
        cell.setBorderWidthTop(3);
        cell.setUseAscender(true); //设置可以居中
        cell.setMinimumHeight(30); //设置单元格高度
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
        cell.setColspan(2);
        table.addCell(cell);
   
        cell = new PdfPCell(new Phrase("笔数", boldFont));
        cell.setMinimumHeight(30); //设置单元格高度
        cell.setUseAscender(true); //设置可以居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("余额", boldFont));
        cell.setMinimumHeight(30); //设置单元格高度
        cell.setUseAscender(true); //设置可以居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("笔数", boldFont));
        cell.setMinimumHeight(30); //设置单元格高度
        cell.setUseAscender(true); //设置可以居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("余额", boldFont));
        cell.setMinimumHeight(30); //设置单元格高度
        cell.setUseAscender(true); //设置可以居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("笔数", boldFont));
        cell.setMinimumHeight(30); //设置单元格高度
        cell.setUseAscender(true); //设置可以居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("余额", boldFont));
        cell.setBorderWidthRight(3);
        cell.setMinimumHeight(30); //设置单元格高度
        cell.setUseAscender(true); //设置可以居中
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(" ", boldFont));
        cell.setBorderWidthLeft(3);
        cell.setBorderWidthTop(3);
        cell.setMinimumHeight(30); //设置单元格高度
        cell.setUseAscender(true); //设置可以居中
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
	    table.addCell(cell);
	    doc.add(table);
//	end
	    
	   
	    //未结清
        table = new PdfPTable(8);
        table.setTotalWidth(new float[]{40, 80, 75, 70, 70, 70, 70, 70}); //设置列宽
        table.setLockedWidth(true); //锁定列宽
        
        String[][] data = datas.get(6);
        for(int i = 0; i < data.length; i++){
    		for(int j = 0; j < data[i].length; j++){
    			
				cell = new PdfPCell(new Phrase(data[i][j], textFont));
    			
    			cell.setMinimumHeight(30); //设置单元格高度
    			cell.setUseAscender(true); //设置可以居中
    			cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
    			cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
    			
    			//设置表头的边框
    			if(i==0 && j==0){
    				cell.setBorderWidthLeft(3);
    				cell.setBorderWidthBottom(3);
    			}
    			if(i==0 && j==(data[i].length-1)){
    				cell.setBorderWidthRight(3);
    			}
    			
    			//设置表体的边框
    			if(i==(data.length-1)){
    				cell.setBorderWidthBottom(3);
    			}
    			if(i>0 && j==(data[1].length-1)){
    				cell.setBorderWidthRight(3);
    			}	
    			
    			if(i==0 && j==0){
    				cell.setRowspan(data[i].length+1);
    			}
    			
    			table.addCell(cell);
    		}
    	}
       
       doc.add(table);
       
       //已结清
       table = new PdfPTable(8);
       table.setTotalWidth(new float[]{40, 80, 75, 70, 70, 70, 70, 70}); //设置列宽
       table.setLockedWidth(true); //锁定列宽
       
       
       data = datas.get(7);
       for(int i = 0; i < data.length; i++){
   		for(int j = 0; j < data[i].length; j++){
   			
				cell = new PdfPCell(new Phrase(data[i][j], textFont));
   			
   			cell.setMinimumHeight(30); //设置单元格高度
   			cell.setUseAscender(true); //设置可以居中
   			cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
   			cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
   			
   			//设置表头的边框
   			if(i==0 && j==0){
   				cell.setBorderWidthLeft(3);
   				cell.setBorderWidthBottom(3);
   			}
   			if(i==0 && j==(data[i].length-1)){
   				cell.setBorderWidthRight(3);
   			}
   			
   			//设置表体的边框
   			if(i==(data.length-1)){
   				cell.setBorderWidthBottom(3);
   			}
   			if(i>0 && j==(data[1].length-1)){
   				cell.setBorderWidthRight(3);
   			}	
   			
   			if(i==0 && j==0){
   				cell.setRowspan(data[i].length);
   			}
   			
   			table.addCell(cell);
   		}
   	}
      
        doc.add(table);
        p1 = new Paragraph("说明：正常类指债权银行内部五级分类为“正常”的债务；", textFont);  
        doc.add(p1);
        p1 = new Paragraph("关注类指债权银行内部五级分类为“关注”的债务；", textFont);  
        doc.add(p1);
        p1 = new Paragraph("不良类指债权银行内部五级分类为“次级”、“可疑”、“损失”的债务。", textFont);  
        doc.add(p1);
        //******************************报数机构说明************************************
   	 
   	 p1 = new Paragraph();  
        p1.setSpacingBefore(20);
        p1.setSpacingAfter(10);
        ph1 = new Phrase(); 
        Chunk c80 = new Chunk(hand, 0, 0);
        Chunk c81 = new Chunk(leftPad("报数机构说明", 9), boldFont1);
        ph1.add(c80);
        ph1.add(c81);
        p1.add(ph1);
        doc.add(p1);
        
        table = new PdfPTable(1);
        table.setTotalWidth(new float[]{500}); //设置列宽
        table.setLockedWidth(true); //锁定列宽
   	 
   	 PDFUtil.setData2(doc, table, datas.get(3), baseFont);

   	 //******************************服务中心说明************************************
   	 
   	 p1 = new Paragraph();  
        p1.setSpacingBefore(20);
        p1.setSpacingAfter(10);
        ph1 = new Phrase(); 
        Chunk c83 = new Chunk(hand, 0, 0);
        Chunk c84 = new Chunk(leftPad("服务中心说明", 9), boldFont1);
        ph1.add(c83);
        ph1.add(c84);
        p1.add(ph1);
        doc.add(p1);
        
        table = new PdfPTable(1);
        table.setTotalWidth(new float[]{500}); //设置列宽
        table.setLockedWidth(true); //锁定列宽
   	 
   	 PDFUtil.setData2(doc, table, datas.get(4), baseFont);

   	 //******************************信息主体说明************************************
   	 
   	 p1 = new Paragraph();  
        p1.setSpacingBefore(20);
        p1.setSpacingAfter(10);
        ph1 = new Phrase(); 
        Chunk c85 = new Chunk(hand, 0, 0);
        Chunk c86 = new Chunk(leftPad("信息主体说明", 9), boldFont1);
        ph1.add(c85);
        ph1.add(c86);
        p1.add(ph1);
        doc.add(p1);
        
        table = new PdfPTable(1);
        table.setTotalWidth(new float[]{500}); //设置列宽
        table.setLockedWidth(true); //锁定列宽
   	 
   	 PDFUtil.setData2(doc, table, datas.get(5), baseFont);
   	 
   	 //******************************查询记录************************************
   	 
   	 p1 = new Paragraph();  
        p1.setSpacingBefore(20);
        p1.setSpacingAfter(10);
        ph1 = new Phrase(); 
        Chunk c87 = new Chunk(hand, 0, 0);
        Chunk c88 = new Chunk(leftPad("查询记录", 7), boldFont1);
        ph1.add(c87);
        ph1.add(c88);
        p1.add(ph1);
        doc.add(p1);
        
        table = new PdfPTable(4);
        table.setTotalWidth(new float[]{130, 130, 130, 130}); //设置列宽
        table.setLockedWidth(true); //锁定列宽
   	 
   	 PDFUtil.setData(doc, table, datas.get(8), baseFont);
   	 
   	 
       
       p1 = new Paragraph();  
       p1.setSpacingBefore(20);
       p1.setSpacingAfter(10);
       ph1 = new Phrase(); 
       Chunk c89 = new Chunk("异议标注（用于记录信息主体对某类信息提出异议处理情况，置于每类信息项下方）：", redBoldFont);
       Chunk c90 = new Chunk("信息主体于"+info.get("info48")+"提出异议：我公司从未发生过"+info.get("info49")+"；业务发生机构于"+info.get("info50")+"提交说明：该笔信息确实存在；信息主体于"+info.get("info51")+"提出声明：该笔信息为我公司"+info.get("info52")+"所致。（在每一板块下详细记录信息主体提出异议处理过程和结果。）", redTextFont);
       ph1.add(c89);
       ph1.add(c90);
       p1.add(ph1);
       doc.add(p1);
       doc.close();
       return path[1];
	}
	
	 /**
     * 设置左边距
     * @param str
     * @param i
     * @return
     */
    public static String leftPad(String str, int i) {
        int addSpaceNo = i-str.length();
        String space = ""; 
        for (int k=0; k<addSpaceNo; k++){
                space= " "+space;
        };
        String result =space + str ;
        return result;
     }

    
    public static void test() throws DocumentException, IOException{
    	//start 输出路径
//    	String[] path = DataUtil.createTempPath(".pdf");//"F://test555.pdf";//
//    	String outPath = path[0] + path[1];
		String outPath = "F://test213125.pdf";
        //设置纸张
        Rectangle rect = new Rectangle(PageSize.A4);
    	
        //创建文档实例
        Document doc=new Document(rect);
        
        //添加中文字体
        BaseFont baseFont=BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        
        //设置字体样式
        Font textFont = new Font(baseFont,11,Font.NORMAL); //正常
        Font redTextFont = new Font(baseFont,11,Font.NORMAL,Color.RED); //正常,红色
        Font boldFont = new Font(baseFont,11,Font.BOLD); //加粗
        Font redBoldFont = new Font(baseFont,11,Font.BOLD,Color.RED); //加粗,红色
        Font firsetTitleFont = new Font(baseFont,22,Font.BOLD); //一级标题
        Font secondTitleFont = new Font(baseFont,15,Font.BOLD); //二级标题
        Font underlineFont = new Font(baseFont,11,Font.UNDERLINE); //下划线斜体
//        String  imagePath= request.getSession().getServletContext().getRealPath("/")+"assets" + File.separator + "images"+File.separator+"hand.png";
////        手指图片
////        String  imagePath = "F://hand.pdf";
//        Image hand = Image.getInstance(imagePath);
        
        //创建输出流
        PdfWriter.getInstance(doc, new FileOutputStream(new File(outPath)));
        
        doc.open();
        doc.newPage();
        //end
        //段落  
        Paragraph p1 = new Paragraph();  
        //短语
        Phrase ph1 = new Phrase();  
        p1 = new Paragraph();  
        ph1 = new Phrase(); 
        Chunk c9 = new Chunk("2.异议标注是", textFont) ;
        Chunk c10 = new Chunk(" 对报告中的信息记录或对信息主体所作的说明。", textFont);
        ph1.add(c9);
        ph1.add(c10);
        p1.add(ph1);
        doc.add(p1);
		// 创建一个有8列的表格  
		PdfPTable table = new PdfPTable(7);
		table.setTotalWidth(new float[]{120, 75, 70, 70, 70, 70, 70 }); //设置列宽
		table.setLockedWidth(true); //锁定列宽
		
		//start 表头信息
		PdfPCell cell;
        cell = new PdfPCell(new Phrase("", boldFont));
        cell.setBorderWidthLeft(3);
        cell.setBorderWidthTop(3);
        cell.setMinimumHeight(30); //设置单元格高度
        cell.setUseAscender(true); //设置可以居中
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        cell.setRowspan(2);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("正常类", boldFont));
        cell.setBorderWidthTop(3);
        cell.setUseAscender(true); //设置可以居中
        cell.setMinimumHeight(30); //设置单元格高度
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
        cell.setColspan(2);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("关注类", boldFont));
        cell.setBorderWidthTop(3);
        cell.setUseAscender(true); //设置可以居中
        cell.setMinimumHeight(30); //设置单元格高度
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
        cell.setColspan(2);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("不良类", boldFont));
        cell.setBorderWidthTop(3);
        cell.setBorderWidthRight(3);
        cell.setBorderWidthTop(3);
        cell.setUseAscender(true); //设置可以居中
        cell.setMinimumHeight(30); //设置单元格高度
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
        cell.setColspan(2);
        table.addCell(cell);
   
        cell = new PdfPCell(new Phrase("笔数", boldFont));
        cell.setMinimumHeight(30); //设置单元格高度
        cell.setUseAscender(true); //设置可以居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("余额", boldFont));
        cell.setMinimumHeight(30); //设置单元格高度
        cell.setUseAscender(true); //设置可以居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("笔数", boldFont));
        cell.setMinimumHeight(30); //设置单元格高度
        cell.setUseAscender(true); //设置可以居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("余额", boldFont));
        cell.setMinimumHeight(30); //设置单元格高度
        cell.setUseAscender(true); //设置可以居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("笔数", boldFont));
        cell.setMinimumHeight(30); //设置单元格高度
        cell.setUseAscender(true); //设置可以居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("余额", boldFont));
        cell.setBorderWidthRight(3);
        cell.setMinimumHeight(30); //设置单元格高度
        cell.setUseAscender(true); //设置可以居中
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(" ", boldFont));
        cell.setBorderWidthLeft(3);
        cell.setBorderWidthTop(3);
        cell.setMinimumHeight(30); //设置单元格高度
        cell.setUseAscender(true); //设置可以居中
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
//	    cell.setColspan(2);
	    table.addCell(cell);
	    String[][] str = new String[0][];
        PDFUtil.setData(doc, table, str, baseFont);
//	end
	    
	    String[][] d21 = new String[][]{  //银行贷款
	    	{"未\r\r结\r\r清","test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司","18905710571"},
	    	{"test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司","18905710571"},
	    	{"test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司","18905710571"}
	    }; 
	    String[][] d22 = new String[][]{  //银行贷款
	    	{"已\r\r结\r\r清","test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司","18905710571"},
	    	{"test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司","18905710571"}
	    }; 
	    //未结清
        table = new PdfPTable(8);
        table.setTotalWidth(new float[]{40, 80, 75, 70, 70, 70, 70, 70}); //设置列宽
        table.setLockedWidth(true); //锁定列宽
        
        String[][] data = d21;
        for(int i = 0; i < data.length; i++){
    		for(int j = 0; j < data[i].length; j++){
    			
				cell = new PdfPCell(new Phrase(data[i][j], textFont));
    			
    			cell.setMinimumHeight(30); //设置单元格高度
    			cell.setUseAscender(true); //设置可以居中
    			cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
    			cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
    			
    			//设置表头的边框
    			if(i==0 && j==0){
    				cell.setBorderWidthLeft(3);
    				cell.setBorderWidthBottom(3);
    			}
    			if(i==0 && j==(data[i].length-1)){
    				cell.setBorderWidthRight(3);
    			}
    			
    			//设置表体的边框
    			if(i==(data.length-1)){
    				cell.setBorderWidthBottom(3);
    			}
    			if(i>0 && j==(data[1].length-1)){
    				cell.setBorderWidthRight(3);
    			}	
    			
    			if(i==0 && j==0){
    				cell.setRowspan(data[i].length+1);
    			}
    			
    			table.addCell(cell);
    		}
    	}
       
//       doc.add(table);
       PDFUtil.setData(doc, table, d21, baseFont);
       
       //已结清
       table = new PdfPTable(8);
       table.setTotalWidth(new float[]{40, 80, 75, 70, 70, 70, 70, 70}); //设置列宽
       table.setLockedWidth(true); //锁定列宽
       
       
       data = d22;
       for(int i = 0; i < data.length; i++){
   		for(int j = 0; j < data[i].length; j++){
   			
				cell = new PdfPCell(new Phrase(data[i][j], textFont));
   			
   			cell.setMinimumHeight(30); //设置单元格高度
   			cell.setUseAscender(true); //设置可以居中
   			cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
   			cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
   			
   			//设置表头的边框
   			if(i==0 && j==0){
   				cell.setBorderWidthLeft(3);
   				cell.setBorderWidthBottom(3);
   			}
   			if(i==0 && j==(data[i].length-1)){
   				cell.setBorderWidthRight(3);
   			}
   			
   			//设置表体的边框
   			if(i==(data.length-1)){
   				cell.setBorderWidthBottom(3);
   			}
   			if(i>0 && j==(data[1].length-1)){
   				cell.setBorderWidthRight(3);
   			}	
   			
   			if(i==0 && j==0){
   				cell.setRowspan(data[i].length);
   			}
   			
   			table.addCell(cell);
   		}
   	}
      
//        doc.add(table);
        PDFUtil.setData(doc, table, d22, baseFont);
        doc.close();
    }
}
