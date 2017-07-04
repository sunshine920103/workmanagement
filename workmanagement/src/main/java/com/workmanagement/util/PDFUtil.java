package com.workmanagement.util;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
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
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;


public class PDFUtil {
	//使用资源字体(ClassPath) 宋体
   private static String basePath;
	
	public static void main(String[] args) throws Exception {
		
		String[][] d1 = new String[][]{  //出资情况
            {"出资方名称","证件类型","证件号码","币种","出资金额","出资方式","出资占比","数据来源","采集时间"},  
            {"1","test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司","18905710571","浙江省杭州市西湖区三墩镇三墩街188号"}
		}; 
		String[][] d2 = new String[][]{  //高管信息
            {"高管类型","姓名","证件类型","证件号码","数据来源","采集时间"},  
            {"2","test","31","软件工程师","浙江杭州","adfa"}
		}; 
		String[][] d3 = new String[][]{  //对外投资情况
			{"序号","对外投资企业名称","对外投资类型","对外投资金额","对外投资形式","数据来源","采集时间"},  
			{"3","test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司"}
		}; 
		String[][] d4 = new String[][]{  //对外担保情况
			{"序号","被担保人名称","被担保人证件类型","被担保人证件号码","对外担保金额","对外担保形式","解除担保日期","数据来源","采集时间"},  
            {"1","test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司","18905710571","浙江省杭州市西湖区三墩镇三墩街188号"}
		}; 
		String[][] d5 = new String[][]{  //关联信息
			{"序号","关联企业名称","组织机构代码（统一社会信用代码）","关联类型"},  
			{"1","test","31","软件工程师"}
		}; 
		String[][] d6 = new String[][]{  //抵质押信息
			{"序号","抵质押权人","抵质押物名称","抵质押物数量","抵质押物价值","抵质押期限","抵质押物权属","数据来源","采集时间"},  
			{"1","test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司","18905710571","浙江省杭州市西湖区三墩镇三墩街188号"}
		}; 
		String[][] d7 = new String[][]{  //财务信息
			{"数据时间","应收账款","流动资产","资产合计","流动负债","负债合计","主营业务收入","利润总额","数据来源","采集时间"},  
			{"1","test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司","18905710571","浙江省杭州市西湖区三墩镇三墩街188号","2000-11-22 12:33"}
		}; 
		String[][] d8 = new String[][]{  //专利信息
			{"序号","专利号","专利名称","授予专利权日期","专利有效期","数据来源","采集时间"},  
			{"3","test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司"}
		}; 
		String[][] d9 = new String[][]{  //进出口情况
			{"数据时间","本年进口商品总价","本年出口商品总价","外汇收入","外汇支出","外汇收支逾期未申报","外汇收支漏申报","外汇收支误申报","贸易外汇收支分类管理等级","数据来源","采集时间"},  
			{"1","100","test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司","18905710571","浙江省杭州市西湖区三墩镇三墩街188号","2000-11-22 12:33"}
		}; 
		String[][] d10 = new String[][]{  //缴税信息
			{"数据时间","税别","累计缴纳金额","欠税总金额","减税总金额","免税总金额","缓税总金额","缓缴日期","退税总金额","纳税信用等级","数据来源","采集时间"},
			{"1","100","test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司","18905710571","浙江省杭州市西湖区三墩镇三墩街188号","浙江","2000-11-22 12:33"}		
		}; 
		String[][] d11 = new String[][]{  //缴纳社会保险信息
			{"险种","初缴时间","最近一次汇缴时间","缴存人数","累计缴存总额","征收机构","数据来源","采集时间"},  
			{"1","test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司","18905710571"}
		}; 
		String[][] d12 = new String[][]{  //缴纳公积金信息
			{"险种","初缴时间","最近一次汇缴时间","缴存人数","累计缴存总额","征收机构","数据来源","采集时间"},  
			{"1","test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司","18905710571"}
		}; 
		String[][] d13 = new String[][]{  //公用事业缴费信息
			{"费用类别","表号","本月使用总量","欠费总金额","补交欠费金额","数据来源","采集时间"},  
			{"3","test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司"}
		};
		String[][] d14 = new String[][]{  //政府评价
			{"评价文件号","评价机构","评价内容","评价等级","评价时间","评价有效期","是否警示","数据来源","采集时间"},  
			{"1","test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司","18905710571","浙江省杭州市西湖区三墩镇三墩街188号"}
		}; 
		String[][] d15 = new String[][]{  //社会评价
			{"评价机构","评价年度","评价等级","评价有效期"},  
			{"1","test","31","软件工程师"}
		}; 
		String[][] d16 = new String[][]{  //行政处罚信息
			{"处罚文书号","裁定处罚部门","处罚时间","违法或违规行为描述","涉及金额","处罚金额","处罚决定","整改情况","数据来源","采集时间"},  
			{"1","test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司","18905710571","浙江省杭州市西湖区三墩镇三墩街188号","2000-11-22 12:33"}
		}; 
		String[][] d17 = new String[][]{  //司法信息
			{"案号","立案法院","立案日期","案由","执行标的","执行标的金额","已执行标的"},  
			{"3","test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司"}
		};
		String[][] d18 = new String[][]{  //司法信息
			{"已执行标的金额","执行标的日期","案件状态","结案日期","执行结案方式","数据来源","采集时间"},  
			{"3","test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司"},
			{"3","test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司"},
			{"3","test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司"}
		};
		String[][] d19 = new String[][]{  //银行授信
			{"编号","授信金融机构","授信额度","起始日期","终止日期","银行内部评级结果","数据来源","采集时间"},  
			{"1","test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司","18905710571"}
		}; 
		String[][] d20 = new String[][]{  //银行贷款
			{" ","编号","贷款金融机构","贷款余额","贷款日期","到期日","五级分类","欠息金额","数据来源","采集时间"}
		}; 
		String[][] d21 = new String[][]{  //银行贷款
			{"未\r\r结\r\r清","test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司","18905710571","浙江省杭州市西湖区三墩镇三墩街188号","2000-11-22 12:33"},
			{"test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司","18905710571","浙江省杭州市西湖区三墩镇三墩街188号","2000-11-22 12:33"}
		}; 
		String[][] d22 = new String[][]{  //银行贷款
			{"已\r\r结\r\r清","test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司","18905710571","浙江省杭州市西湖区三墩镇三墩街188号","2000-11-22 12:33"},
			{"test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司","18905710571","浙江省杭州市西湖区三墩镇三墩街188号","2000-11-22 12:33"}
		}; 
		String[][] d23 = new String[][]{  //贴现
			{"编号","贴现金融机构","贴现金额","贴现日","贴现率","五级分类","数据来源","采集时间"},
			{"1","test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司","18905710571"}
		}; 
		String[][] d24 = new String[][]{  //商业汇票
			{"编号","金融机构","出票人","承兑人","票面金额","承兑到期日","数据来源","采集时间"},
			{"1","test","31","软件工程师","浙江杭州","adfa","浙江水果大王信息技术有限公司","18905710571"}
		}; 
		String[][] d25 = new String[][]{  //报数机构说明
			{"报数机构说明"}
		}; 
		String[][] d26 = new String[][]{  //服务中心说明
			{"服务中心说明"}
		}; 
		String[][] d27 = new String[][]{  //信息主体说明
			{"信息主体说明"}
		}; 
		String[][] d28 = new String[][]{  //查询记录
			{"序号","查询日期","查询用户","查询原因"},  
			{"1","test","31","软件工程师"}
		}; 
		
		
		
		
		
		
		
		
		
		
		
		
		//表格数据
		List<String[][]> data = new ArrayList<String[][]>();
		data.add(d1);//出资情况
		data.add(d2);//高管信息
		data.add(d3);//对外投资情况
		data.add(d4);//对外担保情况
		data.add(d5);//关联信息
		data.add(d6);//抵质押信息
		data.add(d7);//财务信息
		data.add(d8);//专利信息
		data.add(d9);//进出口情况
		data.add(d10);//缴税信息
		data.add(d11);//缴纳社会保险信息
		data.add(d12);//缴纳公积金信息
		data.add(d13);//公用事业缴费信息
		data.add(d14);//政府评价
		data.add(d15);//社会评价
		data.add(d16);//行政处罚信息
		data.add(d17);//司法信息
		data.add(d18);//司法信息
		data.add(d19);//银行授信
		data.add(d20);//银行贷款
		data.add(d21);//银行贷款
		data.add(d22);//银行贷款
		data.add(d23);//贴现
		data.add(d24);//商业汇票
		data.add(d25);//报数机构说明
		data.add(d26);//服务中心说明
		data.add(d27);//信息主体说明
		data.add(d28);//查询记录
		
		
		//信息概况
		Map<String, String> info = new HashMap<String, String>();
		//logo
//		info.put("logo", "F://title.jpg");
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
//		信息主体实缴资本**万元，****年**月**日股东****公司向***公司转让**万元股权
		info.put("info1", "500");
		info.put("info2", "2000年12月08日");
		info.put("info3", "腾讯");
		info.put("info4", "百度");
		info.put("info5", "300");
//		信息主体于20**年首次与金融机构发生信贷关系，报告期内，共在**家金融机构办理过信贷业务，
//		目前**家金融机构对信息主体授信共**万元，**家金融机构**万元贷款未结清。提供对外担保****万元
		info.put("info6", "2015");
		info.put("info7", "10");
		info.put("info8", "6");
		info.put("info9", "1000");
		info.put("info10", "4");
		info.put("info11", "100");
		info.put("info12", "1");
//		报告期内，信息主体共有**条人民银行表彰记录、**条外汇管理表彰记录、**条环保部门表彰记录、
//		**条质监部门表彰记录、**条税务部门表彰记录、**条财政部门表彰记录、**条工商部门表彰记录、**条卫生部门表彰记录、
//		**条海关部门表彰记录、**条其他部门表彰记录
		info.put("info13", "2");
		info.put("info14", "3");
		info.put("info15", "4");
		info.put("info16", "5");
		info.put("info17", "6");
		info.put("info18", "7");
		info.put("info19", "8");
		info.put("info20", "9");
		info.put("info21", "10");
		info.put("info22", "1");
//		
		info.put("info23", "2");
		info.put("info24", "3");
		info.put("info25", "4");
		info.put("info26", "5");
		info.put("info27", "6");
		info.put("info28", "7");
		info.put("info29", "8");
		info.put("info30", "9");
		info.put("info31", "1");
		info.put("info32", "2");
		info.put("info33", "3");
		info.put("info34", "4");
		info.put("info35", "5");
		info.put("info36", "6");
		info.put("info37", "7");
		info.put("info38", "8");
		info.put("info39", "9");
		info.put("info40", "10");
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
		info.put("info49", "1");
		info.put("info50", "2");
		info.put("info51", "3");
		info.put("info52", "4");
		
//		imageWaterMark(createPDF(info, data), "F://title.jpg"); 
	}

