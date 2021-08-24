package com.blog.controller.vo.params;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author: hu
 * @create: 2021-08-17 16:43
 */
@Data
public class PageParams {
  private int page = 1;
  private int pageSize = 10;
  private Long tagId;
  private Long categoryId;
  private String year;
  private String month;

  public String getMonth(){
    if(this.month != null && this.month.length() == 1){
      return "0" + this.month;
    }
    return this.month;
  }
}
