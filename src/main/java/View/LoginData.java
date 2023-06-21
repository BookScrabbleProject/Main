package View;

public class LoginData {

    String playerName;
    int port;
    String ip;
    boolean isHost;
    static LoginData ld=null;

    private LoginData(){
        playerName="default";
        port=-1;
        ip="-1";
        isHost=false;
    }
    public static LoginData getLoginData(){
        if(ld==null)
            ld=new LoginData();
        return ld;
    }



    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean getIsHost() {
        return isHost;
    }

    public void setIsHost(boolean host) {
        isHost = host;
    }
}