	/**
	 * 创建PDF文档  for 企业版和银行版
	 * @return
	 * @throws Exception
	 * @throws docException
	 */
	public static String createPDF(Map<String, String> info, List<String[][]> datas, HttpServletRequest request) throws Exception {
        
    	//输出路径
    	String[] path = DataUtil.createTempPath(".pdf");////
    	String outPath = path[0] + path[1];
//    	String outPath ="F://test555.pdf";
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
        Chunk c2 = new Chunk("报告编号：", boldFont) ;
        Chunk c22 = new Chunk(info.get("reportNumber"), textFont) ;
        ph1.add(c2);
        ph1.add(c22);
        p1.add(ph1);
        doc.add(p1);
        
        p1 = new Paragraph("企业信用报告", firsetTitleFont);
        p1.setLeading(50);
        p1.setAlignment(Element.ALIGN_CENTER);
        doc.add(p1);
//        控制为企业版还是银行版
        p1 = new Paragraph(info.get("banben"), textFont);
        p1.setLeading(20);
        p1.setAlignment(Element.ALIGN_CENTER);
        doc.add(p1);
            
        p1 = new Paragraph();  
        p1.setLeading(20);
        p1.setAlignment(Element.ALIGN_CENTER);
        ph1 = new Phrase(); 
        Chunk c3 = new Chunk("查询时间：", boldFont) ;
        Chunk c33 = new Chunk(info.get("searchDate"), textFont) ;
        Chunk c4 = new Chunk(leftPad("查询人：", 10), boldFont) ;
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
        
        p1 = new Paragraph("信息概况", secondTitleFont);
        p1.setSpacingBefore(30);
        p1.setSpacingAfter(30);
        p1.setAlignment(Element.ALIGN_CENTER);
        doc.add(p1);
        
        p1 = new Paragraph("信息主体实缴资本"+info.get("info1")+"万元，"+info.get("info2")+"股东"+info.get("info3")+"公司向"+info.get("info4")+"公司转让"+info.get("info5")+"万元股权。", textFont);  
        p1.setFirstLineIndent(23);
        p1.setSpacingAfter(15);
        doc.add(p1);

        p1 = new Paragraph("信息主体于"+info.get("info6")+"年首次与金融机构发生信贷关系，报告期内，共在"+info.get("info7")+"家金融机构办理过信贷业务，目前"+info.get("info8")+"家金融机构对信息主体授信共"+info.get("info9")+"万元，"+info.get("info10")+"家金融机构"+info.get("info11")+"万元贷款未结清。提供对外担保"+info.get("info12")+"万元。", textFont);  
        p1.setFirstLineIndent(23);
        p1.setSpacingAfter(15);
        doc.add(p1);

        p1 = new Paragraph("报告期内，信息主体共有"+info.get("info13")+"条人民银行表彰记录、"+info.get("info14")+"条外汇管理表彰记录、"+info.get("info15")+"条环保部门表彰记录、"+info.get("info16")+"条质监部门表彰记录、"+info.get("info17")+"条税务部门表彰记录、"+info.get("info18")+"条财政部门表彰记录、"+info.get("info19")+"条工商部门表彰记录、"+info.get("info20")+"条卫生部门表彰记录、"+info.get("info21")+"条海关部门表彰记录、"+info.get("info22")+"条其他部门表彰记录。", textFont);  
        p1.setFirstLineIndent(23);
        p1.setSpacingAfter(15);
        doc.add(p1);
        
        p1 = new Paragraph("报告期内，信息主体共有"+info.get("info23")+"条人民银行处罚记录、"+info.get("info24")+"条外汇管理处罚记录、"+info.get("info25")+"条环保部门处罚记录、"+info.get("info26")+"条质监部门处罚记录、"+info.get("info27")+"条税务部门处罚记录、"+info.get("info28")+"条财政部门处罚记录、"+info.get("info29")+"条工商部门处罚记录、"+info.get("info30")+"条卫生部门处罚记录、"+info.get("info31")+"条海关部门处罚记录、"+info.get("info32")+"条其他部门处罚记录。共有"+info.get("info33")+"条欠税记录、"+info.get("info34")+"条欠水/电/汽记录、"+info.get("info35")+"条法院判决执行记录。", textFont);  
        p1.setFirstLineIndent(23);
        doc.add(p1);
        
        p1 = new Paragraph("信息主体最近一次缴纳社会保险时间为"+info.get("info36")+"，最近一次缴纳公积金时间为"+info.get("info37")+"。", textFont);  
        p1.setFirstLineIndent(23);
        doc.add(p1);
        
        p1 = new Paragraph("目前，报告中共有"+info.get("info38")+"条报数机构说明、"+info.get("info39")+"条信息主体说明、"+info.get("info40")+"条服务中心说明。", textFont);  
        p1.setFirstLineIndent(23);
        p1.setSpacingAfter(15);
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
        
        setData(doc, table, datas.get(0), baseFont);
    	
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
        
        setData(doc, table, datas.get(1), baseFont);
    	
    	//******************************对外投资情况************************************
    	
    	p1 = new Paragraph();  
        p1.setSpacingBefore(20);
        p1.setSpacingAfter(10);
        ph1 = new Phrase(); 
        Chunk c24 = new Chunk(hand, 0, 0);
        Chunk c25 = new Chunk(leftPad("对外投资情况", 9), boldFont1);
        ph1.add(c24);
        ph1.add(c25);
        p1.add(ph1);
        doc.add(p1);
        
        table = new PdfPTable(7);
        table.setTotalWidth(new float[]{50, 100, 80, 80, 80, 80, 80}); //设置列宽
        table.setLockedWidth(true); //锁定列宽
    	
        setData(doc, table, datas.get(2), baseFont);

    	//******************************对外担保情况************************************
    	
    	p1 = new Paragraph();  
        p1.setSpacingBefore(20);
        p1.setSpacingAfter(10);
        ph1 = new Phrase(); 
        Chunk c27 = new Chunk(hand, 0, 0);
        Chunk c28 = new Chunk(leftPad("对外担保情况", 9), boldFont1);
        ph1.add(c27);
        ph1.add(c28);
        p1.add(ph1);
        doc.add(p1);
        
        table = new PdfPTable(9);
        table.setTotalWidth(new float[]{40, 60, 70, 70, 70, 60, 60, 60, 60}); //设置列宽
        table.setLockedWidth(true); //锁定列宽
    	
    	setData(doc, table, datas.get(3), baseFont);
        
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
    	
    	setData(doc, table, datas.get(4), baseFont);

    	//******************************抵质押信息************************************
    	
    	 p1 = new Paragraph();  
         p1.setSpacingBefore(20);
         p1.setSpacingAfter(10);
         ph1 = new Phrase(); 
         Chunk c34 = new Chunk(hand, 0, 0);
         Chunk c35 = new Chunk(leftPad("抵质押信息", 8), boldFont1);
         ph1.add(c34);
         ph1.add(c35);
         p1.add(ph1);
         doc.add(p1);
         
         table = new PdfPTable(9);
         table.setTotalWidth(new float[]{40, 60, 70, 70, 70, 60, 60, 60, 60}); //设置列宽
         table.setLockedWidth(true); //锁定列宽
    	
    	 setData(doc, table, datas.get(5), baseFont);

    	 //******************************财务信息************************************
    	 
    	 p1 = new Paragraph();  
         p1.setSpacingBefore(20);
         p1.setSpacingAfter(10);
         ph1 = new Phrase(); 
         Chunk c37 = new Chunk(hand, 0, 0);
         Chunk c38 = new Chunk(leftPad("财务信息", 7), boldFont1);
         ph1.add(c37);
         ph1.add(c38);
         p1.add(ph1);
         doc.add(p1);
         
         table = new PdfPTable(10);
         table.setTotalWidth(new float[]{55, 55, 55, 55, 55, 55, 55, 55, 55, 55}); //设置列宽
         table.setLockedWidth(true); //锁定列宽
    	 
    	 setData(doc, table, datas.get(6), baseFont);
    	 
    	 //******************************专利信息************************************
    	 
    	 p1 = new Paragraph();  
         p1.setSpacingBefore(20);
         p1.setSpacingAfter(10);
         ph1 = new Phrase(); 
         Chunk c40 = new Chunk(hand, 0, 0);
         Chunk c41 = new Chunk(leftPad("专利信息", 7), boldFont1);
         ph1.add(c40);
         ph1.add(c41);
         p1.add(ph1);
         doc.add(p1);
         
         table = new PdfPTable(7);
         table.setTotalWidth(new float[]{40, 120, 100, 85, 85, 60, 60}); //设置列宽
         table.setLockedWidth(true); //锁定列宽
    	 
    	 setData(doc, table, datas.get(7), baseFont);

    	 //******************************进出口情况************************************
    	 
    	 p1 = new Paragraph();  
         p1.setSpacingBefore(20);
         p1.setSpacingAfter(10);
         ph1 = new Phrase(); 
         Chunk c43 = new Chunk(hand, 0, 0);
         Chunk c45 = new Chunk(leftPad("进出口情况", 8), boldFont1);
         ph1.add(c43);
         ph1.add(c45);
         p1.add(ph1);
         doc.add(p1);
         
         table = new PdfPTable(11);
         table.setTotalWidth(new float[]{50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50}); //设置列宽
         table.setLockedWidth(true); //锁定列宽
    	 
    	 setData(doc, table, datas.get(8), baseFont);

    	 //******************************缴税信息************************************
    	 
    	 p1 = new Paragraph();  
         p1.setSpacingBefore(20);
         p1.setSpacingAfter(10);
         ph1 = new Phrase(); 
         Chunk c47 = new Chunk(hand, 0, 0);
         Chunk c48 = new Chunk(leftPad("缴税信息", 7), boldFont1);
         ph1.add(c47);
         ph1.add(c48);
         p1.add(ph1);
         doc.add(p1);
         
         table = new PdfPTable(12);
         table.setTotalWidth(new float[]{50, 40, 50, 40, 40, 40, 40, 50, 50, 50, 50, 50}); //设置列宽
         table.setLockedWidth(true); //锁定列宽
    	 
    	 setData(doc, table, datas.get(9), baseFont);

    	 //******************************缴纳社会保险信息************************************
    	 
    	 p1 = new Paragraph();  
         p1.setSpacingBefore(20);
         p1.setSpacingAfter(10);
         ph1 = new Phrase(); 
         Chunk c50 = new Chunk(hand, 0, 0);
         Chunk c51 = new Chunk(leftPad("缴纳社会保险信息", 11), boldFont1);
         ph1.add(c50);
         ph1.add(c51);
         p1.add(ph1);
         doc.add(p1);
         
         table = new PdfPTable(8);
         table.setTotalWidth(new float[]{40, 80, 75, 85, 85, 60, 60, 60}); //设置列宽
         table.setLockedWidth(true); //锁定列宽
    	 
    	 setData(doc, table, datas.get(10), baseFont);
    	 
    	 //******************************缴纳公积金信息************************************
    	 
    	 p1 = new Paragraph();  
         p1.setSpacingBefore(20);
         p1.setSpacingAfter(10);
         ph1 = new Phrase(); 
         Chunk c91 = new Chunk(hand, 0, 0);
         Chunk c92 = new Chunk(leftPad("缴纳公积金信息", 10), boldFont1);
         ph1.add(c91);
         ph1.add(c92);
         p1.add(ph1);
         doc.add(p1);
         
         table = new PdfPTable(8);
         table.setTotalWidth(new float[]{40, 80, 75, 85, 85, 60, 60, 60}); //设置列宽
         table.setLockedWidth(true); //锁定列宽
    	 
    	 setData(doc, table, datas.get(11), baseFont);

    	 //******************************公用事业缴费信息************************************
    	 
    	 p1 = new Paragraph();  
         p1.setSpacingBefore(20);
         p1.setSpacingAfter(10);
         ph1 = new Phrase(); 
         Chunk c53 = new Chunk(hand, 0, 0);
         Chunk c54 = new Chunk(leftPad("公用事业缴费信息", 11), boldFont1);
         ph1.add(c53);
         ph1.add(c54);
         p1.add(ph1);
         doc.add(p1);
         
         table = new PdfPTable(7);
         table.setTotalWidth(new float[]{50, 100, 80, 80, 80, 80, 80}); //设置列宽
         table.setLockedWidth(true); //锁定列宽
    	 
    	 setData(doc, table, datas.get(12), baseFont);

    	 //******************************政府评价************************************
    	 
    	 p1 = new Paragraph();  
         p1.setSpacingBefore(20);
         p1.setSpacingAfter(10);
         ph1 = new Phrase(); 
         Chunk c56 = new Chunk(hand, 0, 0);
         Chunk c57 = new Chunk(leftPad("政府评价", 7), boldFont1);
         ph1.add(c56);
         ph1.add(c57);
         p1.add(ph1);
         doc.add(p1);
         
         table = new PdfPTable(9);
         table.setTotalWidth(new float[]{70, 60, 60, 60, 60, 60, 60, 60, 60}); //设置列宽
         table.setLockedWidth(true); //锁定列宽
    	 
    	 setData(doc, table, datas.get(13), baseFont);

    	 //******************************社会评价************************************
    	 
    	 p1 = new Paragraph();  
         p1.setSpacingBefore(20);
         p1.setSpacingAfter(10);
         ph1 = new Phrase(); 
         Chunk c59 = new Chunk(hand, 0, 0);
         Chunk c60 = new Chunk(leftPad("社会评价", 7), boldFont1);
         ph1.add(c59);
         ph1.add(c60);
         p1.add(ph1);
         doc.add(p1);
         
         table = new PdfPTable(4);
         table.setTotalWidth(new float[]{140, 140, 135, 135}); //设置列宽
         table.setLockedWidth(true); //锁定列宽
    	 
    	 setData(doc, table, datas.get(14), baseFont);

    	 //******************************行政处罚信息************************************
    	 
    	 p1 = new Paragraph();  
         p1.setSpacingBefore(20);
         p1.setSpacingAfter(10);
         ph1 = new Phrase(); 
         Chunk c62 = new Chunk(hand, 0, 0);
         Chunk c63 = new Chunk(leftPad("行政处罚信息", 9), boldFont1);
         ph1.add(c62);
         ph1.add(c63);
         p1.add(ph1);
         doc.add(p1);
         
         table = new PdfPTable(10);
         table.setTotalWidth(new float[]{55, 55, 55, 55, 55, 55, 55, 55, 55, 55}); //设置列宽
         table.setLockedWidth(true); //锁定列宽
    	 
    	 setData(doc, table, datas.get(15), baseFont);

    	 //******************************司法信息************************************
    	 
    	 p1 = new Paragraph();  
         p1.setSpacingBefore(20);
         p1.setSpacingAfter(10);
         ph1 = new Phrase(); 
         Chunk c65 = new Chunk(hand, 0, 0);
         Chunk c66 = new Chunk(leftPad("司法信息", 7), boldFont1);
         ph1.add(c65);
         ph1.add(c66);
         p1.add(ph1);
         doc.add(p1);
         
         table = new PdfPTable(7);
         table.setTotalWidth(new float[]{70, 80, 80, 80, 80, 80, 80}); //设置列宽
         table.setLockedWidth(true); //锁定列宽
    	 
    	 setData(doc, table, datas.get(16), baseFont);
    	 
         table = new PdfPTable(7);
         table.setTotalWidth(new float[]{70, 80, 80, 80, 80, 80, 80}); //设置列宽
         table.setLockedWidth(true); //锁定列宽
         
    	 setData(doc, table, datas.get(17), baseFont);
    	 
    	 //******************************银行授信************************************
    	 
    	 p1 = new Paragraph();  
         p1.setSpacingBefore(20);
         p1.setSpacingAfter(10);
         ph1 = new Phrase(); 
         Chunk c68 = new Chunk(hand, 0, 0);
         Chunk c69 = new Chunk(leftPad("银行授信", 7), boldFont1);
         ph1.add(c68);
         ph1.add(c69);
         p1.add(ph1);
         doc.add(p1);
         
         table = new PdfPTable(8);
         table.setTotalWidth(new float[]{40, 100, 75, 75, 75, 60, 60, 60}); //设置列宽
         table.setLockedWidth(true); //锁定列宽
    	 
    	 setData(doc, table, datas.get(18), baseFont);

    	 //******************************银行贷款************************************
    	 
    	 p1 = new Paragraph();  
         p1.setSpacingBefore(20);
         p1.setSpacingAfter(10);
         ph1 = new Phrase(); 
         Chunk c71 = new Chunk(hand, 0, 0);
         Chunk c72 = new Chunk(leftPad("银行贷款", 7), boldFont1);
         ph1.add(c71);
         ph1.add(c72);
         p1.add(ph1);
         doc.add(p1);
         
         table = new PdfPTable(10);
         table.setTotalWidth(new float[]{30, 55, 75, 55, 55, 55, 55, 55, 55, 55}); //设置列宽
         table.setLockedWidth(true); //锁定列宽
         
         setData(doc, table, datas.get(19), baseFont);

         //未结清
         table = new PdfPTable(10);
         table.setTotalWidth(new float[]{30, 55, 75, 55, 55, 55, 55, 55, 55, 55}); //设置列宽
         table.setLockedWidth(true); //锁定列宽
         
         String[][] data = datas.get(20);
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
        
        //已结清
        table = new PdfPTable(10);
        table.setTotalWidth(new float[]{30, 55, 75, 55, 55, 55, 55, 55, 55, 55}); //设置列宽
        table.setLockedWidth(true); //锁定列宽
        
        data = datas.get(21);
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
         
    	 //******************************贴现************************************
    	 
    	 p1 = new Paragraph();  
         p1.setSpacingBefore(20);
         p1.setSpacingAfter(10);
         ph1 = new Phrase(); 
         Chunk c74 = new Chunk(hand, 0, 0);
         Chunk c75 = new Chunk(leftPad("贴现", 5), boldFont1);
         ph1.add(c74);
         ph1.add(c75);
         p1.add(ph1);
         doc.add(p1);
         
         table = new PdfPTable(8);
         table.setTotalWidth(new float[]{40, 155, 60, 60, 60, 60, 60, 60}); //设置列宽
         table.setLockedWidth(true); //锁定列宽
    	 
    	 setData(doc, table, datas.get(22), baseFont);
    	 
    	 //******************************商业汇票************************************
    	 
    	 p1 = new Paragraph();  
         p1.setSpacingBefore(20);
         p1.setSpacingAfter(10);
         ph1 = new Phrase(); 
         Chunk c77 = new Chunk(hand, 0, 0);
         Chunk c78 = new Chunk(leftPad("商业汇票", 7), boldFont1);
         ph1.add(c77);
         ph1.add(c78);
         p1.add(ph1);
         doc.add(p1);
         
         table = new PdfPTable(8);
         table.setTotalWidth(new float[]{40, 155, 60, 60, 60, 60, 60, 60}); //设置列宽
         table.setLockedWidth(true); //锁定列宽
    	 
    	 setData(doc, table, datas.get(23), baseFont);

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
    	 
    	 setData2(doc, table, datas.get(24), baseFont);

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
    	 
    	 setData2(doc, table, datas.get(25), baseFont);

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
    	 
    	 setData2(doc, table, datas.get(26), baseFont);
    	 
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
    	 
    	 setData(doc, table, datas.get(27), baseFont);
    	 
    	 
        
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
	 * 设置数据
	 * @param doc
	 * @param table
	 * @param data
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void setData(Document doc, PdfPTable table, String[][] data, BaseFont baseFont) throws DocumentException, IOException{
		
		//添加中文字体
        BaseFont bfChinese=BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//        BaseFont baseFont = BaseFont.createFont(basePath,BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
        //设置字体样式
        Font textFont = new Font(baseFont,11,Font.NORMAL); //正常
        Font boldFont = new Font(baseFont,11,Font.BOLD); //加粗
        
		for(int i = 0; i < data.length; i++){
    		for(int j = 0; j < data[i].length; j++){
    			
    			PdfPCell cell;
    			if(i==0){
    				cell = new PdfPCell(new Phrase(data[i][j], textFont));
    			}else{
    				cell = new PdfPCell(new Phrase(data[i][j], textFont));
    			}
    			
    			cell.setMinimumHeight(30); //设置单元格高度
    			cell.setUseAscender(true); //设置可以居中
    			cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
    			cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
    			
    			//设置表头的边框
    			if(i==0){
    				cell.setBorderWidthTop(2);
    			}
    			if(i==0 && j==0){
    				cell.setBorderWidthLeft(2);
    			}
    			if(i==0 && j==(data[i].length-1)){
    				cell.setBorderWidthRight(2);
    			}
    			
    			//设置表体的边框
    			if(i>0 && j==0){
    				cell.setBorderWidthLeft(2);
    			}
    			if(i==(data.length-1)){
    				cell.setBorderWidthBottom(2);
    			}
    			if(i>0 && j==(data[1].length-1)){
    				cell.setBorderWidthRight(2);
    			}
    			
    			table.addCell(cell);
    		}
    	}
    	
    	doc.add(table);
	}
	/**
	 * 说明内容设置数据
	 * @param doc
	 * @param table
	 * @param data
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void setData2(Document doc, PdfPTable table, String[][] data, BaseFont baseFont) throws DocumentException, IOException{
		
		//添加中文字体
//        BaseFont baseFont=BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		//宋体
//        BaseFont baseFont = BaseFont.createFont(basePath,BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
        //设置字体样式
        Font textFont = new Font(baseFont,11,Font.NORMAL); //正常
        
		for(int i = 0; i < data.length; i++){
    		for(int j = 0; j < data[i].length; j++){
    			
    			PdfPCell cell;
    			if(i==0){
    				cell = new PdfPCell(new Phrase(data[i][j], textFont));
    			}else{
    				cell = new PdfPCell(new Phrase(data[i][j], textFont));
    			}
    			
    			cell.setMinimumHeight(30); //设置单元格高度
    			cell.setUseAscender(true); //设置可以居中
//    			cell.setHorizontalAlignment(Cell.ALIGN_CENTER); //设置水平居中
    			cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); //设置垂直居中
    			
    			//设置表头的边框
    			if(i==0){
    				cell.setBorderWidthTop(2);
    			}
    			if(i==0 && j==0){
    				cell.setBorderWidthLeft(2);
    			}
    			if(i==0 && j==(data[i].length-1)){
    				cell.setBorderWidthRight(2);
    			}
    			
    			//设置表体的边框
    			if(i>0 && j==0){
    				cell.setBorderWidthLeft(2);
    			}
    			if(i==(data.length-1)){
    				cell.setBorderWidthBottom(2);
    			}
    			if(i>0 && j==(data[1].length-1)){
    				cell.setBorderWidthRight(2);
    			}
    			
    			table.addCell(cell);
    		}
    	}
    	
    	doc.add(table);
	}
    /**
     * 加水印（字符串）
     * @param inputFile 需要加水印的PDF路径
     * @param outputFile 输出生成PDF的路径
     * @param waterMarkName 水印字符
     */
    public static void stringWaterMark(String inputFile, String waterMarkName) {
		try {
			String[] spe = DataUtil.separatePath(inputFile);
    		String outputFile = spe[0] + "_WM." + spe[1];
    		
			PdfReader reader = new PdfReader(inputFile);
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputFile));

			//添加中文字体
	        BaseFont baseFont=BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);

