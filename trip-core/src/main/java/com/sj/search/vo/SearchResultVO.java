package com.sj.search.vo;


import com.sj.domain.Destination;
import com.sj.domain.Strategy;
import com.sj.domain.Travel;
import com.sj.domain.UserInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class SearchResultVO implements Serializable{


    private Long total = 0L;

    private List<Strategy> strategies = new ArrayList<>();
    private List<Travel> travels = new ArrayList<>();
    private List<UserInfo> users = new ArrayList<>();
    private List<Destination> dests = new ArrayList<>();

}
