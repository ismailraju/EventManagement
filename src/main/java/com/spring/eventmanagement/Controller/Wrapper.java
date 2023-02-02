package com.spring.eventmanagement.Controller;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
class Wrapper<T> implements Serializable {
    private List<T> data;
    private int recordsTotal;
    private int pageLength;
    private int recordsFiltered;
    private int draw;

//      jsonResult.put("recordsTotal",recordsTotal);
//                jsonResult.put("recordsFiltered",recordsFiltered);
//                jsonResult.put("data",dataArray);
//                jsonResult.put("draw",draw);

    public Wrapper() {
    }

    public Wrapper(List<T> data, int recordsTotal, int pageLength, int recordsFiltered ,int draw) {
        this.data = data;
        this.recordsTotal = recordsTotal;
        this.pageLength = pageLength;
        this.recordsFiltered = recordsFiltered;
        this.draw = draw;
    }

}