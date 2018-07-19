package uaa.web.rest.util;

import util.Validators;

import java.time.ZonedDateTime;

/**
 * 工具类
 * 2018-07-19
 */
public final class CommonUtil {
    /**
     *
     * @param belongDate
     * @return 所属日期的零点时间
     */
    public static ZonedDateTime tranferBelongDateToZoneDate(String belongDate){
        if(Validators.verifyBelongDate(belongDate)){
            //如果字段不正确，默认返回当前
            String[] split = belongDate.split("-");
            try {
                return ZonedDateTime.of(Integer.parseInt(split[0]),Integer.parseInt(split[1]),Integer.parseInt(split[2]),
                    0,0,0,0,ZonedDateTime.now().getZone());
            }catch (Exception e){
                //说明时间转换错误，返回当前日期
                ZonedDateTime tmp = ZonedDateTime.now();
                return ZonedDateTime.of(tmp.getYear(),tmp.getMonthValue(),tmp.getDayOfMonth(),
                    0,0,0,0,tmp.getZone());
            }

        }else{
            //如果字段不正确，默认返回当前
            ZonedDateTime tmp = ZonedDateTime.now();
            return ZonedDateTime.of(tmp.getYear(),tmp.getMonthValue(),tmp.getDayOfMonth(),
                0,0,0,0,tmp.getZone());
        }
    }
    public static ZonedDateTime getBelongDateNextZoneDay(String belongDate){
        ZonedDateTime zonedDateTime = tranferBelongDateToZoneDate(belongDate);
        return zonedDateTime.plusDays(1);
    }
    public static void main(String[] args){
        int k = Integer.parseInt("08");
        System.out.println(k);
    }
}
