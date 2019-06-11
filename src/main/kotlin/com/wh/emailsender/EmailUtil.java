package com.wh.emailsender;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.util.Date;
import java.util.Properties;

/**
 * 邮件发送的工具类
 *
 * @author wangyuelin
 */
public class EmailUtil {
    private static final Logger logger = LogManager.getLogger(EmailUtil.class);

    public static void main(String[] args) {
        sendEmail("wanghao262910@163.com", "452599068@qq.com", "wanghao19900914");
        sendEmail("wanghao262910@163.com", "1989278619@qq.com", "wanghao19900914");
    }

    private static Session mSession;

    private static void initSession() {
        if (mSession != null) return;
        Properties props = new Properties();
        //设置用户的认证方式
        props.setProperty("mail.smtp.auth", "true");
        //设置传输协议
        props.setProperty("mail.transport.protocol", "smtp");
        //设置发件人的SMTP服务器地址
        props.setProperty("mail.smtp.host", "smtp.163.com");
        mSession = Session.getDefaultInstance(props);
        mSession.setDebug(true);
    }

    public static void sendEmail(String sender, String receiver, String pwd) {
        try {
            initSession();
            Message msg = getMimeMessage(mSession, sender, receiver);
            Transport transport = mSession.getTransport();
            //设置发件人的账户名和密码
            transport.connect(sender, pwd);
            //发送邮件，并发送到所有收件人地址，message.getAllRecipients()
            // 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
            transport.sendMessage(msg, msg.getAllRecipients());
            //5、关闭邮件连接
            transport.close();
        } catch (Exception e) {
            logger.error("发送邮件失败" + e.getMessage());
        }
    }


    /**
     * 获得创建一封邮件的实例对象
     *
     * @param session
     * @return
     */
    public static MimeMessage getMimeMessage(Session session, String sender, String receiver) throws Exception {
        //创建一封邮件的实例对象
        MimeMessage msg = new MimeMessage(session);
        //设置发件人地址
        msg.setFrom(new InternetAddress(sender));
        /**
         * 设置收件人地址（可以增加多个收件人、抄送、密送），即下面这一行代码书写多行
         * MimeMessage.RecipientType.TO:发送
         * MimeMessage.RecipientType.CC：抄送
         * MimeMessage.RecipientType.BCC：密送
         */
        msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiver));
        //设置邮件主题
        msg.setSubject("不知道什么名字", "UTF-8");

        //1、设置邮件文本内容
        MimeBodyPart text = new MimeBodyPart();
        // 这里添加图片的方式是将整个图片包含到邮件内容中, 实际上也可以以 http 链接的形式添加网络图片
        String htmlText = "<font size =\"20\" face=\"arial\" >详情请查看附件</font>";

        text.setContent(htmlText, "text/html;charset=UTF-8");

        // 9. 创建附件"节点"
        MimeBodyPart attachment = new MimeBodyPart();
        // 读取本地文件
        DataHandler dh2 = new DataHandler(new FileDataSource("/Users/wanghao/Desktop/脑婆备份/邮箱统计1222.docx"));
        // 将附件数据添加到"节点"
        attachment.setDataHandler(dh2);
        // 设置附件的文件名（需要编码）
        attachment.setFileName(MimeUtility.encodeText(dh2.getName()));

        // 10. 设置（文本+图片）和 附件 的关系（合成一个大的混合"节点" / Multipart ）
        MimeMultipart mm = new MimeMultipart();
        mm.addBodyPart(text);
        mm.addBodyPart(attachment);     // 如果有多个附件，可以创建多个多次添加
        mm.setSubType("mixed");         // 混合关系
        // 11. 设置整个邮件的关系（将最终的混合"节点"作为邮件的内容添加到邮件对象）
        msg.setContent(mm);
        //设置邮件的发送时间,默认立即发送
        msg.setSentDate(new Date());

        return msg;
    }


}
