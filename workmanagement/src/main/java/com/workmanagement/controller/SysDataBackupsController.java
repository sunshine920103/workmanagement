package com.workmanagement.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebParam.Mode;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.SysManageLog;
import com.workmanagement.model.SysUser;
import com.workmanagement.service.SysManageLogService;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.service.SysUserLogService;
import com.workmanagement.service.SysUserService;
import com.workmanagement.util.NetworkUtil;
import com.workmanagement.util.SettingUtils;

/**
 * 数据备份
 * 
 * @author xiehao
 */
@Controller
@RequestMapping("/admin/sysDataBackups")
public class SysDataBackupsController {

	@Autowired
	private SysManageLogService adminLogTbService;
	@Autowired
	SysUserService sysUserService;
	@Autowired 
	SysUserLogService sysUserLogService;

	/**
	 * 首页
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/index")
	public String index(Model model, HttpServletRequest request, @RequestParam(required = false) String begin,
			@RequestParam(required = false) String end) throws IOException {
		insert(request);
		// 配置读取备份的地址
		/* List<File> list = getFileSort("c://DATA-BAK");*/
		List<File> list = getFileSort(SettingUtils.getCommonSetting("data.backup.path"));
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		
		//List<File> list = getFileSort("F://list");
		List<Map<String, Object>> li = new ArrayList();

		if (begin != null && end != null) {
			for (File file : list) {
				if (df.format(file.lastModified()).compareTo(begin)>=0 && df.format(file.lastModified()).compareTo(end)<=0) {
					Map<String, Object> m = new HashMap<>();
					m.put("address", file.getName());
					m.put("time", stampToDate(file.lastModified()));
					li.add(m);
				}
			}
//			adminLogTbService.saveNowSysUserLogTb("", NetworkUtil.getIpAddress(request), "其他管理", true);
			
		} else {
			for (File file : list) {
				Map<String, Object> m = new HashMap<>();
				m.put("address", file.getName());
				m.put("time", stampToDate(file.lastModified()));
				li.add(m);

			}
		}
		model.addAttribute("li", li);
		model.addAttribute("begin",begin);
		model.addAttribute("end",end);
		model.addAttribute("prePath", SettingUtils.getCommonSetting("get.data.backup.path"));
	return "sysDataBackups/list";
	}

	/**
	 * 获取目录下所有文件(按时间排序)
	 * 
	 * @param path
	 * @return
	 */

	public List<File> getFileSort(String path) {
		List<File> list = getFiles(path, new ArrayList<File>());
		if (list != null && list.size() > 0) {
			Collections.sort(list, new Comparator<File>() {
				public int compare(File file, File newFile) {
					if (file.lastModified() < newFile.lastModified()) {
						return 1;
					} else if (file.lastModified() == newFile.lastModified()) {
						return 0;
					} else {
						return -1;
					}
				}
			});
		}
		return list;
	}

	/**
	 * 
	 * 
	 * 
	 * 获取目录下所有文件
	 * 
	 * 
	 * 
	 * @param realpath
	 * 
	 * @param files
	 * 
	 * @return
	 * 
	 */

	public List<File> getFiles(String realpath, List<File> files) {
		File realFile = new File(realpath);
		if (realFile.isDirectory()) {
			File[] subfiles = realFile.listFiles();
			for (File file : subfiles) {
				if (file.isDirectory()) {
					getFiles(file.getAbsolutePath(), files);
				} else {
					files.add(file);
				}
			}
		}
		return files;
	}

	/*
	 * 将时间戳转换为时间
	 */
	public String stampToDate(long l) {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		long lt = new Long(l);
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}

	/**
	 * 添加管理日志信息
	 * 
	 * @throws IOException
	 */
	private void insert(HttpServletRequest request) throws IOException {
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		SysUser su = sysUserService.querySystemUserById(userDetails.getSys_user_id());

//		SysManageLog sys = new SysManageLog(su.getSys_user_org_id(), su.getSys_user_org_name(), su.getUsername(),
//				su.getSys_user_role_name(), "数据备份", "", true, NetworkUtil.getIpAddress(request), new Date());
//		adminLogTbService.insertSysUserLogTb(sys);
	}
}
