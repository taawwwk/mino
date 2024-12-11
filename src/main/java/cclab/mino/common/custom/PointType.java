//package cclab.mino.common;
//
//import org.hibernate.engine.spi.SharedSessionContractImplementor;
//import org.hibernate.usertype.UserType;
//import org.hibernate.usertype.ParameterizedType;
//import java.io.Serializable;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Types;
//import java.util.Properties;
//
//public class PointType implements UserType, ParameterizedType {
//
//    @Override
//    public int[] sqlTypes() {
//        return new int[]{Types.VARCHAR};
//    }
//
//    @Override
//    public Class<Point> returnedClass() {
//        return Point.class;
//    }
//
//    @Override
//    public boolean equals(Object x, Object y) {
//        if (x == y) return true;
//        if (x == null || y == null) return false;
//        return x.equals(y);
//    }
//
//    @Override
//    public int hashCode(Object x) {
//        return x.hashCode();
//    }
//
//    @Override
//    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws SQLException {
//        String pointString = rs.getString(names[0]);
//        if (pointString == null) {
//            return null;
//        }
//        String[] parts = pointString.replace("Point(", "").replace(")", "").split(", ");
//        double lat = Double.parseDouble(parts[0].split("=")[1]);
//        double lng = Double.parseDouble(parts[1].split("=")[1]);
//        return new Point(lat, lng);
//    }
//
//    @Override
//    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws SQLException {
//        if (value == null) {
//            st.setNull(index, Types.VARCHAR);
//        } else {
//            Point point = (Point) value;
//            st.setString(index, point.toString());
//        }
//    }
//
//    @Override
//    public Object deepCopy(Object value) {
//        if (value == null) return null;
//        Point point = (Point) value;
//        return new Point(point.getLat(), point.getLng());
//    }
//
//    @Override
//    public boolean isMutable() {
//        return true;
//    }
//
//    @Override
//    public Serializable disassemble(Object value) {
//        return (Serializable) deepCopy(value);
//    }
//
//    @Override
//    public Object assemble(Serializable cached, Object owner) {
//        return deepCopy(cached);
//    }
//
//    @Override
//    public Object replace(Object original, Object target, Object owner) {
//        return deepCopy(original);
//    }
//
//    @Override
//    public void setParameterValues(Properties parameters) {
//        // No parameters to set
//    }
//}
