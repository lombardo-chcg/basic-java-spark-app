package com.lombardo.courses;

import com.lombardo.courses.Model.CourseIdea;
import com.lombardo.courses.Model.CourseIdeaDAO;
import com.lombardo.courses.Model.NotFoundException;
import com.lombardo.courses.Model.SimpleCourseIdeaDAO;
import spark.ModelAndView;
import spark.Request;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        staticFileLocation("/public");

        CourseIdeaDAO dao = new SimpleCourseIdeaDAO();

        before((req, res) -> {
            if (req.cookie("username") != null) {
                req.attribute("username", req.cookie("username"));
            }
        });

        before("/ideas", (req, res) -> {
            if (req.cookie("username") == null) {
                SessionHelper.setFlashMessage(req, "Please log in!");
                res.redirect("/");
                halt();
            }
        });

        get("/", (req, res) -> {
            return new ModelAndView(SessionHelper.flashMessageHelper(req), "index.hbs");
        }, new HandlebarsTemplateEngine());

        post("/sign-in", (req, res) -> {
            Map<String, String> model = new HashMap<>();
            String username = req.queryParams("username");
            res.cookie("username", username);
            model.put("username", username);
            res.redirect("/");
            return null;
        });

        get("/ideas", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("ideas", dao.findAll());
            model.put("flashMessage", SessionHelper.captureFlashMessage(req));
            return new ModelAndView(model, "ideas.hbs");
        }, new HandlebarsTemplateEngine());

        get("/ideas/:slug", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            CourseIdea idea = dao.findBySlug(req.params("slug"));
            model.put("idea", idea);
            return new ModelAndView(model, "ideas-show.hbs");
        }, new HandlebarsTemplateEngine());

        post("/ideas", (req, res) -> {
            String title = req.queryParams("title");
            CourseIdea courseIdea = new CourseIdea(title, req.attribute("username"));
            dao.add(courseIdea);

            res.redirect("/ideas");
            return null;
        });

        post("/ideas/:slug/vote", (req, res) -> {
            CourseIdea idea = dao.findBySlug(req.params("slug"));
            boolean added = idea.addVoter(req.attribute("username"));
            if (added) {
                SessionHelper.setFlashMessage(req, "Thanks for your vote!");
            } else {
                SessionHelper.setFlashMessage(req, "You are only allowed one vote per course.");
            }
            res.redirect("/ideas");
            return null;
        });

        exception(NotFoundException.class, (exc, req, res) -> {
           res.status(404);
            HandlebarsTemplateEngine engine = new HandlebarsTemplateEngine();
            String html = engine.render(new ModelAndView(null, "notfound.hbs"));
            res.body(html);
        });
    }

}
