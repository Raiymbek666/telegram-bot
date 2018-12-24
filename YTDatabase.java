import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class YTDatabase {
    private String user;
    private String password;
    private Statement st;
    YTDatabase (String user, String password)throws ClassNotFoundException, SQLException{
        this.user=user;
        this.password=password;
        Class.forName("org.postgresql.Driver");
        Connection connection = null;
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/telegram",user, password
        );
        st =connection.createStatement();

    }

    public void ins(User user1) throws SQLException{
        st.executeUpdate("insert into users (id_user, status, user_name) values ("
                +user1.getId_user()
                + ", " + 1 +
                ", '"+user1.getUser_name()+"'"+
                ");"
        );
    }

    public void upds (User user1) throws SQLException {
        st.executeUpdate("update users set status = "+
                user1.getStatus()+"where id_user="+
                user1.getId_user()+
                ";");
    }
    public void updp (User user1) throws SQLException {
        st.executeUpdate("update users set phone_number = "+
                user1.getPhone_number()+"where id_user="+
                user1.getId_user()+
                ";");
    }
    public ResultSet gettAllUsers () throws SQLException {
        return st.executeQuery("Select * from users;");
    }
    public ResultSet getResultSet (User user) throws SQLException{
        return st.executeQuery("Select * from users where id_user="+
                user.getId_user()+";");
    }
    public String getPhoneNumber (User user) throws SQLException {
        ResultSet rspn=getResultSet(user);
        if (rspn.next()){
            return rspn.getString("phone_number");
        }
        else return "Не найден номер";
    }


    public ResultSet getResultSetFromParkDoc (User user) throws SQLException{
        return st.executeQuery("Select * from park_doc where id_user="+
                user.getId_user()+";");
    }

    public void createUser (Long user_id) throws SQLException {
        st.executeUpdate("insert into email_with_attachment (id_user) values ("+
                user_id+
                ");");

    }
    public void setUserName (User user) throws SQLException {
        st.executeUpdate("update users set user_name='"+user.getUser_name()+"' where id_user="+user.getId_user()+";");
    }

    public void setCity (Long user_id, String city) throws SQLException {
        st.executeUpdate("update email_with_attachment set city='"+
                city+"' where id_user="+user_id+";");

    }

    public void setPark (Long user_id, String park) throws SQLException {
        st.executeUpdate("update email_with_attachment set park_name='"+
                park+"' where id_user="+user_id+";");

    }

    public void setAmount (Long user_id, Integer amount) throws SQLException {
        st.executeUpdate("update email_with_attachment set amount='"+
                amount+"' where id_user="+user_id+";");
    }

    public void setPDSaas(User user, String saas) throws SQLException{
        ResultSet rs=st.executeQuery("Select * from park_doc where saas='"+saas+"';");

        if (rs.next()){
            st.executeUpdate("update park_doc set id_moderator='"+user.getId_user()+"' where saas='"+saas+"';");
        } else {
            throw new SQLException();
        }

    }

    public void setPat (String user_id, String pat) throws SQLException {
        ResultSet rs=st.executeQuery("Select * from park_doc where id_user='"+user_id+"';");
        if (rs.next()){
            String saas = rs.getString("saas");
            st.executeUpdate("update park_doc set pat='"+pat+"' where saas="+saas+";");
        }

    }

    public void deleteIMPD(User user) throws SQLException{
        st.executeUpdate("Update park_doc set id_moderator='0' where id_moderator='"+
                user.getId_user()+"';");
    }

    public void deleteUserEwA (Long user_id) throws SQLException {
        st.executeUpdate("delete from email_with_attachment where id_user='"
                +user_id+"';");
    }
    public void deleteUserEwA (User user) throws SQLException {
        st.executeUpdate("delete from email_with_attachment where id_user='"
                +user.getId_user()+"';");
    }
    public ResultSet getAttachmentResult (User user) throws SQLException{
        return st.executeQuery("Select * from email_with_attachment where id_user="+
                user.getId_user()+";");
    }
    public ResultSet pdAgent_agreement (String agent_agreement) throws SQLException {
        return st.executeQuery("Select * from park_doc where agent_agreement='"+agent_agreement+"';");
    }
    public ResultSet pdInfo_adv (String info_adv) throws SQLException {
        return st.executeQuery("Select * from park_doc where info_adv='"+info_adv+"';");
    }

    public ResultSet pdB2b (String b2b) throws SQLException {
        return st.executeQuery("Select * from park_doc where b2b='"+b2b+"';");
    }
    public ResultSet pdSaas (String saas) throws SQLException {
        return st.executeQuery("Select * from park_doc where saas='"+saas+"';");
    }

    public ResultSet pdSaas2 (User user) throws SQLException {
        return st.executeQuery("Select * from park_doc where id_moderator='"+user.getId_user()+"';");
    }
    public void setSaasEwA (User user, String saas) throws SQLException {
        st.executeUpdate("Insert into email_with_attachment (id_user,saas) values('"+
                user.getId_user()+"','"+saas+"');");
    }
}
