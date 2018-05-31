package com.nix.cinema.dao;

import com.nix.cinema.model.BorrowTime;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Kiss
 * @date 2018/05/30 22:05
 */
@Repository
public interface BorrowTimeMapper {
    List<BorrowTime> list(@Param("sql") String sql);
}
