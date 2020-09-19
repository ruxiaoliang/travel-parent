package org.itcase.mapperExt;

import org.apache.ibatis.annotations.Update;

public interface RouteMapperExt {
    /**
     * @Description 修改线路统计
     * @param routeId 线路ID
     */
    @Update("update tab_route set attention_count = attention_count+1 where id = #{routeId}" )
    Long updateRouteCountById(Long routeId);
}
