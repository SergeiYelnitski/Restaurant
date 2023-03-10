package com.example.restaurant.util;

import com.example.restaurant.service.exception.IllegalRequestDataException;

public class ValidationUtil {

  public static void assureIdConsistent(HasId bean, int id) {
    if (bean.isNew()) {
      bean.setId(id);
    } else if (bean.id() != id) {
      throw new IllegalRequestDataException(bean + " must be with id=" + id);
    }
  }

  public static void checkNew(HasId bean) {
    if (!bean.isNew()) {
      throw new IllegalRequestDataException(bean + " must be new (id=null)");
    }
  }
}
