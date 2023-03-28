package dev.omochi.bandiscordlogger;

public class DatabaseContain {
    private int number;

    private String BanMcid;
    private String BanUuid;
    private String Banreason;
    private String BanIp;
    private boolean isBanJava;

    private String byMcid;
    private String byUuid;
    private boolean isbyJava;

    private Long TimeUNIX;

    public void setNumber(int number) {
        this.number = number;
    }

    public void setBanMcid(String banMcid) {
        BanMcid = banMcid;
    }
    public void setBanUuid(String banUuid) {
        BanUuid = banUuid;
    }
    public void setBanreason(String banreason) {
        Banreason = banreason;
    }

    public void setBanIp(String banIp) {
        BanIp = banIp;
    }
    public void setBanJava(boolean banJava) {
        isBanJava = banJava;
    }



    public void setByMcid(String byMcid) {
        this.byMcid = byMcid;
    }

    public void setByUuid(String byUuid) {
        this.byUuid = byUuid;
    }

    public void setIsbyJava(boolean isbyJava) {
        this.isbyJava = isbyJava;
    }


    public void setTimeUNIX(Long timeUNIX) {
        TimeUNIX = timeUNIX;
    }

    public int getNumber() {
        return number;
    }



    public String getBanMcid() {
        return BanMcid;
    }

    public String getBanUuid() {
        return BanUuid;
    }

    public String getBanreason() {
        return Banreason;
    }

    public String getBanIp() {
        return BanIp;
    }

    public boolean isBanJava() {
        return isBanJava;
    }

    public String getByMcid() {
        return byMcid;
    }

    public String getByUuid() {
        return byUuid;
    }

    public boolean isIsbyJava() {
        return isbyJava;
    }

    public long getTimeUNIX() {
        return TimeUNIX;
    }

    @Override
    public String toString() {
        return "DatabaseContain{" +
                "number=" + number +
                ", BanMcid='" + BanMcid + '\'' +
                ", BanUuid='" + BanUuid + '\'' +
                ", Banreason='" + Banreason + '\'' +
                ", BanIp='" + BanIp + '\'' +
                ", isBanJava=" + isBanJava +
                ", byMcid='" + byMcid + '\'' +
                ", byUuid='" + byUuid + '\'' +
                ", isbyJava=" + isbyJava +
                ", TimeUNIX=" + TimeUNIX +
                '}';
    }
}
