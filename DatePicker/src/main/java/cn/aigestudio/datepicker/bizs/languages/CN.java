package cn.aigestudio.datepicker.bizs.languages;

/**
 * 中文的默认实现类
 * 如果你想实现更多的语言请参考Language{@link DPLManager}
 *
 * The implementation class of chinese.
 * You can refer to Language{@link DPLManager} if you want to define more language.
 *
 * @author AigeStudio 2015-03-28
 */
public class CN extends DPLManager {
    @Override
    public String[] titleMonth() {
        return new String[]{"01月", "02月", "03月", "04月", "05月", "06月", "07月", "08月", "09月", "10月", "11月", "12月"};
    }

    @Override
    public String titleEnsure() {
        return "确定";
    }

    @Override
    public String titleBC() {
        return "公元前";
    }

    @Override
    public String[] titleWeek() {
        return new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    }
}
