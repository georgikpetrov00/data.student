package com.grandp.data.entity.subject;

public enum LessonType {

  LECTURE("ЛЕКЦИЯ"),
  LABORATORY("ЛАБОРАТОРНО УПРАЖНЕНИЕ"),
  SEMINARY("СЕМИНАРНО УПРАЖНЕНИЕ");

  private String value;

  LessonType(String value) {
    this.value = value;
  }

  public static LessonType of(String val) {
    return switch (val.toUpperCase()) {
      case "ЛЕКЦИЯ" -> LECTURE;
      case "ЛАБОРАТОРНО УПРАЖНЕНИЕ" -> LABORATORY;
      case "СЕМИНАРНО УПРАЖНЕНИЕ" -> SEMINARY;
      default -> throw new RuntimeException("LessonType '" + val + "' does not exist.");
    };
  }

  public String getValue() {
    return value;
  }

}
