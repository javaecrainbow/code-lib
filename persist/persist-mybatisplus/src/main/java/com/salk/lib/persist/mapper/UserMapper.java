package com.salk.lib.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.salk.lib.persist.UserPo;
import org.apache.ibatis.annotations.Select;

/**
 * 用户映射器
 *
 * @author salkli
 * @date 2022/10/11
 */
public interface UserMapper extends BaseMapper<UserPo> {
@Select("<script>select 1 from dual</script>")
    public int seletDeal();
}