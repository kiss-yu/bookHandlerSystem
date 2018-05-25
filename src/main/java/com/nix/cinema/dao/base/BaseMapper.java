package com.nix.cinema.dao.base;
import com.nix.cinema.model.base.BaseModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 11723 on 2017/5/4.
 */
@Repository
public interface BaseMapper<M extends BaseModel<M>>{
    void insert(M object);
    void delete(@Param("id") Integer id);
    void update(M model);
    M select(@Param("id") Integer id);
    Integer maxId(M m);
    Long count();
    List<M> findByOneField(@Param("field") String field,@Param("value") String value);
    List<M> list(@Param("offset") Integer offset, @Param("limit") Integer limit, @Param("order") String order, @Param("sort") String sort, @Param("conditions") String conditions);
}
