package ee.ituk.rsvp.database;

import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StringIdentifierGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        Connection connection = session.connection();

        try {
            Statement statement = connection.createStatement();

            String sid = RandomStringUtils.randomAlphanumeric(10);
            ResultSet rs = statement.executeQuery("select * from rsvp.invites where id='" + sid + "'");

            while (rs.next()) {
                sid = RandomStringUtils.randomAlphanumeric(10);
                rs = statement.executeQuery("select * from rsvp.invites where id='" + sid + "'");
            }
            System.out.println("Generated Id: " + sid);
            return sid;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
