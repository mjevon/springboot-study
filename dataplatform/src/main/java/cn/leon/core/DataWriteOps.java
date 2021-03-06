package cn.leon.core;

import cn.leon.domain.form.StorageForm;
import cn.leon.domain.form.StorageListForm;
import cn.leon.domain.form.UpdateIndexForm;

/**
 * @author mujian
 * @Desc
 * @date 2019/7/4 17:14
 */
public interface DataWriteOps {

    void saveData(StorageListForm form);

    int saveData(StorageForm form);

    int saveIndex(StorageForm form);

    String saveDetail(StorageForm form);

    int updateIndex(UpdateIndexForm form);
}
