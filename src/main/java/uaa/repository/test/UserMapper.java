package uaa.repository.test;

import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.mapstruct.Mapper;
import uaa.domain.User;

import java.util.Map;

//@Mapper
public interface UserMapper {

    String returnSql="user_id,user_name,create_time";
    @SelectProvider(type = UserDaoProvider.class, method = "findUserById")
    User findUserById(User user);
    @SelectProvider(type = UserDaoProvider.class, method = "findByUserName")
    User findUserByName(Map<String, Object> map);

    class UserDaoProvider {
        /**
         * 多条件查询可以建立查询model
         * @param user
         * @return
         */
        public String findUserById(User user) {
            String sql = "SELECT "+returnSql+" FROM user";
            if(user.getId()!=null){
                sql += " where user_id = #{user_id}";
            }
            return sql;
        }

        /**
         * 多条件查询可以用Map
         * @param map
         * @return
         */
        public String findByUserName(Map<String, Object> map) {
            String user_id= (String) map.get("user_id");
            String user_name = (String) map.get("user_name");
            return new SQL() {
                {
                    SELECT(returnSql);
                    FROM("user");
                    WHERE("user_id = " + user_id);
                    AND();
                    WHERE("user_name ="+ user_name );
                }
            }.toString();
        }
    }
}
