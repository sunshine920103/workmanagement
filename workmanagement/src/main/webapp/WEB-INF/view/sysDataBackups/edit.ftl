<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title></title>
    <#include "/fragment/common.ftl"/>
</head>
<body>
<div class="showListBox">
    <table cellpadding="0" cellspacing="0">
    	<caption class="titleFont1 titleFont1Ex">修改定时备份</caption>
        <tbody>
        <tr>
            <td width="200" class="noBorderL firstTD">定时备份</td>
            <td width="500" class="secondTD">
                <input class="verticalMiddle marginR10" type="radio" name="numLimit" checked><span class="fontSize12">开启</span>
                <input class="verticalMiddle marginR10 marginL20" type="radio" name="numLimit"><span class="fontSize12">关闭</span>
            </td>
        </tr>
        <tr>
            <td class="noBorderL firstTD">备份周期</td>
            <td class="secondTD">
                <select class="inputSty">
                    <option>每月</option>
                    <option>每周</option>
                    <option>每日</option>
                </select>
            </td>
        </tr>
        <tr>
            <td class="noBorderL firstTD">备份时间</td>
            <td class="secondTD">
                <select class="inputSty">
                    <option>1日</option>
                    <option>2日</option>
                </select>
            </td>
        </tr>
        </tbody>
    </table>

    <div class="showBtnBox">
        <button type="button" class="cancleBtn">取消</button>
        <button type="button" class="sureBtn">确定</button>
    </div>
</div>
</body>
</html>