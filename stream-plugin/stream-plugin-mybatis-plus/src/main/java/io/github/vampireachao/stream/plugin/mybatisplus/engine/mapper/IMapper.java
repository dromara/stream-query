package io.github.vampireachao.stream.plugin.mybatisplus.engine.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.vampireachao.stream.plugin.mybatisplus.engine.constant.PluginConst;
import org.apache.ibatis.annotations.Param;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * 拓展BaseMapper
 *
 * @author VampireAchao
 */
public interface IMapper<T> extends BaseMapper<T> {

    /**
     * 插入多条数据（mysql语法批量）
     *
     * @param list 数据
     * @return 条数
     */
    long insertOneSql(@Param(PluginConst.COLLECTION_PARAM_NAME) Collection<T> list);

    /**
     * 批量插入, 默认一千分批
     *
     * @param list 集合
     * @return 是否成功
     */
    default boolean batchInsert(Collection<T> list) {
        return this.batchInsert(list, 1000);
    }

    /**
     * 批量插入, 默认一千分批
     *
     * @param list      集合
     * @param batchSize 分割量
     * @return 是否成功
     */
    default boolean batchInsert(Collection<T> list, Integer batchSize) {
        for (List<T> ts : subList(list, batchSize)) {
            this.insertOneSql(ts);
        }
        return true;
    }

    /**
     * 将List进行一个分页
     *
     * @param coll 源List
     * @param size 一页最大的size（）
     * @param <T>  返回一个集合
     */
    static <T> Collection<List<T>> subList(Collection<T> coll, int size) {
        if (CollectionUtils.isEmpty(coll)) {
            return Collections.emptyList();
        }
        List<T> collList = new ArrayList<>(coll);
        size = Math.max(1, size);
        int total = coll.size();
        int page = total / size + 1;
        Collection<List<T>> results = new ArrayList<>(page);
        for (int i = 0; i < page; i++) {
            int fIndex = i * size;
            int tIndex = Math.min(i * size + size, total);
            List<T> list = collList.subList(fIndex, tIndex);
            if (!list.isEmpty()) {
                results.add(list);
            }
        }
        return results;
    }

}
