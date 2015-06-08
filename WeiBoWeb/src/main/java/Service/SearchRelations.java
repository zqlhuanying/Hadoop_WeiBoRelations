package Service;

/**
 * Created by root on 15-5-18.
 */
public interface SearchRelations {

    // 根据名字查询粉丝列表，返回JSON格式，该格式可以直接应用于d3的Force图
    public String search(String name);
}