			int total = reader.getNumberOfPages() + 1;

			PdfContentByte under;
			int j = waterMarkName.length();
			char c = 0;
			int rise = 0;
			//给每一页加水印
			for (int i = 1; i < total; i++) {
				rise = 400;
				under = stamper.getUnderContent(i);
				under.beginText();
				under.setFontAndSize(baseFont, 30);
				under.setTextMatrix(200, 120);
				for (int k = 0; k < j; k++) {
					under.setTextRise(rise);
					c = waterMarkName.charAt(k);
					under.showText(c + "");
				}

				// 添加水印文字
				under.endText();
			}
			stamper.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    /**
     * 加水印（图片）
     * @param inputFile 需要加水印的PDF路径, 加上水印的PDF和未加水印的PDF在一个文件夹里，名称是原来的PFD后加了 _WM 后缀； 
     * 					如： test.pdf 加了水印后 test_WM.pdf
     * @param imageFile 水印图片路径
     * @throws Exception 
     */
    public static String imageWaterMark(String inputFile, String imageFile) throws Exception {
    	String outputFile=null;
    	try {
    		String[] spe = DataUtil.separatePath(inputFile);
    		outputFile = spe[0] + "_WM." + spe[1];
    		
    		PdfReader reader = new PdfReader(inputFile);
    		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputFile));
    		
    		int total = reader.getNumberOfPages() + 1;
    		
    		Image image = Image.getInstance(new URL(imageFile));
    		image.setAbsolutePosition(-100, 0);//坐标
    		image.scaleAbsolute(800,1000);//自定义大小
    		//image.setRotation(-20);//旋转 弧度
    		//image.setRotationDegrees(-45);//旋转 角度
    		//image.scalePercent(50);//依照比例缩放
    		
    		PdfGState gs = new PdfGState();
    		gs.setFillOpacity(0.2f);// 设置透明度为0.2

    		PdfContentByte under;
    		//给每一页加水印
    		for (int i = 1; i < total; i++) {
    			under = stamper.getUnderContent(i);
    			under.beginText();
    			// 添加水印图片
    			under.addImage(image);
        		under.setGState(gs);
    		}
    		stamper.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    		throw e;
    	}
		return outputFile;
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
    
    /**
     * 设置间距
     * @param tmp
     * @return
     */
    public static String printBlank(int tmp){
          String space="";
          for(int m=0;m<tmp;m++){
              space=space+" ";
          }
          return space;
    }


}