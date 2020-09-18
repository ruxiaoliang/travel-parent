package org.itcase.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;
import org.itcase.pojo.Favorite;
import org.itcase.pojo.FavoriteExample;

@Mapper
public interface FavoriteMapper {
    @SelectProvider(type=FavoriteSqlProvider.class, method="countByExample")
    long countByExample(FavoriteExample example);

    @DeleteProvider(type=FavoriteSqlProvider.class, method="deleteByExample")
    int deleteByExample(FavoriteExample example);

    @Delete({
        "delete from tab_favorite",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into tab_favorite (user_id, route_id)",
        "values (#{userId,jdbcType=BIGINT}, #{routeId,jdbcType=BIGINT})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(Favorite record);

    @InsertProvider(type=FavoriteSqlProvider.class, method="insertSelective")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insertSelective(Favorite record);

    @SelectProvider(type=FavoriteSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.BIGINT),
        @Result(column="route_id", property="routeId", jdbcType=JdbcType.BIGINT)
    })
    List<Favorite> selectByExample(FavoriteExample example);

    @Select({
        "select",
        "id, user_id, route_id",
        "from tab_favorite",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.BIGINT),
        @Result(column="route_id", property="routeId", jdbcType=JdbcType.BIGINT)
    })
    Favorite selectByPrimaryKey(Long id);

    @UpdateProvider(type=FavoriteSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") Favorite record, @Param("example") FavoriteExample example);

    @UpdateProvider(type=FavoriteSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") Favorite record, @Param("example") FavoriteExample example);

    @UpdateProvider(type=FavoriteSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Favorite record);

    @Update({
        "update tab_favorite",
        "set user_id = #{userId,jdbcType=BIGINT},",
          "route_id = #{routeId,jdbcType=BIGINT}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(Favorite record);
}