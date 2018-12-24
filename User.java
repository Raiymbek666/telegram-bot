public class User {
    private long id_user;
    private int status;
    private String user_name;
    private String phone_number="";
    public void setId_user (long id_user){
        this.id_user=id_user;
    }
    public void setId_user (String id_user){
        this.id_user=Long.valueOf(id_user);
    }
    public void setStatus (int status){
        this.status=status;
    }
    public void setStatus (String status){
        this.status=Integer.valueOf(status);
    }

    public void setUser_name(String user_name){
        this.user_name=user_name;
    }

    public long getId_user(){
        return id_user;
    }
    public int getStatus(){
        return status;
    }
    public String getUser_name(){
        return user_name;
    }
    public void setPhone_number (String i){
        this.phone_number=i;
    }
    public String getPhone_number () {
        return phone_number;
    }
}
