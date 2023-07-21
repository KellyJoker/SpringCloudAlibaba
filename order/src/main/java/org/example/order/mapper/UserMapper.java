package org.example.order.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.order.entity.pojo.UserPO;

/**
 * 若没有配置*Mapper.xml文件，则需要配合@Select、@Delete、@Insert、@Update注解来操作sql，
 * 此注解可以无须mapper文件，简化配置。也可以两者一起使用。
 * 注意⚠️：若使用以上注解，则配置文件中不需要重复配置sql，否则会报错！
 */
@Mapper
public interface UserMapper{
    /**
     * 删除用户信息
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 添加用户信息
     * @param record
     * @return
     */
    int insert(UserPO record);

    /**
     * 查询用户信息
     * @param id
     * @return
     */
    UserPO selectByPrimaryKey(Integer id);

    /**
     * 查询所有用户信息
     * @return
     */
    @Select("select id, username, age, gender from user_info")
    List<UserPO> selectAll();

    /**
     * 修改用户信息
     * @param record
     * @return
     */
    int updateByPrimaryKey(UserPO record);
}