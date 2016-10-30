package com.lchli.angithub.common.constants;

/**
 * Created by lchli on 2016/10/30.
 */

public final class ServerConst {

    public interface Repo {
        String SORT_FIELD_STAR = "stars";
        String SORT_FIELD_FORKS = "forks";
        String SORT_FIELD_UPDATED = "updated";
        String SORT_FIELD_DEFAULT = "Default";
        String[] SORT_FIELDS = {SORT_FIELD_STAR, SORT_FIELD_FORKS, SORT_FIELD_UPDATED, SORT_FIELD_DEFAULT};

        String SORT_ORDER_ASC = "asc";
        String SORT_ORDER_DESC = "desc";
        String[] SORT_ORDERS = {SORT_ORDER_ASC, SORT_ORDER_DESC};

        String LANG_ALL = "All";
        String LANG_JAVA = "Java";
        String LANG_CPP = "C++";
        String LANG_C = "C";
        String LANG_PYTHON = "Python";
        String[] LANGS = {LANG_JAVA, LANG_CPP, LANG_C, LANG_PYTHON, LANG_ALL};

    }
}
