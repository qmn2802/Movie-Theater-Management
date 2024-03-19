package com.movie_theater.service.fogotPassWord.javamail;

import java.util.Properties;
import java.util.Random;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.movie_theater.service.fogotPassWord.MailConfig;

public class SendHtmlEmail {
    public static String generateRandomPassword() {
        String uppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercaseLetters = uppercaseLetters.toLowerCase();
        String numbers = "0123456789";
        String allCharacters = uppercaseLetters + lowercaseLetters + numbers;

        StringBuilder password = new StringBuilder();

        // Thêm 5 chữ cái đầu tiên
        for (int i = 0; i < 5; i++) {
            int randomIndex = new Random().nextInt(uppercaseLetters.length());
            char randomChar = uppercaseLetters.charAt(randomIndex);
            password.append(randomChar);
        }

        // Thêm 5 số tiếp theo
        for (int i = 0; i < 5; i++) {
            int randomIndex = new Random().nextInt(numbers.length());
            char randomChar = numbers.charAt(randomIndex);
            password.append(randomChar);
        }

        return password.toString();
    }
    public static void sendNewPassWordToEmail(String newPass,String email) {


        // 1) Lấy đối tượng session
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", MailConfig.HOST_NAME);
        props.put("mail.smtp.socketFactory.port", MailConfig.SSL_PORT);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.port", MailConfig.SSL_PORT);

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MailConfig.APP_EMAIL, MailConfig.APP_PASSWORD);
            }
        });

        // 2) Tạo nội dung email
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MailConfig.APP_EMAIL));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

            // 3) Tạo nội dung HTML
            message.setSubject("HTML Message");
            String htmlContent = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                    "<html dir=\"ltr\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" lang=\"en\" style=\"padding:0;Margin:0\">\n" +
                    " <head>\n" +
                    "  <meta charset=\"UTF-8\">\n" +
                    "  <meta content=\"width=device-width, initial-scale=1\" name=\"viewport\">\n" +
                    "  <meta name=\"x-apple-disable-message-reformatting\">\n" +
                    "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                    "  <meta content=\"telephone=no\" name=\"format-detection\">\n" +
                    "  <title>New email template 2024-01-14</title><!--[if (mso 16)]>\n" +
                    "    <style type=\"text/css\">\n" +
                    "    a {text-decoration: none;}\n" +
                    "    </style>\n" +
                    "    <![endif]--><!--[if gte mso 9]><style>sup { font-size: 100% !important; }</style><![endif]--><!--[if gte mso 9]>\n" +
                    "<xml>\n" +
                    "    <o:OfficeDocumentSettings>\n" +
                    "    <o:AllowPNG></o:AllowPNG>\n" +
                    "    <o:PixelsPerInch>96</o:PixelsPerInch>\n" +
                    "    </o:OfficeDocumentSettings>\n" +
                    "</xml>\n" +
                    "<![endif]--><!--[if !mso]><!-- -->\n" +
                    "  <link href=\"https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,400i,700,700i\" rel=\"stylesheet\"><!--<![endif]-->\n" +
                    "  <style type=\"text/css\">\n" +
                    "#outlook a {\n" +
                    "\tpadding:0;\n" +
                    "}\n" +
                    ".ExternalClass {\n" +
                    "\twidth:100%;\n" +
                    "}\n" +
                    ".ExternalClass,\n" +
                    ".ExternalClass p,\n" +
                    ".ExternalClass span,\n" +
                    ".ExternalClass font,\n" +
                    ".ExternalClass td,\n" +
                    ".ExternalClass div {\n" +
                    "\tline-height:100%;\n" +
                    "}\n" +
                    ".es-button {\n" +
                    "\tmso-style-priority:100!important;\n" +
                    "\ttext-decoration:none!important;\n" +
                    "}\n" +
                    "a[x-apple-data-detectors] {\n" +
                    "\tcolor:inherit!important;\n" +
                    "\ttext-decoration:none!important;\n" +
                    "\tfont-size:inherit!important;\n" +
                    "\tfont-family:inherit!important;\n" +
                    "\tfont-weight:inherit!important;\n" +
                    "\tline-height:inherit!important;\n" +
                    "}\n" +
                    ".es-desk-hidden {\n" +
                    "\tdisplay:none;\n" +
                    "\tfloat:left;\n" +
                    "\toverflow:hidden;\n" +
                    "\twidth:0;\n" +
                    "\tmax-height:0;\n" +
                    "\tline-height:0;\n" +
                    "\tmso-hide:all;\n" +
                    "}\n" +
                    "@media only screen and (max-width:600px) {p, ul li, ol li, a { line-height:150%!important } h1, h2, h3, h1 a, h2 a, h3 a { line-height:120%!important } h1 { font-size:30px!important; text-align:center } h2 { font-size:26px!important; text-align:center } h3 { font-size:20px!important; text-align:center } .es-header-body h1 a, .es-content-body h1 a, .es-footer-body h1 a { font-size:30px!important } .es-header-body h2 a, .es-content-body h2 a, .es-footer-body h2 a { font-size:26px!important } .es-header-body h3 a, .es-content-body h3 a, .es-footer-body h3 a { font-size:20px!important } .es-header-body p, .es-header-body ul li, .es-header-body ol li, .es-header-body a { font-size:12px!important } .es-content-body p, .es-content-body ul li, .es-content-body ol li, .es-content-body a { font-size:14px!important } .es-footer-body p, .es-footer-body ul li, .es-footer-body ol li, .es-footer-body a { font-size:12px!important } .es-infoblock p, .es-infoblock ul li, .es-infoblock ol li, .es-infoblock a { font-size:12px!important } *[class=\"gmail-fix\"] { display:none!important } .es-m-txt-c, .es-m-txt-c h1, .es-m-txt-c h2, .es-m-txt-c h3 { text-align:center!important } .es-m-txt-r, .es-m-txt-r h1, .es-m-txt-r h2, .es-m-txt-r h3 { text-align:right!important } .es-m-txt-l, .es-m-txt-l h1, .es-m-txt-l h2, .es-m-txt-l h3 { text-align:left!important } .es-m-txt-r img, .es-m-txt-c img, .es-m-txt-l img { display:inline!important } .es-button-border { display:block!important } a.es-button, button.es-button { font-size:20px!important; display:block!important; padding:10px 0px 10px 0px!important } .es-btn-fw { border-width:10px 0px!important; text-align:center!important } .es-adaptive table, .es-btn-fw, .es-btn-fw-brdr, .es-left, .es-right { width:100%!important } .es-content table, .es-header table, .es-footer table, .es-content, .es-footer, .es-header { width:100%!important; max-width:600px!important } .es-adapt-td { display:block!important; width:100%!important } .adapt-img { width:100%!important; height:auto!important } .es-m-p0 { padding:0px!important } .es-m-p0r { padding-right:0px!important } .es-m-p0l { padding-left:0px!important } .es-m-p0t { padding-top:0px!important } .es-m-p0b { padding-bottom:0!important } .es-m-p20b { padding-bottom:20px!important } .es-mobile-hidden, .es-hidden { display:none!important } tr.es-desk-hidden, td.es-desk-hidden, table.es-desk-hidden { width:auto!important; overflow:visible!important; float:none!important; max-height:inherit!important; line-height:inherit!important } tr.es-desk-hidden { display:table-row!important } table.es-desk-hidden { display:table!important } td.es-desk-menu-hidden { display:table-cell!important } .es-menu td { width:1%!important } table.es-table-not-adapt, .esd-block-html table { width:auto!important } table.es-social { display:inline-block!important } table.es-social td { display:inline-block!important } .es-menu td a { font-size:14px!important } .es-desk-hidden { display:table-row!important; width:auto!important; overflow:visible!important; max-height:inherit!important } }\n" +
                    "@media screen and (max-width:384px) {.mail-message-content { width:414px!important } }\n" +
                    "</style>\n" +
                    " </head>\n" +
                    " <body style=\"width:100%;font-family:arial, 'helvetica neue', helvetica, sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0\">\n" +
                    "  <div dir=\"ltr\" class=\"es-wrapper-color\" lang=\"en\" style=\"background-color:#E6E7E8\"><!--[if gte mso 9]>\n" +
                    "\t\t\t<v:background xmlns:v=\"urn:schemas-microsoft-com:vml\" fill=\"t\">\n" +
                    "\t\t\t\t<v:fill type=\"tile\" color=\"#e6e7e8\"></v:fill>\n" +
                    "\t\t\t</v:background>\n" +
                    "\t\t<![endif]-->\n" +
                    "   <table class=\"es-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"none\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;padding:0;Margin:0;width:100%;height:100%;background-repeat:repeat;background-position:center top;background-color:#E6E7E8\">\n" +
                    "     <tr style=\"border-collapse:collapse\">\n" +
                    "      <td valign=\"top\" style=\"padding:0;Margin:0\">\n" +
                    "       <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-content\" align=\"center\" role=\"none\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\">\n" +
                    "         <tr style=\"border-collapse:collapse\">\n" +
                    "          <td align=\"center\" style=\"padding:0;Margin:0\">\n" +
                    "           <table class=\"es-content-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;width:600px\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" role=\"none\">\n" +
                    "             <tr style=\"border-collapse:collapse\">\n" +
                    "              <td align=\"left\" style=\"Margin:0;padding-top:10px;padding-bottom:10px;padding-left:10px;padding-right:10px\"><!--[if mso]><table style=\"width:580px\" cellpadding=\"0\" cellspacing=\"0\"><tr><td style=\"width:369px\" valign=\"top\"><![endif]-->\n" +
                    "               <table class=\"es-left\" cellspacing=\"0\" cellpadding=\"0\" align=\"left\" role=\"none\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;float:left\">\n" +
                    "                 <tr style=\"border-collapse:collapse\">\n" +
                    "                  <td class=\"es-m-p0r es-m-p20b\" valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:369px\">\n" +
                    "                   <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                    "                     <tr style=\"border-collapse:collapse\">\n" +
                    "                      <td class=\"es-infoblock\" align=\"left\" style=\"padding:0;Margin:0;line-height:14px;font-size:12px;color:#999999\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:arial, 'helvetica neue', helvetica, sans-serif;line-height:18px;color:#999999;font-size:12px\">Put your preheader text here</p></td>\n" +
                    "                     </tr>\n" +
                    "                   </table></td>\n" +
                    "                 </tr>\n" +
                    "               </table><!--[if mso]></td><td style=\"width:20px\"></td><td style=\"width:191px\" valign=\"top\"><![endif]-->\n" +
                    "               <table cellspacing=\"0\" cellpadding=\"0\" align=\"right\" role=\"none\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                    "                 <tr style=\"border-collapse:collapse\">\n" +
                    "                  <td align=\"left\" style=\"padding:0;Margin:0;width:191px\">\n" +
                    "                   <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                    "                     <tr style=\"border-collapse:collapse\">\n" +
                    "                      <td esdev-links-color=\"#666666\" align=\"right\" class=\"es-infoblock\" style=\"padding:0;Margin:0;line-height:14px;font-size:12px;color:#999999\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:arial, 'helvetica neue', helvetica, sans-serif;line-height:18px;color:#999999;font-size:12px\"><a href=\"https://viewstripo.email\" target=\"_blank\" class=\"view\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;text-decoration:underline;color:#666666;font-size:12px\">Not displaying correctly?</a></p></td>\n" +
                    "                     </tr>\n" +
                    "                   </table></td>\n" +
                    "                 </tr>\n" +
                    "               </table><!--[if mso]></td></tr></table><![endif]--></td>\n" +
                    "             </tr>\n" +
                    "           </table></td>\n" +
                    "         </tr>\n" +
                    "       </table>\n" +
                    "       <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-header\" align=\"center\" role=\"none\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;background-color:transparent;background-repeat:repeat;background-position:center top\">\n" +
                    "         <tr style=\"border-collapse:collapse\">\n" +
                    "          <td align=\"center\" style=\"padding:0;Margin:0\">\n" +
                    "           <table class=\"es-header-body\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" role=\"none\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#FFFFFF;width:600px\">\n" +
                    "             <tr style=\"border-collapse:collapse\">\n" +
                    "              <td align=\"left\" style=\"padding:0;Margin:0\">\n" +
                    "               <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"none\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                    "                 <tr style=\"border-collapse:collapse\">\n" +
                    "                  <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:600px\">\n" +
                    "                   <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                    "                     <tr style=\"border-collapse:collapse\">\n" +
                    "                      <td align=\"center\" style=\"padding:0;Margin:0;padding-bottom:10px;font-size:0\">\n" +
                    "                       <table width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                    "                         <tr style=\"border-collapse:collapse\">\n" +
                    "                          <td style=\"padding:0;Margin:0;border-bottom:1px solid #cccccc;background:none;height:1px;width:100%;margin:0px\"></td>\n" +
                    "                         </tr>\n" +
                    "                       </table></td>\n" +
                    "                     </tr>\n" +
                    "                   </table></td>\n" +
                    "                 </tr>\n" +
                    "               </table></td>\n" +
                    "             </tr>\n" +
                    "             <tr style=\"border-collapse:collapse\">\n" +
                    "              <td align=\"left\" style=\"padding:0;Margin:0\">\n" +
                    "               <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"none\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                    "                 <tr style=\"border-collapse:collapse\">\n" +
                    "                  <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:600px\">\n" +
                    "                   <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"none\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                    "                     <tr style=\"border-collapse:collapse\">\n" +
                    "                      <td align=\"center\" style=\"padding:0;Margin:0;display:none\"></td>\n" +
                    "                     </tr>\n" +
                    "                   </table></td>\n" +
                    "                 </tr>\n" +
                    "               </table></td>\n" +
                    "             </tr>\n" +
                    "           </table></td>\n" +
                    "         </tr>\n" +
                    "       </table>\n" +
                    "       <table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" role=\"none\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\">\n" +
                    "         <tr style=\"border-collapse:collapse\">\n" +
                    "          <td align=\"center\" style=\"padding:0;Margin:0\">\n" +
                    "           <table class=\"es-content-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#000000;width:600px\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#000000\" align=\"center\" role=\"none\">\n" +
                    "             <tr style=\"border-collapse:collapse\">\n" +
                    "              <td align=\"left\" style=\"padding:0;Margin:0;padding-top:20px;padding-left:20px;padding-right:20px\">\n" +
                    "               <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"none\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                    "                 <tr style=\"border-collapse:collapse\">\n" +
                    "                  <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:560px\">\n" +
                    "                   <table style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:separate;border-spacing:0px;border-width:1px;border-style:solid;border-color:#000000\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\">\n" +
                    "                     <tr style=\"border-collapse:collapse\">\n" +
                    "                      <td align=\"center\" style=\"padding:0;Margin:0;padding-top:10px;font-size:0px\"><a target=\"_blank\" href=\"#\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;text-decoration:underline;color:#BF9000;font-size:14px\"><img class=\"adapt-img\" src=\"https://ebxhsxm.stripocdn.email/content/guids/899a73a5-7ade-46c6-ad81-ff6ab1f4e52b/images/logo_img.png\" alt=\"Black Friday Sales\" title=\"Black Friday Sales\" width=\"558\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\"></a></td>\n" +
                    "                     </tr>\n" +
                    "                     <tr style=\"border-collapse:collapse\">\n" +
                    "                      <td align=\"center\" style=\"padding:0;Margin:0;padding-top:10px\"><h3 style=\"Margin:0;line-height:24px;mso-line-height-rule:exactly;font-family:'source sans pro', 'helvetica neue', helvetica, arial, sans-serif;font-size:20px;font-style:normal;font-weight:normal;color:#c1b37e\">SAU DUNG QUEN MAT KHAU NUA</h3></td>\n" +
                    "                     </tr>\n" +
                    "                     <tr style=\"border-collapse:collapse\">\n" +
                    "                      <td align=\"center\" style=\"padding:0;Margin:0;padding-bottom:5px\"><h1 style=\"Margin:0;line-height:65px;mso-line-height-rule:exactly;font-family:'source sans pro', 'helvetica neue', helvetica, arial, sans-serif;font-size:54px;font-style:normal;font-weight:normal;color:#ececeb\">Your Password:</h1></td>\n" +
                    "                     </tr>\n" +
                    "                     <tr style=\"border-collapse:collapse\">\n" +
                    "                      <td align=\"center\" style=\"padding:0;Margin:0;padding-bottom:20px\"><h3 style=\"Margin:0;line-height:24px;mso-line-height-rule:exactly;font-family:'source sans pro', 'helvetica neue', helvetica, arial, sans-serif;font-size:20px;font-style:normal;font-weight:normal;color:#d00701\">"+newPass+"</h3></td>\n" +
                    "                     </tr>\n" +
                    "                   </table></td>\n" +
                    "                 </tr>\n" +
                    "               </table></td>\n" +
                    "             </tr>\n" +
                    "             <tr style=\"border-collapse:collapse\">\n" +
                    "              <td align=\"left\" style=\"padding:0;Margin:0\">\n" +
                    "               <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"none\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                    "                 <tr style=\"border-collapse:collapse\">\n" +
                    "                  <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:600px\">\n" +
                    "                   <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                    "                     <tr style=\"border-collapse:collapse\">\n" +
                    "                      <td align=\"center\" style=\"padding:0;Margin:0;font-size:0px\"><a href=\"#\" target=\"_blank\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;text-decoration:underline;color:#BF9000;font-size:14px\"><img src=\"https://ebxhsxm.stripocdn.email/content/guids/899a73a5-7ade-46c6-ad81-ff6ab1f4e52b/images/dalle_20240114_221827_a_row_of_cinema_seats_resembling_psychedelic_mushrooms_exclusively.png\" alt=\"Banner\" class=\"adapt-img\" title=\"Banner\" width=\"600\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\"></a></td>\n" +
                    "                     </tr>\n" +
                    "                   </table></td>\n" +
                    "                 </tr>\n" +
                    "               </table></td>\n" +
                    "             </tr>\n" +
                    "           </table></td>\n" +
                    "         </tr>\n" +
                    "       </table>\n" +
                    "       <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-footer\" align=\"center\" role=\"none\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;background-color:transparent;background-repeat:repeat;background-position:center top\">\n" +
                    "         <tr style=\"border-collapse:collapse\">\n" +
                    "          <td align=\"center\" style=\"padding:0;Margin:0\">\n" +
                    "           <table class=\"es-footer-body\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" role=\"none\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#EFEFEF;width:600px\">\n" +
                    "             <tr style=\"border-collapse:collapse\">\n" +
                    "              <td align=\"left\" style=\"Margin:0;padding-top:20px;padding-bottom:20px;padding-left:20px;padding-right:20px\">\n" +
                    "               <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"none\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                    "                 <tr style=\"border-collapse:collapse\">\n" +
                    "                  <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:560px\">\n" +
                    "                   <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                    "                     <tr style=\"border-collapse:collapse\">\n" +
                    "                      <td align=\"center\" style=\"padding:0;Margin:0;padding-bottom:5px;font-size:0\">\n" +
                    "                       <table class=\"es-table-not-adapt es-social\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                    "                         <tr style=\"border-collapse:collapse\">\n" +
                    "                          <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;padding-right:10px\"><img title=\"Twitter\" src=\"https://ebxhsxm.stripocdn.email/content/assets/img/social-icons/logo-gray/twitter-logo-gray.png\" alt=\"Tw\" width=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\"></td>\n" +
                    "                          <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;padding-right:10px\"><img title=\"Facebook\" src=\"https://ebxhsxm.stripocdn.email/content/assets/img/social-icons/logo-gray/facebook-logo-gray.png\" alt=\"Fb\" width=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\"></td>\n" +
                    "                          <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;padding-right:10px\"><img title=\"Youtube\" src=\"https://ebxhsxm.stripocdn.email/content/assets/img/social-icons/logo-gray/youtube-logo-gray.png\" alt=\"Yt\" width=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\"></td>\n" +
                    "                          <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;padding-right:10px\"><img title=\"Instagram\" src=\"https://ebxhsxm.stripocdn.email/content/assets/img/social-icons/logo-gray/instagram-logo-gray.png\" alt=\"Ig\" width=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\"></td>\n" +
                    "                          <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0\"><img title=\"Pinterest\" src=\"https://ebxhsxm.stripocdn.email/content/assets/img/social-icons/logo-gray/pinterest-logo-gray.png\" alt=\"P\" width=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\"></td>\n" +
                    "                         </tr>\n" +
                    "                       </table></td>\n" +
                    "                     </tr>\n" +
                    "                   </table></td>\n" +
                    "                 </tr>\n" +
                    "               </table></td>\n" +
                    "             </tr>\n" +
                    "           </table></td>\n" +
                    "         </tr>\n" +
                    "       </table>\n" +
                    "     </td>\n" +
                    "     </tr>\n" +
                    "   </table>\n" +
                    "  </div>\n" +
                    " </body>\n" +
                    "</html>";
            message.setContent(htmlContent, "text/html");

            // 4) Gửi email
            Transport.send(message);

            System.out.println("Message sent successfully");
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }
}