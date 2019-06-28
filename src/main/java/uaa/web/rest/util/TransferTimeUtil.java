package uaa.web.rest.util;

import org.apache.commons.lang3.StringUtils;

import java.time.ZonedDateTime;

public class TransferTimeUtil {
    public static String getShortContent(String content){
        if(StringUtils.isBlank(content)){
            return "未知评论";
        }
        return content.length()<=12?content:(content.substring(0,12)+"...");
    }
    public static String transferTime(ZonedDateTime createDate){
        if(createDate==null){
            return "很久以前";
        }
        try {
//            long epochSecond = Instant.now().getEpochSecond();
//            ZonedDateTime now = ZonedDateTime.now();
//            long l = createDate.toEpochSecond();
////            int time = now.getSecond() - createDate.getSecond();
//            final Date nowDate = Date.from(now.toInstant());
//            final Date fromDate = Date.from(createDate.toInstant());
//            long time = (nowDate.getTime()-fromDate.getTime())/1000;
              long time = ZonedDateTime.now().toEpochSecond()-createDate.toEpochSecond();
//            if(time<=-60&&time>-3600){
//                return "" + (int)(-time)/60 + "分钟前";
//            } else if(time<0){
//                return "刚刚";
//            }
            if(time<0){
                return "刚刚";
            }
            if(time<60){
                return "" + time + "秒前";
            } else if(60<=time && time<3600){
                return "" + (int)time/60 + "分钟前";
            } else if(3600<=time && time<=3600*24){
                return "" + (int)time/3600 + "小时前";
            }
            return createDate.getYear() + "-" + createDate.getMonthValue() + "-" +createDate.getDayOfMonth();
        }catch (Exception e){
            return "很久以前";
        }
    }
//    public static void main(String[] args){
//        ZonedDateTime createDate = ZonedDateTime.now();
//        final Date from = Date.from(createDate.toInstant());
//        String s = createDate.getYear() + "-" + createDate.getMonthValue() + "-" +createDate.getDayOfMonth();
//        System.out.println(s);
//
//    }
}
