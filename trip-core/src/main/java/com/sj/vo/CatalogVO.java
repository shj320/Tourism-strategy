package com.sj.vo;


import com.sj.domain.StrategyCatalog;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class CatalogVO {
    private String destName;
    private List<StrategyCatalog> catalogList = new ArrayList<>();
}
