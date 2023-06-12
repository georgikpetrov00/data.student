package com.grandp.data.entity.curriculum;

import java.util.List;

public class UpdateCurriculumRequest {
    private List<Long> subjectIdsToAdd;
    private List<Long> subjectIdsToRemove;

    public List<Long> getSubjectIdsToAdd() {
        return subjectIdsToAdd;
    }

    public void setSubjectIdsToAdd(List<Long> subjectIdsToAdd) {
        this.subjectIdsToAdd = subjectIdsToAdd;
    }

    public List<Long> getSubjectIdsToRemove() {
        return subjectIdsToRemove;
    }

    public void setSubjectIdsToRemove(List<Long> subjectIdsToRemove) {
        this.subjectIdsToRemove = subjectIdsToRemove;
    }
}