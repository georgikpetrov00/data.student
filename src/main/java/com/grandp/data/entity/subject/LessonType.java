package com.grandp.data.entity.subject;

public enum LessonType {

  LECTURE("LECTURE"),
  LABORATORY("LABORATORY"),
  SEMINARY("SEMINARY");

  private String name;

  LessonType(String name) {
    this.name = name;
  }


}
