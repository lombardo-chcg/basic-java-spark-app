package com.lombardo.courses.Model;

import java.util.ArrayList;
import java.util.List;

public class SimpleCourseIdeaDAO implements CourseIdeaDAO {

    private List<CourseIdea> ideas;

    public SimpleCourseIdeaDAO() {
        ideas = new ArrayList<>();
    }

    @Override
    public boolean add(CourseIdea idea) {
        return ideas.add(idea);
    }

    @Override
    public List<CourseIdea> findAll() {
        return new ArrayList<>(ideas);
    }

    public CourseIdea findBySlug(String slug) {
//        return ideas.stream()
//                .filter(idea -> idea.getSlug().equals(slug))
//                .findFirst()
//                .orElseThrow(NotFoundException::new);
        CourseIdea currentIdea = null;
        for (CourseIdea idea : findAll()) {
            System.out.printf("slug %s %n", slug);
            System.out.printf("idea.getslug %s %n", idea.getSlug());
            if (idea.getSlug().equals(slug)) {
                currentIdea = idea;
            }
        }
        return currentIdea;
    }
}
