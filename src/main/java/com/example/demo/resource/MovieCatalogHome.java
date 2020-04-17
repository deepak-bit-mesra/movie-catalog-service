package com.example.demo.resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MovieCatalogHome {
	@RequestMapping("/")
	public ModelAndView ghar() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("a", 1);
		mv.addObject("b",2);
		mv.addObject("c","c");
		mv.setViewName("home.html");
		return mv;
	}

}
