package com.samples;

import com.berlioz.Berlioz;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@RestController
public class DbController {

    @RequestMapping("/db")
    public String index() throws Exception {

        Class.forName("com.mysql.cj.jdbc.Driver");

        Connection con = Berlioz.service("db").mysql()
                .getConnection("jdbc:mysql://x/demo?useSSL=false", "berlioz", "myPassword");

        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from addressBook");

        String output = "Greetings!!! <b>";
        while(resultSet.next()) {
            output += resultSet.getString("name") + "<br />";
        }
        output += "</b>";

        con.close();

        return output;
    }

}