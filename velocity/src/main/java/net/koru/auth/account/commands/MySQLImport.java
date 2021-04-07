package net.koru.auth.account.commands;

import com.velocitypowered.api.command.SimpleCommand;
import net.koru.auth.account.Account;
import net.koru.auth.utils.TaskUtil;
import org.checkerframework.checker.units.qual.A;

import java.sql.*;
import java.util.UUID;

public class MySQLImport implements SimpleCommand {

    @Override
    public void execute(SimpleCommand.Invocation invocation) {
        try {
            importMySQL();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public void importMySQL() throws SQLException {
        Connection connection = DriverManager.getConnection ("jdbc:mysql://149.56.107.180/DynamicBungeeAuth","root", "cMWesVVsPCzsKnpJWJPBZYaBQhqJYq");

        Statement s = connection.createStatement();
        ResultSet rs = s.executeQuery ("select * from playerdata");
        int i = 0;
        while (rs.next()) {
            String salt = rs.getString("salt");
            String password = rs.getString("password");
            UUID uuid = UUID.fromString(rs.getString("uuid"));

            if(password == null) continue;
            if(rs.getString("uuid") == null) continue;
            if(salt == null) continue;

            Account account = new Account(uuid);

            account.setRegister(true);
            account.setSalt(salt);
            account.setPassword(password);

            account.save();
            i++;
        }
        connection.close();
        System.out.println("Accounts imported: " + i);
    }


}