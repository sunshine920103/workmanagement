/**
 *
 */
package com.workmanagement.util;

import com.workmanagement.model.*;
import com.workmanagement.service.SysAreaService;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.service.SysOrgTypeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
public class DataUtil {

    public static final char[] ARRAY = {'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm', '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9', 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M'};
    /**
     * 每位允许的字符
     */
    private static final String POSSIBLE_CHARS = "0123456789";

    /**
     * 获取指定机构及父机构的ID
     *
     * @param org
     * @param sb
     */
    public static void getOrgParentIds(SysOrg org, StringBuffer sb, SysOrgService sysOrgService) {
        if (org.getSys_org_upid() != null) {
            SysOrg sOrg = sysOrgService.queryParentInstitutionsById(org.getSys_org_upid());
            getOrgParentIds(sOrg, sb, sysOrgService);
        }
        sb.append(org.getSys_org_id());
        sb.append(",");
    }

    public static List<ZtreeVo> getZtree(List<SysOrg> sysOrgList, List<SysOrgType> sysOrgType) {
        List<ZtreeVo> ztreeVo = new ArrayList<ZtreeVo>();
        Set<Integer> set = new HashSet<Integer>();
        for (int i = 0; i < sysOrgType.size(); i++) {
            ZtreeVo ztree = new ZtreeVo("A" + sysOrgType.get(i).getSys_org_type_id(), sysOrgType.get(i).getSys_org_type_name());
            ztree.setParent((sysOrgType.get(i).getSys_org_type_upid()) == null ? "A" : ("A" + sysOrgType.get(i).getSys_org_type_upid()));
            ztreeVo.add(ztree);
        }
        for (int i = 0; i < sysOrgList.size(); i++) {
            set.add(sysOrgList.get(i).getSys_org_id());
        }
        for (int i = 0; i < sysOrgList.size(); i++) {
            ZtreeVo ztree = new ZtreeVo(sysOrgList.get(i).getSys_org_id().toString(), sysOrgList.get(i).getSys_org_name());
            if (sysOrgList.get(i).getSys_org_upid() == null || !set.contains(sysOrgList.get(i).getSys_org_upid())) {
                ztree.setParent("A" + sysOrgList.get(i).getSys_org_type_id());
            } else {
                ztree.setParent(sysOrgList.get(i).getSys_org_upid().toString());
            }
            ztreeVo.add(ztree);
        }
        ztreeVo = ztreeVo.stream().filter(n->(n.getId().equals("A80")||n.getParent().equals("A80"))).collect(Collectors.toList());
        return ztreeVo;
    }

    public static List<ZtreeVo> getZtreeTwo(List<SysOrg> sysOrgList, List<SysOrgType> sysOrgType) {
        List<ZtreeVo> ztreeVo = new ArrayList<>();
        for (SysOrgType orgType : sysOrgType) {
            ZtreeVo ztree = new ZtreeVo("A" + orgType.getSys_org_type_id(), orgType.getSys_org_type_name());
            ztree.setParent((orgType.getSys_org_type_upid()) == null ? "A" : ("A" + orgType.getSys_org_type_upid()));
            ztreeVo.add(ztree);
        }
        List<Integer> list = new ArrayList<>();
        for (SysOrg sysOrg : sysOrgList) {
            list.add(sysOrg.getSys_org_id());
        }
        for (SysOrg sysOrg : sysOrgList) {
            ZtreeVo ztree = new ZtreeVo(sysOrg.getSys_org_id().toString(), sysOrg.getSys_org_name());
            if (sysOrg.getSys_org_upid() == null || (sysOrg.getSys_org_upid() != null && !list.contains(sysOrg.getSys_org_upid()))) {
                //如果他没有上级机构
                ztree.setParent("A" + sysOrg.getSys_org_type_id());
            } else {
                //他有上级机构
                ztree.setParent(sysOrg.getSys_org_upid().toString());
            }
            ztreeVo.add(ztree);
        }
        ztreeVo = ztreeVo.stream().filter(n->(n.getId().equals("A80")||n.getParent().equals("A80"))).collect(Collectors.toList());
        return ztreeVo;
    }

    /**
     * 获取指定地区及父地区的ID
     *
     * @param a
     */
    public static void getParentAreaIds(SysArea a, StringBuffer sb, SysAreaService sysAreaService) {
        if (a.getPid() != null) {
            SysArea area = sysAreaService.queryParentAreasById(a.getPid());
            getParentAreaIds(area, sb, sysAreaService);
        }
        sb.append(a.getSysAreaId());
        sb.append(",");
    }

    /**
     * 获取指定地区及子地区的ID
     *
     * @param a
     */
    public static void getChildAreaIds(SysArea a, StringBuffer sb) {
        if (!CollectionUtils.isEmpty(a.getSubArea())) {
            for (SysArea sysArea : a.getSubArea()) {
                getChildAreaIds(sysArea, sb);
            }
        }
        sb.append(a.getSysAreaId());
        sb.append(",");
    }

    /**
     * 获取当前地区的上级和下级的所有地区的id
     */
    public static void getUpAndDownIds(SysArea a, StringBuffer sb, SysAreaService sysAreaService) {
        if (a.getPid() != null) {
            SysArea area = sysAreaService.queryParentAreasById(a.getPid());
            getParentAreaIds(area, sb, sysAreaService);
        }
        sb.append(a.getSysAreaId());
        sb.append(",");
        a = sysAreaService.queryAreaById(a.getSysAreaId());
        if (!CollectionUtils.isEmpty(a.getSubArea())) {
            for (SysArea sysArea : a.getSubArea()) {
                getChildAreaIds(sysArea, sb);
            }
        }
        sb.append(a.getSysAreaId());
        sb.append(",");
    }

    /**
     * 获取指定机构及子机构的ID
     *
     * @param so
     */
    public static void getChildOrgIds(SysOrg so, StringBuffer sb) {
        if (!CollectionUtils.isEmpty(so.getSubSysOrg())) {
            for (SysOrg sysOrg : so.getSubSysOrg()) {
                getChildOrgIds(sysOrg, sb);
            }
        }
        sb.append(so.getSys_org_id());
        sb.append(",");
    }

    /**
     * 获取指定机构类别及子机构类别的ID
     */
    public static void getChildOrgTypeIds(SysOrgType sot, StringBuffer sb) {
        if (!CollectionUtils.isEmpty(sot.getSubSysOrgType())) {
            for (SysOrgType sysOrgType : sot.getSubSysOrgType()) {
                getChildOrgTypeIds(sysOrgType, sb);
            }
        }
        sb.append(sot.getSys_org_type_id());
        sb.append(",");
    }

    /**
     * 获取指定机构类别及父类别的ID
     *
     * @param a
     */
    public static void getParentOrgTypeIds(SysOrgType a, StringBuffer sb, SysOrgTypeService sysOrgTypeService) {
        if (a.getSys_org_type_upid() != null) {
            SysOrgType sysOrgType = sysOrgTypeService.queryInstitutionsTypeByUpid(a.getSys_org_type_upid());
            getParentOrgTypeIds(sysOrgType, sb, sysOrgTypeService);
        }
        sb.append(a.getSys_org_type_id());
        sb.append(",");
    }

    /**
     * 使用StringBuffer拼接字符串
     *
     * @param strs
     * @return
     */
    public static String stitchingString(Object... strs) {
        StringBuffer sb = new StringBuffer();
        for (Object str : strs) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 获取session里的用户信息
     *
     * @return
     */
    public static MyUserDetails getSessionUser() {
        return (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 获取随机id
     *
     * @return
     */
    public static int getRandomId() {
        Random ra = new Random();
        StringBuffer sb = new StringBuffer();
        int num = ra.nextInt(9) + 1;
        for (int i = 0; i < num; i++) {
            sb.append(ra.nextInt(9) + 1).toString();
        }
        return Integer.parseInt(sb.toString());
    }

    public static void main(String[] args) {
        System.out.println(getRandomId());
    }

    /**
     * 字符串数组转整型集合
     *
     * @param s
     * @return
     */
    public static List<Integer> strArrToIntList(String[] s) {
        if (s == null || s.length < 1) {
            return null;
        }

        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < s.length; i++) {
            list.add(Integer.parseInt(s[i]));
        }
        return list;
    }

    /**
     * 分割路径
     *
     * @param path
     * @return 返回分割后的路径
     */
    public static String[] separatePath(String path) {
        if (StringUtils.isBlank(path)) {
            return null;
        }
        String[] sep = path.split("\\.");
        return new String[]{sep[0], sep[1]};
    }

    /**
     * 集合转字符串
     *
     * @param list
     * @param separator
     * @return
     */
    public static <T> String listToString(List<T> list, char separator) {
        return StringUtils.join(list.toArray(), separator);
    }

    /**
     * 判断集合是否为空集合或null，是创建一个空的ArrayList返回，否则原样返回
     *
     * @param list
     * @return
     */
    public static <T> List<T> isEmpty(List<T> list) {
        return CollectionUtils.isEmpty(list) ? new ArrayList<T>() : list;
    }

    /**
     * 生成随机数(共15个字符)
     *
     * @return
     */
    public static String createRandomNO() {
        StringBuffer randomNumber = new StringBuffer();
        randomNumber.append(generateRandomString(9));
        return randomNumber.toString();
    }

    /**
     * 生成临时文件路径并返回
     *
     * @return [0] 返回临时目录  ， [1] 文件名
     */
    public static String[] createTempPath(String suffix) {
        String[] path = new String[2];

        //临时路径
        String temp = SettingUtils.getCommonSetting("upload.file.temp.path");
        StringBuffer tempPath = new StringBuffer();
        tempPath.append(temp);
        tempPath.append("/");

        //创建临时路径
        File file = new File(tempPath.toString());
        if (!file.exists()) {
            file.mkdirs();
        }

        path[0] = tempPath.toString();
        path[1] = generateRandomString(15) + suffix;
        return path;
    }

    /**
     * 填充补齐字符串
     *
     * @param data   需要补齐的字符
     * @param width  需要补齐多宽
     * @param repStr 用于补齐的字符
     * @return
     */
    public static String fillData(String data, Integer width, String repStr) {
        if (!StringUtils.isBlank(data)) {
            StringBuffer d = new StringBuffer();
            int fw = width - data.length();
            for (int i = 1; i <= fw; i++) {
                d.append(repStr);
            }
            d.append(data);
            return d.toString();
        }
        return null;
    }

    /**
     * 填充补齐字符串
     *
     * @param data  需要补齐的字符
     * @param width 需要补齐多宽
     * @return
     */
    public static String fillData(String data, Integer width) {
        return fillData(data, width, "0");
    }

    /**
     * 填充补齐字符串
     *
     * @param data 需要补齐的字符
     * @return
     */
    public static String fillData(String data) {
        return fillData(data, 8, "0");
    }

    /**
     * 图片转化成base64字符串
     *
     * @param path 图片路径
     * @return
     */
    public static String base64GetImageStr(String path) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(path);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }

    /**
     * base64字符串转化成图片
     *
     * @param imgStr base64字符串
     * @param path   生成图片后的保存路径
     * @return
     */
    public static boolean base64GenerateImage(String imgStr, String path) { // 对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) // 图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            // 生成jpeg图片
            String imgFilePath = path;// 新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 生产一个指定长度的随机字符串
     *
     * @param length 字符串长度
     * @return
     */
    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            sb.append(POSSIBLE_CHARS.charAt(random.nextInt(POSSIBLE_CHARS.length())));
        }
        return sb.toString();
    }

    /**
     * GBK
     *
     * @param str
     * @return
     */
    public static String encodeStrGBK(String str) {
        if (str == null)
            return null;
        try {
            return new String(str.getBytes("ISO-8859-1"), "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * UTF8
     *
     * @param str
     * @return
     */
    public static String encodeStrUTF8(String str) {
        if (StringUtils.isBlank(str))
            return null;
        try {
            return new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 上传文件
     *
     * @param request
     * @param isTemp   判断文件存放位置, true 临时目录, false 正式目录
     * @param fileSize 限制文件上传的大小
     * @return
     * @throws Exception
     */
    public static List<String> uploadFile(HttpServletRequest request, boolean isTemp, Long fileSize) throws Exception {
        List<String> files = new ArrayList<String>(); //返回上传到服务器的路径
        String sep = "/"; //文件分隔符
        String fileDir = null;// 存放图片的路径
        String subDir = null;// 如果存放的是正式目录在增加子目录
        File dirPath = null;

        //判断存放的目录
        if (isTemp) {
            fileDir = SettingUtils.getCommonSetting("upload.file.temp.path");
            dirPath = new File(fileDir);
        } else {
            fileDir = SettingUtils.getCommonSetting("upload.file.path");
            subDir = buildDirName(2);
            dirPath = new File(fileDir + sep + subDir);
        }

        //创建存放图片的目录
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        // 创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        // 判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            // 转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            // 取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                //新生成的临时文件的文件名
                StringBuffer newFileName = new StringBuffer();
                // 取得上传文件
                MultipartFile file = multiRequest.getFile(iter.next());
                if (file != null && file.getBytes().length != 0) {
                    // 取得当前上传文件的文件名称
                    String fileTrueName = file.getOriginalFilename();
                    // 获取文件后缀
                    String ext = fileTrueName.substring(fileTrueName.lastIndexOf("."));
                    // 判断上传文件的大小是否合法
                    if (fileSize != null) {
                        if (file.getSize() > fileSize.longValue()) {
                            throw new Exception("文件大小超出限制");
                        }
                    }
                    // 如果名称不为“”,说明该文件存在，否则说明该文件不存在
                    if (fileTrueName.trim() != "") {
                        // 重命名上传后的文件名
                        newFileName.append(System.currentTimeMillis());
                        newFileName.append(ext);
                        // 定义上传路径
                        String fileName = null;
                        if (subDir != null) {
                            fileName = fileDir + sep + subDir + sep + newFileName.toString();
                            // 添加上传后的路径
                            files.add(subDir + sep + newFileName.toString());
                        } else {
                            fileName = fileDir + sep + newFileName.toString();
                            files.add(newFileName.toString());
                        }
                        File localFile = new File(fileName);
                        // 保存上传的文件
                        file.transferTo(localFile);
                    } else {
                        files.add("");
                    }
                } else {
                    files.add("");
                }
            }
        }
        return files;
    }

    /**
     * 根据文件名删除正式文件夹下的图片和缩略图
     *
     * @param fileName
     */
    public static void deleteByUploadFile(String fileName) {
        if (!StringUtils.isBlank(fileName)) {
            String sep = System.getProperty("file.separator");
            String fileDir = SettingUtils.getCommonSetting("upload.file.path");// 存放文件文件夹名称
            String filePath = fileDir + sep + fileName;
            FileUtils.deleteFile(filePath);
        }
    }

    /**
     * 根据文件名删除正式文件夹下的图片和缩略图
     *
     * @param fileName
     */
    public static void deleteByUploadImg(String fileName) {
        if (!StringUtils.isBlank(fileName)) {
            String sep = System.getProperty("file.separator");
            String fileDir = SettingUtils.getCommonSetting("upload.image.path");// 存放文件文件夹名称
            String filePath = fileDir + sep + fileName;
            FileUtils.deleteFile(filePath);
            String[] parsedName = FileUtils.getFullFileNameAndExtension(filePath);
            String thumbPath = parsedName[0] + parsedName[1] + "_S" + parsedName[2];
            FileUtils.deleteFile(thumbPath);
        }
    }

    /**
     * 上传图片
     *
     * @param request
     * @param isTemp      判断文件存放位置, true 临时目录, false 正式目录
     * @param createThumb 是否创建缩略图
     * @param W           缩略图的宽
     * @param H           缩略图的高
     * @param fileSize    限制图片大小
     * @return
     * @throws Exception
     */
    private static List<String> uploadImg(HttpServletRequest request, boolean isTemp, boolean createThumb,
                                          int W, int H, Long fileSize) throws Exception {

        List<String> files = new ArrayList<String>(); //返回上传到服务器的路径
        String sep = "/"; //文件分隔符
        String fileDir = null;// 存放图片的路径
        String subDir = null;// 如果存放的是正式目录在增加子目录
        File dirPath = null;

        //判断存放的目录
        if (isTemp) {
            fileDir = SettingUtils.getCommonSetting("upload.file.temp.path");
            dirPath = new File(fileDir);
        } else {
            fileDir = SettingUtils.getCommonSetting("upload.image.path");
            subDir = buildDirName(2);
            dirPath = new File(fileDir + sep + subDir);
        }

        //创建存放图片的目录
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        // 创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        // 判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            // 转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            // 取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                //新生成的临时文件的文件名
                StringBuffer newFileName = new StringBuffer();
                // 取得上传文件
                MultipartFile file = multiRequest.getFile(iter.next());
                if (file != null && file.getBytes().length != 0) {
                    // 取得当前上传文件的文件名称
                    String fileTrueName = file.getOriginalFilename();
                    // 获取文件后缀
                    String ext = fileTrueName.substring(fileTrueName.lastIndexOf("."));
                    // 判断上传文件的大小
                    if (fileSize != null) {
                        long fs = file.getSize();
                        if (fs > fileSize.longValue()) {
                            throw new Exception("文件大小超出限制");
                        }
                    }
                    // 判断上传文件的后缀是否合法
                    if (!".jpg/.jpeg/.gif/.bmp/.png".contains(ext.toLowerCase())) {
                        throw new Exception("图片格式错误");
                    }
                    // 如果名称不为“”,说明该文件存在，否则说明该文件不存在
                    if (fileTrueName.trim() != "") {
                        // 重命名上传后的文件名
                        newFileName.append(generateRandomString(15));
                        newFileName.append(ext);
                        // 定义上传路径
                        String fileName = null;
                        if (subDir != null) {
                            fileName = fileDir + sep + subDir + sep + newFileName.toString();
                            // 添加上传后的路径
                            files.add(subDir + sep + newFileName.toString());
                        } else {
                            fileName = fileDir + sep + newFileName.toString();
                            files.add(newFileName.toString());
                        }
                        File localFile = new File(fileName);
                        // 保存上传的文件
                        file.transferTo(localFile);
                        //是否创建缩略图
                        if (createThumb && !".gif".contains(ext.toLowerCase())) {
                            if (W == 0 && H == 0) {
                                String widthS = SettingUtils.getCommonSetting("thumbnailator.width");
                                String heightS = SettingUtils.getCommonSetting("thumbnailator.height");
                                Integer width = !StringUtils.isBlank(widthS) ? Integer.valueOf(widthS) : 120;
                                Integer height = !StringUtils.isBlank(heightS) ? Integer.valueOf(heightS) : 100;
                                ImagesTool.img_small(fileName, "_S", width, height);
                            } else {
                                ImagesTool.img_small(fileName, "_S", W, H);
                            }
                        }
                    } else {
                        files.add("");
                    }
                } else {
                    files.add("");
                }
            }
        }
        return files;
    }

    /**
     * 上传图片,不会创建缩略图
     *
     * @param req
     * @param isTemp 判断文件存放位置, true 临时目录, false 正式目录
     * @return 返回上传后的路径
     * @throws Exception
     */
    public static List<String> uploadImg(HttpServletRequest req, boolean isTemp, Long fileSize) throws Exception {
        return uploadImg(req, isTemp, false, 0, 0, fileSize);
    }

    /**
     * 上传图片,会创建缩略图,如果 W 和 H 都传入 0 则使用配置文件中的配置
     *
     * @param req
     * @param isTemp 判断文件存放位置, true 临时目录, false 正式目录
     * @param W      缩略图的宽
     * @param H      缩略图的高
     * @return 返回上传后的路径
     * @throws Exception
     */
    public static List<String> uploadImg(HttpServletRequest req, boolean isTemp, Integer W, Integer H, Long fileSize) throws Exception {
        return uploadImg(req, isTemp, true, W, H, fileSize);
    }

    /**
     * 将临时文件夹中的图片移到正式目录中
     *
     * @param filename
     * @param createThumb
     * @return
     * @throws IOException
     */
    public static String moveToDir(String filename, boolean createThumb) throws IOException {
        String fileDir = SettingUtils.getCommonSetting("upload.file.temp.path");
        //String sep = System.getProperty("file.separator");
        String sep = "/";
        String imgFileDir = SettingUtils.getCommonSetting("upload.image.path");

        File srcFile = new File(fileDir + sep + filename);
        try {
            String filenames = fileDir + sep + filename;
            String[] fileNamesi = filenames.split("\\.");
            FileUtils.deleteFile(fileNamesi[0] + "_S" + "." + fileNamesi[1]);
        } catch (Exception e) {
            System.out.println("图片名称错误。。。。");
        }

        StringBuffer subDir = new StringBuffer();
        for (int i = 0; i < 2; i++) {
            if (i != 0) {
                subDir.append(sep);
            }
            Random random = new Random();
            StringBuffer sb = new StringBuffer();
            sb.append(DataUtil.ARRAY[random.nextInt(DataUtil.ARRAY.length)]);
            sb.append(DataUtil.ARRAY[random.nextInt(DataUtil.ARRAY.length)]);

            subDir.append(sb.toString());
        }

        File dstDir = new File(imgFileDir + sep + subDir);
        if (!dstDir.exists()) {
            dstDir.mkdirs();
        }

        org.apache.commons.io.FileUtils.moveFileToDirectory(srcFile, dstDir, true);

        if (createThumb) {
            String widthS = SettingUtils.getCommonSetting("thumbnailator.width");
            String heightS = SettingUtils.getCommonSetting("thumbnailator.height");
            Integer width = !StringUtils.isBlank(widthS) ? Integer.valueOf(widthS) : 400;
            Integer height = !StringUtils.isBlank(heightS) ? Integer.valueOf(heightS) : 500;

            String fileName = imgFileDir + sep + subDir + sep + filename;
            ImagesTool.img_small(fileName, "_S", width, height);
        }

        return subDir + sep + filename;
    }

	/*======================================================================*/
    /*======================================================================*/
    /*======================================================================*/
    /*======================================================================*/
    /*======================================================================*/

    /**
     * 用于处理上传分片的文件
     *
     * @param req
     * @param name
     */
    public static void uploadSegmentedFile(HttpServletRequest req, String name) {
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) req;
        String sep = System.getProperty("file.separator");
        String fileDir = SettingUtils.getCommonSetting("upload.file.temp.path");// 存放文件文件夹名称

        File dirPath = new File(fileDir);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        MultipartFile mf = multiRequest.getFile("file");
        try {
            byte[] src = mf.getBytes();
            if (src != null && src.length != 0) {
                File dst = new File(fileDir + sep + name);
                FileUtils.copyByteToFile(src, dst);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 合并文件
     *
     * @param fileName
     * @param ext
     * @param toFileName
     * @param size
     */
    public static void mergeFile(String fileName, String ext, String toFileName, int size) {
        String sep = System.getProperty("file.separator");
        String fileDir = SettingUtils.getCommonSetting("upload.file.temp.path");// 存放文件文件夹名称

        OutputStream writer = null;
        InputStream reader = null;
        try {
            writer = new FileOutputStream(fileDir + sep + toFileName);
            int len;
            byte[] cbuf = new byte[1024 * 250];
            File file;
            for (int i = 0; i < size; i++) {
                file = new File(fileDir + sep + fileName + i + ext);
                if (!file.exists()) {
                    break;
                }
                reader = new FileInputStream(file);
                while ((len = reader.read(cbuf, 0, cbuf.length)) != -1) {
                    writer.write(cbuf, 0, len);
                    writer.flush();
                }

                reader.close();
                file.delete();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 将商品图片图片从临时文件夹移动到正式文件里面，
     *
     * @param filename
     * @param createThumb 如果需要创建说略图，则会将图片进行缩放，缩放规则如下：
     *                    标准尺寸为：宽500*高400.
     *                    如果高度比例低于5：4的，不进行裁剪
     *                    如果高度比例等于5：4的，按照宽度比例进行缩放
     *                    如果高度比例大于5：4的，按照宽带比例进行缩放，同时长度超过比例的部分，采取留中间部分的策略
     * @return
     * @throws IOException
     */
    public static String movePicForCommodity(String filename, boolean createThumb) throws IOException {
        String fileDir = SettingUtils.getCommonSetting("upload.file.temp.path");
        String sep = System.getProperty("file.separator");
        String imgFileDir = SettingUtils.getCommonSetting("upload.image.path");

        File srcFile = new File(fileDir + sep + filename);
        try {
            String filenames = fileDir + sep + filename;
            String[] fileNamesi = filenames.split("\\.");
            FileUtils.deleteFile(fileNamesi[0] + "_S" + "." + fileNamesi[1]);
        } catch (Exception e) {
            System.out.println("图片名称错误。。。。");
        }

        StringBuffer subDir = new StringBuffer();
        for (int i = 0; i < 2; i++) {
            if (i != 0) {
                subDir.append(sep);
            }
            Random random = new Random();
            StringBuffer sb = new StringBuffer();
            sb.append(DataUtil.ARRAY[random.nextInt(DataUtil.ARRAY.length)]);
            sb.append(DataUtil.ARRAY[random.nextInt(DataUtil.ARRAY.length)]);

            subDir.append(sb.toString());
        }

        File dstDir = new File(imgFileDir + sep + subDir);
        if (!dstDir.exists()) {
            dstDir.mkdirs();
        }

        org.apache.commons.io.FileUtils.moveFileToDirectory(srcFile, dstDir, true);

        if (createThumb) {
            String StandardHeight = SettingUtils.getCommonSetting("commoditythumb.height");
            String StandardWidth = SettingUtils.getCommonSetting("commoditythumb.width");

            Integer height = !StringUtils.isBlank(StandardHeight) ? Integer.valueOf(StandardHeight) : 400;
            Integer width = !StringUtils.isBlank(StandardWidth) ? Integer.valueOf(StandardWidth) : 500;

            String fileName = imgFileDir + sep + subDir + sep + filename;
            String[] fileNames = fileName.split("\\.");
            ImageUtil.ScaleAndCutCommodityImage(fileNames[0] + "." + fileNames[1], fileNames[0] + "_S" + "." + fileNames[1], width, height);
        }

        return subDir + sep + filename;
    }

    /**
     * 图片裁剪
     *
     * @param filename 图片名
     * @param cx       裁剪宽度
     * @param cy       裁剪高度
     * @param width    原图发大或缩小后的宽度
     * @param left     截图开始的左上角坐标x
     * @param top      截图开始的左上角坐标y
     * @return
     * @throws IOException
     */
    public static String moveImgToDir(String filename, int cx, int cy, int width, int left, int top) throws IOException {
        String fileDir = SettingUtils.getCommonSetting("upload.file.temp.path");
        String sep = System.getProperty("file.separator");
        String imgFileDir = SettingUtils.getCommonSetting("upload.image.path");

        File srcFile = new File(fileDir + sep + filename);

        StringBuffer subDir = new StringBuffer();
        for (int i = 0; i < 2; i++) {
            if (i != 0) {
                subDir.append(sep);
            }
            Random random = new Random();
            StringBuffer sb = new StringBuffer();
            sb.append(DataUtil.ARRAY[random.nextInt(DataUtil.ARRAY.length)]);
            sb.append(DataUtil.ARRAY[random.nextInt(DataUtil.ARRAY.length)]);

            subDir.append(sb.toString());
        }

        File dstDir = new File(imgFileDir + sep + subDir);
        if (!dstDir.exists()) {
            dstDir.mkdirs();
        }

        org.apache.commons.io.FileUtils.moveFileToDirectory(srcFile, dstDir, true);
        String fileName = imgFileDir + sep + subDir + sep + filename;
        String subfilename = ImageResizer.resizeCutImage(fileName, "_S", width);
        ImageResizer.saveResizedCutImage(subfilename, fileName, cx, cy, left, top);

        return subDir + sep + URLEncoder.encode(filename);
    }

    /**
     * 获取图片格式
     *
     * @param img
     * @return
     * @throws IOException
     */
    public static String getImageType(byte[] img) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(img);
        MemoryCacheImageInputStream is = new MemoryCacheImageInputStream(bais);
        Iterator<ImageReader> it = ImageIO.getImageReaders(is);
        ImageReader r = null;
        while (it.hasNext()) {
            r = it.next();
            break;
        }
        if (r == null) {
            return null;
        }
        return r.getFormatName().toLowerCase();
    }

    /**
     * 通过num,创建多级目录的名称
     *
     * @param num 创建几级
     * @return 创建好的目录字符串
     */
    public static String buildDirName(int num) {
        String sep = "/";
        StringBuffer subDir = new StringBuffer();
        for (int i = 0; i < num; i++) {
            if (i != 0) {
                subDir.append(sep);
            }
            Random random = new Random();
            StringBuffer sb = new StringBuffer();
            sb.append(DataUtil.ARRAY[random.nextInt(DataUtil.ARRAY.length)]);
            sb.append(DataUtil.ARRAY[random.nextInt(DataUtil.ARRAY.length)]);

            subDir.append(sb.toString());
        }

        return subDir.toString();
    }

    /**
     * 计算地球上任意两点(经纬度)距离
     *
     * @param long1 第一点经度
     * @param lat1  第一点纬度
     * @param long2 第二点经度
     * @param lat2  第二点纬度
     * @return 返回距离 单位：米
     */
    public static double distance(double long1, double lat1, double long2, double lat2) {
        double a, b, R;
        R = 6378137; // 地球半径
        lat1 = lat1 * Math.PI / 180.0;
        lat2 = lat2 * Math.PI / 180.0;
        a = lat1 - lat2;
        b = (long1 - long2) * Math.PI / 180.0;
        double d;
        double sa2, sb2;
        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1) * Math.cos(lat2) * sb2 * sb2));
        return d;
    }

    /**
     * 判断是否含有中文字符
     *
     * @param str
     * @return
     */
    public static boolean isChineseChar(String str) {
        boolean temp = false;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            temp = true;
        }
        return temp;
    }


}
